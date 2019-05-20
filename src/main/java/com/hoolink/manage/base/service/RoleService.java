package com.hoolink.manage.base.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.hoolink.manage.base.bo.ManageRoleBO;
import com.hoolink.manage.base.bo.RoleMenuPermissionBO;
import com.hoolink.manage.base.vo.req.PageParamVO;
import com.hoolink.manage.base.vo.req.RoleParamVO;

/**
 * @description: 角色管理
 * @author: WeiMin
 * @date: 2019-05-13
 **/
public interface RoleService {

    /**
     * 创建角色
     * @param roleParamVO
     * @return
     * @throws Exception
     */
    Long create(RoleParamVO roleParamVO) throws Exception;

    /**
     * 修改角色
     * @param roleParamVO
     * @throws Exception
     */
    void update(RoleParamVO roleParamVO) throws Exception;

    /**
     * 获得角色信息
     * @param roleId
     * @return
     * @throws Exception
     */
    RoleParamVO getById(Long roleId) throws Exception;

    /**
     * 分页获得角色列表
     * @param pageParamVO
     * @return
     * @throws Exception
     */
    PageInfo<RoleParamVO> listByPage(PageParamVO pageParamVO) throws Exception;
    
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
}
