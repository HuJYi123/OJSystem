package com.ayi.ayiojbackenduserservice.shiro;

import cn.hutool.core.util.StrUtil;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class InvestJwtFilter extends JwtFilter {

	protected String getToken(ServletRequest request, ServletResponse response) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String token = httpServletRequest.getHeader("X-Access-Token");
		if (StrUtil.isBlank(token)) {
			token = httpServletRequest.getParameter("token");
		}
		return token;
	}

	@Override
	protected void setContextToken(String token) {
//		ContextHolder.setToken(token);
	}

	@Override
	protected void postHandle(ServletRequest request, ServletResponse response) throws Exception {
		super.postHandle(request, response);
		// 清除上下文
//		ContextHolder.remove();
	}

}
