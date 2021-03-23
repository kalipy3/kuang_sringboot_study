package com.example.demo.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;

import springfox.documentation.spi.DocumentationType;

import springfox.documentation.spring.web.plugins.Docket;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/*
 * SwaggerConfig.java
 * Copyright (C) 2021 2021-03-23 12:44 kalipy <kalipy@debian>
 *
 * Distributed under terms of the MIT license.
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    //配置swagger的Docket的bean实例
    @Bean
    public Docket docket(Environment environment) {

        //设置swagger的环境
        Profiles profiles = Profiles.of("dev", "test");
        //通过environment.acceptsProfiles判断是否处在自己设定的环境当中
        boolean isEnableSwagger = environment.acceptsProfiles(profiles);

        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .enable(isEnableSwagger)//enable是否启用swagger,如果为false,则swagger不能在浏览器里访问到
            .select()
            //RequestHandlerSelectors 配置要扫描接口的方式
            //basePackage:指定要扫描的包
            //any():扫描全部
            //none():不扫描
            //withClassAnnotation: 扫描类上的注解，参数是一个注解的反射对象
            //withMethodAnnotation: 扫描方法上的注解
            .apis(RequestHandlerSelectors.basePackage("com.example.demo.controller"))
            //paths()排除掉(不扫描)一些路径
            //.paths(PathSelectors.ant("/demo/**"))
            .build();
    }

    //配置swagger信息apiInfo
    private ApiInfo apiInfo() {
        //作者信息
        Contact contact = new Contact("kalipy", "www.kalipy3.com", "3069087972@qq.com");

        return new ApiInfo(
                    "kalipy的swaggerAPI文档",
                    "这是描述信息",
                    "v1.0",
                    "https://www.bilibili.com",
                    contact,
                    "Apache 2.0",
                    "http://www.apache.org/licenses/LICENSE-2.0",
                    new ArrayList()
                );
    }

}
