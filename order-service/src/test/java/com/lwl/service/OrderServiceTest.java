package com.lwl.service;


import com.lwl.dto.OrderCreateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void createOrder() {
        OrderCreateDTO orderDTO = new OrderCreateDTO();
        orderService.createOrder(orderDTO);
    }
}