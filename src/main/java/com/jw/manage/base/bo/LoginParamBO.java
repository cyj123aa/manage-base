package com.jw.manage.base.bo;

import lombok.Data;

/**
 * @Author: xuli
 * @Date: 2019/4/23 17:45
 */
@Data
public class LoginParamBO {

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
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
