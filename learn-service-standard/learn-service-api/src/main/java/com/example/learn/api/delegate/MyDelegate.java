package com.example.learn.api.delegate;

import com.example.learn.model.Wrapper;
import org.springframework.stereotype.Component;

@Component
public interface MyDelegate {
    Wrapper<String> getMessage();
}
