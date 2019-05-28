package com.hoolink.manage.base.vo.res;

import java.util.List;

import lombok.Data;

/**
 * @author lijunling
 *
 * @date 2019/05/22 20:21
 */
@Data
public class DeptTreeVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 组织架构类型:1-公司 2-部门 3-小组
     */
    private Byte deptType;

    /**
     * 子节点
     */
    private List<DeptTreeVO> children;
}
