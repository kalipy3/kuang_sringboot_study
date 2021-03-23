package com.example.demo.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo());
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
