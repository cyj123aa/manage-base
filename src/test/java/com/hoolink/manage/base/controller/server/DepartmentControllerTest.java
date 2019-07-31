package com.hoolink.manage.base.controller.server;

import com.alibaba.fastjson.TypeReference;
import com.hoolink.manage.base.controller.TestController;
import com.hoolink.manage.base.dao.mapper.ManageDepartmentMapper;
import com.hoolink.manage.base.dao.mapper.ext.ManageDepartmentMapperExt;
import com.hoolink.manage.base.dao.model.ManageDepartment;
import com.hoolink.manage.base.service.UserService;
import com.hoolink.sdk.bo.BackBO;
import com.hoolink.sdk.bo.manager.ManageDepartmentTreeBO;
import com.hoolink.sdk.bo.manager.OrganizationDeptBO;
import com.hoolink.sdk.bo.manager.ReadFileOrgInfoBO;
import com.hoolink.sdk.bo.manager.UserDeptAssociationBO;
import com.hoolink.sdk.utils.JSONUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author chenzhixiong
 * @date 2019/7/24 17:43
 */
public class DepartmentControllerTest extends TestController {
    private Logger logger= LoggerFactory.getLogger(DepartmentControllerTest.class);

    @Autowired
    private DepartmentController departmentController;


    @MockBean
    private ManageDepartmentMapper manageDepartmentMapper;
    @MockBean
    private ManageDepartmentMapperExt manageDepartmentMapperExt;
    @MockBean
    private UserService userService;




    @Before
    public void setUp() {
        super.setUp();
        //控制类通过@Autowired自动注入进来，以此构建mockMvc对象
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();
    }

    @Test
    public void getOrganizationList() throws Exception {
        String backBO = "[{\"children\":[{\"children\":[{\"children\":[{\"children\":[],\"deptType\":3,\"expand\":true,\"key\":114,\"parentId\":92,\"title\":\"预研部\"},{\"children\":[],\"deptType\":3,\"expand\":true,\"key\":115,\"parentId\":92,\"title\":\"设计部\"},{\"children\":[],\"deptType\":3,\"expand\":true,\"key\":116,\"parentId\":92,\"title\":\"市场规划部\"},{\"children\":[],\"deptType\":3,\"expand\":true,\"key\":117,\"parentId\":92,\"title\":\"品牌策划部\"},{\"children\":[],\"deptType\":3,\"expand\":true,\"key\":118,\"parentId\":92,\"title\":\"产品策划部\"}],\"deptType\":2,\"expand\":true,\"key\":92,\"parentId\":87,\"title\":\"战略策划部\"},{\"children\":[],\"deptType\":2,\"expand\":true,\"key\":93,\"parentId\":87,\"title\":\"营销支持部\"},{\"children\":[],\"deptType\":2,\"expand\":true,\"key\":94,\"parentId\":87,\"title\":\"营销监督审核服务部\"}],\"deptType\":4,\"expand\":true,\"key\":87,\"parentId\":2,\"title\":\"市场营销体系中心\"},{\"children\":[{\"children\":[],\"deptType\":2,\"expand\":true,\"key\":95,\"parentId\":88,\"title\":\"智慧交通销售部\"},{\"children\":[],\"deptType\":2,\"expand\":true,\"key\":96,\"parentId\":88,\"title\":\"智慧小镇销售部\"},{\"children\":[],\"deptType\":2,\"expand\":true,\"key\":97,\"parentId\":88,\"title\":\"智慧校园销售部\"},{\"children\":[],\"deptType\":2,\"expand\":true,\"key\":98,\"parentId\":88,\"title\":\"智慧别墅销售部\"},{\"children\":[],\"deptType\":2,\"expand\":true,\"key\":99,\"parentId\":88,\"title\":\"智慧社区销售部\"},{\"children\":[],\"deptType\":2,\"expand\":true,\"key\":100,\"parentId\":88,\"title\":\"智慧景区销售部\"},{\"children\":[],\"deptType\":2,\"expand\":true,\"key\":101,\"parentId\":88,\"title\":\"智慧工厂销售部\"},{\"children\":[],\"deptType\":2,\"expand\":true,\"key\":102,\"parentId\":88,\"title\":\"智慧公园销售部\"}],\"deptType\":4,\"expand\":true,\"key\":88,\"parentId\":2,\"title\":\"销售服务体系中心\"},{\"children\":[{\"children\":[],\"deptType\":2,\"expand\":true,\"key\":103,\"parentId\":89,\"title\":\"智能软件开发部\"},{\"children\":[],\"deptType\":2,\"expand\":true,\"key\":104,\"parentId\":89,\"title\":\"智能硬件开发部\"},{\"children\":[],\"deptType\":2,\"expand\":true,\"key\":105,\"parentId\":89,\"title\":\"智能结构开发部\"}],\"deptType\":4,\"expand\":true,\"key\":89,\"parentId\":2,\"title\":\"研发体系中心\"},{\"children\":[{\"children\":[],\"deptType\":2,\"expand\":true,\"key\":106,\"parentId\":90,\"title\":\"采购部\"},{\"children\":[],\"deptType\":2,\"expand\":true,\"key\":107,\"parentId\":90,\"title\":\"行政部\"},{\"children\":[],\"deptType\":2,\"expand\":true,\"key\":108,\"parentId\":90,\"title\":\"人力资源部\"}],\"deptType\":4,\"expand\":true,\"key\":90,\"parentId\":2,\"title\":\"行政体系中心\"},{\"children\":[{\"children\":[],\"deptType\":2,\"expand\":true,\"key\":109,\"parentId\":91,\"title\":\"高端技术预研部\"},{\"children\":[],\"deptType\":2,\"expand\":true,\"key\":110,\"parentId\":91,\"title\":\"IT数据信息安全管理部\"},{\"children\":[],\"deptType\":2,\"expand\":true,\"key\":111,\"parentId\":91,\"title\":\"审计部\"},{\"children\":[],\"deptType\":2,\"expand\":true,\"key\":112,\"parentId\":91,\"title\":\"政企部\"},{\"children\":[],\"deptType\":2,\"expand\":true,\"key\":113,\"parentId\":91,\"title\":\"财务部\"}],\"deptType\":4,\"expand\":true,\"key\":91,\"parentId\":2,\"title\":\"总经办\"}],\"deptType\":1,\"expand\":true,\"key\":2,\"parentId\":0,\"title\":\"互灵\"}]";
        when(manageDepartmentMapperExt.getOrganizationList(any())).thenReturn(JSONUtils.toList(backBO, ManageDepartmentTreeBO.class));
        String param = "{\n" + "  \"idList\": [\n" + "    2\n" + "  ],\n" + "  \"flag\": false\n" + "}";
        String contentAsString = postRequestMethod(param, "/department/getOrganizationList");
        BackBO listBackBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO>(){});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true, listBackBO.getStatus());
    }

    @Test
    public void getOrgListByIdList() throws Exception {
        String backBO = "[{\"created\":1,\"creator\":1,\"deptType\":2,\"enabled\":true,\"id\":25,\"name\":\"灯体制造部\",\"parentId\":6,\"parentIdCode\":\"0_1_6_25_\",\"updated\":1,\"updator\":1}]";
        when(manageDepartmentMapper.selectByExample(any())).thenReturn(JSONUtils.toList(backBO, ManageDepartment.class));
        String param = "[\n" + " 25\n" + "]";
        String contentAsString = postRequestMethod(param, "/department/getOrgListByIdList");
        BackBO listBackBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO>(){});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true, listBackBO.getStatus());
    }

    @Test
    public void getOrgInfoList() throws Exception {
        String backBO = "[{\"deptType\":2,\"expand\":true,\"key\":25,\"parentId\":6,\"title\":\"灯体制造部\"}]";
        when(manageDepartmentMapperExt.getOrgInfoList(any())).thenReturn(JSONUtils.toList(backBO, ManageDepartmentTreeBO.class));
        backBO = "[{\"deptType\":1,\"expand\":true,\"key\":1,\"parentId\":0,\"title\":\"晶日\"},{\"deptType\":1,\"expand\":true,\"key\":2,\"parentId\":0,\"title\":\"互灵\"},{\"deptType\":4,\"expand\":true,\"key\":3,\"parentId\":1,\"title\":\"市场营销体系中心\"},{\"deptType\":4,\"expand\":true,\"key\":4,\"parentId\":1,\"title\":\"研发体系中心\"},{\"deptType\":4,\"expand\":true,\"key\":5,\"parentId\":1,\"title\":\"销售服务体系中心\"},{\"deptType\":4,\"expand\":true,\"key\":6,\"parentId\":1,\"title\":\"制造体系中心\"},{\"deptType\":4,\"expand\":true,\"key\":7,\"parentId\":1,\"title\":\"品质体系中心\"},{\"deptType\":4,\"expand\":true,\"key\":8,\"parentId\":1,\"title\":\"成本管理体系中心\"},{\"deptType\":4,\"expand\":true,\"key\":9,\"parentId\":1,\"title\":\"财务体系中心\"},{\"deptType\":4,\"expand\":true,\"key\":10,\"parentId\":1,\"title\":\"企业运营保障体系中心\"},{\"deptType\":4,\"expand\":true,\"key\":11,\"parentId\":1,\"title\":\"信息文控管理体系中心\"},{\"deptType\":2,\"expand\":true,\"key\":12,\"parentId\":3,\"title\":\"预研部\"},{\"deptType\":2,\"expand\":true,\"key\":13,\"parentId\":3,\"title\":\"市场战略策划部\"},{\"deptType\":2,\"expand\":true,\"key\":14,\"parentId\":3,\"title\":\"市场营销服务部\"},{\"deptType\":2,\"expand\":true,\"key\":15,\"parentId\":4,\"title\":\"光电技术部\"},{\"deptType\":2,\"expand\":true,\"key\":16,\"parentId\":4,\"title\":\"销售技术服务一部\"},{\"deptType\":2,\"expand\":true,\"key\":17,\"parentId\":4,\"title\":\"销售技术服务二部\"},{\"deptType\":2,\"expand\":true,\"key\":18,\"parentId\":5,\"title\":\"国内销售部\"},{\"deptType\":2,\"expand\":true,\"key\":19,\"parentId\":5,\"title\":\"国际销售部\"},{\"deptType\":2,\"expand\":true,\"key\":20,\"parentId\":5,\"title\":\"OEM&ODM部\"},{\"deptType\":2,\"expand\":true,\"key\":21,\"parentId\":5,\"title\":\"招投标部\"},{\"deptType\":2,\"expand\":true,\"key\":22,\"parentId\":6,\"title\":\"计划部\"},{\"deptType\":2,\"expand\":true,\"key\":23,\"parentId\":6,\"title\":\"仓储物流部\"},{\"deptType\":2,\"expand\":true,\"key\":24,\"parentId\":6,\"title\":\"灯具制造部\"},{\"deptType\":2,\"expand\":true,\"key\":25,\"parentId\":6,\"title\":\"灯体制造部\"},{\"deptType\":2,\"expand\":true,\"key\":26,\"parentId\":6,\"title\":\"特种制造部\"},{\"deptType\":2,\"expand\":true,\"key\":27,\"parentId\":7,\"title\":\"品质部\"},{\"deptType\":2,\"expand\":true,\"key\":28,\"parentId\":7,\"title\":\"品保部\"},{\"deptType\":2,\"expand\":true,\"key\":29,\"parentId\":7,\"title\":\"检测中心\"},{\"deptType\":2,\"expand\":true,\"key\":30,\"parentId\":8,\"title\":\"采购部\"},{\"deptType\":2,\"expand\":true,\"key\":31,\"parentId\":8,\"title\":\"产品核价部\"},{\"deptType\":2,\"expand\":true,\"key\":32,\"parentId\":9,\"title\":\"财务部\"},{\"deptType\":2,\"expand\":true,\"key\":33,\"parentId\":10,\"title\":\"董秘办\"},{\"deptType\":2,\"expand\":true,\"key\":34,\"parentId\":10,\"title\":\"总经办\"},{\"deptType\":2,\"expand\":true,\"key\":35,\"parentId\":10,\"title\":\"行政部\"},{\"deptType\":2,\"expand\":true,\"key\":36,\"parentId\":10,\"title\":\"人资部\"},{\"deptType\":2,\"expand\":true,\"key\":37,\"parentId\":10,\"title\":\"设备部\"},{\"deptType\":2,\"expand\":true,\"key\":38,\"parentId\":10,\"title\":\"安环部\"},{\"deptType\":2,\"expand\":true,\"key\":39,\"parentId\":11,\"title\":\"信息综合管理部\"},{\"deptType\":2,\"expand\":true,\"key\":40,\"parentId\":11,\"title\":\"云平台管理部\"},{\"deptType\":2,\"expand\":true,\"key\":41,\"parentId\":11,\"title\":\"图文数据管理部\"},{\"deptType\":3,\"expand\":true,\"key\":42,\"parentId\":12,\"title\":\"秘书室\"},{\"deptType\":3,\"expand\":true,\"key\":43,\"parentId\":13,\"title\":\"市场调研组\"},{\"deptType\":3,\"expand\":true,\"key\":44,\"parentId\":13,\"title\":\"工业设计组\"},{\"deptType\":3,\"expand\":true,\"key\":45,\"parentId\":13,\"title\":\"品牌策划组\"},{\"deptType\":3,\"expand\":true,\"key\":46,\"parentId\":14,\"title\":\"销售策划组\"},{\"deptType\":3,\"expand\":true,\"key\":47,\"parentId\":14,\"title\":\"市场拓展组\"},{\"deptType\":3,\"expand\":true,\"key\":48,\"parentId\":15,\"title\":\"标准组\"},{\"deptType\":3,\"expand\":true,\"key\":49,\"parentId\":15,\"title\":\"光学组\"},{\"deptType\":3,\"expand\":true,\"key\":50,\"parentId\":15,\"title\":\"硬件组\"},{\"deptType\":3,\"expand\":true,\"key\":51,\"parentId\":15,\"title\":\"软件组\"},{\"deptType\":3,\"expand\":true,\"key\":52,\"parentId\":15,\"title\":\"结构组\"},{\"deptType\":3,\"expand\":true,\"key\":53,\"parentId\":16,\"title\":\"光学组\"},{\"deptType\":3,\"expand\":true,\"key\":54,\"parentId\":16,\"title\":\"硬件组\"},{\"deptType\":3,\"expand\":true,\"key\":55,\"parentId\":16,\"title\":\"软件组\"},{\"deptType\":3,\"expand\":true,\"key\":56,\"parentId\":16,\"title\":\"结构组\"},{\"deptType\":3,\"expand\":true,\"key\":57,\"parentId\":17,\"title\":\"光学组\"},{\"deptType\":3,\"expand\":true,\"key\":58,\"parentId\":17,\"title\":\"硬件组\"},{\"deptType\":3,\"expand\":true,\"key\":59,\"parentId\":17,\"title\":\"软件组\"},{\"deptType\":3,\"expand\":true,\"key\":60,\"parentId\":17,\"title\":\"结构组\"},{\"deptType\":3,\"expand\":true,\"key\":61,\"parentId\":18,\"title\":\"东北区\"},{\"deptType\":3,\"expand\":true,\"key\":62,\"parentId\":18,\"title\":\"华北区\"},{\"deptType\":3,\"expand\":true,\"key\":63,\"parentId\":18,\"title\":\"华中区\"},{\"deptType\":3,\"expand\":true,\"key\":64,\"parentId\":18,\"title\":\"华南区\"},{\"deptType\":3,\"expand\":true,\"key\":65,\"parentId\":18,\"title\":\"华东区\"},{\"deptType\":3,\"expand\":true,\"key\":66,\"parentId\":18,\"title\":\"西南区\"},{\"deptType\":3,\"expand\":true,\"key\":67,\"parentId\":18,\"title\":\"西北区\"},{\"deptType\":3,\"expand\":true,\"key\":68,\"parentId\":19,\"title\":\"欧洲区\"},{\"deptType\":3,\"expand\":true,\"key\":69,\"parentId\":19,\"title\":\"美洲区\"},{\"deptType\":3,\"expand\":true,\"key\":70,\"parentId\":19,\"title\":\"亚洲区\"},{\"deptType\":3,\"expand\":true,\"key\":71,\"parentId\":19,\"title\":\"非洲区\"},{\"deptType\":3,\"expand\":true,\"key\":72,\"parentId\":24,\"title\":\"SMT\"},{\"deptType\":3,\"expand\":true,\"key\":73,\"parentId\":24,\"title\":\"组装\"},{\"deptType\":3,\"expand\":true,\"key\":74,\"parentId\":25,\"title\":\"压铸\"},{\"deptType\":3,\"expand\":true,\"key\":75,\"parentId\":25,\"title\":\"打磨\"},{\"deptType\":3,\"expand\":true,\"key\":76,\"parentId\":25,\"title\":\"精加工\"},{\"deptType\":3,\"expand\":true,\"key\":77,\"parentId\":25,\"title\":\"喷塑\"},{\"deptType\":3,\"expand\":true,\"key\":78,\"parentId\":30,\"title\":\"电子组\"},{\"deptType\":3,\"expand\":true,\"key\":79,\"parentId\":30,\"title\":\"五金组\"},{\"deptType\":3,\"expand\":true,\"key\":80,\"parentId\":30,\"title\":\"模具组\"},{\"deptType\":3,\"expand\":true,\"key\":81,\"parentId\":33,\"title\":\"股东大会\"},{\"deptType\":3,\"expand\":true,\"key\":82,\"parentId\":33,\"title\":\"董事会\"},{\"deptType\":3,\"expand\":true,\"key\":83,\"parentId\":33,\"title\":\"监事会\"},{\"deptType\":3,\"expand\":true,\"key\":84,\"parentId\":34,\"title\":\"管理组\"},{\"deptType\":3,\"expand\":true,\"key\":85,\"parentId\":34,\"title\":\"知识产权组\"},{\"deptType\":3,\"expand\":true,\"key\":86,\"parentId\":34,\"title\":\"法务组\"},{\"deptType\":4,\"expand\":true,\"key\":87,\"parentId\":2,\"title\":\"市场营销体系中心\"},{\"deptType\":4,\"expand\":true,\"key\":88,\"parentId\":2,\"title\":\"销售服务体系中心\"},{\"deptType\":4,\"expand\":true,\"key\":89,\"parentId\":2,\"title\":\"研发体系中心\"},{\"deptType\":4,\"expand\":true,\"key\":90,\"parentId\":2,\"title\":\"行政体系中心\"},{\"deptType\":4,\"expand\":true,\"key\":91,\"parentId\":2,\"title\":\"总经办\"},{\"deptType\":2,\"expand\":true,\"key\":92,\"parentId\":87,\"title\":\"战略策划部\"},{\"deptType\":2,\"expand\":true,\"key\":93,\"parentId\":87,\"title\":\"营销支持部\"},{\"deptType\":2,\"expand\":true,\"key\":94,\"parentId\":87,\"title\":\"营销监督审核服务部\"},{\"deptType\":2,\"expand\":true,\"key\":95,\"parentId\":88,\"title\":\"智慧交通销售部\"},{\"deptType\":2,\"expand\":true,\"key\":96,\"parentId\":88,\"title\":\"智慧小镇销售部\"},{\"deptType\":2,\"expand\":true,\"key\":97,\"parentId\":88,\"title\":\"智慧校园销售部\"},{\"deptType\":2,\"expand\":true,\"key\":98,\"parentId\":88,\"title\":\"智慧别墅销售部\"},{\"deptType\":2,\"expand\":true,\"key\":99,\"parentId\":88,\"title\":\"智慧社区销售部\"},{\"deptType\":2,\"expand\":true,\"key\":100,\"parentId\":88,\"title\":\"智慧景区销售部\"},{\"deptType\":2,\"expand\":true,\"key\":101,\"parentId\":88,\"title\":\"智慧工厂销售部\"},{\"deptType\":2,\"expand\":true,\"key\":102,\"parentId\":88,\"title\":\"智慧公园销售部\"},{\"deptType\":2,\"expand\":true,\"key\":103,\"parentId\":89,\"title\":\"智能软件开发部\"},{\"deptType\":2,\"expand\":true,\"key\":104,\"parentId\":89,\"title\":\"智能硬件开发部\"},{\"deptType\":2,\"expand\":true,\"key\":105,\"parentId\":89,\"title\":\"智能结构开发部\"},{\"deptType\":2,\"expand\":true,\"key\":106,\"parentId\":90,\"title\":\"采购部\"},{\"deptType\":2,\"expand\":true,\"key\":107,\"parentId\":90,\"title\":\"行政部\"},{\"deptType\":2,\"expand\":true,\"key\":108,\"parentId\":90,\"title\":\"人力资源部\"},{\"deptType\":2,\"expand\":true,\"key\":109,\"parentId\":91,\"title\":\"高端技术预研部\"},{\"deptType\":2,\"expand\":true,\"key\":110,\"parentId\":91,\"title\":\"IT数据信息安全管理部\"},{\"deptType\":2,\"expand\":true,\"key\":111,\"parentId\":91,\"title\":\"审计部\"},{\"deptType\":2,\"expand\":true,\"key\":112,\"parentId\":91,\"title\":\"政企部\"},{\"deptType\":2,\"expand\":true,\"key\":113,\"parentId\":91,\"title\":\"财务部\"},{\"deptType\":3,\"expand\":true,\"key\":114,\"parentId\":92,\"title\":\"预研部\"},{\"deptType\":3,\"expand\":true,\"key\":115,\"parentId\":92,\"title\":\"设计部\"},{\"deptType\":3,\"expand\":true,\"key\":116,\"parentId\":92,\"title\":\"市场规划部\"},{\"deptType\":3,\"expand\":true,\"key\":117,\"parentId\":92,\"title\":\"品牌策划部\"},{\"deptType\":3,\"expand\":true,\"key\":118,\"parentId\":92,\"title\":\"产品策划部\"}]";
        when(manageDepartmentMapperExt.getAllOrgInfoList()).thenReturn(JSONUtils.toList(backBO, ManageDepartmentTreeBO.class));

        String param = "{\n" + "  \"deptId\": 25\n" + "}";
        String contentAsString = postRequestMethod(param, "/department/getOrgInfoList");
        BackBO listBackBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO>(){});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true, listBackBO.getStatus());
    }

    @Test
    public void getOrgInfoListToDept() throws Exception {
        String backBO = "[{\"deptId\":1,\"deptName\":\"晶日\",\"deptType\":1,\"encryLevelDept\":1,\"lowestLevel\":true},{\"deptId\":2,\"deptName\":\"互灵\",\"deptType\":1,\"encryLevelDept\":1,\"lowestLevel\":true}]";
        when(userService.getOrganizationInfoToDept(any())).thenReturn(JSONUtils.toList(backBO, UserDeptAssociationBO.class));
        backBO = "[{\"deptType\":1,\"key\":1,\"parentId\":0,\"title\":\"晶日\"},{\"deptType\":1,\"key\":2,\"parentId\":0,\"title\":\"互灵\"},{\"deptType\":4,\"key\":3,\"parentId\":1,\"title\":\"市场营销体系中心\"},{\"deptType\":4,\"key\":4,\"parentId\":1,\"title\":\"研发体系中心\"},{\"deptType\":4,\"key\":5,\"parentId\":1,\"title\":\"销售服务体系中心\"},{\"deptType\":4,\"key\":6,\"parentId\":1,\"title\":\"制造体系中心\"},{\"deptType\":4,\"key\":7,\"parentId\":1,\"title\":\"品质体系中心\"},{\"deptType\":4,\"key\":8,\"parentId\":1,\"title\":\"成本管理体系中心\"},{\"deptType\":4,\"key\":9,\"parentId\":1,\"title\":\"财务体系中心\"},{\"deptType\":4,\"key\":10,\"parentId\":1,\"title\":\"企业运营保障体系中心\"},{\"deptType\":4,\"key\":11,\"parentId\":1,\"title\":\"信息文控管理体系中心\"},{\"deptType\":2,\"key\":12,\"parentId\":3,\"title\":\"预研部\"},{\"deptType\":2,\"key\":13,\"parentId\":3,\"title\":\"市场战略策划部\"},{\"deptType\":2,\"key\":14,\"parentId\":3,\"title\":\"市场营销服务部\"},{\"deptType\":2,\"key\":15,\"parentId\":4,\"title\":\"光电技术部\"},{\"deptType\":2,\"key\":16,\"parentId\":4,\"title\":\"销售技术服务一部\"},{\"deptType\":2,\"key\":17,\"parentId\":4,\"title\":\"销售技术服务二部\"},{\"deptType\":2,\"key\":18,\"parentId\":5,\"title\":\"国内销售部\"},{\"deptType\":2,\"key\":19,\"parentId\":5,\"title\":\"国际销售部\"},{\"deptType\":2,\"key\":20,\"parentId\":5,\"title\":\"OEM&ODM部\"},{\"deptType\":2,\"key\":21,\"parentId\":5,\"title\":\"招投标部\"},{\"deptType\":2,\"key\":22,\"parentId\":6,\"title\":\"计划部\"},{\"deptType\":2,\"key\":23,\"parentId\":6,\"title\":\"仓储物流部\"},{\"deptType\":2,\"key\":24,\"parentId\":6,\"title\":\"灯具制造部\"},{\"deptType\":2,\"key\":25,\"parentId\":6,\"title\":\"灯体制造部\"},{\"deptType\":2,\"key\":26,\"parentId\":6,\"title\":\"特种制造部\"},{\"deptType\":2,\"key\":27,\"parentId\":7,\"title\":\"品质部\"},{\"deptType\":2,\"key\":28,\"parentId\":7,\"title\":\"品保部\"},{\"deptType\":2,\"key\":29,\"parentId\":7,\"title\":\"检测中心\"},{\"deptType\":2,\"key\":30,\"parentId\":8,\"title\":\"采购部\"},{\"deptType\":2,\"key\":31,\"parentId\":8,\"title\":\"产品核价部\"},{\"deptType\":2,\"key\":32,\"parentId\":9,\"title\":\"财务部\"},{\"deptType\":2,\"key\":33,\"parentId\":10,\"title\":\"董秘办\"},{\"deptType\":2,\"key\":34,\"parentId\":10,\"title\":\"总经办\"},{\"deptType\":2,\"key\":35,\"parentId\":10,\"title\":\"行政部\"},{\"deptType\":2,\"key\":36,\"parentId\":10,\"title\":\"人资部\"},{\"deptType\":2,\"key\":37,\"parentId\":10,\"title\":\"设备部\"},{\"deptType\":2,\"key\":38,\"parentId\":10,\"title\":\"安环部\"},{\"deptType\":2,\"key\":39,\"parentId\":11,\"title\":\"信息综合管理部\"},{\"deptType\":2,\"key\":40,\"parentId\":11,\"title\":\"云平台管理部\"},{\"deptType\":2,\"key\":41,\"parentId\":11,\"title\":\"图文数据管理部\"},{\"deptType\":3,\"key\":42,\"parentId\":12,\"title\":\"秘书室\"},{\"deptType\":3,\"key\":43,\"parentId\":13,\"title\":\"市场调研组\"},{\"deptType\":3,\"key\":44,\"parentId\":13,\"title\":\"工业设计组\"},{\"deptType\":3,\"key\":45,\"parentId\":13,\"title\":\"品牌策划组\"},{\"deptType\":3,\"key\":46,\"parentId\":14,\"title\":\"销售策划组\"},{\"deptType\":3,\"key\":47,\"parentId\":14,\"title\":\"市场拓展组\"},{\"deptType\":3,\"key\":48,\"parentId\":15,\"title\":\"标准组\"},{\"deptType\":3,\"key\":49,\"parentId\":15,\"title\":\"光学组\"},{\"deptType\":3,\"key\":50,\"parentId\":15,\"title\":\"硬件组\"},{\"deptType\":3,\"key\":51,\"parentId\":15,\"title\":\"软件组\"},{\"deptType\":3,\"key\":52,\"parentId\":15,\"title\":\"结构组\"},{\"deptType\":3,\"key\":53,\"parentId\":16,\"title\":\"光学组\"},{\"deptType\":3,\"key\":54,\"parentId\":16,\"title\":\"硬件组\"},{\"deptType\":3,\"key\":55,\"parentId\":16,\"title\":\"软件组\"},{\"deptType\":3,\"key\":56,\"parentId\":16,\"title\":\"结构组\"},{\"deptType\":3,\"key\":57,\"parentId\":17,\"title\":\"光学组\"},{\"deptType\":3,\"key\":58,\"parentId\":17,\"title\":\"硬件组\"},{\"deptType\":3,\"key\":59,\"parentId\":17,\"title\":\"软件组\"},{\"deptType\":3,\"key\":60,\"parentId\":17,\"title\":\"结构组\"},{\"deptType\":3,\"key\":61,\"parentId\":18,\"title\":\"东北区\"},{\"deptType\":3,\"key\":62,\"parentId\":18,\"title\":\"华北区\"},{\"deptType\":3,\"key\":63,\"parentId\":18,\"title\":\"华中区\"},{\"deptType\":3,\"key\":64,\"parentId\":18,\"title\":\"华南区\"},{\"deptType\":3,\"key\":65,\"parentId\":18,\"title\":\"华东区\"},{\"deptType\":3,\"key\":66,\"parentId\":18,\"title\":\"西南区\"},{\"deptType\":3,\"key\":67,\"parentId\":18,\"title\":\"西北区\"},{\"deptType\":3,\"key\":68,\"parentId\":19,\"title\":\"欧洲区\"},{\"deptType\":3,\"key\":69,\"parentId\":19,\"title\":\"美洲区\"},{\"deptType\":3,\"key\":70,\"parentId\":19,\"title\":\"亚洲区\"},{\"deptType\":3,\"key\":71,\"parentId\":19,\"title\":\"非洲区\"},{\"deptType\":3,\"key\":72,\"parentId\":24,\"title\":\"SMT\"},{\"deptType\":3,\"key\":73,\"parentId\":24,\"title\":\"组装\"},{\"deptType\":3,\"key\":74,\"parentId\":25,\"title\":\"压铸\"},{\"deptType\":3,\"key\":75,\"parentId\":25,\"title\":\"打磨\"},{\"deptType\":3,\"key\":76,\"parentId\":25,\"title\":\"精加工\"},{\"deptType\":3,\"key\":77,\"parentId\":25,\"title\":\"喷塑\"},{\"deptType\":3,\"key\":78,\"parentId\":30,\"title\":\"电子组\"},{\"deptType\":3,\"key\":79,\"parentId\":30,\"title\":\"五金组\"},{\"deptType\":3,\"key\":80,\"parentId\":30,\"title\":\"模具组\"},{\"deptType\":3,\"key\":81,\"parentId\":33,\"title\":\"股东大会\"},{\"deptType\":3,\"key\":82,\"parentId\":33,\"title\":\"董事会\"},{\"deptType\":3,\"key\":83,\"parentId\":33,\"title\":\"监事会\"},{\"deptType\":3,\"key\":84,\"parentId\":34,\"title\":\"管理组\"},{\"deptType\":3,\"key\":85,\"parentId\":34,\"title\":\"知识产权组\"},{\"deptType\":3,\"key\":86,\"parentId\":34,\"title\":\"法务组\"},{\"deptType\":4,\"key\":87,\"parentId\":2,\"title\":\"市场营销体系中心\"},{\"deptType\":4,\"key\":88,\"parentId\":2,\"title\":\"销售服务体系中心\"},{\"deptType\":4,\"key\":89,\"parentId\":2,\"title\":\"研发体系中心\"},{\"deptType\":4,\"key\":90,\"parentId\":2,\"title\":\"行政体系中心\"},{\"deptType\":4,\"key\":91,\"parentId\":2,\"title\":\"总经办\"},{\"deptType\":2,\"key\":92,\"parentId\":87,\"title\":\"战略策划部\"},{\"deptType\":2,\"key\":93,\"parentId\":87,\"title\":\"营销支持部\"},{\"deptType\":2,\"key\":94,\"parentId\":87,\"title\":\"营销监督审核服务部\"},{\"deptType\":2,\"key\":95,\"parentId\":88,\"title\":\"智慧交通销售部\"},{\"deptType\":2,\"key\":96,\"parentId\":88,\"title\":\"智慧小镇销售部\"},{\"deptType\":2,\"key\":97,\"parentId\":88,\"title\":\"智慧校园销售部\"},{\"deptType\":2,\"key\":98,\"parentId\":88,\"title\":\"智慧别墅销售部\"},{\"deptType\":2,\"key\":99,\"parentId\":88,\"title\":\"智慧社区销售部\"},{\"deptType\":2,\"key\":100,\"parentId\":88,\"title\":\"智慧景区销售部\"},{\"deptType\":2,\"key\":101,\"parentId\":88,\"title\":\"智慧工厂销售部\"},{\"deptType\":2,\"key\":102,\"parentId\":88,\"title\":\"智慧公园销售部\"},{\"deptType\":2,\"key\":103,\"parentId\":89,\"title\":\"智能软件开发部\"},{\"deptType\":2,\"key\":104,\"parentId\":89,\"title\":\"智能硬件开发部\"},{\"deptType\":2,\"key\":105,\"parentId\":89,\"title\":\"智能结构开发部\"},{\"deptType\":2,\"key\":106,\"parentId\":90,\"title\":\"采购部\"},{\"deptType\":2,\"key\":107,\"parentId\":90,\"title\":\"行政部\"},{\"deptType\":2,\"key\":108,\"parentId\":90,\"title\":\"人力资源部\"},{\"deptType\":2,\"key\":109,\"parentId\":91,\"title\":\"高端技术预研部\"},{\"deptType\":2,\"key\":110,\"parentId\":91,\"title\":\"IT数据信息安全管理部\"},{\"deptType\":2,\"key\":111,\"parentId\":91,\"title\":\"审计部\"},{\"deptType\":2,\"key\":112,\"parentId\":91,\"title\":\"政企部\"},{\"deptType\":2,\"key\":113,\"parentId\":91,\"title\":\"财务部\"},{\"deptType\":3,\"key\":114,\"parentId\":92,\"title\":\"预研部\"},{\"deptType\":3,\"key\":115,\"parentId\":92,\"title\":\"设计部\"},{\"deptType\":3,\"key\":116,\"parentId\":92,\"title\":\"市场规划部\"},{\"deptType\":3,\"key\":117,\"parentId\":92,\"title\":\"品牌策划部\"},{\"deptType\":3,\"key\":118,\"parentId\":92,\"title\":\"产品策划部\"}]";
        when(manageDepartmentMapperExt.getDeptByParentIdCode(any())).thenReturn(JSONUtils.toList(backBO, ManageDepartmentTreeBO.class));

        String param = "{\"deptId\":1,\"deptType\":1,\"repertoryType\":1,\"resourceList\":[{\"resourceId\":\"edm_1301\",\"resourceType\":false,\"securityLevel\":5}]}\n";
        String contentAsString = postRequestMethod(param, "/department/getOrgInfoListToDept");
        BackBO listBackBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO>(){});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true, listBackBO.getStatus());
    }


    @Test
    public void getOrganization() throws Exception {
        String orgBackBO = "{\"created\":1,\"creator\":1,\"deptType\":3,\"enabled\":true,\"id\":55,\"name\":\"软件组\",\"parentId\":16,\"parentIdCode\":\"0_1_4_16_55_\",\"updated\":1,\"updator\":1}";
        when(manageDepartmentMapper.selectByPrimaryKey(any())).thenReturn(JSONUtils.parse(orgBackBO, ManageDepartment.class));
        String backBO = "[{\"created\":1,\"creator\":1,\"deptType\":2,\"enabled\":true,\"id\":16,\"name\":\"销售技术服务一部\",\"parentId\":null,\"parentIdCode\":\"0_1_4_16_\",\"updated\":1,\"updator\":1}]";
        when(manageDepartmentMapper.selectByExample(any())).thenReturn(JSONUtils.toList(backBO,ManageDepartment.class));
        String param = "{\"deptId\":55,\"deptType\":0,\"parentId\":0}";
        String contentAsString = postRequestMethod(param, "/department/getOrganization");
        BackBO<OrganizationDeptBO> listBackBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO<OrganizationDeptBO>>() {});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true, listBackBO.getStatus());
    }

    @Test
    public void getDeptInfo() throws Exception {
        String orgBackBO = "{\"created\":1,\"creator\":1,\"deptType\":3,\"enabled\":true,\"id\":52,\"name\":\"结构组\",\"parentId\":15,\"parentIdCode\":\"0_1_4_15_52_\",\"updated\":1,\"updator\":1}";
        when(manageDepartmentMapper.selectByPrimaryKey(any())).thenReturn(JSONUtils.parse(orgBackBO, ManageDepartment.class));
        String backBO = "{\"created\":1,\"creator\":1,\"deptType\":2,\"enabled\":true,\"id\":15,\"name\":\"光电技术部\",\"parentId\":4,\"parentIdCode\":\"0_1_4_15_\",\"updated\":1,\"updator\":1}";
        when(manageDepartmentMapper.selectByPrimaryKey(any())).thenReturn(JSONUtils.parse(backBO, ManageDepartment.class));
        String param = "52";
        String contentAsString = postRequestMethod(param, "/department/getDeptInfo");
        BackBO bo = JSONUtils.parse(contentAsString, BackBO.class);
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true, bo.getStatus());
    }


    @Test
    public void getFileOrgList() throws Exception {
        String orgBackBO = "[{\"created\":1,\"creator\":1,\"deptType\":3,\"enabled\":true,\"id\":52,\"name\":\"结构组\",\"parentId\":15,\"parentIdCode\":\"0_1_4_15_52_\",\"updated\":1,\"updator\":1},{\"created\":1,\"creator\":1,\"deptType\":3,\"enabled\":true,\"id\":54,\"name\":\"硬件组\",\"parentId\":16,\"parentIdCode\":\"0_1_4_16_54_\",\"updated\":1,\"updator\":1}]";
        when(manageDepartmentMapper.selectByExample(any())).thenReturn(JSONUtils.toList(orgBackBO, ManageDepartment.class));

        String param = "[" + " 52,54" + "]";
        String contentAsString = postRequestMethod(param, "/department/getFileOrgList");
        BackBO<List<ReadFileOrgInfoBO>> listBackBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO<List<ReadFileOrgInfoBO>>>() {});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true, listBackBO.getStatus());
    }
}
