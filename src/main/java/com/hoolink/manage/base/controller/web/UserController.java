package com.hoolink.manage.base.controller.web;

import com.hoolink.manage.base.bo.PhoneParamBO;
import com.hoolink.manage.base.bo.ManagerUserPageParamBO;
import com.hoolink.manage.base.bo.ManagerUserParamBO;
import com.hoolink.manage.base.bo.ManagerUserInfoParamBO;
import com.github.pagehelper.PageInfo;
import com.hoolink.manage.base.bo.DictParamBO;
import com.hoolink.manage.base.bo.LoginParamBO;
import com.hoolink.manage.base.service.UserService;
import com.hoolink.manage.base.util.ValidatorUtil;
import com.hoolink.manage.base.vo.ManagerBaseGroup;
import com.hoolink.manage.base.vo.req.PhoneParamVO;
import com.hoolink.manage.base.vo.req.ManagerUserPageParamVO;
import com.hoolink.manage.base.vo.req.ManagerUserParamVO;
import com.hoolink.manage.base.vo.req.ManagerUserInfoParamVO;
import com.hoolink.manage.base.vo.req.DictParamVO;
import com.hoolink.manage.base.vo.req.EnableOrDisableUserParamVO;
import com.hoolink.manage.base.vo.req.LoginParamVO;
import com.hoolink.manage.base.vo.res.AccessToEDMOrHoolinkVO;
import com.hoolink.manage.base.vo.res.DeptTreeVO;
import com.hoolink.manage.base.vo.res.DictInfoVO;
import com.hoolink.manage.base.vo.res.LoginResultVO;
import com.hoolink.manage.base.vo.res.ManagerUserInfoVO;
import com.hoolink.manage.base.vo.res.ManagerUserVO;
import com.hoolink.manage.base.vo.res.UserInfoVO;
import com.hoolink.sdk.annotation.LogAndParam;
import com.hoolink.sdk.bo.base.CurrentUserBO;
import com.hoolink.sdk.enums.CheckEnum;
import com.hoolink.sdk.param.BaseParam;
import com.hoolink.sdk.utils.BackVOUtil;
import com.hoolink.sdk.utils.CopyPropertiesUtil;
import com.hoolink.sdk.vo.BackVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @Author: xuli
 * @Date: 2019/4/15 19:23
 */
@RestController
@RequestMapping(value = "/web/user/")
@RestSchema(schemaId = "userController")
public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping(value = "login")
    @ApiOperation(value = "用户登录")
    @LogAndParam(value = "用户登录失败，请稍后重试")
    public BackVO<LoginResultVO> login(@RequestBody LoginParamVO loginParam)throws Exception {
        BackVO validateParameter = ValidatorUtil.validateParameter(loginParam, ManagerBaseGroup.UserLogin.class);
        if (validateParameter != null) {
            return validateParameter;
        }
        //VO转BO
        LoginParamBO paramBO = CopyPropertiesUtil.copyBean(loginParam, LoginParamBO.class);
        //BO转VO
        LoginResultVO loginResultVO= CopyPropertiesUtil.copyBean(userService.login(paramBO),LoginResultVO.class);
        return BackVOUtil.operateAccess(loginResultVO);
    }

    @PostMapping(value = "logout")
    @ApiOperation(value = "用户退出")
    @LogAndParam(value = "用户退出失败，请稍后重试")
    public BackVO<Void> logout() {
        userService.logout();
        return BackVOUtil.operateAccess();
    }

    @PostMapping(value = "resetPassword")
    @ApiOperation(value = "重置密码")
    @LogAndParam(value = "重置密码失败，请稍后重试")
    public BackVO<Void> resetPassword(@RequestBody LoginParamVO loginParam)throws Exception  {
        BackVO validateParameter = ValidatorUtil.validateParameter(loginParam, ManagerBaseGroup.UserLogin.class);
        if (validateParameter != null) {
            return validateParameter;
        }
        //VO转BO
        LoginParamBO paramBO = CopyPropertiesUtil.copyBean(loginParam, LoginParamBO.class);
        userService.resetPassword(paramBO);
        return BackVOUtil.operateAccess();
    }

    @PostMapping(value = "getPhoneCode")
    @ApiOperation(value = "获取手机验证码")
    @LogAndParam(value = "获取手机验证码失败，请稍后重试")
    public BackVO<String> getPhoneCode(@RequestBody BaseParam<String> newPassword)throws Exception  {
        return BackVOUtil.operateAccess(userService.getPhoneCode(newPassword.getData()));
    }

    @PostMapping(value = "verifyPhoneCode")
    @ApiOperation(value = "验证手机验证码")
    @LogAndParam(value = "验证手机验证码失败，请稍后重试")
    public BackVO<String> verifyPhoneCode(@RequestBody PhoneParamVO phoneParam)throws Exception  {
        BackVO validateParameter = ValidatorUtil.validateParameter(phoneParam);
        if (validateParameter != null) {
            return validateParameter;
        }
        PhoneParamBO bindPhone=CopyPropertiesUtil.copyBean(phoneParam, PhoneParamBO.class);
        userService.verifyPhone(bindPhone);
        return BackVOUtil.operateAccess();
    }

    @PostMapping(value = "bindPhone")
    @ApiOperation(value = "绑定手机号")
    @LogAndParam(value = "绑定手机号失败，请稍后重试")
    public BackVO<Void> bindPhone(@RequestBody PhoneParamVO bindPhoneParam)throws Exception  {
        BackVO validateParameter = ValidatorUtil.validateParameter(bindPhoneParam);
        if (validateParameter != null) {
            return validateParameter;
        }
        PhoneParamBO bindPhone=CopyPropertiesUtil.copyBean(bindPhoneParam, PhoneParamBO.class);
        userService.bindPhone(bindPhone);
        return BackVOUtil.operateAccess();
    }

    @PostMapping(value = "forgetPassword")
    @ApiOperation(value = "忘记密码")
    @LogAndParam(value = "忘记密码操作失败，请稍后重试")
    public BackVO<String> forgetPassword(@RequestBody LoginParamVO loginParam)throws Exception  {
        BackVO validateParameter = ValidatorUtil.validateParameter(loginParam, ManagerBaseGroup.ForgetPassword.class);
        if (validateParameter != null) {
            return validateParameter;
        }
        //VO转BO
        LoginParamBO paramBO = CopyPropertiesUtil.copyBean(loginParam, LoginParamBO.class);
        return BackVOUtil.operateAccess(userService.forgetPassword(paramBO));
    }

    @PostMapping(value = "getUserInfo")
    @ApiOperation(value = "获取用户信息")
    @LogAndParam(value = "获取用户信息失败，请稍后重试")
    public BackVO<UserInfoVO> getUserInfo()throws Exception  {
        return BackVOUtil.operateAccess(CopyPropertiesUtil.copyBean(userService.getUserInfo(), UserInfoVO.class));
    }

    @PostMapping(value = "updatePhone")
    @ApiOperation(value = "修改手机号")
    @LogAndParam(value = "修改手机号失败，请稍后重试")
    public BackVO<Void> updatePhone(@RequestBody PhoneParamVO phoneParam)throws Exception  {
        BackVO validateParameter = ValidatorUtil.validateParameter(phoneParam);
        if (validateParameter != null) {
            return validateParameter;
        }
        PhoneParamBO bindPhone=CopyPropertiesUtil.copyBean(phoneParam, PhoneParamBO.class);
        userService.updatePhone(bindPhone);
        return BackVOUtil.operateAccess();
    }

    @PostMapping(value = "getSessionUser")
    @ApiOperation(value = "查询Session用户", notes = "该接口主要提供网关鉴权使用，其他场景不要使用")
    @LogAndParam(value = "查询Session用户失败")
    public CurrentUserBO getSessionUser(@RequestBody @NotNull(message = "Token不允许传入空") String token) {
        return userService.getSessionUser(token);
    }
    
    @PostMapping(value = "list")
    @ApiOperation(value = "获取用户列表带分页")
    @LogAndParam(value = "获取用户列表失败", check = CheckEnum.DATA_NOT_NULL)
    public BackVO<PageInfo<ManagerUserVO>> list(@RequestBody ManagerUserPageParamVO userPageParamVO) throws Exception{
    	ManagerUserPageParamBO userPageParamBO = CopyPropertiesUtil.copyBean(userPageParamVO, ManagerUserPageParamBO.class);
    	return BackVOUtil.operateAccess(CopyPropertiesUtil.copyPageInfo(userService.list(userPageParamBO), ManagerUserVO.class));
    }
    
    @PostMapping(value = "getManagerUserInfo")
    @ApiOperation(value = "获取用户基础信息")
    @LogAndParam(value = "获取用户基础信息失败", check = CheckEnum.DATA_NOT_NULL)
    public BackVO<ManagerUserInfoVO> getManagerUserInfo(@RequestBody ManagerUserInfoParamVO userParamVO) throws Exception{
        BackVO validateParameter = ValidatorUtil.validateParameter(userParamVO);
        if (validateParameter != null) {
            return validateParameter;
        }
    	ManagerUserInfoParamBO userParamBO = CopyPropertiesUtil.copyBean(userParamVO, ManagerUserInfoParamBO.class);
    	return BackVOUtil.operateAccess(CopyPropertiesUtil.copyBean(userService.getManagerUserInfo(userParamBO), ManagerUserInfoVO.class));
    }
    
    @PostMapping(value = "createUser")
    @ApiOperation(value = "创建用户")
    @LogAndParam(value = "创建用户失败", check = CheckEnum.DATA_NOT_NULL)
    public BackVO<Void> createUser(@RequestBody ManagerUserParamVO userVO) throws Exception{
        BackVO validateParameter = ValidatorUtil.validateParameter(userVO, ManagerBaseGroup.CreateUser.class);
        if (validateParameter != null) {
            return validateParameter;
        }
    	userService.createUser(CopyPropertiesUtil.copyBean(userVO, ManagerUserParamBO.class));
    	return BackVOUtil.operateAccess();
    }
    
    @PostMapping(value = "updateUser")
    @ApiOperation(value = "更新用户")
    @LogAndParam(value = "更新用户失败", check = CheckEnum.DATA_NOT_NULL)
    public BackVO<Void> updateUser(@RequestBody ManagerUserParamVO userVO) throws Exception{
        BackVO validateParameter = ValidatorUtil.validateParameter(userVO, ManagerBaseGroup.UpdateUser.class);
        if (validateParameter != null) {
            return validateParameter;
        }
    	userService.updateUser(CopyPropertiesUtil.copyBean(userVO, ManagerUserParamBO.class));
    	return BackVOUtil.operateAccess();
    }
    
    @PostMapping(value = "removeUser")
    @ApiOperation(value = "删除用户")
    @LogAndParam(value = "删除用户失败", check = CheckEnum.DATA_NOT_NULL)
    public BackVO<Void> removeUser(@RequestBody BaseParam<Long> param) throws Exception{
    	userService.removeUser(param.getData());
    	return BackVOUtil.operateAccess();
    }
    
    @PostMapping(value = "enableOrDisableUser")
    @ApiOperation(value = "启用/禁用用户")
    @LogAndParam(value = "启用/禁用用户失败", check = CheckEnum.DATA_NOT_NULL)
    public BackVO<Void> enableOrDisableUser(@RequestBody EnableOrDisableUserParamVO param) throws Exception{
        BackVO validateParameter = ValidatorUtil.validateParameter(param);
        if (validateParameter != null) {
            return validateParameter;
        }
    	userService.enableOrDisableUser(param);
    	return BackVOUtil.operateAccess();
    }
    
    @PostMapping(value = "getDictInfo")
    @ApiOperation(value = "获取字典值数据")
    @LogAndParam(value = "获取字典值数据失败", check = CheckEnum.DATA_NOT_NULL)
    public BackVO<DictInfoVO> getDictInfo(@RequestBody DictParamVO dictParamVO) throws Exception{
        BackVO validateParameter = ValidatorUtil.validateParameter(dictParamVO);
        if (validateParameter != null) {
            return validateParameter;
        }
    	DictParamBO dictParamBO = CopyPropertiesUtil.copyBean(dictParamVO, DictParamBO.class);
    	return BackVOUtil.operateAccess(CopyPropertiesUtil.copyBean(userService.getDictInfo(dictParamBO), DictInfoVO.class));
    }
    
    @PostMapping(value = "getDeptTree")
    @ApiOperation(value = "获取组织架构tree数据")
    @LogAndParam(value = "获取组织架构tree数据失败", check = CheckEnum.DATA_NOT_NULL)
    public BackVO<List<DeptTreeVO>> getDeptTree(@RequestBody BaseParam<Long> companyId) throws Exception{
    	return BackVOUtil.operateAccess(CopyPropertiesUtil.copyList(userService.getDeptTree(Arrays.asList(companyId.getData())), DeptTreeVO.class));
    }
    
    @PostMapping(value = "exportList")
    @ApiOperation(value = "导出用户列表")
    @LogAndParam(value = "导出用户列表失败")
    @ApiResponses({@ApiResponse(code = 200, response = File.class, message = "导出列表"),})
    public ResponseEntity<Resource> exportByParam(@RequestBody ManagerUserPageParamVO userPageParamVO) throws Exception {
    	ManagerUserPageParamBO userPageParamBO = CopyPropertiesUtil.copyBean(userPageParamVO, ManagerUserPageParamBO.class);
        return userService.exportList(userPageParamBO);
    }
    
    @PostMapping(value = "downloadTemplate")
    @ApiOperation(value = "下载模板")
    @LogAndParam(value = "下载模板失败")
    @ApiResponses({@ApiResponse(code = 200, response = File.class, message = "下载模板"),})
    public ResponseEntity<Resource> downloadTemplate() throws Exception {
        return userService.downloadTemplate();
    }
    
    @PostMapping(value = "isAccessToEDMOrHoolink")
    @ApiOperation(value = "首页获取EDM/hoolink权限")
    @LogAndParam(value = "首页获取EDM/hoolink权限失败", check = CheckEnum.DATA_NOT_NULL)
    public BackVO<AccessToEDMOrHoolinkVO> isAccessToEDMOrHoolink() throws Exception{
    	return BackVOUtil.operateAccess(CopyPropertiesUtil.copyBean(userService.isAccessToEDMOrHoolink(), AccessToEDMOrHoolinkVO.class));
    }
}
