package com.hoolink.manage.base.vo.res;

import com.hoolink.sdk.bo.manager.SimpleDeptUserBO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: tonghao
 * @Date: 2019/5/29 19:29
 */
@Data
public class ManageDepartmentTreeVO implements Serializable {

    private static final long serialVersionUID = 7728059630076949882L;
    /** 组织架构id */
    private Long id;

    /** 组织架构名称 */
    private String name;

    /** 组织类型 */
    private Byte deptType;

    /** 父节点id */
    private Long parentId;

    /*** 组织架构下的子节点*/
    private List<ManageDepartmentTreeVO> childTreeList;

    /** 组织架构下的人员 */
    private List<SimpleDeptUserVO> userList;
}
