package com.hoolink.manage.base.dao.mapper.ext;

import com.hoolink.sdk.bo.manager.SimpleDeptUserBO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: tonghao
 * @Date: 2019/5/30 9:47
 */
public interface UserMapperExt {

    /**
     * 查询部门idList下的所有员工
     * @param idList
     * @return
     */
    List<SimpleDeptUserBO> selectAllByDeptIds(@Param("idList") List<Long> idList);
}
