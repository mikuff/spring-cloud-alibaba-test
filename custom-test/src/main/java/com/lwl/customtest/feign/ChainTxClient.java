package com.lwl.customtest.feign;

import com.lwl.dto.ChainInfoDTO;
import com.lwl.feign.ChainTxFeign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "chain-tx",path = "/chain-tx")
public interface ChainTxClient extends ChainTxFeign {

    @Override
    @GetMapping(value = "/getInfoByDbOne")
    ChainInfoDTO getInfoByDbOne(@RequestParam Integer id);

    @Override
    @GetMapping(value = "/getInfoByDbTwo")
    ChainInfoDTO getInfoByDbTwo(@RequestParam Integer id);

    @Override
    @PostMapping(value = "/insertDbOne")
    Integer insertDbOne(@RequestBody ChainInfoDTO dto);

    @Override
    @PostMapping(value = "/insertDbTwo")
    Integer insertDbTwo(@RequestBody ChainInfoDTO dto);


    @Override
    @PostMapping(value = "/insertDbOneError")
    Integer insertDbOneError(@RequestBody ChainInfoDTO dto);

    @Override
    @PostMapping(value = "/insertDbTwoError")
    Integer insertDbTwoError(@RequestBody ChainInfoDTO dto);

    @Override
    @PostMapping(value = "/insertAllDbNormal")
    Integer insertAllDbNormal(ChainInfoDTO dto);

    @Override
    @PostMapping(value = "/insertAllDbError")
    Integer insertAllDbError(ChainInfoDTO dto);
}
