package com.lwl.mqdb.controller;

import com.lwl.dto.MqDbDTO;
import com.lwl.feign.MqDbFeign;
import com.lwl.mqdb.service.MqDbService;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.function.Consumer;

@RestController
@RequestMapping("/mq-db")
public class MqDbController implements MqDbFeign {

    @Autowired
    private MqDbService mqDbService;

    @Override
    @PostMapping(value = "/insertDb")
    public Integer insertDb(@RequestBody MqDbDTO dto) {
        return mqDbService.insertDb(dto);
    }

    @Override
    @GetMapping(value = "/sendMessage")
    public Integer sendMessage(@RequestParam("message") String message) {
        return mqDbService.sendMessage(message);
    }

    @Override
    @PostMapping(value = "/sendMessageAndInsertDb")
    public Integer sendMessageAndInsertDb(@RequestBody MqDbDTO dto) {
        return Try.of(() -> mqDbService.sendMessageAndInsertDb(dto))
                .onFailure(error -> System.out.println(error.getLocalizedMessage()))
                .recover(RuntimeException.class, -1)
                .get();
    }

}
