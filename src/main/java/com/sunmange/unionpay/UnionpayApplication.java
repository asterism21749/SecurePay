package com.sunmange.unionpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class UnionpayApplication {

    public static void main(String[] args) {
        SpringApplication.run(UnionpayApplication.class, args);
    }

}
