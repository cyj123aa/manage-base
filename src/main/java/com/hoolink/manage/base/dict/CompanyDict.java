package com.hoolink.manage.base.dict;

import java.util.ArrayList;
import java.util.List;
import com.hoolink.manage.base.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hoolink.manage.base.bo.DictPairBO;
import com.hoolink.sdk.bo.manager.ManageDepartmentBO;
import com.hoolink.sdk.enums.DeptTypeEnum;

/**
 * 公司字典值
 * @author lijunling
 *
 * @date 2019/05/18 16:12
 */
@Component
public class CompanyDict extends AbstractDict<Long, String>{
	@Autowired
	private DepartmentService departmentService;
	
	@Override
	public List<DictPairBO<Long, String>> getDictPairInfo(Object param) {
		List<DictPairBO<Long, String>> dictPairList = new ArrayList<>();
		List<ManageDepartmentBO> companyList = departmentService.listByDeptType(DeptTypeEnum.COMPANY.getKey());
		companyList.stream().forEach(c -> {
			DictPairBO<Long, String> dictPair = new DictPairBO<>();
			dictPair.setKey(c.getId());
			dictPair.setValue(c.getName());
			dictPairList.add(dictPair);
		});
		return dictPairList;
	}
}
