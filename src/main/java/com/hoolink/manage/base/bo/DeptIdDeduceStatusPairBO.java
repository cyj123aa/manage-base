package com.hoolink.manage.base.bo;

import lombok.Data;

/**
 * 
 * @author lijunling
 *
 * @date 2019/06/03 15:06
 */
@Data
public class DeptIdDeduceStatusPairBO {
	
    /**
     * 部门表ID
     */
    private Long deptId;
    
    /**
     * 选择某组织，会推导并保存其所有子组织，状态: 选中的/推导的
     */
    private Boolean deduceStatus;
}
