package com.hoolink.manage.base.dao.mapper;

import com.hoolink.manage.base.dao.model.ManageButton;
import com.hoolink.manage.base.dao.model.ManageButtonExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ManageButtonMapper {
    /**
     *
     * @param example
     */
    int countByExample(ManageButtonExample example);

    /**
     *
     * @param example
     */
    int deleteByExample(ManageButtonExample example);

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
    int insert(ManageButton record);

    /**
     *
     * @param record
     */
    int insertSelective(ManageButton record);

    /**
     *
     * @param example
     */
    List<ManageButton> selectByExample(ManageButtonExample example);

    /**
     * 根据主键获取一条数据库记录
     *
     * @param id
     */
    ManageButton selectByPrimaryKey(Long id);

    /**
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") ManageButton record, @Param("example") ManageButtonExample example);

    /**
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") ManageButton record, @Param("example") ManageButtonExample example);

    /**
     *
     * @param record
     */
    int updateByPrimaryKeySelective(ManageButton record);

    /**
     * 根据主键来更新数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(ManageButton record);
}