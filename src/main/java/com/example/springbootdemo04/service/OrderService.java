package com.example.springbootdemo04.service;


import com.example.springbootdemo04.entity.OrderInfo;
import com.example.springbootdemo04.entity.SeckillOrder;
import com.example.springbootdemo04.mapper.OrderMapper;
import com.example.springbootdemo04.vo.GoodsVO;
import com.example.springbootdemo04.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OrderService {

    @Autowired
    OrderMapper orderMapper;


    public OrderInfo createOrder(LoginVO loginVO, GoodsVO goodsVO) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
     //   orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goodsVO.getId());
        orderInfo.setGoodsName(goodsVO.getGoodsName());
        orderInfo.setGoodsPrice(goodsVO.getGoodsPrice());
     //   orderInfo.setOrderChannel(1);
     //   orderInfo.setStatus(0);
        orderInfo.setUserId(Long.valueOf(loginVO.getMobile()));
        orderMapper.insertOrderInfo(orderInfo);

        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(Long.valueOf(loginVO.getMobile()));
        seckillOrder.setGoodsId(goodsVO.getId());
        seckillOrder.setOrderId(orderInfo.getId());
        orderMapper.insertOrder(seckillOrder);
//
//        redisService.set(OrderKey.getSeckillOrderByUidGid, "" + user.getId() + "_" + goodsVO.getId(), seckillOrder);

        return orderInfo;
    }
}
