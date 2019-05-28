package com.hoolink.manage.base.bo;
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
public class RoleMenuBO implements Serializable {
    private static final long serialVersionUID = -1365973487574009276L;

    /**
     * 已选
     */
    private List<ManageMenuTreeBO> chooseMenu;
    /**
     * 全部
     */
    private List<ManageMenuTreeBO> manageMenu;
}
