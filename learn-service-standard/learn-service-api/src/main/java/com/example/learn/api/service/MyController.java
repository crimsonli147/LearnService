package com.example.learn.api.service;

import com.example.learn.api.delegate.MyDelegate;
import com.example.learn.model.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/learnService")
public class MyController {
    @Autowired(required = false)
    private MyDelegate delegate;

    @GetMapping("/get/message")
    public Wrapper<String> getMessage() {
        return delegate.getMessage();
    }
}