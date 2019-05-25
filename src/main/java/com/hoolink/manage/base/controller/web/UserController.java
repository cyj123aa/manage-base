package com.hoolink.manage.base.controller.web;

import com.hoolink.manage.base.bo.PhoneParamBO;
import com.hoolink.manage.base.bo.LoginParamBO;
import com.hoolink.manage.base.service.UserService;
import com.hoolink.manage.base.util.ValidatorUtil;
import com.hoolink.manage.base.vo.ManagerBaseGroup;
import com.hoolink.manage.base.vo.req.PhoneParamVO;
import com.hoolink.manage.base.vo.req.LoginParamVO;
import com.hoolink.manage.base.vo.res.LoginResultVO;
import com.hoolink.manage.base.vo.res.UserInfoVO;
import com.hoolink.sdk.annotation.LogAndParam;
import com.hoolink.sdk.bo.base.CurrentUserBO;
import com.hoolink.sdk.param.BaseParam;
import com.hoolink.sdk.utils.BackVOUtil;
import com.hoolink.sdk.utils.CopyPropertiesUtil;
import com.hoolink.sdk.vo.BackVO;
import io.swagger.annotations.ApiOperation;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public BackVO<String> getPhoneCode(@RequestBody BaseParam<String> phone)throws Exception  {
        return BackVOUtil.operateAccess(userService.getPhoneCode(phone.getData(),false));
    }

    @PostMapping(value = "bindPhoneGetCode")
    @ApiOperation(value = "绑定手机号时获取手机验证码")
    @LogAndParam(value = "获取手机验证码失败，请稍后重试")
    public BackVO<String> bindPhoneGetCode(@RequestBody BaseParam<String> phone)throws Exception  {
        return BackVOUtil.operateAccess(userService.getPhoneCode(phone.getData(),true));
    }

    @PostMapping(value = "modifyPhoneGetCode")
    @ApiOperation(value = "修改手机号时获取手机验证码")
    @LogAndParam(value = "修改手机验证码失败，请稍后重试")
    public BackVO<String> modifyPhoneGetCode(@RequestBody BaseParam<String> phone)throws Exception  {
        return BackVOUtil.operateAccess(userService.getPhoneCode(phone.getData(),true));
    }

    @PostMapping(value = "verifyPhoneCode")
    @ApiOperation(value = "验证手机验证码")
    @LogAndParam(value = "验证手机验证码失败，请稍后重试")
    public BackVO<String> verifyPhoneCode(@RequestBody @Valid PhoneParamVO phoneParam)throws Exception  {
        PhoneParamBO bindPhone=CopyPropertiesUtil.copyBean(phoneParam, PhoneParamBO.class);
        userService.verifyPhone(bindPhone);
        return BackVOUtil.operateAccess();
    }

    @PostMapping(value = "bindPhone")
    @ApiOperation(value = "绑定手机号")
    @LogAndParam(value = "绑定手机号失败，请稍后重试")
    public BackVO<Void> bindPhone(@RequestBody @Valid PhoneParamVO bindPhoneParam)throws Exception  {
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
    public BackVO<Void> updatePhone(@RequestBody @Valid PhoneParamVO phoneParam)throws Exception  {
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
}
