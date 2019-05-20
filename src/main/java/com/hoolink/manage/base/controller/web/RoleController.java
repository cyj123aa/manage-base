package com.hoolink.manage.base.controller.web;

import com.github.pagehelper.PageInfo;
import com.hoolink.manage.base.service.RoleService;
import com.hoolink.manage.base.vo.req.PageParamVO;
import com.hoolink.manage.base.vo.req.RoleParamVO;
import com.hoolink.sdk.annotation.LogAndParam;
import com.hoolink.sdk.enums.CheckEnum;
import com.hoolink.sdk.param.BaseParam;
import com.hoolink.sdk.utils.BackVOUtil;
import com.hoolink.sdk.vo.BackVO;
import io.swagger.annotations.ApiOperation;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @description: 角色管理
 * @author: WeiMin
 * @date: 2019-05-13
 **/
@RestController
@RequestMapping(value = "/web/role/")
@RestSchema(schemaId = "roleController")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping(value = "create")
    @ApiOperation(value = "创建角色")
    @LogAndParam(value = "创建角色失败",check = CheckEnum.DATA_NOT_NULL)
    public BackVO<Long> create(@RequestBody RoleParamVO roleParamVO) throws Exception {
        return BackVOUtil.operateAccess(roleService.create(roleParamVO));
    }

    @PostMapping(value = "update")
    @ApiOperation(value = "修改角色")
    @LogAndParam(value = "修改角色失败",check = CheckEnum.DATA_NOT_NULL)
    public BackVO update(@RequestBody RoleParamVO roleParamVO) throws Exception {
        roleService.update(roleParamVO);
        return BackVOUtil.operateAccess();
    }

    @PostMapping(value = "getById")
    @ApiOperation(value = "获取角色信息")
    @LogAndParam(value = "获取角色信息失败",check = CheckEnum.DATA_NOT_NULL)
    public BackVO<RoleParamVO> getById(@RequestBody BaseParam<Long> param) throws Exception {
        return BackVOUtil.operateAccess(roleService.getById(param.getData()));
    }

    @PostMapping(value = "listByPage")
    @ApiOperation(value = "分页获取角色信息")
    @LogAndParam(value = "分页获取角色信息失败",check = CheckEnum.DATA_NOT_NULL)
    public BackVO<PageInfo<RoleParamVO>> listByPage(@RequestBody PageParamVO pageParamVO) throws Exception {
        return BackVOUtil.operateAccess(roleService.listByPage(pageParamVO));
    }
}
