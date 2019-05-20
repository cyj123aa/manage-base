package com.hoolink.manage.base.dao.mapper;

import com.hoolink.manage.base.dao.model.MiddleUserDepartment;
import com.hoolink.manage.base.dao.model.MiddleUserDepartmentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MiddleUserDepartmentMapper {
    /**
     *
     * @param example
     */
    int countByExample(MiddleUserDepartmentExample example);

    /**
     *
     * @param example
     */
    int deleteByExample(MiddleUserDepartmentExample example);

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
    int insert(MiddleUserDepartment record);

    /**
     *
     * @param record
     */
    int insertSelective(MiddleUserDepartment record);

    /**
     *
     * @param example
     */
    List<MiddleUserDepartment> selectByExample(MiddleUserDepartmentExample example);

    /**
     * 根据主键获取一条数据库记录
     *
     * @param id
     */
    MiddleUserDepartment selectByPrimaryKey(Long id);

    /**
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") MiddleUserDepartment record, @Param("example") MiddleUserDepartmentExample example);

    /**
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") MiddleUserDepartment record, @Param("example") MiddleUserDepartmentExample example);

    /**
     *
     * @param record
     */
    int updateByPrimaryKeySelective(MiddleUserDepartment record);

    /**
     * 根据主键来更新数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(MiddleUserDepartment record);
}