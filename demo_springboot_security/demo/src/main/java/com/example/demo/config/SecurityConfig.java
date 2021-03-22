package com.example.demo.config;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/*
 * SecurityConfig.java
 * Copyright (C) 2021 2021-03-22 13:15 kalipy <kalipy@debian>
 *
 * Distributed under terms of the MIT license.
 */

//AOP拦截器
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter 
{
    //授权
    @Override
    public void configure(HttpSecurity http) throws Exception {
        //首页所有人可以访问，功能页只有对应权限的人才能访问
        //请求授权的规则
        http.authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/level1/**").hasRole("vip1")
            .antMatchers("/level2/**").hasRole("vip2")
            .antMatchers("/level3/**").hasRole("vip3");

        //没有权限默认到登录页面
        http.formLogin();

        //注销，开启了注销功能，跳到首页
        http.logout().logoutSuccessUrl("/");

        //开启记住我功能 cookie，默认保存两周
        http.rememberMe();
    }

    //认证, springboot 2.1.x可以直接使用
    //在spring security 5.0+ 需要对密码加密，不然会报错
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())//这些数据通常应该从数据库里得到，这里为了方便我们直接从内存里面放入假数据
            .withUser("hanser").password(new BCryptPasswordEncoder().encode("123456")).roles("vip2", "vip3")
            .and()
            .withUser("root").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1", "vip2", "vip3")
            .and()
            .withUser("yousa").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1");
    }
}

