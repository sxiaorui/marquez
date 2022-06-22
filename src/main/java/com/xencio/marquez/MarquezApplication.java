package com.xencio.marquez;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xencio.marquez.mapper")
public class MarquezApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarquezApplication.class, args);
    }

}
