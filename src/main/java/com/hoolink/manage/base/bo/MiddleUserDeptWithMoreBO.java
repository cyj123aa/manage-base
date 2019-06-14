package com.hoolink.manage.base.bo;

import lombok.Data;

/**
 * 
 * @author lijunling
 *
 * @date 2019/05/23 15:55
 */
@Data
public class MiddleUserDeptWithMoreBO extends MiddleUserDepartmentBO{
    /**
     * 组织类型:1-公司 2-部门 3-小组
     */
    private Byte deptType;
    
	/**
	 * 部门ID
	 */
	private Long deptId;
	
    /**
     * 所属部门
     */
    private String deptName;
    
	/**
	 * 部门密保等级(1-一级,2-二级,3-三级,4-四级)
	 */
	private Integer encryLevelDept;
	
    /**
     * 部门密保等级
     */
    private String encryLevelDeptName;
    
}
