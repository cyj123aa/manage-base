package com.hoolink.manage.base.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.hoolink.manage.base.bo.DepartmentTreeParamBO;
import com.hoolink.manage.base.bo.DeptPositionBO;
import com.hoolink.manage.base.constant.Constant;
import com.hoolink.manage.base.dao.mapper.ext.MiddleUserDepartmentMapperExt;
import com.hoolink.sdk.bo.edm.CheckedParamBO;
import com.hoolink.sdk.bo.edm.DepartmentAndUserTreeBO;
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

	@Resource
	private MiddleUserDepartmentMapperExt middleUserDepartmentMapperExt;
	
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
	public List<DepartmentAndUserTreeBO> listAll(Boolean flag, List<CheckedParamBO> checkedList) {
		List<DeptPositionBO> positionBOList = middleUserDepartmentMapperExt.getDept(ContextUtil.getManageCurrentUser().getUserId());
		if (CollectionUtils.isEmpty(positionBOList)){
			return new ArrayList<>(0);
		}
		List<ManageDepartment> departmentBOList = getTopDepartByDepartId(positionBOList.stream().map(DeptPositionBO::getId).collect(Collectors.toList()));
		ManageDepartmentExample example = new ManageDepartmentExample();
		ManageDepartmentExample.Criteria criteria = example.createCriteria();
		criteria.andEnabledEqualTo(true);
		List<ManageDepartment> deptList = manageDepartmentMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(deptList)){
			return new ArrayList<>(0);
		}
		//拿到父节点
		List<ManageDepartment> allParentList = deptList.stream().filter(d -> Objects.isNull(d.getParentId())).collect(Collectors.toList());
		//过滤两个list中相同的元素放到新集合中
		List<ManageDepartment> parentList = departmentBOList.stream().filter(d1 -> allParentList.stream().map(ManageDepartment::getId).collect(Collectors.toList()).contains(d1.getId())).collect(Collectors.toList());
		List<ManageDepartment> childList = deptList.stream().filter(d -> Objects.nonNull(d.getParentId())).collect(Collectors.toList());
		DeptTreeToolUtils toolUtils = new DeptTreeToolUtils(convertToDepartmentAndUserTree(parentList), convertToDepartmentAndUserTree(childList));
		//组织架构用户map
		Map<Long, List<SimpleDeptUserBO>> userMap = null;
		if (flag){
			userMap = userService.mapUserByDeptIds(null);
		}
		List<DepartmentAndUserTreeBO> treeBOList = toolUtils.getTree(flag, userMap, checkedList);
		return treeBOList;
	}

	private List<DepartmentAndUserTreeBO> convertToDepartmentAndUserTree(List<ManageDepartment> departmentList){
		List<DepartmentAndUserTreeBO> treeBOList = new ArrayList<>();
		for (ManageDepartment department : departmentList){
			DepartmentAndUserTreeBO departmentAndUserTreeBO = new DepartmentAndUserTreeBO();
			departmentAndUserTreeBO.setKey(department.getId());
			departmentAndUserTreeBO.setType(Constant.DEPARTMENT);
			departmentAndUserTreeBO.setTitle(department.getName());
			departmentAndUserTreeBO.setParentId(department.getParentId());
			departmentAndUserTreeBO.setValue(department.getId());
			//默认展开组织架构
			departmentAndUserTreeBO.setExpand(Boolean.TRUE);
			//默认全部取消勾选
			departmentAndUserTreeBO.setChecked(Boolean.FALSE);
			treeBOList.add(departmentAndUserTreeBO);
		}
		return treeBOList;
	}

	@Override
	public List<ManageDepartmentTreeBO> getOrganizationList(ManageDepartmetTreeParamBO departmetTreeParamBO) throws Exception {
		// 根据组织架构idList获取组织架构树
		List<ManageDepartmentTreeBO> manageDepartmentList = manageDepartmentMapperExt.getOrganizationList(departmetTreeParamBO.getIdList());
		return manageDepartmentList;
	}

	@Override
	public List<ManageDepartmentTreeBO> getOrgList(DepartmentTreeParamBO treeParamBO) throws Exception {
		// 根据userId和组织架构层级type获取对应的组织架构id
		OrganizationInfoParamBO paramBO = new OrganizationInfoParamBO();
		paramBO.setUserId(ContextUtil.getManageCurrentUser().getUserId());
		paramBO.setDeptType(treeParamBO.getDeptType());
		List<Long> deptIdList = userService.getOrganizationInfo(paramBO);
		if(CollectionUtils.isEmpty(deptIdList)){
			throw new BusinessException(HoolinkExceptionMassageEnum.ORG_LIST_TREE_ERROR);
		}
		// 根据组织架构id集合获取组织架构树
		List<ManageDepartmentTreeBO> manageDepartmentList = manageDepartmentMapperExt.getOrganizationList(deptIdList);
		if(CollectionUtils.isEmpty(manageDepartmentList)){
			return null;
		}
		return manageDepartmentList;
	}

	/**
	 * 传入idList是因为一个人可能属于多个部门
	 * @param departIdList
	 * @return
	 */
	private List<ManageDepartment> getTopDepartByDepartId(List<Long> departIdList){
		ManageDepartmentExample departmentExample = new ManageDepartmentExample();
		ManageDepartmentExample.Criteria criteria = departmentExample.createCriteria();
		criteria.andParentIdIn(departIdList);
		List<ManageDepartment> departmentList = manageDepartmentMapper.selectByExample(departmentExample);
		if (CollectionUtils.isEmpty(departmentList)){
			//为空说明已经是顶级节点了
			departmentExample.clear();
			criteria.andIdIn(departIdList);
			List<ManageDepartment> topDepart = manageDepartmentMapper.selectByExample(departmentExample);
			return topDepart;
		}
		return getTopDepartByDepartId(departmentList.stream().map(ManageDepartment::getId).collect(Collectors.toList()));
	}

}
