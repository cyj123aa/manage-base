package com.hoolink.manage.base.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: tonghao
 * @Date: 2019/5/29 19:29
 */
@Data
public class ManageDepartmentTreeBO implements Serializable {

    private static final long serialVersionUID = 7728059630076949882L;
    /** 组织架构id */
    private Long id;

    /** 组织架构名称 */
    private String name;

    /** 组织类型 */
    private Byte deptType;

    /** 父节点id */
    private Long parentId;

    private List<SimpleDeptUserBO> userList;

    private List<ManageDepartmentTreeBO> childTreeList;
}
