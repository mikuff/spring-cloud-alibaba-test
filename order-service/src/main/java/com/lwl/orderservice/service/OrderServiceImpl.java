package com.lwl.orderservice.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lwl.Constant;
import com.lwl.dto.OrderContextDTO;
import com.lwl.enums.OrderCreateMessageEnum;
import com.lwl.enums.OrderStatusEnum;
import com.lwl.orderservice.dto.OrderCreateDTO;
import com.lwl.orderservice.entity.OrderEntity;
import com.lwl.orderservice.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public List<OrderEntity> findListByConsumerId(Long consumerId) {
        return orderMapper.selectList(new QueryWrapper<OrderEntity>().eq("consumer_id", consumerId));
    }

    @Override
    public Long findOneByUuid(String uuid) {
        return orderMapper.selectCount(new QueryWrapper<OrderEntity>().eq("uuid", uuid));
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    @Transactional(transactionManager = "rabbitTransactionManager")
    public void createOrder(OrderCreateDTO orderCreateDTO) {
        orderCreateDTO.setUuid(UUID.randomUUID().toString());
        // 提交锁票消息
        String message = JSON.toJSONString(orderCreateDTO);
        MessageProperties properties = new MessageProperties();
        properties.setContentType(Constant.APPLICATION_JSON);
        rabbitTemplate.send(Constant.CREATE_ORDER_EXCHANGE, Constant.ORDER_NEW_ROUTING_KEY, new Message(message.getBytes(StandardCharsets.UTF_8), properties));
    }

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(exchange = @Exchange(value = Constant.CREATE_ORDER_EXCHANGE), value = @Queue(value = Constant.ORDER_LOCKED_QUEUE, durable = "true"), key = Constant.ORDER_LOCKED_ROUTING_KEY))
    @Transactional(transactionManager = "rabbitAndDbTransactionManager")
    public void handleOrderCreate(OrderContextDTO orderContextDTO) {
        log.info("创建订单消息:{}", orderContextDTO);

        // 检查是否处理过
        if (findOneByUuid(orderContextDTO.getUuid()) > 1) {
            log.error("创建订单操作已经处理,订单uuid:{}", orderContextDTO.getUuid());
        } else {
            // 入库订单表
            OrderEntity orderEntity = crateOrder(orderContextDTO);
            orderMapper.insert(orderEntity);

            // 设置订单ID
            orderContextDTO.setOrderId(orderEntity.getId());
        }

        // 设置消息状态
        orderContextDTO.setStatus(OrderCreateMessageEnum.ORDER_NEW.getCode());

        // 发送消息到待缴费队列
        String message = JSON.toJSONString(orderContextDTO);
        MessageProperties properties = new MessageProperties();
        properties.setContentType(Constant.APPLICATION_JSON);
        rabbitTemplate.send(Constant.CREATE_ORDER_EXCHANGE, Constant.ORDER_PAY_ROUTING_KEY, new Message(message.getBytes(StandardCharsets.UTF_8), properties));
    }


    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(exchange = @Exchange(value = Constant.CREATE_ORDER_EXCHANGE), value = @Queue(value = Constant.ORDER_FINISH_QUEUE, durable = "true"), key = Constant.ORDER_FINISH_ROUTING_KEY))
    @Transactional(transactionManager = "rabbitAndDbTransactionManager")
    public void handleOrderFinish(OrderContextDTO orderContextDTO) {
        log.info("结束订单消息:{}", orderContextDTO);

        OrderEntity orderEntity = orderMapper.selectById(orderContextDTO.getOrderId());
        orderEntity.setStatus(OrderStatusEnum.SUCCESS.getCode());
        orderMapper.updateById(orderEntity);
    }

    private OrderEntity crateOrder(OrderContextDTO orderDTO) {
        OrderEntity ret = new OrderEntity();
        ret.setUuid(orderDTO.getUuid());
        ret.setAmount(orderDTO.getAmount());
        ret.setTitle(orderDTO.getTitle());
        ret.setConsumerId(orderDTO.getConsumerId());
        ret.setTicketNum(orderDTO.getTicketNum());
        ret.setStatus(OrderStatusEnum.CREATE.getCode());
        ret.setCreateDate(new Date());
        return ret;
    }

}
