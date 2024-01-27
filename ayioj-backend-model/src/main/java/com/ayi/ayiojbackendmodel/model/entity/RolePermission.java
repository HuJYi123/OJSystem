package com.ayi.ayiojbackendmodel.model.entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RolePermission {

	/**
	 * 角色id
	 */
	private String roleId;

	/**
	 * 权限id
	 */
	private String permissionId;

	/**
	 * 数据权限
	 */
	private String dataRuleIds;

	public RolePermission() {
	}

	public RolePermission(String roleId, String permissionId) {
		this.roleId = roleId;
		this.permissionId = permissionId;
	}

}
