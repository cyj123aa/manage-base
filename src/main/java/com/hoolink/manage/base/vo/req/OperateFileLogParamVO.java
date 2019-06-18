package com.hoolink.manage.base.vo.req;

import com.hoolink.sdk.param.PageParam;

import lombok.Data;

/**
 * 
 * @author lijunling
 *
 * @date 2019/06/17 20:18
 */
@Data
public class OperateFileLogParamVO extends PageParam{
    /**
     * 模糊查询
     */
    private String searchValue;
    
    /**
     * 操作起始时间
     */
    private Long operateStart;
    
    /**
     * 操作截止时间
     */
    private Long operateEnd;
    
    /**
     * 操作人
     */
    private Long operatorId;
}
