package com.example.learn.client.feign;

import com.example.learn.client.model.demo.DemoRequest;
import com.example.learn.client.model.demo.DemoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "demo-service")
public interface DemoFeignClient {
    @PostMapping("/demo-service/v1/demo/query")
    DemoResponse querySomething(@RequestBody DemoRequest request);
}