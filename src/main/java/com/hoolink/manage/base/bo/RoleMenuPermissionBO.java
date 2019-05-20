package com.hoolink.manage.base.bo;

import lombok.Data;

/**
 * 角色对应菜单权限
 * @author lijunling
 *
 * @date 2019/05/18 10:05
 */
@Data
public class RoleMenuPermissionBO {
	/**
	 * 角色id
	 */
    private Long roleId;
    
    /**
     * 菜单码
     */
    private String menuCode;
    
    /**
     * 权限级别 1-只读 2-全部(读、写)
     */
    private Integer permissionFlag;
}
