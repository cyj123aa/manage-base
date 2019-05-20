package com.hoolink.manage.base.service.impl;

import com.hoolink.manage.base.bo.ManageMenuBO;
import com.hoolink.manage.base.dao.mapper.ManageMenuMapper;
import com.hoolink.manage.base.dao.model.ManageMenu;
import com.hoolink.manage.base.dao.model.ManageMenuExample;
import com.hoolink.manage.base.service.MenuService;
import com.hoolink.sdk.exception.BusinessException;
import com.hoolink.sdk.exception.HoolinkExceptionMassageEnum;
import com.hoolink.sdk.utils.CopyPropertiesUtil;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 菜单
 * @author: WeiMin
 * @date: 2019-05-15
 **/
@Service
public class MenuServiceImpl implements MenuService {
    @Resource
    private ManageMenuMapper manageMenuMapper;

    @Override
    public List<ManageMenuBO> listByIdList(List<Long> idList) {
        if(CollectionUtils.isEmpty(idList)){
            throw new BusinessException(HoolinkExceptionMassageEnum.PARAM_ERROR);
        }
        ManageMenuExample example = new ManageMenuExample();
        example.createCriteria().andEnabledEqualTo(true).andIdIn(idList);
        List<ManageMenu> manageMenus = manageMenuMapper.selectByExample(example);
        return CopyPropertiesUtil.copyList(manageMenus, ManageMenuBO.class);
    }
}
