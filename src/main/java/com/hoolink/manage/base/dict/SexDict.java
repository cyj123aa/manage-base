package com.hoolink.manage.base.dict;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hoolink.manage.base.bo.DictPairBO;
import com.hoolink.sdk.enums.ManagerUserSexEnum;
/**
 * 性别字典值
 * @author lijunling
 *
 * @date 2019/05/18 16:13
 */
@Component
public class SexDict extends AbstractDict<Boolean, String>{

	@Override
	public List<DictPairBO<Boolean, String>> getDictPairInfo(Object param) {
		List<DictPairBO<Boolean, String>> dictPairList = new ArrayList<>();
		for(ManagerUserSexEnum sexEnum : ManagerUserSexEnum.values()) {
			DictPairBO<Boolean, String> dictPair = new DictPairBO<>();
			dictPair.setKey(sexEnum.getKey());
			dictPair.setValue(sexEnum.getValue());
			dictPairList.add(dictPair);
		}
		return dictPairList;
	}

}
