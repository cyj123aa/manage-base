package com.hoolink.manage.base.bo;

import lombok.Data;

/**
 * @description: 菜单
 * @author: WeiMin
 * @date: 2019-05-28
 **/
@Data
public class MenuParamBO {

    /**
     * 菜单码
     */
    private String code;

    /**
     * 查询村级
     */
    private Integer level;
}
