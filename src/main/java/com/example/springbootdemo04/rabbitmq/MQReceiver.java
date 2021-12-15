package com.example.springbootdemo04.rabbitmq;


import com.alibaba.fastjson.JSON;
import com.example.springbootdemo04.entity.OrderInfo;
import com.example.springbootdemo04.entity.SeckillOrder;
import com.example.springbootdemo04.service.GoodsService;
import com.example.springbootdemo04.service.OrderService;
import com.example.springbootdemo04.service.SeckillService;
import com.example.springbootdemo04.vo.GoodsVO;
import com.example.springbootdemo04.vo.LoginVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    //这边是从queue取消息
    @RabbitListener(queues=MQConfig.QUEUE)
    public void receive(String message){
        //先写进log
        log.info("receive message:"+message);
        //将获得的message(字符串)转换成Bean
        SeckillOrder seckillOrder = stringToBean(message, SeckillOrder.class);
        //完全不用order_id？
        long userId = seckillOrder.getUserId();
        long goodsId = seckillOrder.getGoodsId();

        GoodsVO goodsVO = goodsService.getGoodsVOByGoodsId(goodsId);
        int stock = goodsVO.getStockCount();
        if(stock <= 0){
            return;
        }

        //判断重复秒杀，这俩是联合主键，如果不判断直接写会报错导致回滚。
//        SeckillOrder order = orderService.getOrderByUserIdGoodsId(userId,goodsId);
//        if(order != null) {
//            return;
//        }
        LoginVO loginVO = new LoginVO();
        loginVO.setMobile(String.valueOf(userId));
        //减库存 下订单 写入秒杀订单
        seckillService.seckill(loginVO, goodsVO);
    }

    //只是写入log？？这有啥用
    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void receiveTopic1(String message) {
        log.info(" topic  queue1 message:" + message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void receiveTopic2(String message) {
        log.info(" topic  queue2 message:" + message);
    }

    public <T> T stringToBean(String str, Class<T> clazz) {
        if (str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }
}
