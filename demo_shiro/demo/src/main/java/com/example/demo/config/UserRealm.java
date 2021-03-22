package com.example.demo.config;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/*
 * UserRealm.java
 * Copyright (C) 2021 2021-03-22 17:03 kalipy <kalipy@debian>
 *
 * Distributed under terms of the MIT license.
 */

//自定义的UserRealm
public class UserRealm extends AuthorizingRealm
{
    //授权
    @Override 
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals)
    {
        System.out.println("执行了授权方法..");
        return null;
    }

    //认证
    @Override 
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行了认证方法..");
        return null;
    }
}


