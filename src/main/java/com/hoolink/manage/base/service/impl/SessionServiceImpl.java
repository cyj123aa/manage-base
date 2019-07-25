package com.hoolink.manage.base.service.impl;


import com.hoolink.manage.base.constant.RedisConstant;
import com.hoolink.manage.base.service.SessionService;
import com.hoolink.manage.base.util.Base64Util;
import com.hoolink.sdk.bo.base.CurrentUserBO;
import com.hoolink.sdk.constants.ContextConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.servicecomb.swagger.invocation.context.ContextUtils;
import org.apache.servicecomb.swagger.invocation.context.InvocationContext;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author zhangxin
 * @date 2019/4/19
 */
@Service
@Slf4j
public class SessionServiceImpl implements SessionService {

    @Resource(name = "redisTemplate")
    private ValueOperations<String, CurrentUserBO> sessionOperation;

    private static final String TOKEN_SEPARATOR = "_";
    private static final int TOKEN_SPLIT_COUNT = 2;
    private static final int SESSION_TIMEOUT_SECONDS = 60;
    private static final int SESSION_TIMEOUT_HOURS = 24;

    @Override
    public String cacheCurrentUser(CurrentUserBO currentUserBO,Boolean isMobile) {
        // token 格式：userId + "_" + uuid 在进行aes128加密
        UUID uuid=UUID.randomUUID();
        log.info("currentUserBO.getUserId():{},uuid:{}",currentUserBO.getUserId(),uuid);
        //因为linux解密错误这里不进行加密
        String token = Base64Util.encode(currentUserBO.getUserId() + TOKEN_SEPARATOR + uuid);
        log.info("token:"+token);
        if(isMobile){
            currentUserBO.setMobileToken(token);
            sessionOperation.set(getMobileKey(currentUserBO.getUserId()), currentUserBO, SESSION_TIMEOUT_HOURS, TimeUnit.HOURS);
        }else{
            currentUserBO.setToken(token);
            sessionOperation.set(getKey(currentUserBO.getUserId()), currentUserBO, SESSION_TIMEOUT_SECONDS, TimeUnit.MINUTES);
        }
        return token;
    }

    @Override
    public String cacheCurrentUserInfo(CurrentUserBO currentUserBO,Boolean isMobile) {
        String token;
        if(isMobile){
            CurrentUserBO userBO = sessionOperation.get(getMobileKey(currentUserBO.getUserId()));
            if(userBO==null){
                return null;
            }
            token=userBO.getToken();
            currentUserBO.setMobileToken(token);
            sessionOperation.set(getMobileKey(currentUserBO.getUserId()), currentUserBO, SESSION_TIMEOUT_HOURS, TimeUnit.HOURS);
        }else{
            CurrentUserBO userBO = sessionOperation.get(getKey(currentUserBO.getUserId()));
            if(userBO==null){
                return null;
            }
            token=userBO.getToken();
            currentUserBO.setToken(token);
            sessionOperation.set(getKey(currentUserBO.getUserId()), currentUserBO, SESSION_TIMEOUT_SECONDS, TimeUnit.MINUTES);
        }
        return token;
    }

    @Override
    public CurrentUserBO getCurrentUser(String token,boolean ismobile) {
        String decrypt = Base64Util.decode(token);
        if (decrypt == null) {
            return null;
        }
        String[] split = decrypt.split(TOKEN_SEPARATOR);
        if (split.length == TOKEN_SPLIT_COUNT) {
            Long userId = Long.valueOf(split[0]);
            if(ismobile){
                return sessionOperation.get(getMobileKey(userId));
            }else{
                return sessionOperation.get(getKey(userId));
            }
        }
        return null;
    }

    @Override
    public Boolean deleteRedisUser(List<Long> userIds){
        if(CollectionUtils.isEmpty(userIds)){
            return true;
        }
        List<String> key=new ArrayList<>();
        List<String> mobileKey=new ArrayList<>();
        for(Long id:userIds){
            key.add(getKey(id));
            mobileKey.add(getMobileKey(id));
        }
        try{
            sessionOperation.getOperations().delete(key);
            sessionOperation.getOperations().delete(mobileKey);
            return true;
        }catch (Exception e){
            log.error("删除用户缓存信息异常，userIds为：{}",userIds);
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean deleteRedisUser(Long userId){
        if(userId==null){
            return true;
        }
        try{
            sessionOperation.getOperations().delete(getKey(userId));
            sessionOperation.getOperations().delete(getMobileKey(userId));
            return true;
        }catch (Exception e){
            log.error("删除用户缓存信息异常，userid为：{}",userId);
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public Long getUserIdByToken() {
        InvocationContext context = ContextUtils.getInvocationContext();
        String token =context.getContext(ContextConstant.TOKEN);
        return getUserIdByToken(token);
    }

    @Override
    public Long getUserIdByMobileToken() {
        InvocationContext context = ContextUtils.getInvocationContext();
        String token =context.getContext(ContextConstant.MOBILE_TOKEN);
        return getUserIdByToken(token);
    }

    public Long getUserIdByToken(String token) {
        String decrypt = Base64Util.decode(token);
        if (decrypt == null) {
            return null;
        }
        String[] split = decrypt.split(TOKEN_SEPARATOR);
        if (split.length == TOKEN_SPLIT_COUNT) {
            Long userId = Long.valueOf(split[0]);
            return userId;
        }
        return null;
    }

    @Override
    public CurrentUserBO getCurrentUser(Long userId) {
        return sessionOperation.get(getKey(userId));
    }

    @Override
    public CurrentUserBO getMobileCurrentUser(Long userId) {
        return sessionOperation.get(getMobileKey(userId));
    }

    public CurrentUserBO getCurrentUser(String token) {
        if(getUserIdByToken(token)==null){
            return null;
        }
        return sessionOperation.get(getKey(getUserIdByToken(token)));
    }

    @Override
    public Boolean refreshSession(Long userId,boolean isMobile) {
        if(isMobile){
            return sessionOperation.getOperations().expire(getMobileKey(userId), SESSION_TIMEOUT_HOURS, TimeUnit.HOURS);
        }
        return sessionOperation.getOperations().expire(getKey(userId), SESSION_TIMEOUT_SECONDS, TimeUnit.MINUTES);
    }

    @Override
    public Boolean deleteSession(Long userId) {
        return sessionOperation.getOperations().delete(getKey(userId));
    }

    @Override
    public Boolean deleteMobileSession(Long userId) {
        return sessionOperation.getOperations().delete(getMobileKey(userId));
    }

    @Override
    public Boolean deleteSession(String token) {
        CurrentUserBO currentUserBO = getCurrentUser(token);
        if (currentUserBO != null) {
            return deleteSession(currentUserBO.getUserId());
        }
        return false;
    }

    private String getKey(Long userId) {
        return RedisConstant.SESSION_PREFIX + userId;
    }

    private String getMobileKey(Long userId) {
        return RedisConstant.SESSION_MOBILE_PREFIX + userId;
    }
}
