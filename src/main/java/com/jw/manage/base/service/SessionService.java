package com.jw.manage.base.service;

import com.jw.sdk.bo.base.CurrentUserBO;

import java.util.List;

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
     * @param isMobile
     * @return
     */
    String cacheCurrentUser(CurrentUserBO currentUserBO,Boolean isMobile);

    /**
     * 缓存当前用户信息 不更新token
     * @param currentUserBO
     * @param isMobile
     * @return
     */
    String cacheCurrentUserInfo(CurrentUserBO currentUserBO,Boolean isMobile);

    /**
     * 缓存当前用户web端和手机端，不更新token
     * @param currentUserBO
     * @return
     */
    void cacheCurrentUserInfo(CurrentUserBO currentUserBO);

    /**
     * 根据传入token 获取用户最新的用户信息
     * 对于异地登录场景，传入的token会和user中的token不一致
     * @param token
     * @param ismobile
     * @return
     */
    CurrentUserBO getCurrentUser(String token,boolean ismobile);

    /**
     * 根据token获取userid
     * @return
     */
    Long getUserIdByToken();

    /**
     * 根据用户idlist删除用户缓存信息
     * @param userIds
     * @return
     */
    Boolean deleteRedisUser(List<Long> userIds);

    /**
     * 根据用户id删除缓存信息
     * @param userId
     * @return
     */
    Boolean deleteRedisUser(Long userId);

    /**
     * 根据移动端token获取userid
     * @return
     */
    Long getUserIdByMobileToken();

    /**
     * 根据用户ID查询当前用户信息
     *
     * @param userId
     * @return
     */
    CurrentUserBO getCurrentUser(Long userId);

    /**
     * 根据用户id查询移动端的用户信息
     * @param userId
     * @return
     */
    CurrentUserBO getMobileCurrentUser(Long userId);

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
     * 删除移动端用户及其token
     * @param userId
     * @return
     */
    Boolean deleteMobileSession(Long userId);

    /**
     * 通过token进行删除用户
     * @param token
     * @return
     */
    Boolean deleteSession(String token);
}
