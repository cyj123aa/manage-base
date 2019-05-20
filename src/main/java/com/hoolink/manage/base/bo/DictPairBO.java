package com.hoolink.manage.base.bo;

import lombok.Data;

/**
 * @author lijunling
 * @description 字典key/value
 * @date 2019/05/15 18:58
 */
@Data
public class DictPairBO<K, V> {
	private K key;
	private V value;
}
