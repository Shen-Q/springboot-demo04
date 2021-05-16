package com.example.springbootdemo04.mapper;

import com.example.springbootdemo04.entity.User;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    @Test
    void selectAll() {
        List<User> userList = userMapper.selectAll();
        for(User user:userList){
            System.out.println(user.toString());
        }
    }

    @Test
    void getById() {
        User user = userMapper.getById(1);
        System.out.println(user.toString());
    }

    @Test
    void update() {
        User user = new User();
        user.setId(Long.valueOf(1));
        user.setPassword("123123");
        int i =userMapper.update(user); //修改成功
        System.out.println(i);
    }
}