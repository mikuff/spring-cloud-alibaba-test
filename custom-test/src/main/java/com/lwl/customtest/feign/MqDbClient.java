package com.lwl.customtest.feign;

import com.lwl.dto.MqDbDTO;
import com.lwl.feign.MqDbFeign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "mq-db", path = "/mq-db")
public interface MqDbClient extends MqDbFeign {

    @Override
    @PostMapping(value = "/insertDb")
    Integer insertDb(@RequestBody MqDbDTO dto);

    @Override
    @GetMapping("/sendMessage")
    Integer sendMessage(@RequestParam("message") String message);

    @Override
    @PostMapping(value = "/sendMessageAndInsertDb")
    Integer sendMessageAndInsertDb(@RequestBody MqDbDTO dto);

}
