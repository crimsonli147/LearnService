package com.example.learn.service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MyService {
    public String getMessage() {
        log.info("okokok");
        return "hello, u get a String!";
    }
}
