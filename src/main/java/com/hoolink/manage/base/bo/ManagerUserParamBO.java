package com.hoolink.manage.base.bo;
import java.util.List;

import com.hoolink.manage.base.vo.res.ManagerUserInfoVO.UserDeptVO;

import lombok.Data;

/**
 * @author lijunling
 * @description
 * @date 2019/05/15 19:00
 */
@Data
public class ManagerUserParamBO {
	/**
	 * 主键
	 */
	private Long id;
	
    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别: 1/0 男/女
     */
    private Boolean sex;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 用户头像ID
     */
    private Long imgId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 所属公司
     */
    private String company;

    /**
     * 职位
     */
    private String position;

    /**
     * 资源库密保等级（1-一级,2-二级,3-三级,4-四级）
     */
    private Integer encryLevelCompany;

    /**
     * 用户状态: 启用/禁用
     */
    private Boolean status;
    
    /**
     * 数据有效性
     */
    private Boolean enabled;
    
    /**
     * 用户部门关系
     */
    private List<UserDeptVO> userDeptPairList;
}
