package com.hoolink.manage.base.vo.req;


import java.util.List;

import lombok.Data;
/**
 * 
 * @author lijunling
 *
 * @date 2019/05/27 19:05
 */
@Data
public class UserDeptPairParamVO {
    /**
     * 互灵/研发体系中心/软件部/测试组/性能测试组（对应各自id集合）
     */
    private List<Long> deptIdList;

    /**
     * 部门密保等级(1-一级,2-二级,3-三级,4-四级)
     */
    private Integer encryLevelDept;
}
