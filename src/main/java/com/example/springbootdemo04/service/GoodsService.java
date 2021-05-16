package com.example.springbootdemo04.service;

import com.example.springbootdemo04.entity.SeckillGoods;
import com.example.springbootdemo04.mapper.GoodsMapper;
import com.example.springbootdemo04.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    GoodsMapper goodsMapper;

    //乐观锁的冲突尝试次数
    private static final int DEFAULT_MAX_RETRIES = 5;

    /**
     * 查询商品列表
     *
     * @return
     */
    public List<GoodsVO> listGoodsVo() {
        return goodsMapper.listGoodsVO();
    }

    public GoodsVO getGoodsVOByGoodsId(long goodsId) { return goodsMapper.getGoodsVOByGoodsId(goodsId);
    }

    //reduceStock方法整个操作包含在事务里，然而事务只能保证隔离性、以及要完成都完成，不能解决并发问题
    public boolean reduceStock(GoodsVO goodsVO) {
        int numAttempts = 0;
        int ret = 0;
        SeckillGoods seckillGoods = new SeckillGoods();
        seckillGoods.setGoodsId(goodsVO.getId());
        //获取版本号
        seckillGoods.setVersion(goodsVO.getVersion());
        do {
            numAttempts++;
            try {
                //再次获得最新的版本号
                int version = goodsMapper.getVersionByGoodsId(goodsVO.getId());
                seckillGoods.setVersion(version);
                //获得最新的库存，根据id和版本号查库存。如果版本号不够新，那么没办法查到，继续下一次循环。
                ret = goodsMapper.reduceStockByVersion(seckillGoods);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (ret != 0)
                //如果查到了！
                break;

        } while (numAttempts < DEFAULT_MAX_RETRIES);

        return ret > 0;
    }
}
