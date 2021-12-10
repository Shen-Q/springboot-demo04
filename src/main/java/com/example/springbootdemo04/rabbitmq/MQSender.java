package com.example.springbootdemo04.rabbitmq;


import com.alibaba.fastjson.JSON;
import com.example.springbootdemo04.entity.SeckillOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {

    private static Logger log = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    AmqpTemplate amqpTemplate;


    public void sendTopic(Object message){
        String msg = JSON.toJSONString(message);
        log.info("send topic message:"+msg);
        //向交换机发送消息，这个交换机存在的意义就是……测试，而已，完全没用上
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key1", msg+"1");
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key2", msg+"2");
    }


    //发送秒杀订单，注意信息直接发给了Queue频道，然后另一端从Queue取
    public void sendSeckillMessage(SeckillOrder seckillOrder){
        String msg = JSON.toJSONString(seckillOrder);
        log.info("send message:"+msg);
        amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);

    }
}
