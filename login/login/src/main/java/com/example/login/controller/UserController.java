package com.example.login.controller;
import com.example.login.dao.User;
import com.example.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    // 登录
    @RequestMapping("/login")
    public String login(User user) {
        return userService.login(user);
    }

    // 注册
    @PostMapping("/register")
    public String register(User user) {
        return userService.register(user);
    }

    // 解决查询数据库中文出现乱码问题
    @RequestMapping( "/alluser")
    public List<User> findAll() {
        return userService.findAll();
    }
}
