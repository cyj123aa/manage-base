package com.hoolink.manage.base.controller.server;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.alibaba.fastjson.TypeReference;
import com.hoolink.manage.base.bo.MiddleUserDeptWithMoreBO;
import com.hoolink.manage.base.bo.UserDeptBO;
import com.hoolink.manage.base.bo.UserSecurityBO;
import com.hoolink.manage.base.controller.TestController;
import com.hoolink.manage.base.dao.mapper.ManageDepartmentMapper;
import com.hoolink.manage.base.dao.mapper.MiddleUserDepartmentMapper;
import com.hoolink.manage.base.dao.mapper.UserMapper;
import com.hoolink.manage.base.dao.mapper.ext.MiddleUserDepartmentMapperExt;
import com.hoolink.manage.base.dao.mapper.ext.UserMapperExt;
import com.hoolink.manage.base.dao.model.ManageDepartment;
import com.hoolink.manage.base.dao.model.MiddleUserDepartment;
import com.hoolink.manage.base.dao.model.User;
import com.hoolink.manage.base.service.MiddleUserDepartmentService;
import com.hoolink.sdk.bo.BackBO;
import com.hoolink.sdk.bo.manager.*;
import com.hoolink.sdk.utils.JSONUtils;
import java.util.List;

import com.hoolink.sdk.vo.BackVO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
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

    @MockBean
    private UserMapperExt userMapperExt;

    @MockBean
    private MiddleUserDepartmentMapper middleUserDepartmentMapper;

    @MockBean
    private ManageDepartmentMapper manageDepartmentMapper;

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

    @Test
    public void getById() throws Exception{
        String userResultData = "{\"created\":1556444743803,\"creator\":1,\"enabled\":true,\"encryLevelCompany\":0,\"firstLogin\":false,\"id\":10,\"imgId\":1,\"lastTime\":1564040790913,\"name\":\"test\",\"passwd\":\"70082FEB1262CF1EA85E54D5DFCD82BA\",\"position\":\"\",\"roleId\":1,\"sex\":true,\"status\":true,\"updated\":1558576724562,\"updator\":10,\"userAccount\":\"test\",\"userNo\":\"a002\",\"viewEncryLevelPermitted\":false}";
        Mockito.when(userMapper.selectByPrimaryKey(any())).thenReturn(JSONUtils.parse(userResultData, new TypeReference<User>(){}));
        String resultContent = postRequestMethod("10", "/user/getById");
        BackVO backVO = JSONUtils.parse(resultContent, BackVO.class);
        Assert.assertEquals(true, backVO.getStatus());
    }

    @Test
    public void listByIdList() throws Exception{
        String userResultData = "[{\"id\":9,\"userNo\":\"a001\",\"name\":\"admin\",\"position\":\"55\",\"roleId\":1,\"roleName\":null,\"company\":null,\"phone\":\"13603060194\",\"userAccount\":\"admin\",\"encryLevelCompany\":1,\"encryLevelCompanyName\":null,\"status\":true,\"statusDesc\":null,\"sex\":true,\"sexDesc\":null,\"created\":1556444743803,\"lastTime\":1564117937765,\"userDeptPairList\":null},{\"id\":10,\"userNo\":\"a002\",\"name\":\"test\",\"position\":\"\",\"roleId\":1,\"roleName\":null,\"company\":null,\"phone\":null,\"userAccount\":\"test\",\"encryLevelCompany\":0,\"encryLevelCompanyName\":null,\"status\":true,\"statusDesc\":null,\"sex\":true,\"sexDesc\":null,\"created\":1556444743803,\"lastTime\":1564040790913,\"userDeptPairList\":null}]";
        Mockito.when(userMapper.selectByExample(any())).thenReturn(JSONUtils.parse(userResultData, new TypeReference<List<User>>(){}));
        String resultContent = postRequestMethod("[1]", "/user/listByIdList");
        BackVO backVO = JSONUtils.parse(resultContent, BackVO.class);
        Assert.assertEquals(true, backVO.getStatus());
    }

    @Test
    public void getUserSecurity() throws Exception{
        String userSecurityResultData = "{\"encryLevelCompany\":0,\"id\":12,\"list\":[{\"deptType\":4,\"encryLevelDept\":2,\"id\":3,\"lowestLevel\":false,\"parentId\":1},{\"deptType\":1,\"encryLevelDept\":2,\"id\":1,\"lowestLevel\":false,\"parentId\":0},{\"deptType\":4,\"encryLevelDept\":2,\"id\":5,\"lowestLevel\":false,\"parentId\":1},{\"deptType\":4,\"encryLevelDept\":2,\"id\":6,\"lowestLevel\":false,\"parentId\":1},{\"deptType\":4,\"encryLevelDept\":2,\"id\":7,\"lowestLevel\":false,\"parentId\":1}]}";
        Mockito.when(middleUserDepartmentMapperExt.getUserSecurity(any())).thenReturn(JSONUtils.parse(userSecurityResultData, new TypeReference<UserSecurityBO>(){}));
        String resultContent = postRequestMethod("1", "/user/getUserSecurity");
        BackVO backVO = JSONUtils.parse(resultContent, BackVO.class);
        Assert.assertEquals(true, backVO.getStatus());
    }

    @Test
    public void getDeptByUser() throws Exception{
        String deptResultData = "[{\"childs\":[{\"childs\":[],\"deptId\":103},{\"childs\":[],\"deptId\":104},{\"childs\":[],\"deptId\":105}],\"deptId\":89,\"deptType\":4,\"encryLevelCompany\":1,\"encryLevelDept\":1},{\"childs\":[{\"childs\":[],\"deptId\":27},{\"childs\":[],\"deptId\":28},{\"childs\":[],\"deptId\":29}],\"deptId\":7,\"deptType\":4,\"encryLevelCompany\":1,\"encryLevelDept\":1}]";
        Mockito.when(userMapperExt.getDeptByUser(any())).thenReturn(JSONUtils.parse(deptResultData, new TypeReference<List<DeptSecurityRepertoryBO>>(){}));
        String resultContent = postRequestMethod("{\"data\":98}", "/user/getDeptByUser");
        BackVO backVO = JSONUtils.parse(resultContent, BackVO.class);
        Assert.assertEquals(true, backVO.getStatus());
    }

    @Test
    public void getOrgInfoToCompany() throws Exception{
        String deptResultData = "[{\"deptId\":3,\"deptName\":\"市场营销体系中心\",\"deptType\":4,\"encryLevelDept\":2,\"lowestLevel\":false},{\"deptId\":1,\"deptName\":\"晶日\",\"deptType\":1,\"encryLevelDept\":2,\"lowestLevel\":false},{\"deptId\":5,\"deptName\":\"销售服务体系中心\",\"deptType\":4,\"encryLevelDept\":2,\"lowestLevel\":false},{\"deptId\":6,\"deptName\":\"制造体系中心\",\"deptType\":4,\"encryLevelDept\":2,\"lowestLevel\":false},{\"deptId\":7,\"deptName\":\"品质体系中心\",\"deptType\":4,\"encryLevelDept\":2,\"lowestLevel\":false}]";
        Mockito.when(middleUserDepartmentMapperExt.getOrganizationInfo(any())).thenReturn(JSONUtils.parse(deptResultData, new TypeReference<List<UserDeptAssociationBO>>(){}));
        String resultContent = postRequestMethod("{\"userId\":12,\"deptType\":1}", "/user/getOrgInfoToCompany");
        BackVO backVO = JSONUtils.parse(resultContent, BackVO.class);
        Assert.assertEquals(true, backVO.getStatus());
    }

    @Test
    public void getUserByDeptIds() throws Exception{
        String userResultData = "[{\"id\":9,\"userAccount\":\"admin\",\"userNo\":\"a001\",\"userName\":\"admin\",\"roleId\":1,\"phone\":\"13603060194\",\"position\":\"55\",\"encryLevelCompany\":1,\"viewEncryLevelPermitted\":false,\"deptId\":2,\"deviceCode\":\"121c83f76054995dcc9\"},{\"id\":74,\"userAccount\":\"xiaoran\",\"userNo\":\"222\",\"userName\":\"xiaoran\",\"roleId\":253,\"phone\":null,\"position\":\"qq\",\"encryLevelCompany\":2,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":\"191e35f7e03ff2bc2c9\"},{\"id\":75,\"userAccount\":\"chenzx\",\"userNo\":\"11112\",\"userName\":\"chenzx\",\"roleId\":1,\"phone\":\"18646163502\",\"position\":\"aaa\",\"encryLevelCompany\":1,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":\"170976fa8acc8aba32a\"},{\"id\":76,\"userAccount\":\"tonghao\",\"userNo\":\"34333\",\"userName\":\"tonghao\",\"roleId\":127,\"phone\":\"13924651051\",\"position\":\"aaa\",\"encryLevelCompany\":1,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":\"1a1018970aea673f188\"},{\"id\":80,\"userAccount\":\"zhangl1x2\",\"userNo\":\"2019002\",\"userName\":\"章立星\",\"roleId\":28,\"phone\":null,\"position\":\"测试\",\"encryLevelCompany\":2,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":null},{\"id\":84,\"userAccount\":\"jr-cy\",\"userNo\":\"20190041\",\"userName\":\"晶日陈烨1\",\"roleId\":28,\"phone\":null,\"position\":\"晶日员工2\",\"encryLevelCompany\":1,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":null},{\"id\":88,\"userAccount\":\"zhangxiao\",\"userNo\":\"zhangxiao\",\"userName\":\"zhangxiao\",\"roleId\":160,\"phone\":\"\",\"position\":\"zhangxiao\",\"encryLevelCompany\":1,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":null},{\"id\":89,\"userAccount\":\"zhuweiwei\",\"userNo\":\"weiwei\",\"userName\":\"weiwei\",\"roleId\":160,\"phone\":null,\"position\":\"weiwei\",\"encryLevelCompany\":1,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":null},{\"id\":73,\"userAccount\":\"testX\",\"userNo\":\"testX\",\"userName\":\"testX\",\"roleId\":7,\"phone\":\"18658135372\",\"position\":\"testX\",\"encryLevelCompany\":1,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":\"191e35f7e03ff2bc2c9\"},{\"id\":78,\"userAccount\":\"xuli\",\"userNo\":\"44445\",\"userName\":\"xuli\",\"roleId\":7,\"phone\":\"\",\"position\":\"aaa\",\"encryLevelCompany\":1,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":\"191e35f7e03ff2bc2c9\"},{\"id\":94,\"userAccount\":\"test007\",\"userNo\":\"test007\",\"userName\":\"test007\",\"roleId\":162,\"phone\":null,\"position\":\"java\",\"encryLevelCompany\":1,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":null},{\"id\":103,\"userAccount\":\"xiaomeng\",\"userNo\":\"54443\",\"userName\":\"xiaomeng\",\"roleId\":11,\"phone\":null,\"position\":\"deee\",\"encryLevelCompany\":1,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":null},{\"id\":107,\"userAccount\":\"weiwei00\",\"userNo\":\"321\",\"userName\":\"薇薇only缓存库查看\",\"roleId\":162,\"phone\":null,\"position\":\"水电费\",\"encryLevelCompany\":1,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":null},{\"id\":114,\"userAccount\":\"chakan\",\"userNo\":\"888\",\"userName\":\"查看-朱薇薇缓存库只查看权限\",\"roleId\":179,\"phone\":null,\"position\":\"薇薇\",\"encryLevelCompany\":1,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":null},{\"id\":59,\"userAccount\":\"test_zjt\",\"userNo\":\"zjt1111\",\"userName\":\"朱锦挺\",\"roleId\":23,\"phone\":null,\"position\":\"销售\",\"encryLevelCompany\":1,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":null},{\"id\":174,\"userAccount\":\"hoolink007\",\"userNo\":\"007\",\"userName\":\"hoolink0078\",\"roleId\":13,\"phone\":null,\"position\":\"sdfasdf\",\"encryLevelCompany\":2,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":null},{\"id\":182,\"userAccount\":\"1441\",\"userNo\":\"ghjk\",\"userName\":\"放烟花\",\"roleId\":7,\"phone\":null,\"position\":\"干活\",\"encryLevelCompany\":1,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":null},{\"id\":183,\"userAccount\":\"cyh\",\"userNo\":\"2019006\",\"userName\":\"陈亚红\",\"roleId\":28,\"phone\":null,\"position\":\"UI\",\"encryLevelCompany\":2,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":null},{\"id\":206,\"userAccount\":\"zhanglx05\",\"userNo\":\"zhanglx05\",\"userName\":\"章立星05\",\"roleId\":253,\"phone\":null,\"position\":\"测试\",\"encryLevelCompany\":1,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":\"140fe1da9ed099a732c\"},{\"id\":207,\"userAccount\":\"zhanglx06\",\"userNo\":\"zhanglx06\",\"userName\":\"章立星06\",\"roleId\":253,\"phone\":null,\"position\":\"测试\",\"encryLevelCompany\":1,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":\"191e35f7e03ff2bc2c9\"},{\"id\":265,\"userAccount\":\"tong\",\"userNo\":\"tong\",\"userName\":\"tong\",\"roleId\":313,\"phone\":null,\"position\":\"java\",\"encryLevelCompany\":5,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":null},{\"id\":271,\"userAccount\":\"zxx\",\"userNo\":\"zxx\",\"userName\":\"赵欣翔\",\"roleId\":253,\"phone\":null,\"position\":\"总监\",\"encryLevelCompany\":2,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":null},{\"id\":278,\"userAccount\":\"角色001\",\"userNo\":\"1001\",\"userName\":\"角色001\",\"roleId\":0,\"phone\":null,\"position\":\"角色001\",\"encryLevelCompany\":2,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":null},{\"id\":266,\"userAccount\":\"chen\",\"userNo\":\"合格可见\",\"userName\":\"chen\",\"roleId\":347,\"phone\":null,\"position\":\"chen\",\"encryLevelCompany\":1,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":null},{\"id\":299,\"userAccount\":\"zjttest\",\"userNo\":\"1111\",\"userName\":\"zjt1111\",\"roleId\":361,\"phone\":null,\"position\":\"销售\",\"encryLevelCompany\":1,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":null},{\"id\":312,\"userAccount\":\"yh001\",\"userNo\":\"yh001\",\"userName\":\"yh001\",\"roleId\":374,\"phone\":null,\"position\":\"yh001\",\"encryLevelCompany\":4,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":null},{\"id\":336,\"userAccount\":\"test_boss\",\"userNo\":\"JR0001\",\"userName\":\"程总\",\"roleId\":401,\"phone\":null,\"position\":\"boss\",\"encryLevelCompany\":1,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":null},{\"id\":345,\"userAccount\":\"ceshidl\",\"userNo\":\"99922\",\"userName\":\"刘亭亭\",\"roleId\":171,\"phone\":\"13003681019\",\"position\":\"测试登录\",\"encryLevelCompany\":1,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":null},{\"id\":358,\"userAccount\":\"yzbg\",\"userNo\":\"000\",\"userName\":\"刘亭亭\",\"roleId\":171,\"phone\":null,\"position\":\"验证Bug\",\"encryLevelCompany\":1,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":null},{\"id\":359,\"userAccount\":\"ceshitg\",\"userNo\":\"999777\",\"userName\":\"刘亭亭\",\"roleId\":171,\"phone\":null,\"position\":\"测试跳过\",\"encryLevelCompany\":1,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":null},{\"id\":360,\"userAccount\":\"ceshitg1\",\"userNo\":\"99988\",\"userName\":\"刘亭亭\",\"roleId\":171,\"phone\":null,\"position\":\"测试跳过\",\"encryLevelCompany\":1,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":null},{\"id\":551,\"userAccount\":\"t001\",\"userNo\":\"t001\",\"userName\":\"t001\",\"roleId\":9,\"phone\":\"15180650806\",\"position\":\"t001\",\"encryLevelCompany\":1,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":null},{\"id\":557,\"userAccount\":\"ltt\",\"userNo\":\"10001\",\"userName\":\"刘亭亭\",\"roleId\":171,\"phone\":\"13003681009\",\"position\":\"测试\",\"encryLevelCompany\":1,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":null},{\"id\":79,\"userAccount\":\"liyus\",\"userNo\":\"66666\",\"userName\":\"liyus\",\"roleId\":168,\"phone\":null,\"position\":\"111\",\"encryLevelCompany\":1,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":null},{\"id\":372,\"userAccount\":\"lishuai1\",\"userNo\":\"556633\",\"userName\":\"李帅\",\"roleId\":169,\"phone\":null,\"position\":\"部门\",\"encryLevelCompany\":1,\"viewEncryLevelPermitted\":null,\"deptId\":2,\"deviceCode\":null}]";
        Mockito.when(userMapperExt.selectAllByDeptIds(any())).thenReturn(JSONUtils.parse(userResultData, new TypeReference<List<SimpleDeptUserBO>>(){}));
        String resultContent = postRequestMethod("[2]", "/user/getUserByDeptIds");
        BackVO backVO = JSONUtils.parse(resultContent, BackVO.class);
        Assert.assertEquals(true, backVO.getStatus());
    }

    @Test
    public void getNameByIds() throws Exception{
        String userResultData = "[{\"id\":12,\"userNo\":\"a004\",\"name\":\"test3\",\"position\":\"xxxxxxxxxx\",\"roleId\":1,\"roleName\":null,\"company\":null,\"phone\":\"13003681011\",\"userAccount\":\"test3\",\"encryLevelCompany\":0,\"encryLevelCompanyName\":null,\"status\":true,\"statusDesc\":null,\"sex\":true,\"sexDesc\":null,\"created\":1556444743803,\"lastTime\":1561364580867,\"userDeptPairList\":null}]";
        Mockito.when(userMapper.selectByExample(any())).thenReturn(JSONUtils.parse(userResultData, new TypeReference<List<User>>(){}));
        String resultContent = postRequestMethod("[12]", "/user/getNameByIds");
        BackVO backVO = JSONUtils.parse(resultContent, BackVO.class);
        Assert.assertEquals(true, backVO.getStatus());
    }

    @Test
    public void getCompanyById() throws Exception{
        String mddleUserData = "[{\"deptId\":3,\"diffDeptGroup\":\"8737fe1c09294b6bbde4a4cf022836f061640271\",\"encryLevelDept\":2,\"id\":478,\"lowestLevel\":false,\"userId\":12},{\"deptId\":1,\"diffDeptGroup\":\"8737fe1c09294b6bbde4a4cf022836f061640271\",\"encryLevelDept\":2,\"id\":479,\"lowestLevel\":false,\"userId\":12},{\"deptId\":5,\"diffDeptGroup\":\"8737fe1c09294b6bbde4a4cf022836f061640271\",\"encryLevelDept\":2,\"id\":480,\"lowestLevel\":false,\"userId\":12},{\"deptId\":6,\"diffDeptGroup\":\"8737fe1c09294b6bbde4a4cf022836f061640271\",\"encryLevelDept\":2,\"id\":481,\"lowestLevel\":false,\"userId\":12},{\"deptId\":7,\"diffDeptGroup\":\"8737fe1c09294b6bbde4a4cf022836f061640271\",\"encryLevelDept\":2,\"id\":482,\"lowestLevel\":false,\"userId\":12}]";
        Mockito.when(middleUserDepartmentMapper.selectByExample(any())).thenReturn(JSONUtils.parse(mddleUserData, new TypeReference<List<MiddleUserDepartment>>(){}));
        String deptResultData = "[{\"created\":1,\"creator\":1,\"deptType\":1,\"enabled\":true,\"id\":1,\"name\":\"晶日\",\"parentId\":0,\"parentIdCode\":\"0_1_\",\"updated\":1,\"updator\":1},{\"created\":1,\"creator\":1,\"deptType\":4,\"enabled\":true,\"id\":3,\"name\":\"市场营销体系中心\",\"parentId\":1,\"parentIdCode\":\"0_1_3_\",\"updated\":1,\"updator\":1},{\"created\":1,\"creator\":1,\"deptType\":4,\"enabled\":true,\"id\":5,\"name\":\"销售服务体系中心\",\"parentId\":1,\"parentIdCode\":\"0_1_5_\",\"updated\":1,\"updator\":1},{\"created\":1,\"creator\":1,\"deptType\":4,\"enabled\":true,\"id\":6,\"name\":\"制造体系中心\",\"parentId\":1,\"parentIdCode\":\"0_1_6_\",\"updated\":1,\"updator\":1},{\"created\":1,\"creator\":1,\"deptType\":4,\"enabled\":true,\"id\":7,\"name\":\"品质体系中心\",\"parentId\":1,\"parentIdCode\":\"0_1_7_\",\"updated\":1,\"updator\":1}]";
        Mockito.when(manageDepartmentMapper.selectByExample(any())).thenReturn(JSONUtils.parse(deptResultData, new TypeReference<List<ManageDepartment>>(){}));
        String resultContent = postRequestMethod("12", "/user/getCompanyById");
        BackVO backVO = JSONUtils.parse(resultContent, BackVO.class);
        Assert.assertEquals(true, backVO.getStatus());
    }
}
