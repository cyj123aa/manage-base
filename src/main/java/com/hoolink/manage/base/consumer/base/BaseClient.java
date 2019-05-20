package com.hoolink.manage.base.consumer.base;

import com.github.pagehelper.PageInfo;
import com.hoolink.sdk.bo.BackBO;
import com.hoolink.sdk.bo.base.CreateUserBO;
import com.hoolink.sdk.bo.base.CustomerMenuBO;
import com.hoolink.sdk.bo.base.CustomerProjectBO;
import com.hoolink.sdk.bo.base.DeviceTypeOfProjectBO;
import com.hoolink.sdk.bo.base.IdProjectBO;
import com.hoolink.sdk.bo.base.ListProjectBO;
import com.hoolink.sdk.bo.base.ListSceneBO;
import com.hoolink.sdk.bo.base.MaintainBO;
import com.hoolink.sdk.bo.base.ProjectAuthParamBO;
import com.hoolink.sdk.bo.base.ProjectBO;
import com.hoolink.sdk.bo.base.ProjectCustomerParamBO;
import com.hoolink.sdk.bo.base.ProjectDeviceTypeParamBO;
import com.hoolink.sdk.bo.base.ProjectMenuParamBO;
import com.hoolink.sdk.bo.base.ProjectMenuReturnBO;
import com.hoolink.sdk.bo.base.ProjectMenuTreeBO;
import com.hoolink.sdk.bo.base.SearchMainteBO;
import com.hoolink.sdk.bo.base.SearchProjectBO;
import com.hoolink.sdk.bo.base.SearchUserBO;
import com.hoolink.sdk.bo.base.UpdateCustomerAuthBO;
import com.hoolink.sdk.bo.base.UpdateDeviceTypeBO;
import com.hoolink.sdk.bo.base.UpdateStatusBO;
import com.hoolink.sdk.bo.base.UpdateUserBO;
import com.hoolink.sdk.bo.base.UserBO;
import com.hoolink.sdk.exception.BusinessException;
import com.hoolink.sdk.exception.HoolinkExceptionMassageEnum;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author chenzhixiong
 * @date 2019/4/16 14:48
 */
@Component
public class BaseClient {
    @Autowired
    private RestTemplate restTemplate;


}
