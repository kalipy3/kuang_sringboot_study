package com.example.demo.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
 * User.java
 * Copyright (C) 2021 2021-03-23 19:11 kalipy <kalipy@debian>
 *
 * Distributed under terms of the MIT license.
 */
@ApiModel("用户实体类")//给生产的文档加注释
public class User
{
    @ApiModelProperty("用户名")//给生产的文档加注释
    private String username;
   
    @ApiModelProperty("密码")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

