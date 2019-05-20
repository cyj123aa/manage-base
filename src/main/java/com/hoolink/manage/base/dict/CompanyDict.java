package com.hoolink.manage.base.dict;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hoolink.sdk.enums.CompanyEnum;

/**
 * 公司字典值
 * @author lijunling
 *
 * @date 2019/05/18 16:12
 */
@Component
public class CompanyDict extends AbstractDict<Integer, String>{
	@Override
	public List<DictPairBO<Integer, String>> getDictPairInfo(Object param) {
		List<DictPairBO<Integer, String>> dictPairList = new ArrayList<>();
		for(CompanyEnum companyEnum : CompanyEnum.values()) {
			DictPairBO<Integer, String> dictPair = new DictPairBO<>();
			dictPair.setKey(companyEnum.getKey());
			dictPair.setValue(companyEnum.getValue());
			dictPairList.add(dictPair);
		}
		return dictPairList;
	}
}
