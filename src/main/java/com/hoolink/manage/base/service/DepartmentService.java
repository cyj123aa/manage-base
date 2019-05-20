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
	 * 根据公司获取所有部门
	 * @param company
	 * @return
	 */
	List<ManageDepartmentBO> listByCompany(String company);
}
