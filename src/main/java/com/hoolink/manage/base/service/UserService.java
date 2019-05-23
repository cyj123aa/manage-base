package com.hoolink.manage.base.service;

import java.util.List;

import com.hoolink.manage.base.bo.LoginParamBO;
import com.hoolink.manage.base.bo.LoginResultBO;
import com.hoolink.manage.base.bo.PhoneParamBO;
import com.hoolink.manage.base.bo.UserInfoBO;
import com.hoolink.sdk.bo.base.CurrentUserBO;
import com.hoolink.sdk.bo.base.UserBO;
import com.hoolink.sdk.bo.manager.ManagerUserBO;

/**
 * @Author: xuli
 * @Date: 2019/4/15 19:24
 */
public interface UserService {

    /**
     * 用户登录
     * @param loginParam
     * @return
     * @throws Exception
     */
    LoginResultBO login(LoginParamBO loginParam) throws Exception;

    /**
     * 用户退出
     */
    void logout();

    /**
     * 根据token获取当前用户，该接口仅供网关鉴权使用
     *
     * @param token
     * @return
     */
    CurrentUserBO getSessionUser(String token);

    /**
     * 重置密码
     * @param loginParam
     * @throws Exception
     */
    void resetPassword(LoginParamBO loginParam) throws Exception ;

    /**
     * 获取手机验证码
     * @param phone
     * @return
     * @throws Exception
     */
    String getPhoneCode(String phone,Boolean flag) throws Exception;

    /**
     * 验证手机验证码
     * @param phoneParam
     * @throws Exception
     */
    void verifyPhone(PhoneParamBO phoneParam)throws Exception;

    /**
     * 绑定手机号
     * @param bindPhoneParam
     * @throws Exception
     */
    void bindPhone(PhoneParamBO bindPhoneParam) throws Exception;

    /**
     * 忘记密码
     * @param loginParam
     * @return
     * @throws Exception
     */
    String forgetPassword(LoginParamBO loginParam) throws Exception;

    /**
     * 获取用户信息
     * @return
     * @throws Exception
     */
    UserInfoBO getUserInfo() throws Exception;

    /**
     * 修改手机号
     * @param phoneParam
     * @throws Exception
     */
    void updatePhone(PhoneParamBO phoneParam) throws Exception;

    /**
     * 获取用户信息
     * @return
     * @throws Exception
     */
    UserBO getUser() throws Exception;
    
    /**
     * 根据id获取用户
     * @param id
     * @return
     */
    ManagerUserBO getById(Long id);
    
    /**
     * 根据id集合获取用户
     * @param idList
     * @return
     */
    List<ManagerUserBO> listByIdList(List<Long> idList);
}
