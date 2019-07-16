package com.hoolink.manage.base.controller;

import com.hoolink.manage.base.ManagerApplication;
import com.hoolink.sdk.bo.base.CurrentUserBO;
import com.hoolink.sdk.utils.JSONUtils;
import org.apache.servicecomb.swagger.invocation.context.ContextUtils;
import org.apache.servicecomb.swagger.invocation.context.InvocationContext;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

/**
 * 测试父类
 * @author ：weimin
 */
@RunWith(value = SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = ManagerApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//配置事务的回滚,对数据库的增删改都会回滚,便于测试用例的循环利用
@Rollback
@Transactional
public abstract class TestController {

    protected MockMvc mockMvc;

    @SpyBean
    private InvocationContext invocationContext;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mockResources();
    }

     /**
     * 业务中需要用到用户信息
     */
    public void mockResources(){
        CurrentUserBO baseUserBO = new CurrentUserBO();
        baseUserBO.setUserId(1L);
        baseUserBO.setRoleId(1L);
        baseUserBO.setAccount("admin");
        ContextUtils.setInvocationContext(invocationContext);
        invocationContext.addContext("manageCurrentUser", JSONUtils.toJSONString(baseUserBO));
    }

    /**
     * post请求
     * @param param
     * @param txId
     * @return
     * @throws Exception
     */
    protected String postRequestMethod(String param, String txId) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(txId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(param)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        return mvcResult.getResponse().getContentAsString();
    }

}
