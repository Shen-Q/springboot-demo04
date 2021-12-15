package com.example.springbootdemo04.controller;

import com.example.springbootdemo04.entity.OrderInfo;
import com.example.springbootdemo04.entity.SeckillOrder;
import com.example.springbootdemo04.rabbitmq.MQConfig;
import com.example.springbootdemo04.rabbitmq.MQReceiver;
import com.example.springbootdemo04.rabbitmq.MQSender;
import com.example.springbootdemo04.redis.GoodsKey;
import com.example.springbootdemo04.service.GoodsService;
import com.example.springbootdemo04.service.OrderService;
import com.example.springbootdemo04.service.RedisService;
import com.example.springbootdemo04.service.SeckillService;
import com.example.springbootdemo04.vo.GoodsVO;
import com.example.springbootdemo04.vo.LoginVO;
import org.apache.tomcat.jni.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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

    private static Logger log = LoggerFactory.getLogger(SeckillController.class);

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;

    @Autowired
    SeckillService seckillService;

    @Autowired
    MQSender sender;

    @Autowired
    OrderService orderService;

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
        GoodsVO goodsVO = goodsService.getGoodsVOByGoodsId(goodsId);//这句是用来传给html页面的
//        LoginVO loginVO = (LoginVO) session.getAttribute("loginUser");
     //加入RabbitMQ后，这里不能直接调用秒杀了，要传数据给queue才行
        LoginVO loginVO = new LoginVO("18630957677","123456");
//         OrderInfo orderInfo = seckillService.seckill(loginVO,goodsVO);

        //传数据给queue信道
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(Long.valueOf(loginVO.getMobile()));
        seckillOrder.setGoodsId(goodsId);
        sender.sendSeckillMessage(seckillOrder);
      //不对啊 应该取到消息自动处理  receiver.receive(seckillOrder);
        int i=0;
        while(i<5) {
//            wait(1000);
            Time.sleep(1000);
            OrderInfo orderInfo = orderService.getOrderInfo(seckillOrder.getUserId(), goodsId);
//            Long count = orderService.getOrderCount(seckillOrder.getUserId());
            if (orderInfo != null) {
                log.info("第"+i+"次时取数据成功，跳转。");
                model.addAttribute("goods", goodsVO);
                model.addAttribute("orderInfo", orderInfo);
                return "order_detail";
            }
            i++;
        }
        return "seckill_fail";
    }

}
