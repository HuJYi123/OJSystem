package com.ayi.ayiojbackenduserservice.shiro;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.ayi.ayiojbackendcommom.constant.CommonConstant;
import com.ayi.ayiojbackendcommom.utils.ConvertUtils;
import com.ayi.ayiojbackendcommom.utils.RedisKeyUtil;
import com.ayi.ayiojbackendmodel.model.entity.User;
import com.ayi.ayiojbackenduserservice.service.SysPermissionService;
import com.ayi.ayiojbackenduserservice.service.SysRoleService;
import com.ayi.ayiojbackenduserservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;

/**
 * Realm
 * @desc
 **/
@Component
@Slf4j
public class MyRealm extends AuthorizingRealm {

	@Autowired
	@Lazy
	private SysPermissionService sysPermissionService;
	@Autowired
	@Lazy
	private UserService userService;

	@Autowired
	@Lazy
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private SysRoleService sysRoleService;
	/**
	 * 必须重写此方法，不然Shiro会报错
	 */
	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof JwtToken;
	}

	/**
	 * 获取授权信息 Shiro中，只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		log.debug("————权限认证 [ roles、permissions]————");
		User sysUser = null;
		String username = null;
		if (principals != null) {
			sysUser = (User) principals.getPrimaryPrincipal();
			username = sysUser.getUserAccount();
		}

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		// 设置该用户拥有角色
		List<String> roles = null;
		// 从redis缓存中查询权限角色
		String key = RedisKeyUtil.assemblyServiceKey(CommonConstant.PREFIX_USER_ROLE, username);
		String rolesStr = stringRedisTemplate.opsForValue().get(key);
		if (StrUtil.isNotBlank(rolesStr)) {
			roles = JSON.parseArray(rolesStr.toString(), String.class);
		} else {
			// 从数据库查询权限放到redis中
			roles = sysRoleService.getRole(username);
			if (!roles.isEmpty()) {
				stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(roles));
			}
		}
		// 设置超时时间（1小时）
		stringRedisTemplate.expire(key, Duration.ofSeconds(CommonConstant.TOKEN_EXPIRE_TIME));

		/**
		 * 设置该用户拥有的角色，比如“admin,test”
		 */
		info.setRoles(new HashSet<>(roles));

		// 从数据库获取所有的权限
//		Set<String> permissionSet = new HashSet<>();
//		List<Permission> permissionList = sysPermissionService.queryByUser(username);
//		for (Permission po : permissionList) {
//			if (ConvertUtils.isNotEmpty(po.getUrl()) || ConvertUtils.isNotEmpty(po.getPerms())) {
//				if (ConvertUtils.isNotEmpty(po.getUrl())) {
//					// TODO URL是怎么控制的？
//					permissionSet.add(po.getUrl());
//				} else if (ConvertUtils.isNotEmpty(po.getPerms())) {
//					permissionSet.add(po.getPerms());
//				}
//
//			}
//		}
//
//		info.addStringPermissions(permissionSet);
		return info;
	}

	/**
	 * 获取身份验证信息 Shiro中，默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
	 * 
	 * @param
	 * @return 返回封装了用户信息的 AuthenticationInfo 实例
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
		log.debug("————身份认证方法————");
		String token = (String) auth.getCredentials();
		if (token == null) {
			throw new AuthenticationException("token为空!");
		}
		// 解密获得username，用于和数据库进行对比
		String username = JwtUtil.getUsername(token);
		if (username == null) {
			throw new AuthenticationException("token非法无效!");
		}

		// 查询用户信息
		User sysUser = userService.getUserByName(username);
		if (sysUser == null) {
			throw new AuthenticationException("用户不存在!");
		}

		// 校验token是否超时失效 & 或者账号密码是否错误
		if (!jwtTokenRefresh(token, username, sysUser.getUserPassword())) {
			throw new AuthenticationException("用户名或密码错误!");
		}

		return new SimpleAuthenticationInfo(sysUser, token, getName());
	}

	/**
	 * JWTToken刷新生命周期 （解决用户一直在线操作，提供Token失效问题）
	 * 1、登录成功后将用户的JWT生成的Token作为k、v存储到cache缓存里面(这时候k、v值一样)
	 * 2、当该用户再次请求时，通过JWTFilter层层校验之后会进入到doGetAuthenticationInfo进行身份验证
	 * 3、当该用户这次请求JWTToken值还在生命周期内，则会通过重新PUT的方式k、v都为Token值，缓存中的token值生命周期时间重新计算(这时候k、v值一样)
	 * 4、当该用户这次请求jwt生成的token值已经超时，但该token对应cache中的k还是存在，则表示该用户一直在操作只是JWT的token失效了，程序会给token对应的k映射的v值重新生成JWTToken并覆盖v值，该缓存生命周期重新计算
	 * 5、当该用户这次请求jwt在生成的token值已经超时，并在cache中不存在对应的k，则表示该用户账户空闲超时，返回用户信息已失效，请重新登录。
	 * 6、每次当返回为true情况下，都会给Response的Header中设置Authorization，该Authorization映射的v为cache对应的v值。
	 * 7、注：当前端接收到Response的Header中的Authorization值会存储起来，作为以后请求token使用
	 * 参考方案：https://blog.csdn.net/qq394829044/article/details/82763936
	 * 
	 * @param userName
	 * @param passWord
	 * @return
	 */
	public boolean jwtTokenRefresh(String token, String userName, String passWord) {
		String key = RedisKeyUtil.assemblyServiceKey(CommonConstant.PREFIX_USER_TOKEN, token);
		String cacheToken = stringRedisTemplate.opsForValue().get(key);
		if (ConvertUtils.isNotEmpty(cacheToken)) {
			// 校验token有效性
			if (!JwtUtil.verify(token, userName, passWord)) {
				String newAuthorization = JwtUtil.sign(userName, passWord);
				stringRedisTemplate.opsForValue().set(key, newAuthorization);
				// 设置超时时间
				stringRedisTemplate.expire(key, Duration.ofSeconds(JwtUtil.EXPIRE_TIME));
			} else {
				stringRedisTemplate.opsForValue().set(key, cacheToken);
				// 设置超时时间
				stringRedisTemplate.expire(key, Duration.ofSeconds(JwtUtil.EXPIRE_TIME));
			}
			return true;
		}
		return false;
	}

}
