package edu.feicui.order_client.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Sogrey
 *
 */
public class TableDBWrapper {
	private SQLiteDatabase mDb;// SQL数据库对象
	/**单例模式 对象*/
	private static TableDBWrapper sInstance;

	/**
	 * 单例模式 <br>
	 * 一个类最多只能有一个实例 <br>
	 * 1、有一个私有静态成员 <br>
	 * 2、有一个公开静态方法getInstance得到这个私有静态成员 <br>
	 * 3、有一个私有的构造方法（不允许被实例化） <br>
	 */

	public static TableDBWrapper getInstance(Context context) {
		if (sInstance == null) {
			synchronized (TableDBWrapper.class) {
				if (sInstance == null) {
					sInstance = new TableDBWrapper(context);
				}
			}
		}
		return sInstance;
	}

	private TableDBWrapper(Context context) {
		DBHelper helpper = new DBHelper(context, DBHelper.DB_NAME, null, DBHelper.DB_VERSION);
		mDb = helpper.getWritableDatabase();
	}
	
	/** 插入数据 */
	public void insertRank(int tableid,int num, String name, int number, 
			String description) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.COLUMN_TABLE_TABLEID, tableid);
		values.put(DBHelper.COLUMN_TABLE_NUM, num);
		values.put(DBHelper.COLUMN_TABLE_NAME, name);
		values.put(DBHelper.COLUMN_TABLE_NUMBER, number);
		values.put(DBHelper.COLUMN_TABLE_DESCRIPTION, description);
		// mDb.insert(table, nullColumnHack, values)/*原型*/
		mDb.insert(DBHelper.TABLE_TABLEID, "", values);
	}
	
	/** 更新数据 */
	public void updateRank(/* 条件 */int tableid,int num, String name, int number, 
			String description) {
		
	}
	/** 清空数据 */
	public void deleteRank(){
		mDb.delete(DBHelper.TABLE_TABLEID, null, null);
	}
	/** 按mode,level查询数据,返回游标Cursor */
	public Cursor rawQueryRank() {
		/* SQL语句查询 */
		String sql = "select * from " + DBHelper.TABLE_TABLEID 
				+ " order by " + DBHelper.COLUMN_TABLE_NUM + " ASC";
		return mDb.rawQuery(sql, null);
	}
}
