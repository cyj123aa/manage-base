package com.hoolink.manage.base.bo;

import lombok.Data;

import java.util.List;

/**
 * @author : chenzb
 * @Description : TODO
 * @date : Created on 2019/5/29 19:16
 */
@Data
public class OrganizationBO {

    /*** 组织架构id */
    private Long id;

    /*** 组织架构名称 */
    private String name;

    /*** 组织架构名称 */
    private Byte deptType;

    /*** 组织架构的父id */
    private Long parentId;

    /*** 组织架构的子节点 */
    private List<OrganizationBO> child;
}
