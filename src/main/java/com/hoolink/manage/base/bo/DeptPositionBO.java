package com.hoolink.manage.base.bo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @description: 用户所属信息
 * @author: WeiMin
 * @date: 2019-05-21
 **/
@Data
public class DeptPositionBO {
    private Long id;

    private String deptName;
    /**
     * 部门 岗级
     */
    private List<DeptPositionBO> deptPositionBOList;
}
