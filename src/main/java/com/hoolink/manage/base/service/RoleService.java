package com.hoolink.manage.base.service;

import java.util.List;
import java.util.Set;

import com.github.pagehelper.PageInfo;
import com.hoolink.manage.base.bo.*;
import com.hoolink.manage.base.dao.model.ManageRole;

/**
 * @description: 角色管理
 * @author: WeiMin
 * @date: 2019-05-13
 **/
public interface RoleService {

    /**
     * 创建角色
     * @param roleParamBO
     * @return
     * @throws Exception
     */
    Long create(RoleParamBO roleParamBO) throws Exception;

    /**
     * 修改角色
     * @param roleParamBO
     * @throws Exception
     */
    void update(RoleParamBO roleParamBO) throws Exception;

    /**
     * 修改角色状态
     * @param roleParamBO
     * @throws Exception
     */
    void updateStatus(RoleParamBO roleParamBO) throws Exception;

    /**
     * 获得角色信息
     * @param roleId
     * @return
     * @throws Exception
     */
    BackRoleBO getById(Long roleId) throws Exception;

    /**
     * 获得角色基础信息
     * @param roleId
     * @return
     * @throws Exception
     */
    ManageRoleBO getBaseRole(Long roleId) throws Exception;

    /**
     * 分页获得角色列表
     * @param pageParamBO
     * @return
     * @throws Exception
     */
    PageInfo<RoleParamBO> listByPage(SearchPageParamBO pageParamBO) throws Exception;
    
    /**
     * 获取id集合获取角色
     * @param idList
     * @return
     * @throws Exception
     */
    List<ManageRoleBO> listByIdList(List<Long> idList) throws Exception;
    
    /**
     * 获取所有角色
     * @return
     * @throws Exception
     */
    List<ManageRoleBO> list();
    
    /**
     * 根据roleId获取菜单的权限类别
     * @param roleId
     * @return
     */
    List<RoleMenuPermissionBO> listMenuAccessByRoleId(Long roleId);
    /**
     * 根据roleId获取权限url
     * @param roleId
     * @return
     */
    Set<String> listAccessUrlByRoleId(Long roleId);
    
    /**
     * 根据id获取角色
     * @param roleId
     * @return
     */
    ManageRoleBO selectById(Long roleId);
}
