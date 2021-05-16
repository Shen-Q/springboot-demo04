package com.example.springbootdemo04.vo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class GoodsVO {
    //GoodsVO可以继承Goods的属性，但是这里不选择继承了
    private String goodsName;
    private String goodsImg;
    private Double goodsPrice;
    private Double seckillPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
    private Long id;
    private Integer version; //版本号，用来实现乐观锁
}
