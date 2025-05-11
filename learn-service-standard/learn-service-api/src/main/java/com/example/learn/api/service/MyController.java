package com.example.learn.api.service;

import com.example.learn.api.delegate.MyDelegate;
import com.example.learn.model.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/demo")
public class MyController {
    @Autowired
    private MyDelegate delegate;

    @GetMapping("/message/query")
    public Wrapper<String> getMessage() {
        return delegate.getMessage();
    }
}