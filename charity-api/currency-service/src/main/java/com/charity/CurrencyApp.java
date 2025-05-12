package com.charity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class CurrencyApp {
    public static void main(String[] args) {
        SpringApplication.run(CurrencyApp.class, args);
    }
}

