package com.hoolink.manage.base.dao.model;

import java.io.Serializable;

public class User implements Serializable {
    /**
     * <pre>
     * 主键
     * 表字段 : manage_user.id
     * </pre>
     */
    private Long id;

    /**
     * <pre>
     * 员工编号
     * 表字段 : manage_user.user_no
     * </pre>
     */
    private String userNo;

    /**
     * <pre>
     * 用户账号
     * 表字段 : manage_user.user_account
     * </pre>
     */
    private String userAccount;

    /**
     * <pre>
     * 姓名
     * 表字段 : manage_user.user_name
     * </pre>
     */
    private String name;

    /**
     * <pre>
     * 性别: 1/0 男/女
     * 表字段 : manage_user.sex
     * </pre>
     */
    private Boolean sex;

    /**
     * <pre>
     * 角色ID
     * 表字段 : manage_user.role_id
     * </pre>
     */
    private Long roleId;

    /**
     * <pre>
     * 密码
     * 表字段 : manage_user.passwd
     * </pre>
     */
    private String passwd;

    /**
     * <pre>
     * 用户头像ID
     * 表字段 : manage_user.img_id
     * </pre>
     */
    private Long imgId;

    /**
     * <pre>
     * 手机号
     * 表字段 : manage_user.phone
     * </pre>
     */
    private String phone;

    /**
     * <pre>
     * 所属公司
     * 表字段 : manage_user.company
     * </pre>
     */
    private String company;

    /**
     * <pre>
     * 职位
     * 表字段 : manage_user.position
     * </pre>
     */
    private String position;

    /**
     * <pre>
     * 资源库密保等级（1-一级,2-二级,3-三级,4-四级）
     * 表字段 : manage_user.encry_level_company
     * </pre>
     */
    private Integer encryLevelCompany;

    /**
     * <pre>
     * 是否第一次登录
     * 表字段 : manage_user.first_login
     * </pre>
     */
    private Boolean firstLogin;

    /**
     * <pre>
     * 最后一次登录时间
     * 表字段 : manage_user.last_time
     * </pre>
     */
    private Long lastTime;

    /**
     * <pre>
     * 用户状态: 启用/禁用
     * 表字段 : manage_user.user_status
     * </pre>
     */
    private Boolean status;

    /**
     * <pre>
     * 创建人
     * 表字段 : manage_user.creator
     * </pre>
     */
    private Long creator;

    /**
     * <pre>
     * 修改人
     * 表字段 : manage_user.updator
     * </pre>
     */
    private Long updator;

    /**
     * <pre>
     * 创建时间
     * 表字段 : manage_user.created
     * </pre>
     */
    private Long created;

    /**
     * <pre>
     * 修改时间
     * 表字段 : manage_user.updated
     * </pre>
     */
    private Long updated;

    /**
     * <pre>
     * 数据有效性
     * 表字段 : manage_user.enabled
     * </pre>
     */
    private Boolean enabled;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table manage_user
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * <pre>
     * 获取：主键
     * 表字段：manage_user.id
     * </pre>
     *
     * @return manage_user.id：主键
     */
    public Long getId() {
        return id;
    }

    /**
     * <pre>
     * 设置：主键
     * 表字段：manage_user.id
     * </pre>
     *
     * @param id
     *            manage_user.id：主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * <pre>
     * 获取：员工编号
     * 表字段：manage_user.user_no
     * </pre>
     *
     * @return manage_user.user_no：员工编号
     */
    public String getUserNo() {
        return userNo;
    }

    /**
     * <pre>
     * 设置：员工编号
     * 表字段：manage_user.user_no
     * </pre>
     *
     * @param userNo
     *            manage_user.user_no：员工编号
     */
    public void setUserNo(String userNo) {
        this.userNo = userNo == null ? null : userNo.trim();
    }

    /**
     * <pre>
     * 获取：用户账号
     * 表字段：manage_user.user_account
     * </pre>
     *
     * @return manage_user.user_account：用户账号
     */
    public String getUserAccount() {
        return userAccount;
    }

    /**
     * <pre>
     * 设置：用户账号
     * 表字段：manage_user.user_account
     * </pre>
     *
     * @param userAccount
     *            manage_user.user_account：用户账号
     */
    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount == null ? null : userAccount.trim();
    }

    /**
     * <pre>
     * 获取：姓名
     * 表字段：manage_user.user_name
     * </pre>
     *
     * @return manage_user.user_name：姓名
     */
    public String getName() {
        return name;
    }

    /**
     * <pre>
     * 设置：姓名
     * 表字段：manage_user.user_name
     * </pre>
     *
     * @param name
     *            manage_user.user_name：姓名
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * <pre>
     * 获取：性别: 1/0 男/女
     * 表字段：manage_user.sex
     * </pre>
     *
     * @return manage_user.sex：性别: 1/0 男/女
     */
    public Boolean getSex() {
        return sex;
    }

    /**
     * <pre>
     * 设置：性别: 1/0 男/女
     * 表字段：manage_user.sex
     * </pre>
     *
     * @param sex
     *            manage_user.sex：性别: 1/0 男/女
     */
    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    /**
     * <pre>
     * 获取：角色ID
     * 表字段：manage_user.role_id
     * </pre>
     *
     * @return manage_user.role_id：角色ID
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * <pre>
     * 设置：角色ID
     * 表字段：manage_user.role_id
     * </pre>
     *
     * @param roleId
     *            manage_user.role_id：角色ID
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * <pre>
     * 获取：密码
     * 表字段：manage_user.passwd
     * </pre>
     *
     * @return manage_user.passwd：密码
     */
    public String getPasswd() {
        return passwd;
    }

    /**
     * <pre>
     * 设置：密码
     * 表字段：manage_user.passwd
     * </pre>
     *
     * @param passwd
     *            manage_user.passwd：密码
     */
    public void setPasswd(String passwd) {
        this.passwd = passwd == null ? null : passwd.trim();
    }

    /**
     * <pre>
     * 获取：用户头像ID
     * 表字段：manage_user.img_id
     * </pre>
     *
     * @return manage_user.img_id：用户头像ID
     */
    public Long getImgId() {
        return imgId;
    }

    /**
     * <pre>
     * 设置：用户头像ID
     * 表字段：manage_user.img_id
     * </pre>
     *
     * @param imgId
     *            manage_user.img_id：用户头像ID
     */
    public void setImgId(Long imgId) {
        this.imgId = imgId;
    }

    /**
     * <pre>
     * 获取：手机号
     * 表字段：manage_user.phone
     * </pre>
     *
     * @return manage_user.phone：手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * <pre>
     * 设置：手机号
     * 表字段：manage_user.phone
     * </pre>
     *
     * @param phone
     *            manage_user.phone：手机号
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * <pre>
     * 获取：所属公司
     * 表字段：manage_user.company
     * </pre>
     *
     * @return manage_user.company：所属公司
     */
    public String getCompany() {
        return company;
    }

    /**
     * <pre>
     * 设置：所属公司
     * 表字段：manage_user.company
     * </pre>
     *
     * @param company
     *            manage_user.company：所属公司
     */
    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
    }

    /**
     * <pre>
     * 获取：职位
     * 表字段：manage_user.position
     * </pre>
     *
     * @return manage_user.position：职位
     */
    public String getPosition() {
        return position;
    }

    /**
     * <pre>
     * 设置：职位
     * 表字段：manage_user.position
     * </pre>
     *
     * @param position
     *            manage_user.position：职位
     */
    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }

    /**
     * <pre>
     * 获取：资源库密保等级（1-一级,2-二级,3-三级,4-四级）
     * 表字段：manage_user.encry_level_company
     * </pre>
     *
     * @return manage_user.encry_level_company：资源库密保等级（1-一级,2-二级,3-三级,4-四级）
     */
    public Integer getEncryLevelCompany() {
        return encryLevelCompany;
    }

    /**
     * <pre>
     * 设置：资源库密保等级（1-一级,2-二级,3-三级,4-四级）
     * 表字段：manage_user.encry_level_company
     * </pre>
     *
     * @param encryLevelCompany
     *            manage_user.encry_level_company：资源库密保等级（1-一级,2-二级,3-三级,4-四级）
     */
    public void setEncryLevelCompany(Integer encryLevelCompany) {
        this.encryLevelCompany = encryLevelCompany;
    }

    /**
     * <pre>
     * 获取：是否第一次登录
     * 表字段：manage_user.first_login
     * </pre>
     *
     * @return manage_user.first_login：是否第一次登录
     */
    public Boolean getFirstLogin() {
        return firstLogin;
    }

    /**
     * <pre>
     * 设置：是否第一次登录
     * 表字段：manage_user.first_login
     * </pre>
     *
     * @param firstLogin
     *            manage_user.first_login：是否第一次登录
     */
    public void setFirstLogin(Boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    /**
     * <pre>
     * 获取：最后一次登录时间
     * 表字段：manage_user.last_time
     * </pre>
     *
     * @return manage_user.last_time：最后一次登录时间
     */
    public Long getLastTime() {
        return lastTime;
    }

    /**
     * <pre>
     * 设置：最后一次登录时间
     * 表字段：manage_user.last_time
     * </pre>
     *
     * @param lastTime
     *            manage_user.last_time：最后一次登录时间
     */
    public void setLastTime(Long lastTime) {
        this.lastTime = lastTime;
    }

    /**
     * <pre>
     * 获取：用户状态: 启用/禁用
     * 表字段：manage_user.user_status
     * </pre>
     *
     * @return manage_user.user_status：用户状态: 启用/禁用
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * <pre>
     * 设置：用户状态: 启用/禁用
     * 表字段：manage_user.user_status
     * </pre>
     *
     * @param status
     *            manage_user.user_status：用户状态: 启用/禁用
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * <pre>
     * 获取：创建人
     * 表字段：manage_user.creator
     * </pre>
     *
     * @return manage_user.creator：创建人
     */
    public Long getCreator() {
        return creator;
    }

    /**
     * <pre>
     * 设置：创建人
     * 表字段：manage_user.creator
     * </pre>
     *
     * @param creator
     *            manage_user.creator：创建人
     */
    public void setCreator(Long creator) {
        this.creator = creator;
    }

    /**
     * <pre>
     * 获取：修改人
     * 表字段：manage_user.updator
     * </pre>
     *
     * @return manage_user.updator：修改人
     */
    public Long getUpdator() {
        return updator;
    }

    /**
     * <pre>
     * 设置：修改人
     * 表字段：manage_user.updator
     * </pre>
     *
     * @param updator
     *            manage_user.updator：修改人
     */
    public void setUpdator(Long updator) {
        this.updator = updator;
    }

    /**
     * <pre>
     * 获取：创建时间
     * 表字段：manage_user.created
     * </pre>
     *
     * @return manage_user.created：创建时间
     */
    public Long getCreated() {
        return created;
    }

    /**
     * <pre>
     * 设置：创建时间
     * 表字段：manage_user.created
     * </pre>
     *
     * @param created
     *            manage_user.created：创建时间
     */
    public void setCreated(Long created) {
        this.created = created;
    }

    /**
     * <pre>
     * 获取：修改时间
     * 表字段：manage_user.updated
     * </pre>
     *
     * @return manage_user.updated：修改时间
     */
    public Long getUpdated() {
        return updated;
    }

    /**
     * <pre>
     * 设置：修改时间
     * 表字段：manage_user.updated
     * </pre>
     *
     * @param updated
     *            manage_user.updated：修改时间
     */
    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    /**
     * <pre>
     * 获取：数据有效性
     * 表字段：manage_user.enabled
     * </pre>
     *
     * @return manage_user.enabled：数据有效性
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * <pre>
     * 设置：数据有效性
     * 表字段：manage_user.enabled
     * </pre>
     *
     * @param enabled
     *            manage_user.enabled：数据有效性
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}