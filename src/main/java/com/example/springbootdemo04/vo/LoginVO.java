package com.example.springbootdemo04.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoginVO {

   //因为框架没有校验手机格式注解，所以自己定义
    private String mobile;
    private String password;
}
