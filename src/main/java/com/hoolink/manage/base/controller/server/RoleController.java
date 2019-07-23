package com.hoolink.manage.base.controller.server;

import com.hoolink.manage.base.bo.ManageRoleBO;
import com.hoolink.manage.base.service.RoleService;
import com.hoolink.sdk.annotation.LogAndParam;
import com.hoolink.sdk.bo.BackBO;
import com.hoolink.sdk.bo.manager.EdmManageRoleBO;
import com.hoolink.sdk.enums.CheckEnum;
import com.hoolink.sdk.utils.BackBOUtil;
import com.hoolink.sdk.utils.CopyPropertiesUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author : lys
 * @Date : 2019/7/8 16:21
 * @Instructions :
 */
@RestController(value = "server.roleController")
@RequestMapping(value = "/role/")
@RestSchema(schemaId = "server.roleController")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping(value = "getBaseRole")
    @ApiOperation(value = "获得角色基础信息")
    @LogAndParam(value = "获得角色基础信息失败",check = CheckEnum.DATA_NOT_NULL)
    public BackBO<EdmManageRoleBO> getBaseRole(@RequestBody Long roleId) throws Exception {
        ManageRoleBO manageRoleBO = roleService.getBaseRole(roleId);
        EdmManageRoleBO edmManageRoleBO = CopyPropertiesUtil.copyBean(manageRoleBO, EdmManageRoleBO.class);
        return BackBOUtil.defaultBackBO(edmManageRoleBO);
    }
}
