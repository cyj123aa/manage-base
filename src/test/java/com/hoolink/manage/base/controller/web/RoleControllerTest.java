package com.hoolink.manage.base.controller.web;

import com.alibaba.fastjson.TypeReference;
import com.hoolink.manage.base.bo.MiddleRoleMenuBO;
import com.hoolink.manage.base.controller.TestController;
import com.hoolink.manage.base.dao.mapper.ManageRoleMapper;
import com.hoolink.manage.base.dao.mapper.MiddleRoleMenuMapper;
import com.hoolink.manage.base.dao.mapper.UserMapper;
import com.hoolink.manage.base.dao.mapper.ext.ManageMenuMapperExt;
import com.hoolink.manage.base.dao.mapper.ext.ManageRoleMapperExt;
import com.hoolink.manage.base.dao.mapper.ext.MiddleRoleMenuMapperExt;
import com.hoolink.manage.base.dao.model.ManageMenu;
import com.hoolink.manage.base.dao.model.ManageRole;
import com.hoolink.manage.base.service.MenuService;
import com.hoolink.manage.base.service.SessionService;
import com.hoolink.manage.base.util.RedisUtil;
import com.hoolink.sdk.bo.BackBO;
import com.hoolink.sdk.param.BaseParam;
import com.hoolink.sdk.utils.JSONUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * 测试
 * @author ：weimin
 */
public class RoleControllerTest extends TestController {

    @Autowired
    private RoleController roleController;

    @MockBean
    private SessionService sessionService;
    @MockBean
    private ManageRoleMapper roleMapper;
    @MockBean
    private MiddleRoleMenuMapper roleMenuMapper;
    @MockBean
    private MiddleRoleMenuMapperExt roleMenuMapperExt;
    @MockBean
    private ManageRoleMapperExt manageRoleMapperExt;
    @MockBean
    private RedisUtil redisUtil;
    @MockBean
    private ManageMenuMapperExt manageMenuMapperExt;
    @MockBean
    private UserMapper userMapper;
    @MockBean
    private MenuService menuService;

    @Before
    public void setUp() {
        super.setUp();
        //控制类通过@Autowired自动注入进来，以此构建mockMvc对象
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
    }

    @Test
    public void create() throws Exception {
        when(roleMapper.selectByExample(any())).thenReturn(null);
        String userRole="{\"created\":1,\"creator\":1,\"enabled\":true,\"id\":1,\"parentId\":0,\"roleDesc\":\"一级管理员\",\"roleLevel\":1,\"roleName\":\"超级管理员\",\"roleStatus\":true,\"roleType\":false}";
        when(manageRoleMapperExt.getUserRole(anyLong())).thenReturn(JSONUtils.parse(userRole, new TypeReference<ManageRole>(){}));
        when(roleMapper.insertSelective(any())).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                ManageRole role=invocationOnMock.getArgument(0);
                role.setId(1L);
                return 1;
            }
        });
        doNothing().when(roleMenuMapperExt).bulkInsert(any());
        doNothing().when(redisUtil).del(any());
        String param="{\"roleDesc\":\"测试\",\"roleName\":\"测试账号A\",\"roleType\":false,\"roleMenuVOList\":[{\"menuId\":1,\"permissionFlag\":null},{\"menuId\":2,\"permissionFlag\":1}]}";
        String contentAsString = postRequestMethod(param, "/web/role/create");
        BackBO backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO<Long>>(){});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true,backBO.getStatus());

        //业务异常--角色名称重复
        String manageRoles="[{\"created\":1563417750921,\"creator\":60,\"enabled\":true,\"id\":472,\"parentId\":7,\"roleDesc\":\"\",\"roleLevel\":3,\"roleName\":\"测试账号\",\"roleStatus\":true,\"roleType\":false,\"updated\":1563794160344,\"updator\":78}]";
        when(roleMapper.selectByExample(any())).thenReturn(JSONUtils.parse(manageRoles, new TypeReference<List<ManageRole>>(){}));
        contentAsString = postRequestMethod(param, "/web/role/create");
        backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO<Long>>(){});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(false,backBO.getStatus());

        //业务异常--用户无访问权限  当前用户角色3级
        when(roleMapper.selectByExample(any())).thenReturn(null);
        userRole="{\"created\":1559284808646,\"creator\":32,\"enabled\":true,\"id\":12,\"parentId\":7,\"roleDesc\":\"三级\",\"roleLevel\":3,\"roleName\":\"三级22\",\"roleStatus\":true,\"roleType\":false,\"updated\":1563418987691,\"updator\":60}";
        when(manageRoleMapperExt.getUserRole(anyLong())).thenReturn(JSONUtils.parse(userRole, new TypeReference<ManageRole>(){}));
        param = "{\"roleDesc\":\"测试\",\"roleName\":\"测试账号B\",\"roleType\":false,\"roleMenuVOList\":[{\"menuId\":1,\"permissionFlag\":null},{\"menuId\":2,\"permissionFlag\":1}]}";
        contentAsString = postRequestMethod(param, "/web/role/create");
        backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO<Long>>() {});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(false,backBO.getStatus());
    }

    @Test
    public void update() throws Exception {
        when(roleMapper.selectByExample(any())).thenReturn(null);
        when(roleMapper.updateByPrimaryKeySelective(any())).thenReturn(1);
        when(roleMenuMapper.deleteByExample(any())).thenReturn(1);
        doNothing().when(roleMenuMapperExt).bulkInsert(any());
        doNothing().when(redisUtil).del(any());
        when(userMapper.selectByExample(any())).thenReturn(null);
        String param="{\"id\":\"12\",\"roleDesc\":\"三级\",\"roleMenuVOList\":[{\"menuId\":4,\"permissionFlag\":null},{\"menuId\":5,\"permissionFlag\":1},{\"menuId\":7,\"permissionFlag\":1}],\"roleName\":\"三级22\",\"roleType\":false}";
        String contentAsString = postRequestMethod(param, "/web/role/update");
        BackBO backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO>(){});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true,backBO.getStatus());

        //业务异常--角色名称重复
        String manageRoles="[{\"created\":1563417750921,\"creator\":60,\"enabled\":true,\"id\":472,\"parentId\":7,\"roleDesc\":\"\",\"roleLevel\":3,\"roleName\":\"测试账号\",\"roleStatus\":true,\"roleType\":false,\"updated\":1563794160344,\"updator\":78}]";
        when(roleMapper.selectByExample(any())).thenReturn(JSONUtils.parse(manageRoles, new TypeReference<List<ManageRole>>(){}));
        contentAsString = postRequestMethod(param, "/web/role/create");
        backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO<Long>>(){});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(false,backBO.getStatus());
    }

    @Test
    public void updateStatus() throws Exception {
        when(roleMapper.updateByPrimaryKeySelective(any())).thenReturn(1);
        when(userMapper.selectByExample(any())).thenReturn(null);
        when(userMapper.updateByExampleSelective(any(),any())).thenReturn(1);
        String param="{\"id\":12,\"roleStatus\":false}";
        String contentAsString = postRequestMethod(param, "/web/role/updateStatus");
        BackBO backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO>(){});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true,backBO.getStatus());
    }

    @Test
    public void getById() throws Exception {
        String baseRole="[{\"created\":1559284808646,\"creator\":32,\"enabled\":true,\"id\":12,\"parentId\":7,\"roleDesc\":\"三级\",\"roleLevel\":3,\"roleName\":\"三级22\",\"roleStatus\":true,\"roleType\":false,\"updated\":1563418987691,\"updator\":60}]";
        when(roleMapper.selectByExample(any())).thenReturn(JSONUtils.parse(baseRole, new TypeReference<List<ManageRole>>(){}));
        String userRole="{\"created\":1,\"creator\":1,\"enabled\":true,\"id\":1,\"parentId\":0,\"roleDesc\":\"一级管理员\",\"roleLevel\":1,\"roleName\":\"超级管理员\",\"roleStatus\":true,\"roleType\":false}";
        when(manageRoleMapperExt.getUserRole(any())).thenReturn(JSONUtils.parse(userRole, new TypeReference<ManageRole>(){}));
        String roleMenu="[{\"menuId\":1,\"menuName\":\"管理中心\",\"parentId\":0,\"permissionFlag\":2},{\"menuId\":2,\"menuName\":\"用户管理\",\"parentId\":1,\"permissionFlag\":2},{\"menuId\":3,\"menuName\":\"角色管理\",\"parentId\":1,\"permissionFlag\":2},{\"menuId\":4,\"menuName\":\"EDM系统\",\"parentId\":0,\"permissionFlag\":2},{\"menuId\":5,\"menuName\":\"部门资源\",\"parentId\":4,\"permissionFlag\":2},{\"menuId\":6,\"menuName\":\"缓存库\",\"parentId\":4,\"permissionFlag\":2},{\"menuId\":7,\"menuName\":\"资源库\",\"parentId\":4,\"permissionFlag\":2},{\"menuId\":10,\"menuName\":\"hoolink管理平台\",\"parentId\":0,\"permissionFlag\":2},{\"menuId\":11,\"menuName\":\"客户管理\",\"parentId\":10,\"permissionFlag\":2},{\"menuId\":12,\"menuName\":\"项目管理\",\"parentId\":10,\"permissionFlag\":2},{\"menuId\":13,\"menuName\":\"设备管理\",\"parentId\":10,\"permissionFlag\":2},{\"menuId\":14,\"menuName\":\"设备列表\",\"parentId\":13,\"permissionFlag\":2},{\"menuId\":15,\"menuName\":\"设备模型\",\"parentId\":13,\"permissionFlag\":2},{\"menuId\":16,\"menuName\":\"设备固件\",\"parentId\":13,\"permissionFlag\":2},{\"menuId\":17,\"menuName\":\"设备质保\",\"parentId\":13,\"permissionFlag\":2},{\"menuId\":18,\"menuName\":\"远程升级\",\"parentId\":13,\"permissionFlag\":2},{\"menuId\":19,\"menuName\":\"强制回滚\",\"parentId\":13,\"permissionFlag\":2},{\"menuId\":20,\"menuName\":\"流程配置\",\"parentId\":10,\"permissionFlag\":2},{\"menuId\":21,\"menuName\":\"字典管理\",\"parentId\":10,\"permissionFlag\":2},{\"menuId\":22,\"menuName\":\"集中器同步\",\"parentId\":13,\"permissionFlag\":2},{\"menuId\":23,\"menuName\":\"数据中心\",\"parentId\":10,\"permissionFlag\":2}]";
        String roleMenu1="[{\"menuId\":4,\"menuName\":\"EDM系统\",\"parentId\":0},{\"menuId\":5,\"menuName\":\"部门资源\",\"parentId\":4,\"permissionFlag\":1},{\"menuId\":7,\"menuName\":\"资源库\",\"parentId\":4,\"permissionFlag\":1}]";
        when(manageMenuMapperExt.getRoleMenu(any())).thenReturn(JSONUtils.parse(roleMenu, new TypeReference<List<MiddleRoleMenuBO>>(){}))
                .thenReturn(JSONUtils.parse(roleMenu1, new TypeReference<List<MiddleRoleMenuBO>>(){}));
        BaseParam<Long> baseParam = new BaseParam<>();
        baseParam.setData(12L);
        String contentAsString = postRequestMethod(JSONUtils.toJSONString(baseParam), "/web/role/getById");
        BackBO backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO>(){});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true,backBO.getStatus());
    }

    @Test
    public void getBaseMenu() throws Exception {
        when(sessionService.getUserIdByToken()).thenReturn(9L);
        String userRole="{\"created\":1,\"creator\":1,\"enabled\":true,\"id\":1,\"parentId\":0,\"roleDesc\":\"一级管理员\",\"roleLevel\":1,\"roleName\":\"超级管理员\",\"roleStatus\":true,\"roleType\":false}";
        when(manageRoleMapperExt.getUserRole(any())).thenReturn(JSONUtils.parse(userRole, new TypeReference<ManageRole>(){}));
        String roleMenu="[{\"menuId\":1,\"menuName\":\"管理中心\",\"parentId\":0,\"permissionFlag\":2},{\"menuId\":2,\"menuName\":\"用户管理\",\"parentId\":1,\"permissionFlag\":2},{\"menuId\":3,\"menuName\":\"角色管理\",\"parentId\":1,\"permissionFlag\":2},{\"menuId\":4,\"menuName\":\"EDM系统\",\"parentId\":0,\"permissionFlag\":2},{\"menuId\":5,\"menuName\":\"部门资源\",\"parentId\":4,\"permissionFlag\":2},{\"menuId\":6,\"menuName\":\"缓存库\",\"parentId\":4,\"permissionFlag\":2},{\"menuId\":7,\"menuName\":\"资源库\",\"parentId\":4,\"permissionFlag\":2},{\"menuId\":10,\"menuName\":\"hoolink管理平台\",\"parentId\":0,\"permissionFlag\":2},{\"menuId\":11,\"menuName\":\"客户管理\",\"parentId\":10,\"permissionFlag\":2},{\"menuId\":12,\"menuName\":\"项目管理\",\"parentId\":10,\"permissionFlag\":2},{\"menuId\":13,\"menuName\":\"设备管理\",\"parentId\":10,\"permissionFlag\":2},{\"menuId\":14,\"menuName\":\"设备列表\",\"parentId\":13,\"permissionFlag\":2},{\"menuId\":15,\"menuName\":\"设备模型\",\"parentId\":13,\"permissionFlag\":2},{\"menuId\":16,\"menuName\":\"设备固件\",\"parentId\":13,\"permissionFlag\":2},{\"menuId\":17,\"menuName\":\"设备质保\",\"parentId\":13,\"permissionFlag\":2},{\"menuId\":18,\"menuName\":\"远程升级\",\"parentId\":13,\"permissionFlag\":2},{\"menuId\":19,\"menuName\":\"强制回滚\",\"parentId\":13,\"permissionFlag\":2},{\"menuId\":20,\"menuName\":\"流程配置\",\"parentId\":10,\"permissionFlag\":2},{\"menuId\":21,\"menuName\":\"字典管理\",\"parentId\":10,\"permissionFlag\":2},{\"menuId\":22,\"menuName\":\"集中器同步\",\"parentId\":13,\"permissionFlag\":2},{\"menuId\":23,\"menuName\":\"数据中心\",\"parentId\":10,\"permissionFlag\":2}]";
        when(manageMenuMapperExt.getRoleMenu(any())).thenReturn(JSONUtils.parse(roleMenu, new TypeReference<List<MiddleRoleMenuBO>>(){}));
        String byCode="{\"created\":1,\"creator\":1,\"enabled\":true,\"id\":1,\"menuCode\":\"MANAGEMENT_CENTER\",\"menuName\":\"管理中心\",\"parentId\":0,\"priority\":1,\"updated\":1,\"updator\":1,\"url\":\"/manage-base/web/role/getBaseMenu\"}";
        when(menuService.getByCode(any())).thenReturn(JSONUtils.parse(byCode, new TypeReference<ManageMenu>(){}));
        String contentAsString = postRequestMethod("{}", "/web/role/getBaseMenu");
        BackBO backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO>(){});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true,backBO.getStatus());
    }

    @Test
    public void getCurrentRoleMenu() throws Exception {
        String userRole="{\"created\":1,\"creator\":1,\"enabled\":true,\"id\":1,\"parentId\":0,\"roleDesc\":\"一级管理员\",\"roleLevel\":1,\"roleName\":\"超级管理员\",\"roleStatus\":true,\"roleType\":false}";
        when(manageRoleMapperExt.getUserRole(any())).thenReturn(JSONUtils.parse(userRole, new TypeReference<ManageRole>(){}));
        String roleMenu="[{\"menuId\":1,\"menuName\":\"管理中心\",\"parentId\":0,\"permissionFlag\":2},{\"menuId\":2,\"menuName\":\"用户管理\",\"parentId\":1,\"permissionFlag\":2},{\"menuId\":3,\"menuName\":\"角色管理\",\"parentId\":1,\"permissionFlag\":2},{\"menuId\":4,\"menuName\":\"EDM系统\",\"parentId\":0,\"permissionFlag\":2},{\"menuId\":5,\"menuName\":\"部门资源\",\"parentId\":4,\"permissionFlag\":2},{\"menuId\":6,\"menuName\":\"缓存库\",\"parentId\":4,\"permissionFlag\":2},{\"menuId\":7,\"menuName\":\"资源库\",\"parentId\":4,\"permissionFlag\":2},{\"menuId\":10,\"menuName\":\"hoolink管理平台\",\"parentId\":0,\"permissionFlag\":2},{\"menuId\":11,\"menuName\":\"客户管理\",\"parentId\":10,\"permissionFlag\":2},{\"menuId\":12,\"menuName\":\"项目管理\",\"parentId\":10,\"permissionFlag\":2},{\"menuId\":13,\"menuName\":\"设备管理\",\"parentId\":10,\"permissionFlag\":2},{\"menuId\":14,\"menuName\":\"设备列表\",\"parentId\":13,\"permissionFlag\":2},{\"menuId\":15,\"menuName\":\"设备模型\",\"parentId\":13,\"permissionFlag\":2},{\"menuId\":16,\"menuName\":\"设备固件\",\"parentId\":13,\"permissionFlag\":2},{\"menuId\":17,\"menuName\":\"设备质保\",\"parentId\":13,\"permissionFlag\":2},{\"menuId\":18,\"menuName\":\"远程升级\",\"parentId\":13,\"permissionFlag\":2},{\"menuId\":19,\"menuName\":\"强制回滚\",\"parentId\":13,\"permissionFlag\":2},{\"menuId\":20,\"menuName\":\"流程配置\",\"parentId\":10,\"permissionFlag\":2},{\"menuId\":21,\"menuName\":\"字典管理\",\"parentId\":10,\"permissionFlag\":2},{\"menuId\":22,\"menuName\":\"集中器同步\",\"parentId\":13,\"permissionFlag\":2},{\"menuId\":23,\"menuName\":\"数据中心\",\"parentId\":10,\"permissionFlag\":2}]";
        when(manageMenuMapperExt.getRoleMenu(any())).thenReturn(JSONUtils.parse(roleMenu, new TypeReference<List<MiddleRoleMenuBO>>(){}));
        String contentAsString = postRequestMethod("", "/web/role/getCurrentRoleMenu");
        BackBO backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO>(){});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true,backBO.getStatus());
    }

    @Test
    public void listByPage() throws Exception {
        String userRole="{\"created\":1,\"creator\":1,\"enabled\":true,\"id\":1,\"parentId\":0,\"roleDesc\":\"一级管理员\",\"roleLevel\":1,\"roleName\":\"超级管理员\",\"roleStatus\":true,\"roleType\":false}";
        when(manageRoleMapperExt.getUserRole(any())).thenReturn(JSONUtils.parse(userRole, new TypeReference<ManageRole>(){}));
        String roles="[{\"created\":1563765624918,\"creator\":9,\"enabled\":true,\"id\":515,\"parentId\":1,\"roleDesc\":\"zlx临时用角色\",\"roleLevel\":2,\"roleName\":\"zlx临时用角色\",\"roleStatus\":true,\"roleType\":false},{\"created\":1563271647232,\"creator\":60,\"enabled\":true,\"id\":461,\"parentId\":7,\"roleDesc\":\"\",\"roleLevel\":3,\"roleName\":\"大角色22\",\"roleStatus\":true,\"roleType\":false,\"updated\":1563772133258,\"updator\":60},{\"created\":1562816165670,\"creator\":104,\"enabled\":true,\"id\":422,\"parentId\":169,\"roleDesc\":\"三级角色\",\"roleLevel\":3,\"roleName\":\"xiao文控下级角色\",\"roleStatus\":true,\"roleType\":false,\"updated\":1562827326628,\"updator\":13},{\"created\":1562741632374,\"creator\":22,\"enabled\":true,\"id\":401,\"parentId\":1,\"roleDesc\":\"程总特用角色所有的文件都能操作\",\"roleLevel\":2,\"roleName\":\"总经理角色\",\"roleStatus\":true,\"roleType\":true,\"updated\":1562745286322,\"updator\":22},{\"created\":1562729526074,\"creator\":22,\"enabled\":true,\"id\":398,\"parentId\":1,\"roleDesc\":\"EDM文控角色\",\"roleLevel\":2,\"roleName\":\"EDM文控角色\",\"roleStatus\":true,\"roleType\":true,\"updated\":1562740568406,\"updator\":22},{\"created\":1562725433785,\"creator\":22,\"enabled\":true,\"id\":395,\"parentId\":1,\"roleDesc\":\"zjt文控角色\",\"roleLevel\":2,\"roleName\":\"zjt文控角色\",\"roleStatus\":true,\"roleType\":true},{\"created\":1562584408946,\"creator\":9,\"enabled\":true,\"id\":374,\"parentId\":1,\"roleDesc\":\"就是一个角色\",\"roleLevel\":2,\"roleName\":\"就是一个角色\",\"roleStatus\":true,\"roleType\":false,\"updated\":1562666051281,\"updator\":15},{\"created\":1562558178404,\"creator\":22,\"enabled\":true,\"id\":361,\"parentId\":1,\"roleDesc\":\"111111\",\"roleLevel\":2,\"roleName\":\"zjt子角色1\",\"roleStatus\":true,\"roleType\":false,\"updated\":1562558402611,\"updator\":22},{\"created\":1562141524545,\"creator\":73,\"enabled\":true,\"id\":314,\"parentId\":7,\"roleDesc\":\"1111\",\"roleLevel\":3,\"roleName\":\"角色001\",\"roleStatus\":true,\"roleType\":false,\"updated\":1563171218949,\"updator\":78},{\"created\":1562049905190,\"creator\":18,\"enabled\":true,\"id\":253,\"parentId\":1,\"roleDesc\":\"zlx测试用文控角色勿动\",\"roleLevel\":2,\"roleName\":\"zlx测试用文控角色勿动\",\"roleStatus\":true,\"roleType\":true,\"updated\":1562826251482,\"updator\":9}]";
        when(manageRoleMapperExt.getRoleByOne(anyLong(),anyString(),anyBoolean())).thenReturn(JSONUtils.parse(roles, new TypeReference<List<ManageRole>>(){}));
        String param="{\"searchValue\":\"角色\",\"status\":true,\"pageNo\":1,\"pageSize\":10}";
        String contentAsString = postRequestMethod(param, "/web/role/listByPage");
        BackBO backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO>(){});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true,backBO.getStatus());
    }

    @Test
    public void getSupportBaseMenu() throws Exception {
        when(sessionService.getUserIdByToken()).thenReturn(9L);
        String userRole="{\"created\":1,\"creator\":1,\"enabled\":true,\"id\":1,\"parentId\":0,\"roleDesc\":\"一级管理员\",\"roleLevel\":1,\"roleName\":\"超级管理员\",\"roleStatus\":true,\"roleType\":false}";
        when(manageRoleMapperExt.getUserRole(any())).thenReturn(JSONUtils.parse(userRole, new TypeReference<ManageRole>(){}));
        String roleMenu="[{\"menuId\":1,\"menuName\":\"管理中心\",\"parentId\":0,\"permissionFlag\":2},{\"menuId\":2,\"menuName\":\"用户管理\",\"parentId\":1,\"permissionFlag\":2},{\"menuId\":3,\"menuName\":\"角色管理\",\"parentId\":1,\"permissionFlag\":2},{\"menuId\":4,\"menuName\":\"EDM系统\",\"parentId\":0,\"permissionFlag\":2},{\"menuId\":5,\"menuName\":\"部门资源\",\"parentId\":4,\"permissionFlag\":2},{\"menuId\":6,\"menuName\":\"缓存库\",\"parentId\":4,\"permissionFlag\":2},{\"menuId\":7,\"menuName\":\"资源库\",\"parentId\":4,\"permissionFlag\":2},{\"menuId\":10,\"menuName\":\"hoolink管理平台\",\"parentId\":0,\"permissionFlag\":2},{\"menuId\":11,\"menuName\":\"客户管理\",\"parentId\":10,\"permissionFlag\":2},{\"menuId\":12,\"menuName\":\"项目管理\",\"parentId\":10,\"permissionFlag\":2},{\"menuId\":13,\"menuName\":\"设备管理\",\"parentId\":10,\"permissionFlag\":2},{\"menuId\":14,\"menuName\":\"设备列表\",\"parentId\":13,\"permissionFlag\":2},{\"menuId\":15,\"menuName\":\"设备模型\",\"parentId\":13,\"permissionFlag\":2},{\"menuId\":16,\"menuName\":\"设备固件\",\"parentId\":13,\"permissionFlag\":2},{\"menuId\":17,\"menuName\":\"设备质保\",\"parentId\":13,\"permissionFlag\":2},{\"menuId\":18,\"menuName\":\"远程升级\",\"parentId\":13,\"permissionFlag\":2},{\"menuId\":19,\"menuName\":\"强制回滚\",\"parentId\":13,\"permissionFlag\":2},{\"menuId\":20,\"menuName\":\"流程配置\",\"parentId\":10,\"permissionFlag\":2},{\"menuId\":21,\"menuName\":\"字典管理\",\"parentId\":10,\"permissionFlag\":2},{\"menuId\":22,\"menuName\":\"集中器同步\",\"parentId\":13,\"permissionFlag\":2},{\"menuId\":23,\"menuName\":\"数据中心\",\"parentId\":10,\"permissionFlag\":2}]";
        when(manageMenuMapperExt.getRoleMenu(any())).thenReturn(JSONUtils.parse(roleMenu, new TypeReference<List<MiddleRoleMenuBO>>(){}));
        String byCode="{\"created\":1,\"creator\":1,\"enabled\":true,\"id\":10,\"menuCode\":\"HOOLINK_MANAGE\",\"menuName\":\"hoolink管理平台\",\"parentId\":0,\"priority\":3,\"updated\":1,\"updator\":1}";
        when(menuService.getByCode(any())).thenReturn(JSONUtils.parse(byCode, new TypeReference<ManageMenu>(){}));
        String contentAsString = postRequestMethod("{}", "/web/role/getSupportBaseMenu");
        BackBO backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO>(){});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true,backBO.getStatus());
    }
}