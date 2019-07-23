package com.hoolink.manage.base.bo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @description: 用户所属信息
 * @author: WeiMin
 * @date: 2019-05-21
 **/
@Data
public class DeptPositionBO  implements Comparable<DeptPositionBO>{
    private Long id;

    private String deptName;

    /**
     * <pre>
     * 1-公司 2-部门 3-小组
     * 表字段 : manage_department.dept_type
     * </pre>
     */
    private Byte deptType;

    private Long parentId;

    private Byte encryLevelDept;

    private String parentIdCode;

    /**
     * <pre>
     * 是否最底层: 是/否
     * 表字段 : middle_user_department.lowest_level
     * </pre>
     */
    private Boolean lowestLevel;

    @Override
    public int compareTo(DeptPositionBO positionBO) {
        return this.deptName.compareTo(positionBO.getDeptName());
    }
}
