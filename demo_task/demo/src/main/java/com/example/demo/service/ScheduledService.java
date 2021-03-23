package com.example.demo.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/*
 * ScheduledService.java
 * Copyright (C) 2021 2021-03-23 20:58 kalipy <kalipy@debian>
 *
 * Distributed under terms of the MIT license.
 */
@Service
public class ScheduledService
{
    //秒 分 时 日 月 周几
    @Scheduled(cron = "11 8 21 * * ?")//每天21点8分11秒执行一次
    public void hello() {
        System.out.println("ScheduledService hello被执行了..");
    }
}

