package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/*
 * JDBCController.java
 * Copyright (C) 2021 2021-03-15 18:25 kalipy <kalipy@debian>
 *
 * Distributed under terms of the MIT license.
 */

@RestController
public class JDBCController
{
   @Autowired
   JdbcTemplate jdbcTemplate;

   //查询数据库所有信息
   //没有实体类，数据库中的东西，怎么获取? Map
   @GetMapping("/userList")
   public List<Map<String, Object>> userList() {
        String sql = "select * from user";
        List<Map<String, Object>> list_maps = jdbcTemplate.queryForList(sql);
        return list_maps;
   }

   @GetMapping("/addUser")
   public String addUser() {
       String sql = "insert into mybatis.user(id, name, pwd) values (4, '小明', '123456')";
       jdbcTemplate.update(sql);
       return "update-ok";
   }
   
   @GetMapping("/updateUser/{id}")
   public String updateUser(@PathVariable("id") int id) {
       String sql = "update mybatis.user set name=?,pwd=? where id=" + id;
       //封装
       Object[] objects = new Object[2];
       objects[0] = "小明2";
       objects[1] = "123456";
       jdbcTemplate.update(sql, objects);
       return "updateUser-ok";
   }
   
   @GetMapping("/deleteUser/{id}")
   public String deleteUser(@PathVariable("id") int id) {
       String sql = "delete from mybatis.user where id = ?";
       jdbcTemplate.update(sql, id);
       return "deleteUser-ok";
   }
}

