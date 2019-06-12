package com.hoolink.manage.base.dao.mapper.ext;

import com.hoolink.manage.base.bo.DeptPositionBO;
import com.hoolink.manage.base.dao.model.ManageDepartment;
import com.hoolink.sdk.bo.manager.ManageDepartmentBO;
import com.hoolink.sdk.bo.manager.ManageDepartmentTreeBO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: tonghao
 * @Date: 2019/5/30 11:48
 */
public interface ManageDepartmentMapperExt {

    /**
     * 获取组织架构
     * @param ids
     * @return
     */
    List<ManageDepartmentTreeBO> getOrganizationList (@Param("ids")List<Long> ids);

    /**
     * 根据公司查询小组
     * @param ids
     * @return
     */
    List<ManageDepartment> getPositionByCompany(@Param("ids")List<Long> ids);

    /**
     * 根据父节点查询下一级
     * @param parentIds
     * @return
     */
    List<DeptPositionBO> listByParentIdList(@Param("parentIds") List<Long> parentIds);

    /**
     * 查询当前一级
     * @param ids
     * @return
     */
    List<DeptPositionBO> listByIdList(@Param("ids") List<Long> ids);

    /**
     * 根据父节点查询所有下级
     * @param parentIds
     * @return
     */
    List<DeptPositionBO> listByParentIdCode(@Param("parentIds") List<Long> parentIds);

    /**
     * 主键查询 （结果集按参数顺序排列）
     * @param ids
     * @return
     */
    List<ManageDepartmentBO> listByIdOrder(@Param("ids") List<Long> ids);

    /**
     * 获取组织架构信息
     * @param ids
     * @return
     */
    List<ManageDepartmentTreeBO> getOrgInfoList (@Param("ids")List<Long> ids);

}
