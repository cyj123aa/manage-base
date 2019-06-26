package com.hoolink.manage.base.controller.web;

import com.hoolink.sdk.bo.manager.DepartmentTreeParamBO;
import com.hoolink.manage.base.service.DepartmentService;
import com.hoolink.manage.base.vo.req.DepartmentTreeParamVO;
import com.hoolink.manage.base.vo.res.DepartmentAndUserTreeVO;
import com.hoolink.manage.base.vo.res.ManageDepartmentTreeVO;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
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
    public BackVO<List<ManageDepartmentTreeVO>> getOrgList(@RequestBody @Valid DepartmentTreeParamVO param) throws Exception{
        // 入参的VO转BO
        DepartmentTreeParamBO treeParamBO = CopyPropertiesUtil.copyBean(param,DepartmentTreeParamBO.class);
        // 出参BO转VO
        List<ManageDepartmentTreeVO> manageDepartmentTreeVOS = CopyPropertiesUtil.copyList(departmentService.getOrgListTree(treeParamBO),ManageDepartmentTreeVO.class);
        return BackVOUtil.operateAccess(manageDepartmentTreeVOS);
    }

    @PostMapping(value = "tree")
    @ApiOperation(value = "查询组织架构")
    @LogAndParam(value = "查询组织架构失败",check = CheckEnum.DATA_NOT_NULL)
    public BackBO<List<DepartmentAndUserTreeVO>> listByRole(@RequestBody BaseParam<Boolean> baseParam) throws Exception {
        List<DepartmentAndUserTreeVO> manageDepartmentTreeVOS = CopyPropertiesUtil.copyList(departmentService.listAll(baseParam.getData(), null), DepartmentAndUserTreeVO.class);
        return BackBOUtil.defaultBackBO(manageDepartmentTreeVOS);
    }
}