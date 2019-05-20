package com.hoolink.manage.base.vo;

/**
 * @Author: xuli
 * @Date: 2019/4/18 16:25
 */
public interface ManagerBaseGroup {
    /**
     * 客户校验分组
     */
    interface CreateCustomer{}
    interface UpdateCustomer{}
    interface UpdateCustomerStatus{}
    interface UpdateCustomerMenu{}

    /**
     * 登录用户校验分组ManagerBaseGroup.forgetPassword
     */
    interface UserLogin{}
    interface ForgetPassword{}

    /**
     * 项目校验分组
     */
    interface createProject{}
    interface updateProject{}

    /**
     * 设备模型
     */
    interface UpdateDeviceModel{}
    interface CreateDeviceModel{}
    /**
     * 权限
     */
    interface UpdateAuth{}
    /**
     * 场景
     */
    interface ReadScene{}
    /**
     * 设备厂家
     */
    interface CreateDeviceFactory{}
    interface UpdateDeviceFactory{}
    /**
     * 固件校验分组
     */
    interface createFirmware{}
    interface updateFirmware{}

    /**
     * 设备升级校验分组
     */
    interface UpgradeDeviceList{}
    interface DeviceStartUpgrade{}
    interface FiltrateFirmware{}

    /**
     * 设备录入记录校验分组
     */
    interface UpdateDeviceEntryRecord{}
    interface DownloadExcel{}
    interface CreateDeviceEntryRecord{}

    /**
     *用户分组
     */
    interface CreateUser{}
    interface UpdateUser{}

}
