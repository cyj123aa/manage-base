package com.hoolink.manage.base.controller.server;

import com.alibaba.fastjson.TypeReference;
import com.hoolink.manage.base.controller.TestController;
import com.hoolink.sdk.bo.BackBO;
import com.hoolink.sdk.bo.edm.EdmMenuTreeBO;
import com.hoolink.sdk.utils.JSONUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;


/**
 * 测试
 * @author ：weimin
 */
public class MenuControllerTest extends TestController {
    private Logger logger=LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuController menuController;

    @Before
    public void setUp() {
        super.setUp();
        //控制类通过@Autowired自动注入进来，以此构建mockMvc对象
        mockMvc = MockMvcBuilders.standaloneSetup(menuController).build();
    }

    @Test
    public void listByCode() throws Exception {
        List<String> list = Arrays.asList("{\"code\": \"EDM\",\"repertoryType\": 1}", "{\"code\": \"EDM\",\"repertoryType\": 2}",
                "{\"code\": \"EDM\",\"repertoryType\": 3}");
        for (String param:list){
            logger.info("url:{},参数param:{}","/menu/listByCode",param);
            String contentAsString = postRequestMethod(param, "/menu/listByCode");
            BackBO backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO<EdmMenuTreeBO>>(){});
            //arg0:期待值  arg1：真实值
            Assert.assertEquals(true,backBO.getStatus());
        }
    }


    @Test
    public void getOrganizationHead() throws Exception {
        List<String> list = Arrays.asList("{\"belongId\": \"19\",\"repertoryType\": 1}", "{\"belongId\": \"1\",\"repertoryType\": 1}",
                "{\"belongId\": \"1\",\"repertoryType\": 1}");
        for (String param:list) {
            logger.info("url:{},参数param:{}", "/menu/getOrganizationHead", param);
            String contentAsString = postRequestMethod(param, "/menu/getOrganizationHead");
            BackBO backBO = JSONUtils.parse(contentAsString, new TypeReference<BackBO<List<EdmMenuTreeBO>>>() {
            });
            //arg0:期待值  arg1：真实值
            Assert.assertEquals(true, backBO.getStatus());
        }
    }
}