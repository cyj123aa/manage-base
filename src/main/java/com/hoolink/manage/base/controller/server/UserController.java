package com.hoolink.manage.base.controller.server;

import com.hoolink.sdk.bo.manager.OrganizationInfoParamBO;
import com.hoolink.sdk.bo.manager.UserDeptAssociationBO;
import com.hoolink.sdk.bo.manager.DeptSecurityRepertoryBO;
import com.hoolink.sdk.bo.manager.UserDeptInfoBO;
import java.util.List;

import com.hoolink.sdk.param.BaseParam;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoolink.manage.base.service.UserService;
import com.hoolink.sdk.annotation.LogAndParam;
import com.hoolink.sdk.bo.BackBO;
import com.hoolink.sdk.bo.manager.ManagerUserBO;
import com.hoolink.sdk.utils.BackBOUtil;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author lijunling
 *
 * @date 2019/05/20 17:30
 */
@RestController(value = "server.userController")
@RequestMapping(value = "/user/")
@RestSchema(schemaId = "server.userController")
public class UserController {

    @Autowired
    private UserService userService;
    
    @PostMapping(value = "getById")
    @ApiOperation(value = "根据id获取用户")
    @LogAndParam(value = "根据id获取用户失败，请稍后重试")
	public BackBO<ManagerUserBO> getById(@RequestBody Long id)throws Exception{
		return BackBOUtil.defaultBackBO(userService.getById(id));
	}

    @PostMapping(value = "getUserSecurity")
    @ApiOperation(value = "获取用户密保等级")
    @LogAndParam(value = "用户密保等级获取失败，请稍后重试")
    public BackBO<UserDeptInfoBO> getUserSecurity(@RequestBody Long userId)throws Exception{
        return BackBOUtil.defaultBackBO(userService.getUserSecurity(userId));
    }
    @PostMapping(value = "getUserList")
    @ApiOperation(value = "根据id集合获取用户")
    @LogAndParam(value = "根据id集合获取用户失败，请稍后重试")
    public BackBO<List<ManagerUserBO>> listByIdList(@RequestBody List<Long> idList)throws Exception{
        return BackBOUtil.defaultBackBO(userService.getUserList(idList));
    }

    @PostMapping(value = "getDeptByUser")
    @ApiOperation(value = "根据用户id查询用户密保等级")
    @LogAndParam(value = "根据用户id查询用户密保等级失败，请稍后重试")
    public BackBO<List<DeptSecurityRepertoryBO>> getDeptByUser(@RequestBody BaseParam<Long> id)throws Exception{
        return BackBOUtil.defaultBackBO(userService.getDeptByUser(id.getData()));
    }


    @PostMapping(value = "getOrgInfoToCompany")
    @ApiOperation(value = "根据用户id获取所在公司信息")
    @LogAndParam(value = "获取公司信息失败，请稍后重试")
    public BackBO<List<UserDeptAssociationBO>> getOrgInfoToCompany(@RequestBody OrganizationInfoParamBO paramBO)throws Exception{
        return BackBOUtil.defaultBackBO(userService.getOrgInfoToCompany(paramBO));
    }
}
