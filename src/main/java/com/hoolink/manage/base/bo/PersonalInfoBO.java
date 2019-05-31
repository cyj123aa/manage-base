package com.hoolink.manage.base.bo;

import com.hoolink.sdk.bo.manager.ManagerUserBO;

import lombok.Data;

/**
 * 个人中心-基础信息
 * @author lijunling
 *
 * @date 2019/05/24 11:51
 */
@Data
public class PersonalInfoBO extends ManagerUserBO{
	/**
     * 用户头像id
     */
    private Long imgId;
    
    /**
     * 用户头像url
     */
    private String imgUrl;
}
