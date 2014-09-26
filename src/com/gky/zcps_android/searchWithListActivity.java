package com.gky.zcps_android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.gky.zcps_android.EntranceActivity.MessageHandler;
import com.gky.zcps_android.EntranceActivity.updateWorkfloatThread;
import com.soloman.file.FileUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class searchWithListActivity extends Activity{
	private Global_var appState;
	public GridView gridView1;
	private FileUtils file = new FileUtils();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置成横屏
		appState = ((Global_var) getApplicationContext()); // 获得全局变量	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchwithlist);
		
		gridView1 = (GridView) findViewById(R.id.gridView1);
		
		// 得到当前线程的Looper实例，由于当前线程是UI线程也可以通过Looper.getMainLooper()得到
				Looper looper = Looper.myLooper();
				// 此处甚至可以不需要设置Looper，因为 Handler默认就使用当前线程的Looper
				messageHandler = new MessageHandler(looper);
				
				searchList();
	}
	
	
	
	public void searchList( ) {

		//List<List<String>> setArray = new ArrayList<List<String>>();
		List<String> tempArray01 = new ArrayList<String>();
		for (int i = 0; i < appState.people_total; i++) {
			tempArray01.add(String.valueOf(i));
		}
			

		//dataArray.add(tempArray01);
		updateGridView(tempArray01);
	}
	
	private void updateGridView(List<String> dataArray) {
		// TODO Auto-generated method stub
		//生成动态数组，并且转入数据  
	      ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();  
		for (int i = 0; i < dataArray.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemCailiaodai", appState.peopleList.get(i).get("id").toString() );// 添加材料袋号
			map.put("ItemName", appState.peopleList.get(i).get("name").toString() );// 姓名
			lstImageItem.add(map);
		} 
	      //生成适配器的ImageItem <====> 动态数组的元素，两者一一对应  
	      SimpleAdapter saImageItems = new SimpleAdapter(this, //没什么解释  
	                                                lstImageItem,//数据来源   
	                                                R.layout.search_grid_head,//night_item的XML实现  
	                                                  
	                                                //动态数组与ImageItem对应的子项          
	                                                new String[] {"ItemCailiaodai","ItemName"},   
	                                                  
	                                                //ImageItem的XML文件里面的一个ImageView,两个TextView ID  
	                                                new int[] {R.id.id_gridHead,R.id.name_gridHead});  
	      //添加并且显示  
	      gridView1.setAdapter(saImageItems);  
	      //添加消息处理  
	      gridView1.setOnItemClickListener(new ItemClickListener());  
	}
	
	// 当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件
	class ItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0,// The AdapterView where the
													// click happened
				View arg1,// The view within the AdapterView that was clicked
				int arg2,// The position of the view in the adapter
				long arg3// The row id of the item that was clicked
		) {
			// 在本例中arg2=arg3
			HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
			// 显示所选Item的ItemText
			String id = (String) item.get("ItemCailiaodai");
			String name = (String) item.get("ItemName");
			
			search(id);
		}

	}
	
	
	
	
	
	public Thread updateworkfloatT;
	public boolean runThread;
	@Override
	public void onResume() {
		super.onResume();

		runThread = true;
		updateworkfloatT = new updateWorkfloatThread();
		updateworkfloatT.start();

	}
	
	@Override
	public void onPause() {
		super.onPause();
			if (appState.xianchangfenzu) {
				runThread = false;
			}
	}
	
	// 更新状态k进程----------------------------------------
	public class updateWorkfloatThread extends Thread {

		public updateWorkfloatThread() {

		}

		@Override
		public void run() {
			while (runThread) {
				System.out.println("updateWorkfloatThread run again");
				updateHandler("selectWorkfoat");
			try {
				Thread.sleep(15*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			}
			
		}

	}
	
	
	private Handler messageHandler;

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
				// 更新UI
				if (!((String) msg.obj == null)) {
					 if ("selectWorkfoat".equals((String) msg.obj)) {
						if (appState.xianchangfenzu) {
							getWokfloat();
						}
					} 
				}

			}
		}
	
	public void getWokfloat(){

		// 要访问的web servlet
		// 注意：IP和端口是本地的 需要换成你的IP和端口
		String servletUrl = appState.HttpHead + "/expert/complete";
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
				
				

				if ( !"正在评分".equals(tmp)){
					finish();
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
	
	
	
	public void search (String id){
			
		for (int j = 0; j < appState.people_total; j++) {
				if (id.equals(appState.peopleList.get(j).get("id").toString())){
					appState.people_cur = j;
					break;
				}
			
		}

		//启动主界面
		Intent it = new Intent(searchWithListActivity.this, MainActivity.class);

		Bundle bundle = new Bundle();
		bundle.putString("jiyu", "普通");
		it.putExtras(bundle);

		startActivity(it);
		this.finish();
		
		
	}
}
