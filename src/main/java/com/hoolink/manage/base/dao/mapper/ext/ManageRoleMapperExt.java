package com.hoolink.manage.base.dao.mapper.ext;

import com.hoolink.manage.base.dao.model.ManageRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description: 角色
 * @author: WeiMin
 * @date: 2019-05-15
 **/
public interface ManageRoleMapperExt {

    /**
     * 获得用户角色信息
     * @param userId
     * @return
     */
    ManageRole getUserRole(@Param("userId") Long userId);

    /**
     * 一级用户查询角色（包括模糊查询）
     * @param roleId
     * @param searchValue
     * @param status
     * @return
     */
    List<ManageRole> getRoleByOne(@Param("roleId") Long roleId,@Param("searchValue") String searchValue,@Param("status") Boolean status);

    /**
     * 二级用户查询角色（包括模糊查询）
     * @param roleId
     * @param searchValue
     * @param status
     * @return
     */
    List<ManageRole> getRoleByTwo(@Param("roleId") Long roleId,@Param("searchValue") String searchValue,@Param("status") Boolean status);
}