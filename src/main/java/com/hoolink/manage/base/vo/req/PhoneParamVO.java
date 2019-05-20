package com.hoolink.manage.base.vo.req;

import com.hoolink.manage.base.vo.ManagerBaseGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: xuli
 * @Date: 2019/4/26 17:39
 */
@Data
public class PhoneParamVO {


    /**
     * 手机号
     */
    @NotBlank(
            message = "手机号不能为空"
    )
    private String phone;

    /**
     * 手机验证码
     */
    @NotBlank(
            message = "手机验证码不能为空"
    )
    private String code;
}
