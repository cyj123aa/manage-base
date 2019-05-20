package com.hoolink.manage.base.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.servicecomb.common.rest.filter.HttpServerFilter;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.foundation.vertx.http.HttpServletRequestEx;
import org.apache.servicecomb.swagger.invocation.Response;
import com.hoolink.manage.base.service.UserService;
import com.hoolink.manage.base.util.RedisUtil;
import com.hoolink.manage.base.util.SpringUtils;
import com.hoolink.sdk.bo.base.CurrentUserBO;
import com.hoolink.sdk.constants.ContextConstant;
import com.hoolink.sdk.enums.PermissionEnum;
import com.hoolink.sdk.exception.HoolinkExceptionMassageEnum;
import com.hoolink.sdk.utils.BackVOUtil;

import lombok.extern.slf4j.Slf4j;

import com.hoolink.manage.base.bo.RoleMenuPermissionBO;
import com.hoolink.manage.base.service.RoleService;
/**
 * 登录校验及权限拦截
 * @author lijunling
 *
 * @date 2019/05/18 09:30
 */
@Slf4j
public class AuthFilter implements HttpServerFilter{
	
	private static final String FUZZY_MATCH = ".*";
	private static final String BACK_SLASH = "/";
	private static final String LOGIN_URL = "/login";
	private static final String GET_SESSION_USER_URL = "/getSessionUser";
	private static final String CREATE_PREFIX = "/create";
	private static final String REMOVE_PREFIX = "/remove";
	private static final String UPDATE_PREFIX = "/update";
	
	private UserService userService = SpringUtils.getBean("userServiceImpl", UserService.class);
	
	private RoleService roleService = SpringUtils.getBean("roleServiceImpl", RoleService.class);
	
    private RedisUtil redisUtil = SpringUtils.getBean("redisUtil", RedisUtil.class);
    
	@Override
	public Response afterReceiveRequest(Invocation invocation, HttpServletRequestEx requestEx) {
		String uri = requestEx.getRequestURI();
		if(uri.endsWith(LOGIN_URL) || uri.endsWith(GET_SESSION_USER_URL)) {
			return null;
		}
		String token = invocation.getContext(ContextConstant.TOKEN);
		CurrentUserBO user = userService.getSessionUser(token);
		//未登录状态
		if(user == null) {
			return Response.succResp(
                    BackVOUtil.operateError(HoolinkExceptionMassageEnum.NOT_LOGIN_ERROR.getMassage()));
		}
		try {
			//根据roleId获取对应菜单权限
			List<RoleMenuPermissionBO> roleMenuPermissionList = new ArrayList<>();
			
			Map<Object,Object> map = redisUtil.hmget(user.getRoleId().toString());
			if(map != null && !map.isEmpty()) {
				for (Map.Entry<Object, Object> entry : map.entrySet()) {
					RoleMenuPermissionBO roleMenuPermission = (RoleMenuPermissionBO)entry.getValue();
					roleMenuPermissionList.add(roleMenuPermission);
				}
			}else {
				roleMenuPermissionList = roleService.listMenuAccessByRoleId(user.getRoleId());
			}
			
			//根据菜单码做判断， 比如管理中心(xx)-用户管理(user),会去匹配uri=/user/**结尾的请求
			RoleMenuPermissionBO roleMenuPermission = roleMenuPermissionList.stream().filter(rmp -> Pattern.matches(FUZZY_MATCH + BACK_SLASH + rmp.getMenuCode() + BACK_SLASH + FUZZY_MATCH, uri)).findFirst().orElse(null);
			
			if(roleMenuPermission == null) {
				//没有权限
				return Response.succResp(
	                    BackVOUtil.operateError(HoolinkExceptionMassageEnum.FORBIDDEN.getMassage()));
			}else {
				//权限级别 1-只读 2-全部(读、写)
				Integer permissionFlag = roleMenuPermission.getPermissionFlag();
				if(permissionFlag == PermissionEnum.READ_ONLY.getKey()) {
					//按钮级别修改的话默认判断create、remove、update
					if(uri.contains(CREATE_PREFIX) || uri.contains(REMOVE_PREFIX) || uri.contains(UPDATE_PREFIX)) {
						//没有权限
						return Response.succResp(
			                    BackVOUtil.operateError(HoolinkExceptionMassageEnum.FORBIDDEN.getMassage()));
					}
				}else if(permissionFlag == PermissionEnum.ALL.getKey()) {
					//放行
					return null;
				}
			}			
		}catch(Exception e) {
			log.error("auth verify failed..., exception:{}", e);
			return Response.succResp(
                    BackVOUtil.operateError(HoolinkExceptionMassageEnum.FORBIDDEN.getMassage()));
		}
		return null;
	}

	@Override
	public int getOrder() {
		return 0;
	}

}
