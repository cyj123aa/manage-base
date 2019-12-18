package com.hoolink.manage.base.consumer.ability;

import com.hoolink.manage.base.consumer.ability.feignback.AbilityFeignFallBack;
import com.jw.sdk.annotation.ClientException;
import com.jw.sdk.bo.BackBO;
import com.jw.sdk.bo.ability.BucketBO;
import com.jw.sdk.bo.ability.ObsBO;
import com.jw.sdk.bo.ability.SmsBO;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author : lys
 * @Date : 2019/4/23 17:01
 * @Instructions :
 */
@FeignClient(value = "hoolink-ability",fallback = AbilityFeignFallBack.class)
@Component
public interface AbilityClient {

    /**
     * 从ability读取上传的固件文件
     * @return
     */
    @RequestMapping(value = "/obs/downloadBytes",method= RequestMethod.POST)
    BackBO<byte[]> readFile(Long fileId) ;

    /**
     * 从ability读取上传的固件文件信息
     * @return
     */
    @ClientException(message="访问OBS服务失败")
    @RequestMapping(value = "/obs/getObs",method= RequestMethod.POST)
    BackBO<ObsBO> getObs(Long fileId) ;

    @RequestMapping(value = "/sms/sendMsg",method= RequestMethod.POST)
    BackBO sendMsg(SmsBO smsBO) ;

    /**
     * 创建桶
     * @param bucketBO
     * @return
     */
    @RequestMapping(value = "/bucket/create",method= RequestMethod.POST)
    BackBO<Long> createBucket(BucketBO bucketBO) ;
    
    /**
     * 调用OBS上传文件
     * @param entry
     * @return
     */
    @RequestMapping(value = "/obs/uploadManager",method= RequestMethod.POST)
    @ClientException(message="调用OBS上传文件失败")
    BackBO<ObsBO> uploadManager(HttpEntity<Map<String, Object>> entry) ;

}
