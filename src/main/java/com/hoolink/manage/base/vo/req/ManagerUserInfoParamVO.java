package com.hoolink.manage.base.vo.req;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 
 * @author lijunling
 *
 * @date 2019/05/18 16:14
 */
@Data
public class ManagerUserInfoParamVO {
	@NotNull(message = "userId不能为空")
	private Long userId;
}
