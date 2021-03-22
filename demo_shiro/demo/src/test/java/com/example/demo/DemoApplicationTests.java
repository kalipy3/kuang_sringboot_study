package com.example.demo;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import com.example.demo.service.UserServiceImpl;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    UserServiceImpl userService;

	@Test
	void contextLoads() {
        System.out.println(userService.queryUserByName("admin"));
	}

}
