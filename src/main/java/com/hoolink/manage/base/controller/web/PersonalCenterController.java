package com.hoolink.manage.base.controller.web;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoolink.manage.base.bo.ManagerUserInfoParamBO;
import com.hoolink.manage.base.service.UserService;
import com.hoolink.manage.base.vo.req.ManagerUserInfoParamVO;
import com.hoolink.manage.base.vo.res.ManagerUserInfoVO;
import com.hoolink.sdk.annotation.LogAndParam;
import com.hoolink.sdk.utils.BackVOUtil;
import com.hoolink.sdk.utils.ContextUtil;
import com.hoolink.sdk.utils.CopyPropertiesUtil;
import com.hoolink.sdk.vo.BackVO;

import io.swagger.annotations.ApiOperation;
/**
 * 个人中心
 * @author lijunling
 *
 * @date 2019/05/18 17:44
 */
@RestController
@RequestMapping(value = "/web/personalCenter/")
@RestSchema(schemaId = "personalCenterController")
public class PersonalCenterController {
    @Autowired
    private UserService userService;
    
    @PostMapping(value = "getManagerUserInfo")
    @ApiOperation(value = "获取个人中心基础信息")
    @LogAndParam(value = "获取个人中心基础信息失败")
    public BackVO<ManagerUserInfoVO> getManagerUserInfo(ManagerUserInfoParamVO userParamVO) throws Exception{
    	ManagerUserInfoParamBO userParamBO = new ManagerUserInfoParamBO();
    	userParamBO.setUserId(ContextUtil.getManageCurrentUser().getUserId());
    	return BackVOUtil.operateAccess(CopyPropertiesUtil.copyBean(userService.getManagerUserInfo(userParamBO), ManagerUserInfoVO.class));
    }
}
