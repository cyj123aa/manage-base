package com.hoolink.manage.base.dao.mapper.ext;

import com.hoolink.sdk.bo.manager.ManageDepartmentTreeBO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: tonghao
 * @Date: 2019/5/30 11:48
 */
public interface ManageDepartmentMapperExt {

    /**
     * 获取组织架构
     * @param ids
     * @return
     */
    List<ManageDepartmentTreeBO> getOrganizationList (@Param("ids")List<Long> ids);

}
