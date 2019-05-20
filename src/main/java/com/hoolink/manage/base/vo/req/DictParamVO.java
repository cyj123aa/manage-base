package com.hoolink.manage.base.vo.req;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * 
 * @author lijunling
 *
 * @date 2019/05/18 16:13
 */
@Data
public class DictParamVO {
	@NotBlank(message = "key不能为空")
	private String key; 
	
	/**
	 * 如果为部门字典值，需传入公司码
	 */
	private Integer companyCode;
}
