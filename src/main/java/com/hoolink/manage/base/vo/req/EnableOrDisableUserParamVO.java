package com.hoolink.manage.base.vo.req;

import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 启用或者禁用用户
 * @author lijunling
 *
 * @date 2019/05/23 12:56
 */
@Data
public class EnableOrDisableUserParamVO {
	/**
	 * 主键
	 */
    @NotNull(message = "id不允许为空")
	private Long id;
    
    /**
     * 用户状态: 启用/禁用
     */
    @NotNull(message = "status不允许为空")
    private Boolean status;
}
