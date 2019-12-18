package com.hoolink.manage.base.service.impl;

import com.hoolink.manage.base.bo.LoginParamBO;
import com.hoolink.manage.base.bo.LoginResultBO;
import com.hoolink.manage.base.bo.ManageRoleBO;
import com.hoolink.manage.base.bo.ManagerUserParamBO;
import com.hoolink.manage.base.bo.MiddleUserDeptWithMoreBO;
import com.hoolink.manage.base.dao.model.User;
import com.hoolink.manage.base.service.UserService;
import com.jw.sdk.bo.base.CurrentUserBO;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: xuli
 * @Date: 2019/4/15 19:24
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {


    @Override
    public LoginResultBO login(LoginParamBO loginParam, Boolean isMobile) throws Exception {
        return null;
    }

    @Override
    public void logout() {

    }

    @Override
    public void mobileLogout() {

    }

    @Override
    public CurrentUserBO getSessionUser(String token, boolean isMobile) {
        return null;
    }

    @Override
    public void createUser(ManagerUserParamBO userBO) throws Exception {

    }

    @Override
    public String cacheSession(User user, Boolean isMobile, Boolean resetToken) throws Exception {
    return null;
    }
}
