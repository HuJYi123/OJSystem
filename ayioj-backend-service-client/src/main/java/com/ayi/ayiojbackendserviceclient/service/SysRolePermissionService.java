package com.ayi.ayiojbackendserviceclient.service;

public interface SysRolePermissionService {

	/**
	 * 保存授权/先删后增
	 * 
	 * @param roleId
	 * @param permissionIds
	 */
	public void saveRolePermission(String roleId, String permissionIds);

}
