package com.drsg.gochat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author YXs
 */
@SpringBootApplication
@MapperScan("com.drsg.gochat.v1.mapper")
public class GoChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoChatApplication.class, args);
    }

}
