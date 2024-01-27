package com.ayi.ayiojbackendcommom.constant;

import java.math.BigDecimal;

public interface BaseConst {

	public String MATERIAL_CATEGORY_ROOT_PARENT_ID = "-1";
	
	public BigDecimal ZERO = new BigDecimal(0);
	
	public BigDecimal ONE = new BigDecimal(1);
	
	public BigDecimal ONE_NEG = new BigDecimal(-1);
	
	public BigDecimal HUNDRED = new BigDecimal(100);
	
	public String BLANK = "";
	
	public static String ALIYUN_RDS_HINT = "/*FORCE_MASTER*/";
	
	public static String SYSTEM_AUDIT_USER_ID = "systemAudit";
	
	public static String SYSTEM_AUDIT_USER_NAME = "系统";
	
	String DEFAULT_LEAD_WAREHOUSE_NUMBER_PREFIX = "LWH";
	
	String DEFAULT_LOCATION_NUMBER = "_default";

	String DEFAULT_LOCATION_NAME = "默认库位";
	
}
