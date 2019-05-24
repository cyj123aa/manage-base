package com.hoolink.manage.base.bo;

import lombok.Data;

/**
 * 
 * @author lijunling
 *
 * @date 2019/05/23 20:08
 */
@Data
public class UserDeptPairParamBO {
    /**
     * 部门表ID
     */
    private Long deptId;

    /**
     * 部门密保等级(1-一级,2-二级,3-三级,4-四级)
     */
    private Integer encryLevelDept;
}
