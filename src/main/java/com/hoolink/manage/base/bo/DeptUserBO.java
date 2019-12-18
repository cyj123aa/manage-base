package com.hoolink.manage.base.bo;

import com.hoolink.sdk.bo.base.UserBO;
import lombok.Data;

import java.util.List;

/**
 * 组织架构下的用户
 * @Author: tonghao
 * @Date: 2019/5/30 9:25
 */
@Data
public class DeptUserBO {

    /** 组织架构id */
    private Long id;

    /** 组织架构名称 */
    private String deptName;

    /** 部门密保等级 */
    private Integer encryLevelDept;

    /** 该组织架构下的用户 */
    private List<UserBO> userList;
}
