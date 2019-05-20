package com.hoolink.manage.base.bo;

import java.util.List;

import lombok.Data;
/**
 * @author lijunling
 * @description
 * @date 2019/05/15 18:54
 */
@Data
public class DictInfoBO<K, V> {
	private List<DictPairBO<K, V>> dictPairList;
}
