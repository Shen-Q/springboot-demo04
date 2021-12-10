package com.example.springbootdemo04.controller;


import com.example.springbootdemo04.service.UserService;
import com.example.springbootdemo04.vo.LoginVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {

    //在控制台打印日志
    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @RequestMapping("/to_login")
    public String login(){
        return "login";
    }

    @RequestMapping("/do_login")
//    @ResponseBody
    public String doLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session, LoginVO loginVO) {//加入JSR303参数校验
        log.info(loginVO.toString());
        boolean res = userService.login(request, response, loginVO);
        // 更新  boolean res = userService.updatePassword(request, response, Long.valueOf(loginVO.getMobile()),loginVO.getPassword());
        if(res==false){
            return "login";
        }
        session.setAttribute("loginUser",loginVO);
        return "redirect:/goods/to_list";
    }


    @RequestMapping("/do_update")
    public String doUpdate(HttpServletRequest request, LoginVO loginVO){
        log.info(loginVO.toString());
        boolean res = userService.updatePassword(request, Long.valueOf(loginVO.getMobile()),loginVO.getPassword());
        if(res==false){
            return "login";
        }
        return "redirect:/goods/to_list";
    }

}
