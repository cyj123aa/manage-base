package com.hoolink.manage.base.dao.mapper;

import com.hoolink.manage.base.dao.model.ManageMenu;
import com.hoolink.manage.base.dao.model.ManageMenuExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ManageMenuMapper {
    /**
     *
     * @param example
     */
    int countByExample(ManageMenuExample example);

    /**
     *
     * @param example
     */
    int deleteByExample(ManageMenuExample example);

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
    int insert(ManageMenu record);

    /**
     *
     * @param record
     */
    int insertSelective(ManageMenu record);

    /**
     *
     * @param example
     */
    List<ManageMenu> selectByExample(ManageMenuExample example);

    /**
     * 根据主键获取一条数据库记录
     *
     * @param id
     */
    ManageMenu selectByPrimaryKey(Long id);

    /**
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") ManageMenu record, @Param("example") ManageMenuExample example);

    /**
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") ManageMenu record, @Param("example") ManageMenuExample example);

    /**
     *
     * @param record
     */
    int updateByPrimaryKeySelective(ManageMenu record);

    /**
     * 根据主键来更新数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(ManageMenu record);
}