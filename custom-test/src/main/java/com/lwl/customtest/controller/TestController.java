package com.lwl.customtest.controller;

import com.alibaba.fastjson.JSON;
import com.lwl.customtest.feign.OrderClient;
import com.lwl.dto.OrderInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/test")
public class TestController {

    @Autowired
    private OrderClient orderClient;

    @PostMapping("/to/order")
    public String toOrder(@RequestBody Map<String, Object> body) {
        OrderInfoDTO dto = orderClient.getInfoById((Integer) body.get("id"));
        return JSON.toJSONString(dto);
    }

}
