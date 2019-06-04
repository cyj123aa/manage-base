package com.hoolink.manage.base.vo.res;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: tonghao
 * @Date: 2019/5/30 10:24
 */
@Data
public class SimpleDeptUserVO implements Serializable {

    private static final long serialVersionUID = -8858464549427029393L;
    /*** 用户ID */
    private Long id;

    /*** 用户账号 */
    private String userAccount;

    /** 员工编号 */
    private String userNo;

    /*** 用户名称 */
    private String userName;

    /*** 角色ID */
    private Long roleId;

    /** 用户手机号 */
    private String phone;

    /** 职位 */
    private String position;

    /** 资源库密保等级（1-一级,2-二级,3-三级,4-四级）*/
    private Integer encryLevelCompany;

    /** 是否可见员工密保等级 */
    private Boolean viewEncryLevelPermitted;

    /** 用户所属组织结构 */
    private Long deptId;
}
