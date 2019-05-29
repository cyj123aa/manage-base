package com.hoolink.manage.base.controller.server;

import com.hoolink.manage.base.service.MenuService;
import com.hoolink.sdk.annotation.LogAndParam;
import com.hoolink.sdk.bo.BackBO;
import com.hoolink.sdk.bo.manager.InitMenuBO;
import com.hoolink.sdk.bo.manager.RoleMenuBO;
import com.hoolink.sdk.enums.CheckEnum;
import com.hoolink.sdk.param.BaseParam;
import com.hoolink.sdk.utils.BackBOUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 菜单
 * @author: WeiMin
 * @date: 2019-05-20
 **/
@RestController
@RequestMapping(value = "/menu/")
@RestSchema(schemaId = "menuController")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @PostMapping(value = "listByCode")
    @ApiOperation(value = "获得初始化菜单")
    @LogAndParam(value = "获得初始化菜单失败",check = CheckEnum.DATA_NOT_NULL)
    public BackBO<InitMenuBO> listByCode(@RequestBody BaseParam<String> baseParam) throws Exception {
        return BackBOUtil.defaultBackBO(menuService.listByCode(baseParam.getData()));
    }

    @PostMapping(value = "listByRole")
    @ApiOperation(value = "根据角色获取菜单")
    @LogAndParam(value = "根据角色获取菜单",check = CheckEnum.DATA_NOT_NULL)
    public BackBO<List<RoleMenuBO>> listByRole(@RequestBody BaseParam<Long> baseParam) throws Exception {
        return BackBOUtil.defaultBackBO(menuService.listByRoleId(baseParam.getData()));
    }
}
