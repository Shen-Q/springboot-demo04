package com.example.springbootdemo04.mapper;

import com.example.springbootdemo04.entity.OrderInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderMapperTest {

    @Autowired
    OrderMapper orderMapper;

    @Test
    void insertOrderInfo() {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId((long)12345666);
        orderInfo.setGoodsId((long)1);
        orderInfo.setGoodsName("苹果");
        orderInfo.setCreateDate(new Date());
        orderInfo.setGoodsPrice(1234.00);
        orderInfo.setGoodsCount(1);
        int i = orderMapper.insertOrderInfo(orderInfo);
        System.out.println(i);
    }

    @Test
    void insertOrder() {
    }
}