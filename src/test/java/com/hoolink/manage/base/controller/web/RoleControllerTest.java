package com.hoolink.manage.base.controller.web;

import com.alibaba.fastjson.TypeReference;
import com.hoolink.manage.base.controller.TestController;
import com.hoolink.manage.base.service.SessionService;
import com.hoolink.sdk.bo.BackBO;
import com.hoolink.sdk.bo.base.CurrentUserBO;
import com.hoolink.sdk.bo.edm.EdmMenuTreeBO;
import com.hoolink.sdk.param.BaseParam;
import com.hoolink.sdk.utils.JSONUtils;
import org.apache.servicecomb.swagger.invocation.context.ContextUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

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

    @Before
    public void setUp() {
        super.setUp();
        //控制类通过@Autowired自动注入进来，以此构建mockMvc对象
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
    }

    @Test
    public void create() throws Exception {
        String param="{\"roleDesc\":\"测试\",\"roleName\":\"测试账号\",\"roleType\":false,\"roleMenuVOList\":[{\"menuId\":1,\"permissionFlag\":null},{\"menuId\":2,\"permissionFlag\":1}]}";
        String contentAsString = postRequestMethod(param, "/web/role/create");
        BackBO backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO<Long>>(){});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true,backBO.getStatus());

        //业务异常--用户无访问权限  当前用户角色3级
        mockThreeUser();
        param = "{\"roleDesc\":\"测试\",\"roleName\":\"测试账号\",\"roleType\":false,\"roleMenuVOList\":[{\"menuId\":1,\"permissionFlag\":null},{\"menuId\":2,\"permissionFlag\":1}]}";
        contentAsString = postRequestMethod(param, "/web/role/create");
        backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO<Long>>() {});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(false,backBO.getStatus());
    }

    /**
     * 3级角色用户
     */
    private void mockThreeUser() {
        CurrentUserBO baseUserBO = new CurrentUserBO();
        baseUserBO.setUserId(34L);
        baseUserBO.setRoleId(12L);
        baseUserBO.setAccount("abc");
        ContextUtils.setInvocationContext(invocationContext);
        invocationContext.addContext("manageCurrentUser", JSONUtils.toJSONString(baseUserBO));
    }

    @Test
    public void update() throws Exception {
        String param="{\"id\":\"12\",\"roleDesc\":\"三级\",\"roleMenuVOList\":[{\"menuId\":4,\"permissionFlag\":null},{\"menuId\":5,\"permissionFlag\":1},{\"menuId\":7,\"permissionFlag\":1}],\"roleName\":\"三级22\",\"roleType\":false}";
        String contentAsString = postRequestMethod(param, "/web/role/update");
        BackBO backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO>(){});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true,backBO.getStatus());
    }

    @Test
    public void updateStatus() throws Exception {
        String param="{\"id\":12,\"roleStatus\":false}";
        String contentAsString = postRequestMethod(param, "/web/role/updateStatus");
        BackBO backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO>(){});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true,backBO.getStatus());
    }

    @Test
    public void getById() throws Exception {
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
        String contentAsString = postRequestMethod("{}", "/web/role/getBaseMenu");
        BackBO backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO>(){});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true,backBO.getStatus());
    }

    @Test
    public void getCurrentRoleMenu() throws Exception {
        String contentAsString = postRequestMethod("", "/web/role/getCurrentRoleMenu");
        BackBO backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO>(){});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true,backBO.getStatus());
    }

    @Test
    public void listByPage() throws Exception {
        String param="{\"searchValue\":\"角色\",\"status\":true,\"pageNo\":1,\"pageSize\":10}";
        String contentAsString = postRequestMethod(param, "/web/role/listByPage");
        BackBO backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO>(){});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true,backBO.getStatus());
    }

    @Test
    public void getSupportBaseMenu() throws Exception {
        when(sessionService.getUserIdByToken()).thenReturn(9L);
        String contentAsString = postRequestMethod("{}", "/web/role/getSupportBaseMenu");
        BackBO backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO>(){});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true,backBO.getStatus());
    }
}