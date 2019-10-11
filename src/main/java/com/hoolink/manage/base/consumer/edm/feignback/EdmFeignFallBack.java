package com.hoolink.manage.base.consumer.edm.feignback;

import com.github.pagehelper.PageInfo;
import com.hoolink.manage.base.consumer.edm.EdmClient;
import com.hoolink.sdk.bo.BackBO;
import com.hoolink.sdk.bo.edm.OperateFileLogBO;
import com.hoolink.sdk.bo.edm.OperateFileLogParamBO;
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
    public BackBO<PageInfo<OperateFileLogBO>> listOperateLog(OperateFileLogParamBO paramBO) {
        return null;
    }
}
