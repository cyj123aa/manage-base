package com.hoolink.manage.base.bo;

import lombok.Data;

/**
 * @Author: xuli
 * @Date: 2019/4/26 17:44
 */
@Data
public class PhoneParamBO {

    /**
     * 手机号
     */
    private String phone;

    /**
     * 手机验证码
     */
    private String code;
}
