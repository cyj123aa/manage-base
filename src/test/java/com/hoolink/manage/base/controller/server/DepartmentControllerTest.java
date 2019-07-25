package com.hoolink.manage.base.controller.server;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;

import com.alibaba.fastjson.TypeReference;
import com.hoolink.manage.base.controller.TestController;
import com.hoolink.manage.base.dao.mapper.ManageDepartmentMapper;
import com.hoolink.manage.base.dao.mapper.UserMapper;
import com.hoolink.manage.base.dao.mapper.ext.MiddleUserDepartmentMapperExt;
import com.hoolink.manage.base.dao.model.ManageDepartment;
import com.hoolink.manage.base.service.MiddleUserDepartmentService;
import com.hoolink.sdk.bo.BackBO;
import com.hoolink.sdk.bo.manager.ManagerUserBO;
import com.hoolink.sdk.bo.manager.OrganizationDeptBO;
import com.hoolink.sdk.bo.manager.OrganizationDeptParamBO;
import com.hoolink.sdk.bo.manager.ReadFileOrgInfoBO;
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
 * @date 2019/7/24 17:43
 */
public class DepartmentControllerTest extends TestController {
    private Logger logger= LoggerFactory.getLogger(DepartmentControllerTest.class);

    @Autowired
    private DepartmentController departmentController;


    @MockBean
    private ManageDepartmentMapper manageDepartmentMapper;




    @Before
    public void setUp() {
        super.setUp();
        //控制类通过@Autowired自动注入进来，以此构建mockMvc对象
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();
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
