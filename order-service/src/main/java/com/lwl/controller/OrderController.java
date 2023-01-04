package com.lwl.controller;

import com.lwl.enums.ResponseEnum;
import com.lwl.dto.OrderCreateDTO;
import com.lwl.service.OrderService;
import com.lwl.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public Result create(@RequestBody OrderCreateDTO orderCreateDTO) {
        try {
            orderService.createOrder(orderCreateDTO);
            return Result.fail(ResponseEnum.OK);
        } catch (Exception e) {
            log.error("创建订单失败:{}", e.getLocalizedMessage());
            return Result.fail(ResponseEnum.CREATE_ORDER_FAIL);
        }
    }
}
