package com.hoolink.manage.base.service.impl;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import com.hoolink.manage.base.dao.mapper.ManageButtonMapper;
import com.hoolink.manage.base.dao.model.ManageButtonExample;
import com.hoolink.manage.base.bo.ManageButtonBO;
import com.hoolink.manage.base.service.ButtonService;
import com.hoolink.sdk.utils.CopyPropertiesUtil;

/**
 * 按钮
 * @author lijunling
 *
 * @date 2019/05/22 13:01
 */
@Service
public class ButtonServiceImpl implements ButtonService{

	@Resource
	ManageButtonMapper manageButtonMapper;
	
	@Override
	public List<ManageButtonBO> listByMenuIdList(List<Long> menuIdList) {
		if(CollectionUtils.isEmpty(menuIdList)) {
			return Collections.emptyList();
		}
		ManageButtonExample example = new ManageButtonExample();
		example.createCriteria().andMenuIdIn(menuIdList).andEnabledEqualTo(true);
		return CopyPropertiesUtil.copyList(manageButtonMapper.selectByExample(example), ManageButtonBO.class);
	}

}
