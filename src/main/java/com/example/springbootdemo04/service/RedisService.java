package com.example.springbootdemo04.service;


import com.example.springbootdemo04.redis.BasePrefix;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    //添加进redis
    public boolean set(BasePrefix prefix, String key, Object value) {
        if(key!=null&&value!=null){
            String realKey = prefix.getPrefix()+key;
            int seconds = prefix.getExpireSeconds();//获取过期时间
            if (seconds <= 0) {
                redisTemplate.opsForValue().set(realKey, value);
            } else {
                //redisTemplate设置过期时间
                redisTemplate.opsForValue().set(realKey, value, seconds, TimeUnit.SECONDS);
            }
            return true;
        }else{
            return false;
        }
    }

    //从redis中获取，泛型应该是要把取回的结果转成实体类
    public <T> T get(BasePrefix prefix, String key,Class<T> clazz) {
            //对key增加前缀，即可用于分类，也避免key重复
            String realKey = prefix.getPrefix() + key;
            Object obj =redisTemplate.opsForValue().get(realKey);
            T t = new ObjectMapper().convertValue(obj,clazz);
            return t;
    }

    public boolean delete(BasePrefix prefix, String key) {
            //生成真正的key
        try{
            String realKey = prefix.getPrefix() + key;
            boolean res = redisTemplate.delete(realKey);
            return res;
        }catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断key是否存在
     */
    public boolean exists(BasePrefix prefix, String key) {
        String realKey = prefix.getPrefix() + key;
        return redisTemplate.hasKey(realKey);
    }


    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        //奇了怪了这个factory哪儿来的。。。

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(factory);

        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);

        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        // 值采用json序列化
        template.setValueSerializer(jacksonSeial);

        // 设置hash key 和value序列化模式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jacksonSeial);

        //下面这个不知道有什么用！！！懂了，这个是调用方法，把所有库存加载到缓存里的~
        //template.afterPropertiesSet();

        return template;
    }

}
