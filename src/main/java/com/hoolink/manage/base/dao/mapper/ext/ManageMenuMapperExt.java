package com.hoolink.manage.base.dao.mapper.ext;

import com.hoolink.manage.base.dao.model.ManageMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @description: 菜单
 * @author: WeiMin
 * @date: 2019-05-15
 **/
public interface ManageMenuMapperExt {

    /**
     * 通过code码查询下一级菜单
     * @param code
     */
    List<ManageMenu> selectByExample(@Param("code") String code);

    /**
     *查询用户当前权限菜单
     * @param roleId
     * @return
     */
    Map<Long,Integer> getRoleMenu(@Param("roleId") Long roleId);

}