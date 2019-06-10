package com.hoolink.manage.base.service;

import java.util.List;

import com.hoolink.manage.base.bo.MiddleUserDepartmentBO;
import com.hoolink.manage.base.bo.MiddleUserDeptWithMoreBO;

/**
 * @author lijunling
 * @description 部门用户关系表
 * @date 2019/05/15 18:57
 */
public interface MiddleUserDepartmentService {
	/**
	 * 根据用户id集合查找对应组织及密保等级
	 * @param userIdList
	 * @return
	 */
	List<MiddleUserDepartmentBO> listByUserIdList(List<Long> userIdList);
	
	/**
	 * 根据用户查找对应组织及密保等级
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
	 * 根据用户删除组织对应关系
	 * @param userId
	 * @return
	 */
	boolean removeByUserId(Long userId);
	
	/**
	 * 查找某组织员工
	 * @param deptIdList
	 * @return
	 */
	List<MiddleUserDepartmentBO> listByDeptIdList(List<Long> deptIdList);
	/**
	 * 根据用户id集合查找对应组织及密保等级（包含部门类型、部门名称、加密等级等更多属性）
	 * @param userIdList
	 * @return
	 */
	List<MiddleUserDeptWithMoreBO> listWithMoreByUserIdList(List<Long> userIdList);
}
