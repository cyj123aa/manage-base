package com.hoolink.manage.base.bo;

import lombok.Data;

/**
 * @author lijunling
 * @description
 * @date 2019/05/15 18:59
 */
@Data
public class ManageDepartmentBO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 所属公司
     */
    private String company;

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
