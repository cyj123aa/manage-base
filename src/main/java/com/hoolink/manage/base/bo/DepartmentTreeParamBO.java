package com.hoolink.manage.base.bo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author : chenzb
 * @Description : TODO
 * @date : Created on 2019/5/30 20:16
 */
@Data
public class DepartmentTreeParamBO {

    /** 层级参数 1-公司 2-部门 3-小组 */
    @NotNull(message = "组织架构层级不能为空")
    private Byte deptType;

    /** 是否需要查询架构下人员标识 */
    @NotNull(message = "是否需要查询组织架构的人员标识不能为空")
    private Boolean flag;
}
