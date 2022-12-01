package com.lwl.chaintx.controller;

import com.lwl.chaintx.service.ChainTxInfoService;
import com.lwl.dto.ChainInfoDTO;
import com.lwl.feign.ChainTxFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chain-tx")
@Slf4j
public class ChainController implements ChainTxFeign {

    @Autowired
    private ChainTxInfoService chainTxInfoService;

    @Override
    @GetMapping("/getInfoByDbOne")
    public ChainInfoDTO getInfoByDbOne(@RequestParam Integer id) {
        return chainTxInfoService.getInfoByDbOne(id);
    }

    @Override
    @GetMapping("/getInfoByDbTwo")
    public ChainInfoDTO getInfoByDbTwo(@RequestParam Integer id) {
        return chainTxInfoService.getInfoByDbTwo(id);
    }

    @Override
    @PostMapping("/insertDbOne")
    public Integer insertDbOne(@RequestBody ChainInfoDTO dto) {
        return chainTxInfoService.insertDbOne(dto);
    }

    @Override
    @PostMapping("/insertDbTwo")
    public Integer insertDbTwo(@RequestBody ChainInfoDTO dto) {
        return chainTxInfoService.insertDbTwo(dto);
    }

    @Override
    @PostMapping("/insertDbOneError")
    public Integer insertDbOneError(@RequestBody ChainInfoDTO dto) {
        try {
            return chainTxInfoService.insertDbOneError(dto);
        } catch (Exception e) {
            log.error("insertDbOneError executor error: {}", e.getLocalizedMessage());
        }
        return -1;
    }

    @Override
    @PostMapping("/insertDbTwoError")
    public Integer insertDbTwoError(@RequestBody ChainInfoDTO dto) {
        try {
            return chainTxInfoService.insertDbTwoError(dto);
        } catch (Exception e) {
            log.error("insertDbTwoError executor error: {}", e.getLocalizedMessage());
        }
        return -1;
    }

    @Override
    @PostMapping("/insertAllDbNormal")
    public Integer insertAllDbNormal(@RequestBody ChainInfoDTO dto) {
        try {
            return chainTxInfoService.insertAllDbNormal(dto);
        } catch (Exception e) {
            log.error("insertAllDbNormal executor error: {}", e.getLocalizedMessage());
        }
        return -1;
    }

    @Override
    @PostMapping("/insertAllDbError")
    public Integer insertAllDbError(@RequestBody ChainInfoDTO dto) {
        try {
            return chainTxInfoService.insertAllDbError(dto);
        } catch (Exception e) {
            log.error("insertAllDbError executor error: {}", e.getLocalizedMessage());
        }
        return -1;
    }

}
