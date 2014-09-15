/**
 * 
 */
package edu.feicui.order_client.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Sogrey
 * 
 */
public class DBHelper extends SQLiteOpenHelper {
	public final static String DB_NAME = "order.db";// 数据库名
	public final static int DB_VERSION = 1;// 数据库版本
	public final static String TABLE_MENU = "menu";// 菜单表名
	public final static String COLUMN_MENU_ID = "_id";// 菜单主键ID
	public final static String COLUMN_MENU_MENUID = "menuid";// 菜品代号
	public final static String COLUMN_MENU_CATEGORY = "category";// 菜单列名-类别
	public final static String COLUMN_MENU_NAME = "name";// 菜单列名-名称
	public final static String COLUMN_MENU_PRICE = "price";// 菜单列名-单价
	public final static String COLUMN_MENU_UNITS = "units";// 菜单列名-单位
	public final static String COLUMN_MENU_PIC = "pic";// 菜单列名-图片路径
	public final static String COLUMN_MENU_REMARK = "remark";// 菜单列名-简介
	public final static String TABLE_TABLEID = "tableid";// 桌号表名
	public final static String COLUMN_TABLE_ID = "_id";// 桌号主键ID
	public final static String COLUMN_TABLE_TABLEID = "tableid";// 桌号列名-房间代号
	public final static String COLUMN_TABLE_NUM = "num";// 桌号列名-房间号
	public final static String COLUMN_TABLE_NAME = "name";// 桌号列名-名称
	public final static String COLUMN_TABLE_NUMBER = "number";// 桌号列名-人数
	public final static String COLUMN_TABLE_DESCRIPTION = "description";// 桌号列名-描述

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DB_NAME, factory, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sqlMenu = "CREATE TABLE " + TABLE_MENU + " ( " // 菜单表
				+ COLUMN_MENU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " // 主键ID
				+ COLUMN_MENU_MENUID + " INTEGER, " // 类别
				+ COLUMN_MENU_CATEGORY + " INTEGER, " // 类别
				+ COLUMN_MENU_NAME + " TEXT, " // 名称
				+ COLUMN_MENU_PRICE + " INTEGER, " // 单价
				+ COLUMN_MENU_UNITS + " INTEGER, "// 单位
				+ COLUMN_MENU_PIC + " TEXT," // 图片路径
				+ COLUMN_MENU_REMARK + " TEXT)";// 简介
		db.execSQL(sqlMenu);// 执行SQL语句，创建表（返回值为空）
		String sqlTable = "CREATE TABLE " + TABLE_TABLEID + " ( "//桌号表
				+ COLUMN_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "// 主键
				+ COLUMN_TABLE_TABLEID + " INTEGER, " // 房间代号
				+ COLUMN_TABLE_NUM + " INTEGER, " // 房间号
				+ COLUMN_MENU_NAME + " TEXT, "// 名称
				+ COLUMN_TABLE_NUMBER + " INTEGER, "// 人数
				+ COLUMN_TABLE_DESCRIPTION + " TEXT)";// 版本
		db.execSQL(sqlTable);// 执行SQL语句，创建表（返回值为空）
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
