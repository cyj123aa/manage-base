package com.hoolink.manage.base.controller.web;

import com.github.pagehelper.PageInfo;
import com.hoolink.manage.base.bo.*;
import com.hoolink.manage.base.service.RoleService;
import com.hoolink.manage.base.vo.req.MenuParamVO;
import com.hoolink.manage.base.vo.req.PageParamVO;
import com.hoolink.manage.base.vo.req.RoleParamVO;
import com.hoolink.manage.base.vo.req.SearchPageParamVO;
import com.hoolink.manage.base.vo.res.BackRoleVO;
import com.hoolink.manage.base.vo.res.ManageMenuTreeVO;
import com.hoolink.manage.base.vo.res.RoleMenuVO;
import com.hoolink.sdk.annotation.LogAndParam;
import com.hoolink.sdk.bo.BackBO;
import com.hoolink.sdk.enums.CheckEnum;
import com.hoolink.sdk.param.BaseParam;
import com.hoolink.sdk.utils.BackBOUtil;
import com.hoolink.sdk.utils.BackVOUtil;
import com.hoolink.sdk.utils.CopyPropertiesUtil;
import com.hoolink.sdk.vo.BackVO;
import io.swagger.annotations.ApiOperation;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


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
        RoleParamBO roleParamBO = CopyPropertiesUtil.copyBean(roleParamVO, RoleParamBO.class);
        return BackVOUtil.operateAccess(roleService.create(roleParamBO));
    }

    @PostMapping(value = "update")
    @ApiOperation(value = "修改角色")
    @LogAndParam(value = "修改角色失败",check = CheckEnum.DATA_NOT_NULL)
    public BackVO update(@RequestBody RoleParamVO roleParamVO) throws Exception {
        RoleParamBO roleParamBO = CopyPropertiesUtil.copyBean(roleParamVO, RoleParamBO.class);
        roleService.update(roleParamBO);
        return BackVOUtil.operateAccess();
    }

    @PostMapping(value = "updateStatus")
    @ApiOperation(value = "修改角色状态")
    @LogAndParam(value = "修改角色状态失败",check = CheckEnum.DATA_NOT_NULL)
    public BackVO updateStatus(@RequestBody RoleParamVO roleParamVO) throws Exception {
        RoleParamBO roleParamBO = CopyPropertiesUtil.copyBean(roleParamVO, RoleParamBO.class);
        roleService.updateStatus(roleParamBO);
        return BackVOUtil.operateAccess();
    }

    @PostMapping(value = "getById")
    @ApiOperation(value = "获取角色信息")
    @LogAndParam(value = "获取角色信息失败",check = CheckEnum.DATA_NOT_NULL)
    public BackVO<BackRoleVO> getById(@RequestBody BaseParam<Long> param) throws Exception {
        BackRoleBO backRoleBO = roleService.getById(param.getData());
        BackRoleVO backRoleVO = CopyPropertiesUtil.copyBean(backRoleBO, BackRoleVO.class);
        return BackVOUtil.operateAccess(backRoleVO);
    }

    @PostMapping(value = "getBaseMenu")
    @ApiOperation(value = "获得管理基础动态菜单")
    @LogAndParam(value = "获得管理基础菜单失败")
    public BackBO<List<ManageMenuTreeVO>> getBaseMenu(@RequestBody MenuParamVO menuParamVO) throws Exception {
        MenuParamBO menuParamBO = CopyPropertiesUtil.copyBean(menuParamVO, MenuParamBO.class);
        List<ManageMenuTreeBO> baseMenu = roleService.getBaseMenu(menuParamBO);
        List<ManageMenuTreeVO> manageMenuTreeVOS = CopyPropertiesUtil.copyList(baseMenu, ManageMenuTreeVO.class);
        return BackBOUtil.defaultBackBO(manageMenuTreeVOS);
    }

    @PostMapping(value = "getCurrentRoleMenu")
    @ApiOperation(value = "获取当前角色权限")
    @LogAndParam(value = "获取当前角色权限失败")
    public BackVO<RoleMenuVO> getCurrentRoleMenu() throws Exception {
        RoleMenuBO roleMenuBO = roleService.getCurrentRoleMenu();
        RoleMenuVO roleMenuVO = CopyPropertiesUtil.copyBean(roleMenuBO, RoleMenuVO.class);
        return BackVOUtil.operateAccess(roleMenuVO);
    }

    @PostMapping(value = "listByPage")
    @ApiOperation(value = "分页获取角色信息")
    @LogAndParam(value = "分页获取角色信息失败",check = CheckEnum.DATA_NOT_NULL)
    public BackVO<PageInfo<RoleParamVO>> listByPage(@RequestBody SearchPageParamVO pageParamVO) throws Exception {
        SearchPageParamBO pageParamBO = CopyPropertiesUtil.copyBean(pageParamVO, SearchPageParamBO.class);
        PageInfo<RoleParamBO> roleParamBOPageInfo = roleService.listByPage(pageParamBO);
        List<RoleParamVO> roleParamVOS = CopyPropertiesUtil.copyList(roleParamBOPageInfo.getList(), RoleParamVO.class);
        PageInfo<RoleParamVO> pageInfo = CopyPropertiesUtil.copyBean(roleParamBOPageInfo, PageInfo.class);
        pageInfo.setList(roleParamVOS);
        return BackVOUtil.operateAccess(pageInfo);
    }
}
