package com.jw.manage.base.vo.res;


import lombok.Data;

/**
 * @Author: xuli
 * @Date: 2019/4/23 17:41
 */
@Data
public class LoginResultVO {

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

    /**
     * 角色名称
     */
    private String roleName;

    private Byte roleLevel;
}
