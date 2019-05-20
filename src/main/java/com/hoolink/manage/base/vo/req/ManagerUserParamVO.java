package com.hoolink.manage.base.vo.req;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.hoolink.manage.base.vo.ManagerBaseGroup;
import com.hoolink.manage.base.vo.res.ManagerUserInfoVO.UserDeptVO;

import lombok.Data;
/**
 * 
 * @author lijunling
 *
 * @date 2019/05/18 16:14
 */
@Data
public class ManagerUserParamVO {
	/**
	 * 主键
	 */
    @NotNull(
            message = "id不允许为空",
            groups = {ManagerBaseGroup.UpdateUser.class}
    )
	private Long id;
    
    /**
     * 用户账号
     */
    @NotBlank(
            message = "账号不允许为空",
            groups = {ManagerBaseGroup.CreateUser.class}
    )
    private String userAccount;

    /**
     * 姓名
     */
    @NotBlank(
            message = "姓名不允许为空",
            groups = {ManagerBaseGroup.CreateUser.class}
    )
    private String name;

    /**
     * 性别: 1/0 男/女
     */
    @NotNull(
            message = "性别不允许为空",
            groups = {ManagerBaseGroup.CreateUser.class}
    )
    private Boolean sex;

    /**
     * 角色ID
     */
    @NotNull(
            message = "角色不允许为空",
            groups = {ManagerBaseGroup.CreateUser.class}
    )
    private Long roleId;

    /**
     * 用户头像ID
     */
    @NotNull(
            message = "头像不允许为空",
            groups = {ManagerBaseGroup.CreateUser.class}
    )
    private Long imgId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 所属公司
     */
    @NotBlank(
            message = "公司不允许为空",
            groups = {ManagerBaseGroup.CreateUser.class}
    )
    private String company;

    /**
     * 职位
     */
    @NotBlank(
            message = "职位不允许为空",
            groups = {ManagerBaseGroup.CreateUser.class}
    )
    private String position;

    /**
     * 资源库密保等级（1-一级,2-二级,3-三级,4-四级）
     */
    @NotNull(
            message = "资源库密保等级不允许为空",
            groups = {ManagerBaseGroup.CreateUser.class}
    )
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
    @NotEmpty(
            message = "部门不允许为空",
            groups = {ManagerBaseGroup.CreateUser.class}
    )
    private List<UserDeptVO> userDeptPairList;
}
