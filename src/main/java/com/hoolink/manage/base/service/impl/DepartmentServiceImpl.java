package com.hoolink.manage.base.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hoolink.manage.base.bo.DeptPositionBO;
import com.hoolink.manage.base.constant.Constant;
import com.hoolink.manage.base.dao.mapper.ManageDepartmentMapper;
import com.hoolink.manage.base.dao.mapper.ext.ManageDepartmentMapperExt;
import com.hoolink.manage.base.dao.mapper.ext.MiddleUserDepartmentMapperExt;
import com.hoolink.manage.base.dao.mapper.ext.UserMapperExt;
import com.hoolink.manage.base.dao.model.ManageDepartment;
import com.hoolink.manage.base.dao.model.ManageDepartmentExample;
import com.hoolink.manage.base.service.DepartmentService;
import com.hoolink.manage.base.service.UserService;
import com.hoolink.manage.base.util.DeptTreeToolUtils;
import com.hoolink.sdk.bo.edm.DepartmentAndUserTreeBO;
import com.hoolink.sdk.bo.edm.DeptVisibleCacheBO;
import com.hoolink.sdk.bo.edm.TreeParamBO;
import com.hoolink.sdk.bo.manager.*;
import com.hoolink.sdk.enums.edm.EdmDeptEnum;
import com.hoolink.sdk.exception.BusinessException;
import com.hoolink.sdk.exception.HoolinkExceptionMassageEnum;
import com.hoolink.sdk.utils.ContextUtil;
import com.hoolink.sdk.utils.CopyPropertiesUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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

	@Resource
	private UserMapperExt userMapperExt;

	@Override
	public List<ManageDepartmentBO> listByIdList(List<Long> idList) {
		if(CollectionUtils.isEmpty(idList)) {
			return Collections.emptyList();
		}
		List<ManageDepartment> deptList = getManageDepartments(idList);
		return CopyPropertiesUtil.copyList(deptList, ManageDepartmentBO.class);
	}

	public List<ManageDepartment> listById(List<Long> idList) {
		if(CollectionUtils.isEmpty(idList)) {
			return Collections.emptyList();
		}
		List<ManageDepartment> deptList = getManageDepartments(idList);
		return deptList;
	}

	private List<ManageDepartment> getManageDepartments(List<Long> idList) {
		ManageDepartmentExample example = new ManageDepartmentExample();
		ManageDepartmentExample.Criteria criteria = example.createCriteria();
		criteria.andIdIn(idList);
		criteria.andEnabledEqualTo(true);
		return manageDepartmentMapper.selectByExample(example);
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
		ManageDepartmentExample.Criteria criteria = example.createCriteria();
		criteria.andEnabledEqualTo(true);
		List<ManageDepartment> deptList = manageDepartmentMapper.selectByExample(example);
		return CopyPropertiesUtil.copyList(deptList, ManageDepartmentBO.class);
	}

	@Override
	public List<DepartmentAndUserTreeBO> listAll(TreeParamBO paramBO) {
		boolean belongJR = false;
		boolean belongHL = false;
		if (Objects.nonNull(paramBO.getBelongId())){
			belongJR = Constant.JR_PARENT_ID_CODE.indexOf(String.valueOf(paramBO.getBelongId())) > 0;
			belongHL = Constant.HL_PARENT_ID_CODE.indexOf(String.valueOf(paramBO.getBelongId())) > 0;
		}else {
			return new ArrayList<>(0);
		}
		List<ManageDepartment> departmentList ;
		List<SimpleDeptUserBO>  userBOList = null;
		//是否直接勾选搜索人员
		boolean isDirectChecked = false;
		if (StringUtils.isNotBlank(paramBO.getName())){
			userBOList = userMapperExt.selectUserAndDeptByUserName(paramBO.getName());
			if (CollectionUtils.isEmpty(userBOList)){
				return new ArrayList<>();
			}
			Set<Long> departIds = getDepartIds(userBOList, belongJR, belongHL);
			departmentList = listById(new ArrayList<>(departIds));
			isDirectChecked = true;
		}else {
			departmentList = getDepartmentByCompany(belongJR, belongHL);
		}
		if (CollectionUtils.isEmpty(departmentList)){
			return new ArrayList<>();
		}
		//拿到父节点
		List<ManageDepartment> allParentList = departmentList.stream().filter(d -> Objects.isNull(d.getParentId()) || d.getParentId() == 0).collect(Collectors.toList());
		//过滤两个list中相同的元素放到新集合中
		List<ManageDepartment> parentList = departmentList.stream().filter(d1 -> allParentList.stream().map(ManageDepartment::getId).collect(Collectors.toList()).contains(d1.getId())).collect(Collectors.toList());
		List<ManageDepartment> childList = departmentList.stream().filter(d -> Objects.nonNull(d.getParentId())).collect(Collectors.toList());
		DeptTreeToolUtils toolUtils = new DeptTreeToolUtils(convertToDepartmentAndUserTree(parentList, isDirectChecked), convertToDepartmentAndUserTree(childList, isDirectChecked));
		//组织架构用户map
		Map<Long, List<SimpleDeptUserBO>> userMap = null;
		if (paramBO.getShowUser()){
			if (StringUtils.isNotBlank(paramBO.getName())){
				userMap = userBOList.stream().distinct().collect(Collectors.groupingBy(SimpleDeptUserBO::getDeptId));
			}else {
				userMap = userService.mapUserByDeptIds(null);
			}
		}
		List<DepartmentAndUserTreeBO> treeBOList = toolUtils.getTree(paramBO.getShowUser(), userMap, paramBO.getCheckedList(), isDirectChecked);
		return treeBOList;
	}

	private Set<Long> getDepartIds(List<SimpleDeptUserBO> userBOList, boolean belongJR, boolean belongHL) {
		List<Long> deptIds = userBOList.stream().map(SimpleDeptUserBO::getDeptId).collect(Collectors.toList());
		ManageDepartmentExample example = new ManageDepartmentExample();
		ManageDepartmentExample.Criteria criteria = example.createCriteria();
		criteria.andIdIn(deptIds);
		List<ManageDepartment> manageDepartments = manageDepartmentMapper.selectByExample(example);
		if (belongJR){
			manageDepartments = manageDepartments.stream().filter(m -> m.getParentIdCode().indexOf(Constant.JR_PARENT_ID_CODE) == 0).collect(Collectors.toList());
		}else if (belongHL){
			manageDepartments = manageDepartments.stream().filter(m -> m.getParentIdCode().indexOf(Constant.HL_PARENT_ID_CODE) == 0).collect(Collectors.toList());
		}
		Set<Long> departIds = new HashSet<>();
		for (ManageDepartment department : manageDepartments){
			List<Long> ids = Arrays.stream(department.getParentIdCode().split(Constant.UNDERLINE)).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
			departIds.addAll(ids);
		}
		return departIds;
	}

	private List<ManageDepartment> getDepartmentByCompany(Boolean belongJR, Boolean belongHL){
		ManageDepartmentExample example = new ManageDepartmentExample();
		ManageDepartmentExample.Criteria criteria = example.createCriteria();
		ManageDepartmentExample.Criteria criteria2 = example.createCriteria();
		criteria.andEnabledEqualTo(true);
		if (belongJR && !belongHL){
			//当前用户所属晶日
			criteria.andParentIdCodeLike(Constant.JR_PARENT_ID_CODE + Constant.PERCENT);
		}else if (belongHL && !belongJR){
			//当前用户所属互灵
			criteria.andParentIdCodeLike(Constant.HL_PARENT_ID_CODE + Constant.PERCENT);
		}else {
			//属于晶日和互灵
			criteria.andParentIdCodeLike(Constant.HL_PARENT_ID_CODE + Constant.PERCENT);
			criteria2.andEnabledEqualTo(true);
			criteria2.andParentIdCodeLike(Constant.JR_PARENT_ID_CODE + Constant.PERCENT);
			example.or(criteria2);
		}
		List<ManageDepartment> deptList = manageDepartmentMapper.selectByExample(example);
		return deptList;
	}

	private List<DepartmentAndUserTreeBO> convertToDepartmentAndUserTree(List<ManageDepartment> departmentList, boolean isDirectChecked){
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
			if (isDirectChecked){
				departmentAndUserTreeBO.setChecked(Boolean.TRUE);
			}else {
				departmentAndUserTreeBO.setChecked(Boolean.FALSE);
			}
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

	@Override
	public List<ManageDepartmentTreeBO> getOrgListTree(DepartmentTreeParamBO treeParamBO) throws Exception {
		// 获取权限范围内的组织架构信息
		PermissionManageDeptBO manageDeptBO = getPermissionManageDeptBO();
		//组装组织架构树
		List<ManageDepartmentTreeBO> topList = manageDeptBO.getManageDepartmentList();
		List<ManageDepartmentTreeBO> allManageDeptList = manageDeptBO.getAllManageDepartmentList();
		List<ManageDepartmentTreeBO> childList = allManageDeptList.stream().filter(manageDepartmentTreeBO -> Objects.nonNull(manageDepartmentTreeBO.getParentId())).collect(Collectors.toList());
		List<ManageDepartmentTreeBO> manageDepartmentTreeBOS = getResourceTree(topList, childList);
		return manageDepartmentTreeBOS;
	}

	private List<ManageDepartmentTreeBO> getResourceTree(List<ManageDepartmentTreeBO> rootList, List<ManageDepartmentTreeBO> bodyList){
		if (bodyList != null && !bodyList.isEmpty()) {
			//声明一个map，用来过滤已操作过的数据
			Map<Long, Long> map = Maps.newHashMapWithExpectedSize(bodyList.size());
			rootList.forEach(beanTree -> getChild(beanTree, map, bodyList));
			return rootList;
		}
		return null;
	}

	public void getChild(ManageDepartmentTreeBO treeBO, Map<Long, Long> map, List<ManageDepartmentTreeBO> bodyList) {
		List<ManageDepartmentTreeBO> childList = Lists.newArrayList();
		bodyList.stream()
				.filter(c -> !map.containsKey(c.getKey()))
				.filter(c -> c.getParentId().equals(treeBO.getKey()))
				.forEach(c -> {
					// 把已经处理过的数据组装起来，用来过滤已经操作过的数据
					map.put(c.getKey(), c.getParentId());
					// 递归
					getChild(c, map, bodyList);
					c.setExpand(true);
					childList.add(c);
				});
		treeBO.setChildren(childList);

	}

	@Override
	public OrganizationDeptBO getOrganization(OrganizationDeptParamBO paramBO) throws Exception {
      OrganizationDeptBO organizationDeptBO = new OrganizationDeptBO();
      ManageDepartment manageDepartment = manageDepartmentMapper.selectByPrimaryKey(paramBO.getDeptId());
      if(manageDepartment != null){
         if(EdmDeptEnum.POSITION.getKey().byteValue() == manageDepartment.getDeptType()){
             organizationDeptBO.setGroupName(manageDepartment.getName());
              getParentOrganization(manageDepartment.getParentId(),organizationDeptBO);
          }
          if(EdmDeptEnum.DEPT.getKey().byteValue() == manageDepartment.getDeptType()){
              organizationDeptBO.setDeptName(manageDepartment.getName());
              getParentOrganization(manageDepartment.getParentId(),organizationDeptBO);
						  getChildrenOrganization(manageDepartment.getId(),organizationDeptBO);
          }

          if(EdmDeptEnum.SYSTEM_CENTER.getKey().byteValue() == manageDepartment.getDeptType()){
              organizationDeptBO.setSystemCenterName(manageDepartment.getName());
              getParentOrganization(manageDepartment.getParentId(),organizationDeptBO);
              getChildrenOrganization(manageDepartment.getId(),organizationDeptBO);
          }

				if(EdmDeptEnum.COMPANY.getKey().byteValue() == manageDepartment.getDeptType()){
					organizationDeptBO.setCompanyName(manageDepartment.getName());
					organizationDeptBO.setDeptId(manageDepartment.getId());
				}

      }
      return organizationDeptBO;
	}

	@Override
	public PermissionManageDeptBO getOrgInfoList(DepartmentTreeParamBO treeParamBO) throws Exception {
		PermissionManageDeptBO manageDeptBO = new PermissionManageDeptBO();
		// 1.赋查询参数
		List<Long> deptIdList = new ArrayList<>();
		deptIdList.add(treeParamBO.getDeptId());

		// 2.根据组织架构id集合获取组织架信息
		List<ManageDepartmentTreeBO> manageDepartmentList = manageDepartmentMapperExt.getOrgInfoList(deptIdList);
		if(CollectionUtils.isEmpty(manageDepartmentList)){
			return null;
		}
		manageDeptBO.setManageDepartmentList(manageDepartmentList);

		// 3.获取所有组织架构信息
		List<ManageDepartmentTreeBO> manageDepartmentTreeBOS = manageDepartmentMapperExt.getAllOrgInfoList();
		manageDeptBO.setAllManageDepartmentList(manageDepartmentTreeBOS);
		return manageDeptBO;
	}

	@Override
	public PermissionManageDeptBO getOrgInfoListToDept(DepartmentTreeParamBO treeParamBO) throws Exception {
		PermissionManageDeptBO manageDeptBO = getPermissionManageDeptBO();
		return manageDeptBO;
	}


	private PermissionManageDeptBO getPermissionManageDeptBO() throws Exception {
		PermissionManageDeptBO manageDeptBO = new PermissionManageDeptBO();
		// 1.根据userIde获取对应的组织架构信息
		OrganizationInfoParamBO paramBO = new OrganizationInfoParamBO();
		paramBO.setUserId(ContextUtil.getManageCurrentUser().getUserId());
		List<UserDeptAssociationBO> deptInfoList = userService.getOrganizationInfoToDept(paramBO);
		if(CollectionUtils.isEmpty(deptInfoList)){
			throw new BusinessException(HoolinkExceptionMassageEnum.ORG_LIST_TREE_ERROR);
		}
		// 2.过滤密保等级不为空的部门数据
		List<Long> deptIdList = deptInfoList.stream().filter(data -> !Objects.isNull(data.getEncryLevelDept()) && data.getLowestLevel()).map(UserDeptAssociationBO::getDeptId).collect(Collectors.toList());
		if(CollectionUtils.isEmpty(deptIdList)){
			throw new BusinessException(HoolinkExceptionMassageEnum.ORG_LIST_TREE_ERROR);
		}
		// 3.根据组织架构id集合获取组织架信息
		List<ManageDepartmentTreeBO> manageDepartmentList = manageDepartmentMapperExt.getDeptByParentIdCode(deptIdList);
		manageDeptBO.setAllManageDepartmentList(manageDepartmentList);
		List<ManageDepartmentTreeBO> departmentList = manageDepartmentList.stream().filter(data -> Constant.DEPT_LEVEL.equals(data.getDeptType())).collect(Collectors.toList());
		manageDeptBO.setManageDepartmentList(departmentList);
		return manageDeptBO;
	}

	@Override
	public String getDeptInfo(Long deptId) throws Exception {
	    if(deptId == null) {
	        return null;
      }
      ManageDepartment department = manageDepartmentMapper.selectByPrimaryKey(deptId);
      if(department == null || department.getDeptType().intValue() == EdmDeptEnum.COMPANY.getKey().intValue() ){
          return null;
      }
      // 组
      if(department.getDeptType().intValue() == EdmDeptEnum.POSITION.getKey().intValue()){
          department = manageDepartmentMapper.selectByPrimaryKey(department.getParentId());
      }
		return department.getName();
	}

    @Override
    public List<ReadFileOrgInfoBO> getFileOrgList(List<Long> deptId) throws Exception {
        List<ReadFileOrgInfoBO> orgInfoBOList = new ArrayList<>();
        if (deptId == null) {
            return null;
        }
        List<ManageDepartment> deptList = getDeptList(deptId);
        if (deptList == null) {
            return null;
        }
        Map<Long, String> idAndParentIdCodeMap = deptList.stream().collect(Collectors.toMap(ManageDepartment::getId, ManageDepartment::getParentIdCode));
        for (Map.Entry<Long, String> entry : idAndParentIdCodeMap.entrySet()) {
            Long key = entry.getKey();
            String value = entry.getValue();
            ReadFileOrgInfoBO readFileOrgInfoBO = new ReadFileOrgInfoBO();
			List<Long> id = Arrays.stream(value.split(Constant.UNDERLINE)).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            List<ManageDepartment> deptParentIdList = getDeptList(id);
            List<String> companyName = deptParentIdList.stream().filter(a -> a.getDeptType().intValue() == 1).map(ManageDepartment::getName).collect(Collectors.toList());
            List<String> deptName = deptParentIdList.stream().filter(a -> a.getDeptType().intValue() == 2).map(ManageDepartment::getName).collect(Collectors.toList());
            List<String> groupName = deptParentIdList.stream().filter(a -> a.getDeptType().intValue() == 3).map(ManageDepartment::getName).collect(Collectors.toList());
            List<String> orgName = deptParentIdList.stream().filter(a -> a.getDeptType().intValue() == 4).map(ManageDepartment::getName).collect(Collectors.toList());
            String orgLevel = null;
            if(CollectionUtils.isNotEmpty(companyName)){
                orgLevel = companyName.get(0)+ Constant.BACKSLASH;
            }
            if(CollectionUtils.isNotEmpty(orgName)){
                orgLevel = orgLevel + orgName.get(0) + Constant.BACKSLASH;
            }
            if(CollectionUtils.isNotEmpty(deptName)){
                orgLevel = orgLevel + deptName.get(0) + Constant.BACKSLASH;
            }
            if(CollectionUtils.isNotEmpty(groupName)){
                orgLevel = orgLevel + groupName.get(0)+ Constant.BACKSLASH;
            }
            readFileOrgInfoBO.setOrgLevel(orgLevel);
            readFileOrgInfoBO.setDepartmentId(key);
            orgInfoBOList.add(readFileOrgInfoBO);
        }
      return orgInfoBOList;
    }

  private  List<ManageDepartment> getDeptList(List<Long> deptId){
      ManageDepartmentExample departmentExample = new ManageDepartmentExample();
      ManageDepartmentExample.Criteria criteria = departmentExample.createCriteria();
      criteria.andIdIn(deptId).andEnabledEqualTo(true);
     return manageDepartmentMapper.selectByExample(departmentExample);
  }

	private void getParentOrganization(Long parentId, OrganizationDeptBO organizationDeptBO) {
		ManageDepartmentExample departmentExample = new ManageDepartmentExample();
		ManageDepartmentExample.Criteria criteria = departmentExample.createCriteria();
		criteria.andIdEqualTo(parentId).andEnabledEqualTo(true);
		List<ManageDepartment> manageDepartments = manageDepartmentMapper.selectByExample(departmentExample);
		if (CollectionUtils.isNotEmpty(manageDepartments)) {
			ManageDepartment manageDepartment = manageDepartments.get(0);
			if (EdmDeptEnum.DEPT.getKey().byteValue() == manageDepartment.getDeptType()) {
				organizationDeptBO.setDeptName(manageDepartment.getName());
			}
			if (EdmDeptEnum.COMPANY.getKey().byteValue() == manageDepartment.getDeptType()) {
				organizationDeptBO.setCompanyName(manageDepartment.getName());
				organizationDeptBO.setDeptId(manageDepartment.getId());
			}
			if (EdmDeptEnum.SYSTEM_CENTER.getKey().byteValue() == manageDepartment.getDeptType()) {
				organizationDeptBO.setSystemCenterName(manageDepartment.getName());
			}
			if (manageDepartment.getParentId() != null) {
				getParentOrganization(manageDepartment.getParentId(), organizationDeptBO);
			}

		}
	}

	private void getChildrenOrganization(Long id, OrganizationDeptBO organizationDeptBO){
		ManageDepartmentExample departmentExample = new ManageDepartmentExample();
		ManageDepartmentExample.Criteria criteria = departmentExample.createCriteria();
		criteria.andParentIdEqualTo(id).andEnabledEqualTo(true);
		List<ManageDepartment> manageDepartments = manageDepartmentMapper.selectByExample(departmentExample);
		if(CollectionUtils.isNotEmpty(manageDepartments)){
			ManageDepartment manageDepartment = manageDepartments.get(0);
			if(EdmDeptEnum.POSITION.getKey().byteValue() == manageDepartment.getDeptType()){
				organizationDeptBO.setGroupName(manageDepartment.getName());
			}
			if(EdmDeptEnum.DEPT.getKey().byteValue() == manageDepartment.getDeptType()){
            organizationDeptBO.setDeptName(manageDepartment.getName());
            getChildrenOrganization(manageDepartment.getId(),organizationDeptBO);
        }
		}
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

    @Override
    public List<DeptVisibleCacheBO> getDeptListByUserId() {
        //获取当前用户所有的关联部门信息
        List<DeptPositionBO> deptList = middleUserDepartmentMapperExt.getDeptParentIdCode(ContextUtil.getManageCurrentUser().getUserId());
        List<DeptVisibleCacheBO> deptVisibleCacheBOS = CopyPropertiesUtil.copyList(deptList, DeptVisibleCacheBO.class);
        return deptVisibleCacheBOS;
    }

}
