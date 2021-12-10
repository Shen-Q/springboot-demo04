package com.example.springbootdemo04.entity;

import lombok.Data;

import java.util.Date;

/**
 * 参加秒杀活动的物品，注意startDate的时间包含时区
 * version是用来实现乐观锁的，之后可以取消
 */
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
