package com.hoolink.manage.base.controller.web;

import com.github.pagehelper.PageInfo;
import com.hoolink.manage.base.bo.RoleParamBO;
import com.hoolink.manage.base.bo.SearchPageParamBO;
import com.hoolink.manage.base.service.RoleService;
import com.hoolink.manage.base.vo.req.PageParamVO;
import com.hoolink.manage.base.vo.req.RoleParamVO;
import com.hoolink.manage.base.vo.req.SearchPageParamVO;
import com.hoolink.sdk.annotation.LogAndParam;
import com.hoolink.sdk.enums.CheckEnum;
import com.hoolink.sdk.param.BaseParam;
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
    public BackVO<RoleParamVO> getById(@RequestBody BaseParam<Long> param) throws Exception {
        RoleParamBO roleParamBO = roleService.getById(param.getData());
        RoleParamVO roleParamVO = CopyPropertiesUtil.copyBean(roleParamBO, RoleParamVO.class);
        return BackVOUtil.operateAccess(roleParamVO);
    }

    @PostMapping(value = "listByPage")
    @ApiOperation(value = "分页获取角色信息")
    @LogAndParam(value = "分页获取角色信息失败",check = CheckEnum.DATA_NOT_NULL)
    public BackVO<PageInfo<RoleParamVO>> listByPage(@RequestBody SearchPageParamVO pageParamVO) throws Exception {
        SearchPageParamBO pageParamBO = CopyPropertiesUtil.copyBean(pageParamVO, SearchPageParamBO.class);
        PageInfo<RoleParamBO> roleParamBOPageInfo = roleService.listByPage(pageParamBO);
        PageInfo<RoleParamVO> pageInfo = CopyPropertiesUtil.copyBean(roleParamBOPageInfo, PageInfo.class);
        return BackVOUtil.operateAccess(pageInfo);
    }
}
