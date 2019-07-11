package com.hoolink.manage.base.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hoolink.manage.base.bo.DeptPositionBO;
import com.hoolink.manage.base.bo.DeptSecurityBO;
import com.hoolink.manage.base.bo.DeptTreeBO;
import com.hoolink.manage.base.bo.DictInfoBO;
import com.hoolink.manage.base.bo.DictParamBO;
import com.hoolink.manage.base.bo.LoginParamBO;
import com.hoolink.manage.base.bo.LoginResultBO;
import com.hoolink.manage.base.bo.ManageRoleBO;
import com.hoolink.manage.base.bo.ManagerUserInfoBO;
import com.hoolink.manage.base.bo.ManagerUserInfoParamBO;
import com.hoolink.manage.base.bo.ManagerUserPageParamBO;
import com.hoolink.manage.base.bo.ManagerUserParamBO;
import com.hoolink.manage.base.bo.MiddleUserDepartmentBO;
import com.hoolink.manage.base.bo.MiddleUserDeptWithMoreBO;
import com.hoolink.manage.base.bo.PersonalInfoBO;
import com.hoolink.manage.base.bo.PhoneParamBO;
import com.hoolink.manage.base.bo.RoleMenuPermissionBO;
import com.hoolink.manage.base.bo.UpdatePasswdParamBO;
import com.hoolink.manage.base.bo.UserDeptBO;
import com.hoolink.manage.base.bo.UserDeptPairBO;
import com.hoolink.manage.base.bo.UserDeptPairParamBO;
import com.hoolink.manage.base.bo.UserInfoBO;
import com.hoolink.manage.base.bo.UserSecurityBO;
import com.hoolink.manage.base.constant.Constant;
import com.hoolink.manage.base.consumer.ability.AbilityClient;
import com.hoolink.manage.base.consumer.edm.EdmClient;
import com.hoolink.manage.base.dao.mapper.ManageDepartmentMapper;
import com.hoolink.manage.base.dao.mapper.ManageRoleMapper;
import com.hoolink.manage.base.dao.mapper.MiddleUserDepartmentMapper;
import com.hoolink.manage.base.dao.mapper.UserMapper;
import com.hoolink.manage.base.dao.mapper.ext.ManageDepartmentMapperExt;
import com.hoolink.manage.base.dao.mapper.ext.MiddleUserDepartmentMapperExt;
import com.hoolink.manage.base.dao.mapper.ext.UserMapperExt;
import com.hoolink.manage.base.dao.model.ManageDepartment;
import com.hoolink.manage.base.dao.model.ManageDepartmentExample;
import com.hoolink.manage.base.dao.model.ManageRole;
import com.hoolink.manage.base.dao.model.MiddleUserDepartment;
import com.hoolink.manage.base.dao.model.MiddleUserDepartmentExample;
import com.hoolink.manage.base.dao.model.User;
import com.hoolink.manage.base.dao.model.UserExample;
import com.hoolink.manage.base.dict.AbstractDict;
import com.hoolink.manage.base.service.DepartmentService;
import com.hoolink.manage.base.service.MiddleUserDepartmentService;
import com.hoolink.manage.base.service.RoleService;
import com.hoolink.manage.base.service.SessionService;
import com.hoolink.manage.base.service.UserService;
import com.hoolink.manage.base.util.SpringUtils;
import com.hoolink.manage.base.vo.req.EnableOrDisableUserParamVO;
import com.hoolink.sdk.bo.BackBO;
import com.hoolink.sdk.bo.ability.ObsBO;
import com.hoolink.sdk.bo.ability.SmsBO;
import com.hoolink.sdk.bo.base.CurrentUserBO;
import com.hoolink.sdk.bo.base.UserBO;
import com.hoolink.sdk.bo.edm.MobileFileBO;
import com.hoolink.sdk.bo.edm.OperateFileLogBO;
import com.hoolink.sdk.bo.edm.OperateFileLogParamBO;
import com.hoolink.sdk.bo.edm.RepertoryBO;
import com.hoolink.sdk.bo.manager.DeptPairBO;
import com.hoolink.sdk.bo.manager.DeptSecurityRepertoryBO;
import com.hoolink.sdk.bo.manager.ManageDepartmentBO;
import com.hoolink.sdk.bo.manager.ManageUserDeptBO;
import com.hoolink.sdk.bo.manager.ManageUserInfoBO;
import com.hoolink.sdk.bo.manager.ManagerUserBO;
import com.hoolink.sdk.bo.manager.OrganizationInfoParamBO;
import com.hoolink.sdk.bo.manager.SimpleDeptUserBO;
import com.hoolink.sdk.bo.manager.UserDeptAssociationBO;
import com.hoolink.sdk.bo.manager.UserDeptInfoBO;
import com.hoolink.sdk.constants.ContextConstant;
import com.hoolink.sdk.enums.DeptTypeEnum;
import com.hoolink.sdk.enums.EncryLevelEnum;
import com.hoolink.sdk.enums.ManagerUserSexEnum;
import com.hoolink.sdk.enums.StatusEnum;
import com.hoolink.sdk.enums.edm.EdmDeptEnum;
import com.hoolink.sdk.exception.BusinessException;
import com.hoolink.sdk.exception.HoolinkExceptionMassageEnum;
import com.hoolink.sdk.utils.ArrayUtil;
import com.hoolink.sdk.utils.ContextUtil;
import com.hoolink.sdk.utils.CopyPropertiesUtil;
import com.hoolink.sdk.utils.MD5Util;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.servicecomb.swagger.invocation.context.ContextUtils;
import org.apache.servicecomb.swagger.invocation.context.InvocationContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: xuli
 * @Date: 2019/4/15 19:24
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private SessionService sessionService;

    @Resource
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AbilityClient abilityClient;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MiddleUserDepartmentService middleUserDepartmentService;

    @Resource
    private MiddleUserDepartmentMapperExt middleUserDepartmentMapperExt;

    @Resource
    private UserMapperExt userMapperExt;

    @Resource
    private ManageDepartmentMapper manageDepartmentMapper;

    @Resource
    private ManageDepartmentMapperExt manageDepartmentMapperExt;

    @Resource
    private MiddleUserDepartmentMapper middleUserDepartmentMapper;

    @Resource
    private ManageRoleMapper manageRoleMapper;
    
    @Autowired
    private EdmClient edmClient;


    /*** 验证码超时时间，10分钟 */
    private static final long TIMEOUT_MINUTES = 10;
    /*** 验证码重发间隔，1分钟只允许1次 */
    private static final long REPEAT_PERIOD = 1;

    @Override
    public LoginResultBO login(LoginParamBO loginParam,Boolean isMobile) throws Exception {
        /*
         * 1.校验：1).检查用户及密码能否关联到用户  2).检查客户被禁用  3).检查用户是否被禁用  4).检查用户所属角色是否被禁用
         * 2.返回Token，是否第一次登录（用于用户协议确认），密码是否被重置（用于强制修改密码），最后一次项目
         */
        UserExample example = new UserExample();
        example.createCriteria().andEnabledEqualTo(true)
                .andUserAccountEqualTo(loginParam.getAccount())
                .andPasswdEqualTo(MD5Util.MD5(loginParam.getPasswd()));
        User user = userMapper.selectByExample(example).stream().findFirst().orElse(null);

        // 检查用户密码错误,用户是否被禁用，角色是否被禁用
        checkAccount(user);
        // 缓存当前用户
        String token = cacheSession(user,isMobile);

        //设置登陆时间
        User toUpdateUser = new User();
        toUpdateUser.setId(user.getId());
        toUpdateUser.setLastTime(System.currentTimeMillis());
        userMapper.updateByPrimaryKeySelective(toUpdateUser);

        //查询角色
        ManageRole manageRole=manageRoleMapper.selectByPrimaryKey(user.getRoleId());

        LoginResultBO loginResult = new LoginResultBO();
        loginResult.setToken(token);
        loginResult.setFirstLogin(user.getFirstLogin());
        loginResult.setPhone(user.getPhone());
        loginResult.setName(user.getName());
        loginResult.setRoleName(manageRole.getRoleName());
        loginResult.setRoleLevel(manageRole.getRoleLevel());
        //设置头像
        if (user.getImgId() != null) {
            try {
                //如果获取头像失败，给予默认头像
                BackBO<ObsBO> obs = abilityClient.getObs(user.getImgId());
                loginResult.setImage(obs.getData().getObjectUrl());
            } catch (Exception e) {
            }
        }
        //设置问候语
        loginResult.setGreetings(setGreeting());
        //设置系统访问权限（EDM和管理平台）
        List<RoleMenuPermissionBO> roleMenuPermissionList = roleService.listMenuAccessByRoleId(user.getRoleId());
        Optional<RoleMenuPermissionBO> edmRoleMenuPermissionOPt = roleMenuPermissionList.stream().filter(rmp -> Constant.EDM.equals(rmp.getMenuCode())).findFirst();
        if (edmRoleMenuPermissionOPt.isPresent()) {
            loginResult.setAccessEDM(true);
        } else {
            loginResult.setAccessEDM(false);
        }
        Optional<RoleMenuPermissionBO> hoolinkRoleMenuPermissionOPt = roleMenuPermissionList.stream().filter(rmp -> Constant.HOOLINK.equals(rmp.getMenuCode())).findFirst();
        if (hoolinkRoleMenuPermissionOPt.isPresent()) {
            loginResult.setAccessHoolink(true);
        } else {
            loginResult.setAccessHoolink(false);
        }
        addRepertory(roleMenuPermissionList,loginResult,null);
        return loginResult;
    }

    private void addRepertory(List<RoleMenuPermissionBO> roleMenuPermissionList,LoginResultBO loginResult,UserInfoBO userInfoBO){
        List<RepertoryBO> edmRepertory=new ArrayList<>();
        List<RepertoryBO> repertory=new ArrayList<>();
        if(roleMenuPermissionList.stream().filter(rmp -> Constant.DEPT_REPERTORY.equals(rmp.getMenuCode())).findFirst().isPresent()){
            RepertoryBO repertoryBO=new RepertoryBO();
            repertoryBO.setType(Constant.REPERTORY_ONE);
            repertoryBO.setName(Constant.REPERTORY_ONE_NAME);
            repertory.add(repertoryBO);
            edmRepertory.add(repertoryBO);
        }
        if(roleMenuPermissionList.stream().filter(rmp -> Constant.CACHE_REPERTORY.equals(rmp.getMenuCode())).findFirst().isPresent()){
            RepertoryBO repertoryBO=new RepertoryBO();
            repertoryBO.setType(Constant.REPERTORY_TWO);
            repertoryBO.setName(Constant.REPERTORY_TWO_NAME);
            edmRepertory.add(repertoryBO);
        }
        if(roleMenuPermissionList.stream().filter(rmp -> Constant.COMPANY_REPERTORY.equals(rmp.getMenuCode())).findFirst().isPresent()){
            RepertoryBO repertoryBO=new RepertoryBO();
            repertoryBO.setType(Constant.REPERTORY_THREE);
            repertoryBO.setName(Constant.REPERTORY_THREE_NAME);
            repertory.add(repertoryBO);
            edmRepertory.add(repertoryBO);
        }
        if(loginResult!=null) {
            loginResult.setEdmRepertory(edmRepertory);
            loginResult.setRepertoryList(repertory);
        }
        if(userInfoBO!=null){
            userInfoBO.setEdmRepertory(edmRepertory);
        }
    }



    @Override
    public void logout() {
        CurrentUserBO currentUser = sessionService.getCurrentUser(sessionService.getUserIdByToken());
        InvocationContext context = ContextUtils.getInvocationContext();
        String token =context.getContext(ContextConstant.TOKEN);
        // 避免导致异地登录账号退出
        if (currentUser != null && Objects.equals(currentUser.getToken(), token)) {
            sessionService.deleteSession(currentUser.getUserId());
        }
    }

    @Override
    public void mobileLogout() {
        CurrentUserBO currentUser = sessionService.getCurrentUser(sessionService.getUserIdByMobileToken());
        InvocationContext context = ContextUtils.getInvocationContext();
        String token =context.getContext(ContextConstant.TOKEN);
        // 避免导致异地登录账号退出
        if (currentUser != null && Objects.equals(currentUser.getToken(), token)) {
            sessionService.deleteSession(currentUser.getUserId());
        }
    }

    @Override
    public CurrentUserBO getSessionUser(String token,boolean ismobile) {
        // 获取当前 session 中的用户
        CurrentUserBO currentUser = sessionService.getCurrentUser(token,ismobile);
        if (currentUser == null) {
            return null;
        }
        // 刷新 session 失效时间
        if (!sessionService.refreshSession(currentUser.getUserId(),ismobile)) {
            return null;
        }
        return currentUser;
    }

    @Override
    public void resetPassword(LoginParamBO loginParam) throws Exception {
        verifyOldPasswdOrCode(loginParam);
        User user = getUserByAccount(loginParam.getAccount());
        //重置密码,并且设置不是首次登录
        user.setId(user.getId());
        user.setPasswd(MD5Util.MD5(loginParam.getPasswd()));
        user.setUpdated(System.currentTimeMillis());
        user.setUpdator(user.getId());
        user.setFirstLogin(false);
        userMapper.updateByPrimaryKeySelective(user);
    }

    private void verifyOldPasswdOrCode(LoginParamBO loginParam){
        String oldPassword=loginParam.getOldPasswd();
        String code=loginParam.getCode();
        UserExample example=new UserExample();
        example.createCriteria().andUserAccountEqualTo(loginParam.getAccount());
        User user=userMapper.selectByExample(example).stream().findFirst().orElse(null);
        if(user!=null){
            if(StringUtils.isNotBlank(oldPassword)){
                if(!Objects.equals(MD5Util.MD5(oldPassword),user.getPasswd())){
                    throw new BusinessException(HoolinkExceptionMassageEnum.RESET_PASSWORD_ERROR);
                }
            }
            if(StringUtils.isNotBlank(code)){
                PhoneParamBO phoneParamBO=new PhoneParamBO();
                phoneParamBO.setCode(code);
                phoneParamBO.setPhone(user.getPhone());
                checkPhoneCode(phoneParamBO);
            }
            if(StringUtils.isBlank(oldPassword) && StringUtils.isBlank(code)){
                throw new BusinessException(HoolinkExceptionMassageEnum.PARAM_ERROR);
            }
        }
    }

    @Override
    public String getPhoneCode(String phone, Boolean flag) throws Exception {
        //为true表示需要校验手机号是否存在
        if (flag) {
            checkPhoneExist(phone);
        }
        //生成随机6位数字
        String code = RandomStringUtils.randomNumeric(Constant.PHONE_COED_LENGTH);
        //调用ability发送验证码
        SmsBO smsBO = new SmsBO();
        String content = messageSource.getMessage("sms.captcha", new Object[]{code}, Locale.getDefault());
        smsBO.setContent(content);
        smsBO.setPhone(phone);
        abilityClient.sendMsg(smsBO);
        // 缓存剩余时间
        Long remainingTime = stringRedisTemplate.opsForValue().getOperations().getExpire(Constant.PHONE_CODE_PREFIX + phone, TimeUnit.MINUTES);
        // 默认1分钟之内仅进行1次验证码发送业务
        if (remainingTime != null && TIMEOUT_MINUTES - remainingTime < REPEAT_PERIOD) {
            throw new BusinessException(HoolinkExceptionMassageEnum.CAPTCHA_CACHE_TOO_FREQUENTLY);
        }
        //手机号与验证码存入
        stringRedisTemplate.opsForValue().set(Constant.PHONE_CODE_PREFIX + phone, code, TIMEOUT_MINUTES, TimeUnit.MINUTES);
        return code;
    }

    @Override
    public void verifyPhone(PhoneParamBO phoneParam) throws Exception {

        checkPhoneCode(phoneParam);
    }

    @Override
    public void bindPhone(PhoneParamBO bindPhoneParam) throws Exception {
        //查看手机号是否已经存在
        checkPhoneExist(bindPhoneParam.getPhone());
        //校验手机验证码
        checkPhoneCode(bindPhoneParam);
        Long userId = getCurrentUserId();
        //绑定手机号
        User user = new User();
        user.setId(userId);
        user.setPhone(bindPhoneParam.getPhone());
        user.setUpdator(userId);
        user.setUpdated(System.currentTimeMillis());
        userMapper.updateByPrimaryKeySelective(user);
        //删除缓存中的验证码信息
        stringRedisTemplate.opsForValue().getOperations().delete(Constant.PHONE_CODE_PREFIX + bindPhoneParam.getPhone());
    }

    private String checkPhoneCode(PhoneParamBO phoneParamBO) {
        //给测试脚本通过
        if (Constant.CESHI_CODE.equals(phoneParamBO.getCode())) {
            return Constant.CESHI_CODE;
        }
        String code = null;
        try {
            code = stringRedisTemplate.opsForValue().get(Constant.PHONE_CODE_PREFIX + phoneParamBO.getPhone());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("从redis中获取手机验证码异常,手机号为{}，验证码为{}", phoneParamBO.getPhone(), phoneParamBO.getCode());
        }
        if (StringUtils.isBlank(code) || !Objects.equals(code, phoneParamBO.getCode())) {
            throw new BusinessException(HoolinkExceptionMassageEnum.PHONE_CODE_ERROR);
        }
        //校验完了不删除验证码，通过过期机制删除
        return code;
    }

    @Override
    public String forgetPassword(LoginParamBO loginParam) throws Exception {
        User user = getUserByAccount(loginParam.getAccount());
        if (StringUtils.isBlank(user.getPhone())) {
            throw new BusinessException(HoolinkExceptionMassageEnum.NOT_BIND_PHONE);
        }
        return user.getPhone();
    }

    @Override
    public UserInfoBO getUserInfo() throws Exception {
        Long userId = getCurrentUserId();
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            throw new BusinessException(HoolinkExceptionMassageEnum.USER_NOT_EXIST_ERROR);
        }

        //封装结果
        UserInfoBO userInfoBO = new UserInfoBO();
        userInfoBO.setPhone(user.getPhone());
        userInfoBO.setUserName(user.getName());
        //设置头像
        if (user.getImgId() != null) {
            try {
                //如果获取头像失败，给予默认头像
                BackBO<ObsBO> obs = abilityClient.getObs(user.getImgId());
                userInfoBO.setImage(obs.getData().getObjectUrl());
            } catch (Exception e) {
            }
        }
        List<RoleMenuPermissionBO> roleMenuPermissionList = roleService.listMenuAccessByRoleId(user.getRoleId());
        addRepertory(roleMenuPermissionList,null,userInfoBO);
        //查询角色
        ManageRole manageRole=manageRoleMapper.selectByPrimaryKey(user.getRoleId());
        userInfoBO.setRoleName(manageRole.getRoleName());
        userInfoBO.setRoleLevel(manageRole.getRoleLevel());
        userInfoBO.setUserId(userId);
        return userInfoBO;
    }

    @Override
    public void updatePhone(PhoneParamBO phoneParam) throws Exception {
        checkPhoneExist(phoneParam.getPhone());
        //校验手机验证码
        checkPhoneCode(phoneParam);
        Long userId = getCurrentUserId();
        User user = new User();
        user.setId(userId);
        user.setPhone(phoneParam.getPhone());
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public UserBO getUser() throws Exception {
        Long userId = ContextUtil.getManageCurrentUser().getUserId();
        User user = userMapper.selectByPrimaryKey(userId);
        UserBO userBO = CopyPropertiesUtil.copyBean(user, UserBO.class);
        return userBO;
    }

    private void checkAccount(User user) {
        // 账号密码错误
        if (user == null) {
            throw new BusinessException(HoolinkExceptionMassageEnum.USER_ACCOUNT_OR_PASSWORD_ERROR);
        }
        // 用户被禁用
        if (user.getStatus() == null || !user.getStatus()) {
            throw new BusinessException(HoolinkExceptionMassageEnum.USER_FORBIDDEN);
        }
        Long roleId=user.getRoleId();
        ManageRole role=manageRoleMapper.selectByPrimaryKey(roleId);
        if(role!=null && !role.getRoleStatus()){
            throw new BusinessException(HoolinkExceptionMassageEnum.USER_ROLE_DISABLED);
        }
    }

    @Override
    public String cacheSession(User user,Boolean isMobile) throws Exception {
        CurrentUserBO currentUserBO = new CurrentUserBO();
        currentUserBO.setUserId(user.getId());
        currentUserBO.setAccount(user.getUserAccount());
        //设置角色id
        currentUserBO.setRoleId(user.getRoleId());
        //设置角色层级
        ManageRoleBO role = roleService.selectById(user.getRoleId());
        if (role != null && role.getRoleStatus()) {
            currentUserBO.setRoleLevel(role.getRoleLevel());
        }
        //设置所属公司
        List<MiddleUserDeptWithMoreBO> middleUserDeptWithMoreBOList = middleUserDepartmentService
                .listWithMoreByUserIdList(Arrays.asList(user.getId()));
        currentUserBO.setComanyIdSet(middleUserDeptWithMoreBOList.stream()
                .filter(cudwm -> DeptTypeEnum.COMPANY.getKey().equals(cudwm.getDeptType()))
                .map(cudwm -> cudwm.getDeptId()).collect(Collectors.toSet()));
        //设置权限url
        currentUserBO.setAccessUrlSet(roleService.listAccessUrlByRoleId(user.getRoleId()));
        //设置角色类型
        currentUserBO.setRoleType(role.getRoleType());
        return sessionService.cacheCurrentUser(currentUserBO,isMobile);
    }

    private Long getCurrentUserId() {
        //获取当前登录用户
        Long userId = ContextUtil.getManageCurrentUser().getUserId();
        if (userId == null) {
            throw new BusinessException(HoolinkExceptionMassageEnum.USER_NOT_EXIST_ERROR);
        }
        return userId;
    }

    private User getUserByAccount(String account) {
        UserExample example = new UserExample();
        example.createCriteria().andUserAccountEqualTo(account);
        User user = userMapper.selectByExample(example).stream().findFirst().orElse(null);
        if (user == null) {
            throw new BusinessException(HoolinkExceptionMassageEnum.ACCOUNT_NOT_EXIST);
        }
        return user;
    }

    private void checkPhoneExist(String phone) {
        //查看手机号是否已经存在
        UserExample example = new UserExample();
        example.createCriteria().andEnabledEqualTo(true).andPhoneEqualTo(phone);
        User user = userMapper.selectByExample(example).stream().findFirst().orElse(null);
        if (user != null) {
            throw new BusinessException(HoolinkExceptionMassageEnum.USER_PHONE_EXISTS);
        }
    }

    @Override
    public PageInfo<ManagerUserBO> list(ManagerUserPageParamBO userPageParamBO) throws Exception {
        if (userPageParamBO.getPageNo() == null || userPageParamBO.getPageSize() == null) {
            throw new BusinessException(HoolinkExceptionMassageEnum.PARAM_ERROR);
        }
        
		//只能看见当前用户对应角色的所有子角色用户
		List<ManageRoleBO> roleList = roleService.listChildrenRoleByRoleId(ContextUtil.getManageCurrentUser().getRoleId(), null);
		if(CollectionUtils.isEmpty(roleList)) {
			return new PageInfo<ManagerUserBO>();
		}
		
        UserExample example = buildUserCriteria(userPageParamBO);
        PageInfo<User> userPageInfo = PageHelper
                .startPage(userPageParamBO.getPageNo(), userPageParamBO.getPageSize())
                .doSelectPageInfo(() -> userMapper.selectByExample(example));
        List<User> userList = userPageInfo.getList();

        //组装用户数据
        List<ManagerUserBO> userBoList = buildUserBOList(userList);
        PageInfo<ManagerUserBO> userBOPageInfo = CopyPropertiesUtil.copyPageInfo(userPageInfo, ManagerUserBO.class);
        userBOPageInfo.setList(userBoList);
        return userBOPageInfo;
    }

    /**
     * excel导出列表(无分页)
     *
     * @param userPageParamBO
     * @return
     * @throws Exception
     */
    @Override
    public List<ManagerUserBO> listWithOutPage(ManagerUserPageParamBO userPageParamBO) throws Exception {
		//只能看见当前用户对应角色的所有子角色用户
		List<ManageRoleBO> roleList = roleService.listChildrenRoleByRoleId(ContextUtil.getManageCurrentUser().getRoleId(), null);
		if(CollectionUtils.isEmpty(roleList)) {
			return Collections.emptyList();
		}
        UserExample example = buildUserCriteria(userPageParamBO);
        return buildUserBOList(userMapper.selectByExample(example));
    }

    @Override
    public boolean checkPassword(String password) throws Exception {
        UserExample example = new UserExample();
        example.createCriteria().andEnabledEqualTo(true)
                .andUserAccountEqualTo(ContextUtil.getManageCurrentUser().getAccount())
                .andPasswdEqualTo(MD5Util.MD5(password));
        User user = userMapper.selectByExample(example).stream().findFirst().orElse(null);
        if(user!=null){
            return true;
        }
        return false;
    }

    /**
     * 组装用户数据
     *
     * @param userList
     * @return
     * @throws Exception
     */
    private List<ManagerUserBO> buildUserBOList(List<User> userList) throws Exception {
        //查询用户对应部门
        List<Long> userIdList = userList.stream().map(user -> user.getId()).collect(Collectors.toList());
        List<MiddleUserDeptWithMoreBO> middleUserDepartmentBOList = middleUserDepartmentService
                .listWithMoreByUserIdList(userIdList);
        Map<Long, List<MiddleUserDeptWithMoreBO>> middleUserDepartmentMap = middleUserDepartmentBOList.stream()
                .collect(Collectors.groupingBy(MiddleUserDeptWithMoreBO::getUserId));

		//查询用户对应角色
		List<Long> roleIdList = userList.stream().map(user -> user.getRoleId()).collect(Collectors.toList());
		List<ManageRoleBO> roleList = roleService.listByIdList(roleIdList);
		
		List<ManagerUserBO> userBoList = new ArrayList<>();
		userList.stream().forEach(user -> {
			//封装结果
			ManagerUserBO userBO = new ManagerUserBO();
			userBO.setEncryLevelCompanyName(EncryLevelEnum.getValue(user.getEncryLevelCompany()));
			userBO.setStatusDesc(StatusEnum.getValue(user.getStatus()));
			ManageRoleBO role = roleList.stream().filter(r -> r.getId().equals(user.getRoleId())).findFirst()
					.orElseGet(ManageRoleBO::new);
			userBO.setRoleName(role.getRoleName());
			
			List<MiddleUserDeptWithMoreBO> userDepartmentList = middleUserDepartmentMap.get(user.getId());
			
			if(CollectionUtils.isNotEmpty(userDepartmentList)) {
				// 用户组织关系
				Map<String, List<MiddleUserDeptWithMoreBO>> byDiffDeptGroupMap = userDepartmentList.stream().collect(Collectors.groupingBy(MiddleUserDeptWithMoreBO::getDiffDeptGroup));

				List<DeptPairBO> deptPairList = new ArrayList<>();
				for (Map.Entry<String, List<MiddleUserDeptWithMoreBO>> entry : byDiffDeptGroupMap.entrySet()) {
					List<MiddleUserDeptWithMoreBO> deptWithMoreList = entry.getValue();
					DeptPairBO deptPair = new DeptPairBO();
					deptPair.setDeptIdList(deptWithMoreList.stream().map(dwm -> dwm.getDeptId()).collect(Collectors.toList()));
					deptPair.setDeptNameList(deptWithMoreList.stream().map(dwm -> dwm.getDeptName()).collect(Collectors.toList()));

					if(CollectionUtils.isNotEmpty(deptWithMoreList)) {
						deptPair.setEncryLevelDept(deptWithMoreList.get(0).getEncryLevelDept());
						deptPair.setEncryLevelDeptName(EncryLevelEnum.getValue(deptWithMoreList.get(0).getEncryLevelDept()));
					}
					deptPair.setDeptNameEncryLevelPair(new StringBuilder(StringUtils.join(deptPair.getDeptNameList(), Constant.RUNG)).append(Constant.BACKSLASH).append(StringUtils.isEmpty(deptPair.getEncryLevelDeptName()) ? "":deptPair.getEncryLevelDeptName()).toString());
					deptPairList.add(deptPair);
				}
				userBO.setUserDeptPairList(deptPairList);

				Set<String> companySet = new HashSet<>();
				userDepartmentList.stream().filter(ud -> DeptTypeEnum.COMPANY.getKey().equals(ud.getDeptType()))
						.forEach(ud -> companySet.add(ud.getDeptName()));
				userBO.setCompany(StringUtils.join(companySet, Constant.COMMA));
			}
			
			BeanUtils.copyProperties(user, userBO);
			userBoList.add(userBO);
		});
		return userBoList;
	}

	/**
	 * 用户列表查询条件
	 * @param userPageParamBO
	 * @return
	 */
	private UserExample buildUserCriteria(ManagerUserPageParamBO userPageParamBO) {
		UserExample userExample = new UserExample();
		UserExample.Criteria criteria = userExample.createCriteria();
		//姓名、职位、手机号、账号
		if(StringUtils.isNotBlank(userPageParamBO.getGroupParam())) {
			andCriteria(criteria, userPageParamBO);
			criteria.andNameLike("%" + userPageParamBO.getGroupParam() + "%");

			UserExample.Criteria groupCriteria1 = userExample.createCriteria();
			andCriteria(groupCriteria1, userPageParamBO);
			groupCriteria1.andPositionLike("%" + userPageParamBO.getGroupParam() + "%");
			userExample.or(groupCriteria1);

			UserExample.Criteria groupCriteria2 = userExample.createCriteria();
			andCriteria(groupCriteria2, userPageParamBO);
			groupCriteria2.andPhoneLike("%" + userPageParamBO.getGroupParam() + "%");
			userExample.or(groupCriteria2);

			UserExample.Criteria groupCriteria3 = userExample.createCriteria();
			andCriteria(groupCriteria3, userPageParamBO);
			groupCriteria3.andUserAccountLike("%" + userPageParamBO.getGroupParam() + "%");
			userExample.or(groupCriteria3);
		}else {
			andCriteria(criteria, userPageParamBO);
		}
		userExample.setOrderByClause(" created desc ");
		return userExample;
	}

	private void andCriteria(UserExample.Criteria criteria, ManagerUserPageParamBO userPageParamBO) {
		if(CollectionUtils.isNotEmpty(userPageParamBO.getDeptId())) {
			List<Long> deptIdList = userPageParamBO.getDeptId();
			transformDeptQueryToUserIdQuery(criteria, Arrays.asList(deptIdList.get(deptIdList.size()-1)));
		}
		if(userPageParamBO.getRoleId() != null) {
			criteria.andRoleIdEqualTo(userPageParamBO.getRoleId());
		}
		if(userPageParamBO.getStatus() != null) {
			criteria.andStatusEqualTo(userPageParamBO.getStatus());
		}
		//只能看见当前用户对应角色的所有子角色用户
		List<ManageRoleBO> roleList = roleService.listChildrenRoleByRoleId(ContextUtil.getManageCurrentUser().getRoleId(), null);
		criteria.andRoleIdIn(roleList.stream().map(r -> r.getId()).collect(Collectors.toList()));

		criteria.andEnabledEqualTo(true);
		//登录用户本身应该过滤掉，不可以看到
		criteria.andIdNotEqualTo(getCurrentUserId());
	}

    /**
     * 将组织相关的查询条件转换为id条件
     *
     * @param criteria
     * @param deptIdList
     */
    private void transformDeptQueryToUserIdQuery(UserExample.Criteria criteria, List<Long> deptIdList) {
        List<MiddleUserDepartmentBO> userDeptList = middleUserDepartmentService.listByDeptIdList(deptIdList);
        List<Long> userIdList = userDeptList.stream().map(ud -> ud.getUserId()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(userIdList)) {
            criteria.andIdIsNull();
        } else {
            criteria.andIdIn(userIdList);
        }
    }

    @Override
    public ManagerUserInfoBO getManagerUserInfo(ManagerUserInfoParamBO userParamBO) throws Exception {
        User user = userMapper.selectByPrimaryKey(userParamBO.getUserId());
        if (user == null) {
            throw new BusinessException(HoolinkExceptionMassageEnum.MANAGER_USER_NOT_EXIST_ERROR);
        }

        ManagerUserInfoBO userInfoBO = new ManagerUserInfoBO();
        BeanUtils.copyProperties(user, userInfoBO);
        userInfoBO.setHasLoginYet(user.getLastTime() != null ? true : false);
        if (user.getImgId() != null) {
            //调用obs服务获取用户头像
            BackBO<ObsBO> obsBackBo = abilityClient.getObs(user.getImgId());
            if (obsBackBo.getData() != null) {
                userInfoBO.setImgUrl(obsBackBo.getData().getObjectUrl());
            }
        }

        // 获取用户组织对应关系
        List<MiddleUserDeptWithMoreBO> userDeptWithMoreList = middleUserDepartmentService.listWithMoreByUserIdList(Arrays.asList(user.getId()));
        Map<String, List<MiddleUserDeptWithMoreBO>> byDiffDeptGroupMap = userDeptWithMoreList.stream().collect(Collectors.groupingBy(MiddleUserDeptWithMoreBO::getDiffDeptGroup));

        List<DeptPairBO> deptPairList = new ArrayList<>();
        for (Map.Entry<String, List<MiddleUserDeptWithMoreBO>> entry : byDiffDeptGroupMap.entrySet()) {
            List<MiddleUserDeptWithMoreBO> deptWithMoreList = entry.getValue();
            DeptPairBO deptPair = new DeptPairBO();
            deptPair.setDeptIdList(deptWithMoreList.stream().map(dwm -> dwm.getDeptId()).collect(Collectors.toList()));
            deptPair.setDeptNameList(deptWithMoreList.stream().map(dwm -> dwm.getDeptName()).collect(Collectors.toList()));
            if (CollectionUtils.isNotEmpty(deptWithMoreList)) {
                deptPair.setEncryLevelDept(deptWithMoreList.get(0).getEncryLevelDept());
            }
            deptPairList.add(deptPair);
        }
        userInfoBO.setUserDeptPairList(deptPairList);

        ManageRoleBO role = roleService.selectById(user.getRoleId());
        userInfoBO.setRoleName(role.getRoleName());
        List<MiddleUserDeptWithMoreBO> companyList = userDeptWithMoreList.stream().filter(udwm -> DeptTypeEnum.COMPANY.getKey().equals(udwm.getDeptType())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(companyList)) {
            userInfoBO.setCompanyId(companyList.get(0).getDeptId());
            userInfoBO.setCompanyName(companyList.get(0).getDeptName());
        }
        return userInfoBO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUser(ManagerUserParamBO userBO) throws Exception {
        //查看账号是否已经存在
        checkAccountExist(userBO.getUserAccount());
        checkUserNoExist(userBO.getUserNo());
        //得到将要入库的deptIdList
        List<UserDeptPairBO> deptPairList = parseGetToCreateDeptId(userBO.getUserDeptPairParamList(), true);

        //创建用户
        User user = CopyPropertiesUtil.copyBean(userBO, User.class);
        user.setStatus(true);
        user.setCreator(ContextUtil.getManageCurrentUser().getUserId());
        user.setCreated(System.currentTimeMillis());
        user.setEnabled(true);
        user.setFirstLogin(true);
        //MD5加密，和前端保持一致，"e+iot"拼接密码，加密两次,再后端加密MD5Util.MD5()
        user.setPasswd(MD5Util.MD5(MD5Util.encode(MD5Util.encode(Constant.ENCODE_PASSWORD_PREFIX + Constant.INITIAL_PASSWORD))));
        userMapper.insertSelective(user);

        //新增用户组织对应关系
        batchInsertUserDept(deptPairList, user.getId());
    }

    /**
     * 查看账号是否已经存在
     *
     * @param account
     */
    private void checkAccountExist(String account) {
        UserExample example = new UserExample();
        example.createCriteria().andUserAccountEqualTo(account);
        User user = userMapper.selectByExample(example).stream().findFirst().orElse(null);
        if (user != null) {
            throw new BusinessException(HoolinkExceptionMassageEnum.USER_ACCOUNT_EXISTS);
        }
    }

    /**
     * 查看编号是否已经存在
     *
     * @param userNo
     */
    private void checkUserNoExist(String userNo) {
        UserExample example = new UserExample();
        example.createCriteria().andUserNoEqualTo(userNo);
        User user = userMapper.selectByExample(example).stream().findFirst().orElse(null);
        if (user != null) {
            throw new BusinessException(HoolinkExceptionMassageEnum.USER_NO_EXISTS);
        }
    }

    /**
     * 遍历拿到所有的父级deptId
     *
     * @param deptList
     * @param selectedDeptIdList
     * @param currentId
     */
    private void traverseGetAllParentDeptId(List<ManageDepartmentBO> deptList, List<Long> selectedDeptIdList, Long currentId) {
        Optional<ManageDepartmentBO> manageDepartmentBOOpt = deptList.stream().filter(d -> d.getId().equals(currentId)).findFirst();
        if (manageDepartmentBOOpt.isPresent()) {
            Long parentId = manageDepartmentBOOpt.get().getParentId();
            if (parentId != 0) {
                selectedDeptIdList.add(parentId);
                traverseGetAllParentDeptId(deptList, selectedDeptIdList, parentId);
            }
        }
    }

    /**
     * 遍历拿到所有的子级deptId
     *
     * @param deptList
     * @param selectedDeptIdList
     * @param currentId
     */
    private void traverseGetAllChildrenDeptId(List<ManageDepartmentBO> deptList, List<Long> selectedDeptIdList, Long currentId) {
        Optional<ManageDepartmentBO> manageDepartmentBOOpt = deptList.stream().filter(d -> d.getId().equals(currentId)).findFirst();
        if (manageDepartmentBOOpt.isPresent()) {
            List<ManageDepartmentBO> childrenDeptList = deptList.stream().filter(d -> manageDepartmentBOOpt.get().getId().equals(d.getParentId())).collect(Collectors.toList());
            childrenDeptList.stream().forEach(cd -> {
                selectedDeptIdList.add(cd.getId());
                traverseGetAllChildrenDeptId(deptList, selectedDeptIdList, cd.getId());
            });
        }
    }

    /**
     * 参数校验
     *
     * @param userDeptPairParamList
     * @param flag
     * @return
     */
    private List<UserDeptPairBO> parseGetToCreateDeptId(List<UserDeptPairParamBO> userDeptPairParamList, boolean flag) {
        if (flag && CollectionUtils.isEmpty(userDeptPairParamList)) {
            throw new BusinessException(HoolinkExceptionMassageEnum.DEPARTMENT_ENCRY_LEVEL_DEFAULT_NULL);
        }
        List<ManageDepartmentBO> deptList = departmentService.listAll();
        return validUserDeptPairParam(deptList, userDeptPairParamList);
    }

    /**
     * 校验用户组织参数
     *
     * @param deptList
     * @param userDeptPairParamList
     * @return
     */
    private List<UserDeptPairBO> validUserDeptPairParam(List<ManageDepartmentBO> deptList, List<UserDeptPairParamBO> userDeptPairParamList) {
        List<UserDeptPairBO> deptPairList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(userDeptPairParamList)) {
            List<List<Long>> allDeptIdList = new ArrayList<>();
            for (UserDeptPairParamBO userDeptPairParam : userDeptPairParamList) {
                List<Long> deptIdGroupList = userDeptPairParam.getDeptIdList();
                if (CollectionUtils.isEmpty(deptIdGroupList)) {
                    throw new BusinessException(HoolinkExceptionMassageEnum.DEPARTMENT_ENCRY_LEVEL_DEFAULT_NULL);
                }

                //从末节点逐级找到父节点校验
                Long lastDeptId = deptIdGroupList.get(deptIdGroupList.size() - 1);
                List<Long> parentOfTheLastDeptId = new ArrayList<>();
                parentOfTheLastDeptId.add(lastDeptId);
                traverseGetAllParentDeptId(deptList, parentOfTheLastDeptId, lastDeptId);

                if (!parentOfTheLastDeptId.containsAll(deptIdGroupList) || deptIdGroupList.size() != parentOfTheLastDeptId.size()) {
                    throw new BusinessException(HoolinkExceptionMassageEnum.DEPARTMENT_FORMAT_ERROR);
                }

                if (userDeptPairParam.getEncryLevelDept() == null) {
                    throw new BusinessException(HoolinkExceptionMassageEnum.DEPARTMENT_ENCRY_LEVEL_DEFAULT_NULL);
                }

                allDeptIdList.add(deptIdGroupList);
            }

            //判断节点是否重复或属于父子关系
            if (allDeptIdList.size() > 1) {
                for (int i = 0; i < allDeptIdList.size(); i++) {
                    List<Long> deptIdGroupList = allDeptIdList.get(i);
                    for (int j = i + 1; j < allDeptIdList.size(); j++) {
                        List<Long> afterDeptIdGroupList = allDeptIdList.get(j);
                        if (deptIdGroupList.containsAll(afterDeptIdGroupList) || afterDeptIdGroupList.containsAll(deptIdGroupList)) {
                            throw new BusinessException(HoolinkExceptionMassageEnum.DEPARTMENT_REPEAT_OR_CONTAIN);
                        }
                    }
                }
            }

            for (int i = 0; i < allDeptIdList.size(); i++) {
                List<Long> deptIdList = allDeptIdList.get(i);
                UserDeptPairBO deptPair = new UserDeptPairBO();
                deptPair.setDeptIdList(deptIdList);
                deptPair.setEncryLevelDept(userDeptPairParamList.get(i).getEncryLevelDept());
                deptPairList.add(deptPair);
            }
        }
        return deptPairList;
    }

    /**
     * 新增用户组织对应关系
     *
     * @param deptPairList
     * @param userId
     */
    private void batchInsertUserDept(List<UserDeptPairBO> deptPairList, Long userId) {
        List<MiddleUserDepartmentBO> middleUserDeptList = new ArrayList<>();
        deptPairList.stream().forEach(dp -> {
            String diffDeptGroup = generateRandom();
            List<Long> deptIdList = dp.getDeptIdList();
            for(int i=0; i<deptIdList.size(); i++) {
            	Long deptId = deptIdList.get(i);
                MiddleUserDepartmentBO middleUserDept = new MiddleUserDepartmentBO();
                middleUserDept.setDeptId(deptId);
                middleUserDept.setUserId(userId);
                middleUserDept.setEncryLevelDept(dp.getEncryLevelDept());
                middleUserDept.setDiffDeptGroup(diffDeptGroup);
                middleUserDept.setLowestLevel(false);
                if(i == deptIdList.size()-1) {
                	middleUserDept.setLowestLevel(true);	
                }
                middleUserDeptList.add(middleUserDept);
            }
        });
        middleUserDepartmentService.batchInsert(middleUserDeptList);
    }

    /**
     * 生成随机字符串
     *
     * @return
     */
    private String generateRandom() {
        int eight = 8;
        StringBuilder str = new StringBuilder(UUID.randomUUID().toString().replaceAll(Constant.RUNG, ""));
        Random random = new Random();
        for (int i = 0; i < eight; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(ManagerUserParamBO userBO) throws Exception {
        //查看账号是否已经存在
        if (StringUtils.isNotEmpty(userBO.getUserAccount())) {
            checkAccountExist(userBO.getUserAccount());
        }
        if (StringUtils.isNotEmpty(userBO.getUserNo())) {
            checkUserNoExist(userBO.getUserNo());
        }
        //得到将要入库的deptIdList
        List<UserDeptPairBO> deptPairList = parseGetToCreateDeptId(userBO.getUserDeptPairParamList(), false);

        if (CollectionUtils.isNotEmpty(deptPairList)) {
            //删除原有用户部门对应关系
            middleUserDepartmentService.removeByUserId(userBO.getId());

            //新增用户组织对应关系
            batchInsertUserDept(deptPairList, userBO.getId());
        }
        //更新用户
        User user = CopyPropertiesUtil.copyBean(userBO, User.class);
        user.setUpdator(ContextUtil.getManageCurrentUser().getUserId());
        user.setUpdated(System.currentTimeMillis());
        userMapper.updateByPrimaryKeySelective(user);
        //更新一下当前用户
        cacheSession(userMapper.selectByPrimaryKey(userBO.getId()),false);
    }

    @Override
    public DictInfoBO getDictInfo(DictParamBO dictParamBO) throws Exception {
        String key = dictParamBO.getKey();
        Object param = dictParamBO.getStatus();
        AbstractDict dict = SpringUtils.getBean(key + Constant.DICT, AbstractDict.class);
        return dict.getDictInfo(param);
    }

    @Override
    public ManagerUserBO getById(Long id) {
        return CopyPropertiesUtil.copyBean(userMapper.selectByPrimaryKey(id), ManagerUserBO.class);
    }

    @Override
    public ManageUserInfoBO getUserInfoById(Long id) {
        ManageUserInfoBO manageUserInfoBO = CopyPropertiesUtil.copyBean(userMapper.selectByPrimaryKey(id), ManageUserInfoBO.class);

        List<UserDeptBO> userCompany = middleUserDepartmentMapperExt.getUserDept(id, EdmDeptEnum.COMPANY.getKey().longValue());
        if (CollectionUtils.isNotEmpty(userCompany)) {
            manageUserInfoBO.setCompany(userCompany.get(0).getDeptName());
            manageUserInfoBO.setCompanyId(userCompany.stream().map(UserDeptBO::getDeptId).collect(Collectors.toList()));
        }

        List<UserDeptBO> userDept = middleUserDepartmentMapperExt.getUserDept(id, EdmDeptEnum.DEPT.getKey().longValue());
        if(CollectionUtils.isEmpty(userDept)){
           userDept = middleUserDepartmentMapperExt.getUserDept(id, EdmDeptEnum.SYSTEM_CENTER.getKey().longValue());
           if(CollectionUtils.isEmpty(userDept)){
               userDept = middleUserDepartmentMapperExt.getUserDept(id, EdmDeptEnum.COMPANY.getKey().longValue());
           }
        }
        manageUserInfoBO.setUserDeptPairList(CopyPropertiesUtil.copyList(userDept,ManageUserDeptBO.class));
        return manageUserInfoBO;
    }

    @Override
    public List<DeptTreeBO> getDeptTree(List<Long> companyIdList) {
        List<ManageDepartmentBO> departmentList = departmentService.listAll();
        List<ManageDepartmentBO> deptParentList;
        if (CollectionUtils.isNotEmpty(companyIdList)) {
            deptParentList = departmentList.stream()
                    .filter(d -> DeptTypeEnum.COMPANY.getKey().equals(d.getDeptType()) && companyIdList.contains(d.getId())).collect(Collectors.toList());
        } else {
            deptParentList = departmentList.stream()
                    .filter(d -> DeptTypeEnum.COMPANY.getKey().equals(d.getDeptType())).collect(Collectors.toList());
        }
        return getChildren(deptParentList, departmentList);
    }

    /**
     * 递归遍历出所有子节点
     *
     * @param deptParentList
     * @param departmentList
     * @return
     */
    private List<DeptTreeBO> getChildren(List<ManageDepartmentBO> deptParentList, List<ManageDepartmentBO> departmentList) {
        List<DeptTreeBO> deptTreeList = new ArrayList<>();
        deptParentList.stream().forEach(d -> {
            DeptTreeBO deptTreeBO = new DeptTreeBO();
            //找出子节点
            List<ManageDepartmentBO> deptChildren = departmentList.stream().filter(c -> d.getId().equals(c.getParentId())).collect(Collectors.toList());
            deptTreeBO.setChildren(getChildren(deptChildren, departmentList));
            deptTreeBO.setLabel(d.getName());
            deptTreeBO.setValue(d.getId());
            deptTreeList.add(deptTreeBO);
        });
        return deptTreeList;
    }

    @Override
    public boolean removeUser(Long id) {
        User user = buildUserToUpdate(id);
        user.setEnabled(false);
        boolean flag=userMapper.updateByPrimaryKeySelective(user) == 1;
        List<Long> list = Arrays.asList(id);
        sessionService.deleteRedisUser(list);
        return flag;
    }

    @Override
    public boolean enableOrDisableUser(EnableOrDisableUserParamVO param) {
        User user = buildUserToUpdate(param.getId());
        user.setStatus(param.getStatus());
        boolean flag=userMapper.updateByPrimaryKeySelective(user) == 1;
        if(!param.getStatus()){
            List<Long> list = Arrays.asList(param.getId());
            sessionService.deleteRedisUser(list);
        }
        return flag;
    }

    /**
     * 生成用户for update
     *
     * @param id
     * @return
     */
    private User buildUserToUpdate(Long id) {
        User user = new User();
        user.setId(id);
        user.setUpdator(ContextUtil.getManageCurrentUser().getUserId());
        user.setUpdated(System.currentTimeMillis());
        return user;
    }

	@Override
	public PersonalInfoBO getPersonalInfo() throws Exception{
		User user = userMapper.selectByPrimaryKey(ContextUtil.getManageCurrentUser().getUserId());
		if(user == null) {
			throw new BusinessException(HoolinkExceptionMassageEnum.MANAGER_USER_NOT_EXIST_ERROR);
		}

		PersonalInfoBO personalInfo = new PersonalInfoBO();
		BeanUtils.copyProperties(user, personalInfo);
		if(user.getImgId() != null) {
			//调用obs服务获取用户头像
			BackBO<ObsBO> obsBackBo = abilityClient.getObs(user.getImgId());
			if(obsBackBo.getData() != null) {
				personalInfo.setImgUrl(obsBackBo.getData().getObjectUrl());
			}
		}

		List<ManageRoleBO> roleList = roleService.listByIdList(Arrays.asList(user.getRoleId()));
		if(CollectionUtils.isNotEmpty(roleList)) {
			personalInfo.setRoleName(roleList.get(0).getRoleName());
		}
		personalInfo.setEncryLevelCompanyName(EncryLevelEnum.getValue(user.getEncryLevelCompany()));
		personalInfo.setStatusDesc(StatusEnum.getValue(user.getStatus()));
		personalInfo.setSexDesc(ManagerUserSexEnum.getValue(user.getSex()));

		// 获取组织树
		List<MiddleUserDeptWithMoreBO> userDepartmentList = middleUserDepartmentService
				.listWithMoreByUserIdList(Arrays.asList(user.getId()));
		Set<String> companySet = new HashSet<>();
		userDepartmentList.stream().filter(ud -> DeptTypeEnum.COMPANY.getKey().equals(ud.getDeptType())).forEach(ud -> {
			companySet.add(ud.getDeptName());
		});
		personalInfo.setCompany(StringUtils.join(companySet, Constant.COMMA));
		// 用户组织关系
		Map<String, List<MiddleUserDeptWithMoreBO>> byDiffDeptGroupMap = userDepartmentList.stream().collect(Collectors.groupingBy(MiddleUserDeptWithMoreBO::getDiffDeptGroup));

		List<DeptPairBO> deptPairList = new ArrayList<>();
		for (Map.Entry<String, List<MiddleUserDeptWithMoreBO>> entry : byDiffDeptGroupMap.entrySet()) {
			List<MiddleUserDeptWithMoreBO> deptWithMoreList = entry.getValue();
			DeptPairBO deptPair = new DeptPairBO();
			deptPair.setDeptIdList(deptWithMoreList.stream().map(dwm -> dwm.getDeptId()).collect(Collectors.toList()));
			deptPair.setDeptNameList(deptWithMoreList.stream().map(dwm -> dwm.getDeptName()).collect(Collectors.toList()));
			if(CollectionUtils.isNotEmpty(deptWithMoreList)) {
				deptPair.setEncryLevelDept(deptWithMoreList.get(0).getEncryLevelDept());
				deptPair.setEncryLevelDeptName(EncryLevelEnum.getValue(deptWithMoreList.get(0).getEncryLevelDept()));
			}
			deptPair.setDeptNameEncryLevelPair(new StringBuilder(StringUtils.join(deptPair.getDeptNameList(), Constant.BACKSLASH)).toString());
			deptPairList.add(deptPair);
		}
		personalInfo.setUserDeptPairList(deptPairList);
		return personalInfo;
	}

    @Override
    public void updatePasswd(UpdatePasswdParamBO updatePasswdParam) {
        //校验手机验证码
        checkPhoneCode(updatePasswdParam.getPhoneParam());
        Long userId = getCurrentUserId();
        User user = buildUserToUpdate(userId);
        user.setPasswd(MD5Util.MD5(updatePasswdParam.getPasswd()));
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public List<ManagerUserBO> listByIdList(List<Long> idList) {
        UserExample example = new UserExample();
        example.createCriteria().andStatusEqualTo(true).andEnabledEqualTo(true);
        return CopyPropertiesUtil.copyList(userMapper.selectByExample(example), ManagerUserBO.class);
    }

    @Override
    public void resetPhone(Long userId) {
        User user = buildUserToUpdate(userId);
        user.setPhone("");
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPasswd(Long userId) {
        User user = buildUserToUpdate(userId);
        //MD5加密，和前端保持一致，"e+iot"拼接密码，加密两次,再后端加密MD5Util.MD5()
        user.setPasswd(MD5Util.MD5(MD5Util.encode(MD5Util.encode(Constant.ENCODE_PASSWORD_PREFIX + Constant.INITIAL_PASSWORD))));
        user.setFirstLogin(true);
        userMapper.updateByPrimaryKeySelective(user);
        sessionService.deleteSession(userId);
    }

    private String setGreeting() {
        Date date = new Date();
        int six = 6;
        int twelve = 12;
        int thirteen = 13;
        int eighteen = 18;
        int twentyFour = 24;
        SimpleDateFormat df = new SimpleDateFormat("HH");
        String str = df.format(date);
        int a = Integer.parseInt(str);
        if (a >= 0 && a <= six) {
            return Constant.GREETING_MORNING;
        }
        if (a > six && a <= twelve) {
            return Constant.GREETING_FORENOON;
        }
        if (a > twelve && a <= thirteen) {
            return Constant.GREETING_NOON;
        }
        if (a > thirteen && a <= eighteen) {
            return Constant.GREETING_AFTERNOON;
        }
        if (a > eighteen && a <= twentyFour) {
            return Constant.GREETING_NIGHT;
        }
        return Constant.GREETING_YOU;
    }

    @Override
    public boolean updateImage(Long imageId) {
        User user = new User();
        Long userId = getCurrentUserId();
        user.setId(userId);
        user.setImgId(imageId);
        user.setUpdator(userId);
        user.setUpdated(System.currentTimeMillis());
        return userMapper.updateByPrimaryKeySelective(user) == 1;
    }
    @Override
    public UserDeptInfoBO getUserSecurity(Long userId) throws Exception{
        UserSecurityBO userSecurity = middleUserDepartmentMapperExt.getUserSecurity(userId);
        if(userSecurity==null){
            throw new BusinessException(HoolinkExceptionMassageEnum.DEPARTMENT_ENCRY_LEVEL_DEFAULT_NULL);
        }
        UserDeptInfoBO userDeptInfoBO = CopyPropertiesUtil.copyBean(userSecurity, UserDeptInfoBO.class);
        //组织架构都可与用户关联
        List<DeptSecurityBO> list = userSecurity.getList();
        if(CollectionUtils.isNotEmpty(list)){
            List<Long> positionList = new ArrayList<>();
            //key positionId
            Map<String, Integer> map = new HashMap<>(list.size());
            //组织架构（最低层级的所有上级）
            Map<Long, Integer> dept = new HashMap<>(list.size());
            list.forEach(deptSecurityBO -> {
                positionList.add(deptSecurityBO.getId());
                if(!EdmDeptEnum.POSITION.getKey().equals(deptSecurityBO.getDeptType().intValue())
                        && deptSecurityBO.getLowestLevel()){
                    dept.put(deptSecurityBO.getId(),deptSecurityBO.getEncryLevelDept());
                } else if(EdmDeptEnum.POSITION.getKey().equals(deptSecurityBO.getDeptType().intValue())
                        && deptSecurityBO.getLowestLevel()){
                    //小组密保等级
                    map.put(deptSecurityBO.getId().toString(),deptSecurityBO.getEncryLevelDept());
                }
            });
            if(!org.springframework.util.CollectionUtils.isEmpty(dept)){
                List<Long> deptList = new ArrayList<>(dept.keySet());
                //本身List也会被查出来
                List<DeptPositionBO> deptPositionBOS = manageDepartmentMapperExt.listByParentIdCode(deptList);
                if(CollectionUtils.isNotEmpty(deptPositionBOS)){
                    deptPositionBOS.forEach(deptPositionBO -> {
                        String parentIdCode = deptPositionBO.getParentIdCode();
                        String[] split = parentIdCode.split(Constant.UNDERLINE);
                        for (int i=0;i<split.length;i++){
                            boolean flag = dept.containsKey(Long.parseLong(split[i]));
                            if(flag){
                                map.put(deptPositionBO.getId().toString(),dept.get(Long.parseLong(split[i])));
                                break;
                            }
                        }
                        positionList.add(deptPositionBO.getId());
                    });
                }
            }
            userDeptInfoBO.setDeptMap(map);
            userDeptInfoBO.setPositionList(ArrayUtil.removeDuplict(positionList));
        }
        return userDeptInfoBO;
    }

    @Override
    public List<ManagerUserBO> getUserList(List<Long> idList) {
        UserExample example = new UserExample();
        example.createCriteria().andStatusEqualTo(true).andEnabledEqualTo(true).andIdIn(idList);
        return CopyPropertiesUtil.copyList(userMapper.selectByExample(example), ManagerUserBO.class);
    }

    @Override
    public Map<Long, List<SimpleDeptUserBO>> mapUserByDeptIds(List<Long> deptIdList) {
        List<SimpleDeptUserBO> userBOList = userMapperExt.selectAllByDeptIds(deptIdList);
        Map<Long, List<SimpleDeptUserBO>> map = userBOList.stream().distinct().collect(Collectors.groupingBy(SimpleDeptUserBO::getDeptId));
        return map;
    }

    @Override
    public List<Long> getOrganizationInfo(OrganizationInfoParamBO paramBO) throws Exception {
        List<Long> deptIdList = new ArrayList<>();
        // 根据用户id获取所在公司或者部门信息
        List<UserDeptAssociationBO> userDeptInfoBOList = middleUserDepartmentMapperExt.getOrganizationInfo(paramBO.getUserId());
        // 根据使用场景不同根据不同组织架构type过滤需要的deptId     1-公司 2-部门 3-小组
        if(Constant.COMPANY_LEVEL.equals(paramBO.getDeptType())){
            deptIdList = userDeptInfoBOList.stream().filter(data -> Constant.COMPANY_LEVEL.equals(data.getDeptType())).map(UserDeptAssociationBO::getDeptId).collect(Collectors.toList());
        }else if(Constant.DEPT_LEVEL.equals(paramBO.getDeptType())){
            deptIdList = userDeptInfoBOList.stream().filter(data -> Constant.DEPT_LEVEL.equals(data.getDeptType())).map(UserDeptAssociationBO::getDeptId).collect(Collectors.toList());
        }
        return deptIdList;
    }

    @Override
    public List<UserDeptAssociationBO> getOrganizationInfoToDept(OrganizationInfoParamBO paramBO) throws Exception {
        // 根据用户id获取所在公司或者部门信息
        List<UserDeptAssociationBO> userDeptInfoBOList = middleUserDepartmentMapperExt.getOrganizationInfo(paramBO.getUserId());
        //过滤出部门以下的层级
        userDeptInfoBOList = userDeptInfoBOList.stream().filter(data -> !Constant.COMPANY_LEVEL.equals(data.getDeptType())
                || !Constant.SYSTEM_CENTER_LEVEL.equals(data.getDeptType())).collect(Collectors.toList());
        return userDeptInfoBOList;
    }

    @Override
    public List<UserDeptAssociationBO> getOrgInfoToCompany(OrganizationInfoParamBO paramBO) throws Exception {
        List<UserDeptAssociationBO> userDeptAssociationBOS = new ArrayList<>();
        // 根据用户id获取所在公司或者部门信息
        List<UserDeptAssociationBO> userDeptInfoBOList = middleUserDepartmentMapperExt.getOrganizationInfo(paramBO.getUserId());
        if(CollectionUtils.isEmpty(userDeptInfoBOList)){
            throw new BusinessException(HoolinkExceptionMassageEnum.ORG_LIST_TREE_ERROR);
        }
        // 根据使用场景不同根据不同组织架构type过滤需要的deptId     1-公司 2-部门 3-小组
        for(UserDeptAssociationBO userDeptAssociationBO : userDeptInfoBOList){
            if(paramBO.getDeptType().equals(userDeptAssociationBO.getDeptType())){
                userDeptAssociationBOS.add(userDeptAssociationBO);
            }
        }
        return userDeptAssociationBOS;
    }

    @Override
    public List<User> getUserNameByIds(List<Long> ids) {
        if(CollectionUtils.isEmpty(ids)){
            return null;
        }
        UserExample example=new UserExample();
        example.createCriteria().andIdIn(ids);
        List<User> list=userMapper.selectByExample(example);
        return list;
    }

    @Override
    public void updateDeviceCode(String deviceCode) throws Exception {
        CurrentUserBO userBO=ContextUtil.getManageCurrentUser();
        User user=new User();
        user.setId(userBO.getUserId());
        user.setDeviceCode(deviceCode);
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public List<MobileFileBO> getCompanyById(Long id) throws Exception {
        if(id==null){
            return null;
        }
        MiddleUserDepartmentExample example=new MiddleUserDepartmentExample();
        example.createCriteria().andUserIdEqualTo(id);
        //根据用户id获取所有的所属组织架构id
        List<MiddleUserDepartment> list=middleUserDepartmentMapper.selectByExample(example);
        List<Long> departmentIds=list.stream().map(d -> d.getDeptId()).collect(Collectors.toList());
        ManageDepartmentExample departmentExample=new ManageDepartmentExample();
        departmentExample.createCriteria().andIdIn(departmentIds);
        //根据他所属的所有组织架构id查询出公司层次的组织架构
        List<ManageDepartment> manageDepartments=manageDepartmentMapper.selectByExample(departmentExample);
        List<ManageDepartment> manageDepartment= manageDepartments.stream().filter(m -> Constant.COMPANY_LEVEL.equals(m.getDeptType())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(manageDepartment)){
            List<MobileFileBO> mobileFiles=new ArrayList<>();
            for(ManageDepartment department:manageDepartment) {
                MobileFileBO mobileFileBO = new MobileFileBO();
                mobileFileBO.setId(department.getId());
                mobileFileBO.setName(department.getName());
                mobileFileBO.setIfDepartment(true);
                mobileFiles.add(mobileFileBO);
            }
            return mobileFiles;
        }
        return null;
    }

    @Override
    public List<MobileFileBO> getDeptByParentId(Long id) throws Exception {
        if(id==null){
            return null;
        }
        //查询下层的时候需要通过人的权限进行过滤
        CurrentUserBO currentUserBO=ContextUtil.getManageCurrentUser();
        List<DeptSecurityRepertoryBO> userList=userMapperExt.getDeptByUser(currentUserBO.getUserId());
        List<Long> deptId=new ArrayList<>();
        for(DeptSecurityRepertoryBO deptSecurity:userList){
            deptId.add(deptSecurity.getDeptId());
            List<DeptSecurityRepertoryBO> childs=deptSecurity.getChilds();
            if(!CollectionUtils.isEmpty(childs)){
                recursionUserDeptId(childs,deptId);
            }
        }
        ManageDepartmentExample example=new ManageDepartmentExample();
        example.createCriteria().andParentIdEqualTo(id).andIdIn(deptId);
        example.setOrderByClause(" convert(dept_name using gbk) collate gbk_chinese_ci asc ");
        List<ManageDepartment> list=manageDepartmentMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        //找出该级别下级的所有
        List<Long> ids=list.stream().map(ManageDepartment::getId).collect(Collectors.toList());
        example.clear();
        example.createCriteria().andParentIdIn(ids);
        List<ManageDepartment> childList=manageDepartmentMapper.selectByExample(example);
        Map<Long,Long> childMap=childList.stream().collect(Collectors.toMap(ManageDepartment::getParentId,ManageDepartment::getId,(key1,key2)->key2));
        List<MobileFileBO> mobileFile=new ArrayList<>();
        list.stream().forEach(m ->{
            MobileFileBO mobileFileBO=new MobileFileBO();
            mobileFileBO.setId(m.getId());
            mobileFileBO.setName(m.getName());
            mobileFileBO.setIfDepartment(true);
            //如果是小组类型就设置为是最下一级组织架构层级
            if(childMap.get(m.getId())!=null){
                mobileFileBO.setIfLastDepartment(false);
            }else{
                mobileFileBO.setIfLastDepartment(true);
            }
            mobileFile.add(mobileFileBO);
        });
        return mobileFile;
    }

    private void recursionUserDeptId(List<DeptSecurityRepertoryBO> deptSecurity,List<Long> deptids){
        for(DeptSecurityRepertoryBO deptSecurityRepertory:deptSecurity){
            deptids.add(deptSecurityRepertory.getDeptId());
            if(!org.springframework.util.CollectionUtils.isEmpty(deptSecurityRepertory.getChilds())){
                List<DeptSecurityRepertoryBO> list=deptSecurityRepertory.getChilds();
                if(!org.springframework.util.CollectionUtils.isEmpty(list)){
                    recursionUserDeptId(list,deptids);
                }
            }
        }
    }

    @Override
    public List<DeptSecurityRepertoryBO> getDeptByUser(Long id){
        return userMapperExt.getDeptByUser(id);
    }

    @Override
    public List<SimpleDeptUserBO> listUserByDeptIds(List<Long> deptIdList) {
        List<SimpleDeptUserBO> userBOList = userMapperExt.selectAllByDeptIds(deptIdList);
        return userBOList;
    }

	@Override
	public String uploadImage(MultipartFile multipartFile) {
		BackBO<ObsBO> obsBo = abilityClient.uploadManager(multipartFile);
		updateImage(obsBo.getData().getId());
        return obsBo.getData().getObjectUrl();
	}

	@Override
	public PageInfo<OperateFileLogBO> listOperateLog(OperateFileLogParamBO paramBO) throws Exception {
		return edmClient.listOperateLog(paramBO).getData();
	}

    @Override
    public SimpleDeptUserBO getUserByDeviceCode(String deviceCode) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andDeviceCodeEqualTo(deviceCode);
        List<User> userList = userMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(userList)){
            return null;
        }
        SimpleDeptUserBO userBO = CopyPropertiesUtil.copyBean(userList.get(0), SimpleDeptUserBO.class);
        return userBO;
    }

    @Override
    public List<Long> getParentDeptByUserId(Long userId) {
        MiddleUserDepartmentExample example=new MiddleUserDepartmentExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<MiddleUserDepartment> list=middleUserDepartmentMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return list.stream().map(m -> m.getDeptId()).collect(Collectors.toList());
    }
}
