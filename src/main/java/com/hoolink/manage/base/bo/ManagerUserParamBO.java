package com.hoolink.manage.base.bo;
import java.util.List;

import lombok.Data;

/**
 * @author lijunling
 * @description
 * @date 2019/05/15 19:00
 */
@Data
public class ManagerUserParamBO {
	/**
	 * 主键
	 */
	private Long id;
	
    /**
     * 用户账号
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
     * 性别: 1/0 男/女
     */
    private Boolean sex;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 用户头像ID
     */
    private Long imgId;

    /**
     * 职位
     */
    private String position;

    /**
     * 资源库密保等级（1-一级,2-二级,3-三级,4-四级）
     */
    private Integer encryLevelCompany;

    /**
     * 用户状态: 启用/禁用
     */
    private Boolean status;
    
    /**
     * 数据有效性
     */
    private Boolean enabled;

    /**
     * 是否接收提醒短信
     */
    private Boolean receiveSms;

    /**
     * 用户部门关系
     */
    private List<UserDeptPairParamBO> userDeptPairParamList;
    
    
}

