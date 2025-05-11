package com.example.learn.client.adapter;

import com.example.learn.client.feign.DemoFeignClient;
import com.example.learn.client.model.demo.DemoRequest;
import com.example.learn.client.model.demo.DemoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DemoAdapter {
    @Autowired
    private DemoFeignClient client;

    public DemoResponse querySomething(DemoRequest request) {
        return client.querySomething(request);
    }
}
