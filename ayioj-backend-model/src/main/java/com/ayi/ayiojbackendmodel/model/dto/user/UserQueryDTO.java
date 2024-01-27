package com.ayi.ayiojbackendmodel.model.dto.user;

import com.ayi.ayiojbackendcommom.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * className:UserQueryDTO
 * Package:com.ayi.ayioj.model.dto.user
 * Description: TODO
 *
 * @Date: 2023/10/24 17:24
 * @Author:hjy
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryDTO extends PageRequest {
    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}
