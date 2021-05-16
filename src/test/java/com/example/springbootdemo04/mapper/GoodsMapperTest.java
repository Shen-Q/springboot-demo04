package com.example.springbootdemo04.mapper;

import com.example.springbootdemo04.vo.GoodsVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GoodsMapperTest {

    @Autowired
    GoodsMapper goodsMapper;

    @Test
    void listGoodsVO() {
        List<GoodsVO> goodsVOList= goodsMapper.listGoodsVO();
        for(GoodsVO goodsVO:goodsVOList){
            System.out.println(goodsVO);
        }

    }
}