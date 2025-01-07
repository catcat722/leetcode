package com.example.login.service.impl;
import com.example.login.common.R;
import com.example.login.dao.User;
import com.example.login.mapper.UserMapper;
import com.example.login.service.UserService;
import com.example.login.util.SmsUtil;
import com.example.login.util.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserServiceImpl  implements UserService {
    @Autowired
    UserMapper userMapper;
    @Resource
    RedisTemplate redisTemplate;

    @Override
    public String login(User user) {
        try {
            User userExistN = userMapper.findByName(user.getUsername());
            if (userExistN!= null) {
                String userExistP = userMapper.findPswByName(user.getUsername());
                if (userExistP.equals(user.getPassword())) {
                    return user.getUsername()+"登录成功，欢迎您！";
                }else{
                    return "登录失败，密码错误！";
                }
            }else {
                return "登录失败，用户不存在!";
            }
        }catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
    @Override
    public String register(User user) {
        try {
            User userExist = userMapper.findByName(user.getUsername());
            if (user.getUsername().equals("")) {
                return "用户名不能为空";
            }else if (user.getPassword().equals("")) {
                return "密码不能为空";
            }else if (userExist != null) {
                return "账号已经存在";
            }else {
                userMapper.save(user);
                return "注册成功";
            }
        }catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
    // 获取所有用户
    @Override
    public List<User> findAll() {
        List<User> list = userMapper.findAll();
        return list;
    }

    @Override
    public R<String> sendMsg(User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)){
            //生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);
            //调用阿里云提供的短信服务API完成发送短信
            SmsUtil.sendMessage("阿里云短信测试","SMS_154950909",phone,code);

            //需要将生成的验证码保存到Session
//            session.setAttribute(phone,code);
//            session.setAttribute("phone",phone);
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            redisTemplate.opsForValue().set("phone",phone,5, TimeUnit.MINUTES);



            return R.success("手机验证码短信发送成功");
        }
        return R.error("短信发送失败");

    }

    @Override
    public R<User> login(@RequestParam Map map, HttpSession session) {
        log.info(map.toString());
        //获取验证码
        String code = map.get("code").toString();
        //获取手机号
        String phone =(String)redisTemplate.opsForValue().get("phone");
//        //从Session中获取保存的验证码
        String codeInSession = (String)redisTemplate.opsForValue().get(phone);

        //进行验证码的比对（页面提交的验证码和Session中保存的验证码比对）
        if(codeInSession != null && codeInSession.equals(code)){
            //如果能够比对成功，说明登录成功
            User user = userMapper.findByPhone(phone);
            if(user == null){
                //判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
                user = new User();
                user.setPhone(phone);
                user.setUsername(null);
                user.setPassword(null);
                userMapper.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("登录失败");
    }

}

