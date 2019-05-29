package com.hoolink.manage.base.bo;

import com.hoolink.sdk.param.PageParam;

import lombok.Data;

/**
 * @author lijunling
 * @description
 * @date 2019/05/15 19:00
 */
@Data
public class ManagerUserPageParamBO extends PageParam{
    /**
     * 所属部门id
     */
    private Long deptId;
    
    /**
     * 所属角色id
     */
    private Long roleId;
    
    /**
     * 账号状态: 启用/禁用
     */
    private Boolean status;
    
    /**
     * 姓名、职位、手机号、账号
     */
    private String groupParam;
}
