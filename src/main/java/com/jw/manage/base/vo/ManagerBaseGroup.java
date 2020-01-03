package com.jw.manage.base.vo;

/**
 * @Author: xuli
 * @Date: 2019/4/18 16:25
 */
public interface ManagerBaseGroup {

    /**
     * 登录用户校验分组ManagerBaseGroup.forgetPassword
     */
    interface UserLogin{}
    interface ForgetPassword{}

    /**
     *用户分组
     */
    interface CreateUser{}
    interface UpdateUser{}

}
