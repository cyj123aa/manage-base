package com.hoolink.manage.base.bo;

import java.util.List;

import lombok.Data;

@Data
public class UserInfoDeptTreeBO{
    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 组织架构类型:1-公司 2-部门 3-小组
     */
    private Byte deptType;

    /**
     * 子节点
     */
    private List<DeptTreeBO> children;
    
    /**
     * 是否选中
     */
    private boolean checked;
    
    /**
	 * 部门密保等级(1-一级,2-二级,3-三级,4-四级)
	 */
	private Integer encryLevelDept;
}
