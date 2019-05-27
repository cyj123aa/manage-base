package com.hoolink.manage.base.bo;

import com.hoolink.manage.base.vo.req.PageParamVO;
import com.hoolink.sdk.bo.rpc.PageParamBO;
import lombok.Data;

/**
 * @description: 检索分页参数
 * @author: WeiMin
 * @date: 2019-05-21
 **/
@Data
public class SearchPageParamBO extends PageParamBO {
    private String searchValue;
}
