package com.hoolink.manage.base.controller.web;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hoolink.manage.base.bo.ManageRoleBO;
import com.hoolink.manage.base.bo.MiddleUserDeptWithMoreBO;
import com.hoolink.manage.base.controller.TestController;
import com.hoolink.manage.base.dao.mapper.ManageRoleMapper;
import com.hoolink.manage.base.dao.mapper.UserMapper;
import com.hoolink.manage.base.dao.model.ManageRole;
import com.hoolink.manage.base.dao.model.User;
import com.hoolink.manage.base.service.MiddleUserDepartmentService;
import com.hoolink.manage.base.service.RoleService;
import com.hoolink.manage.base.service.SessionService;
import com.hoolink.manage.base.service.UserService;
import com.hoolink.manage.base.vo.req.LoginParamVO;
import com.hoolink.manage.base.vo.req.PhoneParamVO;
import com.hoolink.manage.base.vo.res.LoginResultVO;
import com.hoolink.sdk.param.BaseParam;
import com.hoolink.sdk.utils.JSONUtils;
import com.hoolink.sdk.vo.BackVO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @Author: xuli
 * @Date: 2019/7/17 14:25
 */
public class UserControllerTest extends TestController {

    private Logger log= LoggerFactory.getLogger(com.hoolink.manage.base.controller.server.UserController.class);

    @Autowired
    private UserController userController;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private ManageRoleMapper manageRoleMapper;

    @MockBean
    private RoleService roleService;

    @MockBean
    private MiddleUserDepartmentService middleUserDepartmentService;

    @MockBean
    private SessionService sessionService;

    @Before
    public void setUp(){
        super.setUp();
        mockMvc= MockMvcBuilders.standaloneSetup(userController).build();
    }

    /**
     * 正常登录
     * @throws Exception
     */
    @Test
    public void loginRight() throws Exception{
        LoginParamVO loginParam=new LoginParamVO();
        loginParam.setAccount("admin");
        loginParam.setPasswd("36d5c979160b44d9549f09c76638cab8");

        User user=new User();
        user.setId(1L);
        user.setUserAccount("1");
        user.setFirstLogin(false);
        user.setPhone("18596898523");
        user.setStatus(true);
        user.setRoleId(1L);
        user.setImgId(1L);
        List<User> list=new ArrayList<>();
        list.add(user);
        when(userMapper.selectByExample(any())).thenReturn(list);

        ManageRole role=new ManageRole();
        role.setId(1L);
        when(manageRoleMapper.selectByPrimaryKey(any())).thenReturn(role);

        ManageRoleBO manageRoleBO=new ManageRoleBO();
        manageRoleBO.setId(1L);
        manageRoleBO.setRoleStatus(true);
        manageRoleBO.setRoleLevel((byte)1);
        when(roleService.selectById(any())).thenReturn(manageRoleBO);

        List<MiddleUserDeptWithMoreBO> moreBOS=new ArrayList<>();
        MiddleUserDeptWithMoreBO moreBO=new MiddleUserDeptWithMoreBO();
        moreBO.setDeptType((byte)1);
        moreBO.setDeptId(1L);
        moreBOS.add(moreBO);
        when(middleUserDepartmentService.listWithMoreByUserIdList(any())).thenReturn(moreBOS);

        Set<String> set=new HashSet<>();
        set.add("/manage-base/user/bindPhone");
        when(roleService.listAccessUrlByRoleId(any())).thenReturn(set);

        when(sessionService.cacheCurrentUser(any(),any())).thenReturn("");

        when(userMapper.updateByPrimaryKeySelective(any())).thenReturn(1);

        String param=JSONObject.toJSONString(loginParam);
        String contentAsString = postRequestMethod(param, "/web/user/login");
        BackVO backVO= JSONUtils.parse(contentAsString,new TypeReference<BackVO<LoginResultVO>>(){});
        Assert.assertEquals(true,backVO.getStatus());
    }

    @Test
    public void loginFalse() throws Exception{
        LoginParamVO loginParam=new LoginParamVO();
        loginParam.setAccount("admin");
        loginParam.setPasswd("123456");
        String param=JSONObject.toJSONString(loginParam);
        String contentAsString = postRequestMethod(param, "/web/user/login");
        BackVO backVO= JSONUtils.parse(contentAsString,new TypeReference<BackVO<LoginResultVO>>(){});
        Assert.assertEquals(false,backVO.getStatus());
    }

    @Test
    public void logout() throws Exception{
        String contentAsString = postRequestMethod("", "/web/user/logout");
        BackVO backVO= JSONUtils.parse(contentAsString,new TypeReference<BackVO<LoginResultVO>>(){});
        Assert.assertEquals(true,backVO.getStatus());
    }

    @Test
    public void resetPasswordRight() throws Exception{
        LoginParamVO loginParam=new LoginParamVO();
        loginParam.setAccount("admin");
        loginParam.setPasswd("36d5c979160b44d9549f09c76638cab8");
        loginParam.setCode("hoolink2019");
        String param=JSONObject.toJSONString(loginParam);
        String contentAsString = postRequestMethod(param, "/web/user/resetPassword");
        BackVO backVO= JSONUtils.parse(contentAsString,new TypeReference<BackVO<LoginResultVO>>(){});
        Assert.assertEquals(true,backVO.getStatus());
    }

    @Test
    public void resetPasswordFalse() throws Exception{
        LoginParamVO loginParam=new LoginParamVO();
        loginParam.setAccount("admin");
        loginParam.setPasswd("36d5c979160b44d9549f09c76638cab8");
        String param=JSONObject.toJSONString(loginParam);
        String contentAsString = postRequestMethod(param, "/web/user/resetPassword");
        BackVO backVO= JSONUtils.parse(contentAsString,new TypeReference<BackVO<LoginResultVO>>(){});
        Assert.assertEquals(false,backVO.getStatus());
    }

    @Test
    public void getPhoneCode() throws Exception{
        BaseParam<String> phone=new BaseParam<>();
        phone.setData("15589745896");
        String param=JSONObject.toJSONString(phone);
        String contentAsString = postRequestMethod(param, "/web/user/getPhoneCode");
        BackVO backVO= JSONUtils.parse(contentAsString,new TypeReference<BackVO<LoginResultVO>>(){});
        Assert.assertEquals(true,backVO.getStatus());
    }

    @Test
    public void verifyPhoneCodeRight() throws Exception{
        PhoneParamVO paramVO=new PhoneParamVO();
        paramVO.setPhone("18568958536");
        paramVO.setCode("hoolink2019");
        String param=JSONObject.toJSONString(paramVO);
        String contentAsString = postRequestMethod(param, "/web/user/verifyPhoneCode");
        BackVO backVO= JSONUtils.parse(contentAsString,new TypeReference<BackVO<LoginResultVO>>(){});
        Assert.assertEquals(true,backVO.getStatus());
    }

    @Test
    public void verifyPhoneCodeFalse() throws Exception{
        PhoneParamVO paramVO=new PhoneParamVO();
        paramVO.setPhone("18568958536");
        paramVO.setCode("123");
        String param=JSONObject.toJSONString(paramVO);
        String contentAsString = postRequestMethod(param, "/web/user/verifyPhoneCode");
        BackVO backVO= JSONUtils.parse(contentAsString,new TypeReference<BackVO<LoginResultVO>>(){});
        Assert.assertEquals(false,backVO.getStatus());
    }

    @Test
    public void bindPhone() throws Exception{
        PhoneParamVO paramVO=new PhoneParamVO();
        paramVO.setPhone("18568958536");
        String param=JSONObject.toJSONString(paramVO);
        String contentAsString = postRequestMethod(param, "/web/user/bindPhone");
        BackVO backVO= JSONUtils.parse(contentAsString,new TypeReference<BackVO<LoginResultVO>>(){});
        Assert.assertEquals(true,backVO.getStatus());
    }

    @Test
    public void forgetPassword() throws Exception{
        LoginParamVO paramVO=new LoginParamVO();
        paramVO.setAccount("admin");
        String param=JSONObject.toJSONString(paramVO);
        String contentAsString = postRequestMethod(param, "/web/user/forgetPassword");
        BackVO backVO= JSONUtils.parse(contentAsString,new TypeReference<BackVO<LoginResultVO>>(){});
        Assert.assertEquals(true,backVO.getStatus());
    }

    @Test
    public void getUserInfo() throws Exception{
        String contentAsString = postRequestMethod("", "/web/user/getUserInfo");
        BackVO backVO= JSONUtils.parse(contentAsString,new TypeReference<BackVO<LoginResultVO>>(){});
        Assert.assertEquals(true,backVO.getStatus());
    }

    @Test
    public void updatePhoneRight() throws Exception{
        PhoneParamVO paramVO=new PhoneParamVO();
        paramVO.setPhone("18958965623");
        paramVO.setCode("hoolink2019");
        String param=JSONObject.toJSONString(paramVO);
        String contentAsString = postRequestMethod(param, "/web/user/updatePhone");
        BackVO backVO= JSONUtils.parse(contentAsString,new TypeReference<BackVO<LoginResultVO>>(){});
        Assert.assertEquals(true,backVO.getStatus());
    }

    @Test
    public void updatePhoneFalse() throws Exception{
        PhoneParamVO paramVO=new PhoneParamVO();
        paramVO.setPhone("18958965623");
        paramVO.setCode("123");
        String param=JSONObject.toJSONString(paramVO);
        String contentAsString = postRequestMethod(param, "/web/user/updatePhone");
        BackVO backVO= JSONUtils.parse(contentAsString,new TypeReference<BackVO<LoginResultVO>>(){});
        Assert.assertEquals(false,backVO.getStatus());
    }


}
