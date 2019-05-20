package com.hoolink.manage.base.vo.req;

import com.hoolink.sdk.param.PageParam;

import lombok.Data;

/**
 * 
 * @author lijunling
 *
 * @date 2019/05/18 16:14
 */
@Data
public class ManagerUserPageParamVO extends PageParam{
    /**
     * 姓名
     */
    private String name;
    
    /**
     * 职位
     */
    private String position;
    
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
     * 联系电话
     */
    private String phone;
    
    /**
     * 账号
     */
    private String userAccount;
}
