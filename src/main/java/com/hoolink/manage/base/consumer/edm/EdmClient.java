package com.hoolink.manage.base.consumer.edm;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.github.pagehelper.PageInfo;
import com.hoolink.sdk.annotation.ClientException;
import com.hoolink.sdk.bo.BackBO;
import com.hoolink.sdk.bo.edm.OperateFileLogBO;
import com.hoolink.sdk.bo.edm.OperateFileLogParamBO;

/**
 * 
 * @author lijunling
 *
 * @date 2019/06/17 20:11
 */
@Component
public class EdmClient {
    @Resource
    private RestTemplate restTemplate;
    
    /**
     * 获取用户操作日志
     * @param paramBO
     * @return
     */
    @ClientException(message="获取用户操作日志失败")
    public BackBO<PageInfo<OperateFileLogBO>> listOperateLog(OperateFileLogParamBO paramBO) {
        return restTemplate.postForObject("cse://manage-edm/file/operate/listOperateLog", paramBO, BackBO.class);
    }
}
