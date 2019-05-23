package com.hoolink.manage.base.service;

import com.github.pagehelper.PageInfo;
import com.hoolink.manage.base.bo.DictInfoBO;
import com.hoolink.manage.base.bo.DictParamBO;
import com.hoolink.manage.base.bo.LoginParamBO;
import com.hoolink.manage.base.bo.LoginResultBO;
import com.hoolink.manage.base.bo.ManagerUserInfoBO;
import com.hoolink.manage.base.bo.ManagerUserInfoParamBO;
import com.hoolink.manage.base.bo.ManagerUserPageParamBO;
import com.hoolink.manage.base.bo.ManagerUserParamBO;
import com.hoolink.manage.base.bo.PhoneParamBO;
import com.hoolink.manage.base.bo.UserInfoBO;
import com.hoolink.sdk.bo.base.CurrentUserBO;
import com.hoolink.sdk.bo.base.UserBO;
import com.hoolink.sdk.bo.manager.ManagerUserBO;
import com.hoolink.sdk.bo.manager.UserDeptInfoBO;

/**
 * @Author: xuli
 * @Date: 2019/4/15 19:24
 */
public interface UserService {
    /**
     * 用户登录
     *
     * @param loginParam
     * @return
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
     * @param
     * @return
     */
    void resetPassword(LoginParamBO loginParam) throws Exception ;

    /**
     * 获取手机验证码
     * @param phone
     * @return
     */
    String getPhoneCode(String phone) throws Exception;

    /**
     * 验证手机验证码
     * @param phoneParam
     * @throws Exception
     */
    void verifyPhone(PhoneParamBO phoneParam)throws Exception;

    /**
     * 绑定手机号
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
     * @param
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
     * 获取用户列表（带分页）
     * @param userPageParamBO
     * @return
     * @throws Exception
     */
    PageInfo<ManagerUserBO> list(ManagerUserPageParamBO userPageParamBO) throws Exception;
    
    /**
     * 获取用户基础信息
     * @param userParamBO
     * @return
     * @throws Exception
     */
    ManagerUserInfoBO getManagerUserInfo(ManagerUserInfoParamBO userParamBO) throws Exception;
    
    /**
     * 创建用户
     * @param userBO
     * @return
     * @throws Exception
     */
    boolean createUser(ManagerUserParamBO userBO) throws Exception;
    
    /**
     * 更新用户
     * @param userBO
     * @return
     * @throws Exception
     */
    boolean updateUser(ManagerUserParamBO userBO) throws Exception;
    
    /**
     * 获取字典值数据
     * @param dictParamBO
     * @return
     * @throws Exception
     */
    DictInfoBO getDictInfo(DictParamBO dictParamBO) throws Exception;
    
    /**
     * 根据id获取用户
     * @param id
     * @return
     */
    ManagerUserBO getById(Long id);

    /**
     * 用户密保等级
     * @param userId
     * @return
     * @throws Exception
     */
    UserDeptInfoBO getUserSecurity(Long userId) throws Exception;
}
