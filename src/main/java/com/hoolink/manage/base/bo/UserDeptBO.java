package com.hoolink.manage.base.bo;

import lombok.Data;

import java.util.List;

/**
 * @description: 用户所属信息
 * @author: WeiMin
 * @date: 2019-05-21
 **/
@Data
public class UserDeptBO {
    private String companyName;
    /**
     * 部门 岗级
     */
    private List<DeptPositionBO> deptPositionBOS;
}
