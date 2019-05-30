package com.hoolink.manage.base.vo.req;

import lombok.Data;

/**
 * @description: 菜单
 * @author: WeiMin
 * @date: 2019-05-28
 **/
@Data
public class MenuParamVO {

    /**
     * 菜单码
     */
    private String code;

    /**
     * 查询层级
     */
    private Integer level;
}
