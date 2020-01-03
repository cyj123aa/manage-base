package com.jw.manage.base.service;

import com.jw.manage.base.bo.LoginParamBO;
import com.jw.manage.base.bo.LoginResultBO;
import com.jw.manage.base.bo.ManagerUserParamBO;
import com.jw.manage.base.dao.model.User;
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
     * 缓存当前用户
     *
     * @param resetToken 是否需要重置token
     */
    String cacheSession(User user, Boolean isMobile, Boolean resetToken) throws Exception;

    /**
     * 测试接口
     * @return
     */
   String jiuWenTest();


}
