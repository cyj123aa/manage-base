package com.hoolink.manage.base.vo.req;

import java.util.List;

import com.jw.sdk.param.PageParam;

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
     * 所属部门id
     */
    private List<Long> deptId;
    
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
