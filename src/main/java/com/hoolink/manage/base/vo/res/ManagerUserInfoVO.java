package com.hoolink.manage.base.vo.res;

import java.util.List;

import lombok.Data;

/**
 * @author lijunling
 * @description
 * @date 2019/05/15 18:55
 */
@Data
public class ManagerUserInfoVO {
	/**
	 * 用户id
	 */
	private Long id;
    /**
     * 账号
     */
    private String userAccount;
    
    /**
     * 员工编号
     */
    private String userNo;
	
    /**
     * 姓名
     */
    private String name;
    
    /**
     * 性别(1/0 男/女)
     */
    private Boolean sex;
    
    /**
     * 用户头像id
     */
    private Long imgId;
    
    /**
     * 用户头像url
     */
    private String imgUrl;
    
    /**
     * 职位
     */
    private String position;
    
    /**
     * 所属角色id
     */
    private Long roleId;
    
    /**
     * 所属角色
     */
    private String roleName;
    
    /**
     * 所属公司Id
     */
    private Long companyId;
    
    /**
     * 所属公司
     */
    private String companyName;
    
    /**
     * 联系电话
     */
    private String phone;

    /**
     * 资源库密保等级（1-一级,2-二级,3-三级,4-四级）
     */
    private Integer encryLevelCompany;
    
    /**
     * 账号状态: 启用/禁用
     */
    private Boolean status;
    
    /**
     * 用户是否登陆过
     */
    private Boolean hasLoginYet;
    /**
     * 用户组织关系
     */
    private List<DeptPairVO> userDeptPairList;

    /**
     * 是否接收提醒
     */
    private Boolean receiveSms;
}
