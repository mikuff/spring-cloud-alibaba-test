package com.lwl.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lwl.Constant;
import com.lwl.anno.RabbitExecutorErrorRouting;
import com.lwl.dto.OrderContextDTO;
import com.lwl.dto.OrderCreateDTO;
import com.lwl.entity.OrderEntity;
import com.lwl.enums.OrderCreateMessageEnum;
import com.lwl.enums.OrderStatusEnum;
import com.lwl.mapper.OrderMapper;
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
    @RabbitExecutorErrorRouting(exchange = Constant.EXEC_ERROR_EXCHANGE, routingKey = Constant.EXEC_ERROR_QUEUE, desc = "提交索票操作")
    public void createOrder(OrderCreateDTO orderCreateDTO) {
        orderCreateDTO.setUuid(UUID.randomUUID().toString());
        // 提交锁票消息
        String message = JSON.toJSONString(orderCreateDTO);
        sendMessage(message, Constant.CREATE_ORDER_EXCHANGE, Constant.ORDER_NEW_ROUTING_KEY);
    }

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(exchange = @Exchange(value = Constant.CREATE_ORDER_EXCHANGE), value = @Queue(value = Constant.ORDER_LOCKED_QUEUE, durable = "true"), key = Constant.ORDER_LOCKED_ROUTING_KEY))
    @Transactional(transactionManager = "rabbitAndDbTransactionManager")
    @RabbitExecutorErrorRouting(exchange = Constant.EXEC_ERROR_EXCHANGE, routingKey = Constant.EXEC_ERROR_QUEUE, desc = "创建订单操作")
    public void handleOrderCreate(OrderContextDTO orderContextDTO) {
        log.info("创建订单消息:{}", orderContextDTO);

        // 检查是否处理过
        if (findOneByUuid(orderContextDTO.getUuid()) > 1) {
            log.error("创建订单操作已经处理,订单uuid:{}", orderContextDTO.getUuid());
        } else {
            // 入库订单表
            OrderEntity orderEntity = crateOrderEntity(orderContextDTO);
            orderMapper.insert(orderEntity);

            // 设置订单ID
            orderContextDTO.setOrderId(orderEntity.getId());
        }

        // 设置消息状态
        orderContextDTO.setStatus(OrderCreateMessageEnum.ORDER_NEW.getCode());

        // 发送消息到待缴费队列
        sendMessage(orderContextDTO, Constant.CREATE_ORDER_EXCHANGE, Constant.ORDER_PAY_ROUTING_KEY);
    }


    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(exchange = @Exchange(value = Constant.CREATE_ORDER_EXCHANGE), value = @Queue(value = Constant.ORDER_FINISH_QUEUE, durable = "true"), key = Constant.ORDER_FINISH_ROUTING_KEY))
    @Transactional(transactionManager = "rabbitAndDbTransactionManager")
    @RabbitExecutorErrorRouting(exchange = Constant.EXEC_ERROR_EXCHANGE, routingKey = Constant.EXEC_ERROR_QUEUE, desc = "正常订单结束操作")
    public void handleOrderFinish(OrderContextDTO orderContextDTO) {
        // 正常消息
        log.info("正常结束订单消息:{}", orderContextDTO);
        OrderEntity orderEntity = orderMapper.selectById(orderContextDTO.getOrderId());
        orderEntity.setStatus(OrderStatusEnum.SUCCESS.getCode());
        orderMapper.updateById(orderEntity);
        return;
    }

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(exchange = @Exchange(value = Constant.CREATE_ORDER_EXCHANGE), value = @Queue(value = Constant.ORDER_FAIL_QUEUE, durable = "true"), key = Constant.ORDER_FAIL_ROUTING_KEY))
    @Transactional(transactionManager = "rabbitAndDbTransactionManager")
    @RabbitExecutorErrorRouting(exchange = Constant.EXEC_ERROR_EXCHANGE, routingKey = Constant.EXEC_ERROR_QUEUE, desc = "异常订单处理操作")
    public void handleOrderFail(OrderContextDTO orderContextDTO) {
        // 异常结束订单消息
        log.info("异常结束订单消息:{}", orderContextDTO);

        if (OrderCreateMessageEnum.TICKET_LOCKED_FAIL.getCode().equals(orderContextDTO.getStatus())) {
            // 索票失败,回滚索票
            log.info("索票失败消息:{}", orderContextDTO);
            OrderEntity orderEntity = crateOrderEntity(orderContextDTO);
            orderEntity.setStatus(OrderStatusEnum.ERROR.getCode());
            orderEntity.setReason(String.valueOf(orderContextDTO.getStatus()));
            orderMapper.insert(orderEntity);

            // 回滚索票
            sendMessage(orderContextDTO, Constant.CREATE_ORDER_EXCHANGE, Constant.ORDER_UNLOCK_ROUTING_KEY);
            return;
        }

        if (OrderCreateMessageEnum.AMOUNT_NOT_ENOUGH.getCode().equals(orderContextDTO.getStatus())) {
            // 余额不足，订单失败，回滚索票
            log.info("索票失败消息:{}", orderContextDTO);
            OrderEntity orderEntity = crateOrderEntity(orderContextDTO);
            orderEntity.setId(orderContextDTO.getOrderId());
            orderEntity.setStatus(OrderStatusEnum.ERROR.getCode());
            orderEntity.setReason(String.valueOf(orderContextDTO.getStatus()));
            orderMapper.updateById(orderEntity);

            // 回滚索票
            sendMessage(orderContextDTO, Constant.CREATE_ORDER_EXCHANGE, Constant.ORDER_UNLOCK_ROUTING_KEY);
            return;
        }

        // 最终异常,无法进行处理
        log.error("无法处理异常消息:{}", orderContextDTO);
    }

    private OrderEntity crateOrderEntity(OrderContextDTO orderDTO) {
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

    /**
     * @Author lwl
     * @Description 发送消息
     * @Param orderContextDTO 消息对象
     * @Param exchange 交换机名称
     * @Param routingKey 路由key
     * @Return void
     * @Date 2023/1/15 13:09
     * @Version 1.0
     */
    public void sendMessage(OrderContextDTO orderContextDTO, String exchange, String routingKey) {
        String message = JSON.toJSONString(orderContextDTO);
        MessageProperties properties = new MessageProperties();
        properties.setContentType(Constant.APPLICATION_JSON);
        rabbitTemplate.send(exchange, routingKey, new Message(message.getBytes(StandardCharsets.UTF_8), properties));
    }


    /**
     * @Author lwl
     * @Description 发送消息
     * @Param message: 消息内容
     * @Param exchange: 交换机名称
     * @Param routingKey: 路由key
     * @Return void
     * @Date 2023/1/15 13:12
     * @Version 1.0
     */
    public void sendMessage(String message, String exchange, String routingKey) {
        MessageProperties properties = new MessageProperties();
        properties.setContentType(Constant.APPLICATION_JSON);
        rabbitTemplate.send(exchange, routingKey, new Message(message.getBytes(StandardCharsets.UTF_8), properties));
    }

}
