package com.lwl.atomikos.service;

import com.lwl.atomikos.entity.UserEntity;
import com.lwl.atomikos.mapper.dsone.AtomikosDsoneMapper;
import com.lwl.atomikos.mapper.dstwo.AtomikosDstwoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
public class AtomikosServiceImpl implements AtomikosService {

    @Autowired
    private AtomikosDsoneMapper atomikosDsoneMapper;

    @Autowired
    private AtomikosDstwoMapper atomikosDstwoMapper;

    @Override
    @Transactional
    public void insertOne() {
        UserEntity user = new UserEntity();
        user.setUsername("ds-one-" + UUID.randomUUID().toString());
        user.setAddress("测试-one");
        user.setCreateDate(new Date());
        atomikosDsoneMapper.insert(user);
    }

    @Override
    @Transactional
    public void insertTwo() {
        UserEntity user = new UserEntity();
        user.setUsername("ds-two-" + UUID.randomUUID().toString());
        user.setAddress("测试-two");
        user.setCreateDate(new Date());
        atomikosDstwoMapper.insert(user);
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void insertAll() {
        UserEntity user = new UserEntity();
        user.setUsername("ds-one-" + UUID.randomUUID().toString());
        user.setAddress("测试-one");
        user.setCreateDate(new Date());
        atomikosDsoneMapper.insert(user);

        UserEntity userTwo = new UserEntity();
        userTwo.setUsername("ds-two-" + UUID.randomUUID().toString());
        userTwo.setAddress("测试-two");
        userTwo.setCreateDate(new Date());
        atomikosDstwoMapper.insert(userTwo);

        int i = Math.abs(new Random().nextInt(10));
        if (i % 2 == 0) {
            log.error("抛出异常了");
            throw new RuntimeException("抛出异常");
        }

    }
}
