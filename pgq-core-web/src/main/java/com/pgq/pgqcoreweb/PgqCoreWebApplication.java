package com.pgq.pgqcoreweb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@ComponentScan("com.pgq.**")
@MapperScan("com.pgq.**.dao")
public class PgqCoreWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(PgqCoreWebApplication.class, args);
    }

}
