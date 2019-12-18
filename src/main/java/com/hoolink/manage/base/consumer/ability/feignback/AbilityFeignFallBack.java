package com.hoolink.manage.base.consumer.ability.feignback;

import com.hoolink.manage.base.consumer.ability.AbilityClient;
import com.jw.sdk.bo.BackBO;

import com.jw.sdk.bo.ability.BucketBO;
import com.jw.sdk.bo.ability.ObsBO;
import com.jw.sdk.bo.ability.SmsBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author chenzhixiong
 * @date 2019/9/16 17:22
 */
@Component
@Slf4j
public class AbilityFeignFallBack implements AbilityClient {


    @Override
    public BackBO<byte[]> readFile(Long fileId) {
        return null;
    }

    @Override
    public BackBO<Long> createBucket(BucketBO bucketBO) {
        return null;
    }

    @Override
    public BackBO sendMsg(SmsBO smsBO) {
        return null;
    }

    @Override
    public BackBO<ObsBO> getObs(Long fileId) {
        return null;
    }

    @Override
    public BackBO<ObsBO> uploadManager(HttpEntity<Map<String, Object>> entry) {
        return null;
    }
}
