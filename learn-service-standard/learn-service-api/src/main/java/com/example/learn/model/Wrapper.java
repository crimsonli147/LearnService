package com.example.learn.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class Wrapper<T> {
    private int code;
    private String message;
    private T data;
    private long timestamp;

    private Wrapper() {
        this.timestamp = Instant.now().toEpochMilli();  // 初始化时自动设置当前时间戳
    }

    // 成功响应
    public static <T> Wrapper<T> success() {
        Wrapper<T> wrapper = new Wrapper<>();
        wrapper.setCode(200);  // 200表示成功
        wrapper.setMessage("Success");
        return wrapper;
    }

    // 失败响应
    public static <T> Wrapper<T> fail() {
        Wrapper<T> wrapper = new Wrapper<>();
        wrapper.setCode(500);  // 500表示失败
        return wrapper;
    }

    // 设置状态码
    public Wrapper<T> code(int code) {
        this.setCode(code);
        return this;
    }

    // 设置消息
    public Wrapper<T> message(String message) {
        this.setMessage(message);
        return this;
    }

    // 设置数据
    public Wrapper<T> data(T data) {
        this.setData(data);
        return this;
    }

}


