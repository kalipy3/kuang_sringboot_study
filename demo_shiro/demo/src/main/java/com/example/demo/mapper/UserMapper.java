package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

import org.springframework.stereotype.Repository;
import com.example.demo.pojo.User;

/*
 * UserMapper.java
 * Copyright (C) 2021 2021-03-22 19:34 kalipy <kalipy@debian>
 *
 * Distributed under terms of the MIT license.
 */

@Repository 
@Mapper
public interface UserMapper
{
    public User queryUserByName(String name);
}

