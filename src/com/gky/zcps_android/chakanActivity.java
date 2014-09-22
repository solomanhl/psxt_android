/**
 * 查看已投
 * 
 * @author 贺亮
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
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置成横屏
		appState = ((Global_var) getApplicationContext()); // 获得全局变量	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chakan);
		
		bundle = this.getIntent().getExtras();
		leixin = bundle.getString("leixin");		

		updateUI();
	}
	
	/*
	 //检测按键
    @Override 	 
	public boolean onKeyDown(int keyCode, KeyEvent event) { 	
    	//按下键盘上返回按钮   	 
    	if(keyCode == KeyEvent.KEYCODE_BACK){   
    		Log.i("info", "返回按钮");
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
	// 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
	private MyListAdapter saImageItems;	
    private ListView listView_cart;
	
	private void updateUI() {
		// TODO Auto-generated method stub
		lst = new ArrayList<HashMap<String, Object>>();
		saImageItems = new MyListAdapter(this, lst);// 没什么解释	
		listView_cart = (ListView) findViewById(R.id.listView_chakan);
			
		map.put("bianhao", "材料袋号" );
		map.put("xinmin", "姓名" );
		map.put("danwei", "单位" );
		
		if ("jiyu".equals(leixin)){
			map.put("pinyuqinkuang", "主任寄语");
		}else{
		map.put("pinfen", "评分" );
		map.put("pogejieguo", "破格结论" );
		map.put("ceshijieguo", "测试结果" );
		map.put("toupiaoqinkuang", "投票情况");
		
		if (appState.xianchangfenzu) {
			map.put("xiaozuyijian", "小组意见");
		}
		}
		
		lst.add(map);
				
		if ("jiyu".equals(leixin)) {
			listJiyu();

		} else {// 非寄语
			listAllnew();			
			
		}//end 非寄语
		
		
		
		
		// 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
		//MyListAdapter saImageItems = new MyListAdapter(this, lst);// 没什么解释
		
		//绑定数据
		BinderListData(saImageItems);
	}
	
	
	//绑定数据
     public void BinderListData(MyListAdapter saImageItems)
     {
    	// ListView listView_cart = (ListView) findViewById(R.id.listView_chakan);
 		// 添加并且显示
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
	    		map.put("ceshijieguo", "不合格" );
	    	}else{
	    		map.put("ceshijieguo", "合格" );
	    	}
			
			if (appState.xianchangfenzu) {
				map.put("xiaozuyijian", appState.scoreList.get(i).get("opinion").toString());
			}
			
			cursor = null;
			cursor = appState.queryTable(appState.peopleList.get(i).get("id").toString() );
			if (cursor != null && cursor.getCount()>0){//
				cursor.moveToNext();
				if ("pinfen".equals(leixin)){//评分阶段读数据库
					map.put("pinfen", cursor.getString(cursor.getColumnIndex("pinfen")));
					if ("not_use".equals(cursor.getString(cursor.getColumnIndex("poge")))){
						map.put("pogejieguo", "无" );
					}else if ("yes".equals(cursor.getString(cursor.getColumnIndex("poge")))){
						map.put("pogejieguo", "同意" );
					}else if ("no".equals(cursor.getString(cursor.getColumnIndex("poge")))){
						map.put("pogejieguo", "不同意" );
					}
				}else if ("toupiao".equals(leixin)){//投票阶段读scoreList
					if ( appState.xianchangfenzu) {
						map.put("pinfen", appState.scoreList.get(i).get("group_score").toString() );
					}else {
						map.put("pinfen", appState.scoreList.get(i).get("pinjunfen").toString() );
					}
					
					if ("".equals(appState.scoreList.get(i).get("pogejielun").toString()) ){
						map.put("pogejieguo", "无" );
					}else{
						map.put("pogejieguo", appState.scoreList.get(i).get("pogejielun").toString() );
					}		
					
					map.put("toupiaoqinkuang", cursor.getString(cursor.getColumnIndex("toupiao") ));
					
					
				}					
			}else {//没找到   可能是评分阶段  或者是投票阶段中其它评委会的参评人员
				map.put("pinfen", "");
				map.put("pogejieguo", "");		
				
				if ("pinfen".equals(leixin)){//评分阶段读数据库
					map.put("pinfen", "");
					map.put("pogejieguo", "");		
				}else if ("toupiao".equals(leixin)){//投票阶段读scoreList
					if ( appState.xianchangfenzu) {
						map.put("pinfen", appState.scoreList.get(i).get("group_score").toString() );
					}else {
						map.put("pinfen", appState.scoreList.get(i).get("pinjunfen").toString() );
					}
					map.put("pogejieguo", appState.scoreList.get(i).get("pogejielun").toString());		
					map.put("toupiaoqinkuang", "未投票" );
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
			// 在peopleList和scoreList里面找人
			int j;
			for (j = 0; j < appState.people_total; j++) {
				if (cursor.getString(cursor.getColumnIndex("id")).equals(appState.peopleList.get(j).get("id").toString())){
					cur = j;
					break;
				}
			}
			
			if (j==appState.people_total){//没找到
				//do nothing
				
				
			}else{
				map = new HashMap<String, Object>();

				map.put("bianhao" ,cursor.getString(cursor.getColumnIndex("id")));
				map.put("xinmin", appState.peopleList.get(cur).get("name").toString() );
				map.put("danwei", appState.peopleList.get(cur).get("company").toString() );
				map.put("ceshijieguo", appState.peopleList.get(cur).get("ceshijieguo").toString() );
				
				if ("pinfen".equals(leixin)){//评分阶段读数据库
					map.put("pinfen", cursor.getString(cursor.getColumnIndex("pinfen")));
					if ("not_use".equals(cursor.getString(cursor.getColumnIndex("poge")))){
						map.put("pogejieguo", "无" );
					}else if ("yes".equals(cursor.getString(cursor.getColumnIndex("poge")))){
						map.put("pogejieguo", "同意" );
					}else if ("no".equals(cursor.getString(cursor.getColumnIndex("poge")))){
						map.put("pogejieguo", "不同意" );
					}
				}else if ("toupiao".equals(leixin)){//投票阶段读scoreList
					map.put("pinfen", appState.scoreList.get(cur).get("pinjunfen").toString() );
					if ("".equals(appState.scoreList.get(cur).get("pogejielun").toString()) ){
						map.put("pogejieguo", "无" );
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
				// 在failedList里面找人
				int j;
				for (j = 0; j < appState.people_total; j++) {
					if (cursor_jiyu.getString(cursor_jiyu.getColumnIndex("id")).equals(appState.failedList.get(j).get("id_f").toString())){
						cur = j;
						break;
					}
				}
				
				if (j==appState.people_total){//没找到
					//do nothing
					
					
				}else{
					map = new HashMap<String, Object>();

					map.put("bianhao" ,cursor_jiyu.getString(cursor_jiyu.getColumnIndex("id")));
					map.put("xinmin", appState.failedList.get(cur).get("name_f").toString() );
					map.put("danwei", appState.failedList.get(cur).get("company_f").toString() );
					/*
					map.put("ceshijieguo", appState.peopleList.get(cur).get("ceshijieguo").toString() );
					
					if ("pinfen".equals(leixin)){//评分阶段读数据库
						map.put("pinfen", cursor.getString(cursor.getColumnIndex("pinfen")));
						if ("not_use".equals(cursor.getString(cursor.getColumnIndex("poge")))){
							map.put("pogejieguo", "无" );
						}else{
							map.put("pogejieguo", cursor.getString(cursor.getColumnIndex("poge")) );
						}
					}else if ("toupiao".equals(leixin)){//投票阶段读scoreList
						map.put("pinfen", appState.scoreList.get(cur).get("pinjunfen").toString() );
						if ("".equals(appState.scoreList.get(cur).get("pogejielun").toString()) ){
							map.put("pogejieguo", "无" );
						}else{
							map.put("pogejieguo", appState.scoreList.get(cur).get("pogejielun").toString() );
						}						
					}
					*/
					
					
					//pinyuqinkuang
					if (cursor_jiyu.getString(cursor_jiyu.getColumnIndex("pinyu"))!=null 
							&& !"".equals(cursor_jiyu.getString(cursor_jiyu.getColumnIndex("pinyu")))
							){
						map.put("pinyuqinkuang",  "已填写");
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
	 * 以下是自定义的BaseAdapter类
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
		 * 获取列数
		 */
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		/**
		 * 获取某一位置的数据
		 */
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		/**
		 * 获取唯一标识
		 */
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		/**
		 * android绘制每一列的时候，都会调用这个方法
		 */
		ZuJian_chakan zuJian = null;
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			if (convertView == null) {
				zuJian = new ZuJian_chakan();
				// 获取组件布局
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
						zuJian.pinyuqinkuang = (TextView) convertView.findViewById(R.id.pinyuqinkuang);   //这里替代成小组意见
					}
				
				}//end else
				
				
				
				
				zuJian.button_modify = (Button) convertView.findViewById(R.id.button_modify);				
				
				
					if (position == 0 ){
						zuJian.button_modify.setVisibility(View.INVISIBLE);
						System.out.println( "隐藏：position = " + String.valueOf(position));
					}else {
						zuJian.button_modify.setVisibility(View.VISIBLE);
						System.out.println( "显示：position = " + String.valueOf(position));
					}
					// 这里要注意，是使用的tag来存储数据的。
					convertView.setTag(zuJian);				
			} else {
				zuJian = (ZuJian_chakan) convertView.getTag();
				
				if (position == 0 ){
					zuJian.button_modify.setVisibility(View.INVISIBLE);
					System.out.println( "隐藏：position = " + String.valueOf(position));
				}else {
					zuJian.button_modify.setVisibility(View.VISIBLE);
					System.out.println( "显示：position = " + String.valueOf(position));
				}
			}
			// 绑定数据、以及事件触发			
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
					if ("推荐".equals((String) data.get(position).get("xiaozuyijian"))){
						zuJian.pinyuqinkuang.setTextColor(0xff000000);//黑色
					}else if("不推荐".equals((String) data.get(position).get("xiaozuyijian"))){
						zuJian.pinyuqinkuang.setTextColor(0x88AA0000);//红色
					}else{
						zuJian.pinyuqinkuang.setTextColor(0xff000000);//黑色
					}
				}
			
			zuJian.zongfen.setText((String) data.get(position).get("pinfen"));
			
			if ("无".equals((String) data.get(position).get("pogejieguo"))){
				zuJian.pogejieguo.setTextColor(0x880000AA);//蓝色
				zuJian.pogejieguo.setText("");
			}else if("同意".equals((String) data.get(position).get("pogejieguo"))){
				zuJian.pogejieguo.setTextColor(0xff000000);//黑色
				zuJian.pogejieguo.setText((String) data.get(position).get("pogejieguo"));
			}else if("不同意".equals((String) data.get(position).get("pogejieguo"))){
				zuJian.pogejieguo.setTextColor(0x88AA0000);//红色
				zuJian.pogejieguo.setText((String) data.get(position).get("pogejieguo"));
			}else if ("".equals((String) data.get(position).get("pogejieguo"))){
				zuJian.pogejieguo.setTextColor(0xff000000);//黑色
				zuJian.pogejieguo.setText("");
			}else{
				zuJian.pogejieguo.setText((String) data.get(position).get("pogejieguo"));
			}			
			//zuJian.pogejieguo.setTextColor(0xff000000);//黑色
			
			
			zuJian.ceshijieguo.setText((String) data.get(position).get("ceshijieguo"));
			if ("合格".equals((String) data.get(position).get("ceshijieguo"))){
				zuJian.ceshijieguo.setTextColor(0xff000000);//黑色
			}else if("不合格".equals((String) data.get(position).get("ceshijieguo"))){
				zuJian.ceshijieguo.setTextColor(0x88AA0000);//红色
			}else{
				zuJian.ceshijieguo.setTextColor(0xff000000);//黑色
			}

			if ("赞成".equals((String) data.get(position).get("toupiaoqinkuang"))){
				zuJian.toupiaoqinkuang.setTextColor(0x8800AA00);//绿色
			}else if("反对".equals((String) data.get(position).get("toupiaoqinkuang"))){
				zuJian.toupiaoqinkuang.setTextColor(0x88AA0000);//红色
			}else if("弃权".equals((String) data.get(position).get("toupiaoqinkuang"))){
				zuJian.toupiaoqinkuang.setTextColor(0x88AA0000);//红色
			}else{
				zuJian.toupiaoqinkuang.setTextColor(0xff000000);//黑色
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
		if ("pinfen".equals(leixin)){//评分阶段读数据库
			appState.getDB();
			cursor = appState.getAll();
			if (cursor.getCount() >= appState.people_total) {//判断是否全部评分完成
			//if(true){ //测试，调试  直接发
				cursor.close();
				// 弹框 提示是否提交评分
				new AlertDialog.Builder(this)
						.setTitle("是否提交评分数据？")
						.setMessage("所有参评人员已经评分完成。\n点击“确定”按钮提交各项数据到评审服务器，\n点击“返回”按钮可以继续修改。\n注意：选择确定之后，不能修改参评人员的各项数据！")
						.setNegativeButton("返回",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										//this.s = "Negative";
									}
								})
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										//s = "Positive";
										//提交评分，提交成功一个更新一个数据库
										//cursor.close();
										//appState.dbClose();
										appState.getDB();
										/*
										for (cur = 0 ; cur<appState.people_total; cur++){
											// 提交数据 
											String tmp = submitPinfen();
											 if ("接收成功".equals(tmp)){ // 状态改为已提交评分 // 提交状态（未提交/提交评分/提交投票）0 1 2  3
												 //appState.dbClose();
												 //appState.getDB();
												 appState.Update_tijiao(appState.peopleList.get(cur).get("id").toString(), "1");// 提交评分
												
												Toast toast = Toast.makeText(getApplicationContext(),
														appState.peopleList.get(cur).get("id").toString() + "提交成功！", 
														Toast.LENGTH_SHORT);
												toast.setGravity(Gravity.CENTER, 0,0);
												toast.cancel();
												toast.show();
												
											} else if ("接收失败".equals(tmp)) {
												Toast toast = Toast.makeText(getApplicationContext(),
														appState.peopleList.get(cur).get("id").toString() + "服务器接收失败，请重新提交！",
														Toast.LENGTH_SHORT);
												toast.setGravity(Gravity.CENTER, 0, 0);
												toast.show();
											}
										}
										*/
										
										//提交评分，一次性提交后，更新全部数据库
										//以下准备数据
										/* 太慢了
										String dataTransform = "pwhid=" + URLEncoder.encode(appState.pwhid) //评委会
												+ "&pwid=" + URLEncoder.encode(appState.pinweiName)	// 评委
												+ "&data=[";
										*/
										
										StringBuilder dataTransformb = new StringBuilder();
										dataTransformb.append( "pwhid=" + URLEncoder.encode(appState.pwhid) //评委会
												+ "&pwid=" + URLEncoder.encode(appState.pinweiName)	// 评委
												+ "&data=[");
										
										for (cur=0; cur<appState.people_total; cur++){
											cursor = appState.queryTable(appState.peopleList.get(cur).get("id").toString());
											cursor.moveToNext();

											//合成提交参数,调试时暂时屏蔽
											
											dataTransformb.append( "{\"id\":\"" + URLEncoder.encode(appState.peopleList.get(cur).get("id").toString()) + "\","// 参评人
													+ "\"poge\":\"" + URLEncoder.encode(cursor.getString(3)) + "\","// 破格
													+ "\"content\":\"" + URLEncoder.encode(cursor.getString(2)) + "\","  //破格意见
													+ "\"total\":\"" + URLEncoder.encode(cursor.getString(1)) + "\""  //总分
													+ "},");
											
											
											/*
											//调试  测试 用
											dataTransformb.append("{\"id\":\"" + URLEncoder.encode(appState.peopleList.get(cur).get("id").toString()) + "\","// 参评人
													+ "\"poge\":\"" + URLEncoder.encode("") + "\","// 破格
													+ "\"content\":\"" + URLEncoder.encode("") + "\","  //破格意见
													+ "\"total\":\"" + URLEncoder.encode("100") + "\""  //总分
													+ "},");
											*/									
											
											cursor.close();	
										}//end for
										dataTransformb.deleteCharAt(dataTransformb.length() - 1);//去掉最后一个逗号
										dataTransformb .append( "]");
										
										//以下 提交数据 
										String tmp = submitPinfen(dataTransformb.toString());
										 if ("接收成功".equals(tmp)){ // 状态改为已提交评分 // 提交状态（未提交/提交评分/提交投票）0 1 2  3
											// appState.dbClose();
											// appState.getDB();
											for (cur = 0; cur < appState.people_total; cur++) {
												appState.Update_tijiao(appState.peopleList.get(cur).get("id").toString(), "1");// 提交评分
											}

												Toast toast = Toast.makeText(getApplicationContext(),
														"提交成功！", 
														Toast.LENGTH_SHORT);
												toast.setGravity(Gravity.CENTER, 0,0);
												toast.cancel();
												toast.show();
												
												System.out.println("评分提交成功\r\n");
												
												appState.closeMain = true;//提交成功才关闭主窗体
										} else if ("接收失败".equals(tmp)) {
											Toast toast = Toast.makeText(getApplicationContext(),
													 "服务器接收失败，请重新提交！",
													Toast.LENGTH_SHORT);
											toast.setGravity(Gravity.CENTER, 0, 0);
											toast.show();
											
											System.out.println("评分提交失败\r\n");
											//提交失败继续留在主窗体
										}
										
										
										
										appState.dbClose();
										cur = 0;//end for
										
										//appState.closeMain = true;//关闭主窗体
										finish();
									}
								}).show();			
			}else{
				new AlertDialog.Builder(this)
				.setTitle("参评人员评分未完成！")
				.setMessage("还有参评人员未评分，请对所有参评人员评分完成后再提交数据！")
				/*
				.setNegativeButton("返回",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//this.s = "Negative";
							}
						})
						*/
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								
							}
						}).show();			
			}

			cursor.close();
			appState.dbClose();
		}else if ("toupiao".equals(leixin)){//投票阶段读scoreList
			appState.getDB();
		 	//cursor = appState.queryTable_tijiaostate("2");
			cursor = appState.getToupiao();
			if (cursor.getCount() >= appState.people_total) {//判断是否全部评分完成
			//if (true){	//测试 调试  直接发
				cursor.close();
				// 弹框 提示是否提交评分
				new AlertDialog.Builder(this)
						.setTitle("是否提交投票数据？")
						.setMessage("所有参评人员已经投票完成。\n点击“确定”按钮提交投票结果到评审服务器，\n点击“返回”按钮可以继续修改。\n注意：选择确定之后，不能修改参评人员的投票信息！")
						.setNegativeButton("返回",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										//this.s = "Negative";
									}
								})
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										//s = "Positive";
										//cursor.close();
										//appState.dbClose();
										appState.getDB();
										/*
										//提交评分，提交成功一个更新一个数据库
										for (cur=0; cur<appState.people_total; cur++){
											// 提交数据 
											String tmp = submitToupiao();
											 if ("接收成功".equals(tmp)){ // 状态改为已提交评分 // 提交状态（未提交/提交评分/提交投票）0 1 2  3
												// appState.dbClose();
												// appState.getDB();
												 appState.Update_tijiao(appState.peopleList.get(cur).get("id").toString(), "3");// 提交投票


													Toast toast = Toast.makeText(getApplicationContext(),
															appState.peopleList.get(cur).get("id").toString() + "提交成功！", 
															Toast.LENGTH_SHORT);
													toast.setGravity(Gravity.CENTER, 0,0);
													toast.cancel();
													toast.show();
											} else if ("接收失败".equals(tmp)) {
												Toast toast = Toast.makeText(getApplicationContext(),
														appState.peopleList.get(cur).get("id").toString() + "服务器接收失败，请重新提交！",
														Toast.LENGTH_SHORT);
												toast.setGravity(Gravity.CENTER, 0, 0);
												toast.show();
											}
										}//end for
										*/
										
										//提交评分，一次性提交后，更新全部数据库
										//以下准备数据
										/*
										String dataTransform = "pwhid=" + URLEncoder.encode(appState.pwhid) //评委会
												+ "&pwid=" + URLEncoder.encode(appState.pinweiName)	// 评委
												+ "&data=[";
										*/
										
										StringBuilder dataTransformb = new StringBuilder();
										dataTransformb.append("pwhid=" + URLEncoder.encode(appState.pwhid) //评委会
												+ "&pwid=" + URLEncoder.encode(appState.pinweiName)	// 评委
												+ "&data=[");
										
										for (cur=0; cur<appState.people_total; cur++){
											cursor = appState.queryTable(appState.peopleList.get(cur).get("id").toString());
											cursor.moveToNext();

											//合成提交参数
											dataTransformb.append( "{\"id\":\"" + URLEncoder.encode(appState.peopleList.get(cur).get("id").toString()) + "\","// 参评人
													+ "\"vote\":\"" + URLEncoder.encode( cursor.getString(4)) + "\"" //投票
													+ "},");
																						
											cursor.close();	
										}//end for
										//dataTransform = dataTransform.substring(0, dataTransform.length() - 1);//去掉最后一个逗号
										//dataTransform += "]";
										dataTransformb.deleteCharAt(dataTransformb.length() - 1);//去掉最后一个逗号
										dataTransformb .append( "]");
										
										//以下 提交数据 
										String tmp = submitToupiao(dataTransformb.toString());
										 if ("接收成功".equals(tmp)){ // 状态改为已提交评分 // 提交状态（未提交/提交评分/提交投票）0 1 2  3
											// appState.dbClose();
											// appState.getDB();
											for (cur = 0; cur < appState.people_total; cur++) {
												appState.Update_tijiao(appState.peopleList.get(cur).get("id").toString(), "3");// 提交投票
											}

												Toast toast = Toast.makeText(getApplicationContext(),
														"提交成功！", 
														Toast.LENGTH_SHORT);
												toast.setGravity(Gravity.CENTER, 0,0);
												toast.cancel();
												toast.show();
												
												System.out.println("投票提交成功\r\n");
												
												appState.closeMain = true;//提交成功才关闭主窗体
												
												if (appState.xianchangfenzu){
													//现场分组
													appState.pinshenjieshu = true;
												}
												finish();
										} else if ("接收失败".equals(tmp)) {
											Toast toast = Toast.makeText(getApplicationContext(),
													 "服务器接收失败，请重新提交！",
													Toast.LENGTH_SHORT);
											toast.setGravity(Gravity.CENTER, 0, 0);
											toast.show();
											
											System.out.println("投票提交失败\r\n");
											//提交失败继续留在主窗体
										}
										
										appState.dbClose();
										cur = 0;
										//appState.closeMain = true;//关闭主窗体
										
										
									}
								}).show();			
			}else{
				new AlertDialog.Builder(this)
				.setTitle("参评人员投票未完成！")
				.setMessage("还有参评人员未投票，请对所有参评人员投票完成后再提交数据！")
				/*
				.setNegativeButton("返回",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//this.s = "Negative";
							}
						})
						*/
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								
							}
						}).show();			
			}
			cursor.close();
			appState.dbClose();
		}else if ("jiyu".equals(leixin)){//寄语
			appState.getDB();
		 	cursor = appState.getFailedAll();
			if (cursor.getCount() >= appState.people_total) {
				cursor.close();
				// 弹框 提示是否提交评分
				new AlertDialog.Builder(this)
						.setTitle("是否提交主任寄语？")
						.setMessage("所有未通过人员的主任寄语填写完成。\n点击“确定”按钮提交主任寄语到评审服务器，\n点击“返回”按钮可以继续修改。\n注意：选择确定之后，不能修改未通过人员的主任寄语！")
						.setNegativeButton("返回",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										//this.s = "Negative";
									}
								})
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										//s = "Positive";
										//cursor.close();
										//appState.dbClose();
										appState.getDB();
										/*
										//提交评分，提交成功一个更新一个数据库
										for (cur=0; cur<appState.people_total; cur++){
											// 提交数据 
											String tmp = submitJiyu();
											 if ("接收成功".equals(tmp)){ // 状态改为已提交评分 // 提交状态（未提交/提交评分/提交投票）0 1 2  3
												// appState.dbClose();
												// appState.getDB();
												 appState.Update_failedState(appState.failedList.get(cur).get("id_f").toString(), "1");// 提交寄语


													Toast toast = Toast.makeText(getApplicationContext(),
															appState.failedList.get(cur).get("id_f").toString() + "提交成功！", 
															Toast.LENGTH_SHORT);
													toast.setGravity(Gravity.CENTER, 0,0);
													toast.cancel();
													toast.show();
											} else if ("接收失败".equals(tmp)) {
												Toast toast = Toast.makeText(getApplicationContext(),
														appState.failedList.get(cur).get("id_f").toString() + "服务器接收失败，请重新提交！",
														Toast.LENGTH_SHORT);
												toast.setGravity(Gravity.CENTER, 0, 0);
												toast.show();
											}
										}//end for
										*/
										
										//提交评分，一次性提交后，更新全部数据库
										//以下准备数据
										StringBuilder dataTransformb = new StringBuilder();
										dataTransformb.append( "pwhid=" + URLEncoder.encode(appState.pwhid) //评委会
												+ "&pwid=" + URLEncoder.encode(appState.pinweiName)	// 评委
												+ "&data=[" );
										
										
										for (cur=0; cur<appState.people_total; cur++){
											cursor = appState.queryFailed(appState.failedList.get(cur).get("id_f").toString());
											cursor.moveToNext();

											//合成提交参数
											dataTransformb.append(  "{\"id\":\"" + URLEncoder.encode(appState.failedList.get(cur).get("id_f").toString()) + "\","// 参评人
													+ "\"name\":\"" + URLEncoder.encode(appState.failedList.get(cur).get("name_f").toString()) + "\","// 参评人姓名
													+ "\"jiyu\":\"" + URLEncoder.encode(cursor.getString(1)) + "\"" //寄语
													+ "},"  );
																						
											cursor.close();	
										}//end for
										dataTransformb.deleteCharAt(dataTransformb.length() -1 );//去掉最后一个逗号
										dataTransformb.append("]");
										
										//以下 提交数据 
										String tmp = submitJiyu(dataTransformb.toString());
										 if ("接收成功".equals(tmp)){ // 状态改为已提交评分 // 提交状态（未提交/提交评分/提交投票）0 1 2  3
											// appState.dbClose();
											// appState.getDB();
											for (cur = 0; cur < appState.people_total; cur++) {
												appState.Update_failedState(appState.failedList.get(cur).get("id_f").toString(),"1");// 提交寄语
											}

												Toast toast = Toast.makeText(getApplicationContext(),
														"提交成功！", 
														Toast.LENGTH_SHORT);
												toast.setGravity(Gravity.CENTER, 0,0);
												toast.cancel();
												toast.show();
												
												appState.closeMain = true;//提交成功才关闭主窗体
										} else if ("接收失败".equals(tmp)) {
											Toast toast = Toast.makeText(getApplicationContext(),
													 "服务器接收失败，请重新提交！",
													Toast.LENGTH_SHORT);
											toast.setGravity(Gravity.CENTER, 0, 0);
											toast.show();
											
											//提交失败继续留在主窗体
										}
										
										
										appState.dbClose();
										cur = 0;
										//appState.closeMain = true;//关闭主窗体
										
										finish();
									}
								}).show();			
			}else{
				new AlertDialog.Builder(this)
				.setTitle("参评人员投票未完成！")
				.setMessage("还有参评人员未投票，请对所有参评人员投票完成后再提交数据！")
				/*
				.setNegativeButton("返回",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//this.s = "Negative";
							}
						})
						*/
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
		 * (评委会）pwhid （评委id）pwid (参评人）id 10位id
		 * 
		 * （破格）poge yes no 二选一 (破格内容）content 字符串
		 * 
		 * （总分）total 0~100整型数
		 */

		String tmp = "接收失败";

		// 要访问的web servlet
		// 注意：IP和端口是本地的 需要换成你的IP和端口
		String servletUrl = appState.HttpHead + "/expert/score";
		// 将参数传给服务器
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
				// 使用HttpURLConnection打开连接
				HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
				urlConn.setConnectTimeout(appState.REQUEST_TIMEOUT);
				urlConn.setReadTimeout(appState.SO_TIMEOUT);
				// 因为要求使用Post方式提交数据，需要设置为true
				urlConn.setDoOutput(true);
				urlConn.setDoInput(true);
				// 设置以Post方式，注意此处的“POST”必须大写
				urlConn.setRequestMethod("POST");
				// Post 请求不能使用缓存
				urlConn.setUseCaches(false);
				urlConn.setInstanceFollowRedirects(true);
				// 配置本次连接的Content-Type，配置为application/x-www-form-urlencoded
				urlConn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				// 连接，从postUrl.openConnection()至此的配置必须在connect之前完成
				// 要注意的事connection.getOutputStream会隐含地进行connect。
				urlConn.connect();
				// DataOutputStream流上传数据
				DataOutputStream out = new DataOutputStream(
						urlConn.getOutputStream());
				// 将要上传的内容写入流中
				out.writeBytes(dataTransform);
				// 刷新，关闭
				out.flush();
				out.close();
				// 得到读取的数据
				InputStreamReader in = new InputStreamReader(
						urlConn.getInputStream());
				BufferedReader buffer = new BufferedReader(in);
				String str = null;
				while ((str = buffer.readLine()) != null) {
					resultData += str;
				}
				in.close();
				urlConn.disconnect();
				if ("接收成功".equals(resultData)) {
					tmp = "接收成功";
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
		 * (评委会）pwhid （评委id）pwid (参评人）id 10位id (投票）toupiao 同意 反对 弃权 三选1
		 */
		String tmp = "接收失败";

		// 要访问的web servlet
		// 注意：IP和端口是本地的 需要换成你的IP和端口
		String servletUrl = appState.HttpHead + "/expert/vote";
		// 将参数传给服务器
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
				// 使用HttpURLConnection打开连接
				HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
				urlConn.setConnectTimeout(appState.REQUEST_TIMEOUT);
				urlConn.setReadTimeout(appState.SO_TIMEOUT);
				// 因为要求使用Post方式提交数据，需要设置为true
				urlConn.setDoOutput(true);
				urlConn.setDoInput(true);
				// 设置以Post方式，注意此处的“POST”必须大写
				urlConn.setRequestMethod("POST");
				// Post 请求不能使用缓存
				urlConn.setUseCaches(false);
				urlConn.setInstanceFollowRedirects(true);
				// 配置本次连接的Content-Type，配置为application/x-www-form-urlencoded
				urlConn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				// 连接，从postUrl.openConnection()至此的配置必须在connect之前完成
				// 要注意的事connection.getOutputStream会隐含地进行connect。
				urlConn.connect();
				// DataOutputStream流上传数据
				DataOutputStream out = new DataOutputStream(
						urlConn.getOutputStream());
				// 将要上传的内容写入流中
				out.writeBytes(dataTrasform);
				// 刷新，关闭
				out.flush();
				out.close();
				// 得到读取的数据
				InputStreamReader in = new InputStreamReader(
						urlConn.getInputStream());
				BufferedReader buffer = new BufferedReader(in);
				String str = null;
				while ((str = buffer.readLine()) != null) {
					resultData += str;
				}
				in.close();
				urlConn.disconnect();
			if ("接收成功".equals(resultData)) {
					tmp = "接收成功";
					
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
		 * (评委会）pwhid （评委id）pwid (参评人）id 10位id (投票）toupiao 同意 反对 弃权 三选1
		 */
		String tmp = "接收失败";

		// 要访问的web servlet
		// 注意：IP和端口是本地的 需要换成你的IP和端口
		String servletUrl = appState.HttpHead + "/expert/comment";
		// 将参数传给服务器
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
				// 使用HttpURLConnection打开连接
				HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
				urlConn.setConnectTimeout(appState.REQUEST_TIMEOUT);
				urlConn.setReadTimeout(appState.SO_TIMEOUT);
				// 因为要求使用Post方式提交数据，需要设置为true
				urlConn.setDoOutput(true);
				urlConn.setDoInput(true);
				// 设置以Post方式，注意此处的“POST”必须大写
				urlConn.setRequestMethod("POST");
				// Post 请求不能使用缓存
				urlConn.setUseCaches(false);
				urlConn.setInstanceFollowRedirects(true);
				// 配置本次连接的Content-Type，配置为application/x-www-form-urlencoded
				urlConn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				// 连接，从postUrl.openConnection()至此的配置必须在connect之前完成
				// 要注意的事connection.getOutputStream会隐含地进行connect。
				urlConn.connect();
				// DataOutputStream流上传数据
				DataOutputStream out = new DataOutputStream(
						urlConn.getOutputStream());
				// 将要上传的内容写入流中
				out.writeBytes(dataTransform);
				// 刷新，关闭
				out.flush();
				out.close();
				// 得到读取的数据
				InputStreamReader in = new InputStreamReader(
						urlConn.getInputStream());
				BufferedReader buffer = new BufferedReader(in);
				String str = null;
				while ((str = buffer.readLine()) != null) {
					resultData += str;
				}
				in.close();
				urlConn.disconnect();
				if ("接收成功".equals(resultData)) {
					tmp = "接收成功";
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
