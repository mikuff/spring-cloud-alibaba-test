package com.lwl.customtest.feign;

import com.lwl.dto.OrderInfoDTO;
import com.lwl.feign.OrderInfoFeign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Component
@FeignClient(value = "order-service",path = "/inner")
public interface OrderClient extends OrderInfoFeign {

    @GetMapping(value = "/info")
    OrderInfoDTO getInfoById(@RequestParam Integer id);

}
