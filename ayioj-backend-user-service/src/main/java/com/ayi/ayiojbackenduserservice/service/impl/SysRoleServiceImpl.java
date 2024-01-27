package com.ayi.ayiojbackenduserservice.service.impl;

import com.ayi.ayiojbackenduserservice.mapper.RoleMapper;
import com.ayi.ayiojbackendmodel.model.entity.Role;
import com.ayi.ayiojbackenduserservice.service.SysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * className:SysRoleServiceImpl
 * Package:com.ayi.ayioj.service.impl
 * Description: TODO
 *
 * @Date: 2024/1/13 17:58
 * @Author:hjy
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements SysRoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public List<String> getRole(String userAccount) {
        List<String> roleList = roleMapper.getRoleList(userAccount);
        return roleList;
    }

    @Override
    public Long getIdByName(String userRole) {
        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Role::getRoleName, userRole);
        Role role = baseMapper.selectOne(lambdaQueryWrapper);
        return role.getId();
    }
}
