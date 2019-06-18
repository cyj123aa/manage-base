package com.hoolink.manage.base.service;

import com.hoolink.manage.base.dao.model.ManageDepartment;
import com.hoolink.sdk.bo.manager.*;

import java.util.List;

import com.hoolink.sdk.bo.edm.CheckedParamBO;
import com.hoolink.sdk.bo.edm.DepartmentAndUserTreeBO;
import com.hoolink.sdk.bo.manager.ManageDepartmentTreeBO;
import com.hoolink.sdk.bo.manager.ManageDepartmetTreeParamBO;
import com.hoolink.manage.base.bo.DeptPositionBO;

/**
 * @author lijunling
 * @description 部门
 * @date 2019/05/15 18:57
 */
public interface DepartmentService {
	/**
	 * 根据id集合获取部门信息
	 * @param idList
	 * @return
	 */
	List<ManageDepartmentBO> listByIdList(List<Long> idList);

	/**
	 * 根据父节点集合获取部门信息
	 * @param idList
	 * @return
	 */
	List<DeptPositionBO> listByParentIdList(List<Long> idList);

	/**
	 *  获取组织架构树
	 * @return
	 */
	List<ManageDepartmentBO> listAll();
	/**
	 * 根据组织类型查询
	 * @param deptType
	 * @return
	 */
	List<ManageDepartmentBO> listByDeptType(Byte deptType);

	/**
	 * 父级查询组织类型
	 * @param parentList
	 * @return
	 */
	List<ManageDepartment> listByParentList(List<Long> parentList);

	/**
	 * 组织架构树形结构
	 * @param flag 是否查询组织架构下的员工 true:查询 false:只查询组织架构
	 * @param checkedList 所勾选的员工列表 如果 flag == false && checkedList != null 就勾选组织架构，flag == true && checkedList != null勾选组织架构下员工
	 * @return
	 */
	List<DepartmentAndUserTreeBO> listAll(Boolean flag, List<CheckedParamBO> checkedList);

	/**
	 * 获取组织机构树（人员）
	 * @param departmetTreeParamBO
	 * @return
	 * @throws Exception
	 */
	List<ManageDepartmentTreeBO> getOrganizationList (ManageDepartmetTreeParamBO departmetTreeParamBO) throws Exception;

	/**
	 * 获取组织架构树
	 * @param
	 * @param treeParamBO flag true 是否查询组织架构下面的人员
	 * @return
	 * @throws Exception
	 */
	List<ManageDepartmentTreeBO> getOrgList (DepartmentTreeParamBO treeParamBO) throws Exception;
	/**
	 * 获取组织
	 * @param paramBO
	 * @return
	 * @throws Exception
	 */
	OrganizationDeptBO getOrganization (OrganizationDeptParamBO paramBO) throws Exception;

	/**
	 * 获取组织信息
	 * @param treeParamBO
	 * @return
	 * @throws Exception
	 */
	PermissionManageDeptBO getOrgInfoList (DepartmentTreeParamBO treeParamBO) throws Exception;

	/**
	 * 获取部门名称
	 * @param deptId
	 * @return
	 * @throws Exception
	 */
	String getDeptInfo(Long deptId) throws Exception;

	/**
	 * 获取文件的上层架构
	 * @param
	 * @param treeParamBO f
	 * @return
	 * @throws Exception
	 */
	List<ReadFileOrgInfoBO> getFileOrgList (List<ReadFileOrgInfoParamBO> treeParamBO) throws Exception;
}
