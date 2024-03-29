package com.ayi.ayiojbackendcommom.common.shiro;

import com.alibaba.csp.sentinel.util.StringUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ayi.ayiojbackendcommom.utils.ConvertUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * JWT工具类
 **
 */
public class JwtUtil {

	// 过期时间2小时
	public static final long EXPIRE_TIME = 2 * 60 * 60 * 1000;

	/**
	 * 校验token是否正确
	 *
	 * @param token
	 *            密钥
	 * @param secret
	 *            用户的密码
	 * @return 是否正确
	 */
	public static boolean verify(String token, String username, String secret) {
		try {
			// 根据密码生成JWT效验器
			Algorithm algorithm = Algorithm.HMAC256(secret);
			JWTVerifier verifier = JWT.require(algorithm).withClaim("username", username).build();
			// 效验TOKEN
			verifier.verify(token);
			return true;
		} catch (Exception exception) {
			return false;
		}
	}

	/**
	 * 获得token中的信息无需secret解密也能获得
	 *
	 * @return token中包含的用户名
	 */
	public static String getUsername(String token) {
		try {
			DecodedJWT jwt = JWT.decode(token);
			return jwt.getClaim("username").asString();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 生成签名,5min后过期
	 *
	 * @param username
	 *            用户名
	 * @param secret
	 *            用户的密码
	 * @return 加密的token
	 */
	public static String sign(String username, String secret) {
		Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
		Algorithm algorithm = Algorithm.HMAC256(secret);
		// 附带username信息
		return JWT.create().withClaim("username", username).withExpiresAt(date).sign(algorithm);
	}

	/**
	 * 根据request中的token获取用户账号
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String getUserNameByToken(HttpServletRequest request, String token) throws Exception {
		String accessToken = request.getHeader(token);
		String username = getUsername(accessToken);
		if (ConvertUtils.isEmpty(username)) {
			throw new Exception("未获取到用户");
		}
		return username;
	}

	public static String getToken(HttpServletRequest request) {
		String token = null;
		token = request.getHeader("X-Access-Token");
		if (StringUtil.isBlank(token)) {
			token = request.getParameter("token");
		}
		return token;
	}
}
