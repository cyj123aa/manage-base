package com.jw.manage.base.consumer.edm;

import com.jw.manage.base.consumer.edm.feignback.EdmFeignFallBack;
import com.jw.sdk.annotation.ClientException;
import com.jw.sdk.bo.BackBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author : cyj
 * @Date : 2019/4/23 17:01
 * @Instructions :
 */
@FeignClient(value = "manage-edm",fallback = EdmFeignFallBack.class)
@Component
public interface EdmClient {

    /**
     * 用于测试注册中心接口调用
     * @return
     */
    @ClientException(message="测试链路接口")
    @RequestMapping(value = "/edm/c",method= RequestMethod.POST)
    BackBO<String> edm() ;



}
