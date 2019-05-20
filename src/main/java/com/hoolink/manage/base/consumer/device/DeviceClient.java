package com.hoolink.manage.base.consumer.device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author chenzhixiong
 * @date 2019/4/18 11:08
 */
@Component
public class DeviceClient {
    @Autowired
    private RestTemplate restTemplate;


}


