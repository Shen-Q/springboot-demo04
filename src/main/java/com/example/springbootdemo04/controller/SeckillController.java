package com.example.springbootdemo04.controller;

import com.example.springbootdemo04.entity.OrderInfo;
import com.example.springbootdemo04.redis.GoodsKey;
import com.example.springbootdemo04.service.GoodsService;
import com.example.springbootdemo04.service.RedisService;
import com.example.springbootdemo04.service.SeckillService;
import com.example.springbootdemo04.vo.GoodsVO;
import com.example.springbootdemo04.vo.LoginVO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;

    @Autowired
    SeckillService seckillService;

    //做标记，判断是否重复下单
    private HashMap<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();

    //启动项目时就加载了！
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVO> goodsVoList = goodsService.listGoodsVo();
        if (goodsVoList == null) {
            return;
        }
        for (GoodsVO goods : goodsVoList) {
            redisService.set(GoodsKey.getGoodsStock, "" + goods.getId(), goods.getStockCount());
            //初始化商品都是没有处理过的
            localOverMap.put(goods.getId(), false);
        }
    }

    @PostMapping("/do_seckill")
    public String list(Model model, HttpSession session, @RequestParam("goodsId") Long goodsId) throws Exception{
      //  boolean over = localOverMap.get(goodsId);//看看是否已经买过
      //  if(over){
      //      return "";
      //  }
        GoodsVO goodsVO = goodsService.getGoodsVOByGoodsId(goodsId);
        LoginVO loginVO = (LoginVO) session.getAttribute("loginUser");
        OrderInfo orderInfo = seckillService.seckill(loginVO,goodsVO);
        if(orderInfo!=null) {
            model.addAttribute("goods",goodsVO);
            model.addAttribute("orderInfo",orderInfo);
            return "order_detail";
        }else{
            return "seckill_fail";
        }

    }

}
