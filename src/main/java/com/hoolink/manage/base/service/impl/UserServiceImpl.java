package com.hoolink.manage.base.service.impl;

import com.hoolink.manage.base.bo.*;
import com.hoolink.manage.base.bo.ManagerUserInfoBO.UserDeptBO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hoolink.manage.base.constant.Constant;
import com.hoolink.manage.base.consumer.ability.AbilityClient;
import com.hoolink.manage.base.dao.mapper.ManageDepartmentMapper;
import com.hoolink.manage.base.dao.mapper.UserMapper;
import com.hoolink.manage.base.dao.mapper.ext.ManageDepartmentMapperExt;
import com.hoolink.manage.base.dao.mapper.ext.MiddleUserDepartmentMapperExt;
import com.hoolink.manage.base.dao.mapper.ext.UserMapperExt;
import com.hoolink.manage.base.dao.model.ManageDepartment;
import com.hoolink.manage.base.dao.model.ManageDepartmentExample;
import com.hoolink.manage.base.dao.model.User;
import com.hoolink.manage.base.dao.model.UserExample;
import com.hoolink.manage.base.dict.AbstractDict;
import com.hoolink.manage.base.service.DepartmentService;
import com.hoolink.manage.base.service.MiddleUserDepartmentService;
import com.hoolink.manage.base.service.SessionService;
import com.hoolink.manage.base.service.UserService;
import com.hoolink.manage.base.util.SpringUtils;
import com.hoolink.manage.base.vo.res.ManagerUserInfoVO.UserDeptVO;
import com.hoolink.sdk.bo.BackBO;
import com.hoolink.sdk.bo.ability.ObsBO;
import com.hoolink.sdk.bo.ability.SmsBO;
import com.hoolink.sdk.bo.base.CurrentUserBO;
import com.hoolink.sdk.bo.base.UserBO;
import com.hoolink.sdk.bo.manager.*;
import com.hoolink.sdk.enums.CompanyEnum;
import com.hoolink.sdk.enums.EncryLevelEnum;
import com.hoolink.sdk.enums.StatusEnum;
import com.hoolink.sdk.enums.edm.EdmDeptEnum;
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
import com.hoolink.manage.base.service.RoleService;

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
	private static final String DEPT = "dept";
	
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
        currentUserBO.setRoleId(user.getRoleId());
        RoleParamBO role = roleService.getById(user.getRoleId());
        if(role!=null && role.getRoleStatus()) {
        	currentUserBO.setRoleLevel(role.getRoleLevel());
        }
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
		
		//员工列表根据组织架构，全员可见
		User currentUser = userMapper.selectByPrimaryKey(ContextUtil.getManageCurrentUser().getUserId());
		/*if(ContextUtil.getManageCurrentUser().getRoleLevel() != Constant.LEVEL_ONE.equals()){
			criteria.andCompanyEqualTo(currentUser.getCompany());
		}*/
		PageInfo<User> userPageInfo = PageHelper
				.startPage(userPageParamBO.getPageNo(), userPageParamBO.getPageSize())
				.doSelectPageInfo(() -> userMapper.selectByExample(example));
		List<User> userList = userPageInfo.getList();
		
		//查询用户对应部门
		List<Long> userIdList = userList.stream().map(user -> user.getId()).collect(Collectors.toList());
		List<MiddleUserDepartmentBO> middleUserDepartmentBOList = middleUserDepartmentService.listByUserIdList(userIdList);
		Map<Long, List<MiddleUserDepartmentBO>> middleUserDepartmentMap = middleUserDepartmentBOList.stream().collect(Collectors.groupingBy(MiddleUserDepartmentBO::getUserId));
		
		//根据部门id查询部门信息
		List<Long> deptIdList = middleUserDepartmentBOList.stream().map(ud -> ud.getDeptId()).collect(Collectors.toList());
		List<ManageDepartmentBO> departmentList = departmentService.listByIdList(deptIdList);
		
		//查询用户对应角色
		List<Long> roleIdList = userList.stream().map(user -> user.getRoleId()).collect(Collectors.toList());
		List<ManageRoleBO> roleList = roleService.listByIdList(roleIdList);
		
		List<ManagerUserBO> userBoList = new ArrayList<>();
		userList.stream().forEach(user -> {
			ManagerUserBO userBO = new ManagerUserBO();
			userBO.setEncryLevelCompanyName(EncryLevelEnum.getValue(user.getEncryLevelCompany()));
			userBO.setStatusName(StatusEnum.getValue(user.getStatus()));
			ManageRoleBO role = roleList.stream().filter(r -> r.getId().longValue() == user.getRoleId().longValue()).findFirst().orElseGet(ManageRoleBO::new);
			userBO.setRoleName(role.getRoleName());
			
			List<MiddleUserDepartmentBO> userDepartmentList = middleUserDepartmentMap.get(user.getId());
			List<UserDepartmentBO> userDeptPairList = new ArrayList<>();
			userBO.setUserDeptPairList(userDeptPairList);
			
			if(CollectionUtils.isNotEmpty(userDepartmentList)) {
				userDepartmentList.stream().forEach(up -> {
					UserDepartmentBO userDeptPair = new UserDepartmentBO();
					ManageDepartmentBO department = departmentList.stream().filter(d -> d.getId().longValue() == up.getDeptId().longValue()).findFirst().orElseGet(ManageDepartmentBO::new); 
					userDeptPair.setDeptName(department.getName());
					BeanUtils.copyProperties(up, userDeptPair);
					userDeptPair.setEncryLevelDeptName(EncryLevelEnum.getValue(up.getEncryLevelDept()));
					userDeptPairList.add(userDeptPair);
				});
			}
			
			BeanUtils.copyProperties(user, userBO);
			userBoList.add(userBO);
		});
		
		PageInfo<ManagerUserBO> userBOPageInfo = new PageInfo<>();
		userBOPageInfo.setList(userBoList);
		userBOPageInfo.setTotal(userPageInfo.getTotal());
		return userBOPageInfo;
	}

	private void buildUserCriteria(UserExample.Criteria criteria, ManagerUserPageParamBO userPageParamBO) {
		if(StringUtils.isNotBlank(userPageParamBO.getName())) {
			criteria.andNameLike("%" + userPageParamBO.getName() + "%");
		}
		if(StringUtils.isNotBlank(userPageParamBO.getPosition())) {
			criteria.andPositionLike("%" + userPageParamBO.getPosition() + "%");
		}
		if(userPageParamBO.getDeptId() != null) {
			List<MiddleUserDepartmentBO> userDeptList = middleUserDepartmentService.listByDeptId(userPageParamBO.getDeptId());
			List<Long> userIdList = userDeptList.stream().map(ud -> ud.getUserId()).collect(Collectors.toList());
			if(CollectionUtils.isEmpty(userIdList)) {
				criteria.andIdIsNull();
			}else {
				criteria.andIdIn(userIdList);
			}
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

	@Override
	public ManagerUserInfoBO getManagerUserInfo(ManagerUserInfoParamBO userParamBO) throws Exception {
		User user = userMapper.selectByPrimaryKey(userParamBO.getUserId());
		if(user == null) {
			throw new BusinessException(HoolinkExceptionMassageEnum.MANAGER_USER_NOT_EXIST_ERROR);
		}
		
		ManagerUserInfoBO userInfoBO = new ManagerUserInfoBO();
		BeanUtils.copyProperties(user, userInfoBO);
		if(user.getImgId() != null) {
			try {
				BackBO<ObsBO> obsBackBo = abilityClient.getObs(user.getImgId());
				ObsBO obsBO = obsBackBo.getData();
				userInfoBO.setImgUrl(obsBO.getObjectUrl());
			}catch(Exception e) {
				throw new BusinessException(HoolinkExceptionMassageEnum.ACCESS_OBS_FAILED);
			}
		}
		
		//查询用户对应部门
		List<MiddleUserDepartmentBO> userDeptPairList = middleUserDepartmentService.listByUserId(user.getId());
		//只有上级管理员或者文控中心人员可以查看密保等级等敏感信息
		if(ContextUtil.getManageCurrentUser().getRoleLevel() != Constant.LEVEL_ONE && ContextUtil.getManageCurrentUser().getRoleLevel() != Constant.LEVEL_TWO) {
			userInfoBO.setEncryLevelCompany(null);
			userDeptPairList.stream().forEach(udp -> udp.setEncryLevelDept(null));
		}
		userInfoBO.setUserDeptPairList(CopyPropertiesUtil.copyList(userDeptPairList, UserDeptBO.class));
		return userInfoBO;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean createUser(ManagerUserParamBO userBO) throws Exception {
		//校验
		List<UserDeptVO> userDeptPairList = userBO.getUserDeptPairList();
		if(CollectionUtils.isEmpty(userDeptPairList) || userDeptPairList.get(0).getDeptId()==null || userDeptPairList.get(0).getEncryLevelDept()==null) {
			throw new BusinessException(HoolinkExceptionMassageEnum.DEPARTMENT_ENCRY_LEVEL_DEFAULT_NULL);
		}
		//创建用户
		User user = CopyPropertiesUtil.copyBean(userBO, User.class);
		user.setStatus(true);
		user.setCreator(ContextUtil.getManageCurrentUser().getUserId());
		user.setCreated(System.currentTimeMillis());
		user.setEnabled(true);
		user.setFirstLogin(false);
		user.setPasswd(MD5Util.MD5(Constant.INITIAL_PASSWORD));
		userMapper.insertSelective(user);
		
		//用户部门对应关系
		List<MiddleUserDepartmentBO> middleUserDeptList = new ArrayList<>();
		userDeptPairList.stream().forEach(udp -> {
			MiddleUserDepartmentBO middleUserDept = CopyPropertiesUtil.copyBean(udp, MiddleUserDepartmentBO.class);
			middleUserDept.setUserId(user.getId());
			middleUserDeptList.add(middleUserDept);
		});
		return middleUserDepartmentService.batchInsert(middleUserDeptList);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateUser(ManagerUserParamBO userBO) throws Exception {
		boolean flag = false;
		//更新用户
		User user = CopyPropertiesUtil.copyBean(userBO, User.class);
		user.setUpdator(ContextUtil.getManageCurrentUser().getUserId());
		user.setUpdated(System.currentTimeMillis());
		flag = userMapper.updateByPrimaryKeySelective(user)==1;
		
		List<UserDeptVO> userDeptPairList = userBO.getUserDeptPairList();
		if(CollectionUtils.isNotEmpty(userDeptPairList)) {
			for(UserDeptVO userDeptVO : userDeptPairList) {
				if(userDeptVO.getDeptId()==null || userDeptVO.getEncryLevelDept()==null) {
					throw new BusinessException(HoolinkExceptionMassageEnum.DEPARTMENT_ENCRY_LEVEL_DEFAULT_NULL);
				}
			}
			//删除原有用户部门对应关系
			middleUserDepartmentService.removeByUserId(userBO.getId());
			//新增用户部门关系
			List<MiddleUserDepartmentBO> middleUserDeptList = new ArrayList<>();
			userDeptPairList.stream().forEach(udp -> {
				MiddleUserDepartmentBO middleUserDept = CopyPropertiesUtil.copyBean(udp, MiddleUserDepartmentBO.class);
				middleUserDept.setUserId(userBO.getId());
				middleUserDeptList.add(middleUserDept);
			});
			flag = middleUserDepartmentService.batchInsert(middleUserDeptList);
		}
		return flag;
	}

	@Override
	public DictInfoBO getDictInfo(DictParamBO dictParamBO) throws Exception {
		String key = dictParamBO.getKey();
		Object param = null;
		if(DEPT.equals(key)) {
			Integer code = dictParamBO.getCompanyCode();
			param = CompanyEnum.getValue(code);
			if(param == null) {
				throw new BusinessException(HoolinkExceptionMassageEnum.COMPANY_CODE_ERROR);
			}
		}
		AbstractDict dict = SpringUtils.getBean(key + "Dict", AbstractDict.class);
		return dict.getDictInfo(param);
	}

	@Override
	public ManagerUserBO getById(Long id) {
        // 获取用户部门信息
      List<UserDepartmentBO> userDepartmentBOS = CopyPropertiesUtil.copyList(middleUserDepartmentMapperExt.getUserDept(id,2L), UserDepartmentBO.class);
       //  用户基础信息
      ManagerUserBO managerUserBO = CopyPropertiesUtil.copyBean(userMapper.selectByPrimaryKey(id), ManagerUserBO.class);
      managerUserBO.setUserDeptPairList(userDepartmentBOS);
      //  获取用户公司信息
      managerUserBO.setCompany(middleUserDepartmentMapperExt.getUserDept(id,1L).get(0).getDeptName());
      return managerUserBO;
	}

    @Override
    public UserDeptInfoBO getUserSecurity(Long userId) throws Exception{
        UserSecurityBO userSecurity = middleUserDepartmentMapperExt.getUserSecurity(userId);
        if(userSecurity==null){
            throw new BusinessException(HoolinkExceptionMassageEnum.DEPARTMENT_ENCRY_LEVEL_DEFAULT_NULL);
        }
        UserDeptInfoBO userDeptInfoBO = CopyPropertiesUtil.copyBean(userSecurity, UserDeptInfoBO.class);
        //部门 小组 与用户关联
        List<DeptSecurityBO> list = userSecurity.getList();
        if(CollectionUtils.isNotEmpty(list)){
            List<Long> positionList = new ArrayList<>();
            //key positionId
            Map<String, Integer> map = new HashMap<>(list.size());
            Map<Long, Integer> company = new HashMap<>(list.size());
            Map<Long, Integer> dept = new HashMap<>(list.size());
            list.forEach(deptSecurityBO -> {
                if(EdmDeptEnum.COMPANY.getKey().equals(deptSecurityBO.getDeptType().intValue())
                        && deptSecurityBO.getEncryLevelDept()!=null && deptSecurityBO.getEncryLevelDept()!=0){
                    //公司密保等级 转为小组关联
                    company.put(deptSecurityBO.getId(),deptSecurityBO.getEncryLevelDept());
                }else if(EdmDeptEnum.DEPT.getKey().equals(deptSecurityBO.getDeptType().intValue())
                        && deptSecurityBO.getEncryLevelDept()!=null && deptSecurityBO.getEncryLevelDept()!=0){
                    //部门密保等级 转为小组关联
                    dept.put(deptSecurityBO.getId(),deptSecurityBO.getEncryLevelDept());
                }else if(EdmDeptEnum.POSITION.getKey().equals(deptSecurityBO.getDeptType().intValue())
                        && deptSecurityBO.getEncryLevelDept()!=null && deptSecurityBO.getEncryLevelDept()!=0){
                    //小组密保等级
                    positionList.add(deptSecurityBO.getId());
                    map.put(deptSecurityBO.getId().toString(),deptSecurityBO.getEncryLevelDept());
                }
            });
            if(!org.springframework.util.CollectionUtils.isEmpty(company)){
                List<Long> companyList = new ArrayList<>(company.keySet());
                List<ManageDepartment> positionByCompany = manageDepartmentMapperExt.getPositionByCompany(companyList);
                getPositionSecurity(positionList, map, company, positionByCompany);
            }
            if(!org.springframework.util.CollectionUtils.isEmpty(dept)){
                List<Long> deptList = new ArrayList<>(dept.keySet());
                ManageDepartmentExample departmentExample = new ManageDepartmentExample();
                ManageDepartmentExample.Criteria criteria = departmentExample.createCriteria();
                criteria.andParentIdIn(deptList).andEnabledEqualTo(true);
                List<ManageDepartment> departmentList = manageDepartmentMapper.selectByExample(departmentExample);
                getPositionSecurity(positionList, map, dept, departmentList);
            }
            userDeptInfoBO.setDeptMap(map);
            userDeptInfoBO.setPositionList(positionList);
        }
        return userDeptInfoBO;
    }

    /**
     * 将公司 部门 转为小组层面
     * @param positionList
     * @param map
     * @param company
     * @param positionByCompany
     */
    private void getPositionSecurity(List<Long> positionList, Map<String, Integer> map, Map<Long, Integer> company, List<ManageDepartment> positionByCompany) {
        if (CollectionUtils.isNotEmpty(positionByCompany)) {
            positionByCompany.forEach(manageDepartment -> {
                Integer security = company.get(manageDepartment.getParentId());
                if (security != null) {
                    positionList.add(manageDepartment.getId());
                    map.put(manageDepartment.getId().toString(), security);
                }
            });
        }
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
        Map<Long, List<SimpleDeptUserBO>> map = userBOList.stream().collect(Collectors.groupingBy(SimpleDeptUserBO::getDeptId));
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
    public List<UserDeptAssociationBO> getOrgInfoToCompany(OrganizationInfoParamBO paramBO) throws Exception {
        List<UserDeptAssociationBO> userDeptAssociationBOS = new ArrayList<>();
        // 根据用户id获取所在公司或者部门信息
        List<UserDeptAssociationBO> userDeptInfoBOList = middleUserDepartmentMapperExt.getOrganizationInfo(paramBO.getUserId());
        if(CollectionUtils.isEmpty(userDeptInfoBOList)){
            throw new BusinessException(HoolinkExceptionMassageEnum.ORG_LIST_TREE_ERROR);
        }
        // 根据使用场景不同根据不同组织架构type过滤需要的deptId     1-公司 2-部门 3-小组
        for(UserDeptAssociationBO userDeptAssociationBO : userDeptInfoBOList){
            if(Constant.COMPANY_LEVEL.equals(paramBO.getDeptType())){
                userDeptAssociationBOS.add(userDeptAssociationBO);
            }
        }
        return userDeptAssociationBOS;
    }
}
