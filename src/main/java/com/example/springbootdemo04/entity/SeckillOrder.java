package com.example.springbootdemo04.entity;

import lombok.Data;

@Data
public class SeckillOrder {

    private Long id;
    private Long userId;
    private Long orderId;
    private Long goodsId;
}
