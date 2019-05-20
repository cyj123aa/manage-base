package com.hoolink.manage.base.dao.mapper.ext;

import com.hoolink.manage.base.dao.model.MiddleRoleMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description: 角色管理
 * @author: WeiMin
 * @date: 2019-05-13
 **/
public interface MiddleRoleMenuMapperExt {

    /**
     * 批量創建
     * @param middleRoleMenus
     */
    void bulkInsert(@Param("middleRoleMenus") List<MiddleRoleMenu> middleRoleMenus);

}