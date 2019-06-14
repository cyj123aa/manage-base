package com.hoolink.manage.base.vo.res;

import lombok.Data;

/**
 * 个人中心-基础信息
 * @author lijunling
 *
 * @date 2019/05/24 11:51
 */
@Data
public class PersonalInfoVO extends ManagerUserVO{
	/**
     * 用户头像id
     */
    private Long imgId;
    
    /**
     * 用户头像url
     */
    private String imgUrl;
}
