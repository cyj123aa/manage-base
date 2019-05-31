package com.hoolink.manage.base.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 角色相关信息
 * @author: WeiMin
 * @date: 2019-05-13
 **/
@Data
public class MiddleRoleMenuBO implements Serializable {

    /**
     * 菜单表ID
     */
    private Long menuId;

    /**
     * 菜单名 用于展示
     */
    private String menuName;

    /**
     * 父级菜单 用于展示
     */
    private Long parentId;

    /**
     * 优先级 用于展示
     */
    private Integer priority;

    /**
     * 权限级别 1-只读 2-全部(读、写)
     */
    private Integer permissionFlag;

    /**
     * 下一级菜单列表
     */
    private List<MiddleRoleMenuBO> childList;

}