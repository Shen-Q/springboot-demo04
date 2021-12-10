package com.example.springbootdemo04.rabbitmq;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

   // public static final String SECKILL_QUEUE = "seckill.queue";
    public static final String QUEUE = "queue";
    //两个队列……四个队列？
    public static final String TOPIC_QUEUE1 = "topic.queue1";
    public static final String TOPIC_QUEUE2 = "topic.queue2";
    //这个是交换机。
    public static final String TOPIC_EXCHANGE = "topicExchage";

    //这是那三个队列
    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true);
    }
    @Bean
    public Queue topicQueue1() {
        return new Queue(TOPIC_QUEUE1, true);
    }
    @Bean
    public Queue topicQueue2() {
        return new Queue(TOPIC_QUEUE2, true);
    }

    //初始化交换机
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    //将队列和交换机进行绑定
    @Bean
    public Binding topicBinding1() {
        //交换机接收到topic.key1，发送给topicQueue1
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("topic.key1");
    }
    @Bean
    public Binding topicBinding2() {
        //交换机接收到topic.#（任何topic消息），发送给topicQueue2
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("topic.#");
    }

}
