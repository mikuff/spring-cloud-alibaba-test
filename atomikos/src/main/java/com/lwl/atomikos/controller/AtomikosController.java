package com.lwl.atomikos.controller;

import com.lwl.atomikos.service.AtomikosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/atomikos/")
@RestController
public class AtomikosController {

    @Autowired
    private AtomikosService atomikosService;

    @GetMapping("/insert/one")
    public Integer insertOne() {
        atomikosService.insertOne();
        return 1;
    }

    @GetMapping("/insert/two")
    public Integer insertTwo() {
        atomikosService.insertTwo();
        return 1;
    }
}
