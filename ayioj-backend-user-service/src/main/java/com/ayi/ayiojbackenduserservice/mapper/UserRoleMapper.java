package com.ayi.ayiojbackenduserservice.mapper;

import com.ayi.ayiojbackendmodel.model.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

/**
 * className:UserRoleMapper
 * Package:com.ayi.ayioj.mapper
 * Description: TODO
 *
 * @Date: 2024/1/18 16:58
 * @Author:hjy
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {
    @Delete("delete from user_role where userId = #{ids} ;")
    int deleteByUserIds(@Param("ids") Long ids);
}
