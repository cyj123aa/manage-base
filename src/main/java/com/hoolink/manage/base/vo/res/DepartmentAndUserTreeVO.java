package com.hoolink.manage.base.vo.res;

import lombok.Data;

import java.util.List;

/**
 * 带有用户的组织架构树
 * @Author: tonghao
 * @Date: 2019/6/11 9:59
 */
@Data
public class DepartmentAndUserTreeVO {

    /** 节点id */
    private Long key;

    /** 节点名称 */
    private String title;

    /** 节点id */
    private Long value;

    /** 是否展开子节点 */
    private Boolean expand;

    /** 是否勾选 */
    private Boolean checked;

    /** 数据类型 1组织架构 0用户 */
    private Integer type;

    /** 子节点 */
    private List<DepartmentAndUserTreeVO> children;
}
