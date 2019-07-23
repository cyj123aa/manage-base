package com.hoolink.manage.base.bo;

import lombok.Data;

/**
 * @author lijunling
 *
 * @date 2019/05/22 11:49
 */
@Data
public class ManageButtonBO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 菜单表外键
     */
    private Long menuId;

    /**
     * 按钮编码
     */
    private String buttonCode;

    /**
     * 按钮名称
     */
    private String buttonName;

    /**
     * 按钮url
     */
    private String buttonUrl;

    /**
     * 按钮描述
     */
    private String buttonDesc;

    /**
     * 按钮级别 1-查询类 2-操作类
     */
    private Byte buttonType;

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

}
