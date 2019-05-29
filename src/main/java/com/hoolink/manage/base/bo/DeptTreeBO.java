package com.hoolink.manage.base.bo;

import java.util.List;

import lombok.Data;

/**
 * 
 * @author lijunling
 *
 * @date 2019/05/22 20:34
 */
@Data
public class DeptTreeBO {
    /**
     * id
     */
    private Long value;
    /**
     * 名称
     */
    private String label;

    /**
     * 子节点
     */
    private List<DeptTreeBO> children;
}
