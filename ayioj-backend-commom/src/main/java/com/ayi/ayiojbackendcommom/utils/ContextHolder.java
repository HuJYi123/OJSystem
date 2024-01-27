//package com.ayi.ayibackendcommom.utils;
//
//
//import com.ayi.ayiojbackendcommom.utils.Context;
//import com.ayi.ayiojbackendcommom.utils.SpringContextUtils;
//
//public class ContextHolder {
//
//	private static TransmittableThreadLocal<Context> contextHolder = new TransmittableThreadLocal<>();
//
//	public static void remove() {
//		contextHolder.remove();
//	}
//
//	public static void setToken(String token) {
//		String userName = JwtUtil.getUsername(token);
//		if (userName != null) {
//			contextHolder.set(new Context(token, userName));
//		} else {
//			contextHolder.set(null);
//		}
//	}
//
//	public static String getToken() {
//		if (contextHolder.get() == null) {
//			return null;
//		}
//
//		return contextHolder.get().getToken();
//	}
//
//	public static String getCurrentUserId() {
//		User sysUser = getCurrentUser();
//		return sysUser == null ? null : sysUser.getId().toString();
//	}
//
//	public static User getCurrentUser() {
//		if (contextHolder.get() == null) {
//			return null;
//		}
//
//		UserService sysUserService = SpringContextUtils.getBean(UserService.class);
//		return sysUserService.getUserByName(contextHolder.get().getUserName());
//	}
//
////	public static List<String> getCurrentUserMaterialCategory() {
////		// 获取当前用户id
////		User sysUser = ContextHolder.getCurrentUser();
////		MainContextProvider cainContextProvider = SpringContextUtils.getBean(MainContextProvider.class);
////		return cainContextProvider.getCurrentUserMaterialCategory(sysUser.getId().toString());
////	}
//
////	public static List<String> getCurrentUserDepId() {
////		// 获取当前用户id
////		User sysUser = ContextHolder.getCurrentUser();
////		MainContextProvider cainContextProvider = SpringContextUtils.getBean(MainContextProvider.class);
////		return cainContextProvider.getCurrentUserDepId(sysUser.getId().toString());
////	}
//
//}
