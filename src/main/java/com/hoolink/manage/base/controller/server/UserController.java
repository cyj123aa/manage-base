package com.hoolink.manage.base.controller.server;

import com.hoolink.manage.base.bo.PersonalInfoBO;
import com.hoolink.manage.base.dao.model.User;
import com.hoolink.manage.base.vo.req.ManagerUserInfoParamVO;
import com.hoolink.manage.base.vo.res.PersonalInfoVO;
import com.hoolink.sdk.bo.edm.MobileFileBO;
import com.hoolink.sdk.bo.manager.*;

import com.hoolink.sdk.utils.BackVOUtil;
import java.util.List;
import java.util.Map;

import com.hoolink.sdk.param.BaseParam;
import com.hoolink.sdk.utils.CopyPropertiesUtil;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoolink.manage.base.service.UserService;
import com.hoolink.sdk.annotation.LogAndParam;
import com.hoolink.sdk.bo.BackBO;
import com.hoolink.sdk.utils.BackBOUtil;
import io.swagger.annotations.ApiOperation;

import javax.validation.constraints.NotNull;

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

    @PostMapping(value = "listByIdList")
    @ApiOperation(value = "根据id集合获取用户")
    @LogAndParam(value = "根据id集合获取用户失败，请稍后重试")
	public BackBO<List<ManagerUserBO>> listByIdList(@RequestBody List<Long> idList)throws Exception{
		return BackBOUtil.defaultBackBO(userService.listByIdList(idList));
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
    public BackBO<List<ManagerUserBO>> listByIdLists(@RequestBody List<Long> idList)throws Exception{
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

    @PostMapping(value = "getUserInfoById")
    @ApiOperation(value = "根据id获取用户")
    @LogAndParam(value = "根据id获取用户失败，请稍后重试")
    public BackBO<ManageUserInfoBO> getUserInfoById(@RequestBody Long id)throws Exception{
        return BackBOUtil.defaultBackBO(userService.getUserInfoById(id));
    }

    @PostMapping(value = "getUserByDeptIds")
    @ApiOperation(value = "根据组织架构获取用户")
    @LogAndParam(value = "根据组织架构获取用户失败，请稍后重试")
    public BackBO<List<SimpleDeptUserBO>> getUserByDeptIds(@RequestBody List<Long> deptIds){
        return BackBOUtil.defaultBackBO(userService.listUserByDeptIds(deptIds));
    }

    @PostMapping(value = "getNameByIds")
    @ApiOperation(value = "根据用户id集合返回id与名称的map")
    @LogAndParam(value = "根据id获取用户名称失败，请稍后重试")
    public BackBO<List<ManagerUserBO>> getUserNameByIds(@RequestBody  List<Long> ids){
        List<ManagerUserBO> result= CopyPropertiesUtil.copyList(userService.getUserNameByIds(ids),ManagerUserBO.class);
        return BackBOUtil.defaultBackBO(result);
    }

    @PostMapping(value = "getCompanyById")
    @ApiOperation(value = "根据用户id获取公司")
    @LogAndParam(value = "根据用户id获取公司失败，请稍后重试")
    public BackBO<List<MobileFileBO>> getCompanyById(@RequestBody  Long id) throws Exception{
        return BackBOUtil.defaultBackBO(userService.getCompanyById(id));
    }

    @PostMapping(value = "getDeptByParentId")
    @ApiOperation(value = "根据父级id获取下级架构")
    @LogAndParam(value = "根据父级id获取下级架构失败，请稍后重试")
    public BackBO<List<MobileFileBO>> getDeptByParentId(@RequestBody  Long id) throws Exception{
        return BackBOUtil.defaultBackBO(userService.getDeptByParentId(id));
    }

    @PostMapping(value = "getUserByDeviceCode")
    @ApiOperation(value = "根据手机code码获取用户")
    @LogAndParam(value = "根据手机code码获取用户失败，请稍后重试")
    public BackBO<SimpleDeptUserBO> getUserByDeviceCode(@RequestBody  String deviceCode) throws Exception{
        return BackBOUtil.defaultBackBO(userService.getUserByDeviceCode(deviceCode));
    }

    @PostMapping(value = "getParentDeptByUserId")
    @ApiOperation(value = "根据用户id获取上级组织架构")
    @LogAndParam(value = "根据用户id获取上级组织架构失败，请稍后重试")
    public BackBO<List<Long>> getParentDeptByUserId(@RequestBody Long  userId){
        return BackBOUtil.defaultBackBO(userService.getParentDeptByUserId(userId));
    }

    @PostMapping(value = "checkPassword")
    @ApiOperation(value = "校验密码")
    @LogAndParam(value = "校验密码失败，请稍后重试")
    public BackBO<Boolean> checkPassword(@RequestBody String password)throws Exception{
        return BackBOUtil.defaultBackBO(userService.checkPassword(password));
    }
    @PostMapping(value = "getManagerUserInfo")
    @ApiOperation(value = "获取个人基础信息")
    @LogAndParam(value = "获取个人基础信息失败")
    public BackBO<ManagerUserBO> getManagerUserInfo(@RequestBody Long  userId) throws Exception{
        return BackBOUtil.defaultBackBO(userService.getPeopleInfo(userId));
    }
}
