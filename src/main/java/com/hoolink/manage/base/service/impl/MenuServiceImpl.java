package com.hoolink.manage.base.service.impl;

import com.hoolink.manage.base.bo.ManageMenuBO;
import com.hoolink.manage.base.bo.UserDeptBO;
import com.hoolink.manage.base.dao.mapper.ManageMenuMapper;
import com.hoolink.manage.base.dao.mapper.MiddleRoleMenuMapper;
import com.hoolink.manage.base.dao.mapper.ext.ManageMenuMapperExt;
import com.hoolink.manage.base.dao.mapper.ext.MiddleUserDepartmentMapperExt;
import com.hoolink.manage.base.dao.model.ManageMenu;
import com.hoolink.manage.base.dao.model.ManageMenuExample;
import com.hoolink.manage.base.dao.model.MiddleRoleMenu;
import com.hoolink.manage.base.dao.model.MiddleRoleMenuExample;
import com.hoolink.manage.base.service.MenuService;
import com.hoolink.sdk.bo.base.CurrentUserBO;
import com.hoolink.sdk.bo.manager.EdmMenuBO;
import com.hoolink.sdk.bo.manager.InitMenuBO;
import com.hoolink.sdk.constants.Constants;
import com.hoolink.sdk.enums.edm.EdmResourceRepertory;
import com.hoolink.sdk.exception.BusinessException;
import com.hoolink.sdk.exception.HoolinkExceptionMassageEnum;
import com.hoolink.sdk.utils.ContextUtil;
import com.hoolink.sdk.utils.CopyPropertiesUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description: 菜单
 * @author: WeiMin
 * @date: 2019-05-15
 **/
@Service
public class MenuServiceImpl implements MenuService {
    @Resource
    private ManageMenuMapper manageMenuMapper;
    @Resource
    private ManageMenuMapperExt manageMenuMapperExt;
    @Resource
    private MiddleRoleMenuMapper middleRoleMenuMapper;
    @Resource
    private MiddleUserDepartmentMapperExt middleUserDepartmentMapperExt;

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

    @Override
    public InitMenuBO listByCode(String code) {
        if(StringUtils.isEmpty(code)){
            throw new BusinessException(HoolinkExceptionMassageEnum.PARAM_ERROR);
        }
        CurrentUserBO user = ContextUtil.getManageCurrentUser();
        if(user==null){
            throw new BusinessException(HoolinkExceptionMassageEnum.USER_NOT_EXIST_ERROR);
        }
        Long roleId = user.getRoleId();
        Long userId = user.getUserId();
        //角色的edm权限
        List<MiddleRoleMenu> middleRoleMenus = listByRole(roleId);
        //EDM下一级菜单
        List<ManageMenu> menus = manageMenuMapperExt.selectByExample(code);
        if(CollectionUtils.isEmpty(menus)||CollectionUtils.isEmpty(middleRoleMenus)){
            return null;
        }
        //EDM系统用户权限菜单
        List<EdmMenuBO> menuBOS = new ArrayList<>();
        for (ManageMenu menu:menus) {
            for (MiddleRoleMenu middleRoleMenu:middleRoleMenus) {
                if(menu.getId().equals(middleRoleMenu.getMenuId())){
                    EdmMenuBO edmMenuBO = CopyPropertiesUtil.copyBean(menu, EdmMenuBO.class);
                    edmMenuBO.setPermissionFlag(middleRoleMenu.getPermissionFlag());
                    menuBOS.add(edmMenuBO);
                }
            }
        }
        //EDM展示 二级菜单 三级菜单
        //所属公司 部门
        UserDeptBO deptMenu = middleUserDepartmentMapperExt.getDeptMenu(userId);
        InitMenuBO initMenuBO = new InitMenuBO();
        for (EdmMenuBO edmMenuBO:menuBOS) {
            if (EdmResourceRepertory.DEPT_RESOURCE_CODE.getCode().equals(edmMenuBO.getMenuCode())){
                //部门资源
                if(deptMenu!=null){
                    //下级菜单
                    List<EdmMenuBO> twoMenuBOS = new ArrayList<>();
                    EdmMenuBO twoMenu = new EdmMenuBO();
                    twoMenu.setMenuName(deptMenu.getCompanyName());
                    Map<Long, String> deptMap = deptMenu.getDeptMap();
                    if(!org.springframework.util.CollectionUtils.isEmpty(deptMap)){
                        List<EdmMenuBO> threeMenuBOS = new ArrayList<>();
                        for (Map.Entry<Long, String> entry:deptMap.entrySet()) {
                            EdmMenuBO threeMenu = new EdmMenuBO();
                            threeMenu.setId(entry.getKey());
                            threeMenu.setMenuName(entry.getValue());
                        }
                        twoMenu.setEdmMenuVOList(threeMenuBOS);
                    }
                    twoMenuBOS.add(twoMenu);
                    edmMenuBO.setEdmMenuVOList(twoMenuBOS);
                }
                initMenuBO.setDeptVO(edmMenuBO);
            }else if (EdmResourceRepertory.CACHE_RESOURCE_CODE.getCode().equals(edmMenuBO.getMenuCode())){
                //缓冲库
                initMenuBO.setCacheVO(edmMenuBO);
            }else if (EdmResourceRepertory.COMPANY_RESOURCE_CODE.getCode().equals(edmMenuBO.getMenuCode())){
                //资源库 二级菜单
                List<EdmMenuBO> twoMenuBOS = new ArrayList<>();
                EdmMenuBO twoMenu = new EdmMenuBO();
                twoMenu.setMenuName(deptMenu.getCompanyName());
                twoMenuBOS.add(twoMenu);
                edmMenuBO.setEdmMenuVOList(twoMenuBOS);
                initMenuBO.setCompanyVO(edmMenuBO);
            }else if (EdmResourceRepertory.PUBLIC_RESOURCE_CODE.getCode().equals(edmMenuBO.getMenuCode())){
                //公共库 暂不做
                initMenuBO.setPublicVO(edmMenuBO);
            }
        }
        return initMenuBO;
    }

    /**
     * 用户权限下菜单
     * @param roleId
     * @return
     */
    private List<MiddleRoleMenu> listByRole(Long roleId){
        MiddleRoleMenuExample example = new MiddleRoleMenuExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        List<MiddleRoleMenu> middleRoleMenus = middleRoleMenuMapper.selectByExample(example);
        return middleRoleMenus;
    }
}
