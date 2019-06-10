package com.hoolink.manage.base.service;

import java.util.List;

import com.hoolink.manage.base.bo.ManageButtonBO;

/**
 * 按钮
 * @author lijunling
 *
 * @date 2019/05/22 11:48
 */
public interface ButtonService {
	/**
	 * 菜单下的按钮
	 * @param menuIdList
	 * @return
	 */
	List<ManageButtonBO> listByMenuIdList(List<Long> menuIdList);
}
