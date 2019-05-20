package com.hoolink.manage.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.hoolink.manage.base.bo.MiddleUserDepartmentBO;
import com.hoolink.manage.base.dao.mapper.MiddleUserDepartmentMapper;
import com.hoolink.manage.base.dao.mapper.ext.MiddleUserDepartmentMapperExt;
import com.hoolink.manage.base.dao.model.MiddleUserDepartment;
import com.hoolink.manage.base.dao.model.MiddleUserDepartmentExample;
import com.hoolink.manage.base.service.MiddleUserDepartmentService;
import com.hoolink.sdk.utils.CopyPropertiesUtil;

/**
 * @author lijunling
 * @description 用户部门关系
 * @date 2019/05/15 18:56
 */
@Service
public class MiddleUserDepartmentServiceImpl implements MiddleUserDepartmentService{

	@Resource
	MiddleUserDepartmentMapper middleUserDepartmentMapper;
	
	@Resource
	MiddleUserDepartmentMapperExt middleUserDepartmentMapperExt;
	
	@Override
	public List<MiddleUserDepartmentBO> listByUserIdList(List<Long> userIdList) {
		if(CollectionUtils.isEmpty(userIdList)) {
			return new ArrayList<>();
		}
		MiddleUserDepartmentExample example = new MiddleUserDepartmentExample();
		MiddleUserDepartmentExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdIn(userIdList);
		List<MiddleUserDepartment> userDeptPairList = middleUserDepartmentMapper.selectByExample(example);
		return CopyPropertiesUtil.copyList(userDeptPairList, MiddleUserDepartmentBO.class);
	}

	@Override
	public List<MiddleUserDepartmentBO> listByUserId(Long userId) {
		MiddleUserDepartmentExample example = new MiddleUserDepartmentExample();
		MiddleUserDepartmentExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		List<MiddleUserDepartment> userDeptPairList = middleUserDepartmentMapper.selectByExample(example);
		return CopyPropertiesUtil.copyList(userDeptPairList, MiddleUserDepartmentBO.class);
	}

	@Override
	public boolean batchInsert(List<MiddleUserDepartmentBO> middleUserDeptList) {
		return middleUserDepartmentMapperExt.batchInsert(CopyPropertiesUtil.copyList(middleUserDeptList, MiddleUserDepartment.class)) > 0;
	}

	@Override
	public boolean removeByUserId(Long userId) {
		MiddleUserDepartmentExample example = new MiddleUserDepartmentExample();
		MiddleUserDepartmentExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		return middleUserDepartmentMapper.deleteByExample(example) > 0;
	}

	@Override
	public List<MiddleUserDepartmentBO> listByDeptId(Long deptId) {
		MiddleUserDepartmentExample example = new MiddleUserDepartmentExample();
		example.createCriteria().andDeptIdEqualTo(deptId);
		return CopyPropertiesUtil.copyList(middleUserDepartmentMapper.selectByExample(example), MiddleUserDepartmentBO.class);
	}

}
