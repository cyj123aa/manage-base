package com.jw.manage.base.controller.mobile;

import com.jw.manage.base.bo.LoginParamBO;
import com.jw.manage.base.service.UserService;
import com.jw.manage.base.util.ValidatorUtil;
import com.jw.manage.base.vo.ManagerBaseGroup.UserLogin;
import com.jw.manage.base.vo.req.LoginParamVO;
import com.jw.manage.base.vo.res.LoginResultVO;
import com.jw.sdk.annotation.LogAndParam;
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
@RequestMapping(value = "/mobile/user/")
public class MobileUserController {

    @Autowired
    private UserService userService;

    
    @PostMapping(value = "login")
    @ApiOperation(value = "用户登录")
    @LogAndParam(value = "用户登录失败，请稍后重试")
    public BackVO<LoginResultVO> login(@RequestBody LoginParamVO loginParam)throws Exception {
        BackVO validateParameter = ValidatorUtil.validateParameter(loginParam, UserLogin.class);
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
