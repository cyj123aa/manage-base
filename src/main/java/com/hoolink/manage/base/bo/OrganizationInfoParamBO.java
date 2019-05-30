package com.hoolink.manage.base.bo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author : chenzb
 * @Description : TODO
 * @date : Created on 2019/5/30 16:20
 */
@Data
public class OrganizationInfoParamBO {

    /*** 当前用户id */
    @NotNull(message = "用户id不能为空")
    private Long userId;

    /*** 获取某一层级 */
    @NotNull(message = "部门层级type不能为空")
    private Byte deptType;
}
