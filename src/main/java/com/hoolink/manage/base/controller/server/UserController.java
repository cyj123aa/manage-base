package com.hoolink.manage.base.controller.server;

import java.util.List;

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

    @PostMapping(value = "listByIdList")
    @ApiOperation(value = "根据id集合获取用户")
    @LogAndParam(value = "根据id集合获取用户失败，请稍后重试")
    public BackBO<List<ManagerUserBO>> listByIdList(@RequestBody List<Long> idList)throws Exception{
        return BackBOUtil.defaultBackBO(userService.listByIdList(idList));
    }

    @PostMapping(value = "checkPassword")
    @ApiOperation(value = "校验密码")
    @LogAndParam(value = "校验密码失败，请稍后重试")
    public BackBO<Boolean> checkPassword(@RequestBody String password)throws Exception{
        return BackBOUtil.defaultBackBO(userService.checkPassword(password));
    }
}
