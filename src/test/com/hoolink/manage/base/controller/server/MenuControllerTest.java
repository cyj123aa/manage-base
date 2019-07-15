package com.hoolink.manage.base.controller.server;

import com.alibaba.fastjson.TypeReference;
import com.hoolink.manage.base.controller.TestController;
import com.hoolink.sdk.bo.BackBO;
import com.hoolink.sdk.bo.edm.EdmMenuTreeBO;
import com.hoolink.sdk.utils.JSONUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;


public class MenuControllerTest extends TestController {

    @Autowired
    private MenuController menuController;

    @Before
    public void setUp() {
        super.setUp();
        //控制类custController通过@Autowired自动注入进来，以此构建mockMvc对象
        mockMvc = MockMvcBuilders.standaloneSetup(menuController).build();
    }

    @Test
    public void listByCode() throws Exception {
        String param="{\"code\": \"EDM\",\"repertoryType\": 1}";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/menu/listByCode")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(param)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        BackBO backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO<EdmMenuTreeBO>>(){});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true,backBO.getStatus());
    }

    @Test
    public void getOrganizationHead() throws Exception {
        String param="{\"belongId\": \"19\",\"repertoryType\": 1}";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/menu/getOrganizationHead")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(param)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        BackBO backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO<List<EdmMenuTreeBO>>>(){});
        //arg0:期待值  arg1：真实值
        Assert.assertEquals(true,backBO.getStatus());
    }
}