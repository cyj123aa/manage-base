package com.hoolink.manage.base.dao.model;

import java.io.Serializable;

public class ManageRole implements Serializable {
    /**
     * <pre>
     * 主键
     * 表字段 : manage_role.id
     * </pre>
     */
    private Long id;

    /**
     * <pre>
     * 角色名称
     * 表字段 : manage_role.role_name
     * </pre>
     */
    private String roleName;

    /**
     * <pre>
     * 角色描述
     * 表字段 : manage_role.role_desc
     * </pre>
     */
    private String roleDesc;

    /**
     * <pre>
     * 父级角色
     * 表字段 : manage_role.parent_id
     * </pre>
     */
    private Long parentId;

    /**
     * <pre>
     * 所属层级
     * 表字段 : manage_role.role_level
     * </pre>
     */
    private Byte roleLevel;

    /**
     * <pre>
     * 状态: 启用/禁用
     * 表字段 : manage_role.role_status
     * </pre>
     */
    private Boolean roleStatus;

    /**
     * <pre>
     * 创建人
     * 表字段 : manage_role.creator
     * </pre>
     */
    private Long creator;

    /**
     * <pre>
     * 修改人
     * 表字段 : manage_role.updator
     * </pre>
     */
    private Long updator;

    /**
     * <pre>
     * 创建时间
     * 表字段 : manage_role.created
     * </pre>
     */
    private Long created;

    /**
     * <pre>
     * 修改时间
     * 表字段 : manage_role.updated
     * </pre>
     */
    private Long updated;

    /**
     * <pre>
     * 数据有效性
     * 表字段 : manage_role.enabled
     * </pre>
     */
    private Boolean enabled;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table manage_role
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * <pre>
     * 获取：主键
     * 表字段：manage_role.id
     * </pre>
     *
     * @return manage_role.id：主键
     */
    public Long getId() {
        return id;
    }

    /**
     * <pre>
     * 设置：主键
     * 表字段：manage_role.id
     * </pre>
     *
     * @param id
     *            manage_role.id：主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * <pre>
     * 获取：角色名称
     * 表字段：manage_role.role_name
     * </pre>
     *
     * @return manage_role.role_name：角色名称
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * <pre>
     * 设置：角色名称
     * 表字段：manage_role.role_name
     * </pre>
     *
     * @param roleName
     *            manage_role.role_name：角色名称
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    /**
     * <pre>
     * 获取：角色描述
     * 表字段：manage_role.role_desc
     * </pre>
     *
     * @return manage_role.role_desc：角色描述
     */
    public String getRoleDesc() {
        return roleDesc;
    }

    /**
     * <pre>
     * 设置：角色描述
     * 表字段：manage_role.role_desc
     * </pre>
     *
     * @param roleDesc
     *            manage_role.role_desc：角色描述
     */
    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc == null ? null : roleDesc.trim();
    }

    /**
     * <pre>
     * 获取：父级角色
     * 表字段：manage_role.parent_id
     * </pre>
     *
     * @return manage_role.parent_id：父级角色
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * <pre>
     * 设置：父级角色
     * 表字段：manage_role.parent_id
     * </pre>
     *
     * @param parentId
     *            manage_role.parent_id：父级角色
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * <pre>
     * 获取：所属层级
     * 表字段：manage_role.role_level
     * </pre>
     *
     * @return manage_role.role_level：所属层级
     */
    public Byte getRoleLevel() {
        return roleLevel;
    }

    /**
     * <pre>
     * 设置：所属层级
     * 表字段：manage_role.role_level
     * </pre>
     *
     * @param roleLevel
     *            manage_role.role_level：所属层级
     */
    public void setRoleLevel(Byte roleLevel) {
        this.roleLevel = roleLevel;
    }

    /**
     * <pre>
     * 获取：状态: 启用/禁用
     * 表字段：manage_role.role_status
     * </pre>
     *
     * @return manage_role.role_status：状态: 启用/禁用
     */
    public Boolean getRoleStatus() {
        return roleStatus;
    }

    /**
     * <pre>
     * 设置：状态: 启用/禁用
     * 表字段：manage_role.role_status
     * </pre>
     *
     * @param roleStatus
     *            manage_role.role_status：状态: 启用/禁用
     */
    public void setRoleStatus(Boolean roleStatus) {
        this.roleStatus = roleStatus;
    }

    /**
     * <pre>
     * 获取：创建人
     * 表字段：manage_role.creator
     * </pre>
     *
     * @return manage_role.creator：创建人
     */
    public Long getCreator() {
        return creator;
    }

    /**
     * <pre>
     * 设置：创建人
     * 表字段：manage_role.creator
     * </pre>
     *
     * @param creator
     *            manage_role.creator：创建人
     */
    public void setCreator(Long creator) {
        this.creator = creator;
    }

    /**
     * <pre>
     * 获取：修改人
     * 表字段：manage_role.updator
     * </pre>
     *
     * @return manage_role.updator：修改人
     */
    public Long getUpdator() {
        return updator;
    }

    /**
     * <pre>
     * 设置：修改人
     * 表字段：manage_role.updator
     * </pre>
     *
     * @param updator
     *            manage_role.updator：修改人
     */
    public void setUpdator(Long updator) {
        this.updator = updator;
    }

    /**
     * <pre>
     * 获取：创建时间
     * 表字段：manage_role.created
     * </pre>
     *
     * @return manage_role.created：创建时间
     */
    public Long getCreated() {
        return created;
    }

    /**
     * <pre>
     * 设置：创建时间
     * 表字段：manage_role.created
     * </pre>
     *
     * @param created
     *            manage_role.created：创建时间
     */
    public void setCreated(Long created) {
        this.created = created;
    }

    /**
     * <pre>
     * 获取：修改时间
     * 表字段：manage_role.updated
     * </pre>
     *
     * @return manage_role.updated：修改时间
     */
    public Long getUpdated() {
        return updated;
    }

    /**
     * <pre>
     * 设置：修改时间
     * 表字段：manage_role.updated
     * </pre>
     *
     * @param updated
     *            manage_role.updated：修改时间
     */
    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    /**
     * <pre>
     * 获取：数据有效性
     * 表字段：manage_role.enabled
     * </pre>
     *
     * @return manage_role.enabled：数据有效性
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * <pre>
     * 设置：数据有效性
     * 表字段：manage_role.enabled
     * </pre>
     *
     * @param enabled
     *            manage_role.enabled：数据有效性
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}