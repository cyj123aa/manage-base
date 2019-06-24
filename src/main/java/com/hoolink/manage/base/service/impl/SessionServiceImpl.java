package com.hoolink.manage.base.service.impl;


import com.hoolink.manage.base.constant.RedisConstant;
import com.hoolink.manage.base.service.SessionService;
import com.hoolink.manage.base.util.Base64Util;
import com.hoolink.sdk.bo.base.CurrentUserBO;
import com.hoolink.sdk.constants.ContextConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.servicecomb.swagger.invocation.context.ContextUtils;
import org.apache.servicecomb.swagger.invocation.context.InvocationContext;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
    public String cacheCurrentUser(CurrentUserBO currentUserBO) {
        // token 格式：userId + "_" + uuid 在进行aes128加密
        UUID uuid=UUID.randomUUID();
        log.info("currentUserBO.getUserId():{},uuid:{}",currentUserBO.getUserId(),uuid);
        //因为linux解密错误这里不进行加密
        String token = Base64Util.encode(currentUserBO.getUserId() + TOKEN_SEPARATOR + uuid);
        log.info("token:"+token);
        currentUserBO.setToken(token);
        sessionOperation.set(getKey(currentUserBO.getUserId()), currentUserBO, SESSION_TIMEOUT_SECONDS, TimeUnit.MINUTES);
        return token;
    }

    @Override
    public CurrentUserBO getCurrentUser(String token) {
        String decrypt = Base64Util.decode(token);
        if (decrypt == null) {
            return null;
        }
        String[] split = decrypt.split(TOKEN_SEPARATOR);
        if (split.length == TOKEN_SPLIT_COUNT) {
            Long userId = Long.valueOf(split[0]);
            return sessionOperation.get(getKey(userId));
        }
        return null;
    }

    @Override
    public Long getUserIdByToken() {
        InvocationContext context = ContextUtils.getInvocationContext();
        String token =context.getContext(ContextConstant.TOKEN);
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
    public Boolean refreshSession(Long userId,boolean isMobile) {
        if(isMobile){
            return sessionOperation.getOperations().expire(getKey(userId), SESSION_TIMEOUT_HOURS, TimeUnit.HOURS);
        }
        return sessionOperation.getOperations().expire(getKey(userId), SESSION_TIMEOUT_SECONDS, TimeUnit.MINUTES);
    }

    @Override
    public Boolean deleteSession(Long userId) {
        return sessionOperation.getOperations().delete(getKey(userId));
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
}
