package com.hoolink.manage.base.dao.mapper.ext;
import com.hoolink.manage.base.bo.UserDeptBO;
import com.hoolink.manage.base.dao.model.MiddleUserDepartment;
import java.util.List;
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
	 * 用户所属信息
	 * @param userId
	 * @return
	 */
	UserDeptBO getDeptMenu(@Param("userId")Long userId);
}