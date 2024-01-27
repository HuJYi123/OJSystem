package com.ayi.ayiojbackendserviceclient.service;

import com.ayi.ayiojbackendmodel.model.entity.Permission;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SysPermissionService {

	public List<Permission> queryByUser(String username);

	/**
	 * 查询权限树状结构返回给前端
	 * @return
	 */
	List<Permission> list();

//	List<TreeModelVO> queryTreeList();

	/**
	 * 根据parentId查询权限表
	 * @param parentId
	 * @return
	 */
    List<Permission> queryListByParentId(String parentId);

    /**
     * 真实删除
     */
    Boolean deletePermission(String id);

//    Boolean addPermission(SysPermissionDTO sysPermission);
//
//    Boolean editPermission(SysPermissionDTO sysPermission);

//	JSONArray queryByUserPermission(SysPermissionDTO dto);

    List<Permission> queryByUserPermission(String username,Integer menuType, Boolean perms,Boolean all);

//    JSONArray queryByPermission(SysPermissionDTO dto);

    List<Permission> queryPermsByUser(String userName);

//	List<SysPermissionTree2VO> list2(String userId);
}
