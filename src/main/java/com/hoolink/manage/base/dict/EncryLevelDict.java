package com.hoolink.manage.base.dict;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hoolink.sdk.enums.EncryLevelEnum;
/**
 * 加密等级字典值
 * @author lijunling
 *
 * @date 2019/05/18 16:13
 */
@Component
public class EncryLevelDict extends AbstractDict<Integer, String>{

	@Override
	public List<DictPairBO<Integer, String>> getDictPairInfo(Object param) {
		List<DictPairBO<Integer, String>> dictPairList = new ArrayList<>();
		for(EncryLevelEnum encryLevelEnum : EncryLevelEnum.values()) {
			DictPairBO<Integer, String> dictPair = new DictPairBO<>();
			dictPair.setKey(encryLevelEnum.getKey());
			dictPair.setValue(encryLevelEnum.getValue());
			dictPairList.add(dictPair);
		}
		return dictPairList;
	}

}
