package com.hoolink.manage.base.vo.res;

import lombok.Data;

/**
 * @Author: xuli
 * @Date: 2019/4/29 17:11
 */
@Data
public class UserInfoVO {

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
