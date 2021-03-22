package com.example.demo.config;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.pojo.User;
import com.example.demo.service.UserService;

/*
 * UserRealm.java
 * Copyright (C) 2021 2021-03-22 17:03 kalipy <kalipy@debian>
 *
 * Distributed under terms of the MIT license.
 */

//自定义的UserRealm
public class UserRealm extends AuthorizingRealm
{
    @Autowired
    UserService userService;

    //授权
    @Override 
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals)
    {
        System.out.println("执行了授权方法..");

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //拿到当前登录的对象
        Subject subject = SecurityUtils.getSubject();
        User currentUser = (User) subject.getPrincipal();//拿到user对象

        //设置当前用户的权限
        info.addStringPermission(currentUser.getPerms());

        return info;
    }

    //认证
    @Override 
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行了认证方法..");

        UsernamePasswordToken userToken = (UsernamePasswordToken) token;

        //查询数据库
        User user = userService.queryUserByName(userToken.getUsername());

        if (user == null) {
            return null;//会自动抛出异常(UnknownAccountException)
        }

        //密码认证,shiro自动做了
        return new SimpleAuthenticationInfo(user, user.getPwd(), "");//第一个参数(principal)的含义是把这里的uesr传递给上面的授权方法
    }
}


