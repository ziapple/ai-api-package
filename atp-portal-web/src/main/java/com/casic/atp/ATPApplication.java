package com.casic.atp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class ATPApplication {
    public static void main(String[] args) {
        SpringApplication.run(ATPApplication.class, args);
    }
}

