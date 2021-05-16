package com.example.springbootdemo04.util;

import java.util.UUID;

public class UUIDUtil {

    public static String uuid() {
        //本来是这样的b5112c1d-e3ed-4078-9d5b-737346b47d63，需要把-去掉
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void main(String[] args) {
        System.out.println(uuid());
    }
}
