package com.example.springbootdemo04.mapper;

import com.example.springbootdemo04.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.Mapping;

import java.util.List;

@Mapper
public interface UserMapper {

    public List<User> selectAll();

    public User getById(@Param("id") long id);

    public int update(User toBeUpdate);
}
