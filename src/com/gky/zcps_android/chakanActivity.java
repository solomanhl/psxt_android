/**
 * �鿴��Ͷ
 * 
 * @author ����
 * 
 */
package com.gky.zcps_android;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import com.gky.zcps_android.EntranceActivity.MessageHandler;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class chakanActivity extends Activity{
	private Global_var appState;
	private HashMap<String, Object> map = new HashMap<String, Object>();
	private Bundle bundle;
	private String leixin;
	
	private int cur = 0;
	
	private Cursor cursor,cursor_jiyu;
	
	private Handler handler=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//���óɺ���
		appState = ((Global_var) getApplicationContext()); // ���ȫ�ֱ���	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chakan);
		
		bundle = this.getIntent().getExtras();
		leixin = bundle.getString("leixin");		

		updateUI();
	}
	
	/*
	 //��ⰴ��
    @Override 	 
	public boolean onKeyDown(int keyCode, KeyEvent event) { 	
    	//���¼����Ϸ��ذ�ť   	 
    	if(keyCode == KeyEvent.KEYCODE_BACK){   
    		Log.i("info", "���ذ�ť");
    		if (appState.people_cur < appState.people_total -1){
    			appState.people_cur ++;
    		}
    		finish();
    		return true;
    		}else{
    			return super.onKeyDown(keyCode, event);
    		}
    }
    */
	
	private ArrayList<HashMap<String, Object>> lst ;
	// ������������ImageItem <====> ��̬�����Ԫ�أ�����һһ��Ӧ
	private MyListAdapter saImageItems;	
    private ListView listView_cart;
	
	private void updateUI() {
		// TODO Auto-generated method stub
		lst = new ArrayList<HashMap<String, Object>>();
		saImageItems = new MyListAdapter(this, lst);// ûʲô����	
		listView_cart = (ListView) findViewById(R.id.listView_chakan);
			
		map.put("bianhao", "���ϴ���" );
		map.put("xinmin", "����" );
		map.put("danwei", "��λ" );
		
		if ("jiyu".equals(leixin)){
			map.put("pinyuqinkuang", "���μ���");
		}else{
		map.put("pinfen", "����" );
		map.put("pogejieguo", "�Ƹ����" );
		map.put("ceshijieguo", "���Խ��" );
		map.put("toupiaoqinkuang", "ͶƱ���");
		
		if (appState.xianchangfenzu) {
			map.put("xiaozuyijian", "С�����");
		}
		}
		
		lst.add(map);
				
		if ("jiyu".equals(leixin)) {
			listJiyu();

		} else {// �Ǽ���
			listAllnew();			
			
		}//end �Ǽ���
		
		
		
		
		// ������������ImageItem <====> ��̬�����Ԫ�أ�����һһ��Ӧ
		//MyListAdapter saImageItems = new MyListAdapter(this, lst);// ûʲô����
		
		//������
		BinderListData(saImageItems);
	}
	
	
	//������
     public void BinderListData(MyListAdapter saImageItems)
     {
    	// ListView listView_cart = (ListView) findViewById(R.id.listView_chakan);
 		// ��Ӳ�����ʾ
 		listView_cart.setAdapter(saImageItems);
 		saImageItems.notifyDataSetChanged();
    }
	
	private void listAllnew() {
		appState.getDB();
		for (int i = 0; i<appState.people_total; i++){
			map = new HashMap<String, Object>();
			//cursor = null;
			//cursor = appState.queryTable(appState.peopleList.get(i).get("id").toString() );

			map.put("bianhao" , appState.peopleList.get(i).get("id").toString() );
			map.put("xinmin", appState.peopleList.get(i).get("name").toString() );
			map.put("danwei", appState.peopleList.get(i).get("company").toString() );
			
			map.put("ceshijieguo", appState.peopleList.get(i).get("ceshijieguo").toString() );
			if ("".equals(appState.peopleList.get(i).get("ceshijieguo").toString())){
				map.put("ceshijieguo", appState.peopleList.get(i).get("ceshijieguo").toString() );
	    	}else if ("F".equals(appState.peopleList.get(i).get("ceshijieguo").toString())){
	    		map.put("ceshijieguo", "���ϸ�" );
	    	}else{
	    		map.put("ceshijieguo", "�ϸ�" );
	    	}
			
			if (appState.xianchangfenzu) {
				map.put("xiaozuyijian", appState.scoreList.get(i).get("opinion").toString());
			}
			
			cursor = null;
			cursor = appState.queryTable(appState.peopleList.get(i).get("id").toString() );
			if (cursor != null && cursor.getCount()>0){//
				cursor.moveToNext();
				if ("pinfen".equals(leixin)){//���ֽ׶ζ����ݿ�
					map.put("pinfen", cursor.getString(cursor.getColumnIndex("pinfen")));
					if ("not_use".equals(cursor.getString(cursor.getColumnIndex("poge")))){
						map.put("pogejieguo", "��" );
					}else if ("yes".equals(cursor.getString(cursor.getColumnIndex("poge")))){
						map.put("pogejieguo", "ͬ��" );
					}else if ("no".equals(cursor.getString(cursor.getColumnIndex("poge")))){
						map.put("pogejieguo", "��ͬ��" );
					}
				}else if ("toupiao".equals(leixin)){//ͶƱ�׶ζ�scoreList
					if ( appState.xianchangfenzu) {
						map.put("pinfen", appState.scoreList.get(i).get("group_score").toString() );
					}else {
						map.put("pinfen", appState.scoreList.get(i).get("pinjunfen").toString() );
					}
					
					if ("".equals(appState.scoreList.get(i).get("pogejielun").toString()) ){
						map.put("pogejieguo", "��" );
					}else{
						map.put("pogejieguo", appState.scoreList.get(i).get("pogejielun").toString() );
					}		
					
					map.put("toupiaoqinkuang", cursor.getString(cursor.getColumnIndex("toupiao") ));
					
					
				}					
			}else {//û�ҵ�   ���������ֽ׶�  ������ͶƱ�׶���������ί��Ĳ�����Ա
				map.put("pinfen", "");
				map.put("pogejieguo", "");		
				
				if ("pinfen".equals(leixin)){//���ֽ׶ζ����ݿ�
					map.put("pinfen", "");
					map.put("pogejieguo", "");		
				}else if ("toupiao".equals(leixin)){//ͶƱ�׶ζ�scoreList
					if ( appState.xianchangfenzu) {
						map.put("pinfen", appState.scoreList.get(i).get("group_score").toString() );
					}else {
						map.put("pinfen", appState.scoreList.get(i).get("pinjunfen").toString() );
					}
					map.put("pogejieguo", appState.scoreList.get(i).get("pogejielun").toString());		
					map.put("toupiaoqinkuang", "δͶƱ" );
				}					
			}
			cursor.close();
			
			lst.add(map);			

		}//end for
		
		cursor.close();
		appState.dbClose();
	}
	
	private void listAll() {
		// TODO Auto-generated method stub
		appState.getDB();
		cursor = appState.getAll();		
	if (cursor != null && cursor.getCount() > 0) {
		while (cursor.moveToNext()) {
			// ��peopleList��scoreList��������
			int j;
			for (j = 0; j < appState.people_total; j++) {
				if (cursor.getString(cursor.getColumnIndex("id")).equals(appState.peopleList.get(j).get("id").toString())){
					cur = j;
					break;
				}
			}
			
			if (j==appState.people_total){//û�ҵ�
				//do nothing
				
				
			}else{
				map = new HashMap<String, Object>();

				map.put("bianhao" ,cursor.getString(cursor.getColumnIndex("id")));
				map.put("xinmin", appState.peopleList.get(cur).get("name").toString() );
				map.put("danwei", appState.peopleList.get(cur).get("company").toString() );
				map.put("ceshijieguo", appState.peopleList.get(cur).get("ceshijieguo").toString() );
				
				if ("pinfen".equals(leixin)){//���ֽ׶ζ����ݿ�
					map.put("pinfen", cursor.getString(cursor.getColumnIndex("pinfen")));
					if ("not_use".equals(cursor.getString(cursor.getColumnIndex("poge")))){
						map.put("pogejieguo", "��" );
					}else if ("yes".equals(cursor.getString(cursor.getColumnIndex("poge")))){
						map.put("pogejieguo", "ͬ��" );
					}else if ("no".equals(cursor.getString(cursor.getColumnIndex("poge")))){
						map.put("pogejieguo", "��ͬ��" );
					}
				}else if ("toupiao".equals(leixin)){//ͶƱ�׶ζ�scoreList
					map.put("pinfen", appState.scoreList.get(cur).get("pinjunfen").toString() );
					if ("".equals(appState.scoreList.get(cur).get("pogejielun").toString()) ){
						map.put("pogejieguo", "��" );
					}else{
						map.put("pogejieguo", appState.scoreList.get(cur).get("pogejielun").toString() );
					}		
					map.put("toupiaoqinkuang", cursor.getString(cursor.getColumnIndex("toupiao") ));
				}								
				
				lst.add(map);
			}//end if
			
		}//end while
		cursor.close();
		appState.dbClose();
	}else {
		cursor.close();
		appState.dbClose();
	}
	}

	private void listJiyu() {
		// TODO Auto-generated method stub
		appState.getDB();
		cursor_jiyu = appState.getFailedAll();	
		if (cursor_jiyu != null && cursor_jiyu.getCount() > 0) {
			while (cursor_jiyu.moveToNext()) {
				// ��failedList��������
				int j;
				for (j = 0; j < appState.people_total; j++) {
					if (cursor_jiyu.getString(cursor_jiyu.getColumnIndex("id")).equals(appState.failedList.get(j).get("id_f").toString())){
						cur = j;
						break;
					}
				}
				
				if (j==appState.people_total){//û�ҵ�
					//do nothing
					
					
				}else{
					map = new HashMap<String, Object>();

					map.put("bianhao" ,cursor_jiyu.getString(cursor_jiyu.getColumnIndex("id")));
					map.put("xinmin", appState.failedList.get(cur).get("name_f").toString() );
					map.put("danwei", appState.failedList.get(cur).get("company_f").toString() );
					/*
					map.put("ceshijieguo", appState.peopleList.get(cur).get("ceshijieguo").toString() );
					
					if ("pinfen".equals(leixin)){//���ֽ׶ζ����ݿ�
						map.put("pinfen", cursor.getString(cursor.getColumnIndex("pinfen")));
						if ("not_use".equals(cursor.getString(cursor.getColumnIndex("poge")))){
							map.put("pogejieguo", "��" );
						}else{
							map.put("pogejieguo", cursor.getString(cursor.getColumnIndex("poge")) );
						}
					}else if ("toupiao".equals(leixin)){//ͶƱ�׶ζ�scoreList
						map.put("pinfen", appState.scoreList.get(cur).get("pinjunfen").toString() );
						if ("".equals(appState.scoreList.get(cur).get("pogejielun").toString()) ){
							map.put("pogejieguo", "��" );
						}else{
							map.put("pogejieguo", appState.scoreList.get(cur).get("pogejielun").toString() );
						}						
					}
					*/
					
					
					//pinyuqinkuang
					if (cursor_jiyu.getString(cursor_jiyu.getColumnIndex("pinyu"))!=null 
							&& !"".equals(cursor_jiyu.getString(cursor_jiyu.getColumnIndex("pinyu")))
							){
						map.put("pinyuqinkuang",  "����д");
					}else{
						map.put("pinyuqinkuang",  "");
					}
					
					lst.add(map);
				}//end if
				
			}//end while
			cursor_jiyu.close();
			appState.dbClose();
		}else {
			cursor_jiyu.close();
			appState.dbClose();
		}
	}

	/*
	 * �������Զ����BaseAdapter��
	 */
	public class MyListAdapter extends BaseAdapter {
		private ArrayList<HashMap<String, Object>> data;
		private LayoutInflater layoutInflater;
		private Context context;

		public MyListAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
			this.context = context;
			this.data = data;
			this.layoutInflater = LayoutInflater.from(context);
		}

		/**
		 * ��ȡ����
		 */
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		/**
		 * ��ȡĳһλ�õ�����
		 */
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		/**
		 * ��ȡΨһ��ʶ
		 */
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		/**
		 * android����ÿһ�е�ʱ�򣬶�������������
		 */
		ZuJian_chakan zuJian = null;
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			if (convertView == null) {
				zuJian = new ZuJian_chakan();
				// ��ȡ�������
				convertView = layoutInflater.inflate(R.layout.listview_chakan, null);

				zuJian.list_chakan = (RelativeLayout) convertView.findViewById(R.id.list_chakan);
				zuJian.bianhao = (TextView) convertView.findViewById(R.id.bianhao);

				zuJian.xinmin = (TextView) convertView.findViewById(R.id.xinmin);
				zuJian.danwei = (TextView) convertView.findViewById(R.id.danwei);
				
				if ("jiyu".equals(leixin)){
					zuJian.pinyuqinkuang = (TextView) convertView.findViewById(R.id.pinyuqinkuang);
				}else{
				
					zuJian.zongfen = (TextView) convertView.findViewById(R.id.zongfen);
					zuJian.pogejieguo = (TextView) convertView.findViewById(R.id.pogejieguo);
					zuJian.ceshijieguo = (TextView) convertView.findViewById(R.id.ceshijieguo);
					zuJian.toupiaoqinkuang = (TextView) convertView.findViewById(R.id.toupiaoqinkuang);
					
					if ( appState.xianchangfenzu ) {
						zuJian.pinyuqinkuang = (TextView) convertView.findViewById(R.id.pinyuqinkuang);   //���������С�����
					}
				
				}//end else
				
				
				
				
				zuJian.button_modify = (Button) convertView.findViewById(R.id.button_modify);				
				
				
					if (position == 0 ){
						zuJian.button_modify.setVisibility(View.INVISIBLE);
						System.out.println( "���أ�position = " + String.valueOf(position));
					}else {
						zuJian.button_modify.setVisibility(View.VISIBLE);
						System.out.println( "��ʾ��position = " + String.valueOf(position));
					}
					// ����Ҫע�⣬��ʹ�õ�tag���洢���ݵġ�
					convertView.setTag(zuJian);				
			} else {
				zuJian = (ZuJian_chakan) convertView.getTag();
				
				if (position == 0 ){
					zuJian.button_modify.setVisibility(View.INVISIBLE);
					System.out.println( "���أ�position = " + String.valueOf(position));
				}else {
					zuJian.button_modify.setVisibility(View.VISIBLE);
					System.out.println( "��ʾ��position = " + String.valueOf(position));
				}
			}
			// �����ݡ��Լ��¼�����			
			zuJian.bianhao.setText((String) data.get(position).get("bianhao"));
			zuJian.xinmin.setText((String) data.get(position).get("xinmin"));			
			zuJian.danwei.setText((String) data.get(position).get("danwei"));
			
			if ("jiyu".equals(leixin)){
				//pinyuqinkuang
				zuJian.pinyuqinkuang.setText((String) data.get(position).get("pinyuqinkuang"));
				
				/*
				zuJian.pinyuqinkuang.setVisibility(View.VISIBLE);
				
				zuJian.zongfen.setVisibility(View.INVISIBLE);
				zuJian.pogejieguo.setVisibility(View.INVISIBLE);
				zuJian.ceshijieguo.setVisibility(View.INVISIBLE);
				zuJian.toupiaoqinkuang.setVisibility(View.INVISIBLE);
				*/
			}else{
				
				if ( appState.xianchangfenzu ) {
					zuJian.pinyuqinkuang.setText((String) data.get(position).get("xiaozuyijian"));
					if ("�Ƽ�".equals((String) data.get(position).get("xiaozuyijian"))){
						zuJian.pinyuqinkuang.setTextColor(0xff000000);//��ɫ
					}else if("���Ƽ�".equals((String) data.get(position).get("xiaozuyijian"))){
						zuJian.pinyuqinkuang.setTextColor(0x88AA0000);//��ɫ
					}else{
						zuJian.pinyuqinkuang.setTextColor(0xff000000);//��ɫ
					}
				}
			
			zuJian.zongfen.setText((String) data.get(position).get("pinfen"));
			
			if ("��".equals((String) data.get(position).get("pogejieguo"))){
				zuJian.pogejieguo.setTextColor(0x880000AA);//��ɫ
				zuJian.pogejieguo.setText("");
			}else if("ͬ��".equals((String) data.get(position).get("pogejieguo"))){
				zuJian.pogejieguo.setTextColor(0xff000000);//��ɫ
				zuJian.pogejieguo.setText((String) data.get(position).get("pogejieguo"));
			}else if("��ͬ��".equals((String) data.get(position).get("pogejieguo"))){
				zuJian.pogejieguo.setTextColor(0x88AA0000);//��ɫ
				zuJian.pogejieguo.setText((String) data.get(position).get("pogejieguo"));
			}else if ("".equals((String) data.get(position).get("pogejieguo"))){
				zuJian.pogejieguo.setTextColor(0xff000000);//��ɫ
				zuJian.pogejieguo.setText("");
			}else{
				zuJian.pogejieguo.setText((String) data.get(position).get("pogejieguo"));
			}			
			//zuJian.pogejieguo.setTextColor(0xff000000);//��ɫ
			
			
			zuJian.ceshijieguo.setText((String) data.get(position).get("ceshijieguo"));
			if ("�ϸ�".equals((String) data.get(position).get("ceshijieguo"))){
				zuJian.ceshijieguo.setTextColor(0xff000000);//��ɫ
			}else if("���ϸ�".equals((String) data.get(position).get("ceshijieguo"))){
				zuJian.ceshijieguo.setTextColor(0x88AA0000);//��ɫ
			}else{
				zuJian.ceshijieguo.setTextColor(0xff000000);//��ɫ
			}

			if ("�޳�".equals((String) data.get(position).get("toupiaoqinkuang"))){
				zuJian.toupiaoqinkuang.setTextColor(0x8800AA00);//��ɫ
			}else if("����".equals((String) data.get(position).get("toupiaoqinkuang"))){
				zuJian.toupiaoqinkuang.setTextColor(0x88AA0000);//��ɫ
			}else if("��Ȩ".equals((String) data.get(position).get("toupiaoqinkuang"))){
				zuJian.toupiaoqinkuang.setTextColor(0x88AA0000);//��ɫ
			}else{
				zuJian.toupiaoqinkuang.setTextColor(0xff000000);//��ɫ
			}
			zuJian.toupiaoqinkuang.setText((String) data.get(position).get("toupiaoqinkuang"));
			
			/*
				zuJian.pinyuqinkuang.setVisibility(View.INVISIBLE);

				zuJian.zongfen.setVisibility(View.VISIBLE);
				zuJian.pogejieguo.setVisibility(View.VISIBLE);
				zuJian.ceshijieguo.setVisibility(View.VISIBLE);
				zuJian.toupiaoqinkuang.setVisibility(View.VISIBLE);
				*/
			}//end else
			
			
			
			
			
		
			zuJian.button_modify.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					button_fanhui(position);
				}			
			});		

			
			return convertView;
		}

		private void button_fanhui(int position) {
			// TODO Auto-generated method stub
			String code = (String) data.get(position).get("bianhao");	
			
			int j;
			for (j = 0; j < appState.people_total; j++) {
				if ("jiyu".equals(leixin)){
					if (code.equals(appState.failedList.get(j).get("id_f").toString())){
						appState.people_cur = j;
						break;
					}
				}else {
					if (code.equals(appState.peopleList.get(j).get("id").toString())){
						appState.people_cur = j;
						break;
					}
				}
				
			}
			clearPre();
			finish();
		}

	}
	
	private void clearPre() {
		// TODO Auto-generated method stub
		if ("pinfen".equals(appState.tab5_state)) {
    		SharedPreferences userInfo = getSharedPreferences("tab5_pinfen", 0);  
    			userInfo.edit().putString("f1", "").commit();
    			userInfo.edit().putString("f2", "").commit();
    			userInfo.edit().putString("f3", "").commit();  
    			userInfo.edit().putString("po", "").commit();  
				userInfo.edit().putString("postate","not_use").commit();    
    		}else if ("toupiao".equals(appState.tab5_state)) {
    			SharedPreferences userInfo = getSharedPreferences("tab5_toupiao", 0);  
    			userInfo.edit().putString("toupiao", "").commit();
    		}else if ("pinyu".equals(appState.workfloat)){
    			SharedPreferences userInfo = getSharedPreferences("pinyu", 0);  
    			userInfo.edit().putString("pinyu","").commit();
    		}
	}

	public void listView_chakan_submit_onclick(View target){
		if ("pinfen".equals(leixin)){//���ֽ׶ζ����ݿ�
			appState.getDB();
			cursor = appState.getAll();
			if (cursor.getCount() >= appState.people_total) {//�ж��Ƿ�ȫ���������
			//if(true){ //���ԣ�����  ֱ�ӷ�
				cursor.close();
				// ���� ��ʾ�Ƿ��ύ����
				new AlertDialog.Builder(this)
						.setTitle("�Ƿ��ύ�������ݣ�")
						.setMessage("���в�����Ա�Ѿ�������ɡ�\n�����ȷ������ť�ύ�������ݵ������������\n��������ء���ť���Լ����޸ġ�\nע�⣺ѡ��ȷ��֮�󣬲����޸Ĳ�����Ա�ĸ������ݣ�")
						.setNegativeButton("����",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										//this.s = "Negative";
									}
								})
						.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										//s = "Positive";
										//�ύ���֣��ύ�ɹ�һ������һ�����ݿ�
										//cursor.close();
										//appState.dbClose();
										appState.getDB();
										/*
										for (cur = 0 ; cur<appState.people_total; cur++){
											// �ύ���� 
											String tmp = submitPinfen();
											 if ("���ճɹ�".equals(tmp)){ // ״̬��Ϊ���ύ���� // �ύ״̬��δ�ύ/�ύ����/�ύͶƱ��0 1 2  3
												 //appState.dbClose();
												 //appState.getDB();
												 appState.Update_tijiao(appState.peopleList.get(cur).get("id").toString(), "1");// �ύ����
												
												Toast toast = Toast.makeText(getApplicationContext(),
														appState.peopleList.get(cur).get("id").toString() + "�ύ�ɹ���", 
														Toast.LENGTH_SHORT);
												toast.setGravity(Gravity.CENTER, 0,0);
												toast.cancel();
												toast.show();
												
											} else if ("����ʧ��".equals(tmp)) {
												Toast toast = Toast.makeText(getApplicationContext(),
														appState.peopleList.get(cur).get("id").toString() + "����������ʧ�ܣ��������ύ��",
														Toast.LENGTH_SHORT);
												toast.setGravity(Gravity.CENTER, 0, 0);
												toast.show();
											}
										}
										*/
										
										//�ύ���֣�һ�����ύ�󣬸���ȫ�����ݿ�
										//����׼������
										/* ̫����
										String dataTransform = "pwhid=" + URLEncoder.encode(appState.pwhid) //��ί��
												+ "&pwid=" + URLEncoder.encode(appState.pinweiName)	// ��ί
												+ "&data=[";
										*/
										
										StringBuilder dataTransformb = new StringBuilder();
										dataTransformb.append( "pwhid=" + URLEncoder.encode(appState.pwhid) //��ί��
												+ "&pwid=" + URLEncoder.encode(appState.pinweiName)	// ��ί
												+ "&data=[");
										
										for (cur=0; cur<appState.people_total; cur++){
											cursor = appState.queryTable(appState.peopleList.get(cur).get("id").toString());
											cursor.moveToNext();

											//�ϳ��ύ����,����ʱ��ʱ����
											
											dataTransformb.append( "{\"id\":\"" + URLEncoder.encode(appState.peopleList.get(cur).get("id").toString()) + "\","// ������
													+ "\"poge\":\"" + URLEncoder.encode(cursor.getString(3)) + "\","// �Ƹ�
													+ "\"content\":\"" + URLEncoder.encode(cursor.getString(2)) + "\","  //�Ƹ����
													+ "\"total\":\"" + URLEncoder.encode(cursor.getString(1)) + "\""  //�ܷ�
													+ "},");
											
											
											/*
											//����  ���� ��
											dataTransformb.append("{\"id\":\"" + URLEncoder.encode(appState.peopleList.get(cur).get("id").toString()) + "\","// ������
													+ "\"poge\":\"" + URLEncoder.encode("") + "\","// �Ƹ�
													+ "\"content\":\"" + URLEncoder.encode("") + "\","  //�Ƹ����
													+ "\"total\":\"" + URLEncoder.encode("100") + "\""  //�ܷ�
													+ "},");
											*/									
											
											cursor.close();	
										}//end for
										dataTransformb.deleteCharAt(dataTransformb.length() - 1);//ȥ�����һ������
										dataTransformb .append( "]");
										
										//���� �ύ���� 
										String tmp = submitPinfen(dataTransformb.toString());
										 if ("���ճɹ�".equals(tmp)){ // ״̬��Ϊ���ύ���� // �ύ״̬��δ�ύ/�ύ����/�ύͶƱ��0 1 2  3
											// appState.dbClose();
											// appState.getDB();
											for (cur = 0; cur < appState.people_total; cur++) {
												appState.Update_tijiao(appState.peopleList.get(cur).get("id").toString(), "1");// �ύ����
											}

												Toast toast = Toast.makeText(getApplicationContext(),
														"�ύ�ɹ���", 
														Toast.LENGTH_SHORT);
												toast.setGravity(Gravity.CENTER, 0,0);
												toast.cancel();
												toast.show();
												
												System.out.println("�����ύ�ɹ�\r\n");
												
												appState.closeMain = true;//�ύ�ɹ��Źر�������
										} else if ("����ʧ��".equals(tmp)) {
											Toast toast = Toast.makeText(getApplicationContext(),
													 "����������ʧ�ܣ��������ύ��",
													Toast.LENGTH_SHORT);
											toast.setGravity(Gravity.CENTER, 0, 0);
											toast.show();
											
											System.out.println("�����ύʧ��\r\n");
											//�ύʧ�ܼ�������������
										}
										
										
										
										appState.dbClose();
										cur = 0;//end for
										
										//appState.closeMain = true;//�ر�������
										finish();
									}
								}).show();			
			}else{
				new AlertDialog.Builder(this)
				.setTitle("������Ա����δ��ɣ�")
				.setMessage("���в�����Աδ���֣�������в�����Ա������ɺ����ύ���ݣ�")
				/*
				.setNegativeButton("����",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//this.s = "Negative";
							}
						})
						*/
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								
							}
						}).show();			
			}

			cursor.close();
			appState.dbClose();
		}else if ("toupiao".equals(leixin)){//ͶƱ�׶ζ�scoreList
			appState.getDB();
		 	//cursor = appState.queryTable_tijiaostate("2");
			cursor = appState.getToupiao();
			if (cursor.getCount() >= appState.people_total) {//�ж��Ƿ�ȫ���������
			//if (true){	//���� ����  ֱ�ӷ�
				cursor.close();
				// ���� ��ʾ�Ƿ��ύ����
				new AlertDialog.Builder(this)
						.setTitle("�Ƿ��ύͶƱ���ݣ�")
						.setMessage("���в�����Ա�Ѿ�ͶƱ��ɡ�\n�����ȷ������ť�ύͶƱ����������������\n��������ء���ť���Լ����޸ġ�\nע�⣺ѡ��ȷ��֮�󣬲����޸Ĳ�����Ա��ͶƱ��Ϣ��")
						.setNegativeButton("����",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										//this.s = "Negative";
									}
								})
						.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										//s = "Positive";
										//cursor.close();
										//appState.dbClose();
										appState.getDB();
										/*
										//�ύ���֣��ύ�ɹ�һ������һ�����ݿ�
										for (cur=0; cur<appState.people_total; cur++){
											// �ύ���� 
											String tmp = submitToupiao();
											 if ("���ճɹ�".equals(tmp)){ // ״̬��Ϊ���ύ���� // �ύ״̬��δ�ύ/�ύ����/�ύͶƱ��0 1 2  3
												// appState.dbClose();
												// appState.getDB();
												 appState.Update_tijiao(appState.peopleList.get(cur).get("id").toString(), "3");// �ύͶƱ


													Toast toast = Toast.makeText(getApplicationContext(),
															appState.peopleList.get(cur).get("id").toString() + "�ύ�ɹ���", 
															Toast.LENGTH_SHORT);
													toast.setGravity(Gravity.CENTER, 0,0);
													toast.cancel();
													toast.show();
											} else if ("����ʧ��".equals(tmp)) {
												Toast toast = Toast.makeText(getApplicationContext(),
														appState.peopleList.get(cur).get("id").toString() + "����������ʧ�ܣ��������ύ��",
														Toast.LENGTH_SHORT);
												toast.setGravity(Gravity.CENTER, 0, 0);
												toast.show();
											}
										}//end for
										*/
										
										//�ύ���֣�һ�����ύ�󣬸���ȫ�����ݿ�
										//����׼������
										/*
										String dataTransform = "pwhid=" + URLEncoder.encode(appState.pwhid) //��ί��
												+ "&pwid=" + URLEncoder.encode(appState.pinweiName)	// ��ί
												+ "&data=[";
										*/
										
										StringBuilder dataTransformb = new StringBuilder();
										dataTransformb.append("pwhid=" + URLEncoder.encode(appState.pwhid) //��ί��
												+ "&pwid=" + URLEncoder.encode(appState.pinweiName)	// ��ί
												+ "&data=[");
										
										for (cur=0; cur<appState.people_total; cur++){
											cursor = appState.queryTable(appState.peopleList.get(cur).get("id").toString());
											cursor.moveToNext();

											//�ϳ��ύ����
											dataTransformb.append( "{\"id\":\"" + URLEncoder.encode(appState.peopleList.get(cur).get("id").toString()) + "\","// ������
													+ "\"vote\":\"" + URLEncoder.encode( cursor.getString(4)) + "\"" //ͶƱ
													+ "},");
																						
											cursor.close();	
										}//end for
										//dataTransform = dataTransform.substring(0, dataTransform.length() - 1);//ȥ�����һ������
										//dataTransform += "]";
										dataTransformb.deleteCharAt(dataTransformb.length() - 1);//ȥ�����һ������
										dataTransformb .append( "]");
										
										//���� �ύ���� 
										String tmp = submitToupiao(dataTransformb.toString());
										 if ("���ճɹ�".equals(tmp)){ // ״̬��Ϊ���ύ���� // �ύ״̬��δ�ύ/�ύ����/�ύͶƱ��0 1 2  3
											// appState.dbClose();
											// appState.getDB();
											for (cur = 0; cur < appState.people_total; cur++) {
												appState.Update_tijiao(appState.peopleList.get(cur).get("id").toString(), "3");// �ύͶƱ
											}

												Toast toast = Toast.makeText(getApplicationContext(),
														"�ύ�ɹ���", 
														Toast.LENGTH_SHORT);
												toast.setGravity(Gravity.CENTER, 0,0);
												toast.cancel();
												toast.show();
												
												System.out.println("ͶƱ�ύ�ɹ�\r\n");
												
												appState.closeMain = true;//�ύ�ɹ��Źر�������
												
												if (appState.xianchangfenzu){
													//�ֳ�����
													appState.pinshenjieshu = true;
												}
												finish();
										} else if ("����ʧ��".equals(tmp)) {
											Toast toast = Toast.makeText(getApplicationContext(),
													 "����������ʧ�ܣ��������ύ��",
													Toast.LENGTH_SHORT);
											toast.setGravity(Gravity.CENTER, 0, 0);
											toast.show();
											
											System.out.println("ͶƱ�ύʧ��\r\n");
											//�ύʧ�ܼ�������������
										}
										
										appState.dbClose();
										cur = 0;
										//appState.closeMain = true;//�ر�������
										
										
									}
								}).show();			
			}else{
				new AlertDialog.Builder(this)
				.setTitle("������ԱͶƱδ��ɣ�")
				.setMessage("���в�����ԱδͶƱ��������в�����ԱͶƱ��ɺ����ύ���ݣ�")
				/*
				.setNegativeButton("����",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//this.s = "Negative";
							}
						})
						*/
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								
							}
						}).show();			
			}
			cursor.close();
			appState.dbClose();
		}else if ("jiyu".equals(leixin)){//����
			appState.getDB();
		 	cursor = appState.getFailedAll();
			if (cursor.getCount() >= appState.people_total) {
				cursor.close();
				// ���� ��ʾ�Ƿ��ύ����
				new AlertDialog.Builder(this)
						.setTitle("�Ƿ��ύ���μ��")
						.setMessage("����δͨ����Ա�����μ�����д��ɡ�\n�����ȷ������ť�ύ���μ��ﵽ�����������\n��������ء���ť���Լ����޸ġ�\nע�⣺ѡ��ȷ��֮�󣬲����޸�δͨ����Ա�����μ��")
						.setNegativeButton("����",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										//this.s = "Negative";
									}
								})
						.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										//s = "Positive";
										//cursor.close();
										//appState.dbClose();
										appState.getDB();
										/*
										//�ύ���֣��ύ�ɹ�һ������һ�����ݿ�
										for (cur=0; cur<appState.people_total; cur++){
											// �ύ���� 
											String tmp = submitJiyu();
											 if ("���ճɹ�".equals(tmp)){ // ״̬��Ϊ���ύ���� // �ύ״̬��δ�ύ/�ύ����/�ύͶƱ��0 1 2  3
												// appState.dbClose();
												// appState.getDB();
												 appState.Update_failedState(appState.failedList.get(cur).get("id_f").toString(), "1");// �ύ����


													Toast toast = Toast.makeText(getApplicationContext(),
															appState.failedList.get(cur).get("id_f").toString() + "�ύ�ɹ���", 
															Toast.LENGTH_SHORT);
													toast.setGravity(Gravity.CENTER, 0,0);
													toast.cancel();
													toast.show();
											} else if ("����ʧ��".equals(tmp)) {
												Toast toast = Toast.makeText(getApplicationContext(),
														appState.failedList.get(cur).get("id_f").toString() + "����������ʧ�ܣ��������ύ��",
														Toast.LENGTH_SHORT);
												toast.setGravity(Gravity.CENTER, 0, 0);
												toast.show();
											}
										}//end for
										*/
										
										//�ύ���֣�һ�����ύ�󣬸���ȫ�����ݿ�
										//����׼������
										StringBuilder dataTransformb = new StringBuilder();
										dataTransformb.append( "pwhid=" + URLEncoder.encode(appState.pwhid) //��ί��
												+ "&pwid=" + URLEncoder.encode(appState.pinweiName)	// ��ί
												+ "&data=[" );
										
										
										for (cur=0; cur<appState.people_total; cur++){
											cursor = appState.queryFailed(appState.failedList.get(cur).get("id_f").toString());
											cursor.moveToNext();

											//�ϳ��ύ����
											dataTransformb.append(  "{\"id\":\"" + URLEncoder.encode(appState.failedList.get(cur).get("id_f").toString()) + "\","// ������
													+ "\"name\":\"" + URLEncoder.encode(appState.failedList.get(cur).get("name_f").toString()) + "\","// ����������
													+ "\"jiyu\":\"" + URLEncoder.encode(cursor.getString(1)) + "\"" //����
													+ "},"  );
																						
											cursor.close();	
										}//end for
										dataTransformb.deleteCharAt(dataTransformb.length() -1 );//ȥ�����һ������
										dataTransformb.append("]");
										
										//���� �ύ���� 
										String tmp = submitJiyu(dataTransformb.toString());
										 if ("���ճɹ�".equals(tmp)){ // ״̬��Ϊ���ύ���� // �ύ״̬��δ�ύ/�ύ����/�ύͶƱ��0 1 2  3
											// appState.dbClose();
											// appState.getDB();
											for (cur = 0; cur < appState.people_total; cur++) {
												appState.Update_failedState(appState.failedList.get(cur).get("id_f").toString(),"1");// �ύ����
											}

												Toast toast = Toast.makeText(getApplicationContext(),
														"�ύ�ɹ���", 
														Toast.LENGTH_SHORT);
												toast.setGravity(Gravity.CENTER, 0,0);
												toast.cancel();
												toast.show();
												
												appState.closeMain = true;//�ύ�ɹ��Źر�������
										} else if ("����ʧ��".equals(tmp)) {
											Toast toast = Toast.makeText(getApplicationContext(),
													 "����������ʧ�ܣ��������ύ��",
													Toast.LENGTH_SHORT);
											toast.setGravity(Gravity.CENTER, 0, 0);
											toast.show();
											
											//�ύʧ�ܼ�������������
										}
										
										
										appState.dbClose();
										cur = 0;
										//appState.closeMain = true;//�ر�������
										
										finish();
									}
								}).show();			
			}else{
				new AlertDialog.Builder(this)
				.setTitle("������ԱͶƱδ��ɣ�")
				.setMessage("���в�����ԱδͶƱ��������в�����ԱͶƱ��ɺ����ύ���ݣ�")
				/*
				.setNegativeButton("����",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//this.s = "Negative";
							}
						})
						*/
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								
							}
						}).show();			
			}
			cursor.close();
			appState.dbClose();
		}
	}

	private String submitPinfen(String dataTransform) {
		// TODO Auto-generated method stub
		/*
		 * (��ί�ᣩpwhid ����ίid��pwid (�����ˣ�id 10λid
		 * 
		 * ���Ƹ�poge yes no ��ѡһ (�Ƹ����ݣ�content �ַ���
		 * 
		 * ���ܷ֣�total 0~100������
		 */

		String tmp = "����ʧ��";

		// Ҫ���ʵ�web servlet
		// ע�⣺IP�Ͷ˿��Ǳ��ص� ��Ҫ�������IP�Ͷ˿�
		String servletUrl = appState.HttpHead + "/expert/score";
		// ����������������
		String resultData = "";
		URL url = null;
		try {
			url = new URL(servletUrl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (url != null) {
			try {
				// ʹ��HttpURLConnection������
				HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
				urlConn.setConnectTimeout(appState.REQUEST_TIMEOUT);
				urlConn.setReadTimeout(appState.SO_TIMEOUT);
				// ��ΪҪ��ʹ��Post��ʽ�ύ���ݣ���Ҫ����Ϊtrue
				urlConn.setDoOutput(true);
				urlConn.setDoInput(true);
				// ������Post��ʽ��ע��˴��ġ�POST�������д
				urlConn.setRequestMethod("POST");
				// Post ������ʹ�û���
				urlConn.setUseCaches(false);
				urlConn.setInstanceFollowRedirects(true);
				// ���ñ������ӵ�Content-Type������Ϊapplication/x-www-form-urlencoded
				urlConn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				// ���ӣ���postUrl.openConnection()���˵����ñ�����connect֮ǰ���
				// Ҫע�����connection.getOutputStream�������ؽ���connect��
				urlConn.connect();
				// DataOutputStream���ϴ�����
				DataOutputStream out = new DataOutputStream(
						urlConn.getOutputStream());
				// ��Ҫ�ϴ�������д������
				out.writeBytes(dataTransform);
				// ˢ�£��ر�
				out.flush();
				out.close();
				// �õ���ȡ������
				InputStreamReader in = new InputStreamReader(
						urlConn.getInputStream());
				BufferedReader buffer = new BufferedReader(in);
				String str = null;
				while ((str = buffer.readLine()) != null) {
					resultData += str;
				}
				in.close();
				urlConn.disconnect();
				if ("���ճɹ�".equals(resultData)) {
					tmp = "���ճɹ�";
				} else {

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}// if(url!=null)
		else {

		}

		return tmp;
	}
	
	private String submitToupiao(String dataTrasform) {
		// TODO Auto-generated method stub
		/*
		 * (��ί�ᣩpwhid ����ίid��pwid (�����ˣ�id 10λid (ͶƱ��toupiao ͬ�� ���� ��Ȩ ��ѡ1
		 */
		String tmp = "����ʧ��";

		// Ҫ���ʵ�web servlet
		// ע�⣺IP�Ͷ˿��Ǳ��ص� ��Ҫ�������IP�Ͷ˿�
		String servletUrl = appState.HttpHead + "/expert/vote";
		// ����������������
		String resultData = "";
		URL url = null;
		try {
			url = new URL(servletUrl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (url != null) {
			try {
				// ʹ��HttpURLConnection������
				HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
				urlConn.setConnectTimeout(appState.REQUEST_TIMEOUT);
				urlConn.setReadTimeout(appState.SO_TIMEOUT);
				// ��ΪҪ��ʹ��Post��ʽ�ύ���ݣ���Ҫ����Ϊtrue
				urlConn.setDoOutput(true);
				urlConn.setDoInput(true);
				// ������Post��ʽ��ע��˴��ġ�POST�������д
				urlConn.setRequestMethod("POST");
				// Post ������ʹ�û���
				urlConn.setUseCaches(false);
				urlConn.setInstanceFollowRedirects(true);
				// ���ñ������ӵ�Content-Type������Ϊapplication/x-www-form-urlencoded
				urlConn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				// ���ӣ���postUrl.openConnection()���˵����ñ�����connect֮ǰ���
				// Ҫע�����connection.getOutputStream�������ؽ���connect��
				urlConn.connect();
				// DataOutputStream���ϴ�����
				DataOutputStream out = new DataOutputStream(
						urlConn.getOutputStream());
				// ��Ҫ�ϴ�������д������
				out.writeBytes(dataTrasform);
				// ˢ�£��ر�
				out.flush();
				out.close();
				// �õ���ȡ������
				InputStreamReader in = new InputStreamReader(
						urlConn.getInputStream());
				BufferedReader buffer = new BufferedReader(in);
				String str = null;
				while ((str = buffer.readLine()) != null) {
					resultData += str;
				}
				in.close();
				urlConn.disconnect();
			if ("���ճɹ�".equals(resultData)) {
					tmp = "���ճɹ�";
					
				} else {

				}
				Log.i("info", tmp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.i("info", tmp);
				Log.i("info", e.toString());
			}
		}// if(url!=null)
		else {

		}

		return tmp;
	}
	
	private String submitJiyu(String dataTransform) {
		// TODO Auto-generated method stub
		/*
		 * (��ί�ᣩpwhid ����ίid��pwid (�����ˣ�id 10λid (ͶƱ��toupiao ͬ�� ���� ��Ȩ ��ѡ1
		 */
		String tmp = "����ʧ��";

		// Ҫ���ʵ�web servlet
		// ע�⣺IP�Ͷ˿��Ǳ��ص� ��Ҫ�������IP�Ͷ˿�
		String servletUrl = appState.HttpHead + "/expert/comment";
		// ����������������
		String resultData = "";
		URL url = null;
		try {
			url = new URL(servletUrl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (url != null) {
			try {
				// ʹ��HttpURLConnection������
				HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
				urlConn.setConnectTimeout(appState.REQUEST_TIMEOUT);
				urlConn.setReadTimeout(appState.SO_TIMEOUT);
				// ��ΪҪ��ʹ��Post��ʽ�ύ���ݣ���Ҫ����Ϊtrue
				urlConn.setDoOutput(true);
				urlConn.setDoInput(true);
				// ������Post��ʽ��ע��˴��ġ�POST�������д
				urlConn.setRequestMethod("POST");
				// Post ������ʹ�û���
				urlConn.setUseCaches(false);
				urlConn.setInstanceFollowRedirects(true);
				// ���ñ������ӵ�Content-Type������Ϊapplication/x-www-form-urlencoded
				urlConn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				// ���ӣ���postUrl.openConnection()���˵����ñ�����connect֮ǰ���
				// Ҫע�����connection.getOutputStream�������ؽ���connect��
				urlConn.connect();
				// DataOutputStream���ϴ�����
				DataOutputStream out = new DataOutputStream(
						urlConn.getOutputStream());
				// ��Ҫ�ϴ�������д������
				out.writeBytes(dataTransform);
				// ˢ�£��ر�
				out.flush();
				out.close();
				// �õ���ȡ������
				InputStreamReader in = new InputStreamReader(
						urlConn.getInputStream());
				BufferedReader buffer = new BufferedReader(in);
				String str = null;
				while ((str = buffer.readLine()) != null) {
					resultData += str;
				}
				in.close();
				urlConn.disconnect();
				if ("���ճɹ�".equals(resultData)) {
					tmp = "���ճɹ�";
				} else {

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}// if(url!=null)
		else {

		}

		return tmp;
	}
}
