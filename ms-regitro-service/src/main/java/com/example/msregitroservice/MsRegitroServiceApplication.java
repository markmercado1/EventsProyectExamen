package com.example.msregitroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients

@SpringBootApplication
public class MsRegitroServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsRegitroServiceApplication.class, args);
    }

}
