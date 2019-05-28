package com.hoolink.manage.base.bo;

import lombok.Data;

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
