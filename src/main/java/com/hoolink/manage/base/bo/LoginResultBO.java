package com.hoolink.manage.base.bo;

import com.hoolink.sdk.bo.edm.RepertoryBO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xuli
 * @Date: 2019/4/23 17:20
 */
@Data
public class LoginResultBO implements Serializable {

    private static final long serialVersionUID = 410702869003930490L;
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
    private Boolean accessEDM;
    /**
     * hoolink系统权限
     */
    private Boolean accessHoolink;

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
}
