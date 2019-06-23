package com.hoolink.manage.base.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hoolink.manage.base.constant.Constant;
import com.hoolink.sdk.bo.edm.CheckedParamBO;
import com.hoolink.sdk.bo.edm.DepartmentAndUserTreeBO;
import com.hoolink.sdk.bo.manager.SimpleDeptUserBO;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: tonghao
 * @Date: 2019/5/29 19:57
 */
public class DeptTreeToolUtils {

    /** 根节点对象存放到这里 */
    private List<DepartmentAndUserTreeBO> rootList;

    /** 其他节点存放到这里，可以包含根节点 */
    private List<DepartmentAndUserTreeBO> bodyList;

    public DeptTreeToolUtils(List<DepartmentAndUserTreeBO> rootList, List<DepartmentAndUserTreeBO> bodyList) {
        this.rootList = rootList;
        this.bodyList = bodyList;
    }

    public List<DepartmentAndUserTreeBO> getTree(Boolean flag, Map<Long, List<SimpleDeptUserBO>> userMap, List<CheckedParamBO> checkedList) {
        //调用的方法入口
        if (bodyList != null && !bodyList.isEmpty()) {
            //声明一个map，用来过滤已操作过的数据
            Map<Long, Long> map = Maps.newHashMapWithExpectedSize(bodyList.size());
            Boolean hasAddUser =  flag && (userMap != null && !userMap.isEmpty());
            //顶层节点用户
            List<DepartmentAndUserTreeBO> rootUserList = new ArrayList<>();
            rootList.forEach(beanTree -> {
                getChild(beanTree, map, hasAddUser, userMap, checkedList);
            });
            rootList.addAll(rootUserList);
            return rootList;
        }
        return null;
    }

    private void buildUserList(List<DepartmentAndUserTreeBO> rootUserList, DepartmentAndUserTreeBO beanTree, List<SimpleDeptUserBO> userBOList, List<CheckedParamBO> checkedUserList) {
        for (SimpleDeptUserBO userBO : userBOList) {
            DepartmentAndUserTreeBO treeBO = new DepartmentAndUserTreeBO();
            treeBO.setKey(userBO.getId());
            treeBO.setTitle(userBO.getUserName());
            treeBO.setValue(userBO.getId());
            treeBO.setType(Constant.USER);
            treeBO.setDepartId(beanTree.getKey());
            treeBO.setChecked(Boolean.FALSE);
            treeBO.setExpand(Boolean.FALSE);
            if (CollectionUtils.isNotEmpty(checkedUserList)){
                for (CheckedParamBO userParamBO : checkedUserList){
                    if (userParamBO.getId().equals(userBO.getId()) && userParamBO.getDepartId().equals(beanTree.getKey())){
                        treeBO.setChecked(Boolean.TRUE);
                    }
                }
            }
            rootUserList.add(treeBO);
        }
    }

    public void getChild(DepartmentAndUserTreeBO treeDto, Map<Long, Long> map, Boolean hasAddUser, Map<Long, List<SimpleDeptUserBO>> userMap, List<CheckedParamBO> checkedList) {
        List<DepartmentAndUserTreeBO> childList = Lists.newArrayList();
        bodyList.stream()
                .filter(c -> !map.containsKey(c.getKey()))
                .filter(c -> c.getParentId().equals(treeDto.getKey()))
                .forEach(c -> {
                    map.put(c.getKey(), c.getParentId());
                    getChild(c, map, hasAddUser, userMap, checkedList);
                    //子集
                    if (hasAddUser) {
                        if (CollectionUtils.isNotEmpty(userMap.get(c.getKey()))) {
                            List<DepartmentAndUserTreeBO> children = new ArrayList<>();
                            //封装员工数据
                            buildUserList(children, c, userMap.get(c.getKey()), checkedList);
                            if (CollectionUtils.isEmpty(c.getChildren())) {
                                c.setChildren(children);
                            } else {
                                //同级
                                childList.addAll(children);
                            }
                        }
                    }else {
                        if (CollectionUtils.isNotEmpty(checkedList)){
                            //勾选组织架构
                            for (CheckedParamBO paramBO : checkedList){
                                if (c.getKey().equals(paramBO.getDepartId())){
                                    c.setChecked(Boolean.TRUE);
                                }
                            }
                        }
                    }
                    childList.add(c);
                });
        treeDto.setChildren(childList);
    }
}
