package com.cells.companyapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

	private static final String TAG = "SQLiteHelper";

	/** 数据库名 */
	public final static String DB_NAME = "company_app.db";
	/** 数据库版本号 */
	private final static int VERSION = 1;
	/** 数据库表 */
	public final static String T_COLLECTION = "t_collection";

	public static final String _ID = "id";// ID
	public static final String COLLECTION_ID = "collection_id";
	public static final String COMMENT_NUM = "comment_count";
	public static final String LIKE_NUM = "like_count";
	public static final String IMAGE = "image";
	public static final String TYPE = "type";
	public static final String NAME = "name";
	public static final String CONTENT = "content";
	public static final String CREATED_AT = "created_at";

	/** 创建数据库表SQL语句 */
	String sql_collection = "create table if not exists " + T_COLLECTION + "("
			+ _ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , "
			+ COLLECTION_ID + " INTEGER NOT NULL , " + COMMENT_NUM
			+ " INTEGER , " + LIKE_NUM + " INTEGER , " + TYPE
			+ " INTEGER  NOT NULL , " + IMAGE + " varchar(100) , " + NAME
			+ " varchar(100) , " + CONTENT + " TEXT , " + CREATED_AT
			+ " varchar(20))";

	/**
	 * 在SQLiteOpenHelper的子类中，必须有该构造方法
	 * 
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public SQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DB_NAME, factory, version);
	}

	public SQLiteHelper(Context context) {
		this(context, DB_NAME, null, VERSION);
	}

	/**
	 * 数据库的构造方法，用来定义数据库的名称，数据库的查询的结果集，数据库版本。
	 * 
	 * @param context
	 * @param name
	 * @param version
	 * 
	 */
	public SQLiteHelper(Context context, String name, int version) {
		// factory ，游标工厂，设置位null，version数据库版本。
		this(context, DB_NAME, null, version);
	}

	// 该方法是在第一次创建数据库时调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "Create a Database: " + db.getPath());
		// 创建表
		db.execSQL(sql_collection);
	}

	// 该方法在升级数据库时调用
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "Upgrade a Database from version " + oldVersion
				+ " to version " + newVersion);
	}
}
