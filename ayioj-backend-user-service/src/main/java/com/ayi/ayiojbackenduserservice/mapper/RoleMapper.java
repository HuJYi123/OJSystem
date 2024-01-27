package com.ayi.ayiojbackenduserservice.mapper;

import com.ayi.ayiojbackendmodel.model.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * className:RoleMapper
 * Package:com.ayi.ayioj.mapper
 * Description: TODO
 *
 * @Date: 2024/1/17 9:19
 * @Author:hjy
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     *
     * @param userAccount
     * @return
     */
    List<String> getRoleList(@Param("userAccount") String userAccount);

    String getRoleName(Long userId);
}
