package com.lwl.ticketservice.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lwl.ticketservice.entity.TicketEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TicketMapperTest {


    @Autowired
    private TicketMapper ticketMapper;

    @Test
    public void testQuery() {
        List<TicketEntity> list = ticketMapper.selectList(new QueryWrapper<TicketEntity>());
        assert list.isEmpty();
    }


    @Test
    public void testInsert() {
        TicketEntity entity = new TicketEntity();
        entity.setName("测试");
        entity.setOwner(0L);
        entity.setTicketNum(3L);
        int insert = ticketMapper.insert(entity);
        assert insert == 1;

    }
}