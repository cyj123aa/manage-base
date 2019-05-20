package com.hoolink.manage.base.dao.mapper;

import com.hoolink.manage.base.dao.model.User;
import com.hoolink.manage.base.dao.model.UserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    /**
     *
     * @param example
     */
    int countByExample(UserExample example);

    /**
     *
     * @param example
     */
    int deleteByExample(UserExample example);

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
    int insert(User record);

    /**
     *
     * @param record
     */
    int insertSelective(User record);

    /**
     *
     * @param example
     */
    List<User> selectByExample(UserExample example);

    /**
     * 根据主键获取一条数据库记录
     *
     * @param id
     */
    User selectByPrimaryKey(Long id);

    /**
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    /**
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    /**
     *
     * @param record
     */
    int updateByPrimaryKeySelective(User record);

    /**
     * 根据主键来更新数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(User record);
}