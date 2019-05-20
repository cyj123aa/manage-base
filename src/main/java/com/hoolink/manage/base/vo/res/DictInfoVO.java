package com.hoolink.manage.base.vo.res;

import java.util.List;

import lombok.Data;
/**
 * 
 * @author lijunling
 *
 * @date 2019/05/18 16:14
 */
@Data
public class DictInfoVO<K, V> {
	private List<DictPair<K, V>> dictPairList;
	
	@Data
	public static class DictPair<K, V>{
		private K key;
		private V value;
	}
}
