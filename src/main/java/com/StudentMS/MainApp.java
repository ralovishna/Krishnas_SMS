package com.StudentMS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@SpringBootApplication(scanBasePackages = {"com.StudentMS"})
@EnableScheduling
@ComponentScan({"com.StudentMS.controller", "com.StudentMS.config", "com.StudentMS.serviceImpl", "com.StudentMS.service", "com.StudentMS.repository"})
public class MainApp {
    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }
}