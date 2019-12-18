package com.hoolink.manage.base.service;

import com.hoolink.manage.base.bo.LoginParamBO;
import com.hoolink.manage.base.bo.LoginResultBO;
import com.hoolink.manage.base.bo.ManagerUserParamBO;
import com.hoolink.manage.base.dao.model.User;
import com.jw.sdk.bo.base.CurrentUserBO;

/**
 * @Author: chenyuejun
 * @Date: 2019/10/17 19:24
 */
public interface UserService {

    /**
     * 用户登录
     */
    LoginResultBO login(LoginParamBO loginParam, Boolean isMobile) throws Exception;

    /**
     * 用户退出
     */
    void logout();

    /**
     * 移动端退出
     */
    void mobileLogout();

    /**
     * 根据token获取当前用户，该接口仅供网关鉴权使用
     */
    CurrentUserBO getSessionUser(String token, boolean isMobile);



    /**
     * 创建用户
     */
    void createUser(ManagerUserParamBO userBO) throws Exception;


    /**
     * 缓存当前用户
     *
     * @param resetToken 是否需要重置token
     */
    String cacheSession(User user, Boolean isMobile, Boolean resetToken) throws Exception;




}
