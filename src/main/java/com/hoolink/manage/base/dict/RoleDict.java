package com.hoolink.manage.base.dict;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hoolink.manage.base.bo.DictPairBO;
import com.hoolink.manage.base.bo.ManageRoleBO;
import com.hoolink.manage.base.service.RoleService;
import com.hoolink.sdk.bo.base.CurrentUserBO;
import com.hoolink.sdk.utils.ContextUtil;

/**
 * 角色字典值
 * @author lijunling
 *
 * @date 2019/05/18 16:13
 */
@Component
public class RoleDict extends AbstractDict<Long, String>{

    @Autowired
    private RoleService roleService;
    
	@Override
	public List<DictPairBO<Long, String>> getDictPairInfo(Object param) {
		List<DictPairBO<Long, String>> dictPairList = new ArrayList<>();
		CurrentUserBO user = ContextUtil.getManageCurrentUser();
		List<ManageRoleBO> roleList = roleService.listChildrenRoleByRoleId(user.getRoleId(), (Boolean) param);
		roleList.stream().forEach(r -> {
			DictPairBO<Long, String> dictPair = new DictPairBO<>();
			dictPair.setKey(r.getId());
			dictPair.setValue(r.getRoleName());
			dictPairList.add(dictPair);
		});
		return dictPairList;
	}

}
