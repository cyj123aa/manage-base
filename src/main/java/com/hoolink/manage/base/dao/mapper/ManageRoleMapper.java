package com.hoolink.manage.base.dao.mapper;

import com.hoolink.manage.base.dao.model.ManageRole;
import com.hoolink.manage.base.dao.model.ManageRoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ManageRoleMapper {
    /**
     *
     * @param example
     */
    int countByExample(ManageRoleExample example);

    /**
     *
     * @param example
     */
    int deleteByExample(ManageRoleExample example);

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
    int insert(ManageRole record);

    /**
     *
     * @param record
     */
    int insertSelective(ManageRole record);

    /**
     *
     * @param example
     */
    List<ManageRole> selectByExample(ManageRoleExample example);

    /**
     * 根据主键获取一条数据库记录
     *
     * @param id
     */
    ManageRole selectByPrimaryKey(Long id);

    /**
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") ManageRole record, @Param("example") ManageRoleExample example);

    /**
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") ManageRole record, @Param("example") ManageRoleExample example);

    /**
     *
     * @param record
     */
    int updateByPrimaryKeySelective(ManageRole record);

    /**
     * 根据主键来更新数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(ManageRole record);
}