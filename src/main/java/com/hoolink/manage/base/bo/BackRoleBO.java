package com.hoolink.manage.base.bo;

import com.hoolink.manage.base.vo.res.RoleMenuVO;
import lombok.Data;

import java.io.Serializable;

/**
 * @author XuBaofeng.
 * @date 2019-04-25 17:45.
 * <p>
 * description:
 */
@Data
public class BackRoleBO implements Serializable {
    private static final long serialVersionUID = 4429486023395544906L;
    private Long id;
    /*** 名称 */
    private String name;
    /*** 角色状态: 启用/禁用 */
    private Boolean status;
    /*** 角色描述 */
    private String description;
    /*** 该角色所已选权限 */
    private RoleMenuBO beSelectMenus;
}
