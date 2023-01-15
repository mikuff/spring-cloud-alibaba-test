package com.lwl.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lwl.Constant;
import com.lwl.anno.RabbitExecutorErrorRouting;
import com.lwl.dto.OrderContextDTO;
import com.lwl.entity.ConsumerEntity;
import com.lwl.enums.OrderCreateMessageEnum;
import com.lwl.enums.PayInfoStatusEnum;
import com.lwl.mapper.ConsumerMapper;
import com.lwl.entity.PayInfoEntity;
import com.lwl.mapper.PayInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class ConsumerServiceImpl implements ConsumerService {


    @Autowired
    private ConsumerMapper consumerMapper;

    @Autowired
    private PayInfoMapper payInfoMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(exchange = @Exchange(value = Constant.CREATE_ORDER_EXCHANGE), value = @Queue(value = Constant.ORDER_PAY_QUEUE, durable = "true"), key = Constant.ORDER_PAY_ROUTING_KEY))
    @Transactional(transactionManager = "rabbitAndDbTransactionManager")
    @RabbitExecutorErrorRouting(exchange = Constant.EXEC_ERROR_EXCHANGE, routingKey = Constant.EXEC_ERROR_QUEUE, desc = "订单支付操作")
    public void handleOrderPay(OrderContextDTO orderContextDTO) {
        log.info("支付消息:{}", orderContextDTO);

        // 检查是否是重复消息，避免出现重复消息导致余额不足的情况
        boolean isRepeat = checkRepeatMessageByOrderId(orderContextDTO.getOrderId());
        if (isRepeat) {
            log.warn("支付操作已经处理,订单ID:{}", orderContextDTO.getOrderId());
        } else {
            // 检查余额是否充足
            boolean isEnough = checkDepositByConsumerId(orderContextDTO.getConsumerId(), orderContextDTO.getAmount());
            if (!isEnough) {
                log.warn("支付操作余额不足,订单ID:{}", orderContextDTO.getOrderId());

                // 如果余额不足，则订单失败，释放票
                orderContextDTO.setStatus(OrderCreateMessageEnum.AMOUNT_NOT_ENOUGH.getCode());
                sendMessage(orderContextDTO, Constant.CREATE_ORDER_EXCHANGE, Constant.ORDER_FAIL_ROUTING_KEY);
                return;
            }

            // 存储支付记录
            PayInfoEntity payInfoEntity = new PayInfoEntity();
            payInfoEntity.setOrderId(orderContextDTO.getOrderId());
            payInfoEntity.setAmount(orderContextDTO.getAmount());
            payInfoEntity.setStatus(PayInfoStatusEnum.PAID.getCode());
            payInfoMapper.insert(payInfoEntity);

            // 执行扣费操作
            consumerMapper.charge(orderContextDTO.getConsumerId(), orderContextDTO.getAmount());
        }

        // 无论有没有支付过都发送到交票队列
        orderContextDTO.setStatus(OrderCreateMessageEnum.ORDER_PAID.getCode());
        sendMessage(orderContextDTO, Constant.CREATE_ORDER_EXCHANGE, Constant.ORDER_TICKET_MOVE_ROUTING_KEY);
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


    private boolean checkDepositByConsumerId(Long consumerId, Long amount) {
        ConsumerEntity entity = consumerMapper.selectOne(new QueryWrapper<ConsumerEntity>().eq("id", consumerId));
        return entity.getDeposit() > amount;
    }

    private boolean checkRepeatMessageByOrderId(Long orderId) {
        Long count = payInfoMapper.selectCount(new QueryWrapper<PayInfoEntity>().eq("order_id", orderId));
        return count > 1L;
    }
}
