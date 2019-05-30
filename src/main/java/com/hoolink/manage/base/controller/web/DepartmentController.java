package com.hoolink.manage.base.controller.web;

import com.hoolink.manage.base.service.DepartmentService;
import com.hoolink.sdk.annotation.LogAndParam;
import com.hoolink.sdk.bo.BackBO;
import com.hoolink.sdk.bo.manager.ManageDepartmentTreeBO;
import com.hoolink.sdk.exception.BusinessException;
import com.hoolink.sdk.exception.HoolinkExceptionMassageEnum;
import com.hoolink.sdk.param.BaseParam;
import com.hoolink.sdk.utils.BackBOUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author : chenzb
 * @Description : TODO
 * @date : Created on 2019/5/30 19:13
 */
@RestController
@RequestMapping(value = "/web/department/")
@RestSchema(schemaId = "departmentController")
public class DepartmentController {

    @Resource
    private DepartmentService departmentService;

    @PostMapping(value = "getOrgList")
    @ApiOperation(value = "获取组织架构信息")
    @LogAndParam(value = "获取组织架构信息失败")
    public BackBO<List<ManageDepartmentTreeBO>> getOrgList(@RequestBody BaseParam<Byte> param) throws Exception{
        if (null == param || null == param.getData()) {
            throw new BusinessException(HoolinkExceptionMassageEnum.PARAM_ERROR);
        }
        return BackBOUtil.defaultBackBO(departmentService.getOrgList(param.getData()));
    }
}
