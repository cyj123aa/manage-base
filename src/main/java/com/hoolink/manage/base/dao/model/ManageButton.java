package com.hoolink.manage.base.dao.model;

import java.io.Serializable;

public class ManageButton implements Serializable {
    /**
     * <pre>
     * 主键
     * 表字段 : manage_button.id
     * </pre>
     */
    private Long id;

    /**
     * <pre>
     * 菜单表外键
     * 表字段 : manage_button.menu_id
     * </pre>
     */
    private Long menuId;

    /**
     * <pre>
     * 按钮编码
     * 表字段 : manage_button.button_code
     * </pre>
     */
    private String buttonCode;

    /**
     * <pre>
     * 按钮名称
     * 表字段 : manage_button.button_name
     * </pre>
     */
    private String buttonName;

    /**
     * <pre>
     * 按钮url
     * 表字段 : manage_button.button_url
     * </pre>
     */
    private String buttonUrl;

    /**
     * <pre>
     * 按钮描述
     * 表字段 : manage_button.button_desc
     * </pre>
     */
    private String buttonDesc;

    /**
     * <pre>
     * 按钮级别 1-查询类 2-操作类
     * 表字段 : manage_button.button_type
     * </pre>
     */
    private Byte buttonType;

    /**
     * <pre>
     * 创建人
     * 表字段 : manage_button.creator
     * </pre>
     */
    private Long creator;

    /**
     * <pre>
     * 修改人
     * 表字段 : manage_button.updator
     * </pre>
     */
    private Long updator;

    /**
     * <pre>
     * 创建时间
     * 表字段 : manage_button.created
     * </pre>
     */
    private Long created;

    /**
     * <pre>
     * 修改时间
     * 表字段 : manage_button.updated
     * </pre>
     */
    private Long updated;

    /**
     * <pre>
     * 数据有效性
     * 表字段 : manage_button.enabled
     * </pre>
     */
    private Boolean enabled;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table manage_button
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * <pre>
     * 获取：主键
     * 表字段：manage_button.id
     * </pre>
     *
     * @return manage_button.id：主键
     */
    public Long getId() {
        return id;
    }

    /**
     * <pre>
     * 设置：主键
     * 表字段：manage_button.id
     * </pre>
     *
     * @param id
     *            manage_button.id：主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * <pre>
     * 获取：菜单表外键
     * 表字段：manage_button.menu_id
     * </pre>
     *
     * @return manage_button.menu_id：菜单表外键
     */
    public Long getMenuId() {
        return menuId;
    }

    /**
     * <pre>
     * 设置：菜单表外键
     * 表字段：manage_button.menu_id
     * </pre>
     *
     * @param menuId
     *            manage_button.menu_id：菜单表外键
     */
    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    /**
     * <pre>
     * 获取：按钮编码
     * 表字段：manage_button.button_code
     * </pre>
     *
     * @return manage_button.button_code：按钮编码
     */
    public String getButtonCode() {
        return buttonCode;
    }

    /**
     * <pre>
     * 设置：按钮编码
     * 表字段：manage_button.button_code
     * </pre>
     *
     * @param buttonCode
     *            manage_button.button_code：按钮编码
     */
    public void setButtonCode(String buttonCode) {
        this.buttonCode = buttonCode == null ? null : buttonCode.trim();
    }

    /**
     * <pre>
     * 获取：按钮名称
     * 表字段：manage_button.button_name
     * </pre>
     *
     * @return manage_button.button_name：按钮名称
     */
    public String getButtonName() {
        return buttonName;
    }

    /**
     * <pre>
     * 设置：按钮名称
     * 表字段：manage_button.button_name
     * </pre>
     *
     * @param buttonName
     *            manage_button.button_name：按钮名称
     */
    public void setButtonName(String buttonName) {
        this.buttonName = buttonName == null ? null : buttonName.trim();
    }

    /**
     * <pre>
     * 获取：按钮url
     * 表字段：manage_button.button_url
     * </pre>
     *
     * @return manage_button.button_url：按钮url
     */
    public String getButtonUrl() {
        return buttonUrl;
    }

    /**
     * <pre>
     * 设置：按钮url
     * 表字段：manage_button.button_url
     * </pre>
     *
     * @param buttonUrl
     *            manage_button.button_url：按钮url
     */
    public void setButtonUrl(String buttonUrl) {
        this.buttonUrl = buttonUrl == null ? null : buttonUrl.trim();
    }

    /**
     * <pre>
     * 获取：按钮描述
     * 表字段：manage_button.button_desc
     * </pre>
     *
     * @return manage_button.button_desc：按钮描述
     */
    public String getButtonDesc() {
        return buttonDesc;
    }

    /**
     * <pre>
     * 设置：按钮描述
     * 表字段：manage_button.button_desc
     * </pre>
     *
     * @param buttonDesc
     *            manage_button.button_desc：按钮描述
     */
    public void setButtonDesc(String buttonDesc) {
        this.buttonDesc = buttonDesc == null ? null : buttonDesc.trim();
    }

    /**
     * <pre>
     * 获取：按钮级别 1-查询类 2-操作类
     * 表字段：manage_button.button_type
     * </pre>
     *
     * @return manage_button.button_type：按钮级别 1-查询类 2-操作类
     */
    public Byte getButtonType() {
        return buttonType;
    }

    /**
     * <pre>
     * 设置：按钮级别 1-查询类 2-操作类
     * 表字段：manage_button.button_type
     * </pre>
     *
     * @param buttonType
     *            manage_button.button_type：按钮级别 1-查询类 2-操作类
     */
    public void setButtonType(Byte buttonType) {
        this.buttonType = buttonType;
    }

    /**
     * <pre>
     * 获取：创建人
     * 表字段：manage_button.creator
     * </pre>
     *
     * @return manage_button.creator：创建人
     */
    public Long getCreator() {
        return creator;
    }

    /**
     * <pre>
     * 设置：创建人
     * 表字段：manage_button.creator
     * </pre>
     *
     * @param creator
     *            manage_button.creator：创建人
     */
    public void setCreator(Long creator) {
        this.creator = creator;
    }

    /**
     * <pre>
     * 获取：修改人
     * 表字段：manage_button.updator
     * </pre>
     *
     * @return manage_button.updator：修改人
     */
    public Long getUpdator() {
        return updator;
    }

    /**
     * <pre>
     * 设置：修改人
     * 表字段：manage_button.updator
     * </pre>
     *
     * @param updator
     *            manage_button.updator：修改人
     */
    public void setUpdator(Long updator) {
        this.updator = updator;
    }

    /**
     * <pre>
     * 获取：创建时间
     * 表字段：manage_button.created
     * </pre>
     *
     * @return manage_button.created：创建时间
     */
    public Long getCreated() {
        return created;
    }

    /**
     * <pre>
     * 设置：创建时间
     * 表字段：manage_button.created
     * </pre>
     *
     * @param created
     *            manage_button.created：创建时间
     */
    public void setCreated(Long created) {
        this.created = created;
    }

    /**
     * <pre>
     * 获取：修改时间
     * 表字段：manage_button.updated
     * </pre>
     *
     * @return manage_button.updated：修改时间
     */
    public Long getUpdated() {
        return updated;
    }

    /**
     * <pre>
     * 设置：修改时间
     * 表字段：manage_button.updated
     * </pre>
     *
     * @param updated
     *            manage_button.updated：修改时间
     */
    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    /**
     * <pre>
     * 获取：数据有效性
     * 表字段：manage_button.enabled
     * </pre>
     *
     * @return manage_button.enabled：数据有效性
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * <pre>
     * 设置：数据有效性
     * 表字段：manage_button.enabled
     * </pre>
     *
     * @param enabled
     *            manage_button.enabled：数据有效性
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}