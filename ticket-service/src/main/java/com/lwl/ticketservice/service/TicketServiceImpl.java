package com.lwl.ticketservice.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lwl.Constant;
import com.lwl.dto.OrderContextDTO;
import com.lwl.enums.OrderCreateMessageEnum;
import com.lwl.enums.ResponseEnum;
import com.lwl.exception.BussniessException;
import com.lwl.ticketservice.dto.OrderDTO;
import com.lwl.ticketservice.entity.TicketEntity;
import com.lwl.ticketservice.mapper.TicketMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(exchange = @Exchange(value = Constant.CREATE_ORDER_EXCHANGE), value = @Queue(value = Constant.ORDER_NEW_QUEUE, durable = "true"), key = Constant.ORDER_NEW_ROUTING_KEY))
    @Transactional(transactionManager = "rabbitAndDbTransactionManager")
    public void handleTicketLock(OrderContextDTO orderContextDTO) {
        log.info("锁票消息:{}", orderContextDTO);

        // 锁票
        Integer lock = ticketMapper.lockTicket(orderContextDTO.getConsumerId(), orderContextDTO.getTicketNum());
        if (lock == 1) {
            // 锁票成功
            // 更新状态为锁票成功
            orderContextDTO.setStatus(OrderCreateMessageEnum.TICKET_LOCKED.getCode());

            // 发送消息到创建订单队列
            sendMessage(orderContextDTO, Constant.ORDER_LOCKED_ROUTING_KEY);
            return;
        }

        // 发送到 order_finished
        orderContextDTO.setStatus(OrderCreateMessageEnum.TICKET_LOCKED_FAIL.getCode());
        sendMessage(orderContextDTO, Constant.ORDER_FINISH_ROUTING_KEY);
    }

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(exchange = @Exchange(value = Constant.CREATE_ORDER_EXCHANGE), value = @Queue(value = Constant.ORDER_UNLOCK_QUEUE, durable = "true"), key = Constant.ORDER_UNLOCK_ROUTING_KEY))
    @Transactional(transactionManager = "rabbitAndDbTransactionManager")
    public void handleTicketUnlock(OrderContextDTO orderContextDTO) {
        log.info("解锁票消息:{}", orderContextDTO);
        // 锁票
        Integer lock = ticketMapper.unlockTicket(orderContextDTO.getConsumerId(), orderContextDTO.getTicketNum());
        if (lock != 1) {
            log.error("解锁票失败, ticketNum: {}", orderContextDTO.getTicketNum());
        }
    }

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(exchange = @Exchange(value = Constant.CREATE_ORDER_EXCHANGE), value = @Queue(value = Constant.ORDER_TICKET_MOVE_QUEUE, durable = "true"), key = Constant.ORDER_TICKET_MOVE_ROUTING_KEY))
    @Transactional(transactionManager = "rabbitAndDbTransactionManager")
    public void handleTicketMove(OrderContextDTO orderContextDTO) {
        log.info("交票消息:{}", orderContextDTO);
        int count = ticketMapper.moveTicket(orderContextDTO.getConsumerId(), orderContextDTO.getTicketNum());
        if (count == 0) {
            log.warn("交票操作已经处理,交票信息:{}", orderContextDTO);
        }
        orderContextDTO.setStatus(OrderCreateMessageEnum.TICKET_MOVED.getCode());

        // 发送消息到订单结束队列
        sendMessage(orderContextDTO, Constant.ORDER_FINISH_ROUTING_KEY);
    }


    @Override
    public TicketEntity lock(OrderDTO orderDTO) {
        // 正常写法
        try {
            // 业务流程
            Integer lock = ticketMapper.lockTicket(orderDTO.getConsumerId(), orderDTO.getTicketNum());
            if (lock < 1) {
                throw new BussniessException(ResponseEnum.NOT_FIND_TICKET);
            }
            return selectByTicketNum(orderDTO.getTicketNum());
        } catch (BussniessException e) {
            // 当抛出 SQLException 异常时
            log.error("执行索票操作失败,错误信息: {}", e.getLocalizedMessage());
            throw new BussniessException(ResponseEnum.FAIL);
        } catch (RuntimeException e) {
            // 当抛出业务异常时
            log.error("无法根据票号查询到具体的票据, 票据号(ticketNum): {}", orderDTO.getTicketNum());
            throw e;
        }
    }


    private TicketEntity selectByTicketNum(Long ticketNum) {
        return ticketMapper.selectOne(new QueryWrapper<TicketEntity>().eq("ticket_num", ticketNum));
    }


    private void sendMessage(OrderContextDTO orderContextDTO, String routingKey) {
        MessageProperties properties = new MessageProperties();
        properties.setContentType(Constant.APPLICATION_JSON);

        String messageBody = JSON.toJSONString(orderContextDTO);
        Message message = new Message(messageBody.getBytes(), properties);
        rabbitTemplate.send(Constant.CREATE_ORDER_EXCHANGE, routingKey, message);
    }

}
