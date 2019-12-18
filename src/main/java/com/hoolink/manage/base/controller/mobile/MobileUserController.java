package com.hoolink.manage.base.controller.mobile;

import com.hoolink.manage.base.bo.LoginParamBO;
import com.hoolink.manage.base.bo.ManagerUserInfoParamBO;
import com.hoolink.manage.base.bo.ManagerUserParamBO;
import com.hoolink.manage.base.bo.MobileUpdateParamBO;
import com.hoolink.manage.base.bo.PhoneParamBO;
import com.hoolink.manage.base.service.UserService;
import com.hoolink.manage.base.util.ValidatorUtil;
import com.hoolink.manage.base.vo.ManagerBaseGroup;
import com.hoolink.manage.base.vo.req.EnableOrDisableUserParamVO;
import com.hoolink.manage.base.vo.req.LoginParamVO;
import com.hoolink.manage.base.vo.req.ManagerUserInfoParamVO;
import com.hoolink.manage.base.vo.req.ManagerUserParamVO;
import com.hoolink.manage.base.vo.req.MobileUpdateParamVO;
import com.hoolink.manage.base.vo.req.PhoneParamVO;
import com.hoolink.manage.base.vo.res.LoginResultVO;
import com.hoolink.manage.base.vo.res.ManagerUserInfoVO;
import com.hoolink.manage.base.vo.res.UserInfoVO;
import com.jw.sdk.annotation.LogAndParam;
import com.jw.sdk.enums.CheckEnum;
import com.jw.sdk.param.BaseParam;
import com.jw.sdk.utils.BackVOUtil;
import com.jw.sdk.utils.CopyPropertiesUtil;
import com.jw.sdk.vo.BackVO;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
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
@RequestMapping(value = "/mobile/user/")
public class MobileUserController {

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
        LoginResultVO loginResultVO= CopyPropertiesUtil.copyBean(userService.login(paramBO,true),LoginResultVO.class);
        return BackVOUtil.operateAccess(loginResultVO);
    }

    @PostMapping(value = "logout")
    @ApiOperation(value = "用户退出")
    @LogAndParam(value = "用户退出失败，请稍后重试")
    public BackVO<Void> logout() {
        userService.mobileLogout();
        return BackVOUtil.operateAccess();
    }


    

}
