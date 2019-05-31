package com.hoolink.manage.base.vo.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author : chenzb
 * @Description : 组织架树入参
 * @date : Created on 2019/5/30 20:16
 */
@Data
public class DepartmentTreeParamVO {

    /** 层级参数 1-公司 2-部门 3-小组 */
    @NotNull(message = "组织架构层级不能为空")
    private Byte deptType;

}
