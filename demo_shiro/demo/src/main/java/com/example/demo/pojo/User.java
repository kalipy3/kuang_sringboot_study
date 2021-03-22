package com.example.demo.pojo;

/*
 * User.java
 * Copyright (C) 2021 2021-03-22 18:55 kalipy <kalipy@debian>
 *
 * Distributed under terms of the MIT license.
 */

public class User
{
    private int id;
    private String name;
    private String pwd;
    private String perms;

    public User() {
    }

    public User(int id, String name, String pwd, String perms) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
        this.perms = perms;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    @Override
    public String toString() {
        return "User{" +
            "id = " + getId() +
            ", name = " + getName() +
            ", pwd = " + getPwd() +
            ", perms = " + getPerms() +
            "}";
    }

}
