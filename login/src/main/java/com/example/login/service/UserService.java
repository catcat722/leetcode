package com.example.login.service;

import com.example.login.common.R;
import com.example.login.dao.User;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;


public interface UserService  {

    public String login(User user);
    public String register(User user);
    public List<User> findAll();
    public R<String> sendMsg(User user, HttpSession session);
    public R<User> login(Map map, HttpSession session);

}
