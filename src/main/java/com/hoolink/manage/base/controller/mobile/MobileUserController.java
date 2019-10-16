package com.hoolink.manage.base.controller.mobile;

import com.github.pagehelper.PageInfo;
import com.hoolink.manage.base.bo.*;
import com.hoolink.manage.base.service.ExcelService;
import com.hoolink.manage.base.service.UserService;
import com.hoolink.manage.base.util.ValidatorUtil;
import com.hoolink.manage.base.vo.ManagerBaseGroup;
import com.hoolink.manage.base.vo.req.*;
import com.hoolink.manage.base.vo.res.*;
import com.hoolink.sdk.annotation.LogAndParam;
import com.hoolink.sdk.bo.edm.OperateFileLogParamBO;
import com.hoolink.sdk.enums.CheckEnum;
import com.hoolink.sdk.param.BaseParam;
import com.hoolink.sdk.utils.BackVOUtil;
import com.hoolink.sdk.utils.CopyPropertiesUtil;
import com.hoolink.sdk.vo.BackVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xuli
 * @Date: 2019/4/15 19:23
 */
@RestController
@RequestMapping(value = "/mobile/user/")
public class MobileUserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private ExcelService excelService;
    
    @PostMapping(value = "login")
    @ApiOperation(value = "用户登录")
    @LogAndParam(value = "用户登录失败，请稍后重试")
    public BackVO<LoginResultVO> login(@RequestBody LoginParamVO loginParam)throws Exception {
        BackVO validateParameter = ValidatorUtil.validateParameter(loginParam, ManagerBaseGroup.UserLogin.class);
        if (validateParameter != null) {
            return validateParameter;
        }
        //VO转BO
        LoginParamBO paramBO = CopyPropertiesUtil.copyBean(loginParam, LoginParamBO.class);
        //BO转VO
        LoginResultVO loginResultVO= CopyPropertiesUtil.copyBean(userService.login(paramBO,true),LoginResultVO.class);
        return BackVOUtil.operateAccess(loginResultVO);
    }

    @PostMapping(value = "logout")
    @ApiOperation(value = "用户退出")
    @LogAndParam(value = "用户退出失败，请稍后重试")
    public BackVO<Void> logout() {
        userService.mobileLogout();
        return BackVOUtil.operateAccess();
    }

    @PostMapping(value = "resetPassword")
    @ApiOperation(value = "重置密码")
    @LogAndParam(value = "重置密码失败，请稍后重试")
    public BackVO<Void> resetPassword(@RequestBody LoginParamVO loginParam)throws Exception  {
        BackVO validateParameter = ValidatorUtil.validateParameter(loginParam, ManagerBaseGroup.UserLogin.class);
        if (validateParameter != null) {
            return validateParameter;
        }
        //VO转BO
        LoginParamBO paramBO = CopyPropertiesUtil.copyBean(loginParam, LoginParamBO.class);
        userService.resetPassword(paramBO);
        return BackVOUtil.operateAccess();
    }

    @PostMapping(value = "getPhoneCode")
    @ApiOperation(value = "获取手机验证码")
    @LogAndParam(value = "获取手机验证码失败，请稍后重试")
    public BackVO<String> getPhoneCode(@RequestBody BaseParam<String> phone)throws Exception  {
    	userService.getPhoneCode(phone.getData(),false);
        return BackVOUtil.operateAccess();
    }

    @PostMapping(value = "bindPhoneGetCode")
    @ApiOperation(value = "绑定手机号时获取手机验证码")
    @LogAndParam(value = "获取手机验证码失败，请稍后重试")
    public BackVO<String> bindPhoneGetCode(@RequestBody BaseParam<String> phone)throws Exception  {
    	userService.getPhoneCode(phone.getData(),true);
        return BackVOUtil.operateAccess();
    }

    @PostMapping(value = "modifyPhoneGetCode")
    @ApiOperation(value = "修改手机号时获取手机验证码")
    @LogAndParam(value = "修改手机验证码失败，请稍后重试")
    public BackVO<String> modifyPhoneGetCode(@RequestBody BaseParam<String> phone)throws Exception  {
        return BackVOUtil.operateAccess(userService.getPhoneCode(phone.getData(),true));
    }

    @PostMapping(value = "verifyPhoneCode")
    @ApiOperation(value = "验证手机验证码")
    @LogAndParam(value = "验证手机验证码失败，请稍后重试")
    public BackVO<String> verifyPhoneCode(@RequestBody PhoneParamVO phoneParam)throws Exception  {
        BackVO validateParameter = ValidatorUtil.validateParameter(phoneParam);
        if (validateParameter != null) {
            return validateParameter;
        }
        PhoneParamBO bindPhone=CopyPropertiesUtil.copyBean(phoneParam, PhoneParamBO.class);
        userService.verifyPhone(bindPhone);
        return BackVOUtil.operateAccess();
    }

    @PostMapping(value = "bindPhone")
    @ApiOperation(value = "绑定手机号")
    @LogAndParam(value = "绑定手机号失败，请稍后重试")
    public BackVO<Void> bindPhone(@RequestBody PhoneParamVO bindPhoneParam)throws Exception  {
        BackVO validateParameter = ValidatorUtil.validateParameter(bindPhoneParam);
        if (validateParameter != null) {
            return validateParameter;
        }
        PhoneParamBO bindPhone=CopyPropertiesUtil.copyBean(bindPhoneParam, PhoneParamBO.class);
        userService.bindPhone(bindPhone);
        return BackVOUtil.operateAccess();
    }

    @PostMapping(value = "forgetPassword")
    @ApiOperation(value = "忘记密码")
    @LogAndParam(value = "忘记密码操作失败，请稍后重试")
    public BackVO<String> forgetPassword(@RequestBody LoginParamVO loginParam)throws Exception  {
        BackVO validateParameter = ValidatorUtil.validateParameter(loginParam, ManagerBaseGroup.ForgetPassword.class);
        if (validateParameter != null) {
            return validateParameter;
        }
        //VO转BO
        LoginParamBO paramBO = CopyPropertiesUtil.copyBean(loginParam, LoginParamBO.class);
        return BackVOUtil.operateAccess(userService.forgetPassword(paramBO));
    }

    @PostMapping(value = "getUserInfo")
    @ApiOperation(value = "获取用户信息")
    @LogAndParam(value = "获取用户信息失败，请稍后重试")
    public BackVO<UserInfoVO> getUserInfo()throws Exception  {
        return BackVOUtil.operateAccess(CopyPropertiesUtil.copyBean(userService.getUserInfo(), UserInfoVO.class));
    }

    @PostMapping(value = "updatePhone")
    @ApiOperation(value = "修改手机号")
    @LogAndParam(value = "修改手机号失败，请稍后重试")
    public BackVO<Void> updatePhone(@RequestBody PhoneParamVO phoneParam)throws Exception  {
        BackVO validateParameter = ValidatorUtil.validateParameter(phoneParam);
        if (validateParameter != null) {
            return validateParameter;
        }
        PhoneParamBO bindPhone=CopyPropertiesUtil.copyBean(phoneParam, PhoneParamBO.class);
        userService.updatePhone(bindPhone);
        return BackVOUtil.operateAccess();
    }


    @PostMapping(value = "list")
    @ApiOperation(value = "获取用户列表带分页")
    @LogAndParam(value = "获取用户列表失败", check = CheckEnum.DATA_NOT_NULL)
    public BackVO<PageInfo<ManagerUserVO>> list(@RequestBody ManagerUserPageParamVO userPageParamVO) throws Exception{
    	ManagerUserPageParamBO userPageParamBO = CopyPropertiesUtil.copyBean(userPageParamVO, ManagerUserPageParamBO.class);
    	return BackVOUtil.operateAccess(CopyPropertiesUtil.copyPageInfo(userService.list(userPageParamBO), ManagerUserVO.class));
    }
    
    @PostMapping(value = "getManagerUserInfo")
    @ApiOperation(value = "获取用户基础信息")
    @LogAndParam(value = "获取用户基础信息失败", check = CheckEnum.DATA_NOT_NULL)
    public BackVO<ManagerUserInfoVO> getManagerUserInfo(@RequestBody ManagerUserInfoParamVO userParamVO) throws Exception{
        BackVO validateParameter = ValidatorUtil.validateParameter(userParamVO);
        if (validateParameter != null) {
            return validateParameter;
        }
    	ManagerUserInfoParamBO userParamBO = CopyPropertiesUtil.copyBean(userParamVO, ManagerUserInfoParamBO.class);
    	return BackVOUtil.operateAccess(CopyPropertiesUtil.copyBean(userService.getManagerUserInfo(userParamBO), ManagerUserInfoVO.class));
    }
    
    @PostMapping(value = "createUser")
    @ApiOperation(value = "创建用户")
    @LogAndParam(value = "创建用户失败", check = CheckEnum.DATA_NOT_NULL)
    public BackVO<Void> createUser(@RequestBody ManagerUserParamVO userVO) throws Exception{
        BackVO validateParameter = ValidatorUtil.validateParameter(userVO, ManagerBaseGroup.CreateUser.class);
        if (validateParameter != null) {
            return validateParameter;
        }
    	userService.createUser(CopyPropertiesUtil.copyBean(userVO, ManagerUserParamBO.class));
    	return BackVOUtil.operateAccess();
    }
    
    @PostMapping(value = "updateUser")
    @ApiOperation(value = "更新用户")
    @LogAndParam(value = "更新用户失败", check = CheckEnum.DATA_NOT_NULL)
    public BackVO<Void> updateUser(@RequestBody ManagerUserParamVO userVO) throws Exception{
        BackVO validateParameter = ValidatorUtil.validateParameter(userVO, ManagerBaseGroup.UpdateUser.class);
        if (validateParameter != null) {
            return validateParameter;
        }
    	userService.updateUser(CopyPropertiesUtil.copyBean(userVO, ManagerUserParamBO.class));
    	return BackVOUtil.operateAccess();
    }
    
    @PostMapping(value = "removeUser")
    @ApiOperation(value = "删除用户")
    @LogAndParam(value = "删除用户失败", check = CheckEnum.DATA_NOT_NULL)
    public BackVO<Void> removeUser(@RequestBody BaseParam<Long> param) throws Exception{
    	userService.removeUser(param.getData());
    	return BackVOUtil.operateAccess();
    }
    
    @PostMapping(value = "enableOrDisableUser")
    @ApiOperation(value = "启用/禁用用户")
    @LogAndParam(value = "启用/禁用用户失败", check = CheckEnum.DATA_NOT_NULL)
    public BackVO<Void> enableOrDisableUser(@RequestBody EnableOrDisableUserParamVO param) throws Exception{
        BackVO validateParameter = ValidatorUtil.validateParameter(param);
        if (validateParameter != null) {
            return validateParameter;
        }
    	userService.enableOrDisableUser(param);
    	return BackVOUtil.operateAccess();
    }
    
    @PostMapping(value = "getDictInfo")
    @ApiOperation(value = "获取字典值数据")
    @LogAndParam(value = "获取字典值数据失败", check = CheckEnum.DATA_NOT_NULL)
    public BackVO<DictInfoVO> getDictInfo(@RequestBody DictParamVO dictParamVO) throws Exception{
        BackVO validateParameter = ValidatorUtil.validateParameter(dictParamVO);
        if (validateParameter != null) {
            return validateParameter;
        }
    	DictParamBO dictParamBO = CopyPropertiesUtil.copyBean(dictParamVO, DictParamBO.class);
    	return BackVOUtil.operateAccess(CopyPropertiesUtil.copyBean(userService.getDictInfo(dictParamBO), DictInfoVO.class));
    }
    
    @PostMapping(value = "getDeptTree")
    @ApiOperation(value = "获取组织架构tree数据")
    @LogAndParam(value = "获取组织架构tree数据失败", check = CheckEnum.DATA_NOT_NULL)
    public BackVO<List<DeptTreeVO>> getDeptTree(@RequestBody GetDeptTreeParamVO deptTreeParamVO) throws Exception{
    	List<Long> companyIdList = new ArrayList<>();
    	if(deptTreeParamVO.getCompanyId() != null) {
    		companyIdList.add(deptTreeParamVO.getCompanyId());
    	}
    	return BackVOUtil.operateAccess(CopyPropertiesUtil.copyList(userService.getDeptTree(companyIdList), DeptTreeVO.class));
    }
    
    @PostMapping(value = "exportList")
    @ApiOperation(value = "导出用户列表")
    @LogAndParam(value = "导出用户列表失败")
    @ApiResponses({@ApiResponse(code = 200, response = File.class, message = "导出列表"),})
    public ResponseEntity<Resource> exportByParam(@RequestBody ManagerUserPageParamVO userPageParamVO) throws Exception {
    	ManagerUserPageParamBO userPageParamBO = CopyPropertiesUtil.copyBean(userPageParamVO, ManagerUserPageParamBO.class);
        return excelService.exportList(userPageParamBO);
    }
    
    @PostMapping(value = "downloadTemplate")
    @ApiOperation(value = "下载模板")
    @LogAndParam(value = "下载模板失败")
    @ApiResponses({@ApiResponse(code = 200, response = File.class, message = "下载模板"),})
    public ResponseEntity<Resource> downloadTemplate() throws Exception {
        return excelService.downloadTemplate();
    }
    
    @PostMapping(value = "updatePassword")
    @ApiOperation(value = "修改密码")
    @LogAndParam(value = "修改密码失败，请稍后重试", check = CheckEnum.DATA_NOT_NULL)
    public BackVO<Void> updatePassword(@RequestBody @Valid MobileUpdateParamVO mobileUpdateParamVO)throws Exception  {
        //VO转BO
        MobileUpdateParamBO mobileUpdateParamBO = CopyPropertiesUtil.copyBean(mobileUpdateParamVO, MobileUpdateParamBO.class);
        userService.updateMobilePassword(mobileUpdateParamBO);
        return BackVOUtil.operateAccess();
    }
    
    @PostMapping(value = "resetPhone")
    @ApiOperation(value = "重置手机号码")
    @LogAndParam(value = "重置手机号码失败，请稍后重试", check = CheckEnum.DATA_NOT_NULL)
    public BackVO<Void> resetPhone(@RequestBody BaseParam<Long> userIdParam)throws Exception  {
        userService.resetPhone(userIdParam.getData());
        return BackVOUtil.operateAccess();
    }
    
    @PostMapping(value = "resetPasswd")
    @ApiOperation(value = "重置密码")
    @LogAndParam(value = "重置密码失败，请稍后重试", check = CheckEnum.DATA_NOT_NULL)
    public BackVO<Void> resetPasswd(@RequestBody BaseParam<Long> userIdParam)throws Exception  {
        userService.resetPasswd(userIdParam.getData());
        return BackVOUtil.operateAccess();
    }
    
    @PostMapping(value = "uploadExcel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "excel上传")
    @LogAndParam(value = "excel上传失败")
    public BackVO<UserExcelDataVO> uploadExcel(@RequestPart("file") MultipartFile multipartFile, String deptIdList) throws Exception {
    	UserExcelDataBO userExcelDataBO = excelService.uploadExcel(multipartFile, deptIdList);
    	UserExcelDataVO userExcelDataVO=CopyPropertiesUtil.copyBean(userExcelDataBO,UserExcelDataVO.class);
        return BackVOUtil.operateAccess(userExcelDataVO);
    }

    @PostMapping(value = "updateDeviceCode")
    @ApiOperation(value = "更新设备码")
    @LogAndParam(value = "更新设备码失败，请稍后重试", check = CheckEnum.DATA_NOT_NULL)
    public BackVO<Void> updateDeviceCode(@RequestBody BaseParam<String> deviceCode)throws Exception  {
        userService.updateDeviceCode(deviceCode.getData());
        return BackVOUtil.operateAccess();
    }
    
    @PostMapping(value = "listOperateLog")
    @ApiOperation(value = "获取用户操作日志带分页")
    @LogAndParam(value = "获取用户操作日志失败", check = CheckEnum.DATA_NOT_NULL)
    public BackVO<PageInfo<OperateFileLogVO>> listOperateLog(@RequestBody OperateFileLogParamVO paramVO) throws Exception{
    	OperateFileLogParamBO paramBO = CopyPropertiesUtil.copyBean(paramVO, OperateFileLogParamBO.class);
    	return BackVOUtil.operateAccess(CopyPropertiesUtil.copyPageInfo(userService.listOperateLog(paramBO), OperateFileLogVO.class));
    }
}
