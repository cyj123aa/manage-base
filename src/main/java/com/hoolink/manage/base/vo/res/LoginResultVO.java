package com.hoolink.manage.base.vo.res;

import com.hoolink.sdk.bo.edm.RepertoryBO;
import lombok.Data;

import java.util.List;

/**
 * @Author: xuli
 * @Date: 2019/4/23 17:41
 */
@Data
public class LoginResultVO {

    /**
     * 用户token
     */
    private String token;
    /**
     * 是否是第一次登录
     */
    private Boolean firstLogin;
    /**
     * 手机号
     */
    private String phone;

    /**
     * EDM系统权限
     */
    private Boolean accessEDM = false;
    /**
     * hoolink系统权限
     */
    private Boolean accessHoolink = false;

    /**
     * 问候语
     */
    private String greetings;

    /**
     * 用户名
     */
    private String name;

    /**
     * 图像url
     */
    private String image;

    /**
     * edm库的权限，1 部门资源，2 缓存库，3 资源库
     */
    private List<RepertoryBO> edmRepertory;

    private List<RepertoryBO> repertoryList;

    /**
     * 角色名称
     */
    private String roleName;

    private Byte roleLevel;
}
