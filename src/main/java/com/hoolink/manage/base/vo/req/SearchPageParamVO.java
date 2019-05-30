package com.hoolink.manage.base.vo.req;

import lombok.Data;

/**
 * @description: 检索分页参数
 * @author: WeiMin
 * @date: 2019-05-21
 **/
@Data
public class SearchPageParamVO extends PageParamVO {
    private String searchValue;

    private Boolean status;
}
