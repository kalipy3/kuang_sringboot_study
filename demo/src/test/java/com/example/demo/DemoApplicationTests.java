package com.example.demo;

import javax.sql.DataSource;
import java.sql.Connection;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {
    @Autowired
    DataSource dataSource;

    @Test
    void contextLoads() throws Exception {
        //打印一下默认的数据源
        System.out.println(dataSource.getClass());
        //获取数据库连接
        Connection conn = dataSource.getConnection();
        System.out.println(conn);

        conn.close();
    }

}
