package com.example.springbootdemo04.controller;


import com.example.springbootdemo04.entity.User;
import com.example.springbootdemo04.service.GoodsService;
import com.example.springbootdemo04.vo.GoodsVO;
import com.example.springbootdemo04.vo.LoginVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/to_list")
    public String list(Model model, User user){
        List<GoodsVO> goodsList = goodsService.listGoodsVo();
 //       model.addAttribute("user", user);//好像没用上，也存不了吧？能存吗？
        model.addAttribute("goodsList", goodsList);
        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model, @PathVariable("goodsId") Integer goodsId, HttpSession session){

        //根据id查询商品详情
        GoodsVO goodsVO = goodsService.getGoodsVOByGoodsId(goodsId);
        model.addAttribute("goods", goodsVO);
        LoginVO loginUser = (LoginVO) session.getAttribute("loginUser");
        model.addAttribute("loginUser",loginUser);
        long startTime = goodsVO.getStartDate().getTime();
        long endTime = goodsVO.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int seckillStatus = 0;
        int remainSeconds = 0;

        if (now < startTime) {//秒杀还没开始，倒计时
            seckillStatus = 0;
            remainSeconds = (int) ((startTime - now) / 1000);
        } else if (now > endTime) {//秒杀已经结束
            seckillStatus = 2;
            remainSeconds = -1;
        } else {//秒杀进行中
            seckillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("seckillStatus", seckillStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        return "goods_detail";

    }

}
