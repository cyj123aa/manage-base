package com.hoolink.manage.base.service;

import java.util.List;

import com.hoolink.manage.base.bo.MiddleUserDepartmentBO;

/**
 * @author lijunling
 * @description 部门用户关系表
 * @date 2019/05/15 18:57
 */
public interface MiddleUserDepartmentService {
	/**
	 * 根据用户id集合查找对应部门及密保等级
	 * @param userIdList
	 * @return
	 */
	List<MiddleUserDepartmentBO> listByUserIdList(List<Long> userIdList);
	
	/**
	 * 根据用户查找对应部门及密保等级
	 * @param userId
	 * @return
	 */
	List<MiddleUserDepartmentBO> listByUserId(Long userId);
	
	/**
	 * 批量新增
	 * @param middleUserDeptList
	 * @return
	 */
	boolean batchInsert(List<MiddleUserDepartmentBO> middleUserDeptList);
	
	/**
	 * 根据用户删除部门对应关系
	 * @param userId
	 * @return
	 */
	boolean removeByUserId(Long userId);
	/**
	 * 根据某部门员工
	 * @param deptId
	 * @return
	 */
	List<MiddleUserDepartmentBO> listByDeptId(Long deptId);
}
