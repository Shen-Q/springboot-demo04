package com.example.springbootdemo04.entity;

import lombok.Data;

import java.util.Date;

@Data
public class SeckillGoods {
    //id和goodsId一样
    private Long id;
    private Long goodsId;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
    private int version;
}
