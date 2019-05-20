package com.hoolink.manage.base.dao.mapper;

import com.hoolink.manage.base.dao.model.MiddleRoleMenu;
import com.hoolink.manage.base.dao.model.MiddleRoleMenuExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MiddleRoleMenuMapper {
    /**
     *
     * @param example
     */
    int countByExample(MiddleRoleMenuExample example);

    /**
     *
     * @param example
     */
    int deleteByExample(MiddleRoleMenuExample example);

    /**
     * 根据主键删除数据库的记录
     *
     * @param id
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入数据库记录
     *
     * @param record
     */
    int insert(MiddleRoleMenu record);

    /**
     *
     * @param record
     */
    int insertSelective(MiddleRoleMenu record);

    /**
     *
     * @param example
     */
    List<MiddleRoleMenu> selectByExample(MiddleRoleMenuExample example);

    /**
     * 根据主键获取一条数据库记录
     *
     * @param id
     */
    MiddleRoleMenu selectByPrimaryKey(Long id);

    /**
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") MiddleRoleMenu record, @Param("example") MiddleRoleMenuExample example);

    /**
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") MiddleRoleMenu record, @Param("example") MiddleRoleMenuExample example);

    /**
     *
     * @param record
     */
    int updateByPrimaryKeySelective(MiddleRoleMenu record);

    /**
     * 根据主键来更新数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(MiddleRoleMenu record);
}