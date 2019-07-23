package com.hoolink.manage.base.controller.server;

import com.alibaba.fastjson.TypeReference;
import com.hoolink.manage.base.bo.DeptPositionBO;
import com.hoolink.manage.base.controller.TestController;
import com.hoolink.manage.base.dao.mapper.ManageDepartmentMapper;
import com.hoolink.manage.base.dao.mapper.ext.ManageDepartmentMapperExt;
import com.hoolink.manage.base.dao.mapper.ext.ManageMenuMapperExt;
import com.hoolink.manage.base.dao.mapper.ext.MiddleUserDepartmentMapperExt;
import com.hoolink.manage.base.dao.model.ManageDepartment;
import com.hoolink.manage.base.dao.model.ManageMenu;
import com.hoolink.sdk.bo.BackBO;
import com.hoolink.sdk.bo.edm.EdmMenuTreeBO;
import com.hoolink.sdk.bo.manager.ManageDepartmentBO;
import com.hoolink.sdk.utils.JSONUtils;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


/**
 * 测试
 * @author ：weimin
 */
public class MenuControllerTest extends TestController {
    private Logger logger=LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuController menuController;

    @MockBean
    private ManageMenuMapperExt manageMenuMapperExt;

    @MockBean
    private MiddleUserDepartmentMapperExt middleUserDepartmentMapperExt;

    @MockBean
    private ManageDepartmentMapperExt manageDepartmentMapperExt;

    @MockBean
    private ManageDepartmentMapper manageDepartmentMapper;

    @Before
    public void setUp() {
        super.setUp();
        //控制类通过@Autowired自动注入进来，以此构建mockMvc对象
        mockMvc = MockMvcBuilders.standaloneSetup(menuController).build();
    }

    @Test
    public void listByCode() throws Exception {
        String manageMenu="{\"created\":1,\"creator\":1,\"enabled\":true,\"id\":5,\"menuCode\":\"DEPT_REPERTORY\",\"menuName\":\"部门资源\",\"parentId\":4,\"priority\":1,\"updated\":1,\"updator\":1}";
        String deptAllList="[{\"deptName\":\"互灵\",\"deptType\":1,\"encryLevelDept\":1,\"id\":2,\"lowestLevel\":true,\"parentId\":0,\"parentIdCode\":\"0_2_\"},{\"deptName\":\"晶日\",\"deptType\":1,\"encryLevelDept\":1,\"id\":1,\"lowestLevel\":true,\"parentId\":0,\"parentIdCode\":\"0_1_\"}]";
        String deptPositionBOS="[{\"deptName\":\"IT数据信息安全管理部\",\"deptType\":2,\"id\":110,\"parentId\":91,\"parentIdCode\":\"0_2_91_110_\"},{\"deptName\":\"OEM&ODM部\",\"deptType\":2,\"id\":20,\"parentId\":5,\"parentIdCode\":\"0_1_5_20_\"},{\"deptName\":\"SMT\",\"deptType\":3,\"id\":72,\"parentId\":24,\"parentIdCode\":\"0_1_6_24_72_\"},{\"deptName\":\"安环部\",\"deptType\":2,\"id\":38,\"parentId\":10,\"parentIdCode\":\"0_1_10_38_\"},{\"deptName\":\"标准组\",\"deptType\":3,\"id\":48,\"parentId\":15,\"parentIdCode\":\"0_1_4_15_48_\"},{\"deptName\":\"财务部\",\"deptType\":2,\"id\":32,\"parentId\":9,\"parentIdCode\":\"0_1_9_32_\"},{\"deptName\":\"财务部\",\"deptType\":2,\"id\":113,\"parentId\":91,\"parentIdCode\":\"0_2_91_113_\"},{\"deptName\":\"财务体系中心\",\"deptType\":4,\"id\":9,\"parentId\":1,\"parentIdCode\":\"0_1_9_\"},{\"deptName\":\"采购部\",\"deptType\":2,\"id\":30,\"parentId\":8,\"parentIdCode\":\"0_1_8_30_\"},{\"deptName\":\"采购部\",\"deptType\":2,\"id\":106,\"parentId\":90,\"parentIdCode\":\"0_2_90_106_\"},{\"deptName\":\"仓储物流部\",\"deptType\":2,\"id\":23,\"parentId\":6,\"parentIdCode\":\"0_1_6_23_\"},{\"deptName\":\"产品策划部\",\"deptType\":3,\"id\":118,\"parentId\":92,\"parentIdCode\":\"0_2_87_92_118_\"},{\"deptName\":\"产品核价部\",\"deptType\":2,\"id\":31,\"parentId\":8,\"parentIdCode\":\"0_1_8_31_\"},{\"deptName\":\"成本管理体系中心\",\"deptType\":4,\"id\":8,\"parentId\":1,\"parentIdCode\":\"0_1_8_\"},{\"deptName\":\"打磨\",\"deptType\":3,\"id\":75,\"parentId\":25,\"parentIdCode\":\"0_1_6_25_75_\"},{\"deptName\":\"灯具制造部\",\"deptType\":2,\"id\":24,\"parentId\":6,\"parentIdCode\":\"0_1_6_24_\"},{\"deptName\":\"灯体制造部\",\"deptType\":2,\"id\":25,\"parentId\":6,\"parentIdCode\":\"0_1_6_25_\"},{\"deptName\":\"电子组\",\"deptType\":3,\"id\":78,\"parentId\":30,\"parentIdCode\":\"0_1_8_30_78_\"},{\"deptName\":\"东北区\",\"deptType\":3,\"id\":61,\"parentId\":18,\"parentIdCode\":\"0_1_5_18_61_\"},{\"deptName\":\"董秘办\",\"deptType\":2,\"id\":33,\"parentId\":10,\"parentIdCode\":\"0_1_10_33_\"},{\"deptName\":\"董事会\",\"deptType\":3,\"id\":82,\"parentId\":33,\"parentIdCode\":\"0_1_10_33_82_\"},{\"deptName\":\"法务组\",\"deptType\":3,\"id\":86,\"parentId\":34,\"parentIdCode\":\"0_1_10_34_86_\"},{\"deptName\":\"非洲区\",\"deptType\":3,\"id\":71,\"parentId\":19,\"parentIdCode\":\"0_1_5_19_71_\"},{\"deptName\":\"高端技术预研部\",\"deptType\":2,\"id\":109,\"parentId\":91,\"parentIdCode\":\"0_2_91_109_\"},{\"deptName\":\"工业设计组\",\"deptType\":3,\"id\":44,\"parentId\":13,\"parentIdCode\":\"0_1_3_13_44_\"},{\"deptName\":\"股东大会\",\"deptType\":3,\"id\":81,\"parentId\":33,\"parentIdCode\":\"0_1_10_33_81_\"},{\"deptName\":\"管理组\",\"deptType\":3,\"id\":84,\"parentId\":34,\"parentIdCode\":\"0_1_10_34_84_\"},{\"deptName\":\"光电技术部\",\"deptType\":2,\"id\":15,\"parentId\":4,\"parentIdCode\":\"0_1_4_15_\"},{\"deptName\":\"光学组\",\"deptType\":3,\"id\":49,\"parentId\":15,\"parentIdCode\":\"0_1_4_15_49_\"},{\"deptName\":\"光学组\",\"deptType\":3,\"id\":53,\"parentId\":16,\"parentIdCode\":\"0_1_4_16_53_\"},{\"deptName\":\"光学组\",\"deptType\":3,\"id\":57,\"parentId\":17,\"parentIdCode\":\"0_1_4_17_57_\"},{\"deptName\":\"国际销售部\",\"deptType\":2,\"id\":19,\"parentId\":5,\"parentIdCode\":\"0_1_5_19_\"},{\"deptName\":\"国内销售部\",\"deptType\":2,\"id\":18,\"parentId\":5,\"parentIdCode\":\"0_1_5_18_\"},{\"deptName\":\"互灵\",\"deptType\":1,\"id\":2,\"parentId\":0,\"parentIdCode\":\"0_2_\"},{\"deptName\":\"华北区\",\"deptType\":3,\"id\":62,\"parentId\":18,\"parentIdCode\":\"0_1_5_18_62_\"},{\"deptName\":\"华东区\",\"deptType\":3,\"id\":65,\"parentId\":18,\"parentIdCode\":\"0_1_5_18_65_\"},{\"deptName\":\"华南区\",\"deptType\":3,\"id\":64,\"parentId\":18,\"parentIdCode\":\"0_1_5_18_64_\"},{\"deptName\":\"华中区\",\"deptType\":3,\"id\":63,\"parentId\":18,\"parentIdCode\":\"0_1_5_18_63_\"},{\"deptName\":\"计划部\",\"deptType\":2,\"id\":22,\"parentId\":6,\"parentIdCode\":\"0_1_6_22_\"},{\"deptName\":\"监事会\",\"deptType\":3,\"id\":83,\"parentId\":33,\"parentIdCode\":\"0_1_10_33_83_\"},{\"deptName\":\"检测中心\",\"deptType\":2,\"id\":29,\"parentId\":7,\"parentIdCode\":\"0_1_7_29_\"},{\"deptName\":\"结构组\",\"deptType\":3,\"id\":52,\"parentId\":15,\"parentIdCode\":\"0_1_4_15_52_\"},{\"deptName\":\"结构组\",\"deptType\":3,\"id\":56,\"parentId\":16,\"parentIdCode\":\"0_1_4_16_56_\"},{\"deptName\":\"结构组\",\"deptType\":3,\"id\":60,\"parentId\":17,\"parentIdCode\":\"0_1_4_17_60_\"},{\"deptName\":\"晶日\",\"deptType\":1,\"id\":1,\"parentId\":0,\"parentIdCode\":\"0_1_\"},{\"deptName\":\"精加工\",\"deptType\":3,\"id\":76,\"parentId\":25,\"parentIdCode\":\"0_1_6_25_76_\"},{\"deptName\":\"美洲区\",\"deptType\":3,\"id\":69,\"parentId\":19,\"parentIdCode\":\"0_1_5_19_69_\"},{\"deptName\":\"秘书室\",\"deptType\":3,\"id\":42,\"parentId\":12,\"parentIdCode\":\"0_1_3_12_42_\"},{\"deptName\":\"模具组\",\"deptType\":3,\"id\":80,\"parentId\":30,\"parentIdCode\":\"0_1_8_30_80_\"},{\"deptName\":\"欧洲区\",\"deptType\":3,\"id\":68,\"parentId\":19,\"parentIdCode\":\"0_1_5_19_68_\"},{\"deptName\":\"喷塑\",\"deptType\":3,\"id\":77,\"parentId\":25,\"parentIdCode\":\"0_1_6_25_77_\"},{\"deptName\":\"品保部\",\"deptType\":2,\"id\":28,\"parentId\":7,\"parentIdCode\":\"0_1_7_28_\"},{\"deptName\":\"品牌策划部\",\"deptType\":3,\"id\":117,\"parentId\":92,\"parentIdCode\":\"0_2_87_92_117_\"},{\"deptName\":\"品牌策划组\",\"deptType\":3,\"id\":45,\"parentId\":13,\"parentIdCode\":\"0_1_3_13_45_\"},{\"deptName\":\"品质部\",\"deptType\":2,\"id\":27,\"parentId\":7,\"parentIdCode\":\"0_1_7_27_\"},{\"deptName\":\"品质体系中心\",\"deptType\":4,\"id\":7,\"parentId\":1,\"parentIdCode\":\"0_1_7_\"},{\"deptName\":\"企业运营保障体系中心\",\"deptType\":4,\"id\":10,\"parentId\":1,\"parentIdCode\":\"0_1_10_\"},{\"deptName\":\"人力资源部\",\"deptType\":2,\"id\":108,\"parentId\":90,\"parentIdCode\":\"0_2_90_108_\"},{\"deptName\":\"人资部\",\"deptType\":2,\"id\":36,\"parentId\":10,\"parentIdCode\":\"0_1_10_36_\"},{\"deptName\":\"软件组\",\"deptType\":3,\"id\":51,\"parentId\":15,\"parentIdCode\":\"0_1_4_15_51_\"},{\"deptName\":\"软件组\",\"deptType\":3,\"id\":55,\"parentId\":16,\"parentIdCode\":\"0_1_4_16_55_\"},{\"deptName\":\"软件组\",\"deptType\":3,\"id\":59,\"parentId\":17,\"parentIdCode\":\"0_1_4_17_59_\"},{\"deptName\":\"设备部\",\"deptType\":2,\"id\":37,\"parentId\":10,\"parentIdCode\":\"0_1_10_37_\"},{\"deptName\":\"设计部\",\"deptType\":3,\"id\":115,\"parentId\":92,\"parentIdCode\":\"0_2_87_92_115_\"},{\"deptName\":\"审计部\",\"deptType\":2,\"id\":111,\"parentId\":91,\"parentIdCode\":\"0_2_91_111_\"},{\"deptName\":\"市场调研组\",\"deptType\":3,\"id\":43,\"parentId\":13,\"parentIdCode\":\"0_1_3_13_43_\"},{\"deptName\":\"市场规划部\",\"deptType\":3,\"id\":116,\"parentId\":92,\"parentIdCode\":\"0_2_87_92_116_\"},{\"deptName\":\"市场拓展组\",\"deptType\":3,\"id\":47,\"parentId\":14,\"parentIdCode\":\"0_1_3_14_47_\"},{\"deptName\":\"市场营销服务部\",\"deptType\":2,\"id\":14,\"parentId\":3,\"parentIdCode\":\"0_1_3_14_\"},{\"deptName\":\"市场营销体系中心\",\"deptType\":4,\"id\":3,\"parentId\":1,\"parentIdCode\":\"0_1_3_\"},{\"deptName\":\"市场营销体系中心\",\"deptType\":4,\"id\":87,\"parentId\":2,\"parentIdCode\":\"0_2_87_\"},{\"deptName\":\"市场战略策划部\",\"deptType\":2,\"id\":13,\"parentId\":3,\"parentIdCode\":\"0_1_3_13_\"},{\"deptName\":\"特种制造部\",\"deptType\":2,\"id\":26,\"parentId\":6,\"parentIdCode\":\"0_1_6_26_\"},{\"deptName\":\"图文数据管理部\",\"deptType\":2,\"id\":41,\"parentId\":11,\"parentIdCode\":\"0_1_11_41_\"},{\"deptName\":\"五金组\",\"deptType\":3,\"id\":79,\"parentId\":30,\"parentIdCode\":\"0_1_8_30_79_\"},{\"deptName\":\"西北区\",\"deptType\":3,\"id\":67,\"parentId\":18,\"parentIdCode\":\"0_1_5_18_67_\"},{\"deptName\":\"西南区\",\"deptType\":3,\"id\":66,\"parentId\":18,\"parentIdCode\":\"0_1_5_18_66_\"},{\"deptName\":\"销售策划组\",\"deptType\":3,\"id\":46,\"parentId\":14,\"parentIdCode\":\"0_1_3_14_46_\"},{\"deptName\":\"销售服务体系中心\",\"deptType\":4,\"id\":5,\"parentId\":1,\"parentIdCode\":\"0_1_5_\"},{\"deptName\":\"销售服务体系中心\",\"deptType\":4,\"id\":88,\"parentId\":2,\"parentIdCode\":\"0_2_88_\"},{\"deptName\":\"销售技术服务二部\",\"deptType\":2,\"id\":17,\"parentId\":4,\"parentIdCode\":\"0_1_4_17_\"},{\"deptName\":\"销售技术服务一部\",\"deptType\":2,\"id\":16,\"parentId\":4,\"parentIdCode\":\"0_1_4_16_\"},{\"deptName\":\"信息文控管理体系中心\",\"deptType\":4,\"id\":11,\"parentId\":1,\"parentIdCode\":\"0_1_11_\"},{\"deptName\":\"信息综合管理部\",\"deptType\":2,\"id\":39,\"parentId\":11,\"parentIdCode\":\"0_1_11_39_\"},{\"deptName\":\"行政部\",\"deptType\":2,\"id\":35,\"parentId\":10,\"parentIdCode\":\"0_1_10_35_\"},{\"deptName\":\"行政部\",\"deptType\":2,\"id\":107,\"parentId\":90,\"parentIdCode\":\"0_2_90_107_\"},{\"deptName\":\"行政体系中心\",\"deptType\":4,\"id\":90,\"parentId\":2,\"parentIdCode\":\"0_2_90_\"},{\"deptName\":\"压铸\",\"deptType\":3,\"id\":74,\"parentId\":25,\"parentIdCode\":\"0_1_6_25_74_\"},{\"deptName\":\"亚洲区\",\"deptType\":3,\"id\":70,\"parentId\":19,\"parentIdCode\":\"0_1_5_19_70_\"},{\"deptName\":\"研发体系中心\",\"deptType\":4,\"id\":4,\"parentId\":1,\"parentIdCode\":\"0_1_4_\"},{\"deptName\":\"研发体系中心\",\"deptType\":4,\"id\":89,\"parentId\":2,\"parentIdCode\":\"0_2_89_\"},{\"deptName\":\"营销监督审核服务部\",\"deptType\":2,\"id\":94,\"parentId\":87,\"parentIdCode\":\"0_2_87_94_\"},{\"deptName\":\"营销支持部\",\"deptType\":2,\"id\":93,\"parentId\":87,\"parentIdCode\":\"0_2_87_93_\"},{\"deptName\":\"硬件组\",\"deptType\":3,\"id\":50,\"parentId\":15,\"parentIdCode\":\"0_1_4_15_50_\"},{\"deptName\":\"硬件组\",\"deptType\":3,\"id\":54,\"parentId\":16,\"parentIdCode\":\"0_1_4_16_54_\"},{\"deptName\":\"硬件组\",\"deptType\":3,\"id\":58,\"parentId\":17,\"parentIdCode\":\"0_1_4_17_58_\"},{\"deptName\":\"预研部\",\"deptType\":2,\"id\":12,\"parentId\":3,\"parentIdCode\":\"0_1_3_12_\"},{\"deptName\":\"预研部\",\"deptType\":3,\"id\":114,\"parentId\":92,\"parentIdCode\":\"0_2_87_92_114_\"},{\"deptName\":\"云平台管理部\",\"deptType\":2,\"id\":40,\"parentId\":11,\"parentIdCode\":\"0_1_11_40_\"},{\"deptName\":\"战略策划部\",\"deptType\":2,\"id\":92,\"parentId\":87,\"parentIdCode\":\"0_2_87_92_\"},{\"deptName\":\"招投标部\",\"deptType\":2,\"id\":21,\"parentId\":5,\"parentIdCode\":\"0_1_5_21_\"},{\"deptName\":\"政企部\",\"deptType\":2,\"id\":112,\"parentId\":91,\"parentIdCode\":\"0_2_91_112_\"},{\"deptName\":\"知识产权组\",\"deptType\":3,\"id\":85,\"parentId\":34,\"parentIdCode\":\"0_1_10_34_85_\"},{\"deptName\":\"制造体系中心\",\"deptType\":4,\"id\":6,\"parentId\":1,\"parentIdCode\":\"0_1_6_\"},{\"deptName\":\"智慧别墅销售部\",\"deptType\":2,\"id\":98,\"parentId\":88,\"parentIdCode\":\"0_2_88_98_\"},{\"deptName\":\"智慧工厂销售部\",\"deptType\":2,\"id\":101,\"parentId\":88,\"parentIdCode\":\"0_2_88_101_\"},{\"deptName\":\"智慧公园销售部\",\"deptType\":2,\"id\":102,\"parentId\":88,\"parentIdCode\":\"0_2_88_102_\"},{\"deptName\":\"智慧交通销售部\",\"deptType\":2,\"id\":95,\"parentId\":88,\"parentIdCode\":\"0_2_88_95_\"},{\"deptName\":\"智慧景区销售部\",\"deptType\":2,\"id\":100,\"parentId\":88,\"parentIdCode\":\"0_2_88_100_\"},{\"deptName\":\"智慧社区销售部\",\"deptType\":2,\"id\":99,\"parentId\":88,\"parentIdCode\":\"0_2_88_99_\"},{\"deptName\":\"智慧小镇销售部\",\"deptType\":2,\"id\":96,\"parentId\":88,\"parentIdCode\":\"0_2_88_96_\"},{\"deptName\":\"智慧校园销售部\",\"deptType\":2,\"id\":97,\"parentId\":88,\"parentIdCode\":\"0_2_88_97_\"},{\"deptName\":\"智能结构开发部\",\"deptType\":2,\"id\":105,\"parentId\":89,\"parentIdCode\":\"0_2_89_105_\"},{\"deptName\":\"智能软件开发部\",\"deptType\":2,\"id\":103,\"parentId\":89,\"parentIdCode\":\"0_2_89_103_\"},{\"deptName\":\"智能硬件开发部\",\"deptType\":2,\"id\":104,\"parentId\":89,\"parentIdCode\":\"0_2_89_104_\"},{\"deptName\":\"总经办\",\"deptType\":2,\"id\":34,\"parentId\":10,\"parentIdCode\":\"0_1_10_34_\"},{\"deptName\":\"总经办\",\"deptType\":4,\"id\":91,\"parentId\":2,\"parentIdCode\":\"0_2_91_\"},{\"deptName\":\"组装\",\"deptType\":3,\"id\":73,\"parentId\":24,\"parentIdCode\":\"0_1_6_24_73_\"}]";
        when(manageMenuMapperExt.selectByExample(any(),any())).thenReturn(JSONUtils.parse(manageMenu,new TypeReference<ManageMenu>(){}));
        when(middleUserDepartmentMapperExt.getDept(any())).thenReturn(JSONUtils.parse(deptAllList,new TypeReference<List<DeptPositionBO>>(){}));
        when(manageDepartmentMapperExt.listByParentIdCode(any())).thenReturn(JSONUtils.parse(deptPositionBOS,new TypeReference<List<DeptPositionBO>>(){}));
        String param="{\"code\": \"EDM\",\"repertoryType\": 1}";
        logger.info("url:{},参数param:{}","/menu/listByCode",param);
        String contentAsString = postRequestMethod(param, "/menu/listByCode");
        BackBO backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO<EdmMenuTreeBO>>(){});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true,backBO.getStatus());

        manageMenu="{\"created\":1,\"creator\":1,\"enabled\":true,\"id\":6,\"menuCode\":\"CACHE_REPERTORY\",\"menuName\":\"缓存库\",\"parentId\":4,\"priority\":2,\"updated\":1,\"updator\":1}";
        when(manageMenuMapperExt.selectByExample(any(),any())).thenReturn(JSONUtils.parse(manageMenu,new TypeReference<ManageMenu>(){}));
        param="{\"code\": \"EDM\",\"repertoryType\": 2}";
        logger.info("url:{},参数param:{}","/menu/listByCode",param);
        contentAsString = postRequestMethod(param, "/menu/listByCode");
        backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO<EdmMenuTreeBO>>(){});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true,backBO.getStatus());

        manageMenu="{\"created\":1,\"creator\":1,\"enabled\":true,\"id\":7,\"menuCode\":\"COMPANY_REPERTORY\",\"menuName\":\"资源库\",\"parentId\":4,\"priority\":3,\"updated\":1,\"updator\":1}";
        when(manageMenuMapperExt.selectByExample(any(),any())).thenReturn(JSONUtils.parse(manageMenu,new TypeReference<ManageMenu>(){}));
        param="{\"code\": \"EDM\",\"repertoryType\": 3}";
        logger.info("url:{},参数param:{}","/menu/listByCode",param);
        contentAsString = postRequestMethod(param, "/menu/listByCode");
        backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO<EdmMenuTreeBO>>(){});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true,backBO.getStatus());
    }


    @Test
    public void getOrganizationHead() throws Exception {
        String manageMenu="{\"created\":1,\"creator\":1,\"enabled\":true,\"id\":5,\"menuCode\":\"DEPT_REPERTORY\",\"menuName\":\"部门资源\",\"parentId\":4,\"priority\":1,\"updated\":1,\"updator\":1}";
        String manageDepartment="{\"created\":1,\"creator\":1,\"deptType\":2,\"enabled\":true,\"id\":16,\"name\":\"销售技术服务一部\",\"parentId\":4,\"parentIdCode\":\"0_1_4_16_\",\"updated\":1,\"updator\":1}";
        String manageDepartments="[{\"created\":1,\"creator\":1,\"deptType\":3,\"enabled\":true,\"id\":53,\"name\":\"光学组\",\"parentId\":16,\"parentIdCode\":\"0_1_4_16_53_\",\"updated\":1,\"updator\":1},{\"created\":1,\"creator\":1,\"deptType\":3,\"enabled\":true,\"id\":54,\"name\":\"硬件组\",\"parentId\":16,\"parentIdCode\":\"0_1_4_16_54_\",\"updated\":1,\"updator\":1},{\"created\":1,\"creator\":1,\"deptType\":3,\"enabled\":true,\"id\":55,\"name\":\"软件组\",\"parentId\":16,\"parentIdCode\":\"0_1_4_16_55_\",\"updated\":1,\"updator\":1},{\"created\":1,\"creator\":1,\"deptType\":3,\"enabled\":true,\"id\":56,\"name\":\"结构组\",\"parentId\":16,\"parentIdCode\":\"0_1_4_16_56_\",\"updated\":1,\"updator\":1}]";
        String manageDepartmentBOS="[{\"created\":1,\"creator\":1,\"deptType\":1,\"enabled\":true,\"id\":1,\"name\":\"晶日\",\"parentId\":0,\"updated\":1,\"updator\":1},{\"created\":1,\"creator\":1,\"deptType\":4,\"enabled\":true,\"id\":4,\"name\":\"研发体系中心\",\"parentId\":1,\"updated\":1,\"updator\":1},{\"created\":1,\"creator\":1,\"deptType\":2,\"enabled\":true,\"id\":16,\"name\":\"销售技术服务一部\",\"parentId\":4,\"updated\":1,\"updator\":1}]";
        when(manageMenuMapperExt.selectByExample(any(),any())).thenReturn(JSONUtils.parse(manageMenu,new TypeReference<ManageMenu>(){}));
        when(manageDepartmentMapper.selectByPrimaryKey(any())).thenReturn(JSONUtils.parse(manageDepartment,new TypeReference<ManageDepartment>(){}));
        when(manageDepartmentMapper.selectByExample(any())).thenReturn(JSONUtils.parse(manageDepartments,new TypeReference<List<ManageDepartment>>(){}));
        when(manageDepartmentMapperExt.listByIdOrder(any())).thenReturn(JSONUtils.parse(manageDepartmentBOS,new TypeReference<List<ManageDepartmentBO>>(){}));
        String param="{\"belongId\": \"16\",\"repertoryType\": 1}";
        logger.info("url:{},参数param:{}", "/menu/getOrganizationHead", param);
        String contentAsString = postRequestMethod(param, "/menu/getOrganizationHead");
        BackBO backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO<List<EdmMenuTreeBO>>>() {});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true, backBO.getStatus());

        manageMenu="{\"created\":1,\"creator\":1,\"enabled\":true,\"id\":6,\"menuCode\":\"CACHE_REPERTORY\",\"menuName\":\"缓存库\",\"parentId\":4,\"priority\":2,\"updated\":1,\"updator\":1}";
        manageDepartment="{\"created\":1,\"creator\":1,\"deptType\":1,\"enabled\":true,\"id\":1,\"name\":\"晶日\",\"parentId\":0,\"parentIdCode\":\"0_1_\",\"updated\":1,\"updator\":1}";
        manageDepartments="[{\"created\":1,\"creator\":1,\"deptType\":4,\"enabled\":true,\"id\":3,\"name\":\"市场营销体系中心\",\"parentId\":1,\"parentIdCode\":\"0_1_3_\",\"updated\":1,\"updator\":1},{\"created\":1,\"creator\":1,\"deptType\":4,\"enabled\":true,\"id\":4,\"name\":\"研发体系中心\",\"parentId\":1,\"parentIdCode\":\"0_1_4_\",\"updated\":1,\"updator\":1},{\"created\":1,\"creator\":1,\"deptType\":4,\"enabled\":true,\"id\":5,\"name\":\"销售服务体系中心\",\"parentId\":1,\"parentIdCode\":\"0_1_5_\",\"updated\":1,\"updator\":1},{\"created\":1,\"creator\":1,\"deptType\":4,\"enabled\":true,\"id\":6,\"name\":\"制造体系中心\",\"parentId\":1,\"parentIdCode\":\"0_1_6_\",\"updated\":1,\"updator\":1},{\"created\":1,\"creator\":1,\"deptType\":4,\"enabled\":true,\"id\":7,\"name\":\"品质体系中心\",\"parentId\":1,\"parentIdCode\":\"0_1_7_\",\"updated\":1,\"updator\":1},{\"created\":1,\"creator\":1,\"deptType\":4,\"enabled\":true,\"id\":8,\"name\":\"成本管理体系中心\",\"parentId\":1,\"parentIdCode\":\"0_1_8_\",\"updated\":1,\"updator\":1},{\"created\":1,\"creator\":1,\"deptType\":4,\"enabled\":true,\"id\":9,\"name\":\"财务体系中心\",\"parentId\":1,\"parentIdCode\":\"0_1_9_\",\"updated\":1,\"updator\":1},{\"created\":1,\"creator\":1,\"deptType\":4,\"enabled\":true,\"id\":10,\"name\":\"企业运营保障体系中心\",\"parentId\":1,\"parentIdCode\":\"0_1_10_\",\"updated\":1,\"updator\":1},{\"created\":1,\"creator\":1,\"deptType\":4,\"enabled\":true,\"id\":11,\"name\":\"信息文控管理体系中心\",\"parentId\":1,\"parentIdCode\":\"0_1_11_\",\"updated\":1,\"updator\":1}]";
        when(manageMenuMapperExt.selectByExample(any(),any())).thenReturn(JSONUtils.parse(manageMenu,new TypeReference<ManageMenu>(){}));
        when(manageDepartmentMapper.selectByPrimaryKey(any())).thenReturn(JSONUtils.parse(manageDepartment,new TypeReference<ManageDepartment>(){}));
        when(manageDepartmentMapper.selectByExample(any())).thenReturn(JSONUtils.parse(manageDepartments,new TypeReference<List<ManageDepartment>>(){}));
        param="{\"belongId\": \"1\",\"repertoryType\": 2}";
        logger.info("url:{},参数param:{}", "/menu/getOrganizationHead", param);
        contentAsString = postRequestMethod(param, "/menu/getOrganizationHead");
        backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO<List<EdmMenuTreeBO>>>() {});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true, backBO.getStatus());

        manageMenu="{\"created\":1,\"creator\":1,\"enabled\":true,\"id\":7,\"menuCode\":\"COMPANY_REPERTORY\",\"menuName\":\"资源库\",\"parentId\":4,\"priority\":3,\"updated\":1,\"updator\":1}";
        manageDepartment="{\"created\":1,\"creator\":1,\"deptType\":1,\"enabled\":true,\"id\":1,\"name\":\"晶日\",\"parentId\":0,\"parentIdCode\":\"0_1_\",\"updated\":1,\"updator\":1}";
        manageDepartments="[{\"created\":1,\"creator\":1,\"deptType\":4,\"enabled\":true,\"id\":3,\"name\":\"市场营销体系中心\",\"parentId\":1,\"parentIdCode\":\"0_1_3_\",\"updated\":1,\"updator\":1},{\"created\":1,\"creator\":1,\"deptType\":4,\"enabled\":true,\"id\":4,\"name\":\"研发体系中心\",\"parentId\":1,\"parentIdCode\":\"0_1_4_\",\"updated\":1,\"updator\":1},{\"created\":1,\"creator\":1,\"deptType\":4,\"enabled\":true,\"id\":5,\"name\":\"销售服务体系中心\",\"parentId\":1,\"parentIdCode\":\"0_1_5_\",\"updated\":1,\"updator\":1},{\"created\":1,\"creator\":1,\"deptType\":4,\"enabled\":true,\"id\":6,\"name\":\"制造体系中心\",\"parentId\":1,\"parentIdCode\":\"0_1_6_\",\"updated\":1,\"updator\":1},{\"created\":1,\"creator\":1,\"deptType\":4,\"enabled\":true,\"id\":7,\"name\":\"品质体系中心\",\"parentId\":1,\"parentIdCode\":\"0_1_7_\",\"updated\":1,\"updator\":1},{\"created\":1,\"creator\":1,\"deptType\":4,\"enabled\":true,\"id\":8,\"name\":\"成本管理体系中心\",\"parentId\":1,\"parentIdCode\":\"0_1_8_\",\"updated\":1,\"updator\":1},{\"created\":1,\"creator\":1,\"deptType\":4,\"enabled\":true,\"id\":9,\"name\":\"财务体系中心\",\"parentId\":1,\"parentIdCode\":\"0_1_9_\",\"updated\":1,\"updator\":1},{\"created\":1,\"creator\":1,\"deptType\":4,\"enabled\":true,\"id\":10,\"name\":\"企业运营保障体系中心\",\"parentId\":1,\"parentIdCode\":\"0_1_10_\",\"updated\":1,\"updator\":1},{\"created\":1,\"creator\":1,\"deptType\":4,\"enabled\":true,\"id\":11,\"name\":\"信息文控管理体系中心\",\"parentId\":1,\"parentIdCode\":\"0_1_11_\",\"updated\":1,\"updator\":1}]";
        when(manageMenuMapperExt.selectByExample(any(),any())).thenReturn(JSONUtils.parse(manageMenu,new TypeReference<ManageMenu>(){}));
        when(manageDepartmentMapper.selectByPrimaryKey(any())).thenReturn(JSONUtils.parse(manageDepartment,new TypeReference<ManageDepartment>(){}));
        when(manageDepartmentMapper.selectByExample(any())).thenReturn(JSONUtils.parse(manageDepartments,new TypeReference<List<ManageDepartment>>(){}));
        param="{\"belongId\": \"1\",\"repertoryType\": 3}";
        logger.info("url:{},参数param:{}", "/menu/getOrganizationHead", param);
        contentAsString = postRequestMethod(param, "/menu/getOrganizationHead");
        backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO<List<EdmMenuTreeBO>>>() {});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true, backBO.getStatus());
    }
}