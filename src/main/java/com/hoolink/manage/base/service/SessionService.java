package com.hoolink.manage.base.service;

import com.hoolink.sdk.bo.base.CurrentUserBO;

/**
 * token 相关接口
 *
 * @author zhangxin
 * @date 2019/4/19
 */
public interface SessionService {

    /**
     * 缓存当前用户信息，将token返回出去
     * @param currentUserBO
     * @return
     */
    String cacheCurrentUser(CurrentUserBO currentUserBO);

    /**
     * 根据传入token 获取用户最新的用户信息
     * 对于异地登录场景，传入的token会和user中的token不一致
     *
     * @param token
     * @return
     */
    CurrentUserBO getCurrentUser(String token);

    /**
     * 根据用户ID查询当前用户信息
     *
     * @param userId
     * @return
     */
    CurrentUserBO getCurrentUser(Long userId);

    /**
     * 刷新 token 失效时间
     * @param userId
     * @param ismobile
     * @return
     */
    Boolean refreshSession(Long userId,boolean ismobile);

    /**
     * 删除用户及其token
     * @param userId
     * @return
     */
    Boolean deleteSession(Long userId);

    /**
     * 通过token进行删除用户
     * @param token
     * @return
     */
    Boolean deleteSession(String token);
}
