package com.hoolink.manage.base.service.impl;

import com.hoolink.manage.base.bo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hoolink.manage.base.constant.Constant;
import com.hoolink.manage.base.consumer.ability.AbilityClient;
import com.hoolink.manage.base.dao.mapper.UserMapper;
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
import com.hoolink.sdk.bo.manager.ManagerUserBO;
import com.hoolink.sdk.bo.manager.ManagerUserBO.UserDepartmentBO;
import com.hoolink.sdk.enums.DeptTypeEnum;
import com.hoolink.sdk.enums.EncryLevelEnum;
import com.hoolink.sdk.enums.ManagerUserSexEnum;
import com.hoolink.sdk.enums.StatusEnum;
import com.hoolink.sdk.exception.BusinessException;
import com.hoolink.sdk.exception.HoolinkExceptionMassageEnum;
import com.hoolink.sdk.utils.ContextUtil;
import com.hoolink.sdk.utils.CopyPropertiesUtil;
import com.hoolink.sdk.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    /*** 验证码超时时间，10分钟 */
    private static final long TIMEOUT_MINUTES = 10;
    /*** 验证码重发间隔，1分钟只允许1次 */
    private static final long REPEAT_PERIOD = 1;

    @Override
    public LoginResultBO login(LoginParamBO loginParam) throws Exception {
        /*
         * 1.校验：1).检查用户及密码能否关联到用户  2).检查客户被禁用  3).检查用户是否被禁用  4).检查用户所属角色是否被禁用
         * 2.返回Token，是否第一次登录（用于用户协议确认），密码是否被重置（用于强制修改密码），最后一次项目
         */
        UserExample example = new UserExample();
        example.createCriteria().andEnabledEqualTo(true)
                .andUserAccountEqualTo(loginParam.getAccount())
                .andPasswdEqualTo(MD5Util.MD5(loginParam.getPasswd()));
        User user = userMapper.selectByExample(example).stream().findFirst().orElse(null);

        // 检查用户密码错误
        checkAccount(user);
        // 缓存当前用户
        String token = cacheSession(user);
        
        //设置登陆时间
		User toUpdateUser = new User();
		toUpdateUser.setId(user.getId());
		toUpdateUser.setLastTime(System.currentTimeMillis());
		userMapper.updateByPrimaryKeySelective(toUpdateUser);

        LoginResultBO loginResult = new LoginResultBO();
        loginResult.setToken(token);
        loginResult.setFirstLogin(user.getFirstLogin());
        loginResult.setPhone(user.getPhone());
        loginResult.setName(user.getName());
        //设置头像
		if(user.getImgId()!=null) {
			try {
				//如果获取头像失败，给予默认头像
				BackBO<ObsBO> obs=abilityClient.getObs(user.getImgId());
				loginResult.setImage(obs.getData().getObjectUrl());
			}catch (Exception e){}
		}
        //设置问候语
        loginResult.setGreetings(setGreeting());
        //设置系统访问权限（EDM和管理平台）
		List<RoleMenuPermissionBO> roleMenuPermissionList = roleService.listMenuAccessByRoleId(user.getRoleId());
		Optional<RoleMenuPermissionBO> edmRoleMenuPermissionOPt = roleMenuPermissionList.stream().filter(rmp -> Constant.EDM.equals(rmp.getMenuCode())).findFirst();
		if(edmRoleMenuPermissionOPt.isPresent()) {
			loginResult.setAccessEDM(true);
		}else {
			loginResult.setAccessEDM(false);
		}
		Optional<RoleMenuPermissionBO> hoolinkRoleMenuPermissionOPt = roleMenuPermissionList.stream().filter(rmp -> Constant.HOOLINK.equals(rmp.getMenuCode())).findFirst();
		if(hoolinkRoleMenuPermissionOPt.isPresent()) {
			loginResult.setAccessHoolink(true);
		}else {
			loginResult.setAccessHoolink(false);
		}
        return loginResult;
    }

    @Override
    public void logout() {
        CurrentUserBO currentUser = sessionService.getCurrentUser(ContextUtil.getManageCurrentUser().getUserId());
        // 避免导致异地登录账号退出
        if (currentUser != null && Objects.equals(currentUser.getToken(), ContextUtil.getManageCurrentUser().getToken())) {
            sessionService.deleteSession(ContextUtil.getManageCurrentUser().getUserId());
        }
    }

    @Override
    public CurrentUserBO getSessionUser(String token) {
        // 获取当前 session 中的用户
        CurrentUserBO currentUser = sessionService.getCurrentUser(token);
        if (currentUser == null) {
            return null;
        }
        // 刷新 session 失效时间
        if (!sessionService.refreshSession(currentUser.getUserId())) {
            return null;
        }
        return currentUser;
    }

    @Override
    public void resetPassword(LoginParamBO loginParam) throws Exception {
        User user = getUserByAccount(loginParam.getAccount());
        //重置密码,并且设置不是首次登录
        user.setId(user.getId());
        user.setPasswd(MD5Util.MD5(loginParam.getPasswd()));
        user.setUpdated(System.currentTimeMillis());
        user.setUpdator(user.getId());
        user.setFirstLogin(false);
        userMapper.updateByPrimaryKeySelective(user);
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
        //校验完了后删除验证码缓存信息，一个验证码只能用一次
        stringRedisTemplate.opsForValue().getOperations().delete(Constant.PHONE_CODE_PREFIX + phoneParamBO.getPhone());
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
        userInfoBO.setRoleName(Constant.USER_ROLE_NAME);
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
    }

    private String cacheSession(User user) throws Exception {
        CurrentUserBO currentUserBO = new CurrentUserBO();
        currentUserBO.setUserId(user.getId());
        currentUserBO.setAccount(user.getUserAccount());
        //设置角色id
        currentUserBO.setRoleId(user.getRoleId());
        //设置角色层级
        ManageRoleBO role = roleService.selectById(user.getRoleId());
        if(role!=null && role.getRoleStatus()) {
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
        return sessionService.cacheCurrentUser(currentUserBO);
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
		if(userPageParamBO.getPageNo()==null || userPageParamBO.getPageSize()==null) {
			throw new BusinessException(HoolinkExceptionMassageEnum.PARAM_ERROR);
		}
		UserExample example = buildUserCriteria(userPageParamBO);
		PageInfo<User> userPageInfo = PageHelper
				.startPage(userPageParamBO.getPageNo(), userPageParamBO.getPageSize())
				.doSelectPageInfo(() -> userMapper.selectByExample(example));
		List<User> userList = userPageInfo.getList();
		
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
			List<UserDepartmentBO> userDeptPairList = new ArrayList<>();
			userBO.setUserDeptPairList(userDeptPairList);
			
			if(CollectionUtils.isNotEmpty(userDepartmentList)) {
				// 用户组织关系
				userDepartmentList.stream().filter(ud -> DeptTypeEnum.DEPARTMENT.getKey().equals(ud.getDeptType()))
						.forEach(ud -> {
							UserDepartmentBO userDeptPair = new UserDepartmentBO();
							BeanUtils.copyProperties(ud, userDeptPair);
							userDeptPairList.add(userDeptPair);
						});

				Set<String> companySet = new HashSet<>();
				userDepartmentList.stream().filter(ud -> DeptTypeEnum.COMPANY.getKey().equals(ud.getDeptType()))
						.forEach(ud -> companySet.add(ud.getDeptName()));
				userBO.setCompany(StringUtils.join(companySet, Constant.COMMA));
			}
			
			BeanUtils.copyProperties(user, userBO);
			userBoList.add(userBO);
		});
		
		PageInfo<ManagerUserBO> userBOPageInfo = CopyPropertiesUtil.copyPageInfo(userPageInfo, ManagerUserBO.class);
		userBOPageInfo.setList(userBoList);
		return userBOPageInfo;
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
			groupCriteria2.andPhoneEqualTo(userPageParamBO.getGroupParam());
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
		//只有一级用户可以看到所有员工， 其他根据组织架构显示员工列表
		if(!Constant.LEVEL_ONE.equals(ContextUtil.getManageCurrentUser().getRoleLevel())){
			transformDeptQueryToUserIdQuery(criteria, ContextUtil.getManageCurrentUser().getComanyIdSet().stream().collect(Collectors.toList()));
		}
		criteria.andEnabledEqualTo(true);
	}

	/**
	 * 将组织相关的查询条件转换为id条件
	 * @param criteria
	 * @param deptIdList
	 */
	private void transformDeptQueryToUserIdQuery(UserExample.Criteria criteria, List<Long> deptIdList) {
		List<MiddleUserDepartmentBO> userDeptList = middleUserDepartmentService.listByDeptIdList(deptIdList);
		List<Long> userIdList = userDeptList.stream().map(ud -> ud.getUserId()).collect(Collectors.toList());
		if(CollectionUtils.isEmpty(userIdList)) {
			criteria.andIdIsNull();
		}else {
			criteria.andIdIn(userIdList);
		}
	}
	
	@Override
	public ManagerUserInfoBO getManagerUserInfo(ManagerUserInfoParamBO userParamBO) throws Exception {
		User user = userMapper.selectByPrimaryKey(userParamBO.getUserId());
		if(user == null) {
			throw new BusinessException(HoolinkExceptionMassageEnum.MANAGER_USER_NOT_EXIST_ERROR);
		}
		
		ManagerUserInfoBO userInfoBO = new ManagerUserInfoBO();
		BeanUtils.copyProperties(user, userInfoBO);
		userInfoBO.setHasLoginYet(user.getLastTime() != null ? true:false);
		if(user.getImgId() != null) {
			//调用obs服务获取用户头像
			BackBO<ObsBO> obsBackBo = abilityClient.getObs(user.getImgId());
			if(obsBackBo.getData() != null) {
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
			if(CollectionUtils.isNotEmpty(deptWithMoreList)) {
				deptPair.setEncryLevelDept(deptWithMoreList.get(0).getEncryLevelDept());
			}
			deptPairList.add(deptPair);
		}
		userInfoBO.setUserDeptPairList(deptPairList);
		
		ManageRoleBO role = roleService.selectById(user.getRoleId());
		userInfoBO.setRoleName(role.getRoleName());
		List<MiddleUserDeptWithMoreBO> companyList = userDeptWithMoreList.stream().filter(udwm -> DeptTypeEnum.COMPANY.getKey().equals(udwm.getDeptType())).collect(Collectors.toList());
		if(CollectionUtils.isNotEmpty(companyList)) {
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
		List<DeptPairBO> deptPairList = parseGetToCreateDeptId(userBO.getUserDeptPairParamList(), true);
		
		//创建用户
		User user = CopyPropertiesUtil.copyBean(userBO, User.class);
		user.setStatus(true);
		user.setCreator(ContextUtil.getManageCurrentUser().getUserId());
		user.setCreated(System.currentTimeMillis());
		user.setEnabled(true);
		user.setFirstLogin(false);
		//MD5加密，和前端保持一致，"e+iot"拼接密码，加密两次,再后端加密MD5Util.MD5()
		user.setPasswd(MD5Util.MD5(MD5Util.encode(MD5Util.encode(Constant.ENCODE_PASSWORD_PREFIX + Constant.INITIAL_PASSWORD))));
		userMapper.insertSelective(user);
		
		//新增用户组织对应关系
		batchInsertUserDept(deptPairList, user.getId());
	}
	
	/**
	 * 查看账号是否已经存在
	 * @param account
	 */
    private void checkAccountExist(String account){
        UserExample example=new UserExample();
        example.createCriteria().andUserAccountEqualTo(account);
        User user = userMapper.selectByExample(example).stream().findFirst().orElse(null);
        if(user != null){
            throw new BusinessException(HoolinkExceptionMassageEnum.USER_ACCOUNT_EXISTS);
        }
    }
    
    /**
     * 查看编号是否已经存在
     * @param userNo
     */
    private void checkUserNoExist(String userNo){
        UserExample example=new UserExample();
        example.createCriteria().andUserNoEqualTo(userNo);
        User user = userMapper.selectByExample(example).stream().findFirst().orElse(null);
        if(user != null){
            throw new BusinessException(HoolinkExceptionMassageEnum.USER_NO_EXISTS);
        }
    }
    
	/**
	 * 遍历拿到所有的父级deptId
	 * @param deptList
	 * @param selectedDeptIdList
	 * @param currentId
	 */
	private void traverseGetAllParentDeptId(List<ManageDepartmentBO> deptList, List<Long> selectedDeptIdList, Long currentId) {
		Optional<ManageDepartmentBO> manageDepartmentBOOpt = deptList.stream().filter(d -> d.getId().equals(currentId)).findFirst();
		if(manageDepartmentBOOpt.isPresent()) {
			Long parentId = manageDepartmentBOOpt.get().getParentId();
			if(parentId != null) {
				selectedDeptIdList.add(parentId);
				traverseGetAllParentDeptId(deptList, selectedDeptIdList, parentId);
			}
		}
	}
	
	/**
	 * 遍历拿到所有的子级deptId
	 * @param deptList
	 * @param selectedDeptIdList
	 * @param currentId
	 */
	private void traverseGetAllChildrenDeptId(List<ManageDepartmentBO> deptList, List<Long> selectedDeptIdList, Long currentId) {
		Optional<ManageDepartmentBO> manageDepartmentBOOpt = deptList.stream().filter(d -> d.getId().equals(currentId)).findFirst();
		if(manageDepartmentBOOpt.isPresent()) {
			List<ManageDepartmentBO> childrenDeptList = deptList.stream().filter(d -> manageDepartmentBOOpt.get().getId().equals(d.getParentId())).collect(Collectors.toList());
			childrenDeptList.stream().forEach(cd -> {
				selectedDeptIdList.add(cd.getId());
				traverseGetAllChildrenDeptId(deptList, selectedDeptIdList, cd.getId());
			});
		}
	}
	
	/**
	 * 参数校验
	 * @param userDeptPairParamList
	 * @param flag
	 * @return
	 */
	private List<DeptPairBO> parseGetToCreateDeptId(List<UserDeptPairParamBO> userDeptPairParamList, boolean flag){
		if(flag && CollectionUtils.isEmpty(userDeptPairParamList)) {
			throw new BusinessException(HoolinkExceptionMassageEnum.DEPARTMENT_ENCRY_LEVEL_DEFAULT_NULL);
		}
		List<ManageDepartmentBO> deptList = departmentService.listAll();
		return validUserDeptPairParam(deptList, userDeptPairParamList);
	}

	/**
	 * 校验用户组织参数
	 * @param deptList
	 * @param userDeptPairParamList
	 * @return
	 */
	private List<DeptPairBO> validUserDeptPairParam(List<ManageDepartmentBO> deptList, List<UserDeptPairParamBO> userDeptPairParamList){
		List<DeptPairBO> deptPairList = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(userDeptPairParamList)) {
			List<List<Long>> allDeptIdList = new ArrayList<>();
			for(UserDeptPairParamBO userDeptPairParam : userDeptPairParamList) {
				List<Long> deptIdGroupList = userDeptPairParam.getDeptIdList();
				if(CollectionUtils.isEmpty(deptIdGroupList)) {
					throw new BusinessException(HoolinkExceptionMassageEnum.DEPARTMENT_ENCRY_LEVEL_DEFAULT_NULL);
				}
				
				//从末节点逐级找到父节点校验
				Long lastDeptId = deptIdGroupList.get(deptIdGroupList.size()-1);
				List<Long> parentOfTheLastDeptId = new ArrayList<>();
				parentOfTheLastDeptId.add(lastDeptId);
				traverseGetAllParentDeptId(deptList, parentOfTheLastDeptId, lastDeptId);
				
				if(!parentOfTheLastDeptId.containsAll(deptIdGroupList) || deptIdGroupList.size()!=parentOfTheLastDeptId.size()) {
					throw new BusinessException(HoolinkExceptionMassageEnum.DEPARTMENT_FORMAT_ERROR);
				}
				
				//判断末节点是否至少为部门类型
				Optional<ManageDepartmentBO> manageDepartmentBOOpt = deptList.stream().filter(d -> d.getId().equals(lastDeptId)).findFirst();
				if(!manageDepartmentBOOpt.isPresent() || DeptTypeEnum.DEPARTMENT.getKey() > manageDepartmentBOOpt.get().getDeptType()) {
					throw new BusinessException(HoolinkExceptionMassageEnum.TYPE_AT_LEASE_DEPT);
				}else if(userDeptPairParam.getEncryLevelDept() == null) {
					throw new BusinessException(HoolinkExceptionMassageEnum.DEPARTMENT_ENCRY_LEVEL_DEFAULT_NULL);
				}
				
				allDeptIdList.add(deptIdGroupList);
			}
			
			//判断节点是否重复或属于父子关系
			if(allDeptIdList.size() > 1) {
				for(int i=0; i<allDeptIdList.size(); i++) {
					List<Long> deptIdGroupList = allDeptIdList.get(i);
					for(int j=i+1; j<allDeptIdList.size(); j++) {
						List<Long> afterDeptIdGroupList = allDeptIdList.get(j);
						if(deptIdGroupList.containsAll(afterDeptIdGroupList) || afterDeptIdGroupList.containsAll(deptIdGroupList)) {
							throw new BusinessException(HoolinkExceptionMassageEnum.DEPARTMENT_REPEAT_OR_CONTAIN);
						}
					}
				}
			}
			
			for(int i=0; i<allDeptIdList.size(); i++) {
				List<Long> deptIdList = allDeptIdList.get(i);
				//从末节点逐级找到子节点入库
				Long lastDeptId = deptIdList.get(deptIdList.size()-1);
				List<Long> chidrenDeptId = new ArrayList<>();
				traverseGetAllChildrenDeptId(deptList, chidrenDeptId, lastDeptId);
				List<Long> toStoreDeptIdList = new ArrayList<>();
				toStoreDeptIdList.addAll(deptIdList);
				toStoreDeptIdList.addAll(chidrenDeptId);
				DeptPairBO deptPair = new DeptPairBO();
				deptPair.setDeptIdList(toStoreDeptIdList);
				deptPair.setEncryLevelDept(userDeptPairParamList.get(i).getEncryLevelDept());
				deptPairList.add(deptPair);
			}
		}
		return deptPairList;
	}
	
	/**
	 * 新增用户组织对应关系
	 * @param deptPairList
	 * @param userId
	 */
	private void batchInsertUserDept(List<DeptPairBO> deptPairList, Long userId) {
		List<MiddleUserDepartmentBO> middleUserDeptList = new ArrayList<>();
		deptPairList.stream().forEach(dp -> {
			String diffDeptGroup = generateRandom();
			for(Long deptId : dp.getDeptIdList()) {
				MiddleUserDepartmentBO middleUserDept = new MiddleUserDepartmentBO();
				middleUserDept.setDeptId(deptId);
				middleUserDept.setUserId(userId);
				middleUserDept.setEncryLevelDept(dp.getEncryLevelDept());
				middleUserDept.setDiffDeptGroup(diffDeptGroup);
				middleUserDeptList.add(middleUserDept);
			}
		});
		middleUserDepartmentService.batchInsert(middleUserDeptList);
	}
	
	/**
	 * 生成随机字符串
	 * @return
	 */
	private String generateRandom() {
		int eight = 8;
		StringBuilder str=new StringBuilder(UUID.randomUUID().toString().replaceAll(Constant.RUNG, ""));
		Random random=new Random();
		for(int i=0; i<eight; i++){
		    str.append(random.nextInt(10));
		}
		return str.toString();
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateUser(ManagerUserParamBO userBO) throws Exception {
		//查看账号是否已经存在
		if(StringUtils.isNotEmpty(userBO.getUserAccount())) {
			checkAccountExist(userBO.getUserAccount());
		}
		if(StringUtils.isNotEmpty(userBO.getUserNo())) {
			checkUserNoExist(userBO.getUserNo());
		}
		//得到将要入库的deptIdList
		List<DeptPairBO> deptPairList = parseGetToCreateDeptId(userBO.getUserDeptPairParamList(), false);
		
		if(CollectionUtils.isNotEmpty(deptPairList)) {
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
	}

	@Override
	public DictInfoBO getDictInfo(DictParamBO dictParamBO) throws Exception {
		String key = dictParamBO.getKey();
		Object param = null;
		AbstractDict dict = SpringUtils.getBean(key + Constant.DICT, AbstractDict.class);
		return dict.getDictInfo(param);
	}

	@Override
	public ManagerUserBO getById(Long id) {
		return CopyPropertiesUtil.copyBean(userMapper.selectByPrimaryKey(id), ManagerUserBO.class);
	}

	@Override
	public List<DeptTreeBO> getDeptTree(List<Long> companyIdList) {
		List<ManageDepartmentBO> departmentList = departmentService.listAll();
		List<ManageDepartmentBO> deptParentList;
		if(CollectionUtils.isNotEmpty(companyIdList)) {
			deptParentList = departmentList.stream()
					.filter(d -> DeptTypeEnum.COMPANY.getKey().equals(d.getDeptType()) && companyIdList.contains(d.getId())).collect(Collectors.toList());
		}else {
			deptParentList = departmentList.stream()
					.filter(d -> DeptTypeEnum.COMPANY.getKey().equals(d.getDeptType())).collect(Collectors.toList());
		}
		return getChildren(deptParentList, departmentList);
	}
	
	/**
	 * 递归遍历出所有子节点
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
		return userMapper.updateByPrimaryKeySelective(user) == 1;
	}

	@Override
	public boolean enableOrDisableUser(EnableOrDisableUserParamVO param) {
		User user = buildUserToUpdate(param.getId());
		user.setStatus(param.getStatus());
		return userMapper.updateByPrimaryKeySelective(user) == 1;
	}
	
	/**
	 * 生成用户for update
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
		
		List<MiddleUserDeptWithMoreBO> userDeptPairList = userDepartmentList.stream().filter(ud -> DeptTypeEnum.DEPARTMENT.getKey().equals(ud.getDeptType())).collect(Collectors.toList());
		personalInfo.setUserDeptPairList(CopyPropertiesUtil.copyList(userDeptPairList, UserDepartmentBO.class));
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
	public void resetPasswd(Long userId) {
		User user = buildUserToUpdate(userId);
		//MD5加密，和前端保持一致，"e+iot"拼接密码，加密两次,再后端加密MD5Util.MD5()
		user.setPasswd(MD5Util.MD5(MD5Util.encode(MD5Util.encode(Constant.ENCODE_PASSWORD_PREFIX + Constant.INITIAL_PASSWORD))));
		userMapper.updateByPrimaryKeySelective(user);
	}

	private String setGreeting(){
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
		user.setId(getCurrentUserId());
		user.setImgId(imageId);
		return userMapper.updateByPrimaryKeySelective(user) == 1;
	}
}
