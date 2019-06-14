package com.hoolink.manage.base.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.hoolink.manage.base.bo.DeptTreeBO;
import com.hoolink.manage.base.bo.DictInfoBO;
import com.hoolink.manage.base.bo.DictParamBO;
import com.hoolink.manage.base.bo.LoginParamBO;
import com.hoolink.manage.base.bo.LoginResultBO;
import com.hoolink.manage.base.bo.ManagerUserInfoBO;
import com.hoolink.manage.base.bo.ManagerUserInfoParamBO;
import com.hoolink.manage.base.bo.ManagerUserPageParamBO;
import com.hoolink.manage.base.bo.ManagerUserParamBO;
import com.hoolink.manage.base.bo.PersonalInfoBO;
import com.hoolink.manage.base.bo.PhoneParamBO;
import com.hoolink.manage.base.bo.UpdatePasswdParamBO;
import com.hoolink.manage.base.bo.UserInfoBO;
import com.hoolink.manage.base.vo.req.EnableOrDisableUserParamVO;
import com.hoolink.manage.base.bo.*;
import com.hoolink.sdk.bo.base.CurrentUserBO;
import com.hoolink.sdk.bo.base.UserBO;
import com.hoolink.sdk.bo.manager.DeptSecurityRepertoryBO;
import com.hoolink.sdk.bo.manager.ManagerUserBO;
import com.hoolink.sdk.bo.manager.OrganizationInfoParamBO;
import com.hoolink.sdk.bo.manager.SimpleDeptUserBO;
import com.hoolink.sdk.bo.manager.UserDeptInfoBO;
import com.hoolink.sdk.bo.manager.*;

import java.util.List;
import java.util.Map;

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
     * @param token
     * @param isMobile
     * @return
     */
    CurrentUserBO getSessionUser(String token,boolean isMobile);

    /**
     * 重置密码
     * @param loginParam
     * @throws Exception
     */
    void resetPassword(LoginParamBO loginParam) throws Exception ;

    /**
     * 获取手机验证码
     * @param phone
     * @param flag
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
     * 根据id获取用户
     * @param id
     * @return
     */
    ManageUserInfoBO getUserInfoById(Long id);

    /**
     * 根据id集合获取用户
     * @param idList
     * @return
     */
    List<ManagerUserBO> listByIdList(List<Long> idList);

    /**
     * 员工列表(分页)
     * @param userPageParamBO
     * @return
     * @throws Exception
     */
    PageInfo<ManagerUserBO> list(ManagerUserPageParamBO userPageParamBO) throws Exception;
    
    /**
     * 根据id集合获取用户
     * @param userParamBO
     * @return
     * @throws Exception
     */
    ManagerUserInfoBO getManagerUserInfo(ManagerUserInfoParamBO userParamBO) throws Exception;
    
    /**
     * 创建用户
     * @param userBO
     * @throws Exception
     */
    void createUser(ManagerUserParamBO userBO) throws Exception;
    
    /**
     * 更新用户
     * @param userBO
     * @throws Exception
     */
    void updateUser(ManagerUserParamBO userBO) throws Exception;
    
    /**
     * 获取字典值数据
     * @param dictParamBO
     * @return
     * @throws Exception
     */
    DictInfoBO getDictInfo(DictParamBO dictParamBO) throws Exception;
    
    /**
     * 获取组织架构树
     * @param companyIdList
     * @return
     */
    List<DeptTreeBO> getDeptTree(List<Long> companyIdList);

    /**
     * 删除用户
     * @param id
     * @return
     */
    boolean removeUser(Long id);

    /**
     * 启用/禁用用户
     * @param param
     * @return
     */
    boolean enableOrDisableUser(EnableOrDisableUserParamVO param);

    /**
     * 获取个人中心基础信息
     * @return
     * @throws Exception
     */
    PersonalInfoBO getPersonalInfo() throws Exception;

    /**
     * 修改密码
     * @param updatePasswdParam
     */
    void updatePasswd(UpdatePasswdParamBO updatePasswdParam);

    /**
     * 重置手机号
     * @param userId
     */
    void resetPhone(Long userId);

    /**
     * 重置密码
     * @param userId
     */
    void resetPasswd(Long userId);

    /**
     * 个人中心-保存头像
     * @param imageId
     * @return
     */
    boolean updateImage(Long imageId);

    /**
     * excel导出列表(无分页)
     * @param userPageParamBO
     * @return
     * @throws Exception
     */
    List<ManagerUserBO> listWithOutPage(ManagerUserPageParamBO userPageParamBO) throws Exception;

    /**
     * 用户密保等级
     * @param userId
     * @return
     * @throws Exception
     */
    UserDeptInfoBO getUserSecurity(Long userId) throws Exception;

    /**
     * 根据id集合获取用户
     * @param idList
     * @return
     */
    List<ManagerUserBO> getUserList(List<Long> idList);

    /**
     * 根据用户id查询部门资源密保等级
     * @param id
     * @return
     */
    List<DeptSecurityRepertoryBO> getDeptByUser(Long id);
    /**
     * 查询组织结构id集合下的所有用户 deptIdList查询所有
     * @param deptIdList
     * @return
     */
    Map<Long, List<SimpleDeptUserBO>> mapUserByDeptIds(List<Long> deptIdList);

    /**
     * 根据用户id获取所在公司或者部门信息
     * @param paramBO
     * @return
     * @throws Exception
     */
    List<Long> getOrganizationInfo(OrganizationInfoParamBO paramBO)throws Exception;

    /**
     * 根据用户id获取所在公司的信息
     * @param paramBO
     * @return
     * @throws Exception
     */
    List<UserDeptAssociationBO> getOrgInfoToCompany(OrganizationInfoParamBO paramBO)throws Exception;
}
