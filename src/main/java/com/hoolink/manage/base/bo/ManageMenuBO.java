package com.hoolink.manage.base.bo;

import lombok.Data;

/**
 * 菜单
 * @author lijunling
 *
 * @date 2019/05/18 16:12
 */
@Data
public class ManageMenuBO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String menuName;

    /**
     * 链接地址
     */
    private String url;

    /**
     * 菜单码
     */
    private String menuCode;

    /**
     * 同级菜单排列顺序(数值越小，优先级越高)
     */
    private Integer priority;

    /**
     * 父级ID
     */
    private Long parentId;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 修改人
     */
    private Long updator;

    /**
     * 创建时间
     */
    private Long created;

    /**
     * 修改时间
     */
    private Long updated;

    /**
     * 数据有效性
     */
    private Boolean enabled;

    /**
     * 权限级别 1-只读 2-全部(读、写)
     */
    private Integer permissionFlag;
}
