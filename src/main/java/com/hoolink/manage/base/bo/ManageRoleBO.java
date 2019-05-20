package com.hoolink.manage.base.bo;

import lombok.Data;

/**
 * @author lijunling
 * @description
 * @date 2019/05/15 18:59
 */
@Data
public class ManageRoleBO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String roleDesc;

    /**
     * 父级角色
     */
    private Long parentId;

    /**
     * 所属层级
     */
    private Byte roleLevel;

    /**
     * 状态: 启用/禁用
     */
    private Boolean roleStatus;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 修改人
     */
    private Long updator;

    /**
     * 创建时间
     */
    private Long created;

    /**
     * 修改时间
     */
    private Long updated;

    /**
     * 数据有效性
     */
    private Boolean enabled;
}
