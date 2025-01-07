package com.example.login.controller;
import com.example.login.common.R;
import com.example.login.dao.User;
import com.example.login.service.UserService;
import com.example.login.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

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

    @RequestMapping("/alluser")
    public List<User> findAll() {
        return userService.findAll();
    }

    @PostMapping("/sendMsg")
    public R<String> sendMsg(User user, HttpSession session) {
        return userService.sendMsg(user, session);
    }

    @PostMapping("/smslogin")
    public R<User> login(@RequestParam Map map, HttpSession session) {
        return userService.login(map, session);
    }
}
