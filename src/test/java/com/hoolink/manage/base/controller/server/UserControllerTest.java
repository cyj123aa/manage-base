package com.hoolink.manage.base.controller.server;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.alibaba.fastjson.TypeReference;
import com.hoolink.manage.base.bo.MiddleUserDeptWithMoreBO;
import com.hoolink.manage.base.bo.UserDeptBO;
import com.hoolink.manage.base.controller.TestController;
import com.hoolink.manage.base.dao.mapper.UserMapper;
import com.hoolink.manage.base.dao.mapper.ext.MiddleUserDepartmentMapperExt;
import com.hoolink.manage.base.dao.model.User;
import com.hoolink.manage.base.service.MiddleUserDepartmentService;
import com.hoolink.sdk.bo.BackBO;
import com.hoolink.sdk.bo.manager.ManageUserInfoBO;
import com.hoolink.sdk.bo.manager.ManagerUserBO;
import com.hoolink.sdk.utils.JSONUtils;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * @author chenzhixiong
 * @date 2019/7/24 16:57
 */
public class UserControllerTest extends TestController {
    private Logger logger= LoggerFactory.getLogger(UserControllerTest.class);

    @Autowired
    private UserController userController;


    @MockBean
    private MiddleUserDepartmentMapperExt middleUserDepartmentMapperExt;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private MiddleUserDepartmentService middleUserDepartmentService;

    @Before
    public void setUp() {
        super.setUp();
        //控制类通过@Autowired自动注入进来，以此构建mockMvc对象
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void getManagerUserInfo() throws Exception {
        String getUserListBackBO = "[{\"created\":1562293383052,\"creator\":57,\"enabled\":true,\"encryLevelCompany\":1,\"firstLogin\":false,\"id\":289,\"lastTime\":1562293383660,\"name\":\"UN82367944572\",\"passwd\":\"70082FEB1262CF1EA85E54D5DFCD82BA\",\"position\":\"PS83335873564\",\"roleId\":344,\"sex\":true,\"status\":false,\"updated\":1562293383895,\"updator\":57,\"userAccount\":\"UA57586154539\",\"userNo\":\"UNO79742693034\"}]";
        when(userMapper.selectByExample(any())).thenReturn(JSONUtils.toList(getUserListBackBO, User.class));
        String middleUserDeptBackBO = "[{\"deptId\":1,\"deptName\":\"晶日\",\"deptType\":1,\"diffDeptGroup\":\"089066e9f02c457294042e8e3b357d1049397938\",\"encryLevelDept\":1,\"encryLevelDeptName\":\"特级\",\"id\":1136,\"lowestLevel\":false,\"userId\":286},{\"deptId\":11,\"deptName\":\"信息文控管理体系中心\",\"deptType\":4,\"diffDeptGroup\":\"089066e9f02c457294042e8e3b357d1049397938\",\"encryLevelDept\":1,\"encryLevelDeptName\":\"特级\",\"id\":1137,\"lowestLevel\":true,\"userId\":286},{\"deptId\":1,\"deptName\":\"晶日\",\"deptType\":1,\"diffDeptGroup\":\"ba3e5a41cc3e4b62a6da2d96dad940d578782694\",\"encryLevelDept\":1,\"encryLevelDeptName\":\"特级\",\"id\":1142,\"lowestLevel\":false,\"userId\":289},{\"deptId\":11,\"deptName\":\"信息文控管理体系中心\",\"deptType\":4,\"diffDeptGroup\":\"ba3e5a41cc3e4b62a6da2d96dad940d578782694\",\"encryLevelDept\":1,\"encryLevelDeptName\":\"特级\",\"id\":1143,\"lowestLevel\":true,\"userId\":289}]";
        when(middleUserDepartmentService.listWithMoreByUserIdList(any())).thenReturn(JSONUtils.toList(middleUserDeptBackBO, MiddleUserDeptWithMoreBO.class));
        String param = "[" + " 286,289" + "]";
        logger.info("url:{},参数param:{}", "/user/getManagerUserInfo", param);
        String contentAsString = postRequestMethod(param, "/user/getManagerUserInfo");
        BackBO<List<ManagerUserBO>> listBackBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO<List<ManagerUserBO>>>() {});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true, listBackBO.getStatus());

    }


    @Test
    public void getUserList() throws Exception {
        String getUserListBackBO = "[{\"created\":1559611950609,\"creator\":10,\"enabled\":true,\"encryLevelCompany\":1,\"firstLogin\":false,\"id\":53,\"name\":\"李依依\",\"passwd\":\"70082FEB1262CF1EA85E54D5DFCD82BA\",\"position\":\"测试\",\"roleId\":11,\"sex\":false,\"status\":true,\"updated\":1561368659383,\"updator\":9,\"userAccount\":\"lyy\",\"userNo\":\"4001\"},{\"created\":1559611950642,\"creator\":10,\"enabled\":true,\"encryLevelCompany\":5,\"firstLogin\":false,\"id\":56,\"imgId\":1757,\"name\":\"欧阳娜娜\",\"passwd\":\"70082FEB1262CF1EA85E54D5DFCD82BA\",\"position\":\"开发\",\"roleId\":12,\"sex\":false,\"status\":true,\"updated\":1563418987693,\"updator\":60,\"userAccount\":\"oynn\",\"userNo\":\"4004\"}]";
        when(userMapper.selectByExample(any())).thenReturn(JSONUtils.toList(getUserListBackBO, User.class));

        String param = "[" + " 53,56" + "]";
        logger.info("url:{},参数param:{}", "/user/getUserList", param);
        String contentAsString = postRequestMethod(param, "/user/getUserList");
        BackBO<List<ManagerUserBO>> listBackBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO<List<ManagerUserBO>>>() {});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true, listBackBO.getStatus());

    }


    @Test
    public void getUserInfoById() throws Exception {
        String getUserListBackBO = "{\"created\":1559611950609,\"creator\":10,\"enabled\":true,\"encryLevelCompany\":1,\"firstLogin\":false,\"id\":53,\"name\":\"李依依\",\"passwd\":\"70082FEB1262CF1EA85E54D5DFCD82BA\",\"position\":\"测试\",\"roleId\":11,\"sex\":false,\"status\":true,\"updated\":1561368659383,\"updator\":9,\"userAccount\":\"lyy\",\"userNo\":\"4001\"}";
        when(userMapper.selectByPrimaryKey(any())).thenReturn(JSONUtils.parse(getUserListBackBO, User.class));
        String getMiddleListBackBO = "[{\"deptId\":2,\"deptName\":\"互灵\",\"deptType\":1,\"encryLevelDept\":4},{\"deptId\":2,\"deptName\":\"互灵\",\"deptType\":1,\"encryLevelDept\":1}]";
        when(middleUserDepartmentMapperExt.getUserDept(any(), any())).thenReturn(JSONUtils.toList(getMiddleListBackBO, UserDeptBO.class));
        String param = "53";
        logger.info("url:{},参数param:{}", "/user/getUserInfoById", param);
        String contentAsString = postRequestMethod(param, "/user/getUserInfoById");
        BackBO<ManageUserInfoBO> listBackBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO<ManageUserInfoBO>>() {});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true, listBackBO.getStatus());

    }

}
