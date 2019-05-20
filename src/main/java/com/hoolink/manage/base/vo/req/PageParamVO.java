package com.hoolink.manage.base.vo.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author : lys
 * @Date : 2019/4/19 13:29
 * @Instructions :
 */
@Data
public class PageParamVO implements Serializable {
    private Integer pageNo;
    private Integer pageSize;
}
