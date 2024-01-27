package com.ayi.ayiojbackenduserservice.service.impl;

import com.ayi.ayiojbackenduserservice.mapper.UserRoleMapper;
import com.ayi.ayiojbackendmodel.model.entity.UserRole;
import com.ayi.ayiojbackenduserservice.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * className:UserRoleServiceImpl
 * Package:com.ayi.ayioj.service.impl
 * Description: TODO
 *
 * @Date: 2024/1/18 16:59
 * @Author:hjy
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Override
    public Boolean deleteByUserIds(Long ids) {
        int i = userRoleMapper.deleteByUserIds(ids);
        return i >= 0;
    }

    @Override
    public Boolean addUserRole(UserRole userRole) {
        int insert = baseMapper.insert(userRole);
        return insert > 0;
    }
}
