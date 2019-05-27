package com.hoolink.manage.base.controller.server;

import com.hoolink.manage.base.service.MenuService;
import com.hoolink.sdk.annotation.LogAndParam;
import com.hoolink.sdk.bo.BackBO;
import com.hoolink.sdk.bo.manager.InitMenuBO;
import com.hoolink.sdk.enums.CheckEnum;
import com.hoolink.sdk.utils.BackBOUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @LogAndParam(value = "获得初始化菜单失败",check = CheckEnum.STRING_NOT_BLANK)
    public BackBO<InitMenuBO> listByCode(String code){
        return BackBOUtil.defaultBackBO(menuService.listByCode(code));
    }
}
