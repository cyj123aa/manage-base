package com.hoolink.manage.base.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hoolink.manage.base.bo.*;
import com.hoolink.manage.base.constant.Constant;
import com.hoolink.manage.base.dao.mapper.ManageRoleMapper;
import com.hoolink.manage.base.dao.mapper.MiddleRoleMenuMapper;
import com.hoolink.manage.base.dao.mapper.ext.ManageRoleMapperExt;
import com.hoolink.manage.base.dao.mapper.ext.MiddleRoleMenuMapperExt;
import com.hoolink.manage.base.dao.model.*;
import com.hoolink.manage.base.service.MenuService;
import com.hoolink.manage.base.service.RoleService;
import com.hoolink.manage.base.util.RedisUtil;
import com.hoolink.manage.base.vo.req.MiddleRoleMenuVO;
import com.hoolink.manage.base.vo.req.PageParamVO;
import com.hoolink.manage.base.vo.req.RoleParamVO;
import com.hoolink.sdk.exception.BusinessException;
import com.hoolink.sdk.exception.HoolinkExceptionMassageEnum;
import com.hoolink.sdk.utils.ContextUtil;
import com.hoolink.sdk.utils.CopyPropertiesUtil;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description: 角色管理
 * @author: WeiMin
 * @date: 2019-05-13
 **/
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
	private static final long SESSION_TIMEOUT_SECONDS = 60 * 60 * 24 * 7L;
    @Resource
    private ManageRoleMapper roleMapper;
    @Resource
    private MiddleRoleMenuMapper roleMenuMapper;
    @Resource
    private MiddleRoleMenuMapperExt roleMenuMapperExt;
    @Resource
    private ManageRoleMapperExt manageRoleMapperExt;
    @Autowired
    private MenuService menuService;
    @Resource
    RedisUtil redisUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(RoleParamBO roleParamBO ) throws Exception {
        List<MiddleRoleMenuBO> roleMenuVOList = roleParamBO.getRoleMenuVOList();
        if(CollectionUtils.isEmpty(roleMenuVOList)){
            throw new BusinessException(HoolinkExceptionMassageEnum.PARAM_ERROR);
        }
        //role 等级
        ManageRole role = CopyPropertiesUtil.copyBean(roleParamBO, ManageRole.class);
        role.setEnabled(true);
        role.setRoleStatus(true);
        role.setCreated(System.currentTimeMillis());
        role.setCreator(ContextUtil.getManageCurrentUser().getUserId());
        //维护父节点 等级（1--3）
        ManageRole userRole = getUserRole();
        if(userRole!=null){
            role.setParentId(userRole.getId());
            if(Constant.LEVEL_THREE.equals(userRole.getRoleLevel())){
                throw new BusinessException(HoolinkExceptionMassageEnum.NOT_AUTH);
            }else if (Constant.LEVEL_THREE > userRole.getRoleLevel()){
                role.setRoleLevel((byte)((int)userRole.getRoleLevel()+1));
            }
        }
        roleMapper.insertSelective(role);
        //權限
        List<MiddleRoleMenu> middleRoleMenus = CopyPropertiesUtil.copyList(roleMenuVOList, MiddleRoleMenu.class);
        middleRoleMenus.forEach(middleRoleMenu -> middleRoleMenu.setRoleId(role.getId()));
        //批量創建
        roleMenuMapperExt.bulkInsert(middleRoleMenus);
        //保存角色对应权限到redis
        saveRoleMenuPermissionToRedis(middleRoleMenus);
        return role.getId();
    }
    
    /**
     * 保存角色对应权限到redis
     * @param middleRoleMenus
     */
    private void saveRoleMenuPermissionToRedis(List<MiddleRoleMenu> middleRoleMenus) {
    	String roleId = middleRoleMenus.get(0).getRoleId().toString(); 
    	redisUtil.del(roleId);
		List<Long> menuIdList = middleRoleMenus.stream().map(mrm -> mrm.getMenuId()).collect(Collectors.toList());
		List<ManageMenuBO> menus = menuService.listByIdList(menuIdList);
		
		Map<String, Object> map = new HashMap<>(15);
    	middleRoleMenus.stream().forEach(mrm -> {
    		RoleMenuPermissionBO roleMenuPermission = new RoleMenuPermissionBO();
    		BeanUtils.copyProperties(mrm, roleMenuPermission);
    		ManageMenuBO menu = menus.stream().filter(m -> m.getId().longValue()==mrm.getMenuId().longValue()).findFirst().orElseGet(ManageMenuBO::new);
    		roleMenuPermission.setMenuCode(menu.getMenuCode());
    		map.put(roleMenuPermission.getMenuCode(), roleMenuPermission);
    	});
    	redisUtil.hmset(roleId, map, SESSION_TIMEOUT_SECONDS);
    }

    /**
     * 获得用户角色
     * @return
     * @throws Exception
     */
    private ManageRole getUserRole() throws Exception {
        Long userId = ContextUtil.getManageCurrentUser().getUserId();
        if(userId==null){
            throw new BusinessException(HoolinkExceptionMassageEnum.USER_USER_NOT_EXIST);
        }
        return manageRoleMapperExt.getUserRole(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RoleParamBO roleParamBO) throws Exception {
        if(roleParamBO.getId()==null){
            throw new BusinessException(HoolinkExceptionMassageEnum.PARAM_ERROR);
        }
        updateRole(roleParamBO);
        List<MiddleRoleMenuBO> roleMenuVOList = roleParamBO.getRoleMenuVOList();
        //權限 传参：勾选的权限
        MiddleRoleMenuExample example=new MiddleRoleMenuExample();
        example.createCriteria().andRoleIdEqualTo(roleParamBO.getId());
        roleMenuMapper.deleteByExample(example);
        if(CollectionUtils.isEmpty(roleMenuVOList)){
            return;
        }
        List<MiddleRoleMenu> middleRoleMenus = CopyPropertiesUtil.copyList(roleMenuVOList, MiddleRoleMenu.class);
        middleRoleMenus.forEach(middleRoleMenu -> middleRoleMenu.setRoleId(roleParamBO.getId()));
        //批量創建
        roleMenuMapperExt.bulkInsert(middleRoleMenus);
        //保存角色对应权限到redis
        saveRoleMenuPermissionToRedis(middleRoleMenus);
    }

    @Override
    public void updateStatus(RoleParamBO roleParamBO) throws Exception {
        if(roleParamBO.getId()==null || roleParamBO.getRoleStatus()!=null){
            throw new BusinessException(HoolinkExceptionMassageEnum.PARAM_ERROR);
        }
        updateRole(roleParamBO);
    }

    /**
     * 更新详情
     * @param roleParamBO
     */
    private void updateRole(RoleParamBO roleParamBO) {
        ManageRole role = CopyPropertiesUtil.copyBean(roleParamBO, ManageRole.class);
        role.setUpdated(System.currentTimeMillis());
        role.setUpdator(ContextUtil.getManageCurrentUser().getUserId());
        roleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public RoleParamBO getById(Long roleId) throws Exception {
        ManageRoleExample example=new ManageRoleExample();
        example.createCriteria().andEnabledEqualTo(true).andIdEqualTo(roleId);
        List<ManageRole> roles = roleMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(roles)){
            throw new BusinessException(HoolinkExceptionMassageEnum.ROLE_USER_NOT_EXIST);
        }
        //角色详情
        RoleParamBO roleParamBO = CopyPropertiesUtil.copyBean(roles.get(0), RoleParamBO.class);
        MiddleRoleMenuExample example1=new MiddleRoleMenuExample();
        example1.createCriteria().andRoleIdEqualTo(roleId);
        List<MiddleRoleMenu> middleRoleMenus = roleMenuMapper.selectByExample(example1);
        if(CollectionUtils.isEmpty(middleRoleMenus)){
            return roleParamBO;
        }
        List<MiddleRoleMenuBO> roleMenuVOList=CopyPropertiesUtil.copyList(middleRoleMenus,MiddleRoleMenuBO.class);
        List<Long> menuIdList = new ArrayList<>();
        roleMenuVOList.forEach(middleRoleMenuVO -> menuIdList.add(middleRoleMenuVO.getMenuId()));
        //角色菜单
        List<ManageMenuBO> manageMenus = menuService.listByIdList(menuIdList);
        for (MiddleRoleMenuBO menuBO:roleMenuVOList){
            for (ManageMenuBO menu:manageMenus){
                if(menuBO.getMenuId().equals(menu.getId())){
                    menuBO.setMenuName(menu.getMenuName());
                    menuBO.setParentId(menu.getParentId());
                    menuBO.setPriority(menu.getPriority());
                }
            }
        }
        roleParamBO.setRoleMenuVOList(roleMenuVOList);
        return roleParamBO;
    }

    @Override
    public PageInfo<RoleParamBO> listByPage(SearchPageParamBO pageParamBO) throws Exception {
        if(pageParamBO.getPageNo()==null || pageParamBO.getPageSize()==null){
            throw new BusinessException(HoolinkExceptionMassageEnum.PARAM_ERROR);
        }
        //一级用户(全部角色) 二级用户(自己及创建的角色)  三级用户不能查看
        PageHelper.startPage(pageParamBO.getPageNo(), pageParamBO.getPageSize());
        ManageRoleExample example=new ManageRoleExample();
        example.setOrderByClause("created desc");
        //获得用户角色
        ManageRole userRole = getUserRole();
        List<ManageRole> roles=null;
        if(userRole!=null){
            Byte roleLevel = userRole.getRoleLevel();
            if(Constant.LEVEL_THREE.equals(roleLevel)){
                throw new BusinessException(HoolinkExceptionMassageEnum.NOT_AUTH);
            }else if (Constant.LEVEL_TWO.equals(roleLevel)){
                roles = manageRoleMapperExt.getRoleByTwo(userRole.getId(),pageParamBO.getSearchValue());
            }else if (Constant.LEVEL_ONE.equals(roleLevel)){
                roles = manageRoleMapperExt.getRoleByOne(pageParamBO.getSearchValue());
            }
        }
        List<RoleParamBO> roleParamBOS = CopyPropertiesUtil.copyList(roles, RoleParamBO.class);
        PageInfo<RoleParamBO> pageInfo = new PageInfo<>(roleParamBOS);
        return pageInfo;
    }

	@Override
	public List<ManageRoleBO> listByIdList(List<Long> idList) throws Exception {
		if(CollectionUtils.isEmpty(idList)) {
			return new ArrayList<>();
		}
		ManageRoleExample example = new ManageRoleExample();
		ManageRoleExample.Criteria criteria = example.createCriteria();
        criteria.andEnabledEqualTo(true);
        criteria.andIdIn(idList);
        List<ManageRole> roleList = roleMapper.selectByExample(example);
		return CopyPropertiesUtil.copyList(roleList, ManageRoleBO.class);
	}

	@Override
	public List<ManageRoleBO> list(){
		ManageRoleExample example = new ManageRoleExample();
		ManageRoleExample.Criteria criteria = example.createCriteria();
        criteria.andEnabledEqualTo(true);
        List<ManageRole> roleList = roleMapper.selectByExample(example);
		return CopyPropertiesUtil.copyList(roleList, ManageRoleBO.class);
	}

	@Override
	public List<RoleMenuPermissionBO> listMenuAccessByRoleId(Long roleId) {
		MiddleRoleMenuExample example = new MiddleRoleMenuExample();
		MiddleRoleMenuExample.Criteria criteria = example.createCriteria();
		criteria.andRoleIdEqualTo(roleId);
		List<MiddleRoleMenu> roleMenuList = roleMenuMapper.selectByExample(example);
		List<Long> menuIdList = roleMenuList.stream().map(rm -> rm.getMenuId()).collect(Collectors.toList());
		List<ManageMenuBO> menus = menuService.listByIdList(menuIdList);
		
		List<RoleMenuPermissionBO> roleMenuPermissionList = new ArrayList<>();
		roleMenuList.stream().forEach(rm -> {
			RoleMenuPermissionBO roleMenuPermission = new RoleMenuPermissionBO();
			BeanUtils.copyProperties(rm, roleMenuPermission);
			ManageMenuBO menu = menus.stream().filter(m -> m.getId().longValue() == rm.getMenuId().longValue()).findFirst().orElseGet(ManageMenuBO::new);
			roleMenuPermission.setMenuCode(menu.getMenuCode());
			roleMenuPermissionList.add(roleMenuPermission);
		});
		return roleMenuPermissionList;
	}
}
