package com.hoolink.manage.base.bo;

import lombok.Data;

/**
 * @author lijunling
 * @description
 * @date 2019/05/15 18:59
 */
@Data
public class DictParamBO {
	private String key;
	/**
	 * 如果为部门字典值，需传入公司码
	 */
	private Integer companyCode;

	/** true 启用 false 禁用 */
	private Boolean status;
}
