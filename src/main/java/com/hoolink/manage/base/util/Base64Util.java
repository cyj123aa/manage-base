package com.hoolink.manage.base.util;

import java.io.UnsupportedEncodingException;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;



/**
 * @Author: xuli
 * @Date: 2019/5/8 9:53
 */
@Slf4j
public class Base64Util {

    private static final String CHARSET = "utf-8";
    /**
     * 解密
     *
     * @param data
     * @return
     * @author jqlin
     */
    public static String decode(String data) {
        try {
            if (null == data) {
                return null;
            }
            return new String(Base64.decodeBase64(data.getBytes(CHARSET)), CHARSET);
        } catch (UnsupportedEncodingException e) {
            log.error(String.format("字符串：%s，解密异常", data), e);
        }
        return null;
    }

    /**
     * 加密
     *
     * @param data
     * @return
     * @author jqlin
     */
    public static String encode(String data) {
        try {
            if (null == data) {
                return null;
            }
            return new String(Base64.encodeBase64(data.getBytes(CHARSET)), CHARSET);
        } catch (UnsupportedEncodingException e) {
            log.error(String.format("字符串：%s，加密异常", data), e);
        }
        return null;
    }
}
