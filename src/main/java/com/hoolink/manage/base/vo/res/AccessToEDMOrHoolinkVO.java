package com.hoolink.manage.base.vo.res;

import lombok.Data;

/**
 * 
 * @author lijunling
 *
 * @date 2019/05/28 10:29
 */
@Data
public class AccessToEDMOrHoolinkVO {
	/**
	 * EDM系统权限
	 */
	private boolean accessEDM;
	/**
	 * hoolink系统权限
	 */
	private boolean accessHoolink;
}
