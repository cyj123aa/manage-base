package com.hoolink.manage.base.bo;

import lombok.Data;

/**
 * @author lijunling
 * @description
 * @date 2019/05/15 19:00
 */
@Data
public class MiddleUserDepartmentBO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户表ID
     */
    private Long userId;

    /**
     * 部门表ID
     */
    private Long deptId;

    /**
     * 部门密保等级(1-一级,2-二级,3-三级,4-四级)
     */
    private Integer encryLevelDept;
    
    /**
     * 同一用户不同部门区分标识
     */
    private String diffDeptGroup;
    
    /**
     * 选择某组织，会推导并保存其所有子组织，状态: 选中的/推导的
     */
    private Boolean deduceStatus;
}
