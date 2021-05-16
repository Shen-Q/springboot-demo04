package com.example.springbootdemo04.service;

import com.example.springbootdemo04.entity.User;
import com.example.springbootdemo04.redis.BasePrefix;
import com.example.springbootdemo04.redis.UserKey;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class RedisServiceTest {

    @Autowired
    RedisService redisService;

    @Test
    void set() {

        redisService.set(UserKey.getById,"2","abcdefg");
    }

    @Test
    void get() {
        BasePrefix basePrefix = UserKey.getById;
        User res = redisService.get(basePrefix,"18181818", User.class);
        System.out.println(res.getNickname());
    }
}