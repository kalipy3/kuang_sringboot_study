package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;

/*
 * MyController.java
 * Copyright (C) 2021 2021-03-22 17:24 kalipy <kalipy@debian>
 *
 * Distributed under terms of the MIT license.
 */
@Controller
public class MyController
{
    @RequestMapping({"/", "/index"}) 
    public String toIndex(Model model) {
        model.addAttribute("msg", "hello Shiro");
        return "index";
    }
    @RequestMapping("/user/add") 
    public String add() {
        return "user/add";
    }
    @RequestMapping("/user/update") 
    public String update() {
        return "user/update";
    }
    @RequestMapping("/toLogin") 
    public String toLogin() {
        return "login";
    }
}

