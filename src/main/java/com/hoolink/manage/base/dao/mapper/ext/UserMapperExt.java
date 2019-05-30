package com.hoolink.manage.base.dao.mapper.ext;

import com.hoolink.sdk.bo.manager.DeptSecurityRepertoryBO;

import java.util.List;

/**
 * @Author: xuli
 * @Date: 2019/5/30 11:51
 */
public interface UserMapperExt {

    /**
     * 获取用户密保信息
     * @param id
     * @return
     */
    List<DeptSecurityRepertoryBO> getDeptByUser(Long id);
}
