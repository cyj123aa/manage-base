package com.hoolink.manage.base.consumer.ability;

import com.hoolink.sdk.annotation.ClientException;
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

    /**
     * 从ability读取上传的固件文件
     * @return
     */
    public BackBO<byte[]> readFile(Long fileId) {
        BackBO<byte[]> backBO = restTemplate.postForObject("cse://hoolink-ability/obs/downloadBytes", fileId, BackBO.class);
        if(backBO == null || !backBO.getStatus() || backBO.getData() == null){
            throw new BusinessException(HoolinkExceptionMassageEnum.READ_FIRMWARE_FILE_ERROR);
        }
        return backBO;
    }

    /**
     * 从ability读取上传的固件文件信息
     * @return
     */
    @ClientException(message="访问OBS服务失败")
    public BackBO<ObsBO> getObs(Long fileId) {
        BackBO<ObsBO> backBO = restTemplate.postForObject("cse://hoolink-ability/obs/getObs", fileId, BackBO.class);
        if(backBO == null || !backBO.getStatus() || backBO.getData() == null){
            throw new BusinessException(HoolinkExceptionMassageEnum.READ_FIRMWARE_FILE_ERROR);
        }
        return backBO;
    }

    public BackBO sendMsg(SmsBO smsBO) {
        BackBO backBO = restTemplate.postForObject("cse://hoolink-ability/sms/sendMsg", smsBO, BackBO.class);
        if(backBO == null || !backBO.getStatus()){
            throw new BusinessException(HoolinkExceptionMassageEnum.PHONE_CODE_SEND_ERROR);
        }
        return backBO;
    }

    /**
     * 创建桶
     * @param bucketBO
     * @return
     */
    public BackBO<Long> createBucket(BucketBO bucketBO) {
        BackBO<Long> backBO = restTemplate.postForObject("cse://hoolink-ability/bucket/create", bucketBO, BackBO.class);
        if(backBO == null || !backBO.getStatus() || backBO.getData() == null){
            throw new BusinessException(HoolinkExceptionMassageEnum.CREATE_BUCKET_ERROR);
        }
        return backBO;
    }
}
