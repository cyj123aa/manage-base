package com.hoolink.manage.base.vo.req;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 
 * @author lijunling
 *
 * @date 2019/05/28 16:51
 */
@Data
public class UpdatePasswdParamVO {
	/**
	 * 新密码
	 */
    @NotBlank(
            message = "新密码不允许为空"
    )
	private String passwd;
	/**
	 * 验证码
	 */
    @NotNull(
            message = "验证码不允许为空"
    )
	private PhoneParamVO phoneParam;
}
