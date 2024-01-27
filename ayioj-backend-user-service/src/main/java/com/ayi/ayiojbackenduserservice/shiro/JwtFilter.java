package com.ayi.ayiojbackenduserservice.shiro;

import com.alibaba.csp.sentinel.util.StringUtil;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @desc 鉴权登录拦截器
 **/
public abstract class JwtFilter extends BasicHttpAuthenticationFilter {

	/**
	 * 执行登录认证
	 *
	 * @param request
	 * @param response
	 * @param mappedValue
	 * @return
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		try {
			executeLogin(request, response);
			return true;
		} catch (Exception e) {
			// throw new AuthenticationException("Token失效，请重新登录", e);
			return false;
		}
	}

	/**
	 * 获取当前token
	 * 
	 * @param request
	 * @return
	 */
	protected String getToken(ServletRequest request) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String token = httpServletRequest.getHeader("X-Access-Token");
		if (StringUtil.isBlank(token)) {
			token = httpServletRequest.getParameter("token");
		}
		return token;
	}

	/**
	 * 将当前token
	 * 
	 * @param token
	 */
	protected void setContextToken(String token) {
	}

	/**
	 *
	 */
	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {

		String token = getToken(request);
		JwtToken jwtToken = new JwtToken(token);
		// 提交给realm进行登入，如果错误他会抛出异常并被捕获
		getSubject(request, response).login(jwtToken);
		// 保存当前token
		setContextToken(token);
		// 如果没有抛出异常则代表登入成功，返回true
		return true;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setStatus(HttpServletResponse.SC_OK); // 设置状态码为 200
		httpResponse.setContentType("application/json;charset=utf-8");

		// 自定义错误信息，你可以根据需要修改 code 和 message 的值
		String json = "{\"code\":\"401\",\"message\":\"Token失效，请重新登录\"}";

		PrintWriter writer = httpResponse.getWriter();
		writer.write(json);
		writer.flush();
		writer.close();

		return false; // 这里返回 false 表示请求结束
	}

	/**
	 * 对跨域提供支持
	 */
	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
		httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
		httpServletResponse.setHeader("Access-Control-Allow-Headers",
				httpServletRequest.getHeader("Access-Control-Request-Headers"));
		// 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
		if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
			httpServletResponse.setStatus(HttpStatus.OK.value());
			return false;
		}
		return super.preHandle(request, response);
	}
}
