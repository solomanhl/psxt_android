/**
 * 数据库
 * 
 * @author 贺亮
 * 
 */
package com.soloman.DB;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Database {

	private int dbversion = 1;
	private String db_name = "psxt.db";
	private String table_people = "canpinrenyuan";// 参评人员
	private String table_Failedpeople = "failed";// 不通过的   主任寄语
	private String table_pwh = "pwh";// 保存评委会id
	private Context mCtx = null;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase SQLdb;

	public class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
			System.out
					.println("DB databasehelper(context,name,factory,version)");
		}

		public DatabaseHelper(Context context) {
			super(context, db_name, null, dbversion);
			// TODO Auto-generated constructor stub
			System.out.println("DB databasehelper(context)");
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			System.out.println("DB onCreate");
			// 只在第一次创建的时候进入
			db.execSQL("create table IF NOT EXISTS " + table_people + 
					"(id varchar(10) ," + //id
					"pinfen varchar(3), " + // 评分 0-100
					"pogeyijian varchar(100), " + // 破格意见
					"poge varchar(6), " + // 破格 同意 不同意
					"toupiao varchar(6), " +// 投票同意  反对 弃权 未投票
					"tijiaostate varchar(1), " +//提交状态（保存/提交评分/保存投票/提交投票）0 1 2 3
					"f1 varchar(3), " +	//分数1
					"f2 varchar(3), " +	//分数2
					"f3 varchar(3) )"); 	//分数3	
			SQLdb = db;
			System.out.println("DB onCreate --- table_people");
			
			db.execSQL("create table IF NOT EXISTS " + table_Failedpeople + 
					"(id varchar(10) ," + //id
					"pinyu varchar(200), " + // 评语
					"zhuangtai varchar(1) )");//提交状态（保存/提交0 1
			SQLdb = db;
			System.out.println("DB onCreate --- table_Failedpeople");
			
			db.execSQL("create table IF NOT EXISTS " + table_pwh + 
					"(pwhid varchar(10) )");//pwhid  评委会id
			SQLdb = db;
			System.out.println("DB onCreate --- table_pwh");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

		}
	}

	/** 构造函数 */
	public Database(Context ctx) {
		this.mCtx = ctx;
		System.out.println("DB构造函数");
	}

	public Database open() throws SQLException {
		System.out.println("DB Open");
		dbHelper = new DatabaseHelper(mCtx);
		// 只有调用getReadableDatabase或者getWriteableDatabase方法，才会创建数据库对象
		SQLdb = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	public long add(String id, String pinfen, String pogeyijian, String poge, String toupiao, String tijiaostate,
			String f1,String f2,String f3) {
		///id 评分 破格 投票 提交状态（未提交/提交评分/提交投票）
		ContentValues cv = new ContentValues();

		cv.put("id", id);
		cv.put("pinfen", pinfen);
		cv.put("pogeyijian", pogeyijian);
		cv.put("poge", poge);
		cv.put("toupiao", toupiao);
		cv.put("tijiaostate", tijiaostate);
		cv.put("f1", f1);
		cv.put("f2", f2);
		cv.put("f3", f3);

		System.out.println("DB.add " + table_people);
		return SQLdb.insert(table_people, null, cv);
	}

	public long addpinweihui(String id) {
		///id 评分 破格 投票 提交状态（未提交/提交评分/提交投票）
		ContentValues cv = new ContentValues();

		cv.put("pwhid", id);

		System.out.println("DB.add " + table_pwh);
		return SQLdb.insert(table_pwh, null, cv);
	}
	
	public Cursor getToupiao(){
		Cursor cursor = null;
		try {
			String sql ="select toupiao from canpinrenyuan where toupiao!=?";   
			cursor = SQLdb.rawQuery(sql, new String[] { "" });
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}
		return cursor;
	}
	
	public Cursor getpwh(){
		Cursor cursor = null;
		try {
			cursor = SQLdb.query(table_pwh, // table名
					new String[] { "pwhid"}, // 字段
					null, // 条件
					null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}
		return cursor;
	}
	
	// 修改信息
	public int Update_people(String id, String pinfen, String pogeyijian,
			String poge, String toupiao, String tijiaostate, String f1,
			String f2, String f3) {
		ContentValues cv = new ContentValues();
		cv.put("pinfen", pinfen);
		cv.put("pogeyijian", pogeyijian);
		cv.put("poge", poge);
		cv.put("toupiao", toupiao);
		cv.put("tijiaostate", tijiaostate);
		cv.put("f1", f1);
		cv.put("f2", f2);
		cv.put("f3", f3);
		String[] args = { String.valueOf(id) };
		return SQLdb.update(table_people, cv, "id=?", args);
	}
	
	// 修改信息
	public int Update_tijiao(String id, String tijiaostate) {
		//提交状态（保存/提交评分/保存投票/提交投票）0 1 2 3
		ContentValues cv = new ContentValues();
		cv.put("tijiaostate", tijiaostate);
		String[] args = { String.valueOf(id) };
		return SQLdb.update(table_people, cv, "id=?", args);
	}
	
	public int Update_pwh(String id, String newid) {
		//提交状态（保存/提交评分/保存投票/提交投票）0 1 2 3
		ContentValues cv = new ContentValues();
		cv.put("pwhid", newid);//更新为newid
		String[] args = { String.valueOf(id) };
		return SQLdb.update(table_pwh, cv, "pwhid=?", args);
	}
	
	
	// 修改信息
	public int Update_toupiao(String id, String toupiao, String tijiaostate) {
		// 投票0 1 2 同意 赞成 反对
		//提交状态（未提交/提交评分/提交投票）0 1 2 3
		ContentValues cv = new ContentValues();
		cv.put("toupiao", toupiao);
		cv.put("tijiaostate", tijiaostate);
		String[] args = { String.valueOf(id) };
		return SQLdb.update(table_people, cv, "id=?", args);
	}

	
	public long del(int id) {
		return SQLdb.delete(table_people, "id = " + id, null);
	}

	public Cursor queryTable(String id) {
		Cursor cursor = null;
		try {
			cursor = SQLdb.query(table_people, // table名
					new String[] { "id", "pinfen", "pogeyijian", "poge", "toupiao","tijiaostate" ,"f1","f2","f3"}, // 字段
					"id = '" + id + "'", // 条件
					null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}
		return cursor;
	}
	
	public Cursor queryTable_tijiaostate(String tijiaostate) {
		Cursor cursor = null;
		try {
			cursor = SQLdb.query(table_people, // table名
					new String[] { "id", "pinfen", "pogeyijian", "poge", "toupiao","tijiaostate"  ,"f1","f2","f3"}, // 字段
					"tijiaostate = '" + tijiaostate + "'", // 条件
					null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}
		return cursor;
	}
	
	public Cursor queryTable_tijiaostate(String id, String tijiaostate) {
		Cursor cursor = null;
		try {
			cursor = SQLdb.query(table_people, // table名
					new String[] { "id", "pinfen", "pogeyijian", "poge", "toupiao","tijiaostate" ,"f1","f2","f3" }, // 字段
					"id = '" + id  + "' and tijiaostate = '" + tijiaostate + "'", // 条件
					null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}
		return cursor;
	}
	
	public Cursor getAll() {
		Cursor cursor = null;
		try {
			cursor = SQLdb.query(table_people, // table名
					new String[] { "id", "pinfen", "pogeyijian", "poge", "toupiao","tijiaostate"  ,"f1","f2","f3"}, // 字段
					null, // 条件
					null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}
		return cursor;
	}

	/*
	 * public Cursor getAll(){ return SQLdb.rawQuery("select * from " +
	 * table_list, null); }
	 * 
	 * public Cursor getThis(String tableid){ return SQLdb.query(table_list, new
	 * String [] {"tableid","foodname","num"}, "tableid = " + tableid, null,
	 * null, null, null); }
	 */
	public void clearThis(String tableid) {
		SQLdb.delete(tableid, null, null);
	}
	
	
	
	
	public long Add_pinyu(String id, String pinyu, String zhuangtai) {
		///id 评分 破格 投票 提交状态（未提交/提交评分/提交投票）
		ContentValues cv = new ContentValues();

		cv.put("id", id);
		cv.put("pinyu", pinyu);
		cv.put("zhuangtai", zhuangtai);

		System.out.println("DB.Add_pinyu " + table_Failedpeople);
		return SQLdb.insert(table_Failedpeople, null, cv);
	}
	
	public Cursor queryFailed(String id) {
		Cursor cursor = null;
		try {
			cursor = SQLdb.query(table_Failedpeople, // table名
					new String[] { "id", "pinyu", "zhuangtai"}, // 字段
					"id = '" + id + "'", // 条件
					null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}
		return cursor;
	}
	
	public Cursor queryFailed_zhuangtai(String zhuangtai) {
		Cursor cursor = null;
		try {
			cursor = SQLdb.query(table_Failedpeople, // table名
					new String[] { "id", "pinyu", "zhuangtai"}, // 字段
					"zhuangtai = '" + zhuangtai + "'", // 条件
					null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}
		return cursor;
	}
	
	public int Update_pinyu(String id, String pinyu) {
		ContentValues cv = new ContentValues();
		cv.put("pinyu", pinyu);
		
		String[] args = { String.valueOf(id) };
		return SQLdb.update(table_Failedpeople, cv, "id=?", args);
	}
	
	public int Update_failedState(String id, String zhuangtai) {
		ContentValues cv = new ContentValues();
		cv.put("zhuangtai", zhuangtai);
		
		String[] args = { String.valueOf(id) };
		return SQLdb.update(table_Failedpeople, cv, "id=?", args);
	}
	
	public Cursor getFailedAll() {
		Cursor cursor = null;
		try {
			cursor = SQLdb.query(table_Failedpeople, // table名
					new String[] { "id", "pinyu", "zhuangtai"}, // 字段
					null, // 条件
					null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}
		return cursor;
	}
}
