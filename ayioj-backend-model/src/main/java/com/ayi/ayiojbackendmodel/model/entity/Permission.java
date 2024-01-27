package com.ayi.ayiojbackendmodel.model.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 菜单权限表
 * </p>
 *
 */
@SuppressWarnings("serial")
@Data
@Accessors(chain = true)
public class Permission {

    /**
     * 父id
     */
    private String parentId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 路径
     */
    private String url;

    /**
     * 组件
     */
    private String component;

    /**
     * 组件名字
     */
    private String componentName;

    /**
     * 一级菜单跳转地址
     */
    private String redirect;

    /**
     * 类型（0：一级菜单；1：子菜单 ；2：按钮权限）
     */
    private Integer menuType;

    /**
     * 菜单权限编码，例如：“sys:schedule:list,sys:schedule:info”,多个逗号隔开
     */
    private String perms;

    /**
     * 菜单排序
     */
    private Integer sortNo;

    /**
     * alwaysShow
     */
    private Boolean alwaysShow;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 是否路由菜单: 0:不是  1:是（默认值1）
     */
    private Boolean route;

    /**
     * 是否叶子节点: 1:是  0:不是
     */
    private Boolean leaf;

    /**
     * 是否隐藏路由菜单: 0否,1是（默认值0）
     */
    private Boolean hidden;

    /**
     * 子系统编码
     */
    private String sysTypeNumber;
    
    /**
     * 是否悬浮
     */
    private Boolean affix;
    
    /**
     * 完整路径id
     */
    private String fullIds;
    
    private Boolean canVisite;
}
