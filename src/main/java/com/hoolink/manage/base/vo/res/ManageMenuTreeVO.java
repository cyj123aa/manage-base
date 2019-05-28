package com.hoolink.manage.base.vo.res;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author wangdong
 * @date 2019/4/17 10:44
 */
@Data
public class ManageMenuTreeVO implements Serializable {
    private static final long serialVersionUID = -2649250362604180026L;

    /*** 菜单名称 */
    private String title;

    /*** 菜单名称 */
    private String value;

    /*** 菜单ID */
    private Long key;

    /*** 子节点 */
    private List<ManageMenuTreeVO> children;

    /*** 1：只读\ 2：全部 */
    private Integer readonly;
}
