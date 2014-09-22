package com.gky.zcps_android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class searchActivity extends Activity{
	private Global_var appState;
	private EditText editText1;
	private Button button1;
	private FileUtils file = new FileUtils();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置成横屏
		appState = ((Global_var) getApplicationContext()); // 获得全局变量	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		
		editText1 = (EditText)  findViewById(R.id.editText1);
		button1 = (Button) findViewById(R.id.button1);
		
		// 得到当前线程的Looper实例，由于当前线程是UI线程也可以通过Looper.getMainLooper()得到
				Looper looper = Looper.myLooper();
				// 此处甚至可以不需要设置Looper，因为 Handler默认就使用当前线程的Looper
				messageHandler = new MessageHandler(looper);
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
	
	
	
	public void button1_onclick (View v){
		String type  = "0";
		appState.tab5_state = "pinfen";
		if ("pinfen".endsWith(appState.workfloat)  ){
			type="0";
			appState.tab5_state = "pinfen";
		}else if ("toupiao".endsWith(appState.workfloat)  ){
			type="1";
			appState.tab5_state = "toupiao";
		}else if ("xiaozuyijian".endsWith(appState.workfloat)  ){
			type="2";
			appState.tab5_state = "xiaozuyijian";
		}
		
		
		// 下载被搜索人的一览表
		file.deleteFile(appState.SDpath
				+ "psxt/conference/applicant_one.xml");
		int f = appState.dl_file("conference/", "applicant_one.xml",
				"?code=" + URLEncoder.encode(editText1.getText().toString())
						+ "&type=" + type  //0评分阶段，2小组意见阶段，1投票阶段
						+ "&pwid=" + URLEncoder.encode(appState.pinweiName));       
		
		// -1出错 0正常 1已经存在
		if (f == 0) {

		} else if (f == -1) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"找不到参评人员", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
		
		// 解析一览表
		appState.decodeXML(appState.SDpath
				+ "psxt/conference/applicant_one.xml");
		
		
		if (appState.peopleList != null && appState.peopleList.size()>0){
		//启动主界面
		Intent it = new Intent(searchActivity.this, MainActivity.class);

		Bundle bundle = new Bundle();
		bundle.putString("jiyu", "普通");
		it.putExtras(bundle);

		startActivity(it);
		this.finish();
		}else{
			Toast toast = Toast.makeText(getApplicationContext(),
					"找不到参评人员", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
		
	}
}
