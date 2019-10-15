package com.hoolink.manage.base.util;

import com.hoolink.sdk.constants.CommonConstants;
import com.hoolink.sdk.constants.UserConstant;
import com.hoolink.sdk.utils.ContextUtil;
import com.hoolink.sdk.utils.PasswordUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

/**
 * 用户相关工具类
 *
 * @author zhangxin
 * @date 2019/4/16
 */
public class UserUtil {

    /**
     * 默认的用户密码
     */
    private static final byte[] USER_DEFAULT_PASSWORD = "1+iot123456".getBytes();

    /**
     * 获取默认的用户加密密码
     *
     * @return
     */
    public static String getDefaultPassword() {
        String password = DigestUtils.md5DigestAsHex(USER_DEFAULT_PASSWORD);
        return PasswordUtil.encryptionPassword(password);
    }

    public static String getMD5Password(String password) {
        if (StringUtils.isBlank(password)) {
            return "";
        }
        return PasswordUtil.encryptionPassword(password);
    }

    /**
     * 是否超级管理员用户
     */
    public static boolean isSuperAdmin() {
        return CommonConstants.DEFAULT_USER_ID.equals(ContextUtil.getCurrentUser().getUserId());
    }

    /**
     * 是否不是超级管理员
     */
    public static boolean isNotSuperAdmin() {
        return !isSuperAdmin();
    }

    /**
     * 是否管理员
     */
    public static boolean isAdmin() {
        return UserConstant.USER_TYPE_ADMIN == ContextUtil.getCurrentUser().getUserType();
    }

    /**
     * 是否不是管理员
     */
    public static boolean isNotAdmin() {
        return !isAdmin();
    }

}
