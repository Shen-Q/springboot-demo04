package com.example.springbootdemo04.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 订单详细信息
 */
@Data
@ToString
public class OrderInfo {

    private Long id;
    //订单用户 判断是否只秒杀一次
    private Long userId;
    //订单物品
    private Long goodsId;
    private String goodsName;
    private Integer goodsCount;
    private Double goodsPrice;
    private Date createDate;
}
