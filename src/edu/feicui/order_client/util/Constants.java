package edu.feicui.order_client.util;

import android.os.Environment;

/**
 * 常量列表<br>
 * 全局調用，為統一管理<br>
 * 
 * @author Sogrey
 * */
public final class Constants {
	/** menu卡路径 */
	public final static String PATH_menuCARD = Environment
			.getExternalStorageDirectory().getAbsolutePath();
	/**
	 * Log文件保存路径<br>
	 * "/menucard/order_client/order_log.txt"
	 */
	public final static String PATH_LOG = PATH_menuCARD
			+ "/order_client/order_log.txt";
	/**
	 * 缓存图片文件夹保存路径<br>
	 * "/menucard/edu.feicui.order_client/imgs"
	 */
	public static final String PATH_CACHE = PATH_menuCARD
			+ "/edu.feicui.order_client/imgs";
	/**用户名*/
	public final static String USER_NAME = "name";
	/**密码*/
	public final static String PASSWORD = "password";
	/**
	 * 初始密码
	 */
	public final static String INCEPTIVE_PASSWORD = "888888";
	/** 服务器地址 */
	public static final String PATH_SERVER = "http://192.168.2.200:8080/GourmetOrderServer";

	/** 登陆地址 */
	public static final String PATH_LOGIN = PATH_SERVER + "/loginServlet";
	/** 更新菜单桌号，获取菜单列表、桌号列表 */
	public static final String PATH_UPDATE = PATH_SERVER + "/updateServlet";
	/** 查询所有订单流水 */
	public static final String PATH_QUERY_ALL_OREDER = PATH_SERVER + "/queryAllOreder";
	/** 查询指定订单 */
	public static final String PATH_QUERY_OREDER = PATH_SERVER + "/queryOreder";
	/** 添加订单 */
	public static final String PATH_OREDER = PATH_SERVER + "/orderServlet";
	/** 加单 */
	public static final String PATH_ADD_ORDER = PATH_SERVER + "/addOrderServlet";
	/** 更改桌号-换台/并台 */
	public static final String PATH_CHANGE_TABLE = PATH_SERVER + "/changetableServlet";
	/** 结单 */
	public static final String PATH_PAY_MONEY = PATH_SERVER + "/payMoneyServlet";
	
	/** 菜品图片地址 */
	public static final String PATH_IMAGES = PATH_SERVER + "/images";
	/** 退出应用对话框ID */
	public static final int DIALOG_EXIT = 0x400;

	/** 流操作单位长度 */
	public final static int SIZE_BUFFER = 1024;

	/** 桌号更换参数名 */
	public static final String CHANGE_TABLE_NAME = "ChangeTable";
	/** 桌号 */
	public static final String CHOOSE_TABLE_ID = "TableId";
	
	/** 要查询的订单号 */
	public static final String NUMBER = "number";
	/** 查单 */
	public static final String CHECK_ORDER = "check_order";
	/** 查单=>结算 */
	public static final int CHECKOUT = 0x10;
	/** 查单=>订单进度 */
	public static final int ORDER_PROGRESS  = 0x11;
	/** 查单=>加单*/
	public static final int ADD_ORDER = 0x12;
	/** 查单=>查单看详情 */
	public static final int CHECK_ORDER_INFO = 0x13;
	
	/** 点餐 */
	public static final String CHOOSEDISH = "ChooseDish";
	/** 点餐=>点餐 */
	public static final int CHOOSE_ORDER = 0x20;
	/** 点餐=>加单 */
	public static final int CHOOSE_ADD_ORDER  = 0x21;

	
	/** 参数-换台 */
	public static final int CHANGE_TABLE = 0x01;
	/** 参数-并台 */
	public static final int MERGE_TABLE = 0x02;
	
	/** 热菜 */
	public static final int MENU_HOT_FOOD = 1201;
	/** 凉菜 */
	public static final int MENU_COOL_FOOD = 1202;
	/** 汤羹 */
	public static final int MENU_SOUP_FOOD = 1203;
	/** 饮料 */
	public static final int MENU_DRINK_FOOD = 1204;
	/** 主食 */
	public static final int MENU_STAPLE_FOOD = 1205;
	/** 餐具 */
	public static final int MENU_TABLEWARE_FOOD = 1206;
	
	/** 流水号-按点餐时间格式 */
	public final static String FMT_SERIAL_NUMBER = "yyyyMMddkkmm";
	/** 日期格式 */
	public final static String FMT_DATE = "yyyy-MM-dd";
	
	/** 更新数据对话框 */
	public static final int DIALOG_ID_UPDATA = 2000;
	
}
