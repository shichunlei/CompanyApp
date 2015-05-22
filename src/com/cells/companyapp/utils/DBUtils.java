package com.cells.companyapp.utils;

import java.util.ArrayList;
import java.util.List;

import com.cells.companyapp.been.Collection;
import com.cells.companyapp.db.SQLiteHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * DB 操作辅助类
 * 
 * @author 师春雷
 * 
 */
public class DBUtils {

	private static final String TAG = "DBUtils";

	private SQLiteHelper helper = null;
	private SQLiteDatabase database = null;
	private Cursor cursor = null;

	private static final String[] COLLECTIONS = new String[] {
			SQLiteHelper._ID, SQLiteHelper.COLLECTION_ID,
			SQLiteHelper.COMMENT_NUM, SQLiteHelper.LIKE_NUM, SQLiteHelper.TYPE,
			SQLiteHelper.IMAGE, SQLiteHelper.NAME, SQLiteHelper.CONTENT,
			SQLiteHelper.CREATED_AT };

	public DBUtils(Context context) {
		super();
		helper = new SQLiteHelper(context);
	}

	/**
	 * 添加收藏
	 * 
	 * @param collection
	 * @return
	 */
	public boolean insert(Collection collection) {
		boolean flag = false;
		long id = -1;

		try {
			database = helper.getReadableDatabase();
			ContentValues values = new ContentValues();

			values.put(SQLiteHelper.COLLECTION_ID,
					collection.getCollection_id());
			values.put(SQLiteHelper.COMMENT_NUM, collection.getComment_count());
			values.put(SQLiteHelper.LIKE_NUM, collection.getLike_count());
			values.put(SQLiteHelper.TYPE, collection.getType());
			values.put(SQLiteHelper.IMAGE, collection.getImage());
			values.put(SQLiteHelper.NAME, collection.getName());
			values.put(SQLiteHelper.CONTENT, collection.getContent());
			values.put(SQLiteHelper.CREATED_AT, collection.getCreated_at());

			id = database.insert(SQLiteHelper.T_COLLECTION, null, values);

			flag = (id != -1 ? true : false);

		} catch (Exception e) {
			Log.v(TAG, "insert.SQLException");
			e.printStackTrace();
		} finally {
			closeDatabase();
		}

		return flag;
	}

	/**
	 * 根据博客ID查询对应信息（收藏博客详情）
	 * 
	 * @param id
	 * @return
	 */
	public Collection queryById(int id) {
		Collection collection = new Collection();
		try {
			database = helper.getReadableDatabase();
			cursor = database.query(true, SQLiteHelper.T_COLLECTION,
					COLLECTIONS, SQLiteHelper._ID + "='" + id + "'", null,
					null, null, null, null);
			while (cursor.moveToNext()) {
				collection.setId(cursor.getInt(cursor
						.getColumnIndexOrThrow(SQLiteHelper._ID)));
				collection.setCollection_id(cursor.getInt(cursor
						.getColumnIndexOrThrow(SQLiteHelper.COLLECTION_ID)));
				collection.setComment_count(cursor.getInt(cursor
						.getColumnIndexOrThrow(SQLiteHelper.COMMENT_NUM)));
				collection.setLike_count(cursor.getInt(cursor
						.getColumnIndexOrThrow(SQLiteHelper.LIKE_NUM)));
				collection.setType(cursor.getInt(cursor
						.getColumnIndexOrThrow(SQLiteHelper.TYPE)));
				collection.setImage(cursor.getString(cursor
						.getColumnIndexOrThrow(SQLiteHelper.IMAGE)));
				collection.setName(cursor.getString(cursor
						.getColumnIndexOrThrow(SQLiteHelper.NAME)));
				collection.setContent(cursor.getString(cursor
						.getColumnIndexOrThrow(SQLiteHelper.CONTENT)));
				collection.setCreated_at(cursor.getString(cursor
						.getColumnIndexOrThrow(SQLiteHelper.CREATED_AT)));
			}
		} catch (SQLException e) {
			Log.e("", "queryById.SQLException");
			e.printStackTrace();
		} finally {
			closeDatabase();
		}
		Log.d(TAG, "queryById:" + collection.toString());

		return collection;
	}

	/**
	 * 查询所有数据（收藏列表）
	 * 
	 * @return（List数据）
	 */
	public List<Collection> queryAll() {
		List<Collection> list = new ArrayList<Collection>();
		try {
			database = helper.getReadableDatabase();
			cursor = database.query(SQLiteHelper.T_COLLECTION, COLLECTIONS,
					null, null, null, null, null);
			while (cursor.moveToNext()) {
				Collection collection = new Collection();
				collection.setId(cursor.getInt(cursor
						.getColumnIndexOrThrow(SQLiteHelper._ID)));
				collection.setCollection_id(cursor.getInt(cursor
						.getColumnIndexOrThrow(SQLiteHelper.COLLECTION_ID)));
				collection.setComment_count(cursor.getInt(cursor
						.getColumnIndexOrThrow(SQLiteHelper.COMMENT_NUM)));
				collection.setLike_count(cursor.getInt(cursor
						.getColumnIndexOrThrow(SQLiteHelper.LIKE_NUM)));
				collection.setType(cursor.getInt(cursor
						.getColumnIndexOrThrow(SQLiteHelper.TYPE)));
				collection.setImage(cursor.getString(cursor
						.getColumnIndexOrThrow(SQLiteHelper.IMAGE)));
				collection.setName(cursor.getString(cursor
						.getColumnIndexOrThrow(SQLiteHelper.NAME)));
				collection.setContent(cursor.getString(cursor
						.getColumnIndexOrThrow(SQLiteHelper.CONTENT)));
				collection.setCreated_at(cursor.getString(cursor
						.getColumnIndexOrThrow(SQLiteHelper.CREATED_AT)));
				list.add(collection);
			}
		} catch (SQLException e) {
			Log.e("", "queryAll.SQLException");
			e.printStackTrace();
		} finally {
			closeDatabase();
		}
		Log.d(TAG, "queryAll:" + list.toString());

		return list;
	}

	/**
	 * 删除表内信息
	 * 
	 */
	public boolean deleteAll() {
		boolean flag = false;
		int count = 0;
		try {
			database = helper.getWritableDatabase();
			count = database.delete(SQLiteHelper.T_COLLECTION, null, null);

			flag = (count > 0 ? true : false);
		} catch (Exception e) {
			Log.e("", "deleteAll.SQLException");
			Log.e(TAG, e.getMessage());
		} finally {
			closeDatabase();
		}
		return flag;
	}

	/**
	 * 根据 “id” 删除对应信息
	 * 
	 * @param id
	 */
	public boolean deleteById(int id) {
		boolean flag = false;
		int count = 0;
		try {
			database = helper.getWritableDatabase();
			count = database.delete(SQLiteHelper.T_COLLECTION, SQLiteHelper._ID
					+ "='" + id + "'", null);
			flag = (count > 0 ? true : false);
		} catch (Exception e) {
			Log.e("", "deleteById.SQLException");
			Log.e(TAG, e.getMessage());
		} finally {
			closeDatabase();
		}
		return flag;
	}

	// 关闭数据库
	private void closeDatabase() {
		if (database != null) {
			database.close();
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
	}
}
