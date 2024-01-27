package com.ayi.ayiojbackendcommom.constant;



public interface CommonConstant {

    /**
     * 升序
     */
    String SORT_ORDER_ASC = "ascend";

    /**
     * 降序
     */
    String SORT_ORDER_DESC = " descend";

    /**
     * 正常状态
     */
    Integer STATUS_NORMAL = 0;

    /**
     * 禁用状态
     */
    Integer STATUS_DISABLE = -1;

    /**
     * 删除标志
     */
    Integer DEL_FLAG_1 = 1;

    /**
     * 未删除
     */
    Integer DEL_FLAG_0 = 0;

    /**
     * 系统日志类型： 登录
     */
    int LOG_TYPE_1 = 1;

    /**
     * 系统日志类型： 操作
     */
    int LOG_TYPE_2 = 2;


    /**
     * {@code 500 Server Error} (HTTP/1.0 - RFC 1945)
     */
    public static final Integer SC_INTERNAL_SERVER_ERROR_500 = 500;
    /**
     * {@code 200 OK} (HTTP/1.0 - RFC 1945)
     */
    public static final Integer SC_OK_200 = 200;


    public static String PREFIX_USER_ROLE = "PREFIX_USER_ROLE";
    public static String PREFIX_USER_PERMISSION = "PREFIX_USER_PERMISSION";
    public static int TOKEN_EXPIRE_TIME = 3600; //3600秒即是一小时

    public static String PREFIX_USER_TOKEN = "PREFIX_USER_TOKEN";

    public static String PREFIX_QUESTION = "question";

    /**
     * 0：一级菜单
     */
    public static Integer MENU_TYPE_0 = 0;
    /**
     * 1：子菜单
     */
    public static Integer MENU_TYPE_1 = 1;
    /**
     * 2：按钮权限
     */
    public static Integer MENU_TYPE_2 = 2;

    public static String X_ACCESS_TOKEN = "X-Access-Token";

    public static String PARAMETER_ACCESS_TOKEN = "token";

    public static String DEFAULT_PASSWORD = "123456";

    public static String ALIYUN_RDS_HINT = BaseConst.ALIYUN_RDS_HINT;

    //竖向打印类型
    public static String PRINT_VERTICAL = "1";
    //横向打印类型
    public static String PRINT_HORIZONTAL = "2";

    String PASSWD_TYPE_MD5 = "MD5";


    String PASSWD_TYPE_PBE = "PBE";

    /**
     * 系统默认审核用户Id
     */
    public static String SYSTEM_AUDIT_USER_ID = "systemAudit";
}
