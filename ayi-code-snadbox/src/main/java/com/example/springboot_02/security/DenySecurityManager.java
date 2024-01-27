package com.example.springboot_02.security;

import java.security.Permission;

/**
 * className:DefaultSecurityManager
 * Package:com.example.springboot_02.security
 * Description: 禁用所有权限的安全管理器
 *
 * @Date: 2023/12/17 18:08
 * @Author:hjy
 */
public class DenySecurityManager extends SecurityManager{

    /**
     * 检查所有的权限
     * @param perm   the requested permission.
     */
    @Override
    public void checkPermission(Permission perm) {
        throw new SecurityException("权限不足：" + perm.toString());
    }
}
