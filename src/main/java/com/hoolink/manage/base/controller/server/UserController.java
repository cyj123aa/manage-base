package com.hoolink.manage.base.controller.server;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoolink.manage.base.bo.ManagerUserBO;
import com.hoolink.manage.base.service.UserService;
import com.hoolink.sdk.annotation.LogAndParam;
import com.hoolink.sdk.utils.BackVOUtil;
import com.hoolink.sdk.vo.BackVO;

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
	public BackVO<ManagerUserBO> getById(@RequestBody Long id)throws Exception{
		return BackVOUtil.operateAccess(userService.getById(id));
	}
}
