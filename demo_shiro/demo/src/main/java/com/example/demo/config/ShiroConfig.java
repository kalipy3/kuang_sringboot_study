package com.example.demo.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;

import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * ShiroConfig.java
 * Copyright (C) 2021 2021-03-22 17:01 kalipy <kalipy@debian>
 *
 * Distributed under terms of the MIT license.
 */
@Configuration
public class ShiroConfig
{
    //ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //设置安全管理器
        bean.setSecurityManager(defaultWebSecurityManager);

        //添加shiro的内置过滤器
        /*
         * anon: 无需认证就可以访问
         * authc: 必须认证才能访问
         * user: 必须拥有 记住我 功能才能用
         * perms: 拥有对某个资源的权限才能访问
         * role: 拥有某个角色权限才能访问
         */

        //拦截
        Map<String, String> filterMap = new LinkedHashMap<>();

        //授权,正常情况下，未授权会跳转到未授权页面
        filterMap.put("/user/add", "perms[user:add]");//这里只是设置权限，并没有把权限赋给用户，把权限赋给用户是在UserRealm类的授权方法中进行的
        filterMap.put("/user/update", "perms[user:update]");
        
        filterMap.put("/user/*", "authc");
        bean.setFilterChainDefinitionMap(filterMap);
        
        //设置登录页面的请求地址
        bean.setLoginUrl("/toLogin");

        //未授权页面
        bean.setUnauthorizedUrl("/noauth");

        return bean;
    }
    //DefaultWebSecurityManager
    @Bean(name="securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联UserRealm
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    //创建realm对象,需要自定义类
    @Bean
    public UserRealm userRealm() {
        return new UserRealm();
    }
}

