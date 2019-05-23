package com.hoolink.manage.base.dao.mapper.ext;
import com.hoolink.manage.base.bo.DeptPositionBO;
import com.hoolink.manage.base.dao.model.MiddleUserDepartment;
import java.util.List;

import com.hoolink.sdk.bo.manager.UserDeptInfoBO;
import org.apache.ibatis.annotations.Param;

/**
 * @author lijunling
 * @description 用户部门关系表
 * @date 2019/05/15 19:00
 */
public interface MiddleUserDepartmentMapperExt {
	/**
	 * 批量新增
	 * @param middleUserDeptList
	 * @return
	 */
    int batchInsert(@Param("middleUserDeptList") List<MiddleUserDepartment> middleUserDeptList);

	/**
	 * 用户绑定组织架构信息
	 * @param userId
	 * @return
	 */
	List<DeptPositionBO> getDept(@Param("userId")Long userId);

	/**
	 * 用户密保等级
	 * @param userId
	 * @return
	 */
	UserDeptInfoBO getUserSecurity(@Param("userId")Long userId);
}