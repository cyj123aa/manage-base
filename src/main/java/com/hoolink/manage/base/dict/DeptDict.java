package com.hoolink.manage.base.dict;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hoolink.manage.base.bo.DictPairBO;
import com.hoolink.manage.base.bo.ManageDepartmentBO;
import com.hoolink.manage.base.service.DepartmentService;

/**
 * 部门字典值
 * @author lijunling
 *
 * @date 2019/05/18 16:13
 */
@Component
public class DeptDict extends AbstractDict<Long, String>{

    @Autowired
    private DepartmentService departmentService;
    
	@Override
	public List<DictPairBO<Long, String>> getDictPairInfo(Object param) {
		String company = (String)param;
		List<DictPairBO<Long, String>> dictPairList = new ArrayList<>();
		List<ManageDepartmentBO> deptList = departmentService.listByCompany(company);
		deptList.stream().forEach(d -> {
			DictPairBO<Long, String> dictPair = new DictPairBO<>();
			dictPair.setKey(d.getId());
			dictPair.setValue(d.getName());
			dictPairList.add(dictPair);
		});
		return dictPairList;
	}

}
