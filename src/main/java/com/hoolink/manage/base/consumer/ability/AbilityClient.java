package com.hoolink.manage.base.consumer.ability;

import com.hoolink.sdk.bo.BackBO;
import com.hoolink.sdk.bo.ability.BucketBO;
import com.hoolink.sdk.bo.ability.ObsBO;
import com.hoolink.sdk.bo.ability.SmsBO;
import com.hoolink.sdk.exception.BusinessException;
import com.hoolink.sdk.exception.HoolinkExceptionMassageEnum;
import com.hoolink.sdk.param.BaseParam;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @Author : lys
 * @Date : 2019/4/23 17:01
 * @Instructions :
 */
@Component
public class AbilityClient {

    @Resource
    private RestTemplate restTemplate;


}
