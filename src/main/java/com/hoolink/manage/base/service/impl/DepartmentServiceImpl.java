package com.hoolink.manage.base.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.hoolink.manage.base.dao.model.ManageDepartment;
import com.hoolink.manage.base.dao.model.ManageDepartmentExample;
import com.hoolink.manage.base.service.DepartmentService;
import com.hoolink.sdk.utils.CopyPropertiesUtil;
import com.hoolink.manage.base.bo.ManageDepartmentBO;
import com.hoolink.manage.base.dao.mapper.ManageDepartmentMapper;

/**
 * @author lijunling
 * @description 部门
 * @date 2019/05/15 18:57
 */
@Service
public class DepartmentServiceImpl implements DepartmentService{

	@Resource
	ManageDepartmentMapper manageDepartmentMapper;
	
	@Override
	public List<ManageDepartmentBO> listByIdList(List<Long> idList) {
		if(CollectionUtils.isEmpty(idList)) {
			return Collections.emptyList();
		}
		ManageDepartmentExample example = new ManageDepartmentExample();
		ManageDepartmentExample.Criteria criteria = example.createCriteria();
		criteria.andIdIn(idList);
		criteria.andEnabledEqualTo(true);
		List<ManageDepartment> deptList = manageDepartmentMapper.selectByExample(example);
		return CopyPropertiesUtil.copyList(deptList, ManageDepartmentBO.class);
	}

	@Override
	public List<ManageDepartmentBO> listAll() {
		ManageDepartmentExample example = new ManageDepartmentExample();
		example.createCriteria().andEnabledEqualTo(true);
		List<ManageDepartment> deptList = manageDepartmentMapper.selectByExample(example);
		return CopyPropertiesUtil.copyList(deptList, ManageDepartmentBO.class);
	}

	@Override
	public List<ManageDepartmentBO> listByDeptType(Byte deptType) {
		ManageDepartmentExample example = new ManageDepartmentExample();
		example.createCriteria().andEnabledEqualTo(true).andDeptTypeEqualTo(deptType);
		List<ManageDepartment> deptList = manageDepartmentMapper.selectByExample(example);
		return CopyPropertiesUtil.copyList(deptList, ManageDepartmentBO.class);
	}

}
