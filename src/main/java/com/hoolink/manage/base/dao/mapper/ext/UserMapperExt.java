package com.hoolink.manage.base.dao.mapper.ext;


import com.hoolink.sdk.bo.manager.DeptSecurityRepertoryBO;
import com.hoolink.sdk.bo.manager.SimpleDeptUserBO;
import org.apache.ibatis.annotations.Param;


import java.util.List;

/**
 * @Author: xuli
 * @Date: 2019/5/30 11:51
 * @Author: tonghao
 * @Date: 2019/5/30 9:47
 */
public interface UserMapperExt {

    /**
     * 获取用户密保信息
     * @param id
     * @return
     */
    List<DeptSecurityRepertoryBO> getDeptByUser(Long id);

    /**
     * 查询部门idList下的所有员工
     * @param idList
     * @return
     */
    List<SimpleDeptUserBO> selectAllByDeptIds(@Param("idList") List<Long> idList);

    /**
     * 根据用户名模糊搜索
     * @param userName
     * @return
     */
    List<SimpleDeptUserBO> selectUserAndDeptByUserName(@Param("userName")String userName);

}
