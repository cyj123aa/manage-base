package com.hoolink.manage.base.vo.req;

import com.hoolink.manage.base.vo.ManagerBaseGroup;
import lombok.Data;

import javax.annotation.RegEx;
import javax.validation.constraints.NotBlank;

/**
 * @Author: xuli
 * @Date: 2019/4/23 17:42
 */
@Data
public class LoginParamVO {

    /**
     * 账号
     */
    @NotBlank(
            message = "账号不允许为空",
            groups = {ManagerBaseGroup.UserLogin.class,ManagerBaseGroup.ForgetPassword.class}
    )
    private String account;

    /**
     * 密码
     */
    @NotBlank(
            message = "密码不允许为空",
            groups = {ManagerBaseGroup.UserLogin.class}
    )
    private String passwd;

    /**
     * 旧密码
     */
    private String oldPasswd;

    /**
     * 手机验证码
     */
    private String code;

}
