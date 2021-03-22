package com.example.demo.controller;

import org.apache.catalina.security.SecurityUtil;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

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
    @RequestMapping("/login") 
    public String login(String username, String password, Model model) {
        //获取当前用户
        Subject subject = SecurityUtils.getSubject();
        //封装用户登录的数据
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            subject.login(token);//执行登录方法,会把用户信息提交到doGetAuthtication认证方法中校验用户信息
            return "index";
        } catch (UnknownAccountException e) {
            model.addAttribute("msg", "用户名错误");
            return "login";
        } catch (IncorrectCredentialsException e) {
            model.addAttribute("msg", "密码错误");
            return "login";
        }
    }
}

