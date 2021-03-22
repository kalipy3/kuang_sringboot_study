package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.example.demo.mapper.UserMapper;

import com.example.demo.pojo.User;

/*
 * UserServiceImpl.java
 * Copyright (C) 2021 2021-03-22 19:46 kalipy <kalipy@debian>
 *
 * Distributed under terms of the MIT license.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override 
    public User queryUserByName(String name) {
        return userMapper.queryUserByName(name);    
    }
}

