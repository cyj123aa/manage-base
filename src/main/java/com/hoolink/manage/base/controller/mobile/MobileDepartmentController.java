package com.hoolink.manage.base.controller.mobile;

import com.hoolink.manage.base.service.DepartmentService;
import com.hoolink.manage.base.vo.req.DepartmentTreeParamVO;
import com.hoolink.manage.base.vo.res.ManageDepartmentTreeVO;
import com.hoolink.sdk.annotation.LogAndParam;
import com.hoolink.sdk.bo.manager.DepartmentTreeParamBO;
import com.hoolink.sdk.utils.BackVOUtil;
import com.hoolink.sdk.utils.CopyPropertiesUtil;
import com.hoolink.sdk.vo.BackVO;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping(value = "/mobile/department/")
public class MobileDepartmentController {

    @Resource
    private DepartmentService departmentService;

    @PostMapping(value = "getOrgList")
    @ApiOperation(value = "获取组织架构信息")
    @LogAndParam(value = "获取组织架构信息失败")
    public BackVO<List<ManageDepartmentTreeVO>> getOrgList(@RequestBody @Valid DepartmentTreeParamVO param) throws Exception{
        // 入参的VO转BO
        DepartmentTreeParamBO treeParamBO = CopyPropertiesUtil.copyBean(param,DepartmentTreeParamBO.class);
        // 出参BO转VO
        List<ManageDepartmentTreeVO> manageDepartmentTreeVOS = CopyPropertiesUtil.copyList(departmentService.getOrgList(treeParamBO),ManageDepartmentTreeVO.class);
        return BackVOUtil.operateAccess(manageDepartmentTreeVOS);
    }
}
