package com.hoolink.manage.base.dict;

import java.util.List;

import com.hoolink.manage.base.bo.DictInfoBO;

/**
 * 抽象字典值
 * @author lijunling
 *
 * @date 2019/05/18 16:12
 */
public abstract class AbstractDict<K, V> {
	/**
	 * 获取字典组合值
	 * @param param
	 * @return
	 */
	public abstract List<DictPairBO<K, V>> getDictPairInfo(Object param);
	
	public DictInfoBO<K, V> getDictInfo(Object param){
		DictInfoBO<K, V> dictInfo = new DictInfoBO<>();
		dictInfo.setDictPairList(getDictPairInfo(param));
		return dictInfo;
	}
}
