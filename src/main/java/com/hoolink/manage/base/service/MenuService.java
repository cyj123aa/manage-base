package com.hoolink.manage.base.service;

import com.hoolink.manage.base.bo.ManageMenuBO;
import com.hoolink.sdk.bo.manager.InitMenuBO;

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
     * @param code
     * @return
     */
    InitMenuBO listByCode(String code) throws Exception;
}
