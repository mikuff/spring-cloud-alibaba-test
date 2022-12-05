package com.lwl.customtest.controller;


import com.lwl.customtest.feign.MqDbClient;
import com.lwl.dto.MqDbDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/mq-db")
public class MqDbController {

    @Autowired
    private MqDbClient mqDbClient;


    @PostMapping(value = "/insertDb")
    Integer insertDb(@RequestBody MqDbDTO dto) {
        return mqDbClient.insertDb(dto);
    }

    @GetMapping("/sendMessage")
    Integer sendMessage(@RequestParam("message") String message) {
        return mqDbClient.sendMessage(message);
    }

    @PostMapping(value = "/sendMessageAndInsertDb")
    Integer sendMessageAndInsertDb(@RequestBody MqDbDTO dto) {
        return mqDbClient.sendMessageAndInsertDb(dto);
    }
}
