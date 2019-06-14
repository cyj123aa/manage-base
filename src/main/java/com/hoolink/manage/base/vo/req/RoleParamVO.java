package com.hoolink.manage.base.vo.req;

import lombok.Data;

import java.util.List;

/**
 * @description: 角色相关信息
 * @author: WeiMin
 * @date: 2019-05-13
 **/
@Data
public class RoleParamVO {
    /**
     * roleId
     */
    private Long id;

    private String roleName;

    private String roleDesc;

    private List<MiddleRoleMenuVO> roleMenuVOList;

    /**
     * 状态: 启用/禁用
     */
    private Boolean roleStatus;
    /**
     * 所属层级
     */
    private Byte roleLevel;

    /**
     * <pre>
     * 角色类型【0：普通角色  1：文控角色】
     * 表字段 : manage_role.role_type
     * </pre>
     */
    private Boolean roleType;

}
