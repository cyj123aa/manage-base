package com.hoolink.manage.base.controller.server;

import com.hoolink.manage.base.bo.ManageDepartmentTreeBO;
import com.hoolink.manage.base.bo.ManageDepartmetTreeParamBO;
import com.hoolink.manage.base.service.DepartmentService;
import com.hoolink.sdk.annotation.LogAndParam;
import com.hoolink.sdk.bo.BackBO;
import com.hoolink.sdk.enums.CheckEnum;
import com.hoolink.sdk.utils.BackBOUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: tonghao
 * @Date: 2019/5/29 20:16
 */
@RestController
@RequestMapping(value = "/department/")
@RestSchema(schemaId = "departmentController")
public class DepartmentController {

    @Resource
    private DepartmentService departmentService;

    @PostMapping(value = "tree")
    @ApiOperation(value = "查询组织架构")
    @LogAndParam(value = "查询组织架构失败",check = CheckEnum.DATA_NOT_NULL)
    public BackBO<List<ManageDepartmentTreeBO>> listByRole(@RequestBody ManageDepartmetTreeParamBO paramBO) throws Exception {
        return BackBOUtil.defaultBackBO(departmentService.listAll(paramBO.getIdList(), paramBO.getFlag()));
    }
}
