package com.lwl.customtest.controller;

import com.lwl.customtest.feign.ChainTxClient;
import com.lwl.dto.ChainInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@Slf4j
@RequestMapping("/chain-tx")
public class ChainController {

    @Autowired
    private ChainTxClient chainTxClient;

    /**
     * 数据源One查找
     */
    @GetMapping("/getInfoByDbOne")
    public ChainInfoDTO getInfoByDbOne(@RequestParam Integer id) {
        return chainTxClient.getInfoByDbOne(id);
    }

    /**
     * 数据源Two查找
     */
    @GetMapping("/getInfoByDbTwo")
    public ChainInfoDTO getInfoByDbTwo(@RequestParam Integer id) {
        return  chainTxClient.getInfoByDbTwo(id);
    }

    /**
     * 数据源One插入
     */
    @PostMapping("/insertDbOne")
    public Integer insertDbOne(@RequestBody ChainInfoDTO dto) {
        return chainTxClient.insertDbOne(dto);
    }

    /**
     * 数据源Two插入
     */
    @PostMapping("/insertDbTwo")
    public Integer insertDbTwo(@RequestBody ChainInfoDTO dto) {
        return chainTxClient.insertDbTwo(dto);
    }

    /**
     * 添加事务注解,数据源One插入报错
     */
    @PostMapping("/insertDbOneError")
    public Integer insertDbOneError(@RequestBody ChainInfoDTO dto) {
        return chainTxClient.insertDbOneError(dto);
    }

    /**
     * 添加事务注解,数据源Two插入报错
     */
    @PostMapping("/insertDbTwoError")
    public Integer insertDbTwoError(@RequestBody ChainInfoDTO dto) {
        return chainTxClient.insertDbTwoError(dto);
    }

    /**
     * 添加事务注解,数据源One，数据源Two同时插入
     */
    @PostMapping("/insertAllDbNormal")
    public Integer insertAllDbNormal(@RequestBody ChainInfoDTO dto) {
        return chainTxClient.insertAllDbNormal(dto);
    }

    /**
     * 添加事务注解,数据源One，数据源Two同时插入报错
     */
    @PostMapping("/insertAllDbError")
    public Integer insertAllDbError(@RequestBody ChainInfoDTO dto) {
        return chainTxClient.insertAllDbError(dto);
    }

}
