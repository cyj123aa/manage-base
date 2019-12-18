package com.hoolink.manage.base.controller.web;

import com.hoolink.manage.base.bo.LoginParamBO;
import com.hoolink.manage.base.bo.ManagerUserParamBO;
import com.hoolink.manage.base.bo.PhoneParamBO;
import com.hoolink.manage.base.bo.UpdatePasswdParamBO;
import com.hoolink.manage.base.service.UserService;
import com.hoolink.manage.base.util.ValidatorUtil;
import com.hoolink.manage.base.vo.ManagerBaseGroup;
import com.hoolink.manage.base.vo.req.EnableOrDisableUserParamVO;
import com.hoolink.manage.base.vo.req.LoginParamVO;
import com.hoolink.manage.base.vo.req.ManagerUserParamVO;
import com.hoolink.manage.base.vo.req.PhoneParamVO;
import com.hoolink.manage.base.vo.req.UpdatePasswdParamVO;
import com.hoolink.manage.base.vo.res.LoginResultVO;
import com.hoolink.manage.base.vo.res.UserInfoVO;
import com.jw.sdk.annotation.LogAndParam;
import com.jw.sdk.bo.base.CurrentUserBO;
import com.jw.sdk.enums.CheckEnum;
import com.jw.sdk.param.BaseParam;
import com.jw.sdk.utils.BackVOUtil;
import com.jw.sdk.utils.CopyPropertiesUtil;
import com.jw.sdk.vo.BackVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xuli
 * @Date: 2019/4/15 19:23
 */
@RestController
@RequestMapping(value = "/web/user/")
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
        LoginResultVO loginResultVO= CopyPropertiesUtil.copyBean(userService.login(paramBO,false),LoginResultVO.class);
        return BackVOUtil.operateAccess(loginResultVO);
    }

    @PostMapping(value = "logout")
    @ApiOperation(value = "用户退出")
    @LogAndParam(value = "用户退出失败，请稍后重试")
    public BackVO<Void> logout() {
        userService.logout();
        return BackVOUtil.operateAccess();
    }



    @PostMapping(value = "getSessionUser")
    @ApiOperation(value = "查询Session用户", notes = "该接口主要提供网关鉴权使用，其他场景不要使用")
    @LogAndParam(value = "查询Session用户失败")
    public CurrentUserBO getSessionUser(@RequestBody String token,boolean isMobile) {
        return userService.getSessionUser(token,isMobile);
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
    





}
