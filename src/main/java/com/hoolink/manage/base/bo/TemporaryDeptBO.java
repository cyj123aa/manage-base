package com.hoolink.manage.base.bo;

import lombok.Data;

/**
 * @description: 用户临时权限
 * @author: WeiMin
 * @date: 2019-05-29
 **/
@Data
public class TemporaryDeptBO {
    private DeptPositionBO company;
    private DeptPositionBO dept;
    private DeptPositionBO position;
}
