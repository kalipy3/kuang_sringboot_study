package com.example.demo.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.pojo.User;
/*
 * HelloController.java
 * Copyright (C) 2021 2021-03-23 19:07 kalipy <kalipy@debian>
 *
 * Distributed under terms of the MIT license.
 */

@RestController
public class HelloController
{
    @GetMapping(value = "/hello")
    public String hello() {
        return "hello";
    }
   
    //只要我们的接口中，返回值中存在实体类，他就会被扫描到swagger中
    @PostMapping(value = "/user")
    public User user() {
        return new User();
    }

    @ApiOperation("hello2...")
    @GetMapping(value = "/hello2")
    public String hello2(@ApiParam("用户名") String username) {
        return "hello2" + username;
    }
    
    @ApiOperation("Post...")
    @PostMapping(value = "/post2")
    public User post2(@ApiParam("用户名") User user) {
        return user;
    }
}

