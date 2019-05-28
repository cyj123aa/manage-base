package com.hoolink.manage.base.vo.res;

import com.hoolink.manage.base.bo.ManageMenuTreeBO;
import com.hoolink.sdk.bo.base.CurrencyMenuBO;
import com.hoolink.sdk.bo.base.ProjectMenuBO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author XuBaofeng.
 * @date 2019-04-29 10:21.
 * <p>
 * description:
 */
@Data
public class RoleMenuVO implements Serializable {
    private static final long serialVersionUID = -1365973487574009276L;

    /**
     * 已选
     */
    private List<ManageMenuTreeVO> chooseMenu;
    /**
     * 全部
     */
    private List<ManageMenuTreeVO> manageMenu;
}
