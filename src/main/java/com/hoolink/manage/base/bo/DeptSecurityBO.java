package com.hoolink.manage.base.bo;

import lombok.Data;
import org.hibernate.validator.constraints.SafeHtml;

/**
 * @description: 部门安全等级联系
 * @author: WeiMin
 * @date: 2019-05-24
 **/
@Data
public class DeptSecurityBO {
    /**
     * 部门 岗级
     */
    private Long id;
    /**
     *  2-部门 3-小组
     */
    private Byte deptType;
    /**
     * 部门密保等级
     */
    private Integer encryLevelDept;

    private Long parentId;

    /**
     * <pre>
     * 是否最底层: 是/否
     * 表字段 : middle_user_department.lowest_level
     * </pre>
     */
    private Boolean lowestLevel;

}
