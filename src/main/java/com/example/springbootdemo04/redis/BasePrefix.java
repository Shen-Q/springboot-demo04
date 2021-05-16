package com.example.springbootdemo04.redis;

import lombok.Data;

@Data
public class BasePrefix {

    private int expireSeconds;
    private String prefix;

    public BasePrefix(String prefix){
        this(0, prefix);//默认0代表永不过期
    }

    public BasePrefix(int expireSeconds, String prefix){
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    public String getPrefix() {
        String className = getClass().getSimpleName();//拿到参数类类名
        return className + ":" + prefix;
    }
}
