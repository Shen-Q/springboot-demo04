package com.example.springbootdemo04.mapper;


import com.example.springbootdemo04.entity.OrderInfo;
import com.example.springbootdemo04.entity.SeckillOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper {

    int insertOrderInfo(OrderInfo orderInfo);

//    int insertOrder(SeckillOrder seckillOrder);

    SeckillOrder getOrderByUserIdGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

    OrderInfo getOrderInfo(@Param("userId") long userId, @Param("goodsId") long goodsId);
}
