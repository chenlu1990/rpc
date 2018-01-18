package com.example.demo;

/**
 * Created by chenlu on 2018/1/18.
 */
public class HelloServiceImpl implements HelloService{
    @Override
    public String sayHi(String name) {
        return "Hi, " + name;
    }
}
