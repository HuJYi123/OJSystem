package com.ayi.ayiojbackenduserservice.mapper;

import com.ayi.ayiojbackendmodel.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户数据库操作
 */
public interface UserMapperaa extends BaseMapper<User> {

    List<User> selectUserByUserId(String userAccount);

    Long insertUser(@Param("user") User user);
}




