package com.hoolink.manage.base.constant;


/**
 * @author chenzhixiong
 * @date 2019/4/16 14:47
 */
public class Constant {
    public static final Integer PHONE_COED_LENGTH=4;
    public static final String PHONE_CODE_PREFIX="manage_login_";
    public static final String USER_ROLE_NAME = "系统管理员";
    /**加密前缀**/
    public static final String ENCODE_PASSWORD_PREFIX = "e+iot";
    public static final String INITIAL_PASSWORD = "123456";
    
    public static final Byte LEVEL_ONE = 1;
    public static final Byte LEVEL_TWO = 2;
    public static final Byte LEVEL_THREE = 3;

    public static final String DICT = "Dict";

    /**反斜杠**/
    public static final String BACKSLASH = "/";
    /**分号**/
    public static final String SEMICOLON = ";";

    /**
     * 用户导出表头字段
     */
    public static final String EXCEL_USER_NO = "用户编号";
    public static final String EXCEL_USER_NAME = "姓名";
    public static final String EXCEL_USER_SEX = "用户性别";
    public static final String EXCEL_USER_POSITION = "职位";
    public static final String EXCEL_USER_DEPT_PAIR = "可见部门/密级";
    public static final String EXCEL_USER_ROLENAME = "所属角色";
    public static final String EXCEL_USER_COMPANY = "所属公司";
    public static final String EXCEL_USER_PHONE = "手机号码";
    public static final String EXCEL_USER_ACCOUNT = "账号";
    public static final String EXCEL_USER_VIEW_ENCRY_PERMITTED = "是否可见员工密级";
    public static final String EXCEL_USER_ENCRY_LEVEL_COMPANY = "资源库密保等级";
    public static final String EXCEL_USER_STATUS = "账号状态";
    public static final String EXCEL_USER_LAST_TIME = "末次登陆时间";
    public static final String EXCEL_USER_CREATED = "创建时间";

    public static final String EXCEL_USER_DEPT = "所属部门";
    public static final String EXCEL_USER_TEAM = "所属team";
    public static final String EXCEL_USER_ENCRY_LEVEL_DEPT = "密保等级";
    public static final String EXCEL_ID = "id";
    /**
     * 导出文件数据末次登录时间字段为空，填充数据
     */
    public static final String NO_DATA = "无";

    /**
     * 用户列表导出文件名称
     */
    public static final String USER_EXPORT_EXCEL_TITLE = "用户列表";
    public static final String HOOLINK_USER_EXPORT_EXCEL_TITLE = "互灵公司员工表格.xls";

    /**
     * 隐藏表格，用于存储属性
     */
    public static final String EXCEL_HIDE_SHEET = "hideSheet";

    /**
     * 操作sheet
     */
    public static final String EXCEL_SHEET1 = "sheet1";

    /**
     * 感叹号
     */
    public static final String EXCLAMATION_MARK = "!";
    /**
     * 下划线
     */
    public static final String UNDERLINE = "_";
    /**
     * 逗号
     */
    public static final String COMMA = ",";
    /**
     * 横杠
     */
    public static final String RUNG = "-";

    /**
     * 错误信息
     */
    public static final String ERROR = "error";

    /**
     * excel公式
     */
    public static final String EXCEL_COMPANY_LIST = "companyList";
    public static final String EXCEL_ROLE_LIST = "roleList";
    public static final String EXCEL_ENCRY_LEVEL_LIST = "encryLevelList";
    public static final String EXCEL_VIEW_ENCRY_PERMITTED_LIST = "viewEncryLevelPermittedList";
    public static final String EXCEL_SEX_LIST = "sexList";
    public static final String EXCEL_DEPT_FORMULA = "INDIRECT($D1)";
    public static final String EXCEL_TEAM_FORMULA = "INDIRECT($E1)";


    /**
     * 菜单
     */
    public static final String EDM = "EDM";
    public static final String HOOLINK = "HOOLINK_MANAGE";
    public static final String MANAGE_CENTER = "MANAGEMENT_CENTER";
    public static final String MANAGE_CENTER_NAME = "管理中心";
    public static final String ROLE_MANAGE_NAME = "角色管理";
    public static final String DEPT_REPERTORY = "DEPT_REPERTORY";
    public static final String CACHE_REPERTORY = "CACHE_REPERTORY";
    public static final String COMPANY_REPERTORY = "COMPANY_REPERTORY";
    public static final Integer REPERTORY_ONE = 1;
    public static final String REPERTORY_ONE_NAME = "部门资源";
    public static final Integer REPERTORY_TWO = 2;
    public static final String REPERTORY_TWO_NAME = "缓存库";
    public static final Integer REPERTORY_THREE = 3;
    public static final String REPERTORY_THREE_NAME = "资源库";

    /**
     * 登录
     */
    public static final String GREETING_MORNING="凌晨好";
    public static final String GREETING_FORENOON="上午好";
    public static final String GREETING_NOON="中午好";
    public static final String GREETING_AFTERNOON="下午好";
    public static final String GREETING_NIGHT="晚上好";
    public static final String GREETING_YOU="你好";

    public static final String CESHI_CODE="hoolink2019";




    /**
     * 组织架构层级 1-公司 2-部门 3-小组
     */
    public static final Byte COMPANY_LEVEL = 1;
    public static final Byte DEPT_LEVEL = 2;
    public static final Byte POSITION_LEVEL = 3;

    public static final Integer USER = 0;
    public static final Integer DEPARTMENT = 1;
}
