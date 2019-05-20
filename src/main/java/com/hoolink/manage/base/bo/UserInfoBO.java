package com.hoolink.manage.base.bo;

import lombok.Data;

/**
 * @Author: xuli
 * @Date: 2019/4/29 17:04
 */
@Data
public class UserInfoBO {

    /**
     * 姓名
     */
    private String  userName;

    /**
     * 电话
     */
    private String phone;

    /**
     * 角色
     */
    private String roleName;
}
