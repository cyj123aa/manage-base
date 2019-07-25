package com.hoolink.manage.base.vo.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: xuli
 * @Date: 2019/7/25 10:56
 */
@Data
public class MobileUpdateParamVO {

    @NotBlank(
            message = "原密码不能为空"
    )
    private String oldPassword;

    @NotBlank(
            message = "新密码不能为空"
    )
    private String newPassword;
}
