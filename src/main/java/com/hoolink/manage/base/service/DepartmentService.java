package com.hoolink.manage.base.service;

import java.util.List;

import com.hoolink.manage.base.bo.ManageDepartmentBO;

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
}
