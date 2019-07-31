package com.hoolink.manage.base.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: xuli
 * @Date: 2019/7/25 11:01
 */
@Data
public class MobileUpdateParamBO {

    @NotBlank(
            message = "原密码不能为空"
    )
    private String oldPassword;

    @NotBlank(
            message = "新密码不能为空"
    )
    private String newPassword;
}
