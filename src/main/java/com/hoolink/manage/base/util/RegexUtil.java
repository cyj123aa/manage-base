package com.hoolink.manage.base.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: xuli
 * @Date: 2019/7/24 14:51
 */
@Component
public class RegexUtil {

    private static final String PHONE_REGEX="(^1((3[0-9]|4[5-8]|5[0-35-9]|6[67]|7[015-8]|8[0-9]|9[189])\\d{8}$))|(^1(349|4[14]0|740)\\d{7}$)";

    public boolean matchPhone(String phone){
        if(StringUtils.isBlank(phone)){
            return false;
        }
        Pattern p=Pattern.compile(PHONE_REGEX);
        Matcher m=p.matcher(phone);
        if(m.matches()){
            return true;
        }else{
            return false;
        }
    }
}
