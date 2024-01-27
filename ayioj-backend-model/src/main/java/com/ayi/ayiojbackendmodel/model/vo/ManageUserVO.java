package com.ayi.ayiojbackendmodel.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * className:ManaUserVO
 * Package:com.ayi.ayioj.model.vo
 * Description: TODO
 *
 * @Date: 2024/1/19 0:17
 * @Author:hjy
 */
@Data
public class ManageUserVO {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userName;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    /**
     * 创建时间
     */
    private Date createTime;

}
