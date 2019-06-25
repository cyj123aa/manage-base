package com.hoolink.manage.base.service.impl;

import com.hoolink.manage.base.bo.*;
import com.hoolink.manage.base.constant.Constant;
import com.hoolink.manage.base.dao.mapper.ManageDepartmentMapper;
import com.hoolink.manage.base.dao.mapper.ManageMenuMapper;
import com.hoolink.manage.base.dao.mapper.MiddleRoleMenuMapper;
import com.hoolink.manage.base.dao.mapper.ext.ManageDepartmentMapperExt;
import com.hoolink.manage.base.dao.mapper.ext.ManageMenuMapperExt;
import com.hoolink.manage.base.dao.mapper.ext.MiddleRoleMenuMapperExt;
import com.hoolink.manage.base.dao.mapper.ext.MiddleUserDepartmentMapperExt;
import com.hoolink.manage.base.dao.model.*;
import com.hoolink.manage.base.service.DepartmentService;
import com.hoolink.manage.base.service.MenuService;
import com.hoolink.manage.base.service.UserService;
import com.hoolink.manage.base.util.DeptTreeToolUtils;
import com.hoolink.sdk.bo.base.CurrentUserBO;
import com.hoolink.sdk.bo.edm.EdmMenuTreeBO;
import com.hoolink.sdk.bo.edm.MenuParamBO;
import com.hoolink.sdk.bo.edm.ResourceParamBO;
import com.hoolink.sdk.bo.manager.*;
import com.hoolink.sdk.bo.manager.EdmMenuBO;
import com.hoolink.sdk.bo.manager.InitMenuBO;
import com.hoolink.sdk.bo.manager.ManageDepartmentBO;
import com.hoolink.sdk.bo.manager.RoleMenuBO;
import com.hoolink.sdk.constants.Constants;
import com.hoolink.sdk.enums.edm.EdmDeptEnum;
import com.hoolink.sdk.enums.edm.EdmResourceRepertory;
import com.hoolink.sdk.exception.BusinessException;
import com.hoolink.sdk.exception.HoolinkExceptionMassageEnum;
import com.hoolink.sdk.utils.ArrayUtil;
import com.hoolink.sdk.utils.ContextUtil;
import com.hoolink.sdk.utils.CopyPropertiesUtil;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
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
    @Autowired
    private DepartmentService departmentService;
    @Resource
    private ManageDepartmentMapper manageDepartmentMapper;
    @Resource
    private ManageDepartmentMapperExt manageDepartmentMapperExt;

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
        if(StringUtils.isEmpty(code) || paramBO.getRepertoryType()==null){
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
        EdmResourceRepertory byType = getByType(paramBO.getRepertoryType());
        ManageMenu manageMenu = manageMenuMapperExt.selectByExample(code, byType.getCode());
        if(CollectionUtils.isEmpty(middleRoleMenus) || manageMenu==null){
            return null;
        }
        //EDM系统用户权限菜单
        EdmMenuTreeBO edmMenuTreeBO = null;
        for (MiddleRoleMenu middleRoleMenu : middleRoleMenus) {
            if (manageMenu.getId().equals(middleRoleMenu.getMenuId())) {
                //转为EdmMenuTreeBO
                edmMenuTreeBO = getEdmMenuTreeBO(paramBO.getRepertoryType(), manageMenu, middleRoleMenu);
                break;
            }
        }
        if(edmMenuTreeBO==null){
            return null;
        }
        //EDM展示 默认3级 部门资源库初始化所有组织架构
        //用户权限下所有组织架构列表
        List<DeptPositionBO> deptAllList = middleUserDepartmentMapperExt.getDept(userId);
        if (DEPT_RESOURCE_CODE.getKey().equals(paramBO.getRepertoryType())) {
            //用户处在 公司级别 体系中心级别 部门级别
            deptAllList = getAllDeptOnUser(paramBO, deptAllList);
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
                    edmMenuTreeBO.setExistChild(false);
                    break;
                }
                List<EdmMenuTreeBO> twoMenuBOS = new ArrayList<>();
                companyList.forEach(company -> {
                    EdmMenuTreeBO twoMenu = getEdmMenuTreeBO(company,true,byType.getKey());
                    twoMenuBOS.add(twoMenu);
                });
                edmMenuTreeBO.setChildren(twoMenuBOS);
                edmMenuTreeBO.setExistChild(true);
                break;
            case CACHE_RESOURCE_CODE:
                //缓冲库
                edmMenuTreeBO.setEnableUpdate(true);
                break;
            default:break;
        }
        return edmMenuTreeBO;
    }

    /**
     * 获得用户权限下所有组织架构
     * @param paramBO
     * @param deptAllList
     * @return
     */
    private List<DeptPositionBO> getAllDeptOnUser(ResourceParamBO paramBO, List<DeptPositionBO> deptAllList) {
        List<Long> highLevel = new ArrayList<>();
        for (DeptPositionBO deptPositionBO : deptAllList) {
            if (!EdmDeptEnum.POSITION.getKey().equals(deptPositionBO.getDeptType().intValue()) && deptPositionBO.getLowestLevel()) {
                highLevel.add(deptPositionBO.getId());
            }
        }
        if (CollectionUtils.isNotEmpty(highLevel)) {
            List<DeptPositionBO> deptPositionBOS = manageDepartmentMapperExt.listByParentIdCode(highLevel);
            if (CollectionUtils.isNotEmpty(deptPositionBOS)) {
                deptAllList.addAll(deptPositionBOS);
            }
        }
        deptAllList = removeDuplict(deptAllList);
        return deptAllList;
    }

    /**
     * 去重
     * @param list
     * @return
     */
    private List<DeptPositionBO> removeDuplict(List<DeptPositionBO> list) {
        Set<DeptPositionBO> set = new TreeSet<>((o1, o2) -> o1.getId().compareTo(o2.getId()));
        set.addAll(list);
        return new ArrayList<>(set);
    }

    /**
     * 数据封装 一级目录
     * @param repertoryType
     * @param manageMenu
     * @param middleRoleMenu
     * @return
     */
    private EdmMenuTreeBO getEdmMenuTreeBO(Integer repertoryType, ManageMenu manageMenu, MiddleRoleMenu middleRoleMenu) {
        EdmMenuTreeBO edmMenuTreeBO;
        edmMenuTreeBO = new EdmMenuTreeBO();
        edmMenuTreeBO.setKey(Constant.MENU_PREFIX+manageMenu.getId());
        edmMenuTreeBO.setValue(manageMenu.getId().toString());
        edmMenuTreeBO.setCode(manageMenu.getMenuCode());
        edmMenuTreeBO.setEnableUpdate(false);
        edmMenuTreeBO.setMenuType(null);
        edmMenuTreeBO.setExpand(true);
        if(middleRoleMenu!=null){
            edmMenuTreeBO.setReadOnly(middleRoleMenu.getPermissionFlag());
        }
        edmMenuTreeBO.setTitle(manageMenu.getMenuName());
        edmMenuTreeBO.setRepertoryType(repertoryType);
        return edmMenuTreeBO;
    }

    /**
     * 数据封装
     * @param deptPositionBO
     * @return
     */
    private EdmMenuTreeBO getEdmMenuTreeBO(DeptPositionBO deptPositionBO,boolean flag,Integer repertoryType) {
        EdmMenuTreeBO menuTreeBO = new EdmMenuTreeBO();
        menuTreeBO.setKey(Constant.DEPT_PREFIX+deptPositionBO.getId());
        menuTreeBO.setValue(deptPositionBO.getId().toString());
        menuTreeBO.setTitle(deptPositionBO.getDeptName());
        menuTreeBO.setEnableUpdate(flag);
        menuTreeBO.setMenuType(true);
        menuTreeBO.setExpand(true);
        menuTreeBO.setRepertoryType(repertoryType);
        return menuTreeBO;
    }

    /**
     * 数据封装
     * @param deptPositionBO
     * @return
     */
    private EdmMenuTreeBO getEdmMenuTreeBO(ManageDepartmentBO deptPositionBO,Integer repertoryType,Boolean flag) {
        EdmMenuTreeBO menuTreeBO = new EdmMenuTreeBO();
        menuTreeBO.setKey(Constant.DEPT_PREFIX+deptPositionBO.getId());
        menuTreeBO.setValue(deptPositionBO.getId().toString());
        menuTreeBO.setTitle(deptPositionBO.getName());
        menuTreeBO.setMenuType(true);
        menuTreeBO.setEnableUpdate(flag);
        menuTreeBO.setRepertoryType(repertoryType);
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
                    edmMenuTreeBO=getEdmMenuTreeBO(deptPositionBO,true,DEPT_RESOURCE_CODE.getKey());
                    edmMenuTreeBO.setExpand(false);
                }else{
                    edmMenuTreeBO=getEdmMenuTreeBO(deptPositionBO,false,DEPT_RESOURCE_CODE.getKey());
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
                edmMenuBO.setChildren(firstMenus);
                edmMenuBO.setExistChild(true);
            }else{
                edmMenuBO.setExistChild(false);
            }
        }
    }

    /**
     * 封装下级递归目录
     * @param map
     * @param edmMenuTreeBOList
     */
    private void fillNextMenu(Map<Long, List<EdmMenuTreeBO>> map,List<EdmMenuTreeBO> edmMenuTreeBOList){
        for (EdmMenuTreeBO childMenu : edmMenuTreeBOList) {
            String menuId = childMenu.getValue();
            List<EdmMenuTreeBO> menuBOS = map.get(Long.valueOf(menuId));
            if (org.springframework.util.CollectionUtils.isEmpty(menuBOS)) {
                childMenu.setExistChild(false);
                childMenu.setEnableUpdate(true);
                continue;
            }
            fillNextMenu(map,menuBOS);
            childMenu.setChildren(menuBOS);
            childMenu.setExistChild(true);
            childMenu.setEnableUpdate(false);
        }
    }

    @Override
    public List<EdmMenuTreeBO> getOrganizationHead(MenuParamBO paramBO) throws Exception {
        if(paramBO.getRepertoryType()==null){
            throw new BusinessException(HoolinkExceptionMassageEnum.PARAM_ERROR);
        }
        EdmResourceRepertory byType = EdmResourceRepertory.getByType(paramBO.getRepertoryType());
        List<EdmMenuTreeBO> edmMenuTreeBOS=new ArrayList<>();
        //查询一级
        ManageMenu manageMenu = manageMenuMapperExt.selectByExample(Constants.EDM_CODE, byType.getCode());
        if(manageMenu==null){
            return null;
        }
        EdmMenuTreeBO edmMenuTreeBO = getEdmMenuTreeBO(paramBO.getRepertoryType(), manageMenu, null);
        edmMenuTreeBOS.add(edmMenuTreeBO);
        if (StringUtils.isEmpty(paramBO.getBelongId())) {
            //只有一级目录
            return edmMenuTreeBOS;
        }
        Long strId = Long.valueOf(paramBO.getBelongId());
        if(strId==0){
            //只有一级目录
            return edmMenuTreeBOS;
        }
        ManageDepartment manageDepartment = manageDepartmentMapper.selectByPrimaryKey(strId);
        //查询下级
        ManageDepartmentExample example = new ManageDepartmentExample();
        example.createCriteria().andEnabledEqualTo(true).andParentIdEqualTo(strId);
        List<ManageDepartment> manageDepartments = manageDepartmentMapper.selectByExample(example);
        Boolean flag=true;
        if(CollectionUtils.isNotEmpty(manageDepartments)){
            flag=false;
        }
        final Boolean enableUpdate=flag;
        if(manageDepartment!=null){
            String parentIdCode = manageDepartment.getParentIdCode();
            String[] split = parentIdCode.split(Constant.UNDERLINE);
            if(split.length==Constant.LEVEL){
                //一级组织架构
                edmMenuTreeBOS.add(getEdmMenuTreeBO(CopyPropertiesUtil.copyBean(manageDepartment,ManageDepartmentBO.class),paramBO.getRepertoryType(),enableUpdate));
                return edmMenuTreeBOS;
            }
            String[] split1 = new String[split.length-1];
            System.arraycopy(split, 1, split1, 0, split1.length);
            List<String> ids = Arrays.asList(split1);
            List<Long> collect = ids.stream().map(id -> Long.parseLong(id)).collect(Collectors.toList());
            List<ManageDepartmentBO> manageDepartmentBOS = manageDepartmentMapperExt.listByIdOrder(collect);
            if(CollectionUtils.isNotEmpty(manageDepartmentBOS)){
                manageDepartmentBOS.forEach(manageDepartmentBO -> {
                    if(manageDepartmentBO.getId().equals(paramBO.getBelongId())){
                        edmMenuTreeBOS.add(getEdmMenuTreeBO(manageDepartmentBO,paramBO.getRepertoryType(),enableUpdate));
                    }else{
                        edmMenuTreeBOS.add(getEdmMenuTreeBO(manageDepartmentBO,paramBO.getRepertoryType(),false));
                    }
                });
            }
        }
        return edmMenuTreeBOS;
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

    @Override
    public List<ManageMenuBO> listAll() {
        ManageMenuExample example = new ManageMenuExample();
        example.createCriteria().andEnabledEqualTo(true);
        List<ManageMenu> menus = manageMenuMapper.selectByExample(example);
        return CopyPropertiesUtil.copyList(menus, ManageMenuBO.class);
    }

    @Override
    public ManageMenu getByCode(String code) {
        ManageMenuExample example = new ManageMenuExample();
        example.createCriteria().andEnabledEqualTo(true).andMenuCodeEqualTo(code);
        List<ManageMenu> menus = manageMenuMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(menus)){
            throw new BusinessException(HoolinkExceptionMassageEnum.PARAM_ERROR);
        }
        //当前menu
        ManageMenu menu = menus.get(0);
        return menu;
    }
}
