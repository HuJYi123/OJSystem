package com.example.springboot_02.security;

import java.security.Permission;

/**
 * className:DefaultSecurityManager
 * Package:com.example.springboot_02.security
 * Description: 默认安全管理器
 *
 * @Date: 2023/12/17 18:08
 * @Author:hjy
 */
public class DefaultSecurityManager extends SecurityManager{

    /**
     * 检查所有的权限
     * @param perm   the requested permission.
     */
    @Override
    public void checkPermission(Permission perm) {
        super.checkPermission(perm);
    }
}
