package com.example.demo.service;


import com.example.demo.pojo.User;
/*
 * UserService.java
 * Copyright (C) 2021 2021-03-22 19:43 kalipy <kalipy@debian>
 *
 * Distributed under terms of the MIT license.
 */

public interface UserService
{
    public User queryUserByName(String name); 
}

