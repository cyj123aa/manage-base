package com.hoolink.manage.base.controller.server;

import com.hoolink.sdk.bo.manager.DepartmentTreeParamBO;
import com.hoolink.manage.base.service.DepartmentService;
import com.hoolink.sdk.annotation.LogAndParam;
import com.hoolink.sdk.bo.BackBO;
import com.hoolink.sdk.bo.manager.*;
import com.hoolink.sdk.utils.BackBOUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author: tonghao
 * @Date: 2019/5/29 20:16
 */
@RestController(value = "server.departmentController")
@RequestMapping(value = "/department/")
@RestSchema(schemaId = "server.departmentController")
public class DepartmentController {

    @Resource
    private DepartmentService departmentService;


    @PostMapping(value = "getOrganizationList")
    @ApiOperation(value = "获取组织架构信息")
    @LogAndParam(value = "获取组织架构信息失败")
    public BackBO<List<ManageDepartmentTreeBO>> getOrganizationList(@RequestBody @Valid ManageDepartmetTreeParamBO paramBO) throws Exception{
        return BackBOUtil.defaultBackBO(departmentService.getOrganizationList(paramBO));
    }

    @PostMapping(value = "getOrganization")
    @ApiOperation(value = "获取组织架构信息")
    @LogAndParam(value = "获取组织架构信息失败")
    public BackBO<OrganizationDeptBO> getOrganization(@RequestBody @Valid OrganizationDeptParamBO paramBO) throws Exception{
        return BackBOUtil.defaultBackBO(departmentService.getOrganization(paramBO));
    }

    @PostMapping(value = "getOrgListByIdList")
    @ApiOperation(value = "获取组织架构信息集合")
    @LogAndParam(value = "获取组织架构信息失败")
    public BackBO<List<ManageDepartmentBO>> getOrgListByIdList(@RequestBody List<Long> idList) throws Exception{
        return BackBOUtil.defaultBackBO(departmentService.listByIdList(idList));
    }

    @PostMapping(value = "getOrgInfoList")
    @ApiOperation(value = "组织架构信息")
    @LogAndParam(value = "获取组织架构信息失败")
    public BackBO<PermissionManageDeptBO> getOrgTree(@RequestBody DepartmentTreeParamBO param) throws Exception{
        return BackBOUtil.defaultBackBO(departmentService.getOrgInfoList(param));
    }
}
