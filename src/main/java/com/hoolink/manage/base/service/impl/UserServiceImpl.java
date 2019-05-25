package com.hoolink.manage.base.service.impl;

import com.hoolink.manage.base.bo.*;
import com.hoolink.manage.base.constant.Constant;
import com.hoolink.manage.base.consumer.ability.AbilityClient;
import com.hoolink.manage.base.dao.mapper.UserMapper;
import com.hoolink.manage.base.dao.model.User;
import com.hoolink.manage.base.dao.model.UserExample;
import com.hoolink.manage.base.service.*;
import com.hoolink.sdk.bo.ability.SmsBO;
import com.hoolink.sdk.bo.base.CurrentUserBO;
import com.hoolink.sdk.bo.base.UserBO;
import com.hoolink.sdk.bo.manager.ManagerUserBO;
import com.hoolink.sdk.exception.BusinessException;
import com.hoolink.sdk.exception.HoolinkExceptionMassageEnum;
import com.hoolink.sdk.utils.ContextUtil;
import com.hoolink.sdk.utils.CopyPropertiesUtil;
import com.hoolink.sdk.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
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

        LoginResultBO loginResult = new LoginResultBO();
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
        currentUserBO.setRoleId(user.getRoleId());
        RoleParamBO role = roleService.getById(user.getRoleId());
        if (role != null && role.getRoleStatus()) {
            currentUserBO.setRoleLevel(role.getRoleLevel());
        }
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

    private void buildUserCriteria(UserExample.Criteria criteria, ManagerUserPageParamBO userPageParamBO) {
        if (StringUtils.isNotBlank(userPageParamBO.getName())) {
            criteria.andNameLike("%" + userPageParamBO.getName() + "%");
        }
        if (StringUtils.isNotBlank(userPageParamBO.getPosition())) {
            criteria.andPositionLike("%" + userPageParamBO.getPosition() + "%");
        }
        if (userPageParamBO.getDeptId() != null) {
            List<MiddleUserDepartmentBO> userDeptList = middleUserDepartmentService.listByDeptId(userPageParamBO.getDeptId());
            List<Long> userIdList = userDeptList.stream().map(ud -> ud.getUserId()).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(userIdList)) {
                criteria.andIdIsNull();
            } else {
                criteria.andIdIn(userIdList);
            }
        }
        if (userPageParamBO.getRoleId() != null) {
            criteria.andRoleIdEqualTo(userPageParamBO.getRoleId());
        }
        if (userPageParamBO.getStatus() != null) {
            criteria.andStatusEqualTo(userPageParamBO.getStatus());
        }
        if (StringUtils.isNotBlank(userPageParamBO.getPhone())) {
            criteria.andPhoneEqualTo(userPageParamBO.getPhone());
        }
        if (StringUtils.isNotBlank(userPageParamBO.getUserAccount())) {
            criteria.andUserAccountLike("%" + userPageParamBO.getUserAccount() + "%");
        }
        criteria.andEnabledEqualTo(true);
    }

    @Override
    public ManagerUserBO getById(Long id) {
        return CopyPropertiesUtil.copyBean(userMapper.selectByPrimaryKey(id), ManagerUserBO.class);
    }

    @Override
    public List<ManagerUserBO> listByIdList(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
        }
        UserExample example = new UserExample();
        example.createCriteria().andEnabledEqualTo(true).andStatusEqualTo(true).andIdIn(idList);
        return CopyPropertiesUtil.copyList(userMapper.selectByExample(example), ManagerUserBO.class);
    }
}
