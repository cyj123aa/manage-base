package com.jw.manage.base.consumer.edm.feignback;

import com.jw.manage.base.consumer.edm.EdmClient;
import com.jw.sdk.bo.BackBO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author chenzhixiong
 * @date 2019/9/16 17:22
 */
@Component
@Slf4j
public class EdmFeignFallBack implements EdmClient {


    @Override
    public  BackBO<String>  edm() {
        return null;
    }


}
