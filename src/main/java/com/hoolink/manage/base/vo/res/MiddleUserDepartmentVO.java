package com.hoolink.manage.base.vo.res;

import lombok.Data;

/**
 * 
 * @author lijunling
 *
 * @date 2019/05/27 14:40
 */
@Data
public class MiddleUserDepartmentVO {
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
}
