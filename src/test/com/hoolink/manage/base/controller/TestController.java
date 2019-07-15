package com.hoolink.manage.base.controller;

import com.hoolink.sdk.bo.base.CurrentUserBO;
import com.hoolink.sdk.utils.JSONUtils;
import org.apache.servicecomb.swagger.invocation.context.ContextUtils;
import org.apache.servicecomb.swagger.invocation.context.InvocationContext;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 测试父类
 * @author ：weimin
 */
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

}
