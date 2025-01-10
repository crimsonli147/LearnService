package com.example.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example.learn.*"})
public class LearnServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(LearnServiceApplication.class, args);
        System.out.println("hello");
    }
}