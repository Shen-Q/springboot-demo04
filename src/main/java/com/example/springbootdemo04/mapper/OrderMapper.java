package com.example.springbootdemo04.mapper;


import com.example.springbootdemo04.entity.OrderInfo;
import com.example.springbootdemo04.entity.SeckillOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    int insertOrderInfo(OrderInfo orderInfo);

    int insertOrder(SeckillOrder seckillOrder);
}
