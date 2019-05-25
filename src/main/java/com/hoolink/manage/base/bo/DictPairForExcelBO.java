package com.hoolink.manage.base.bo;

import java.util.List;

import lombok.Data;

/**
 * 属性（excel模板）
 * @author lijunling
 *
 * @date 2019/05/25 10:58
 */
@Data
public class DictPairForExcelBO {
	private DictPairBO<Long, String> parentDictPair;
	/** 直接子级**/
	private List<DictPairBO<Long, String>> childrenDictPairList;
}
