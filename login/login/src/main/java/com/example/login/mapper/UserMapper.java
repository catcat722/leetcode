package com.example.login.mapper;
import com.example.login.dao.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

// 将类标识为bean
@Repository
@Mapper
public interface UserMapper {
    List<User> findAll();
    User findByName(String username);
    String findPswByName(String userName);
    void save(User user);
}