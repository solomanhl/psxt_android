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
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//���óɺ���
		appState = ((Global_var) getApplicationContext()); // ���ȫ�ֱ���	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		
		editText1 = (EditText)  findViewById(R.id.editText1);
		button1 = (Button) findViewById(R.id.button1);
		
		// �õ���ǰ�̵߳�Looperʵ�������ڵ�ǰ�߳���UI�߳�Ҳ����ͨ��Looper.getMainLooper()�õ�
				Looper looper = Looper.myLooper();
				// �˴��������Բ���Ҫ����Looper����Ϊ HandlerĬ�Ͼ�ʹ�õ�ǰ�̵߳�Looper
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
	
	// ����״̬k����----------------------------------------
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
		// ����һ��Message���󣬲��ѵõ���������Ϣ��ֵ��Message����
		Message message = Message.obtain();// ��һ��
		message = Message.obtain();// ��һ��
		message.obj = obj; // �ڶ���
		messageHandler.sendMessage(message);// ������
	}
	
	// ���໯һ��Handler
		class MessageHandler extends Handler {
			public MessageHandler(Looper looper) {
				super(looper);
			}

			@Override
			public void handleMessage(Message msg) {
				// ����UI
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

		// Ҫ���ʵ�web servlet
		// ע�⣺IP�Ͷ˿��Ǳ��ص� ��Ҫ�������IP�Ͷ˿�
		String servletUrl = appState.HttpHead + "/expert/complete";
		// ����������·��
		String requestUrl = servletUrl;
		System.out.println("url===" + requestUrl);
		try {
			URL url = new URL(requestUrl);
			try {
				// �������
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
				
				

				if ( !"��������".equals(tmp)){
					finish();
				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
				Log.i("info", e.toString());
				Toast toast = Toast.makeText(getApplicationContext(),
						"����ʱ����ִ���", Toast.LENGTH_LONG);
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
		
		
		// ���ر������˵�һ����
		file.deleteFile(appState.SDpath
				+ "psxt/conference/applicant_one.xml");
		int f = appState.dl_file("conference/", "applicant_one.xml",
				"?code=" + URLEncoder.encode(editText1.getText().toString())
						+ "&type=" + type  //0���ֽ׶Σ�2С������׶Σ�1ͶƱ�׶�
						+ "&pwid=" + URLEncoder.encode(appState.pinweiName));       
		
		// -1���� 0���� 1�Ѿ�����
		if (f == 0) {

		} else if (f == -1) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"�Ҳ���������Ա", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
		
		// ����һ����
		appState.decodeXML(appState.SDpath
				+ "psxt/conference/applicant_one.xml");
		
		
		if (appState.peopleList != null && appState.peopleList.size()>0){
		//����������
		Intent it = new Intent(searchActivity.this, MainActivity.class);

		Bundle bundle = new Bundle();
		bundle.putString("jiyu", "��ͨ");
		it.putExtras(bundle);

		startActivity(it);
		this.finish();
		}else{
			Toast toast = Toast.makeText(getApplicationContext(),
					"�Ҳ���������Ա", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
		
	}
}
