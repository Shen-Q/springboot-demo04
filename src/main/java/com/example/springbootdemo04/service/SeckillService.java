package com.example.springbootdemo04.service;

import com.example.springbootdemo04.entity.OrderInfo;
import com.example.springbootdemo04.vo.GoodsVO;
import com.example.springbootdemo04.vo.LoginVO;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeckillService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;


    //保证这三个操作，减库存 下订单 写入秒杀订单是一个事物
    @Transactional
    public OrderInfo seckill(LoginVO loginVO, GoodsVO goodsVO) {
        boolean success = goodsService.reduceStock(goodsVO);
        if (success) {
            //下订单 写入秒杀订单
            OrderInfo orderInfo = orderService.createOrder(loginVO, goodsVO);
            return orderInfo;
        } else {
            //      setGoodsOver(goodsVO.getId());
            return new OrderInfo();

        }
    }
}
