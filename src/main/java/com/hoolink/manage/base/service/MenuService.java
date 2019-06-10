package com.hoolink.manage.base.service;

import com.hoolink.manage.base.bo.ManageMenuBO;
import com.hoolink.sdk.bo.edm.EdmMenuTreeBO;
import com.hoolink.sdk.bo.edm.ResourceParamBO;
import com.hoolink.manage.base.dao.model.ManageMenu;
import com.hoolink.manage.base.dao.model.ManageMenu;
import com.hoolink.sdk.bo.manager.InitMenuBO;
import com.hoolink.sdk.bo.manager.RoleMenuBO;

import java.util.List;

/**
 * @description: 菜单
 * @author: WeiMin
 * @date: 2019-05-15
 **/
public interface MenuService {

    /**
     * 获得菜单集合
     * @param idList
     * @return
     */
    List<ManageMenuBO> listByIdList(List<Long> idList);

    /**
     * 通过code码查询初始化菜单
     * @param paramBO
     * @return
     */
    EdmMenuTreeBO listByCode(ResourceParamBO paramBO) throws Exception;

    /**
     * 根据角色获取菜单
     * @param roleId
     * @return
     * @throws Exception
     */
    List<RoleMenuBO> listByRoleId(Long roleId) throws Exception;

    /**
     * 查询所有的菜单
     * @return
     */
    List<ManageMenuBO> listAll();

    /**
     * 查找menu
     * @param code
     * @return
     */
    ManageMenu getByCode(String code);
}
