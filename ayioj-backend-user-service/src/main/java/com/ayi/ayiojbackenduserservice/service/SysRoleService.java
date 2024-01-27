package com.ayi.ayiojbackenduserservice.service;



import com.ayi.ayiojbackendmodel.model.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysRoleService extends IService<Role> {
    List<String> getRole(String username);

    Long getIdByName(String userRole);

//	Page<SysRoleVo> queryPage(SysRoleQuery dto);
//
//	Boolean add(SysRoleDTO sysRole);
//
//	Boolean updateById(SysRoleDTO sysRole);
//
//	Boolean removeById(String id);
//
//	Boolean removeByIds(List<String> idList);
//
//	List<SysRoleVo> getRoleList(Boolean isReference);
//
//	/**
//	 * 角色授权
//	 * @return
//	 */
//	Boolean roleAuth(List<SysRolePermissionDTO> dto);
//
//	List<String> getPerm(SysIdDTO dto);
//
//	Boolean addOrEdit(SysRoleDTO role);
//
//	List<String> getRoleNameList();
}
