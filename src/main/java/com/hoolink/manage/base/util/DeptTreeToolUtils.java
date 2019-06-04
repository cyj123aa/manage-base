package com.hoolink.manage.base.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hoolink.sdk.bo.manager.ManageDepartmentTreeBO;
import com.hoolink.sdk.bo.manager.SimpleDeptUserBO;
import com.hoolink.manage.base.service.UserService;
import org.apache.commons.collections.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author: tonghao
 * @Date: 2019/5/29 19:57
 */
public class DeptTreeToolUtils {

    @Resource
    private UserService userService;

    /** 根节点对象存放到这里 */
    private List<ManageDepartmentTreeBO> rootList;

    /** 其他节点存放到这里，可以包含根节点 */
    private List<ManageDepartmentTreeBO> bodyList;

    public DeptTreeToolUtils(List<ManageDepartmentTreeBO> rootList, List<ManageDepartmentTreeBO> bodyList) {
        this.rootList = rootList;
        this.bodyList = bodyList;
    }

    public List<ManageDepartmentTreeBO> getTree(Boolean flag, Map<Long, List<SimpleDeptUserBO>> userMap) {
        //调用的方法入口
        if (bodyList != null && !bodyList.isEmpty()) {
            //声明一个map，用来过滤已操作过的数据
            Map<Long, Long> map = Maps.newHashMapWithExpectedSize(bodyList.size());
            rootList.forEach(beanTree -> getChild(beanTree, map, flag, userMap));
            return rootList;
        }
        return null;
    }

    public void getChild(ManageDepartmentTreeBO treeDto, Map<Long, Long> map, Boolean flag, Map<Long, List<SimpleDeptUserBO>> userMap) {
        List<ManageDepartmentTreeBO> childList = Lists.newArrayList();
        bodyList.stream()
                .filter(c -> !map.containsKey(c.getKey()))
                .filter(c -> c.getParentId().equals(treeDto.getKey()))
                .forEach(c -> {
                    map.put(c.getKey(), c.getParentId());
                    getChild(c, map, flag, userMap);
                    childList.add(c);
                    Boolean hasAddUser = CollectionUtils.isEmpty(c.getChildren()) && flag && (userMap != null && !userMap.isEmpty());
                    if (hasAddUser) {
                        c.setUserList(userMap.get(c.getKey()));
                    }
                });
        treeDto.setChildren(childList);

    }
}
