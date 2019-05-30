package com.hoolink.manage.base.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.hoolink.sdk.bo.manager.ManageDepartmentTreeBO;
import com.hoolink.sdk.bo.manager.ManageDepartmetTreeParamBO;
import com.hoolink.sdk.bo.manager.OrganizationInfoParamBO;
import com.hoolink.sdk.bo.manager.SimpleDeptUserBO;
import com.hoolink.manage.base.dao.mapper.ext.ManageDepartmentMapperExt;
import com.hoolink.manage.base.service.UserService;
import com.hoolink.manage.base.util.DeptTreeToolUtils;
import com.hoolink.sdk.exception.BusinessException;
import com.hoolink.sdk.exception.HoolinkExceptionMassageEnum;
import com.hoolink.sdk.utils.ContextUtil;
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

	@Resource
	UserService userService;

	@Resource
	private ManageDepartmentMapperExt manageDepartmentMapperExt;
	
	@Override
	public List<ManageDepartmentBO> listByIdList(List<Long> idList) {
		if(CollectionUtils.isEmpty(idList)) {
			return new ArrayList<>();
		}
		ManageDepartmentExample example = new ManageDepartmentExample();
		ManageDepartmentExample.Criteria criteria = example.createCriteria();
		criteria.andIdIn(idList);
		criteria.andEnabledEqualTo(true);
		List<ManageDepartment> deptList = manageDepartmentMapper.selectByExample(example);
		return CopyPropertiesUtil.copyList(deptList, ManageDepartmentBO.class);
	}

	@Override
	public List<ManageDepartmentBO> listByCompany(String company) {
		ManageDepartmentExample example = new ManageDepartmentExample();
		ManageDepartmentExample.Criteria criteria = example.createCriteria();
		//criteria.andCompanyEqualTo(company);
		criteria.andEnabledEqualTo(true);
		List<ManageDepartment> deptList = manageDepartmentMapper.selectByExample(example);
		return CopyPropertiesUtil.copyList(deptList, ManageDepartmentBO.class);
	}

	@Override
	public List<ManageDepartmentTreeBO> listAll(List<Long> idList, Boolean flag) {
		ManageDepartmentExample example = new ManageDepartmentExample();
		ManageDepartmentExample.Criteria criteria = example.createCriteria();
		if (CollectionUtils.isNotEmpty(idList)){
			criteria.andIdIn(idList);
		}
		criteria.andEnabledEqualTo(true);
		List<ManageDepartment> deptList = manageDepartmentMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(deptList)){
			return new ArrayList<>(0);
		}
		//拿到父节点
		List<ManageDepartment> parentList = deptList.stream().filter(d -> Objects.isNull(d.getParentId())).collect(Collectors.toList());
		List<ManageDepartment> childList = deptList.stream().filter(d -> Objects.nonNull(d.getParentId())).collect(Collectors.toList());
		DeptTreeToolUtils toolUtils = new DeptTreeToolUtils(CopyPropertiesUtil.copyList(parentList, ManageDepartmentTreeBO.class), CopyPropertiesUtil.copyList(childList, ManageDepartmentTreeBO.class));
		//组织架构用户map
		Map<Long, List<SimpleDeptUserBO>> userMap = null;
		if (flag){
			userMap = userService.mapUserByDeptIds(idList);
		}
		List<ManageDepartmentTreeBO> treeBOList =  toolUtils.getTree(flag, userMap);
		return treeBOList;
	}

	@Override
	public List<ManageDepartmentTreeBO> getOrganizationList(ManageDepartmetTreeParamBO departmetTreeParamBO) throws Exception {
		// 根据组织架构idList获取组织架构树
		List<ManageDepartmentTreeBO> manageDepartmentList = manageDepartmentMapperExt.getOrganizationList(departmetTreeParamBO.getIdList());
		return manageDepartmentList;
	}

	@Override
	public List<ManageDepartmentTreeBO> getOrgList(Byte deptType, Boolean flag) throws Exception {
		// 根据userId和组织架构层级type获取对应的组织架构id
		OrganizationInfoParamBO paramBO = new OrganizationInfoParamBO();
		paramBO.setUserId(ContextUtil.getManageCurrentUser().getUserId());
		paramBO.setDeptType(deptType);
		List<Long> deptIdList = userService.getOrganizationInfo(paramBO);
		if(CollectionUtils.isEmpty(deptIdList)){
			throw new BusinessException(HoolinkExceptionMassageEnum.ORG_LIST_TREE_ERROR);
		}
		// 根据组织架构id集合和是否需要查询架构下人员标识获取组织架构树
		List<ManageDepartmentTreeBO> manageDepartmentList = manageDepartmentMapperExt.getOrganizationList(deptIdList);
		if(CollectionUtils.isEmpty(manageDepartmentList)){
			throw new BusinessException(HoolinkExceptionMassageEnum.ORG_LIST_TREE_ERROR);
		}
		if (flag){
			//组织架构用户map
			List<ManageDepartmentTreeBO> boList = manageDepartmentList.stream().filter(m -> CollectionUtils.isEmpty(m.getChildTreeList())).collect(Collectors.toList());
			Map<Long, List<SimpleDeptUserBO>> userMap = userService.mapUserByDeptIds(deptIdList);
			boList.stream().forEach(b ->{
				b.setUserList(userMap.get(b.getId()));
			});
		}
		return manageDepartmentList;
	}

}
