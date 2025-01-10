package com.example.learn.adapter.cxf;

import com.example.learn.api.delegate.MyDelegate;
import com.example.learn.model.Wrapper;
import com.example.learn.service.service.MyService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MyCxfService implements MyDelegate {
    @Resource
    private MyService myService;

    @Override
    public Wrapper<String> getMessage() {
        String res = myService.getMessage();
        return Wrapper.<String>success().data(res);
    }
}
