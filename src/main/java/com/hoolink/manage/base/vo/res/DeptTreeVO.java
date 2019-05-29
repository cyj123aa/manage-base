package com.hoolink.manage.base.vo.res;

import java.util.List;
import lombok.Data;

/**
 * @author lijunling
 *
 * @date 2019/05/22 20:21
 */
@Data
public class DeptTreeVO {
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
    private List<DeptTreeVO> children;
}
