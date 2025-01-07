package com.example.login;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.annotation.WebFilter;

@RestController
public class TestController {
    @GetMapping("/hello")
    public String hello(){
        return "hello ";
    }
}
