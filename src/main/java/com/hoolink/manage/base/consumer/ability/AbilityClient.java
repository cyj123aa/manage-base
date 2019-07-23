package com.hoolink.manage.base.consumer.ability;

import com.hoolink.sdk.annotation.ClientException;
import com.hoolink.sdk.bo.BackBO;
import com.hoolink.sdk.bo.ability.BucketBO;
import com.hoolink.sdk.bo.ability.ObsBO;
import com.hoolink.sdk.bo.ability.SmsBO;
import com.hoolink.sdk.exception.BusinessException;
import com.hoolink.sdk.exception.HoolinkExceptionMassageEnum;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author : lys
 * @Date : 2019/4/23 17:01
 * @Instructions :
 */
@Slf4j
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
        return restTemplate.postForObject("cse://hoolink-ability/obs/getObs", fileId, BackBO.class);
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
    
    /**
     * 调用OBS上传文件
     * @param multipartFileBO
     * @return
     */
    @ClientException(message="调用OBS上传文件失败")
    public BackBO<ObsBO> uploadManager(MultipartFile multipartFile) {
        InputStream in = null;
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
		try {
			in = multipartFile.getInputStream();
	        byte[] buff = new byte[1024];
	        int bytesRead = 0;
	        while ((bytesRead = in.read(buff)) != -1) {
	          bao.write(buff, 0, bytesRead);
	        }
			ByteArrayResource arrayResource = new ByteArrayResource(bao.toByteArray()){ 
				@Override
				public String getFilename() throws IllegalStateException { 
					return multipartFile.getOriginalFilename();
				}
			}; 
			
	    	Map<String, Object> map = new HashMap<>(1);
	    	map.put("file", arrayResource);
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.setContentType(org.springframework.http.MediaType.MULTIPART_FORM_DATA);
	    	HttpEntity<Map<String, Object>> entry = new HttpEntity<>(map, headers);
	        return restTemplate.postForObject("cse://hoolink-ability/obs/uploadManager", entry, BackBO.class);
		} catch (IOException e) {
			log.error("调用OBS上传文件失败");
			throw new BusinessException("调用OBS上传文件失败");
		}finally {
			try {
				if(in != null) {
					in.close();
				}
				bao.close();
			} catch (IOException e) {
				log.error("调用OBS上传文件失败");
				throw new BusinessException("调用OBS上传文件失败");
			}
		}


    }
}
