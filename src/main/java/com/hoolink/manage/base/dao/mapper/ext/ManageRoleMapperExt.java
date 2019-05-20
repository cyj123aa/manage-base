package com.hoolink.manage.base.dao.mapper.ext;

import com.hoolink.manage.base.dao.model.ManageRole;
import org.apache.ibatis.annotations.Param;

public interface ManageRoleMapperExt {

    /**
     * 获得用户角色信息
     * @param userId
     * @return
     */
    ManageRole getUserRole(@Param("userId") Long userId);
}