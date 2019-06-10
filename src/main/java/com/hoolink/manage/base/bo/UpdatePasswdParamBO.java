package com.hoolink.manage.base.bo;

import lombok.Data;

/**
 * 修改密码
 * @author lijunling
 *
 * @date 2019/05/29 17:51
 */
@Data
public class UpdatePasswdParamBO {
	/**
	 * 新密码
	 */
	private String passwd;
	/**
	 * 验证码
	 */
	private PhoneParamBO phoneParam;
}
