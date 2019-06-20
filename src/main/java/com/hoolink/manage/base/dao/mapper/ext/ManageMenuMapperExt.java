package com.hoolink.manage.base.dao.mapper.ext;

import com.hoolink.manage.base.bo.MiddleRoleMenuBO;
import com.hoolink.manage.base.bo.DeptPositionBO;
import com.hoolink.manage.base.bo.TemporaryDeptBO;
import com.hoolink.manage.base.dao.model.ManageMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description: 菜单
 * @author: WeiMin
 * @date: 2019-05-15
 **/
public interface ManageMenuMapperExt {

    /**
     * 通过code码查询下一级菜单
     * @param code
     * @param currentCode
     * @return
     */
    ManageMenu selectByExample(@Param("code") String code,@Param("currentCode")String currentCode);

    /**
     * 用户临时权限组织架构信息
     * @param positions
     * @return
     */
    List<TemporaryDeptBO> getTemporaryDept(@Param("positions")List<Long> positions);

    /**
     *查询用户当前权限菜单
     * @param roleId
     * @return
     */
    List<MiddleRoleMenuBO> getRoleMenu(@Param("roleId") Long roleId);

}