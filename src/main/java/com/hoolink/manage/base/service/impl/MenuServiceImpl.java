package com.hoolink.manage.base.service.impl;

import com.hoolink.manage.base.bo.DeptPositionBO;
import com.hoolink.manage.base.bo.ManageMenuBO;
import com.hoolink.manage.base.bo.ManagerUserInfoBO;
import com.hoolink.manage.base.bo.TemporaryDeptBO;
import com.hoolink.manage.base.dao.mapper.ManageMenuMapper;
import com.hoolink.manage.base.dao.mapper.MiddleRoleMenuMapper;
import com.hoolink.manage.base.dao.mapper.ext.ManageMenuMapperExt;
import com.hoolink.manage.base.dao.mapper.ext.MiddleRoleMenuMapperExt;
import com.hoolink.manage.base.dao.mapper.ext.MiddleUserDepartmentMapperExt;
import com.hoolink.manage.base.dao.model.ManageMenu;
import com.hoolink.manage.base.dao.model.ManageMenuExample;
import com.hoolink.manage.base.dao.model.MiddleRoleMenu;
import com.hoolink.manage.base.dao.model.MiddleRoleMenuExample;
import com.hoolink.manage.base.service.MenuService;
import com.hoolink.manage.base.service.UserService;
import com.hoolink.sdk.bo.base.CurrentUserBO;
import com.hoolink.sdk.bo.edm.EdmMenuTreeBO;
import com.hoolink.sdk.bo.edm.ResourceParamBO;
import com.hoolink.sdk.bo.manager.EdmMenuBO;
import com.hoolink.sdk.bo.manager.InitMenuBO;
import com.hoolink.sdk.bo.manager.RoleMenuBO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.hoolink.sdk.enums.edm.EdmResourceRepertory.*;

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
    @Resource
    private MiddleRoleMenuMapperExt middleRoleMenuMapperExt;

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
    public EdmMenuTreeBO listByCode(ResourceParamBO paramBO) throws Exception {
        String code = paramBO.getCode();
        if(StringUtils.isEmpty(code) || paramBO.getType()==null){
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
        EdmResourceRepertory byType = getByType(paramBO.getType());
        ManageMenu manageMenu = manageMenuMapperExt.selectByExample(code, byType.getCode());
        if(CollectionUtils.isEmpty(middleRoleMenus) || manageMenu==null){
            return null;
        }
        //EDM系统用户权限菜单
        EdmMenuTreeBO edmMenuTreeBO = null;
        for (MiddleRoleMenu middleRoleMenu : middleRoleMenus) {
            if (manageMenu.getId().equals(middleRoleMenu.getMenuId())) {
                //转为EdmMenuTreeBO
                edmMenuTreeBO = new EdmMenuTreeBO();
                edmMenuTreeBO.setKey(manageMenu.getId());
                edmMenuTreeBO.setValue(manageMenu.getId().toString());
                edmMenuTreeBO.setCode(manageMenu.getMenuCode());
                edmMenuTreeBO.setEnableUpdate(false);
                edmMenuTreeBO.setReadOnly(middleRoleMenu.getPermissionFlag());
                edmMenuTreeBO.setTitle(manageMenu.getMenuName());
                edmMenuTreeBO.setType(paramBO.getType());
                break;
            }
        }
        if(edmMenuTreeBO==null){
            return null;
        }
        //EDM展示 二级菜单 三级菜单
        //部门资源库存在四级菜单 岗级菜单
        //用户权限下所有组织架构列表
        List<DeptPositionBO> deptAllList = middleUserDepartmentMapperExt.getDept(userId);
        if (DEPT_RESOURCE_CODE.getKey().equals(paramBO.getType())) {
            //临时文件所属组织架构 部门库存在
            List<Long> positions = paramBO.getPositionList();
            if (CollectionUtils.isNotEmpty(positions)) {
                //TODO
                List<TemporaryDeptBO> temporaryDept = manageMenuMapperExt.getTemporaryDept(positions);
                if (CollectionUtils.isNotEmpty(temporaryDept)) {
                    for (TemporaryDeptBO temporaryDeptBO : temporaryDept) {
                        if (CollectionUtils.isEmpty(deptAllList)) {
                            deptAllList = new ArrayList<>();
                        }
                        deptAllList.add(temporaryDeptBO.getCompany());
                        deptAllList.add(temporaryDeptBO.getDept());
                        deptAllList.add(temporaryDeptBO.getPosition());
                    }
                }
                deptAllList = ArrayUtil.removeDuplict(deptAllList);
            }
        }
        switch (byType) {
            case DEPT_RESOURCE_CODE:
                //部门资源
                getDeptInitMenu(deptAllList, edmMenuTreeBO);
                break;
            case COMPANY_RESOURCE_CODE:
                //资源库 二级菜单
                List<DeptPositionBO> companyList = null;
                if(CollectionUtils.isNotEmpty(deptAllList)){
                    companyList = deptAllList.stream().filter(deptPositionBO -> EdmDeptEnum.COMPANY.getKey().equals(deptPositionBO.getDeptType().intValue()))
                            .collect(Collectors.toList());
                }
                if(CollectionUtils.isEmpty(companyList)){
                    break;
                }
                List<EdmMenuTreeBO> twoMenuBOS = new ArrayList<>();
                companyList.forEach(company -> {
                    EdmMenuTreeBO twoMenu = getEdmMenuTreeBO(company,true);
                    twoMenuBOS.add(twoMenu);
                });
                edmMenuTreeBO.setChildren(twoMenuBOS);
                break;
            case CACHE_RESOURCE_CODE:
                //缓冲库
                break;
            default:break;
        }
        return edmMenuTreeBO;
    }

    /**
     * 数据封装
     * @param deptPositionBO
     * @return
     */
    private EdmMenuTreeBO getEdmMenuTreeBO(DeptPositionBO deptPositionBO,boolean flag) {
        EdmMenuTreeBO menuTreeBO = new EdmMenuTreeBO();
        menuTreeBO.setKey(deptPositionBO.getId());
        menuTreeBO.setValue(deptPositionBO.getId().toString());
        menuTreeBO.setTitle(deptPositionBO.getDeptName());
        menuTreeBO.setEnableUpdate(flag);
        return menuTreeBO;
    }

    @Override
    public List<RoleMenuBO> listByRoleId(Long roleId) throws Exception {
        return middleRoleMenuMapperExt.listMenuByRoleId(roleId);
    }

    /**
     * 部门资源菜单初始化
     * @param deptAllList
     * @param edmMenuBO
     */
    private void getDeptInitMenu(List<DeptPositionBO> deptAllList, EdmMenuTreeBO edmMenuBO) {
        if(CollectionUtils.isNotEmpty(deptAllList)){
            //key 父级id 封装BO
            Map<Long, List<EdmMenuTreeBO>> map = new HashMap<>(deptAllList.size());
            deptAllList.forEach(deptPositionBO -> {
                Long parentId=0L;
                if(deptPositionBO.getParentId()!=null && deptPositionBO.getParentId()!=0){
                    parentId=deptPositionBO.getParentId();
                }
                EdmMenuTreeBO edmMenuTreeBO;
                if(EdmDeptEnum.POSITION.getKey().equals(deptPositionBO.getDeptType().intValue())){
                    edmMenuTreeBO=getEdmMenuTreeBO(deptPositionBO,false);
                }else{
                    edmMenuTreeBO=getEdmMenuTreeBO(deptPositionBO,true);
                }
                if(map.containsKey(parentId)){
                    map.get(parentId).add(edmMenuTreeBO);
                }else{
                    List<EdmMenuTreeBO> edmMenuTreeBOS = new ArrayList<>();
                    edmMenuTreeBOS.add(edmMenuTreeBO);
                    map.put(parentId,edmMenuTreeBOS);
                }
            });
            //封装子集菜单
            List<EdmMenuTreeBO> firstMenus = map.get(0L);
            if(CollectionUtils.isNotEmpty(firstMenus)){
                fillNextMenu(map,firstMenus);
            }
            edmMenuBO.setChildren(firstMenus);
        }
    }

    /**
     * 封装下级递归目录
     * @param map
     * @param edmMenuTreeBOList
     */
    private void fillNextMenu(Map<Long, List<EdmMenuTreeBO>> map,List<EdmMenuTreeBO> edmMenuTreeBOList){
        for (EdmMenuTreeBO childMenu : edmMenuTreeBOList) {
            Long menuId = childMenu.getKey();
            List<EdmMenuTreeBO> menuBOS = map.get(menuId);
            if (org.springframework.util.CollectionUtils.isEmpty(menuBOS)) {
                continue;
            }
            fillNextMenu(map,menuBOS);
            childMenu.setChildren(menuBOS);
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
