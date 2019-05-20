package com.hoolink.manage.base.dao.mapper;

import com.hoolink.manage.base.dao.model.ManageDepartment;
import com.hoolink.manage.base.dao.model.ManageDepartmentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ManageDepartmentMapper {
    /**
     *
     * @param example
     */
    int countByExample(ManageDepartmentExample example);

    /**
     *
     * @param example
     */
    int deleteByExample(ManageDepartmentExample example);

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
    int insert(ManageDepartment record);

    /**
     *
     * @param record
     */
    int insertSelective(ManageDepartment record);

    /**
     *
     * @param example
     */
    List<ManageDepartment> selectByExample(ManageDepartmentExample example);

    /**
     * 根据主键获取一条数据库记录
     *
     * @param id
     */
    ManageDepartment selectByPrimaryKey(Long id);

    /**
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") ManageDepartment record, @Param("example") ManageDepartmentExample example);

    /**
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") ManageDepartment record, @Param("example") ManageDepartmentExample example);

    /**
     *
     * @param record
     */
    int updateByPrimaryKeySelective(ManageDepartment record);

    /**
     * 根据主键来更新数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(ManageDepartment record);
}