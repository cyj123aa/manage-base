package com.hoolink.manage.base.vo.res;

import lombok.Data;

/**
 * 
 * @author lijunling
 *
 * @date 2019/06/17 20:15
 */
@Data
public class OperateFileLogVO {
    /**
     * 操作时间
     */
    private Long created;
    
    /**
     * 操作者
     */
    private String operateName;
    
    /**
     * 操作内容
     */
    private String operateContent;
    
    /**
     * 相关文件
     */
    private String targetResouceName;
    
    /**
     * 操作结果
     */
    private String operateResult;
}
