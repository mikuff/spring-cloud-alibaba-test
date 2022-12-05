package com.lwl.mqdb.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lwl.dto.MqDbDTO;
import com.lwl.mqdb.entity.MqDbInfoEntity;
import com.lwl.mqdb.mapper.MqDbInfoMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
public class MqDbServiceImpl implements MqDbService {

    @Autowired
    private MqDbInfoMapper mapper;


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public Integer insertDb(MqDbDTO dto) {
        MqDbInfoEntity entity = new MqDbInfoEntity();
        BeanUtils.copyProperties(dto, entity);
        return mapper.insert(entity);
    }

    @Override
    public Integer sendMessage(String message) {
        rabbitTemplate.send("mq_db_exchange", "mq_db", new Message(message.getBytes(), new MessageProperties()));
        return 1;
    }

    @Override
    @Transactional(transactionManager = "rabbitAndDbTransactionManager")
    public Integer sendMessageAndInsertDb(MqDbDTO dto) {
        MqDbInfoEntity entity = new MqDbInfoEntity();
        BeanUtils.copyProperties(dto, entity);
        mapper.insert(entity);
        rabbitTemplate.send("mq_db_exchange", "mq_db", new Message(dto.getMessage().getBytes(), new MessageProperties()));
        if (dto.getMessage().equals("error")) {
            throw new RuntimeException();
        }
        return 1;
    }
}
