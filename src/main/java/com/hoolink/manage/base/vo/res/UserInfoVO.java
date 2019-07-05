package com.hoolink.manage.base.vo.res;

import com.hoolink.sdk.bo.edm.RepertoryBO;
import lombok.Data;

import java.util.List;

/**
 * @Author: xuli
 * @Date: 2019/4/29 17:11
 */
@Data
public class UserInfoVO {

    /**
     * 姓名
     */
    private String  userName;

    /**
     * 电话
     */
    private String phone;

    /**
     * 角色
     */
    private String roleName;

    /**
     * 图像url
     */
    private String image;

    /**
     * edm库的权限，1 部门资源，2 缓存库，3 资源库
     */
    private List<RepertoryBO> edmRepertory;

    private Byte roleLevel;
}
