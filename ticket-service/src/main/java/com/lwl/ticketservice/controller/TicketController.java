package com.lwl.ticketservice.controller;

import com.lwl.enums.ResponseEnum;
import com.lwl.exception.BussniessException;
import com.lwl.result.Result;
import com.lwl.ticketservice.dto.OrderDTO;
import com.lwl.ticketservice.entity.TicketEntity;
import com.lwl.ticketservice.service.TicketService;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Consumer;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/lock")
    public Result lock(@RequestBody OrderDTO orderDTO) {
        TicketEntity lock = ticketService.lock(orderDTO);
        return Result.success(lock);
    }

    @PostMapping("/lock2")
    public Result lock2(@RequestBody OrderDTO orderDTO) {
        // 获取分布式锁，在外层获取分布式锁的原因是，
        // 如果mybatis开启了缓存，mybatis很有可能对sql语句进行优化，修改批量提交到数据库中，修改没有及时刷新到数据库中 如果在里面加锁，会出现没有锁住的情况，
        // 如果在外层加锁，大部分锁表都是不允许的，可以锁行

//        Lock lock = new distributedLock();3
//        if(lock.tryLock()){
//            TicketEntity lock = ticketService.lock(orderDTO);
//        }
        TicketEntity lock = ticketService.lock(orderDTO);
        return Result.success(lock);
    }

}
