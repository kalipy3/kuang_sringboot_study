package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.AsyncService;
/*
 * AsyncController.java
 * Copyright (C) 2021 2021-03-23 19:47 kalipy <kalipy@debian>
 *
 * Distributed under terms of the MIT license.
 */
@RestController
public class AsyncController
{
    @Autowired
    AsyncService asyncService;

    @RequestMapping
    public String hello() {
        asyncService.hello();//前端页面会加载3秒
        return "ok";
    }
}

