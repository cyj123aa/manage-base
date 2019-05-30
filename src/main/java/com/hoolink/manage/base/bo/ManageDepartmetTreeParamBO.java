package com.hoolink.manage.base.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: tonghao
 * @Date: 2019/5/30 11:06
 */
@Data
public class ManageDepartmetTreeParamBO implements Serializable {

    /** 组织架构id集合 */
    private List<Long> idList;

    /** 是否需要查询架构下人员 */
    private Boolean flag;
}
