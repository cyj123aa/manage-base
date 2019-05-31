package com.hoolink.manage.base.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: xuli
 * @Date: 2019/4/23 17:20
 */
@Data
public class LoginResultBO implements Serializable {

    private static final long serialVersionUID = 410702869003930490L;
    /**
     * 用户token
     */
    private String token;
    /**
     * 是否是第一次登录
     */
    private Boolean firstLogin;
    /**
     * 手机号
     */
    private String phone;

    /**
     * EDM系统权限
     */
    private Boolean accessEDM;
    /**
     * hoolink系统权限
     */
    private Boolean accessHoolink;

    /**
     * 问候语
     */
    private String greetings;

    /**
     * 用户名
     */
    private String name;

    /**
     * 图像url
     */
    private String image;
}
