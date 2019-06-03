package com.hoolink.manage.base.vo.res;

import java.util.List;

import lombok.Data;

/**
 * 
 * @author lijunling
 *
 * @date 2019/05/28 09:02
 */
@Data
public class DeptPairVO {
    /**
     * 互灵/研发体系中心/软件部/测试组/性能测试组（对应各自id集合）
     */
    private List<Long> deptIdList;
    
    /**
     * 互灵/研发体系中心/软件部/测试组/性能测试组（对应各自名称集合）
     */
    private List<String> deptNameList;
    
    /**
     * 互灵/研发体系中心/软件部/测试组/性能测试组（对应各自名称集合
     */
    private String deptNameStr;
    
    /**
     * 部门密保等级(1-一级,2-二级,3-三级,4-四级)
     */
    private Integer encryLevelDept;
    
    /**
     * 部门密保等级(1-一级,2-二级,3-三级,4-四级)
     */
    private String encryLevelDeptName;
}
