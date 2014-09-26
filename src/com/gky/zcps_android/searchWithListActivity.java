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
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//���óɺ���
		appState = ((Global_var) getApplicationContext()); // ���ȫ�ֱ���	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchwithlist);
		
		gridView1 = (GridView) findViewById(R.id.gridView1);
		
		// �õ���ǰ�̵߳�Looperʵ�������ڵ�ǰ�߳���UI�߳�Ҳ����ͨ��Looper.getMainLooper()�õ�
				Looper looper = Looper.myLooper();
				// �˴��������Բ���Ҫ����Looper����Ϊ HandlerĬ�Ͼ�ʹ�õ�ǰ�̵߳�Looper
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
		//���ɶ�̬���飬����ת������  
	      ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();  
		for (int i = 0; i < dataArray.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemCailiaodai", appState.peopleList.get(i).get("id").toString() );// ��Ӳ��ϴ���
			map.put("ItemName", appState.peopleList.get(i).get("name").toString() );// ����
			lstImageItem.add(map);
		} 
	      //������������ImageItem <====> ��̬�����Ԫ�أ�����һһ��Ӧ  
	      SimpleAdapter saImageItems = new SimpleAdapter(this, //ûʲô����  
	                                                lstImageItem,//������Դ   
	                                                R.layout.search_grid_head,//night_item��XMLʵ��  
	                                                  
	                                                //��̬������ImageItem��Ӧ������          
	                                                new String[] {"ItemCailiaodai","ItemName"},   
	                                                  
	                                                //ImageItem��XML�ļ������һ��ImageView,����TextView ID  
	                                                new int[] {R.id.id_gridHead,R.id.name_gridHead});  
	      //��Ӳ�����ʾ  
	      gridView1.setAdapter(saImageItems);  
	      //�����Ϣ����  
	      gridView1.setOnItemClickListener(new ItemClickListener());  
	}
	
	// ��AdapterView������(���������߼���)���򷵻ص�Item�����¼�
	class ItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0,// The AdapterView where the
													// click happened
				View arg1,// The view within the AdapterView that was clicked
				int arg2,// The position of the view in the adapter
				long arg3// The row id of the item that was clicked
		) {
			// �ڱ�����arg2=arg3
			HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
			// ��ʾ��ѡItem��ItemText
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
	
	
	
	public void search (String id){
			
		for (int j = 0; j < appState.people_total; j++) {
				if (id.equals(appState.peopleList.get(j).get("id").toString())){
					appState.people_cur = j;
					break;
				}
			
		}

		//����������
		Intent it = new Intent(searchWithListActivity.this, MainActivity.class);

		Bundle bundle = new Bundle();
		bundle.putString("jiyu", "��ͨ");
		it.putExtras(bundle);

		startActivity(it);
		this.finish();
		
		
	}
}
