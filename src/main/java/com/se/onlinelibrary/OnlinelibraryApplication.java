package com.se.onlinelibrary;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.se.onlinelibrary.mapper")
@EnableTransactionManagement
public class OnlinelibraryApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlinelibraryApplication.class, args);
    }
}
