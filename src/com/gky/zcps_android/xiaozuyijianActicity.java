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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class xiaozuyijianActicity extends Activity{
	private Global_var appState;
	private HashMap<String, Object> map = new HashMap<String, Object>();
	public String xiaozufenArray []  ,xiaozuyijianArray [], lianghuaArray[];

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置成横屏
		appState = ((Global_var) getApplicationContext()); // 获得全局变量	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xiaozuyijian);
		
		 xiaozufenArray  = new String [appState.people_total];
		 xiaozuyijianArray  = new String [appState.people_total];
		 lianghuaArray  = new String [appState.people_total];
		 
		 for (int i=0 ;i<appState.people_total; i++){
			 xiaozufenArray[i] = "";
			 xiaozuyijianArray[i] = "推荐";
		 }
				 
		updateUI();
	}
	
	
	private Cursor cursor = null;
	@Override
	public void onStart () {
		super.onStart();
		appState.getDB();
		
		for (int i = 0; i< appState.people_total; i++) {
			cursor = appState.queryTable(appState.peopleList.get(i).get("id").toString() );
			if (cursor != null && cursor.getCount() != 0) {
				cursor.moveToNext();
				if ( !"".equals(cursor.getString(9)) && cursor.getString(9) != null ) {	//9小组评分
					xiaozufenArray [i] = cursor.getString(9);
				}
				if ( !"".equals(cursor.getString(10)) && cursor.getString(10) !=null ) {	//10小组意见
					xiaozuyijianArray [i] = cursor.getString(10);
				}
			}		
			cursor.close();
			
			lianghuaArray[i] = appState.peopleList.get(i).get("lianghua").toString();
		}
		
	}
	
	@Override
	public void onStop () {
		super.onStop();
		
		for (int i = 0; i< appState.people_total; i++) {
			cursor = appState.queryTable(appState.peopleList.get(i).get("id").toString() );
			if (cursor != null && cursor.getCount() != 0) { //如果数据库有这个人，更新
				appState.Update_people(appState.peopleList.get(i).get("id").toString(), xiaozufenArray [i], xiaozuyijianArray [i]);
			}else {	//如果数据库没有这个人 添加
				appState.add(appState.peopleList.get(i).get("id").toString(), xiaozufenArray [i], xiaozuyijianArray [i]);
			}
			cursor.close();
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
			if (lianghuaArray[position].equals("量化")) {	//量化评分才弹出修改小组分数页面
				popXiaozufen(position);
			}
			
			
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
			//map.put("xiaozupinfen", xiaozufenArray[i]);
			//map.put("opinion", "通过");
			
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
 			zuJian.pinwei1.setText((String) data.get(position).get("pinwei"));
 			
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
 			
 			if ("推荐".equals((String) data.get(position).get("opinion"))){
 				zuJian.xiaozuyijian1.setSelection(0);
 				//zuJian.xiaozuyijian1.setBackgroundColor(0xff000000);//黑色
 			}else if ("不推荐".equals((String) data.get(position).get("opinion"))){
 				zuJian.xiaozuyijian1.setSelection(1);
 				//zuJian.xiaozuyijian1.setBackgroundColor(0x88AA0000);//红色
 			}else{
 				zuJian.xiaozuyijian1.setSelection(0);
 				//zuJian.xiaozuyijian1.setBackgroundColor(0xff000000);//黑色
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
							
							HashMap<String, Object> m = new HashMap<String, Object>();
							m = lst.get(position);
							m.remove("opinion");
							m.put("opinion", str);
							
							lst.remove(position);
							lst.add(position, m);
							
							xiaozuyijianArray [position] = str;
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
 	// 弹框 提示是否提交评分
		new AlertDialog.Builder(this)
				.setTitle("是否提交小组意见？")
				.setMessage("点击“确定”按钮提交投票结果到评审服务器，\n点击“返回”按钮可以继续修改。\n注意：选择确定之后，不能修改参评人员的信息！")
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

						// 合成提交参数,调试时暂时屏蔽
						for (int cur = 0; cur < appState.people_total; cur++) {
							dataTransformb.append("{\"id\":\""
									+ URLEncoder.encode(appState.peopleList.get(cur).get("id").toString()) + "\","// 参评人
									+ "\"opinion\":\"" + URLEncoder.encode(xiaozuyijianArray[cur]) + "\","// 小组意见
									+ "\"total\":\"" + URLEncoder.encode(appState.scoreList.get(cur).get("pinjunfen").toString()) + "\"," // 个人分
									+ "\"group_score\":\"" + URLEncoder.encode(xiaozufenArray[cur]) + "\"" // 小组分
									+ "},");
						}
						dataTransformb.deleteCharAt(dataTransformb.length() - 1);// 去掉最后一个逗号
						dataTransformb.append("]");

						String tmp = submitxiaozuyijian(dataTransformb.toString());
						if ("接收成功".equals(tmp)){ 
								Toast toast = Toast.makeText(getApplicationContext(),"提交成功！",  Toast.LENGTH_SHORT);
								toast.setGravity(Gravity.CENTER, 0,0);
								toast.cancel();
								toast.show();
								
								System.out.println("小组意见提交成功\r\n");								
								appState.closeMain = true;//提交成功才关闭主窗体
								finish();
						} else if ("接收失败".equals(tmp)) {
							Toast toast = Toast.makeText(getApplicationContext(), "服务器接收失败，请重新提交！", Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							
							System.out.println("小组意见提交失败\r\n");
							//提交失败继续留在主窗体
						}

						
					}
				}).show();
		
 	}
 	
 	private String submitxiaozuyijian(String dataTrasform) {
 		String tmp = "接收失败";
 		
 	// 要访问的web servlet
 				// 注意：IP和端口是本地的 需要换成你的IP和端口
 				String servletUrl = appState.HttpHead + "/expert/opinion";
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

     
}
