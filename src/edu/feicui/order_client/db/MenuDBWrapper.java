package edu.feicui.order_client.db;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Sogrey
 * 
 */
public class MenuDBWrapper {
	private SQLiteDatabase mDb;// SQL数据库对象
	/** 单例模式 对象 */
	private static MenuDBWrapper sInstance;

	/**
	 * 单例模式 <br>
	 * 一个类最多只能有一个实例 <br>
	 * 1、有一个私有静态成员 <br>
	 * 2、有一个公开静态方法getInstance得到这个私有静态成员 <br>
	 * 3、有一个私有的构造方法（不允许被实例化） <br>
	 */

	public static MenuDBWrapper getInstance(Context context) {
		if (sInstance == null) {
			synchronized (MenuDBWrapper.class) {
				if (sInstance == null) {
					sInstance = new MenuDBWrapper(context);
				}
			}
		}
		return sInstance;
	}

	private MenuDBWrapper(Context context) {
		DBHelper helpper = new DBHelper(context, DBHelper.DB_NAME, null,
				DBHelper.DB_VERSION);
		mDb = helpper.getWritableDatabase();
	}

	/** 插入数据 */
	public void insertRank(int id,int category, String name, int price, int units,
			String pic, String remark) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.COLUMN_MENU_MENUID, id);
		values.put(DBHelper.COLUMN_MENU_CATEGORY, category);
		values.put(DBHelper.COLUMN_MENU_NAME, name);
		values.put(DBHelper.COLUMN_MENU_PRICE, price);
		values.put(DBHelper.COLUMN_MENU_UNITS, units);
		values.put(DBHelper.COLUMN_MENU_PIC, pic);
		values.put(DBHelper.COLUMN_MENU_REMARK, remark);
		// mDb.insert(table, nullColumnHack, values)/*原型*/
		mDb.insert(DBHelper.TABLE_MENU, "", values);
	}

	/** 更新数据 */
	public void updateRank(/* 条件 */String name, int price, int units,
			String pic, int version, String remark) {
	}
	/** 清空数据 */
	public void deleteRank(){
		mDb.delete(DBHelper.TABLE_MENU, null, null);
	}

	/** 查询所有数据,返回游标Cursor */
	public Cursor rawQueryRank() {
		/* SQL语句查询 */
		String sql = "select * from " + DBHelper.TABLE_MENU + " order by "
				+ DBHelper.COLUMN_MENU_CATEGORY + " ASC";
		return mDb.rawQuery(sql, null);
	}

	/** 按CATEGORY查询数据,返回游标Cursor */
	public Cursor rawQueryRank(int category) {
		/* SQL语句查询 */
		String sql = "select * from " + DBHelper.TABLE_MENU + " where "
				+ DBHelper.COLUMN_MENU_CATEGORY + " =" + category  ;
		return mDb.rawQuery(sql, null);
	}
	/** 按NAME查询数据,返回游标Cursor */
	public Cursor rawQueryRank(String name) {
		/* SQL语句查询 */
		String sql = "select * from " + DBHelper.TABLE_MENU + " where "
				+ DBHelper.COLUMN_MENU_NAME + " =\"" + name+"\""  ;
		return mDb.rawQuery(sql, null);
	}
	/** 按MENUID查询NAME数据,返回游标Cursor */
	public Cursor queryRank(int menuId) {
		/* SQL语句查询 */
		String sql = "select * from " + DBHelper.TABLE_MENU + " where "
				+ DBHelper.COLUMN_MENU_MENUID + " =\"" + menuId+"\""  ;
		return mDb.rawQuery(sql, null);
	}
}
