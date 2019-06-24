package com.hoolink.manage.base.bo;

import lombok.Data;

import java.util.List;

/**
 * @description: 用户安全等级信息
 * @author: WeiMin
 * @date: 2019-05-24
 **/
@Data
public class UserSecurityBO {
    /**
     * userId
     */
    private Long id;

    /**
     * 资源库密保等级
     */
    private Integer encryLevelCompany;
    /**
     * 部门安全等级 岗级
     */
    private List<DeptSecurityBO> list;
}
