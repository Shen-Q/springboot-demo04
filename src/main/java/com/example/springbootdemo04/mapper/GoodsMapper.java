package com.example.springbootdemo04.mapper;


import com.example.springbootdemo04.entity.Goods;
import com.example.springbootdemo04.entity.SeckillGoods;
import com.example.springbootdemo04.vo.GoodsVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsMapper {

    List<GoodsVO> listGoodsVO();

    GoodsVO getGoodsVOByGoodsId(long goodsId);

    int getVersionByGoodsId(long goodsId);

    int reduceStockByVersion(SeckillGoods seckillGoods);
}
