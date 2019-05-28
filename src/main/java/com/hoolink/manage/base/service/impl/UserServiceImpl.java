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
import com.hoolink.sdk.enums.StatusEnum;
import com.hoolink.sdk.enums.ViewEncryLevelPermittedEnum;
import com.hoolink.sdk.exception.BusinessException;
import com.hoolink.sdk.exception.HoolinkExceptionMassageEnum;
import com.hoolink.sdk.utils.ContextUtil;
import com.hoolink.sdk.utils.CopyPropertiesUtil;
import com.hoolink.sdk.utils.DateUtil;
import com.hoolink.sdk.utils.ExcelUtil;
import com.hoolink.sdk.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import com.hoolink.manage.base.service.RoleService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
    public LoginResultBO login(LoginParamBO loginParam)throws Exception {
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

        LoginResultBO loginResult=new LoginResultBO();
        loginResult.setToken(token);
        loginResult.setFirstLogin(user.getFirstLogin());
        loginResult.setPhone(user.getPhone());



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
        User user=getUserByAccount(loginParam.getAccount());
        //重置密码,并且设置不是首次登录
        user.setId(user.getId());
        user.setPasswd(MD5Util.MD5(loginParam.getPasswd()));
        user.setUpdated(System.currentTimeMillis());
        user.setUpdator(user.getId());
        user.setFirstLogin(false);
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public String getPhoneCode(String phone) throws Exception {
        //生成随机6位数字
        String code= RandomStringUtils.randomNumeric(Constant.PHONE_COED_LENGTH);
        //调用ability发送验证码
        SmsBO smsBO=new SmsBO();
        String content = messageSource.getMessage("sms.captcha", new Object[]{code}, Locale.getDefault());
        smsBO.setContent(content);
        smsBO.setPhone(phone);
        abilityClient.sendMsg(smsBO);
        // 缓存剩余时间
        Long remainingTime = stringRedisTemplate.opsForValue().getOperations().getExpire(Constant.PHONE_CODE_PREFIX+phone, TimeUnit.MINUTES);
        // 默认1分钟之内仅进行1次验证码发送业务
        if (remainingTime != null && TIMEOUT_MINUTES - remainingTime < REPEAT_PERIOD) {
            throw new BusinessException(HoolinkExceptionMassageEnum.CAPTCHA_CACHE_TOO_FREQUENTLY);
        }
        //手机号与验证码存入
        stringRedisTemplate.opsForValue().set(Constant.PHONE_CODE_PREFIX+phone,code, TIMEOUT_MINUTES, TimeUnit.MINUTES);
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
        User user=new User();
        user.setId(userId);
        user.setPhone(bindPhoneParam.getPhone());
        user.setUpdator(userId);
        user.setUpdated(System.currentTimeMillis());
        userMapper.updateByPrimaryKeySelective(user);
        //删除缓存中的验证码信息
        stringRedisTemplate.opsForValue().getOperations().delete(Constant.PHONE_CODE_PREFIX+bindPhoneParam.getPhone());
    }

    private String checkPhoneCode(PhoneParamBO phoneParamBO){
        String code=null;
        try{
            code=stringRedisTemplate.opsForValue().get(Constant.PHONE_CODE_PREFIX+phoneParamBO.getPhone());
        }catch (Exception e){
            e.printStackTrace();
            log.error("从redis中获取手机验证码异常,手机号为{}，验证码为{}",phoneParamBO.getPhone(),phoneParamBO.getCode());
        }
        if(StringUtils.isBlank(code) || !Objects.equals(code,phoneParamBO.getCode())){
            throw new BusinessException(HoolinkExceptionMassageEnum.PHONE_CODE_ERROR);
        }
        //校验完了后删除验证码缓存信息，一个验证码只能用一次
        stringRedisTemplate.opsForValue().getOperations().delete(Constant.PHONE_CODE_PREFIX+phoneParamBO.getPhone());
        return code;
    }

    @Override
    public String forgetPassword(LoginParamBO loginParam) throws Exception {
        User user=getUserByAccount(loginParam.getAccount());
        if(StringUtils.isBlank(user.getPhone())){
            throw new BusinessException(HoolinkExceptionMassageEnum.NOT_BIND_PHONE);
        }
        return user.getPhone();
    }

    @Override
    public UserInfoBO getUserInfo() throws Exception {
        Long userId = getCurrentUserId();
        User user = userMapper.selectByPrimaryKey(userId);
        if(user==null){
            throw new BusinessException(HoolinkExceptionMassageEnum.USER_NOT_EXIST_ERROR);
        }
        //封装结果
        UserInfoBO userInfoBO=new UserInfoBO();
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
        CurrentUserBO currentUserBO=new CurrentUserBO();
        currentUserBO.setUserId(user.getId());
        currentUserBO.setAccount(user.getUserAccount());
        //设置角色id
        currentUserBO.setRoleId(user.getRoleId());
        //设置角色层级
		ManageRoleBO baseRole = roleService.getBaseRole(user.getRoleId());
		if(baseRole!=null && baseRole.getRoleStatus()) {
        	currentUserBO.setRoleLevel(baseRole.getRoleLevel());
        }
        //设置所属公司
		List<MiddleUserDeptWithMoreBO> middleUserDeptWithMoreBOList = middleUserDepartmentService
				.listWithMoreByUserIdList(Arrays.asList(user.getId()));
		currentUserBO.setComanyIdSet(middleUserDeptWithMoreBOList.stream()
				.filter(cudwm -> DeptTypeEnum.COMPANY.getKey().equals(cudwm.getDeptType()))
				.map(cudwm -> cudwm.getDeptId()).collect(Collectors.toSet()));
        //设置权限url
        currentUserBO.setAuthUrls(roleService.listAccessUrlByRoleId(user.getRoleId()));
        return sessionService.cacheCurrentUser(currentUserBO);
    }

    private Long getCurrentUserId(){
        //获取当前登录用户
        Long userId=ContextUtil.getManageCurrentUser().getUserId();
        if(userId==null){
            throw new BusinessException(HoolinkExceptionMassageEnum.USER_NOT_EXIST_ERROR);
        }
        return userId;
    }

    private User getUserByAccount(String account){
        UserExample example=new UserExample();
        example.createCriteria().andUserAccountEqualTo(account);
        User user=userMapper.selectByExample(example).stream().findFirst().orElse(null);
        if(user==null){
            throw new BusinessException(HoolinkExceptionMassageEnum.ACCOUNT_NOT_EXIST);
        }
        return user;
    }

    private void checkPhoneExist(String phone){
        //查看手机号是否已经存在
        UserExample example=new UserExample();
        example.createCriteria().andEnabledEqualTo(true).andPhoneEqualTo(phone);
        User user=userMapper.selectByExample(example).stream().findFirst().orElse(null);
        if(user !=null){
            throw new BusinessException(HoolinkExceptionMassageEnum.USER_PHONE_EXISTS);
        }
    }

	@Override
	public PageInfo<ManagerUserBO> list(ManagerUserPageParamBO userPageParamBO) throws Exception {
		UserExample example = new UserExample();
		UserExample.Criteria criteria = example.createCriteria();
		buildUserCriteria(criteria, userPageParamBO);
		
		//只有一级用户可以看到所有员工， 其他根据组织架构显示员工列表
		if(!Constant.LEVEL_ONE.equals(ContextUtil.getManageCurrentUser().getRoleLevel())){
			transformDeptQueryToUserIdQuery(criteria, ContextUtil.getManageCurrentUser().getComanyIdSet().stream().collect(Collectors.toList()));
		}
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
			userBO.setViewEncryLevelPermittedDesc(
					ViewEncryLevelPermittedEnum.getValue(user.getViewEncryLevelPermitted()));
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
						.forEach(ud -> {
							companySet.add(ud.getDeptName());
						});
				userBO.setCompany(companySet.toString());
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
	 * @param criteria
	 * @param userPageParamBO
	 */
	private void buildUserCriteria(UserExample.Criteria criteria, ManagerUserPageParamBO userPageParamBO) {
		if(StringUtils.isNotBlank(userPageParamBO.getName())) {
			criteria.andNameLike("%" + userPageParamBO.getName() + "%");
		}
		if(StringUtils.isNotBlank(userPageParamBO.getPosition())) {
			criteria.andPositionLike("%" + userPageParamBO.getPosition() + "%");
		}
		if(userPageParamBO.getDeptId() != null) {
			transformDeptQueryToUserIdQuery(criteria, Arrays.asList(userPageParamBO.getDeptId()));
		}
		if(userPageParamBO.getRoleId() != null) {
			criteria.andRoleIdEqualTo(userPageParamBO.getRoleId());
		}
		if(userPageParamBO.getStatus() != null) {
			criteria.andStatusEqualTo(userPageParamBO.getStatus());
		}
		if(StringUtils.isNotBlank(userPageParamBO.getPhone())) {
			criteria.andPhoneEqualTo(userPageParamBO.getPhone());
		}
		if(StringUtils.isNotBlank(userPageParamBO.getUserAccount())) {
			criteria.andUserAccountLike("%" + userPageParamBO.getUserAccount() + "%");
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
		if(user.getImgId() != null) {
			//调用obs服务获取用户头像
			BackBO<ObsBO> obsBackBo = abilityClient.getObs(user.getImgId());
			if(obsBackBo.getData() != null) {
				userInfoBO.setImgUrl(obsBackBo.getData().getObjectUrl());
			}
		}
		
		// 获取组织树
		List<MiddleUserDeptWithMoreBO> middleUserDeptWithMoreList = middleUserDepartmentService.listWithMoreByUserIdList(Arrays.asList(user.getId()));
		List<Long> companyIdList = middleUserDeptWithMoreList.stream()
				.filter(mudwm -> DeptTypeEnum.COMPANY.getKey().equals(mudwm.getDeptType()))
				.map(mudwm -> mudwm.getDeptId()).collect(Collectors.toList());
		
		List<DeptTreeBO> deptTreeList = getDeptTree(companyIdList, middleUserDeptWithMoreList);
		userInfoBO.setDeptTreeList(deptTreeList);
		
		//是否可见员工密保等级
		if(!user.getViewEncryLevelPermitted()) {
			userInfoBO.setEncryLevelCompany(null);
			
			deptTreeList.stream().forEach(d -> {
				removeEncryLevelCompanyAttr(d);
			});
		}
		return userInfoBO;
	}
	
	/**
	 * 如果员工密保等级不可见，encryLevelDept置为null
	 * @param parentDeptTree
	 */
	private void removeEncryLevelCompanyAttr(DeptTreeBO parentDeptTree) {
		List<DeptTreeBO> children = parentDeptTree.getChildren();
		children.stream().forEach(c -> {
			c.setEncryLevelDept(null);
			removeEncryLevelCompanyAttr(c);
		});
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void createUser(ManagerUserParamBO userBO) throws Exception {
		//查看账号是否已经存在
		checkAccountExist(userBO.getUserAccount());
		List<UserDeptPairParamBO> userDeptPairParamList = userBO.getUserDeptPairParamList();
		//得到将要入库的deptIdList
		Set<Long> toCreateDeptIdSet = parseGetToCreateDeptIdSet(userBO.getUserDeptPairParamList());
		
		//创建用户
		User user = CopyPropertiesUtil.copyBean(userBO, User.class);
		user.setStatus(true);
		user.setCreator(ContextUtil.getManageCurrentUser().getUserId());
		user.setCreated(System.currentTimeMillis());
		user.setEnabled(true);
		user.setFirstLogin(false);
		user.setPasswd(MD5Util.MD5(Constant.INITIAL_PASSWORD));
		userMapper.insertSelective(user);
		
		//新增用户组织对应关系
		batchInsertUserDept(toCreateDeptIdSet, userDeptPairParamList, user.getId());
	}
	
	/**
	 * 查看账号是否已经存在
	 * @param account
	 */
    private void checkAccountExist(String account){
        UserExample example=new UserExample();
        example.createCriteria().andEnabledEqualTo(true).andStatusEqualTo(true).andUserAccountEqualTo(account);
        User user = userMapper.selectByExample(example).stream().findFirst().orElse(null);
        if(user != null){
            throw new BusinessException(HoolinkExceptionMassageEnum.USER_ACCOUNT_EXISTS);
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
	 * 解析参数获得将要入库的deptIdList
	 * @param userDeptPairParamList
	 * @return
	 */
	private Set<Long> parseGetToCreateDeptIdSet(List<UserDeptPairParamBO> userDeptPairParamList){
		if(CollectionUtils.isEmpty(userDeptPairParamList)) {
			throw new BusinessException(HoolinkExceptionMassageEnum.DEPARTMENT_ENCRY_LEVEL_DEFAULT_NULL);
		}
		
		List<ManageDepartmentBO> deptList = departmentService.listAll();
		//判断选中的组织等级是否含有部门且有密保等级
		boolean isContainDeptAndEncryLevel = false;
		for(UserDeptPairParamBO userDeptPairParam : userDeptPairParamList) {
			Optional<ManageDepartmentBO> manageDepartmentBOOpt = deptList.stream().filter(d -> d.getId().equals(userDeptPairParam.getDeptId())).findFirst();
			if(manageDepartmentBOOpt.isPresent() && DeptTypeEnum.DEPARTMENT.getKey() <= manageDepartmentBOOpt.get().getDeptType() && userDeptPairParam.getEncryLevelDept() != null) {
				isContainDeptAndEncryLevel = true;
				break;
			}
		}
		if(!isContainDeptAndEncryLevel) {
			throw new BusinessException(HoolinkExceptionMassageEnum.DEPARTMENT_ENCRY_LEVEL_DEFAULT_NULL);
		}
		
		List<Long> selectedDeptIdList = userDeptPairParamList.stream().map(udpp -> udpp.getDeptId()).collect(Collectors.toList());
		//找出选中的所有末节点(没有子节点的)
		for(Iterator<Long> it = selectedDeptIdList.iterator(); it.hasNext();) {
			Long selectedDeptId = it.next();
			List<Long> chidrenOfSelectedDeptId = new ArrayList<>();
			traverseGetAllChildrenDeptId(deptList, chidrenOfSelectedDeptId, selectedDeptId);
			chidrenOfSelectedDeptId.retainAll(selectedDeptIdList);
			if(CollectionUtils.isNotEmpty(chidrenOfSelectedDeptId)) {
				//如果不是末节点，直接删除
				it.remove();
			}
		}

		//得到选中的所有的末节点之后，得到对应的父节点
		List<Long> parentOfSelectedDeptId = new ArrayList<>();
		selectedDeptIdList.stream().forEach(sdi -> {
			parentOfSelectedDeptId.add(sdi);
			traverseGetAllParentDeptId(deptList, parentOfSelectedDeptId, sdi);
		});
		
		//得到选中的所有的末节点之后，得到对应的子节点
		List<Long> chidrenOfSelectedDeptId = new ArrayList<>();
		selectedDeptIdList.stream().forEach(sdi -> {
			traverseGetAllChildrenDeptId(deptList, chidrenOfSelectedDeptId, sdi);
		});
		
		//将要入库的deptIdList
		Set<Long> toCreateDeptIdSet = new HashSet<>();
		toCreateDeptIdSet.addAll(parentOfSelectedDeptId);
		toCreateDeptIdSet.addAll(chidrenOfSelectedDeptId);
		return toCreateDeptIdSet;
	}

	/**
	 * 新增用户组织对应关系
	 * @param toCreateDeptIdSet
	 * @param userDeptPairParamList
	 * @param userId
	 */
	private void batchInsertUserDept(Set<Long> toCreateDeptIdSet, List<UserDeptPairParamBO> userDeptPairParamList, Long userId) {
		List<MiddleUserDepartmentBO> middleUserDeptList = new ArrayList<>();
		toCreateDeptIdSet.stream().forEach(tcdi -> {
			MiddleUserDepartmentBO middleUserDept = new MiddleUserDepartmentBO();
			middleUserDept.setDeptId(tcdi);
			Optional<UserDeptPairParamBO> userDeptPairParamBOOpt = userDeptPairParamList.stream().filter(udp -> tcdi.equals(udp.getDeptId())).findFirst();
			userDeptPairParamBOOpt.ifPresent(u -> middleUserDept.setEncryLevelDept(u.getEncryLevelDept()));
			middleUserDept.setUserId(userId);
			middleUserDeptList.add(middleUserDept);
		});
		middleUserDepartmentService.batchInsert(middleUserDeptList);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateUser(ManagerUserParamBO userBO) throws Exception {
		List<UserDeptPairParamBO> userDeptPairParamList = userBO.getUserDeptPairParamList();
		//得到将要入库的deptIdList
		Set<Long> toCreateDeptIdSet = parseGetToCreateDeptIdSet(userBO.getUserDeptPairParamList());
		
		//删除原有用户部门对应关系
		middleUserDepartmentService.removeByUserId(userBO.getId());
		
		//新增用户组织对应关系
		batchInsertUserDept(toCreateDeptIdSet, userDeptPairParamList, userBO.getId());
		
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
	public List<DeptTreeBO> getDeptTree(List<Long> companyIdList, List<MiddleUserDeptWithMoreBO> middleUserDeptWithMoreList) {
		List<ManageDepartmentBO> departmentList = departmentService.listAll();
		List<ManageDepartmentBO> deptParentList = departmentList.stream()
				.filter(d -> DeptTypeEnum.COMPANY.getKey().equals(d.getDeptType()) && companyIdList.contains(d.getId())).collect(Collectors.toList());
		return getChildren(deptParentList, departmentList, middleUserDeptWithMoreList);
	}
	
	/**
	 * 递归遍历出所有子节点
	 * @param deptParentList
	 * @param departmentList
	 * @param middleUserDeptWithMoreList
	 * @return
	 */
	private List<DeptTreeBO> getChildren(List<ManageDepartmentBO> deptParentList, List<ManageDepartmentBO> departmentList, List<MiddleUserDeptWithMoreBO> middleUserDeptWithMoreList) {
		List<DeptTreeBO> deptTreeList = new ArrayList<>();
		deptParentList.stream().forEach(d -> {
			DeptTreeBO deptTreeBO = new DeptTreeBO();
			//找出子节点
			List<ManageDepartmentBO> deptChildren = departmentList.stream().filter(c -> d.getId().equals(c.getParentId())).collect(Collectors.toList());
			deptTreeBO.setChildren(getChildren(deptChildren, departmentList, middleUserDeptWithMoreList));
			
			if(CollectionUtils.isNotEmpty(middleUserDeptWithMoreList)) {
				Optional<MiddleUserDeptWithMoreBO> middleUserDeptWithMoreOpt = middleUserDeptWithMoreList.stream().filter(mudwm -> d.getId().equals(mudwm.getDeptId())).findFirst();
				if(middleUserDeptWithMoreOpt.isPresent()) {
					deptTreeBO.setEncryLevelDept(middleUserDeptWithMoreOpt.get().getEncryLevelDept());
					deptTreeBO.setChecked(true);
				}
			}
			
			BeanUtils.copyProperties(d, deptTreeBO);
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
		personalInfo.setViewEncryLevelPermittedDesc(
				ViewEncryLevelPermittedEnum.getValue(user.getViewEncryLevelPermitted()));
		
		// 获取组织树
		List<MiddleUserDeptWithMoreBO> userDepartmentList = middleUserDepartmentService
				.listWithMoreByUserIdList(Arrays.asList(user.getId()));
		Set<String> companySet = new HashSet<>();
		userDepartmentList.stream().filter(ud -> DeptTypeEnum.COMPANY.getKey().equals(ud.getDeptType())).forEach(ud -> {
			companySet.add(ud.getDeptName());
		});
		personalInfo.setCompany(companySet.toString());
		
		List<MiddleUserDeptWithMoreBO> userDeptPairList = userDepartmentList.stream().filter(ud -> DeptTypeEnum.DEPARTMENT.getKey().equals(ud.getDeptType())).collect(Collectors.toList());
		personalInfo.setUserDeptPairList(CopyPropertiesUtil.copyList(userDeptPairList, UserDepartmentBO.class));
		return personalInfo;
	}

	@Override
	public ResponseEntity<org.springframework.core.io.Resource> exportList(ManagerUserPageParamBO userPageParamBO)
			throws Exception {
		PageInfo<ManagerUserBO> pageInfo = list(userPageParamBO);
		List<ManagerUserBO> userList = pageInfo.getList();
		//表头
        List<String> head = new ArrayList<>();
        head.add(Constant.EXCEL_USER_NO);
        head.add(Constant.EXCEL_USER_NAME);
        head.add(Constant.EXCEL_USER_POSITION);
        head.add(Constant.EXCEL_USER_DEPT_PAIR);
        head.add(Constant.EXCEL_USER_ROLENAME);
        head.add(Constant.EXCEL_USER_COMPANY);
        head.add(Constant.EXCEL_USER_PHONE);
        head.add(Constant.EXCEL_USER_ACCOUNT);
        head.add(Constant.EXCEL_USER_VIEW_ENCRY_PERMITTED);
        head.add(Constant.EXCEL_USER_ENCRY_LEVEL_COMPANY);
        head.add(Constant.EXCEL_USER_STATUS);
        head.add(Constant.EXCEL_USER_LAST_TIME);
        head.add(Constant.EXCEL_USER_CREATED);
        
        //表体
        List<List<String>> contents = new ArrayList<>();
        userList.stream().forEach(user -> {
        	List<String> content = new ArrayList<>();
            content.add(user.getUserNo());
            content.add(user.getName());
            content.add(user.getPosition());
            List<UserDepartmentBO> userDeptPairList = user.getUserDeptPairList();
            StringBuilder sb = new StringBuilder();
            userDeptPairList.stream().forEach(udp -> {
            	sb.append(udp.getDeptName()).append(Constant.BACKSLASH).append(udp.getEncryLevelDeptName()).append(Constant.SEMICOLON);
            });
            if(sb.length() > 0) {
            	sb.deleteCharAt(sb.lastIndexOf(Constant.SEMICOLON));
            }
            content.add(sb.toString());
            content.add(user.getRoleName());
            content.add(user.getCompany());
            content.add(user.getPhone());
            content.add(user.getUserAccount());
            content.add(user.getViewEncryLevelPermittedDesc());
            content.add(user.getEncryLevelCompanyName());
            content.add(user.getStatusDesc());
            if (user.getLastTime() == null) {
                content.add(Constant.NO_DATA);
            }else {
            	content.add(DateUtil.getStringByTimeStamp(user.getLastTime()));
            }
            if (user.getCreated() != null) {
                content.add(DateUtil.getStringByTimeStamp(user.getCreated()));
            }
            contents.add(content);
        });
		return ExcelUtil.getResponseEntity(head, contents, Constant.USER_EXPORT_EXCEL_TITLE);
	}

	@Override
	public ResponseEntity<org.springframework.core.io.Resource> downloadTemplate() throws Exception {
		Workbook workbook = buildUserWorkbook();
		return ExcelUtil.export(workbook, Constant.HOOLINK_USER_EXPORT_EXCEL_TITLE);
	}
	
	private Workbook buildUserWorkbook() {
		Workbook wb = new HSSFWorkbook();
		Sheet hideSheet = wb.createSheet(Constant.EXCEL_HIDE_SHEET);
		//wb.setSheetHidden(wb.getSheetIndex(Constant.HIDE_SHEET), true);
		
		//获取组织架构字典
		List<DictPairForExcelBO> deptPairListForExcel = listDeptPairForExcel();
		//获取角色字典
		deptPairListForExcel.add(getRolePairForExcel());
		//获取加密等级字典值
		deptPairListForExcel.add(getEncryLevelPairForExcel());
		//获取是否可见员工密保等级字典值
		deptPairListForExcel.add(getViewEncryPermittedPairForExcel());
		//插入组织架构属性到隐藏的sheet
		int rowId = 0;
		for(DictPairForExcelBO deptPairForExcel : deptPairListForExcel) {
			Row row = hideSheet.createRow(rowId++);
			//插入对应id属性
			Row idrow = hideSheet.createRow(rowId++);
			DictPairBO<Long, String> parentDeptPair = deptPairForExcel.getParentDictPair();
			row.createCell(0).setCellValue(parentDeptPair.getValue());
			idrow.createCell(0).setCellValue(parentDeptPair.getKey());
			
			List<DictPairBO<Long, String>> childrenDeptPairList = deptPairForExcel.getChildrenDictPairList();
			for(int j=0; j<childrenDeptPairList.size(); j++) {
				row.createCell(j + 1).setCellValue(childrenDeptPairList.get(j).getValue());
				idrow.createCell(j + 1).setCellValue(childrenDeptPairList.get(j).getKey());
			}
			
			// 添加名称管理器
			String range = getRange(1, rowId-1, childrenDeptPairList.size());
			Name name = wb.createName();
			name.setNameName(parentDeptPair.getValue());
			String formula = Constant.EXCEL_HIDE_SHEET + Constant.EXCLAMATION_MARK + range;
			name.setRefersToFormula(formula);
		}
		
		
		
		//设置表头
		//员工编号、姓名、职位、所属公司、所属部门、所属team、部门密保等级、所属角色、联系电话、账号、是否可见员工密级、资源库密保等级
		String[] headerArray = {Constant.EXCEL_USER_NO, Constant.EXCEL_USER_NAME, Constant.EXCEL_USER_POSITION, Constant.EXCEL_USER_COMPANY, Constant.EXCEL_USER_DEPT,
				Constant.EXCEL_USER_TEAM, Constant.EXCEL_USER_ENCRY_LEVEL_DEPT, Constant.EXCEL_USER_ROLENAME, Constant.EXCEL_USER_PHONE, Constant.EXCEL_USER_ACCOUNT,
				Constant.EXCEL_USER_VIEW_ENCRY_PERMITTED, Constant.EXCEL_USER_ENCRY_LEVEL_COMPANY};
		Sheet sheet1 = wb.createSheet(Constant.EXCEL_SHEET1);
		Row row0 = sheet1.createRow(0);
		for(int i=0; i<headerArray.length; i++) {
			row0.createCell(i).setCellValue(headerArray[i]);
		}
		
		//设置公式
		List<FormulaForExcelBO> formulaForExcelList = new ArrayList<>();
		formulaForExcelList.add(new FormulaForExcelBO(Constant.EXCEL_USER_COMPANY, Constant.EXCEL_COMPANY_LIST, HoolinkExceptionMassageEnum.EXCEL_COMPANY_ERROR.getMassage()));
		formulaForExcelList.add(new FormulaForExcelBO(Constant.EXCEL_USER_DEPT, Constant.EXCEL_DEPT_FORMULA, HoolinkExceptionMassageEnum.EXCEL_DEPT_ERROR.getMassage()));
		formulaForExcelList.add(new FormulaForExcelBO(Constant.EXCEL_USER_TEAM, Constant.EXCEL_TEAM_FORMULA, HoolinkExceptionMassageEnum.EXCEL_TEAM_ERROR.getMassage()));
		formulaForExcelList.add(new FormulaForExcelBO(Constant.EXCEL_USER_ROLENAME, Constant.EXCEL_ROLE_LIST, HoolinkExceptionMassageEnum.EXCEL_ROLE_ERROR.getMassage()));
		formulaForExcelList.add(new FormulaForExcelBO(Constant.EXCEL_USER_ENCRY_LEVEL_DEPT, Constant.EXCEL_ENCRY_LEVEL_LIST, HoolinkExceptionMassageEnum.EXCEL_ENCRY_LEVEL_ERROR.getMassage()));
		formulaForExcelList.add(new FormulaForExcelBO(Constant.EXCEL_USER_ENCRY_LEVEL_COMPANY, Constant.EXCEL_ENCRY_LEVEL_LIST, HoolinkExceptionMassageEnum.EXCEL_ENCRY_LEVEL_ERROR.getMassage()));
		formulaForExcelList.add(new FormulaForExcelBO(Constant.EXCEL_USER_VIEW_ENCRY_PERMITTED, Constant.EXCEL_VIEW_ENCRY_PERMITTED_LIST, HoolinkExceptionMassageEnum.EXCEL_VIEW_ENCRY_PERMITTED_ERROR.getMassage()));
		
		for(FormulaForExcelBO formulaForExcel : formulaForExcelList) {
			List<String> headerList = Arrays.asList(headerArray);
			if(headerList.contains(formulaForExcel.getKey())) {
				int index = headerList.indexOf(formulaForExcel.getKey());
				DVConstraint formula = DVConstraint.createFormulaListConstraint(formulaForExcel.getFormula());
				CellRangeAddressList rangeAddressList = new CellRangeAddressList(1, 10000, index, index);
				DataValidation cacse = new HSSFDataValidation(rangeAddressList, formula);
				cacse.createErrorBox(Constant.ERROR, formulaForExcel.getErrMsg());
				sheet1.addValidationData(cacse);					
			}
		}
		sheet1.setForceFormulaRecalculation(true);
		return wb;
	}
	
	/**
	 * 得到组织架构的excel属性
	 * @return
	 */
	private List<DictPairForExcelBO> listDeptPairForExcel(){
		List<DictPairForExcelBO> deptPairForExcelList = new ArrayList<>();
		List<ManageDepartmentBO> deptList = departmentService.listAll();
		List<ManageDepartmentBO> parentDeptList = deptList.stream().filter(d -> d.getParentId()==null).collect(Collectors.toList());
		
		//加入顶级组织
		DictPairForExcelBO deptPairForExcel = new DictPairForExcelBO();
		DictPairBO<Long, String> parentDeptPair = new DictPairBO<>();
		parentDeptPair.setKey(-1L);
		parentDeptPair.setValue(Constant.EXCEL_COMPANY_LIST);
		deptPairForExcel.setParentDictPair(parentDeptPair);
		List<DictPairBO<Long, String>> childrenDeptPairList = new ArrayList<>();
		deptPairForExcel.setChildrenDictPairList(childrenDeptPairList);
		deptPairForExcelList.add(deptPairForExcel);
		
		//遍历查找到每个组织的直接子级
		parentDeptList.stream().forEach(pd -> {
			DictPairBO<Long, String> childDeptPair = new DictPairBO<>();
			childDeptPair.setKey(pd.getId());
			childDeptPair.setValue(pd.getName());
			childrenDeptPairList.add(childDeptPair);
			
			buildDeptPairForExcel(deptPairForExcelList, deptList, pd);
		});
		return deptPairForExcelList;
	}
	
	/**
	 * 遍历查找到每个组织的直接子级
	 * @param deptPairForExcelList
	 * @param deptList
	 * @param parentDept
	 */
	private void buildDeptPairForExcel(List<DictPairForExcelBO> deptPairForExcelList, List<ManageDepartmentBO> deptList, ManageDepartmentBO parentDept) {
		DictPairForExcelBO deptPairForExcel = new DictPairForExcelBO();
		
		DictPairBO<Long, String> parentDeptPair = new DictPairBO<>();
		parentDeptPair.setKey(parentDept.getId());
		parentDeptPair.setValue(parentDept.getName());
		deptPairForExcel.setParentDictPair(parentDeptPair);
		
		//查找直接子级
		List<DictPairBO<Long, String>> childrenDeptPairList = new ArrayList<>();
		List<ManageDepartmentBO> childrenDeptList = deptList.stream().filter(d -> parentDept.getId().equals(d.getParentId())).collect(Collectors.toList());
		if(CollectionUtils.isNotEmpty(childrenDeptList)) {
			//存在子级
			deptPairForExcelList.add(deptPairForExcel);
			childrenDeptList.stream().forEach(cd -> {
				DictPairBO<Long, String> childDeptPair = new DictPairBO<>();
				childDeptPair.setKey(cd.getId());
				childDeptPair.setValue(cd.getName());
				childrenDeptPairList.add(childDeptPair);
				buildDeptPairForExcel(deptPairForExcelList, deptList, cd);
			});
		}
		deptPairForExcel.setChildrenDictPairList(childrenDeptPairList);
	}
	
	private String getRange(int offset, int rowId, int colCount) {
		char start = (char)('A' + offset);
		if (colCount <= 25) {
			char end = (char)(start + colCount - 1);
			return "$" + start + "$" + rowId + ":$" + end + "$" + rowId;
		} else {
			char endPrefix = 'A';
			char endSuffix = 'A';
			if ((colCount - 25) / 26 == 0 || colCount == 51) {// 26-51之间，包括边界（仅两次字母表计算）
				if ((colCount - 25) % 26 == 0) {// 边界值
					endSuffix = (char)('A' + 25);
				} else {
					endSuffix = (char)('A' + (colCount - 25) % 26 - 1);
				}
			} else {// 51以上
				if ((colCount - 25) % 26 == 0) {
					endSuffix = (char)('A' + 25);
					endPrefix = (char)(endPrefix + (colCount - 25) / 26 - 1);
				} else {
					endSuffix = (char)('A' + (colCount - 25) % 26 - 1);
					endPrefix = (char)(endPrefix + (colCount - 25) / 26);
				}
			}
			return "$" + start + "$" + rowId + ":$" + endPrefix + endSuffix + "$" + rowId;
		}
	}
	/**
	 * 获取角色字典值
	 * @return
	 */
	private DictPairForExcelBO getRolePairForExcel(){
		DictPairForExcelBO rolePairForExcel = new DictPairForExcelBO();
		DictPairBO<Long, String> parentRolePair = new DictPairBO<>();
		parentRolePair.setKey(-1L);
		parentRolePair.setValue(Constant.EXCEL_ROLE_LIST);
		rolePairForExcel.setParentDictPair(parentRolePair);
		
		List<DictPairBO<Long, String>> childrenRolePairList = new ArrayList<>();
		rolePairForExcel.setChildrenDictPairList(childrenRolePairList);
		List<ManageRoleBO> roleList = roleService.list();
		roleList.stream().forEach(r -> {
			DictPairBO<Long, String> childRolePair = new DictPairBO<>();
			childRolePair.setKey(r.getId());
			childRolePair.setValue(r.getRoleName());
			childrenRolePairList.add(childRolePair);
		});
		return rolePairForExcel;
	}
	
	/**
	 * 获取加密等级字典值
	 * @return
	 */
	private DictPairForExcelBO getEncryLevelPairForExcel(){
		DictPairForExcelBO encryLevelPairForExcel = new DictPairForExcelBO();
		DictPairBO<Long, String> parentEncryLevelPair = new DictPairBO<>();
		parentEncryLevelPair.setKey(-1L);
		parentEncryLevelPair.setValue(Constant.EXCEL_ENCRY_LEVEL_LIST);
		encryLevelPairForExcel.setParentDictPair(parentEncryLevelPair);
		
		List<DictPairBO<Long, String>> childrenEncryLevelPairList = new ArrayList<>();
		encryLevelPairForExcel.setChildrenDictPairList(childrenEncryLevelPairList);
		for(EncryLevelEnum encryLevelEnum : EncryLevelEnum.values()) {
			DictPairBO<Long, String> childEncryLevelPair = new DictPairBO<>();
			childEncryLevelPair.setKey(encryLevelEnum.getKey().longValue());
			childEncryLevelPair.setValue(encryLevelEnum.getValue());
			childrenEncryLevelPairList.add(childEncryLevelPair);
		}
		return encryLevelPairForExcel;
	}
	
	/**
	 * 获取是否可见员工密保等级字典值
	 * @return
	 */
	private DictPairForExcelBO getViewEncryPermittedPairForExcel(){
		DictPairForExcelBO viewEncryPermittedPairForExcel = new DictPairForExcelBO();
		DictPairBO<Long, String> parentViewEncryPermittedPair = new DictPairBO<>();
		parentViewEncryPermittedPair.setKey(-1L);
		parentViewEncryPermittedPair.setValue(Constant.EXCEL_VIEW_ENCRY_PERMITTED_LIST);
		viewEncryPermittedPairForExcel.setParentDictPair(parentViewEncryPermittedPair);
		
		List<DictPairBO<Long, String>> childrenViewEncryPermittedPairList = new ArrayList<>();
		viewEncryPermittedPairForExcel.setChildrenDictPairList(childrenViewEncryPermittedPairList);
		for(ViewEncryLevelPermittedEnum viewEncryLevelPermittedEnum : ViewEncryLevelPermittedEnum.values()) {
			DictPairBO<Long, String> childViewEncryPermittedPair = new DictPairBO<>();
			childViewEncryPermittedPair.setKey(viewEncryLevelPermittedEnum.getKey()==true ? 1L:0L);
			childViewEncryPermittedPair.setValue(viewEncryLevelPermittedEnum.getValue());
			childrenViewEncryPermittedPairList.add(childViewEncryPermittedPair);
		}
		return viewEncryPermittedPairForExcel;
	}
}
