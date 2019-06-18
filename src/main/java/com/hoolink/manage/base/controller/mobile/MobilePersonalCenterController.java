package com.hoolink.manage.base.controller.mobile;

import com.hoolink.manage.base.service.UserService;
import com.hoolink.manage.base.vo.req.ManagerUserInfoParamVO;
import com.hoolink.manage.base.vo.res.PersonalInfoVO;
import com.hoolink.sdk.annotation.LogAndParam;
import com.hoolink.sdk.param.BaseParam;
import com.hoolink.sdk.utils.BackVOUtil;
import com.hoolink.sdk.utils.CopyPropertiesUtil;
import com.hoolink.sdk.vo.BackVO;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.core.MediaType;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 个人中心
 * @author lijunling
 *
 * @date 2019/05/18 17:44
 */
@RestController
@RequestMapping(value = "/mobile/personalCenter/")
@RestSchema(schemaId = "mobile.personalCenterController")
public class MobilePersonalCenterController {
    @Autowired
    private UserService userService;
    
    @PostMapping(value = "getManagerUserInfo")
    @ApiOperation(value = "获取个人中心基础信息")
    @LogAndParam(value = "获取个人中心基础信息失败")
    public BackVO<PersonalInfoVO> getManagerUserInfo(ManagerUserInfoParamVO userParamVO) throws Exception{
    	return BackVOUtil.operateAccess(CopyPropertiesUtil.copyBean(userService.getPersonalInfo(), PersonalInfoVO.class));
    }
    
    
    @PostMapping(value = "updateImageId")
    @ApiOperation(value = "个人中心保存头像")
    @LogAndParam(value = "保存头像失败")
    public BackVO<PersonalInfoVO> updateImageId(@RequestBody BaseParam<Long> imageIdParam) throws Exception{
    	userService.updateImage(imageIdParam.getData());
    	return BackVOUtil.operateAccess();
    }
    
    @PostMapping(value = "uploadImage", consumes = MediaType.MULTIPART_FORM_DATA)
    @ApiOperation(value = "个人中心保存头像")
    @LogAndParam(value = "个人中心保存头像失败")
    public BackVO<Void> uploadImage(@RequestPart("file") MultipartFile multipartFile) throws Exception{
    	userService.uploadImage(multipartFile);
    	return BackVOUtil.operateAccess();
    }
}
