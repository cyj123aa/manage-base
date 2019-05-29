package com.hoolink.manage.base.service.impl;

import com.hoolink.manage.base.bo.DeptPositionBO;
import com.hoolink.manage.base.bo.ManageMenuBO;
import com.hoolink.manage.base.bo.ManagerUserInfoBO;
import com.hoolink.manage.base.bo.TemporaryDeptBO;
import com.hoolink.manage.base.dao.mapper.ManageMenuMapper;
import com.hoolink.manage.base.dao.mapper.MiddleRoleMenuMapper;
import com.hoolink.manage.base.dao.mapper.ext.ManageMenuMapperExt;
import com.hoolink.manage.base.dao.mapper.ext.MiddleUserDepartmentMapperExt;
import com.hoolink.manage.base.dao.model.ManageMenu;
import com.hoolink.manage.base.dao.model.ManageMenuExample;
import com.hoolink.manage.base.dao.model.MiddleRoleMenu;
import com.hoolink.manage.base.dao.model.MiddleRoleMenuExample;
import com.hoolink.manage.base.service.MenuService;
import com.hoolink.manage.base.service.UserService;
import com.hoolink.sdk.bo.base.CurrentUserBO;
import com.hoolink.sdk.bo.edm.ResourceParamBO;
import com.hoolink.sdk.bo.manager.EdmMenuBO;
import com.hoolink.sdk.bo.manager.InitMenuBO;
import com.hoolink.sdk.bo.manager.UserDeptInfoBO;
import com.hoolink.sdk.enums.edm.EdmDeptEnum;
import com.hoolink.sdk.enums.edm.EdmResourceRepertory;
import com.hoolink.sdk.exception.BusinessException;
import com.hoolink.sdk.exception.HoolinkExceptionMassageEnum;
import com.hoolink.sdk.utils.ArrayUtil;
import com.hoolink.sdk.utils.ContextUtil;
import com.hoolink.sdk.utils.CopyPropertiesUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    @Resource
    private ManageMenuMapperExt manageMenuMapperExt;
    @Resource
    private MiddleRoleMenuMapper middleRoleMenuMapper;
    @Resource
    private MiddleUserDepartmentMapperExt middleUserDepartmentMapperExt;
    @Autowired
    private UserService userService;

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
    public InitMenuBO listByCode(ResourceParamBO paramBO) throws Exception {
        String code = paramBO.getCode();
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
        //EDM 一级菜单
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
        //部门资源库存在四级菜单 岗级菜单
        //用户权限下所有组织架构列表
        List<DeptPositionBO> deptAllList = middleUserDepartmentMapperExt.getDept(userId);
        //临时文件所属组织架构
        List<Long> positions = paramBO.getPositionList();
        if(CollectionUtils.isNotEmpty(positions)){
            List<TemporaryDeptBO> temporaryDept = manageMenuMapperExt.getTemporaryDept(positions);
            if(CollectionUtils.isNotEmpty(temporaryDept)){
                for (TemporaryDeptBO temporaryDeptBO:temporaryDept){
                    if(CollectionUtils.isEmpty(deptAllList)){
                        deptAllList=new ArrayList<>();
                    }
                    deptAllList.add(temporaryDeptBO.getCompany());
                    deptAllList.add(temporaryDeptBO.getDept());
                    deptAllList.add(temporaryDeptBO.getPosition());
                }
            }
            deptAllList= ArrayUtil.removeDuplict(deptAllList);
        }
        List<DeptPositionBO> companyList = new ArrayList<>();
        List<DeptPositionBO> deptList = new ArrayList<>();
        List<DeptPositionBO> positionList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(deptAllList)){
            for (DeptPositionBO deptPositionBO : deptAllList){
                if(EdmDeptEnum.COMPANY.getKey().equals(deptPositionBO.getDeptType().intValue())){
                    companyList.add(deptPositionBO);
                }else if(EdmDeptEnum.DEPT.getKey().equals(deptPositionBO.getDeptType().intValue())){
                    deptList.add(deptPositionBO);
                }else if(EdmDeptEnum.POSITION.getKey().equals(deptPositionBO.getDeptType().intValue())){
                    positionList.add(deptPositionBO);
                }
            }
        }
        InitMenuBO initMenuBO = new InitMenuBO();
        for (EdmMenuBO edmMenuBO:menuBOS) {
            if (EdmResourceRepertory.DEPT_RESOURCE_CODE.getCode().equals(edmMenuBO.getMenuCode())){
                //部门资源
                getDeptInitMenu(companyList,deptList,positionList, edmMenuBO);
                initMenuBO.setDeptVO(edmMenuBO);
            }else if (EdmResourceRepertory.CACHE_RESOURCE_CODE.getCode().equals(edmMenuBO.getMenuCode())){
                //缓冲库
                initMenuBO.setCacheVO(edmMenuBO);
            }else if (EdmResourceRepertory.COMPANY_RESOURCE_CODE.getCode().equals(edmMenuBO.getMenuCode())){
                //资源库 二级菜单
                if(CollectionUtils.isEmpty(companyList)){
                    initMenuBO.setCompanyVO(edmMenuBO);
                    continue;
                }
                List<EdmMenuBO> twoMenuBOS = new ArrayList<>();
                companyList.forEach(company -> {
                    EdmMenuBO twoMenu = new EdmMenuBO();
                    twoMenu.setId(company.getId());
                    twoMenu.setMenuName(company.getDeptName());
                    twoMenuBOS.add(twoMenu);
                });
                edmMenuBO.setEdmMenuVOList(twoMenuBOS);
                initMenuBO.setCompanyVO(edmMenuBO);
            }else if (EdmResourceRepertory.PUBLIC_RESOURCE_CODE.getCode().equals(edmMenuBO.getMenuCode())){
                //公共库 暂不做
                initMenuBO.setPublicVO(edmMenuBO);
            }
        }
       /* //查询用户密保等级 岗级
        UserDeptInfoBO userSecurity = userService.getUserSecurity(userId);
        if(userSecurity!=null){
            initMenuBO.setEncryLevelCompany(userSecurity.getEncryLevelCompany());
            initMenuBO.setPositionList(userSecurity.getPositionList());
        }*/
        return initMenuBO;
    }

    /**
     * 部门资源菜单初始化
     * @param companyList
     * @param deptList
     * @param positionList
     * @param edmMenuBO
     */
    private void getDeptInitMenu(List<DeptPositionBO> companyList,List<DeptPositionBO> deptList,List<DeptPositionBO> positionList, EdmMenuBO edmMenuBO) {
        if(CollectionUtils.isNotEmpty(companyList)){
            //下级菜单
            List<EdmMenuBO> twoMenuBOS = new ArrayList<>();
            for (DeptPositionBO company:companyList){
                EdmMenuBO twoMenu = new EdmMenuBO();
                twoMenu.setId(company.getId());
                twoMenu.setMenuName(company.getDeptName());
                if(!org.springframework.util.CollectionUtils.isEmpty(deptList)){
                    List<EdmMenuBO> threeMenuBOS = new ArrayList<>();
                    for (DeptPositionBO deptPositionBO:deptList) {
                        if(company.getId().equals(deptPositionBO.getParentId())){
                            EdmMenuBO threeMenu = new EdmMenuBO();
                            threeMenu.setId(deptPositionBO.getId());
                            threeMenu.setMenuName(deptPositionBO.getDeptName());
                            if(!org.springframework.util.CollectionUtils.isEmpty(positionList)) {
                                List<EdmMenuBO> fourMenuBOS = new ArrayList<>();
                                for (DeptPositionBO positionBO : positionList) {
                                    if(deptPositionBO.getId().equals(positionBO.getParentId())){
                                        EdmMenuBO fourMenu = new EdmMenuBO();
                                        fourMenu.setId(positionBO.getId());
                                        fourMenu.setMenuName(positionBO.getDeptName());
                                        fourMenuBOS.add(fourMenu);
                                    }
                                }
                                threeMenu.setEdmMenuVOList(fourMenuBOS);
                            }
                            threeMenuBOS.add(threeMenu);
                        }
                    }
                    twoMenu.setEdmMenuVOList(threeMenuBOS);
                }
                twoMenuBOS.add(twoMenu);
            }
            edmMenuBO.setEdmMenuVOList(twoMenuBOS);
        }
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
