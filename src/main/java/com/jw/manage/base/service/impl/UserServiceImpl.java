package com.jw.manage.base.service.impl;

import com.jw.manage.base.bo.LoginParamBO;
import com.jw.manage.base.bo.LoginResultBO;
import com.jw.manage.base.constant.Constant;
import com.jw.manage.base.consumer.edm.EdmClient;
import com.jw.manage.base.dao.model.User;
import com.jw.manage.base.service.SessionService;
import com.jw.manage.base.service.UserService;
import com.jw.sdk.bo.base.CurrentUserBO;
import com.jw.sdk.exception.BusinessException;
import com.jw.sdk.exception.ExceptionMassageEnum;
import com.jw.sdk.utils.ContextUtil;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: chenyuejun
 * @Date: 2019/4/15 19:24
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private SessionService sessionService;
    @Autowired
    private EdmClient edmClient;
    @Override
    public LoginResultBO login(LoginParamBO loginParam, Boolean isMobile) throws Exception {
        User user=new User();
        user.setId(1L);
        user.setStatus(true);
        user.setName("陈岳军");
        user.setPasswd("123456");
        user.setUserAccount("chenyuejun");
        // 检查用户是否被禁用，角色是否被禁用
        checkAccount(user);
        // 缓存当前用户
        String token = cacheSession(user, isMobile, true);
        LoginResultBO loginResult = new LoginResultBO();
        loginResult.setToken(token);
        loginResult.setFirstLogin(user.getFirstLogin());
        loginResult.setPhone(user.getPhone());
        loginResult.setName(user.getName());
        //设置问候语
        loginResult.setGreetings(setGreeting());

        return loginResult;
    }

    @Override
    public void logout() {
        CurrentUserBO currentUser = sessionService.getCurrentUser(sessionService.getUserIdByToken());
//        InvocationContext context = ContextUtils.getInvocationContext();
//        String token = context.getContext(ContextConstant.TOKEN);
        String token = null;
        // 避免导致异地登录账号退出
        if (currentUser != null && Objects.equals(currentUser.getToken(), token)) {
            sessionService.deleteSession(currentUser.getUserId());
        }
    }

    @Override
    public void mobileLogout() {
        CurrentUserBO currentUser = sessionService.getMobileCurrentUser(sessionService.getUserIdByMobileToken());
//        InvocationContext context = ContextUtils.getInvocationContext();
//        String token = context.getContext(ContextConstant.MOBILE_TOKEN);
        // 避免导致异地登录账号退出
        String token = null;
        if (currentUser != null && Objects.equals(currentUser.getMobileToken(), token)) {
            sessionService.deleteMobileSession(currentUser.getUserId());
        }
    }

    @Override
    public CurrentUserBO getSessionUser(String token, boolean isMobile) {
        // 获取当前 session 中的用户
        CurrentUserBO currentUser = sessionService.getCurrentUser(token, isMobile);
        if (currentUser == null) {
            return null;
        }
        // 刷新 session 失效时间
        if (!sessionService.refreshSession(currentUser.getUserId(), isMobile)) {
            return null;
        }
        return currentUser;
    }



    @Override
    public String cacheSession(User user, Boolean isMobile, Boolean resetToken) throws Exception {
        CurrentUserBO currentUserBO = new CurrentUserBO();
        currentUserBO.setUserId(user.getId());
        currentUserBO.setAccount(user.getUserAccount());
        currentUserBO.setUserName(URLEncoder.encode(user.getName(), "utf-8"));
        //设置角色id
        currentUserBO.setRoleId(user.getRoleId());

        //设置权限属性 url   有a b 两种角色
        Set<String> role=new HashSet<>();
        role.add("a");
        role.add("b");
        currentUserBO.setAccessUrlSet(role);
        //设置角色类型

        if (resetToken) {
            //更新token
            return sessionService.cacheCurrentUser(currentUserBO, isMobile);
        } else {
            //维持用户原token
            return sessionService.cacheCurrentUserInfo(currentUserBO, isMobile);
        }
    }


    @Override
    public String jiuWenTest(){
     log.info("come id={},user={}", ContextUtil.getManageCurrentUser().getUserId(),ContextUtil.getManageCurrentUser().getAccount());
        return   edmClient.edm().getData();
    }

    private String setGreeting() {
        Date date = new Date();
        int six = 6;
        int twelve = 12;
        int thirteen = 13;
        int eighteen = 18;
        int twentyFour = 24;
        SimpleDateFormat df = new SimpleDateFormat("HH");
        String str = df.format(date);
        int a = Integer.parseInt(str);
        if (a >= 0 && a <= six) {
            return Constant.GREETING_MORNING;
        }
        if (a > six && a <= twelve) {
            return Constant.GREETING_FORENOON;
        }
        if (a > twelve && a <= thirteen) {
            return Constant.GREETING_NOON;
        }
        if (a > thirteen && a <= eighteen) {
            return Constant.GREETING_AFTERNOON;
        }
        if (a > eighteen && a <= twentyFour) {
            return Constant.GREETING_NIGHT;
        }
        return Constant.GREETING_YOU;
    }

    /**
     *     用户检查
     */

    private void checkAccount(User user) {
        if (user == null) {
            throw new BusinessException(ExceptionMassageEnum.USER_ACCOUNT_OR_PASSWORD_ERROR);
        }

        // 用户被禁用
        if (user.getStatus() == null || !user.getStatus()) {
            throw new BusinessException(ExceptionMassageEnum.USER_FORBIDDEN);
        }
    }
}
