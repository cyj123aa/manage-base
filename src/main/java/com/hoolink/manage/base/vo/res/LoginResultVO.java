package com.hoolink.manage.base.vo.res;

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
    private boolean accessEDM;
    /**
     * hoolink系统权限
     */
    private boolean accessHoolink;

    /**
     * 问候语
     */
    private String greetings;
}
