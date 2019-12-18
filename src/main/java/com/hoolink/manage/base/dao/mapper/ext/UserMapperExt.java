package com.hoolink.manage.base.dao.mapper.ext;



import org.apache.ibatis.annotations.Param;


/**
 * @Author: xuli
 * @Date: 2019/5/30 11:51
 * @Author: tonghao
 * @Date: 2019/5/30 9:47
 */
public interface UserMapperExt {


    /**
     * 更新设备的code为空
     * @param deviceCode
     * @return
     */
    int updateDeviceCodeEmpty(@Param("deviceCode") String deviceCode);

}
