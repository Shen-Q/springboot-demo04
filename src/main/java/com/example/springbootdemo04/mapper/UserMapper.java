package com.example.springbootdemo04.mapper;

import com.example.springbootdemo04.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface UserMapper {

    List<User> selectAll();

    User getById(@Param("id") long id);

    int update(User toBeUpdate);
}
