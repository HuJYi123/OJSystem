package com.ayi.ayiojbackenduserservice.service;

import com.ayi.ayiojbackendmodel.model.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * className:UserRoleService
 * Package:com.ayi.ayioj.service
 * Description: TODO
 *
 * @Date: 2024/1/18 16:59
 * @Author:hjy
 */
public interface UserRoleService extends IService<UserRole> {
    
    Boolean deleteByUserIds(Long ids);

    Boolean addUserRole(UserRole userRole);
}
