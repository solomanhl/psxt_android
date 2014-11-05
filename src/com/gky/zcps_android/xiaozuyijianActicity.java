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
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class xiaozuyijianActicity extends Activity{
	private Global_var appState;
	private HashMap<String, Object> map = new HashMap<String, Object>();
	public String xiaozufenArray []  ,xiaozuyijianArray [],  lianghuaArray[],toupiaoArray [];
	public Button listView_xiaozuyijian_submit;

	public Thread updateworkfloatT;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置成横屏
		appState = ((Global_var) getApplicationContext()); // 获得全局变量	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xiaozuyijian);
		
		setTitle(appState.pwhname + " （" +appState.peopleList.get(0).get("shenbaojibie").toString() + "）");
		
		listView_xiaozuyijian_submit = (Button) findViewById(R.id.listView_xiaozuyijian_submit);
		
		 xiaozufenArray  = new String [appState.people_total];
		 xiaozuyijianArray  = new String [appState.people_total];
		 toupiaoArray  = new String [appState.people_total];
		 lianghuaArray  = new String [appState.people_total];
		 
		 for (int i=0 ;i<appState.people_total; i++){
			 xiaozufenArray[i] = "";
			 xiaozuyijianArray[i] = "";
			 toupiaoArray[i] = "";
		 }
				 
//		updateUI();
		
		// 得到当前线程的Looper实例，由于当前线程是UI线程也可以通过Looper.getMainLooper()得到
		Looper looper = Looper.myLooper();
		// 此处甚至可以不需要设置Looper，因为 Handler默认就使用当前线程的Looper
		messageHandler = new MessageHandler(looper);
		
		if ("xiaozuyijian".equals(appState.workfloat)){
			updateworkfloatT = new updateWorkfloatThread();
			updateworkfloatT.start();
		}
		
	}
	
	
	private Cursor cursor = null;
	@Override
	public void onStart () {
		super.onStart();
		appState.getDB();
		
		for (int i = 0; i< appState.people_total; i++) {
			if ("xiaozuyijian".equals(appState.workfloat)){
				cursor = appState.queryTable(appState.peopleList.get(i).get("id").toString() );
				if (cursor != null && cursor.getCount() != 0) {
					cursor.moveToNext();
					if ( !"".equals(cursor.getString(4)) && cursor.getString(9) != null ) {	//4投票
						toupiaoArray [i] = cursor.getString(4);
					}
					if ( !"".equals(cursor.getString(9)) && cursor.getString(9) != null ) {	//9小组评分
						xiaozufenArray [i] = cursor.getString(9);
					}
//					if ( !"".equals(cursor.getString(10)) && cursor.getString(10) !=null ) {	//10小组意见
//						//xiaozuyijianArray [i] = cursor.getString(10);						
//					}					
				}		
				cursor.close();
				
				xiaozuyijianArray [i] = appState.scoreList.get(i).get("opinion").toString();
			}else if ("toupiao".equals(appState.workfloat)){
				//默认按小组意见显示投票
//				xiaozuyijianArray [i] = appState.scoreList.get(i).get("opinion").toString();
//				if ("推荐".equals(xiaozuyijianArray[i] )){
//					toupiaoArray[i] = "赞成";
//	 			}else if ("不推荐".equals(xiaozuyijianArray[i] )){
//	 				toupiaoArray[i] = "反对";
//	 			}else{
//	 				toupiaoArray[i] = "赞成";
//	 			}
				toupiaoArray[i] = "";
			}
			
			lianghuaArray[i] = appState.peopleList.get(i).get("lianghua").toString();
		}
		updateUI();
	}
	
	@Override
	public void onStop () {
		super.onStop();
		
		try {
			updateworkfloatT.sleep(1);
			if (updateworkfloatT != null){
				updateworkfloatT.interrupt();
			}			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if (!appState.closeMain){
			for (int i = 0; i< appState.people_total; i++) {
				cursor = appState.queryTable(appState.peopleList.get(i).get("id").toString() );
				if (cursor != null && cursor.getCount() != 0) { //如果数据库有这个人，更新
					appState.Update_people(appState.peopleList.get(i).get("id").toString(), xiaozufenArray [i], xiaozuyijianArray [i], toupiaoArray[i]);
				}else {	//如果数据库没有这个人 添加
					appState.add(appState.peopleList.get(i).get("id").toString(), xiaozufenArray [i], xiaozuyijianArray [i], toupiaoArray[i]);
				}
				cursor.close();
			}			
			
		}
		appState.dbClose();
	}
	
	
	private ArrayList<HashMap<String, Object>> lst ;
	// 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
	private MyListAdapter saImageItems;	
    private ListView listView_cart;
    
	private void updateUI() {
		// TODO Auto-generated method stub
		lst = new ArrayList<HashMap<String, Object>>();
		saImageItems = new MyListAdapter(this, lst);// 没什么解释	
		listView_cart = (ListView) findViewById(R.id.listView_xiaozuyijian);
			
//		map.put("xuhao", "序号" );
//		map.put("bianhao", "材料袋号" );
//		map.put("xinmin", "姓名" );
//		map.put("danwei", "单位" );
//		map.put("pinwei", "主审评委" );
//		map.put("pinfen", "分数" );
//		map.put("xiaozupinfen", "小组评分" );
//		map.put("pogejieguo", "破格结果");
//		map.put("xiaozuyijian", "小组意见");
//
//		lst.add(map);

		listAllnew();		
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
 		
 	// 点击控件监听器
 		listView_cart.setOnItemClickListener(new ItemClickListener());
    }
     
	class ItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0,// The AdapterView where the click happened
				View arg1,// The view within the AdapterView that was clicked
				int position,// The position of the view in the adapter
				long id// The row id of the item that was clicked
		) {
			//暂时屏蔽，现在任何时候都不弹出小组分
//			if (lianghuaArray[position].equals("量化")) {	//量化评分才弹出修改小组分数页面
//				popXiaozufen(position);
//			}
			
			
//			HashMap<String, Object> m = new HashMap<String, Object>();
//			m = lst.get(position);
//			m.remove("xiaozupinfen");
//			m.put("xiaozupinfen",String.valueOf(position) );
//
//			lst.remove(position);
//			lst.add(position, m);
//			
//			xiaozufenArray [position]  = String.valueOf(position);
//			
//			saImageItems.notifyDataSetChanged();
		}
	}
     
	private int index = -1;
	//弹出小组分窗口
	private void popXiaozufen(int position) {		
		// TODO Auto-generated method stub
		index = position;
		Intent intent = new Intent();
		intent.setClass(xiaozuyijianActicity.this, xiaozufen_Activity.class);
		
		Bundle bundle = new Bundle();
		bundle.putString("info","材料袋号：" + lst.get(position).get("bianhao") + "\n姓名：" + lst.get(position).get("xinmin") + "\n评分：" + lst.get(position).get("pinfen"));
		intent.putExtras(bundle);

		startActivityForResult(intent, 1);// 需要下一个Activity返回数据,在onActivityResult()中接收			
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != -1 && resultCode !=0) {
			HashMap<String, Object> m = new HashMap<String, Object>();
			m = lst.get(index);
			m.remove("xiaozupinfen");
			m.put("xiaozupinfen", String.valueOf(resultCode));

			lst.remove(index);
			lst.add(index, m);

			xiaozufenArray[index] = String.valueOf(resultCode);

			saImageItems.notifyDataSetChanged();
		}
	}
     
     private void listAllnew() {
    	 
		for (int i = 0; i < appState.people_total; i++) {
			map = new HashMap<String, Object>();
			map.put("bianhao", appState.peopleList.get(i).get("id").toString());
			map.put("xinmin", appState.peopleList.get(i).get("name").toString());
			map.put("danwei", appState.peopleList.get(i).get("company").toString());
			map.put("pinwei",  appState.peopleList.get(i).get("expert_name").toString());
			map.put("pinfen", appState.scoreList.get(i).get("pinjunfen").toString());
			
			map.put("lianghua", appState.peopleList.get(i).get("lianghua").toString());
			map.put("gerenyijian", appState.peopleList.get(i).get("gerenyijian").toString());
			map.put("expert_name", appState.peopleList.get(i).get("expert_name").toString()); //主审评委
			//map.put("xiaozupinfen", xiaozufenArray[i]);
			map.put("opinion", appState.scoreList.get(i).get("opinion").toString() );
			map.put("toupiao", toupiaoArray[i] );
			
			if ("".equals( appState.scoreList.get(i).get("pogejielun").toString())){
				map.put("pogejieguo", "无" );
			}else if ("同意".equals(appState.scoreList.get(i).get("pogejielun").toString())){
				map.put("pogejieguo", "同意" );
			}else if ("不同意".equals(appState.scoreList.get(i).get("pogejielun").toString())){
				map.put("pogejieguo", "不同意" );
			}

			if ("".equals(appState.peopleList.get(i).get("ceshijieguo").toString())){
				map.put("ceshi", appState.peopleList.get(i).get("ceshijieguo").toString());
	    	}else if ("F".equals(appState.peopleList.get(i).get("ceshijieguo").toString())){
	    		map.put("ceshi", "不合格");
	    	}else{
	    		map.put("ceshi", "合格");
	    	}
			
			//map.put("xiaozuyijian", "小组意见");
			lst.add(map);	
		}
		saImageItems.notifyDataSetChanged();
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
 		Zujian_xiaozuyijian zuJian = null;
 		
 		private String[] adapterData1, adapterData2; 
 		private ArrayAdapter<String> adapter1, adapter2;
 		
 		@Override
 		public View getView(final int position, View convertView, ViewGroup parent) {
 			// TODO Auto-generated method stub
 			
 			if (convertView == null) {
 				zuJian = new Zujian_xiaozuyijian();
 				// 获取组件布局
 				convertView = layoutInflater.inflate(R.layout.listview_xiaozuyijian, null);

 				zuJian.list_xiaozuyijian = (RelativeLayout) convertView.findViewById(R.id.list_xiaozuyijian);
 				
 				zuJian.xuhao1 = (TextView) convertView.findViewById(R.id.xuhao1);
 				zuJian.bianhao1 = (TextView) convertView.findViewById(R.id.bianhao1);
				zuJian.xinmin1 = (TextView) convertView.findViewById(R.id.xinmin1);
				zuJian.danwei1 = (TextView) convertView.findViewById(R.id.danwei1);
				zuJian.pinwei1 = (TextView) convertView.findViewById(R.id.pinwei1);
				zuJian.fenshu1 = (TextView) convertView.findViewById(R.id.fenshu1);
				zuJian.xiaozufen1 = (TextView) convertView.findViewById(R.id.xiaozufen1);
				zuJian.poge1 = (TextView) convertView.findViewById(R.id.poge1);
				zuJian.ceshi1 = (TextView) convertView.findViewById(R.id.ceshi1);
				zuJian.xiaozuyijian1 = (Spinner) convertView.findViewById(R.id.xiaozuyijian1);	
				zuJian.toupiao1 = (Spinner) convertView.findViewById(R.id.toupiao1);
				
				
				adapterData1 = new String[] { "推荐", "不推荐", ""}; 
				adapter1 = new ArrayAdapter<String>(xiaozuyijianActicity.this, R.layout.myspinner, adapterData1);  
				adapter1.setDropDownViewResource(R.layout.myspinner);  
		        zuJian.xiaozuyijian1.setAdapter(adapter1);  
		        
		        adapterData2 = new String[] { "赞成", "反对", "弃权", ""}; 
				adapter2 = new ArrayAdapter<String>(xiaozuyijianActicity.this, R.layout.myspinner, adapterData2);  
				adapter2.setDropDownViewResource(R.layout.myspinner);  
		        zuJian.toupiao1.setAdapter(adapter2);  	

 				
// 					if (position == 0 ){
// 						zuJian.xuhao1.setVisibility(View.INVISIBLE);
// 						zuJian.xiaozuyijian1.setVisibility(View.INVISIBLE);
// 						System.out.println( "隐藏：position = " + String.valueOf(position));
// 						zuJian.xiaozufen1.setEnabled(false);
// 						zuJian.xiaozufen1.setText("小组评分");
// 					}else {
 						//zuJian.xuhao1.setVisibility(View.VISIBLE);
 						//zuJian.xiaozuyijian1.setVisibility(View.VISIBLE);
 						System.out.println( "显示：position = " + String.valueOf(position));
 						//zuJian.xiaozufen1.setEnabled(true);
 						//zuJian.xiaozufen1.setText(xiaozufenArray[position - 1]);
// 					}
 					// 这里要注意，是使用的tag来存储数据的。
 					convertView.setTag(zuJian);				
 			} else {
 				zuJian = (Zujian_xiaozuyijian) convertView.getTag();
 				
// 				if (position == 0 ){
// 					zuJian.xuhao1.setVisibility(View.INVISIBLE);
// 					zuJian.xiaozuyijian1.setVisibility(View.INVISIBLE);
// 					System.out.println( "隐藏：position = " + String.valueOf(position));
// 					zuJian.xiaozufen1.setEnabled(false);
// 					zuJian.xiaozufen1.setText("小组评分");
// 				}else {
 					//zuJian.xuhao1.setVisibility(View.VISIBLE);
 					//zuJian.xiaozuyijian1.setVisibility(View.VISIBLE);
 					System.out.println( "显示：position = " + String.valueOf(position));
 					//zuJian.xiaozufen1.setEnabled(true);
 					//zuJian.xiaozufen1.setText(xiaozufenArray[position - 1]);
// 				}
 			}
 			
 			// 绑定数据、以及事件触发		
 			zuJian.xuhao1.setText(String.valueOf(position + 1));
 			zuJian.bianhao1.setText((String) data.get(position).get("bianhao"));
 			zuJian.xinmin1.setText((String) data.get(position).get("xinmin"));			
 			zuJian.danwei1.setText((String) data.get(position).get("danwei"));
 			zuJian.pinwei1.setText((String) data.get(position).get("expert_name"));
 			
 			if (data.get(position).get("lianghua").equals("量化")) {	//量化评分
 				zuJian.fenshu1.setText((String) data.get(position).get("pinfen"));
 			} else {	//不量化评分
 				if ( data.get(position).get("gerenyijian").toString().equals("yes")) {
 					zuJian.fenshu1.setText("推荐");
 				} else if ( data.get(position).get("gerenyijian").toString().equals("no")) {
 					zuJian.fenshu1.setText("不推荐");
 				}
 			}
 			
 			
 			if ("无".equals((String) data.get(position).get("pogejieguo"))){
 				zuJian.poge1.setTextColor(0x880000AA);//蓝色
 				zuJian.poge1.setText("");
 			}else if("同意".equals((String) data.get(position).get("pogejieguo"))){
 				zuJian.poge1.setTextColor(0xff000000);//黑色
 				zuJian.poge1.setText((String) data.get(position).get("pogejieguo"));
 			}else if("不同意".equals((String) data.get(position).get("pogejieguo"))){
 				zuJian.poge1.setTextColor(0x88AA0000);//红色
 				zuJian.poge1.setText((String) data.get(position).get("pogejieguo"));
 			}else if ("".equals((String) data.get(position).get("pogejieguo"))){
 				zuJian.poge1.setTextColor(0xff000000);//黑色
 				zuJian.poge1.setText("");
 			}else{
 				zuJian.poge1.setText((String) data.get(position).get("pogejieguo"));
 			} 			
 			
 			//zuJian.xiaozuyijian1.setAdapter(adapter);
 			//默认设成通过
 			//zuJian.xiaozuyijian1.setPromptId(1);
 			
 			zuJian.xiaozufen1.setText( xiaozufenArray[position]); 			
 			
 			
 			if ("toupiao".equals(appState.workfloat)){
 				zuJian.toupiao1.setEnabled(true);
// 				listView_xiaozuyijian_submit.setVisibility(View.VISIBLE);
 			}else{
 				zuJian.toupiao1.setEnabled(false);
 				zuJian.toupiao1.setVisibility(View.GONE);
 			}
 			
 			if ("推荐".equals((String) data.get(position).get("opinion") )){
 				zuJian.xiaozuyijian1.setSelection(0);
// 				zuJian.toupiao1.setSelection(0);
 			}else if ("不推荐".equals((String) data.get(position).get("opinion") )){
 				zuJian.xiaozuyijian1.setSelection(1);
// 				zuJian.toupiao1.setSelection(1);
 			}else{
 				zuJian.xiaozuyijian1.setSelection(2);
// 				zuJian.toupiao1.setSelection(0);
 			}
 			
 			//默认跟小组意见关联
 			if ("赞成".equals((String) data.get(position).get("toupiao"))){
 				zuJian.toupiao1.setSelection(0);
 				//zuJian.xiaozuyijian1.setBackgroundColor(0xff000000);//黑色
 			}else if ("反对".equals((String) data.get(position).get("toupiao"))){
 				zuJian.toupiao1.setSelection(1);
 				//zuJian.xiaozuyijian1.setBackgroundColor(0x88AA0000);//红色
 			}else if ("弃权".equals((String) data.get(position).get("toupiao"))){
 				zuJian.toupiao1.setSelection(2);
 				//zuJian.xiaozuyijian1.setBackgroundColor(0x88AA0000);//红色
 			}else if ("".equals((String) data.get(position).get("toupiao"))){
 				zuJian.toupiao1.setSelection(3);
 				//zuJian.xiaozuyijian1.setBackgroundColor(0x88AA0000);//红色
 			}

 			
 			if ("xiaozuyijian".equals(appState.workfloat)){
 				zuJian.xiaozuyijian1.setEnabled(true);
// 				listView_xiaozuyijian_submit.setVisibility(View.GONE);
 			}else{
 				zuJian.xiaozuyijian1.setEnabled(false);
 			}
 			
 			
 			zuJian.ceshi1.setText((String) data.get(position).get("ceshi"));
 			if("合格".equals((String) data.get(position).get("ceshi"))){
 				zuJian.ceshi1.setTextColor(0xff000000);//黑色
 				zuJian.ceshi1.setText((String) data.get(position).get("ceshi"));
 			}else if("不合格".equals((String) data.get(position).get("ceshi"))){
 				zuJian.ceshi1.setTextColor(0x88AA0000);//红色
 				zuJian.ceshi1.setText((String) data.get(position).get("ceshi"));
 			}else if ("".equals((String) data.get(position).get("ceshi"))){
 				zuJian.ceshi1.setTextColor(0xff000000);//黑色
 				zuJian.ceshi1.setText("");
 			}else{
 				zuJian.ceshi1.setText((String) data.get(position).get("ceshi"));
 			} 			
 			
 			
			zuJian.xiaozuyijian1.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {
							// TODO Auto-generated method stub
							String str = parent.getItemAtPosition(position1).toString();
							//Toast.makeText(xiaozuyijianActicity.this, "你点击的是:" + str, 2000).show();
							//注意：这里的position1是spinner选项item的position 从0开始		
							
							if ( "xiaozuyijian".equals(appState.workfloat) //，是小组意见状态
									&& ( appState.pinweiName.equals(appState.peopleList.get(position).get("expert_name").toString())  //主审评委是自己||是空
											|| "".equals(appState.peopleList.get(position).get("expert_name").toString())  )  
								){
								String pwhid = appState.pwhid;// 评委会
								String pwid = appState.pinweiName;// 评委
								
								HashMap<String, Object> m = new HashMap<String, Object>();
								m = lst.get(position);
								m.remove("opinion");		
								m.remove("expert_name");
								
								if (!"".equals(str) ){ //不为空，写自己评委名字
									m.put("opinion", str);
									m.put("expert_name", appState.pinweiName);
								}else{//为空，评委名字清空
									m.put("opinion", "");
									m.put("expert_name", "");
								}
								
								
								
								lst.remove(position);
								lst.add(position, m);			
								
								saImageItems.notifyDataSetChanged();

								
								//以下提交单个人的小组意见
								
							}		
							
							
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							// TODO Auto-generated method stub

						}

					});
					
			zuJian.toupiao1.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {
					// TODO Auto-generated method stub
					String str = parent.getItemAtPosition(position1).toString();
					//Toast.makeText(xiaozuyijianActicity.this, "你点击的是:" + str, 2000).show();
					//注意：这里的position1是spinner选项item的position 从0开始
					
					
					HashMap<String, Object> m = new HashMap<String, Object>();
					m = lst.get(position);
					m.remove("toupiao");
					m.put("toupiao", str);
					
					lst.remove(position);
					lst.add(position, m);
					
					toupiaoArray [position] = str;

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub

				}

			});
			
			//edittext很麻烦，先加一个触摸监听
//			zuJian.xiaozufen1.setOnTouchListener(new OnTouchListener() {
//				@Override
//				public boolean onTouch(View v, MotionEvent event) {
//					// TODO Auto-generated method stub
//					if(event.getAction() == MotionEvent.ACTION_DOWN) {
//                        index= position;
//                }
//					return false;
//				}
//
//        });
			
			
//				zuJian.xiaozufen1.addTextChangedListener(new TextWatcher() {
//					public void onTextChanged (CharSequence s, int start, int before, int count) {
//						// TODO Auto-generated method stub
//						Log.i("TAG", s.toString());
						//这段加了黑屏
//						if (position != 0 &&  "".equals(zuJian.xiaozufen1.getText().toString()) ){
//							if (Integer.valueOf(zuJian.xiaozufen1.getText().toString() ) > 100) {
//								zuJian.xiaozufen1.setText("100");
//							}else if (Integer.valueOf(zuJian.xiaozufen1.getText().toString() ) < 0) {
//								zuJian.xiaozufen1.setText("0");
//							}
//						}


						//if(index!= -1 && index == position) {
							//if (!("".equals(s.toString()))) {
//								HashMap<String, Object> m = new HashMap<String, Object>();
//								m = lst.get(position);
//								m.remove("xiaozupinfen");
//								m.put("xiaozupinfen",s.toString() );
//
//								lst.remove(position);
//								lst.add(position, m);
//								
//								xiaozufenArray [position]  = s.toString();
							//}
						//}
//					}

//					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//						// TODO Auto-generated method stub
//					}
//
//					public void afterTextChanged(Editable s) {
//						// TODO Auto-generated method stub
//						
//				}
//													
//				});
			
			
 			return convertView;
 		}

 	}
 	
 	
 	
 	public void listView_xiaozuyijian_submit_onclick(View target){
 		//发查重请求
 		String pwhid = appState.pwhid;// 评委会
		String pwid = appState.pinweiName;// 评委
		boolean sendcheck = false;
		boolean sendtoupiao = false;

		StringBuilder dataTransformb = new StringBuilder();
		dataTransformb.append("pwhid=" + URLEncoder.encode(pwhid) // 评委会
				+ "&pwid=" + URLEncoder.encode(pwid) // 评委
				+ "&data=");

		if ("xiaozuyijian".equals(appState.workfloat)){
			String s, n;
			sendcheck = false;

			HashMap<String, Object> m = new HashMap<String, Object>();		
				// 合成提交参数,调试时暂时屏蔽
				for (int cur = 0; cur < appState.people_total; cur++) {
					m = lst.get(cur);
					s = m.get("opinion").toString();
					n = m.get("expert_name").toString();
						xiaozuyijianArray[cur] = s;
						
						if (s != null && !"".equalsIgnoreCase(s) && pwid.equals(n) ){
							dataTransformb.append(URLEncoder.encode(appState.peopleList.get(cur).get("id").toString()) + ",");
							sendcheck = true;
						}
						
				}	
				dataTransformb.deleteCharAt(dataTransformb.length() - 1);// 去掉最后一个逗号
				
				if (sendcheck){//请求查重
					String tmp = submitcheck(dataTransformb.toString());
					if ("没有重复".equals(tmp)){ 
							System.out.println("没有重复");
							popTijiaoWindow();
					} else  {
						//弹框显示已经被提交到服务器的人
						new AlertDialog.Builder(this)
	 					.setTitle("下列参评人员已经被其他评委填写小组意见，返回修改或继续提交？")
	 					.setMessage(tmp)
	 					.setNegativeButton("返回修改",
	 							new DialogInterface.OnClickListener() {
	 								@Override
	 								public void onClick(DialogInterface dialog, int which) {
	 									//this.s = "Negative";
	 						}
	 					})
	 					.setPositiveButton("继续提交", new DialogInterface.OnClickListener() {
	 						public void onClick(DialogInterface dialog, int whichButton) {
	 							String pwhid = appState.pwhid;// 评委会
	 							String pwid = appState.pinweiName;// 评委

	 							StringBuilder dataTransformb = new StringBuilder();
	 							dataTransformb.append("pwhid=" + URLEncoder.encode(pwhid) // 评委会
	 									+ "&pwid=" + URLEncoder.encode(pwid) // 评委
	 									+ "&data=[");

	 							if ("xiaozuyijian".equals(appState.workfloat)){
	 								// 合成提交参数,调试时暂时屏蔽
//	 								for (int cur = 0; cur < appState.people_total; cur++) {
//	 									dataTransformb.append("{\"id\":\""
//	 											+ URLEncoder.encode(appState.peopleList.get(cur).get("id").toString()) + "\","// 参评人
//	 											+ "\"opinion\":\"" + URLEncoder.encode(xiaozuyijianArray[cur]) + "\","// 小组意见
//	 											+ "\"total\":\"" + URLEncoder.encode(appState.scoreList.get(cur).get("pinjunfen").toString()) + "\"," // 个人分
//	 											+ "\"group_score\":\"" + URLEncoder.encode(xiaozufenArray[cur]) + "\"" // 小组分
//	 											+ "},");
//	 								}
	 								String s, n;

	 								HashMap<String, Object> m = new HashMap<String, Object>();		
	 									// 合成提交参数,调试时暂时屏蔽
	 									for (int cur = 0; cur < appState.people_total; cur++) {
	 										m = lst.get(cur);
	 										s = m.get("opinion").toString();
	 										n = m.get("expert_name").toString();
	 											xiaozuyijianArray[cur] = s;
	 											
	 											if (s != null && !"".equalsIgnoreCase(s) && pwid.equals(n) ){
	 												dataTransformb.append("{\"id\":\""
	 														+ URLEncoder.encode(appState.peopleList.get(cur).get("id").toString()) + "\","// 参评人
	 														+ "\"opinion\":\"" + URLEncoder.encode(xiaozuyijianArray[cur]) + "\","// 小组意见
	 														//+ "\"total\":\"" + URLEncoder.encode(appState.scoreList.get(cur).get("pinjunfen").toString()) + "\"," // 个人分
	 														+ "\"total\":\"" + URLEncoder.encode("-1") + "\"," // 个人分
	 														+ "\"group_score\":\"" + URLEncoder.encode(xiaozufenArray[cur]) + "\"" // 小组分
	 														+ "},");
	 											}
	 											
	 									}							
	 							}else if("toupiao".equals(appState.workfloat)){
	 								// 合成提交参数,调试时暂时屏蔽
	 								for (int cur = 0; cur < appState.people_total; cur++) {
	 									//合成提交参数
	 									dataTransformb.append( "{\"id\":\"" + URLEncoder.encode(appState.peopleList.get(cur).get("id").toString()) + "\","// 参评人
	 											+ "\"vote\":\"" + URLEncoder.encode( toupiaoArray[cur]) + "\"" //投票
	 											+ "},");
	 								}
	 							}
	 							
	 							dataTransformb.deleteCharAt(dataTransformb.length() - 1);// 去掉最后一个逗号
	 							dataTransformb.append("]");

	 							String tmp = submitxiaozuyijian(dataTransformb.toString());
	 							if ("接收成功".equals(tmp)){ 
	 									try {
	 										updateworkfloatT.sleep(1);
	 										if (updateworkfloatT != null){
	 											updateworkfloatT.interrupt();
	 										}
	 									} catch (InterruptedException e) {
	 										// TODO Auto-generated catch block
	 										e.printStackTrace();
	 									}
	 									
	 									Toast toast = Toast.makeText(getApplicationContext(),"提交成功！",  Toast.LENGTH_SHORT);
	 									toast.setGravity(Gravity.CENTER, 0,0);
	 									toast.cancel();
	 									toast.show();
	 									
	 									System.out.println("小组意见/投票提交成功\r\n");					
	 									appState.closeMain = true;//提交成功才关闭主窗体
	 									xiaozuyijianActicity.this.finish();; //返回到入口界面
	 							} else if ("接收失败".equals(tmp)) {
	 								Toast toast = Toast.makeText(getApplicationContext(), "服务器接收失败，请重新提交！", Toast.LENGTH_SHORT);
	 								toast.setGravity(Gravity.CENTER, 0, 0);
	 								toast.show();
	 								
	 								System.out.println("小组意见/投票提交失败\r\n");
	 								//提交失败继续留在主窗体
	 							}

	 							
	 						}
	 					}).show();
					}
				}
		}else if ( "toupiao".equals(appState.workfloat)){
			sendtoupiao = true;;
			for (int cur = 0; cur < appState.people_total; cur++) {
					if ("".equals(toupiaoArray[cur]) || toupiaoArray[cur] == null){
						sendtoupiao = false;
						break;
					}
				}
			
			if (sendtoupiao){
				popTijiaoWindow();
			}else {
				new AlertDialog.Builder(this)
					.setTitle("提示")
					.setMessage("您的电脑中还有参评人员未投票，请全部投票后再提交数据。")
					.setNegativeButton("返回修改",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									//this.s = "Negative";
						}
					}).show();
			}
			
		}
 		
		
		
 	
		
 	}
 	
 	private void popTijiaoWindow(){
 	// 弹框 提示是否提交评分
 			new AlertDialog.Builder(this)
 					.setTitle("是否提交？")
 					.setMessage("点击“确定”按钮提交数据到评审服务器，\n点击“返回”按钮可以继续修改。\n注意：选择确定之后，不能修改参评人员的信息！")
 					.setNegativeButton("返回",
 							new DialogInterface.OnClickListener() {
 								@Override
 								public void onClick(DialogInterface dialog, int which) {
 									//this.s = "Negative";
 						}
 					})
 					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
 						public void onClick(DialogInterface dialog, int whichButton) {
 							String pwhid = appState.pwhid;// 评委会
 							String pwid = appState.pinweiName;// 评委

 							StringBuilder dataTransformb = new StringBuilder();
 							dataTransformb.append("pwhid=" + URLEncoder.encode(pwhid) // 评委会
 									+ "&pwid=" + URLEncoder.encode(pwid) // 评委
 									+ "&data=[");

 							if ("xiaozuyijian".equals(appState.workfloat)){
 								// 合成提交参数,调试时暂时屏蔽
// 								for (int cur = 0; cur < appState.people_total; cur++) {
// 									dataTransformb.append("{\"id\":\""
// 											+ URLEncoder.encode(appState.peopleList.get(cur).get("id").toString()) + "\","// 参评人
// 											+ "\"opinion\":\"" + URLEncoder.encode(xiaozuyijianArray[cur]) + "\","// 小组意见
// 											+ "\"total\":\"" + URLEncoder.encode(appState.scoreList.get(cur).get("pinjunfen").toString()) + "\"," // 个人分
// 											+ "\"group_score\":\"" + URLEncoder.encode(xiaozufenArray[cur]) + "\"" // 小组分
// 											+ "},");
// 								}
 								String s, n;

 								HashMap<String, Object> m = new HashMap<String, Object>();		
 									// 合成提交参数,调试时暂时屏蔽
 									for (int cur = 0; cur < appState.people_total; cur++) {
 										m = lst.get(cur);
 										s = m.get("opinion").toString();
 										n = m.get("expert_name").toString();
 											xiaozuyijianArray[cur] = s;
 											
 											if (s != null && !"".equalsIgnoreCase(s) && pwid.equals(n) ){
 												dataTransformb.append("{\"id\":\""
 														+ URLEncoder.encode(appState.peopleList.get(cur).get("id").toString()) + "\","// 参评人
 														+ "\"opinion\":\"" + URLEncoder.encode(xiaozuyijianArray[cur]) + "\","// 小组意见
 														//+ "\"total\":\"" + URLEncoder.encode(appState.scoreList.get(cur).get("pinjunfen").toString()) + "\"," // 个人分
 														+ "\"total\":\"" + URLEncoder.encode("-1") + "\"," // 个人分
 														+ "\"group_score\":\"" + URLEncoder.encode(xiaozufenArray[cur]) + "\"" // 小组分
 														+ "},");
 											}
 											
 									}							
 							}else if("toupiao".equals(appState.workfloat)){
 								// 合成提交参数,调试时暂时屏蔽
 								for (int cur = 0; cur < appState.people_total; cur++) {
 									//合成提交参数
 									dataTransformb.append( "{\"id\":\"" + URLEncoder.encode(appState.peopleList.get(cur).get("id").toString()) + "\","// 参评人
 											+ "\"vote\":\"" + URLEncoder.encode( toupiaoArray[cur]) + "\"" //投票
 											+ "},");
 								}
 							}
 							
 							dataTransformb.deleteCharAt(dataTransformb.length() - 1);// 去掉最后一个逗号
 							dataTransformb.append("]");

 							String tmp = submitxiaozuyijian(dataTransformb.toString());
 							if ("接收成功".equals(tmp)){ 
 									Toast toast = Toast.makeText(getApplicationContext(),"提交成功！",  Toast.LENGTH_SHORT);
 									toast.setGravity(Gravity.CENTER, 0,0);
 									toast.cancel();
 									toast.show();
 									
 									System.out.println("小组意见/投票提交成功\r\n");								

 									try {
 										updateworkfloatT.sleep(1);
 										if (updateworkfloatT != null){
 											updateworkfloatT.interrupt();
 										}
 									} catch (InterruptedException e) {
 										// TODO Auto-generated catch block
 										e.printStackTrace();
 									}
 									
 									appState.closeMain = true;//提交成功才关闭主窗体
 									xiaozuyijianActicity.this.finish();; //返回到入口界面
 							} else if ("接收失败".equals(tmp)) {
 								Toast toast = Toast.makeText(getApplicationContext(), "服务器接收失败，请重新提交！", Toast.LENGTH_SHORT);
 								toast.setGravity(Gravity.CENTER, 0, 0);
 								toast.show();
 								
 								System.out.println("小组意见/投票提交失败\r\n");
 								//提交失败继续留在主窗体
 							}

 							
 						}
 					}).show();
 	}
 	
 	private String submitxiaozuyijian(String dataTrasform) {
 		String tmp = "接收失败";
 		
 	// 要访问的web servlet
 				// 注意：IP和端口是本地的 需要换成你的IP和端口
 				String servletUrl = "";
 				if ("xiaozuyijian".equals(appState.workfloat)){
 					servletUrl = appState.HttpHead + "/expert/opinion";
 				}else if ("toupiao".equals(appState.workfloat)){
 					servletUrl = appState.HttpHead + "/expert/vote";
 				}
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
 						out.writeBytes(dataTrasform.toString());
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
 	
 	private String submitcheck(String dataTrasform) {
 		
 	// 要访问的web servlet
 				// 注意：IP和端口是本地的 需要换成你的IP和端口
 				String servletUrl = "";

 				servletUrl = appState.HttpHead + "/expert/check";

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
 						out.writeBytes(dataTrasform.toString());
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
 						
 					} catch (IOException e) {
 						// TODO Auto-generated catch block
 						e.printStackTrace();
 					}
 				}// if(url!=null)
 				else {

 				}
 				return resultData;
 	}

 	public void getWokfloat(){

		// 要访问的web servlet
		// 注意：IP和端口是本地的 需要换成你的IP和端口
		String servletUrl = appState.HttpHead + "/expert/complete?pwid=" + URLEncoder.encode(appState.pinweiName);
		
		// 完整的请求路径
		String requestUrl = servletUrl;
		System.out.println("url===" + requestUrl);
		try {
			URL url = new URL(requestUrl);
			try {
				// 获得连接
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setConnectTimeout(appState.REQUEST_TIMEOUT);
				conn.setReadTimeout(appState.SO_TIMEOUT);
				BufferedReader in = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));
				String line = in.readLine();
				String tmp = line;
				while (line != null) {
//					Toast toast = Toast.makeText(getApplicationContext(), line, Toast.LENGTH_LONG);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();

					line = in.readLine();
				}
				in.close();
				conn.disconnect();
				
				
				//downloadfinish = true;  //强制置true
				if ("正在评分".equals(tmp)){
					appState.workfloat = "pinfen";
				}else if ("正在投票".equals(tmp)){
					if ("xiaozuyijian".equals(appState.workfloat)){
						try {
							updateworkfloatT.sleep(1);
							if (updateworkfloatT != null){
								updateworkfloatT.interrupt();
							}
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						appState.workfloat = "toupiao";
						appState.closeMain = true;//提交成功才关闭主窗体
						xiaozuyijianActicity.this.finish();; //返回到入口界面
					}
					
					if (appState.pinshenjieshu) {
						
					}
				}else if ("正在写意见".equals(tmp)){
					appState.workfloat = "xiaozuyijian";

				}else if ("投票结束".equals(tmp)) {

				}else if("评审结束".equals(tmp)){

				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
				Log.i("info", e.toString());
				Toast toast = Toast.makeText(getApplicationContext(),
						"请求超时或出现错误", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		} catch (MalformedURLException e) {

			e.printStackTrace();
		}
}
     
 	
 	private Handler messageHandler;
 	private int cnt = 0;

	private void updateHandler(Object obj) {
		// 创建一个Message对象，并把得到的网络信息赋值给Message对象
		Message message = Message.obtain();// 第一步
		message = Message.obtain();// 第一步
		message.obj = obj; // 第二步
		messageHandler.sendMessage(message);// 第三步
	}

	// 子类化一个Handler
	class MessageHandler extends Handler {
		public MessageHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			if (!((String) msg.obj == null)) {
				if ("send".equals((String) msg.obj))	{
//					submitXiaozuyijian();
				}else if ("request".equals((String) msg.obj))	{
					getWokfloat();
				}				
			}
		}
	}
	
	// 更新状态k进程----------------------------------------
	public class updateWorkfloatThread extends Thread {

		public updateWorkfloatThread() {

		}

		
		@Override
		public void run() {
			while ( !this.isInterrupted()) {
				System.out.println("xiaozuyijianThread run again");
				cnt++;
				if (cnt == 300) {
					//updateHandler("send");
					updateHandler("request");
				}
//					else if (cnt == 600){
//					updateHandler("request");
//				}
				else	if (cnt > 300) {
					cnt = 0;
				}

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}
	
	
	
	public void submitXiaozuyijian(){
		boolean tijiao = false;
		String s;
		String pwhid = appState.pwhid;// 评委会
		String pwid = appState.pinweiName;// 评委
		StringBuilder dataTransformb = new StringBuilder();
		dataTransformb.append("pwhid=" + URLEncoder.encode(pwhid) // 评委会
				+ "&pwid=" + URLEncoder.encode(pwid) // 评委
				+ "&data=[");

		HashMap<String, Object> m = new HashMap<String, Object>();		
			// 合成提交参数,调试时暂时屏蔽
			for (int cur = 0; cur < appState.people_total; cur++) {
				m = lst.get(cur);
				s = m.get("opinion").toString();
//				if ( !s.equals(xiaozuyijianArray[cur]) ){
//					tijiao = true;
					xiaozuyijianArray[cur] = s;
					
					dataTransformb.append("{\"id\":\""
							+ URLEncoder.encode(appState.peopleList.get(cur).get("id").toString()) + "\","// 参评人
							+ "\"opinion\":\"" + URLEncoder.encode(xiaozuyijianArray[cur]) + "\","// 小组意见
							//+ "\"total\":\"" + URLEncoder.encode(appState.scoreList.get(cur).get("pinjunfen").toString()) + "\"," // 个人分
							+ "\"total\":\"" + URLEncoder.encode("-1") + "\"," // 个人分
							+ "\"group_score\":\"" + URLEncoder.encode(xiaozufenArray[cur]) + "\"" // 小组分
							+ "},");
//				}				
			}

		
		dataTransformb.deleteCharAt(dataTransformb.length() - 1);// 去掉最后一个逗号
		dataTransformb.append("]");

//		if (tijiao){
			String tmp = submitxiaozuyijian(dataTransformb.toString());
			if ("接收成功".equals(tmp)){ 
					Toast toast = Toast.makeText(getApplicationContext(),"提交成功！",  Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0,0);
					toast.cancel();
					toast.show();
					
					System.out.println("小组意见/投票提交成功\r\n");								
//					appState.closeMain = true;//提交成功才关闭主窗体
//					finish();
					//提交成功检测一次状态
					getWokfloat();
			} else if ("接收失败".equals(tmp)) {
				Toast toast = Toast.makeText(getApplicationContext(), "服务器接收失败，请重新提交！", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				
				System.out.println("小组意见/投票提交失败\r\n");
				//提交失败小组意见框变空
//				zuJian.xiaozuyijian1.setSelection(2);
			}
//		}
	}
	
	
	
}
