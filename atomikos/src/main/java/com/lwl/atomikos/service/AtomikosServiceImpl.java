package com.lwl.atomikos.service;

import com.lwl.atomikos.entity.UserEntity;
import com.lwl.atomikos.mapper.dsone.AtomikosDsoneMapper;
import com.lwl.atomikos.mapper.dstwo.AtomikosDstwoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class AtomikosServiceImpl implements AtomikosService {

    @Autowired
    private AtomikosDsoneMapper atomikosDsoneMapper;

    @Autowired
    private AtomikosDstwoMapper atomikosDstwoMapper;

    @Override
    public void insertOne() {
        UserEntity user = new UserEntity();
        user.setUsername("ds-one-" + UUID.randomUUID().toString());
        user.setAddress("测试-one");
        user.setCreateDate(new Date());
        atomikosDsoneMapper.insert(user);
    }

    @Override
    public void insertTwo() {
        UserEntity user = new UserEntity();
        user.setUsername("ds-two-" + UUID.randomUUID().toString());
        user.setAddress("测试-two");
        user.setCreateDate(new Date());
        atomikosDsoneMapper.insert(user);
    }
}
