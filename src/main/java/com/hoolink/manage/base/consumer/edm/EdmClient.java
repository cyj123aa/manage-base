package com.hoolink.manage.base.consumer.edm;

import com.github.pagehelper.PageInfo;
import com.hoolink.manage.base.consumer.edm.feignback.EdmFeignFallBack;
import com.hoolink.sdk.annotation.ClientException;
import com.hoolink.sdk.bo.BackBO;
import com.hoolink.sdk.bo.edm.OperateFileLogBO;
import com.hoolink.sdk.bo.edm.OperateFileLogParamBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * @author lijunling
 *
 * @date 2019/06/17 20:11
 */
@FeignClient(value = "manage-edm",fallback = EdmFeignFallBack.class)
@Component
public interface EdmClient {
    
    /**
     * 获取用户操作日志
     * @param paramBO
     * @return
     */
    @ClientException(message="获取用户操作日志失败")
    @RequestMapping(value = "/file/operate/listOperateLog",method= RequestMethod.POST)
    BackBO<PageInfo<OperateFileLogBO>> listOperateLog(OperateFileLogParamBO paramBO) ;

}
