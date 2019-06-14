package com.hoolink.manage.base.bo;

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

}
