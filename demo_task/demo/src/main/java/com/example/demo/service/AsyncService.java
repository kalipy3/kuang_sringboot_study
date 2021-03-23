package com.example.demo.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/*
 * AsyncService.java
 * Copyright (C) 2021 2021-03-23 19:44 kalipy <kalipy@debian>
 *
 * Distributed under terms of the MIT license.
 */

@Service
public class AsyncService
{
    @Async//告诉spring这是一个异步的方法
    public void hello() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("数据正在处理..");
    }
}

