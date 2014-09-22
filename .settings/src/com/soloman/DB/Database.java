/**
 * ���ݿ�
 * 
 * @author ����
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
	private String table_people = "canpinrenyuan";// ������Ա
	private String table_Failedpeople = "failed";// ��ͨ����   ���μ���
	private String table_pwh = "pwh";// ������ί��id
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
			// ֻ�ڵ�һ�δ�����ʱ�����
			db.execSQL("create table IF NOT EXISTS " + table_people + 
					"(id varchar(10) ," + //id
					"pinfen varchar(3), " + // ���� 0-100
					"pogeyijian varchar(100), " + // �Ƹ����
					"poge varchar(6), " + // �Ƹ� ͬ�� ��ͬ��
					"toupiao varchar(6), " +// ͶƱͬ��  ���� ��Ȩ δͶƱ
					"tijiaostate varchar(1), " +//�ύ״̬������/�ύ����/����ͶƱ/�ύͶƱ��0 1 2 3
					"f1 varchar(3), " +	//����1
					"f2 varchar(3), " +	//����2
					"f3 varchar(3) )"); 	//����3	
			SQLdb = db;
			System.out.println("DB onCreate --- table_people");
			
			db.execSQL("create table IF NOT EXISTS " + table_Failedpeople + 
					"(id varchar(10) ," + //id
					"pinyu varchar(200), " + // ����
					"zhuangtai varchar(1) )");//�ύ״̬������/�ύ0 1
			SQLdb = db;
			System.out.println("DB onCreate --- table_Failedpeople");
			
			db.execSQL("create table IF NOT EXISTS " + table_pwh + 
					"(pwhid varchar(10) )");//pwhid  ��ί��id
			SQLdb = db;
			System.out.println("DB onCreate --- table_pwh");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

		}
	}

	/** ���캯�� */
	public Database(Context ctx) {
		this.mCtx = ctx;
		System.out.println("DB���캯��");
	}

	public Database open() throws SQLException {
		System.out.println("DB Open");
		dbHelper = new DatabaseHelper(mCtx);
		// ֻ�е���getReadableDatabase����getWriteableDatabase�������Żᴴ�����ݿ����
		SQLdb = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	public long add(String id, String pinfen, String pogeyijian, String poge, String toupiao, String tijiaostate,
			String f1,String f2,String f3) {
		///id ���� �Ƹ� ͶƱ �ύ״̬��δ�ύ/�ύ����/�ύͶƱ��
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
		///id ���� �Ƹ� ͶƱ �ύ״̬��δ�ύ/�ύ����/�ύͶƱ��
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
			cursor = SQLdb.query(table_pwh, // table��
					new String[] { "pwhid"}, // �ֶ�
					null, // ����
					null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}
		return cursor;
	}
	
	// �޸���Ϣ
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
	
	// �޸���Ϣ
	public int Update_tijiao(String id, String tijiaostate) {
		//�ύ״̬������/�ύ����/����ͶƱ/�ύͶƱ��0 1 2 3
		ContentValues cv = new ContentValues();
		cv.put("tijiaostate", tijiaostate);
		String[] args = { String.valueOf(id) };
		return SQLdb.update(table_people, cv, "id=?", args);
	}
	
	public int Update_pwh(String id, String newid) {
		//�ύ״̬������/�ύ����/����ͶƱ/�ύͶƱ��0 1 2 3
		ContentValues cv = new ContentValues();
		cv.put("pwhid", newid);//����Ϊnewid
		String[] args = { String.valueOf(id) };
		return SQLdb.update(table_pwh, cv, "pwhid=?", args);
	}
	
	
	// �޸���Ϣ
	public int Update_toupiao(String id, String toupiao, String tijiaostate) {
		// ͶƱ0 1 2 ͬ�� �޳� ����
		//�ύ״̬��δ�ύ/�ύ����/�ύͶƱ��0 1 2 3
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
			cursor = SQLdb.query(table_people, // table��
					new String[] { "id", "pinfen", "pogeyijian", "poge", "toupiao","tijiaostate" ,"f1","f2","f3"}, // �ֶ�
					"id = '" + id + "'", // ����
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
			cursor = SQLdb.query(table_people, // table��
					new String[] { "id", "pinfen", "pogeyijian", "poge", "toupiao","tijiaostate"  ,"f1","f2","f3"}, // �ֶ�
					"tijiaostate = '" + tijiaostate + "'", // ����
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
			cursor = SQLdb.query(table_people, // table��
					new String[] { "id", "pinfen", "pogeyijian", "poge", "toupiao","tijiaostate" ,"f1","f2","f3" }, // �ֶ�
					"id = '" + id  + "' and tijiaostate = '" + tijiaostate + "'", // ����
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
			cursor = SQLdb.query(table_people, // table��
					new String[] { "id", "pinfen", "pogeyijian", "poge", "toupiao","tijiaostate"  ,"f1","f2","f3"}, // �ֶ�
					null, // ����
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
		///id ���� �Ƹ� ͶƱ �ύ״̬��δ�ύ/�ύ����/�ύͶƱ��
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
			cursor = SQLdb.query(table_Failedpeople, // table��
					new String[] { "id", "pinyu", "zhuangtai"}, // �ֶ�
					"id = '" + id + "'", // ����
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
			cursor = SQLdb.query(table_Failedpeople, // table��
					new String[] { "id", "pinyu", "zhuangtai"}, // �ֶ�
					"zhuangtai = '" + zhuangtai + "'", // ����
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
			cursor = SQLdb.query(table_Failedpeople, // table��
					new String[] { "id", "pinyu", "zhuangtai"}, // �ֶ�
					null, // ����
					null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}
		return cursor;
	}
}
