package com.lwl.controller;

import com.alibaba.fastjson.JSON;
import com.lwl.dto.OrderInfoDTO;
import com.lwl.feign.OrderInfoFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/inner")
@Slf4j
public class OrderInfoController implements OrderInfoFeign {
    @GetMapping("/info")
    public OrderInfoDTO getInfoById(@RequestParam Integer id) {
        log.info("order-service,接收到消息: {}", id);
        return new OrderInfoDTO(id, "测试消息");
    }
}
