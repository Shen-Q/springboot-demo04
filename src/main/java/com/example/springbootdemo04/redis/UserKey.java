package com.example.springbootdemo04.redis;

public class UserKey extends BasePrefix {

    public static final int TOKEN_EXPIRE = 3600*24 *2;//如果存在了token里，默认两天 3600*24 *2

    private UserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static UserKey getById = new UserKey(2000, "id");
    public static UserKey token = new UserKey(TOKEN_EXPIRE,"token");
}
