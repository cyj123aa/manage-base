package com.hoolink.manage.base.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoolink.sdk.bo.manager.ManageDepartmentBO;
import com.hoolink.manage.base.bo.MiddleUserDepartmentBO;
import com.hoolink.manage.base.bo.MiddleUserDeptWithMoreBO;
import com.hoolink.manage.base.dao.mapper.MiddleUserDepartmentMapper;
import com.hoolink.manage.base.dao.mapper.ext.MiddleUserDepartmentMapperExt;
import com.hoolink.manage.base.dao.model.MiddleUserDepartment;
import com.hoolink.manage.base.dao.model.MiddleUserDepartmentExample;
import com.hoolink.manage.base.service.DepartmentService;
import com.hoolink.manage.base.service.MiddleUserDepartmentService;
import com.hoolink.sdk.enums.EncryLevelEnum;
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
	
	@Autowired
	DepartmentService departmentService;
	
	@Override
	public List<MiddleUserDepartmentBO> listByUserIdList(List<Long> userIdList) {
		if(CollectionUtils.isEmpty(userIdList)) {
			return Collections.emptyList();
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
	public List<MiddleUserDepartmentBO> listByDeptIdList(List<Long> deptIdList) {
		if(CollectionUtils.isEmpty(deptIdList)) {
			return Collections.emptyList();
		}
		MiddleUserDepartmentExample example = new MiddleUserDepartmentExample();
		example.createCriteria().andDeptIdIn(deptIdList);
		return CopyPropertiesUtil.copyList(middleUserDepartmentMapper.selectByExample(example), MiddleUserDepartmentBO.class);
	}

	@Override
	public List<MiddleUserDeptWithMoreBO> listWithMoreByUserIdList(List<Long> userIdList) {
		List<MiddleUserDeptWithMoreBO> middleUserDeptWithMoreList = new ArrayList<>();
		List<MiddleUserDepartmentBO> middleUserDeptList = listByUserIdList(userIdList);
		//设置组织类型重新组装
		List<Long> deptIdList = middleUserDeptList.stream().map(mud -> mud.getDeptId()).collect(Collectors.toList());
		List<ManageDepartmentBO> deptList = departmentService.listByIdList(deptIdList);
		middleUserDeptList.stream().forEach(mud -> {
			MiddleUserDeptWithMoreBO middleUserDeptWithMore = new MiddleUserDeptWithMoreBO();
			BeanUtils.copyProperties(mud, middleUserDeptWithMore);
			ManageDepartmentBO dept = deptList.stream().filter(d -> d.getId().equals(mud.getDeptId())).findFirst().orElse(new ManageDepartmentBO());
			middleUserDeptWithMore.setDeptType(dept.getDeptType());
			
			middleUserDeptWithMore.setDeptName(dept.getName());
			middleUserDeptWithMore.setEncryLevelDeptName(EncryLevelEnum.getValue(mud.getEncryLevelDept()));
			middleUserDeptWithMoreList.add(middleUserDeptWithMore);
		});
		return middleUserDeptWithMoreList;
	}

}
