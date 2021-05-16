package com.example.springbootdemo04.service;

import com.alibaba.druid.util.StringUtils;
import com.example.springbootdemo04.entity.User;
import com.example.springbootdemo04.mapper.UserMapper;
import com.example.springbootdemo04.redis.UserKey;
import com.example.springbootdemo04.util.UUIDUtil;
import com.example.springbootdemo04.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisService redisService;

    public static final String COOKIE_NAME_TOKEN = "token";

    public User getById(HttpServletRequest request,HttpServletResponse response,long id) {
        //先查找浏览器中是否有token
        String token = getCookieValue(request,COOKIE_NAME_TOKEN);
        //有的话用token去redis里取
        if(token!=null){
            User user =  redisService.get(UserKey.token,""+token,User.class);
            //如果取到了，则返回
            if(id==user.getId()){
                return user;
            }else{
                return null;
            }
        }

        //如果没有token，去数据库取
        User user = userMapper.getById(id);
        //生成token，再存入redis缓存
        if (user != null) {
            token = UUIDUtil.uuid();
            addCookie(response, token, user);
            redisService.set(UserKey.token,""+token, user);
            return user;
        }else{
            return null;
        }
    }

    /**
     * 典型缓存同步场景：更新密码
     */
    public boolean updatePassword(HttpServletRequest request,long id, String pass) {
        //直接从数据库里取id，如果没有返回false
        User user = userMapper.getById(id);
        if(user == null) {
            return false;
       //     throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //如果有，使用新的密码更新数据库和缓存
        String token = getCookieValue(request,COOKIE_NAME_TOKEN);
        user.setPassword(pass);
        //1.更新数据库:先更新数据库再更新缓存（这里需要加事务吗？）
        userMapper.update(user);
        //2.更新缓存：先删除再插入
        redisService.delete(UserKey.token,""+token);
        redisService.set(UserKey.token, ""+token, user);

        return true;
    }

    public boolean login(HttpServletRequest request ,HttpServletResponse response, LoginVO loginVO) {
        if (loginVO == null) {
      //      throw new GlobalException(CodeMsg.SERVER_ERROR);
            return false;
        }
        String mobile = loginVO.getMobile();
        String pass = loginVO.getPassword();
        //判断手机号是否存在，查询User
        User user = getById(request,response,Long.parseLong(mobile));
        if (user == null||!user.getPassword().equals(pass)) {
        //    throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
            return false;
        }
        return true;
    }


    /**
     * 将token做为key，用户信息做为value 存入redis模拟session
     * 同时将token存入cookie，保存登录状态
     */
    //cookie
    public void addCookie(HttpServletResponse response, String token, User user) {
        redisService.set(UserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(UserKey.token.getExpireSeconds());
        cookie.setPath("/");//设置为网站根目录，这样所有页面都可共享
        response.addCookie(cookie);
    }

    //遍历所有cookie，找到需要的那个cookie 名字应该是，"token"
    public String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * 根据token获取用户信息，类似于从session里取user一样，但是token是放在客户端的。
     */
    public User getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        User user = redisService.get(UserKey.token, token, User.class);
        //延长有效期，有效期等于最后一次操作+有效期
        if (user != null) {
            addCookie(response, token, user);
        }
        return user;
    }

}
