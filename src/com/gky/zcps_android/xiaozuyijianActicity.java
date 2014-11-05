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
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//���óɺ���
		appState = ((Global_var) getApplicationContext()); // ���ȫ�ֱ���	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xiaozuyijian);
		
		setTitle(appState.pwhname + " ��" +appState.peopleList.get(0).get("shenbaojibie").toString() + "��");
		
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
		
		// �õ���ǰ�̵߳�Looperʵ�������ڵ�ǰ�߳���UI�߳�Ҳ����ͨ��Looper.getMainLooper()�õ�
		Looper looper = Looper.myLooper();
		// �˴��������Բ���Ҫ����Looper����Ϊ HandlerĬ�Ͼ�ʹ�õ�ǰ�̵߳�Looper
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
					if ( !"".equals(cursor.getString(4)) && cursor.getString(9) != null ) {	//4ͶƱ
						toupiaoArray [i] = cursor.getString(4);
					}
					if ( !"".equals(cursor.getString(9)) && cursor.getString(9) != null ) {	//9С������
						xiaozufenArray [i] = cursor.getString(9);
					}
//					if ( !"".equals(cursor.getString(10)) && cursor.getString(10) !=null ) {	//10С�����
//						//xiaozuyijianArray [i] = cursor.getString(10);						
//					}					
				}		
				cursor.close();
				
				xiaozuyijianArray [i] = appState.scoreList.get(i).get("opinion").toString();
			}else if ("toupiao".equals(appState.workfloat)){
				//Ĭ�ϰ�С�������ʾͶƱ
//				xiaozuyijianArray [i] = appState.scoreList.get(i).get("opinion").toString();
//				if ("�Ƽ�".equals(xiaozuyijianArray[i] )){
//					toupiaoArray[i] = "�޳�";
//	 			}else if ("���Ƽ�".equals(xiaozuyijianArray[i] )){
//	 				toupiaoArray[i] = "����";
//	 			}else{
//	 				toupiaoArray[i] = "�޳�";
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
				if (cursor != null && cursor.getCount() != 0) { //������ݿ�������ˣ�����
					appState.Update_people(appState.peopleList.get(i).get("id").toString(), xiaozufenArray [i], xiaozuyijianArray [i], toupiaoArray[i]);
				}else {	//������ݿ�û������� ���
					appState.add(appState.peopleList.get(i).get("id").toString(), xiaozufenArray [i], xiaozuyijianArray [i], toupiaoArray[i]);
				}
				cursor.close();
			}			
			
		}
		appState.dbClose();
	}
	
	
	private ArrayList<HashMap<String, Object>> lst ;
	// ������������ImageItem <====> ��̬�����Ԫ�أ�����һһ��Ӧ
	private MyListAdapter saImageItems;	
    private ListView listView_cart;
    
	private void updateUI() {
		// TODO Auto-generated method stub
		lst = new ArrayList<HashMap<String, Object>>();
		saImageItems = new MyListAdapter(this, lst);// ûʲô����	
		listView_cart = (ListView) findViewById(R.id.listView_xiaozuyijian);
			
//		map.put("xuhao", "���" );
//		map.put("bianhao", "���ϴ���" );
//		map.put("xinmin", "����" );
//		map.put("danwei", "��λ" );
//		map.put("pinwei", "������ί" );
//		map.put("pinfen", "����" );
//		map.put("xiaozupinfen", "С������" );
//		map.put("pogejieguo", "�Ƹ���");
//		map.put("xiaozuyijian", "С�����");
//
//		lst.add(map);

		listAllnew();		
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
 		
 	// ����ؼ�������
 		listView_cart.setOnItemClickListener(new ItemClickListener());
    }
     
	class ItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0,// The AdapterView where the click happened
				View arg1,// The view within the AdapterView that was clicked
				int position,// The position of the view in the adapter
				long id// The row id of the item that was clicked
		) {
			//��ʱ���Σ������κ�ʱ�򶼲�����С���
//			if (lianghuaArray[position].equals("����")) {	//�������ֲŵ����޸�С�����ҳ��
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
	//����С��ִ���
	private void popXiaozufen(int position) {		
		// TODO Auto-generated method stub
		index = position;
		Intent intent = new Intent();
		intent.setClass(xiaozuyijianActicity.this, xiaozufen_Activity.class);
		
		Bundle bundle = new Bundle();
		bundle.putString("info","���ϴ��ţ�" + lst.get(position).get("bianhao") + "\n������" + lst.get(position).get("xinmin") + "\n���֣�" + lst.get(position).get("pinfen"));
		intent.putExtras(bundle);

		startActivityForResult(intent, 1);// ��Ҫ��һ��Activity��������,��onActivityResult()�н���			
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
			map.put("expert_name", appState.peopleList.get(i).get("expert_name").toString()); //������ί
			//map.put("xiaozupinfen", xiaozufenArray[i]);
			map.put("opinion", appState.scoreList.get(i).get("opinion").toString() );
			map.put("toupiao", toupiaoArray[i] );
			
			if ("".equals( appState.scoreList.get(i).get("pogejielun").toString())){
				map.put("pogejieguo", "��" );
			}else if ("ͬ��".equals(appState.scoreList.get(i).get("pogejielun").toString())){
				map.put("pogejieguo", "ͬ��" );
			}else if ("��ͬ��".equals(appState.scoreList.get(i).get("pogejielun").toString())){
				map.put("pogejieguo", "��ͬ��" );
			}

			if ("".equals(appState.peopleList.get(i).get("ceshijieguo").toString())){
				map.put("ceshi", appState.peopleList.get(i).get("ceshijieguo").toString());
	    	}else if ("F".equals(appState.peopleList.get(i).get("ceshijieguo").toString())){
	    		map.put("ceshi", "���ϸ�");
	    	}else{
	    		map.put("ceshi", "�ϸ�");
	    	}
			
			//map.put("xiaozuyijian", "С�����");
			lst.add(map);	
		}
		saImageItems.notifyDataSetChanged();
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
 		Zujian_xiaozuyijian zuJian = null;
 		
 		private String[] adapterData1, adapterData2; 
 		private ArrayAdapter<String> adapter1, adapter2;
 		
 		@Override
 		public View getView(final int position, View convertView, ViewGroup parent) {
 			// TODO Auto-generated method stub
 			
 			if (convertView == null) {
 				zuJian = new Zujian_xiaozuyijian();
 				// ��ȡ�������
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
				
				
				adapterData1 = new String[] { "�Ƽ�", "���Ƽ�", ""}; 
				adapter1 = new ArrayAdapter<String>(xiaozuyijianActicity.this, R.layout.myspinner, adapterData1);  
				adapter1.setDropDownViewResource(R.layout.myspinner);  
		        zuJian.xiaozuyijian1.setAdapter(adapter1);  
		        
		        adapterData2 = new String[] { "�޳�", "����", "��Ȩ", ""}; 
				adapter2 = new ArrayAdapter<String>(xiaozuyijianActicity.this, R.layout.myspinner, adapterData2);  
				adapter2.setDropDownViewResource(R.layout.myspinner);  
		        zuJian.toupiao1.setAdapter(adapter2);  	

 				
// 					if (position == 0 ){
// 						zuJian.xuhao1.setVisibility(View.INVISIBLE);
// 						zuJian.xiaozuyijian1.setVisibility(View.INVISIBLE);
// 						System.out.println( "���أ�position = " + String.valueOf(position));
// 						zuJian.xiaozufen1.setEnabled(false);
// 						zuJian.xiaozufen1.setText("С������");
// 					}else {
 						//zuJian.xuhao1.setVisibility(View.VISIBLE);
 						//zuJian.xiaozuyijian1.setVisibility(View.VISIBLE);
 						System.out.println( "��ʾ��position = " + String.valueOf(position));
 						//zuJian.xiaozufen1.setEnabled(true);
 						//zuJian.xiaozufen1.setText(xiaozufenArray[position - 1]);
// 					}
 					// ����Ҫע�⣬��ʹ�õ�tag���洢���ݵġ�
 					convertView.setTag(zuJian);				
 			} else {
 				zuJian = (Zujian_xiaozuyijian) convertView.getTag();
 				
// 				if (position == 0 ){
// 					zuJian.xuhao1.setVisibility(View.INVISIBLE);
// 					zuJian.xiaozuyijian1.setVisibility(View.INVISIBLE);
// 					System.out.println( "���أ�position = " + String.valueOf(position));
// 					zuJian.xiaozufen1.setEnabled(false);
// 					zuJian.xiaozufen1.setText("С������");
// 				}else {
 					//zuJian.xuhao1.setVisibility(View.VISIBLE);
 					//zuJian.xiaozuyijian1.setVisibility(View.VISIBLE);
 					System.out.println( "��ʾ��position = " + String.valueOf(position));
 					//zuJian.xiaozufen1.setEnabled(true);
 					//zuJian.xiaozufen1.setText(xiaozufenArray[position - 1]);
// 				}
 			}
 			
 			// �����ݡ��Լ��¼�����		
 			zuJian.xuhao1.setText(String.valueOf(position + 1));
 			zuJian.bianhao1.setText((String) data.get(position).get("bianhao"));
 			zuJian.xinmin1.setText((String) data.get(position).get("xinmin"));			
 			zuJian.danwei1.setText((String) data.get(position).get("danwei"));
 			zuJian.pinwei1.setText((String) data.get(position).get("expert_name"));
 			
 			if (data.get(position).get("lianghua").equals("����")) {	//��������
 				zuJian.fenshu1.setText((String) data.get(position).get("pinfen"));
 			} else {	//����������
 				if ( data.get(position).get("gerenyijian").toString().equals("yes")) {
 					zuJian.fenshu1.setText("�Ƽ�");
 				} else if ( data.get(position).get("gerenyijian").toString().equals("no")) {
 					zuJian.fenshu1.setText("���Ƽ�");
 				}
 			}
 			
 			
 			if ("��".equals((String) data.get(position).get("pogejieguo"))){
 				zuJian.poge1.setTextColor(0x880000AA);//��ɫ
 				zuJian.poge1.setText("");
 			}else if("ͬ��".equals((String) data.get(position).get("pogejieguo"))){
 				zuJian.poge1.setTextColor(0xff000000);//��ɫ
 				zuJian.poge1.setText((String) data.get(position).get("pogejieguo"));
 			}else if("��ͬ��".equals((String) data.get(position).get("pogejieguo"))){
 				zuJian.poge1.setTextColor(0x88AA0000);//��ɫ
 				zuJian.poge1.setText((String) data.get(position).get("pogejieguo"));
 			}else if ("".equals((String) data.get(position).get("pogejieguo"))){
 				zuJian.poge1.setTextColor(0xff000000);//��ɫ
 				zuJian.poge1.setText("");
 			}else{
 				zuJian.poge1.setText((String) data.get(position).get("pogejieguo"));
 			} 			
 			
 			//zuJian.xiaozuyijian1.setAdapter(adapter);
 			//Ĭ�����ͨ��
 			//zuJian.xiaozuyijian1.setPromptId(1);
 			
 			zuJian.xiaozufen1.setText( xiaozufenArray[position]); 			
 			
 			
 			if ("toupiao".equals(appState.workfloat)){
 				zuJian.toupiao1.setEnabled(true);
// 				listView_xiaozuyijian_submit.setVisibility(View.VISIBLE);
 			}else{
 				zuJian.toupiao1.setEnabled(false);
 				zuJian.toupiao1.setVisibility(View.GONE);
 			}
 			
 			if ("�Ƽ�".equals((String) data.get(position).get("opinion") )){
 				zuJian.xiaozuyijian1.setSelection(0);
// 				zuJian.toupiao1.setSelection(0);
 			}else if ("���Ƽ�".equals((String) data.get(position).get("opinion") )){
 				zuJian.xiaozuyijian1.setSelection(1);
// 				zuJian.toupiao1.setSelection(1);
 			}else{
 				zuJian.xiaozuyijian1.setSelection(2);
// 				zuJian.toupiao1.setSelection(0);
 			}
 			
 			//Ĭ�ϸ�С���������
 			if ("�޳�".equals((String) data.get(position).get("toupiao"))){
 				zuJian.toupiao1.setSelection(0);
 				//zuJian.xiaozuyijian1.setBackgroundColor(0xff000000);//��ɫ
 			}else if ("����".equals((String) data.get(position).get("toupiao"))){
 				zuJian.toupiao1.setSelection(1);
 				//zuJian.xiaozuyijian1.setBackgroundColor(0x88AA0000);//��ɫ
 			}else if ("��Ȩ".equals((String) data.get(position).get("toupiao"))){
 				zuJian.toupiao1.setSelection(2);
 				//zuJian.xiaozuyijian1.setBackgroundColor(0x88AA0000);//��ɫ
 			}else if ("".equals((String) data.get(position).get("toupiao"))){
 				zuJian.toupiao1.setSelection(3);
 				//zuJian.xiaozuyijian1.setBackgroundColor(0x88AA0000);//��ɫ
 			}

 			
 			if ("xiaozuyijian".equals(appState.workfloat)){
 				zuJian.xiaozuyijian1.setEnabled(true);
// 				listView_xiaozuyijian_submit.setVisibility(View.GONE);
 			}else{
 				zuJian.xiaozuyijian1.setEnabled(false);
 			}
 			
 			
 			zuJian.ceshi1.setText((String) data.get(position).get("ceshi"));
 			if("�ϸ�".equals((String) data.get(position).get("ceshi"))){
 				zuJian.ceshi1.setTextColor(0xff000000);//��ɫ
 				zuJian.ceshi1.setText((String) data.get(position).get("ceshi"));
 			}else if("���ϸ�".equals((String) data.get(position).get("ceshi"))){
 				zuJian.ceshi1.setTextColor(0x88AA0000);//��ɫ
 				zuJian.ceshi1.setText((String) data.get(position).get("ceshi"));
 			}else if ("".equals((String) data.get(position).get("ceshi"))){
 				zuJian.ceshi1.setTextColor(0xff000000);//��ɫ
 				zuJian.ceshi1.setText("");
 			}else{
 				zuJian.ceshi1.setText((String) data.get(position).get("ceshi"));
 			} 			
 			
 			
			zuJian.xiaozuyijian1.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {
							// TODO Auto-generated method stub
							String str = parent.getItemAtPosition(position1).toString();
							//Toast.makeText(xiaozuyijianActicity.this, "��������:" + str, 2000).show();
							//ע�⣺�����position1��spinnerѡ��item��position ��0��ʼ		
							
							if ( "xiaozuyijian".equals(appState.workfloat) //����С�����״̬
									&& ( appState.pinweiName.equals(appState.peopleList.get(position).get("expert_name").toString())  //������ί���Լ�||�ǿ�
											|| "".equals(appState.peopleList.get(position).get("expert_name").toString())  )  
								){
								String pwhid = appState.pwhid;// ��ί��
								String pwid = appState.pinweiName;// ��ί
								
								HashMap<String, Object> m = new HashMap<String, Object>();
								m = lst.get(position);
								m.remove("opinion");		
								m.remove("expert_name");
								
								if (!"".equals(str) ){ //��Ϊ�գ�д�Լ���ί����
									m.put("opinion", str);
									m.put("expert_name", appState.pinweiName);
								}else{//Ϊ�գ���ί�������
									m.put("opinion", "");
									m.put("expert_name", "");
								}
								
								
								
								lst.remove(position);
								lst.add(position, m);			
								
								saImageItems.notifyDataSetChanged();

								
								//�����ύ�����˵�С�����
								
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
					//Toast.makeText(xiaozuyijianActicity.this, "��������:" + str, 2000).show();
					//ע�⣺�����position1��spinnerѡ��item��position ��0��ʼ
					
					
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
			
			//edittext���鷳���ȼ�һ����������
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
						//��μ��˺���
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
 		//����������
 		String pwhid = appState.pwhid;// ��ί��
		String pwid = appState.pinweiName;// ��ί
		boolean sendcheck = false;
		boolean sendtoupiao = false;

		StringBuilder dataTransformb = new StringBuilder();
		dataTransformb.append("pwhid=" + URLEncoder.encode(pwhid) // ��ί��
				+ "&pwid=" + URLEncoder.encode(pwid) // ��ί
				+ "&data=");

		if ("xiaozuyijian".equals(appState.workfloat)){
			String s, n;
			sendcheck = false;

			HashMap<String, Object> m = new HashMap<String, Object>();		
				// �ϳ��ύ����,����ʱ��ʱ����
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
				dataTransformb.deleteCharAt(dataTransformb.length() - 1);// ȥ�����һ������
				
				if (sendcheck){//�������
					String tmp = submitcheck(dataTransformb.toString());
					if ("û���ظ�".equals(tmp)){ 
							System.out.println("û���ظ�");
							popTijiaoWindow();
					} else  {
						//������ʾ�Ѿ����ύ������������
						new AlertDialog.Builder(this)
	 					.setTitle("���в�����Ա�Ѿ���������ί��дС������������޸Ļ�����ύ��")
	 					.setMessage(tmp)
	 					.setNegativeButton("�����޸�",
	 							new DialogInterface.OnClickListener() {
	 								@Override
	 								public void onClick(DialogInterface dialog, int which) {
	 									//this.s = "Negative";
	 						}
	 					})
	 					.setPositiveButton("�����ύ", new DialogInterface.OnClickListener() {
	 						public void onClick(DialogInterface dialog, int whichButton) {
	 							String pwhid = appState.pwhid;// ��ί��
	 							String pwid = appState.pinweiName;// ��ί

	 							StringBuilder dataTransformb = new StringBuilder();
	 							dataTransformb.append("pwhid=" + URLEncoder.encode(pwhid) // ��ί��
	 									+ "&pwid=" + URLEncoder.encode(pwid) // ��ί
	 									+ "&data=[");

	 							if ("xiaozuyijian".equals(appState.workfloat)){
	 								// �ϳ��ύ����,����ʱ��ʱ����
//	 								for (int cur = 0; cur < appState.people_total; cur++) {
//	 									dataTransformb.append("{\"id\":\""
//	 											+ URLEncoder.encode(appState.peopleList.get(cur).get("id").toString()) + "\","// ������
//	 											+ "\"opinion\":\"" + URLEncoder.encode(xiaozuyijianArray[cur]) + "\","// С�����
//	 											+ "\"total\":\"" + URLEncoder.encode(appState.scoreList.get(cur).get("pinjunfen").toString()) + "\"," // ���˷�
//	 											+ "\"group_score\":\"" + URLEncoder.encode(xiaozufenArray[cur]) + "\"" // С���
//	 											+ "},");
//	 								}
	 								String s, n;

	 								HashMap<String, Object> m = new HashMap<String, Object>();		
	 									// �ϳ��ύ����,����ʱ��ʱ����
	 									for (int cur = 0; cur < appState.people_total; cur++) {
	 										m = lst.get(cur);
	 										s = m.get("opinion").toString();
	 										n = m.get("expert_name").toString();
	 											xiaozuyijianArray[cur] = s;
	 											
	 											if (s != null && !"".equalsIgnoreCase(s) && pwid.equals(n) ){
	 												dataTransformb.append("{\"id\":\""
	 														+ URLEncoder.encode(appState.peopleList.get(cur).get("id").toString()) + "\","// ������
	 														+ "\"opinion\":\"" + URLEncoder.encode(xiaozuyijianArray[cur]) + "\","// С�����
	 														//+ "\"total\":\"" + URLEncoder.encode(appState.scoreList.get(cur).get("pinjunfen").toString()) + "\"," // ���˷�
	 														+ "\"total\":\"" + URLEncoder.encode("-1") + "\"," // ���˷�
	 														+ "\"group_score\":\"" + URLEncoder.encode(xiaozufenArray[cur]) + "\"" // С���
	 														+ "},");
	 											}
	 											
	 									}							
	 							}else if("toupiao".equals(appState.workfloat)){
	 								// �ϳ��ύ����,����ʱ��ʱ����
	 								for (int cur = 0; cur < appState.people_total; cur++) {
	 									//�ϳ��ύ����
	 									dataTransformb.append( "{\"id\":\"" + URLEncoder.encode(appState.peopleList.get(cur).get("id").toString()) + "\","// ������
	 											+ "\"vote\":\"" + URLEncoder.encode( toupiaoArray[cur]) + "\"" //ͶƱ
	 											+ "},");
	 								}
	 							}
	 							
	 							dataTransformb.deleteCharAt(dataTransformb.length() - 1);// ȥ�����һ������
	 							dataTransformb.append("]");

	 							String tmp = submitxiaozuyijian(dataTransformb.toString());
	 							if ("���ճɹ�".equals(tmp)){ 
	 									try {
	 										updateworkfloatT.sleep(1);
	 										if (updateworkfloatT != null){
	 											updateworkfloatT.interrupt();
	 										}
	 									} catch (InterruptedException e) {
	 										// TODO Auto-generated catch block
	 										e.printStackTrace();
	 									}
	 									
	 									Toast toast = Toast.makeText(getApplicationContext(),"�ύ�ɹ���",  Toast.LENGTH_SHORT);
	 									toast.setGravity(Gravity.CENTER, 0,0);
	 									toast.cancel();
	 									toast.show();
	 									
	 									System.out.println("С�����/ͶƱ�ύ�ɹ�\r\n");					
	 									appState.closeMain = true;//�ύ�ɹ��Źر�������
	 									xiaozuyijianActicity.this.finish();; //���ص���ڽ���
	 							} else if ("����ʧ��".equals(tmp)) {
	 								Toast toast = Toast.makeText(getApplicationContext(), "����������ʧ�ܣ��������ύ��", Toast.LENGTH_SHORT);
	 								toast.setGravity(Gravity.CENTER, 0, 0);
	 								toast.show();
	 								
	 								System.out.println("С�����/ͶƱ�ύʧ��\r\n");
	 								//�ύʧ�ܼ�������������
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
					.setTitle("��ʾ")
					.setMessage("���ĵ����л��в�����ԱδͶƱ����ȫ��ͶƱ�����ύ���ݡ�")
					.setNegativeButton("�����޸�",
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
 	// ���� ��ʾ�Ƿ��ύ����
 			new AlertDialog.Builder(this)
 					.setTitle("�Ƿ��ύ��")
 					.setMessage("�����ȷ������ť�ύ���ݵ������������\n��������ء���ť���Լ����޸ġ�\nע�⣺ѡ��ȷ��֮�󣬲����޸Ĳ�����Ա����Ϣ��")
 					.setNegativeButton("����",
 							new DialogInterface.OnClickListener() {
 								@Override
 								public void onClick(DialogInterface dialog, int which) {
 									//this.s = "Negative";
 						}
 					})
 					.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
 						public void onClick(DialogInterface dialog, int whichButton) {
 							String pwhid = appState.pwhid;// ��ί��
 							String pwid = appState.pinweiName;// ��ί

 							StringBuilder dataTransformb = new StringBuilder();
 							dataTransformb.append("pwhid=" + URLEncoder.encode(pwhid) // ��ί��
 									+ "&pwid=" + URLEncoder.encode(pwid) // ��ί
 									+ "&data=[");

 							if ("xiaozuyijian".equals(appState.workfloat)){
 								// �ϳ��ύ����,����ʱ��ʱ����
// 								for (int cur = 0; cur < appState.people_total; cur++) {
// 									dataTransformb.append("{\"id\":\""
// 											+ URLEncoder.encode(appState.peopleList.get(cur).get("id").toString()) + "\","// ������
// 											+ "\"opinion\":\"" + URLEncoder.encode(xiaozuyijianArray[cur]) + "\","// С�����
// 											+ "\"total\":\"" + URLEncoder.encode(appState.scoreList.get(cur).get("pinjunfen").toString()) + "\"," // ���˷�
// 											+ "\"group_score\":\"" + URLEncoder.encode(xiaozufenArray[cur]) + "\"" // С���
// 											+ "},");
// 								}
 								String s, n;

 								HashMap<String, Object> m = new HashMap<String, Object>();		
 									// �ϳ��ύ����,����ʱ��ʱ����
 									for (int cur = 0; cur < appState.people_total; cur++) {
 										m = lst.get(cur);
 										s = m.get("opinion").toString();
 										n = m.get("expert_name").toString();
 											xiaozuyijianArray[cur] = s;
 											
 											if (s != null && !"".equalsIgnoreCase(s) && pwid.equals(n) ){
 												dataTransformb.append("{\"id\":\""
 														+ URLEncoder.encode(appState.peopleList.get(cur).get("id").toString()) + "\","// ������
 														+ "\"opinion\":\"" + URLEncoder.encode(xiaozuyijianArray[cur]) + "\","// С�����
 														//+ "\"total\":\"" + URLEncoder.encode(appState.scoreList.get(cur).get("pinjunfen").toString()) + "\"," // ���˷�
 														+ "\"total\":\"" + URLEncoder.encode("-1") + "\"," // ���˷�
 														+ "\"group_score\":\"" + URLEncoder.encode(xiaozufenArray[cur]) + "\"" // С���
 														+ "},");
 											}
 											
 									}							
 							}else if("toupiao".equals(appState.workfloat)){
 								// �ϳ��ύ����,����ʱ��ʱ����
 								for (int cur = 0; cur < appState.people_total; cur++) {
 									//�ϳ��ύ����
 									dataTransformb.append( "{\"id\":\"" + URLEncoder.encode(appState.peopleList.get(cur).get("id").toString()) + "\","// ������
 											+ "\"vote\":\"" + URLEncoder.encode( toupiaoArray[cur]) + "\"" //ͶƱ
 											+ "},");
 								}
 							}
 							
 							dataTransformb.deleteCharAt(dataTransformb.length() - 1);// ȥ�����һ������
 							dataTransformb.append("]");

 							String tmp = submitxiaozuyijian(dataTransformb.toString());
 							if ("���ճɹ�".equals(tmp)){ 
 									Toast toast = Toast.makeText(getApplicationContext(),"�ύ�ɹ���",  Toast.LENGTH_SHORT);
 									toast.setGravity(Gravity.CENTER, 0,0);
 									toast.cancel();
 									toast.show();
 									
 									System.out.println("С�����/ͶƱ�ύ�ɹ�\r\n");								

 									try {
 										updateworkfloatT.sleep(1);
 										if (updateworkfloatT != null){
 											updateworkfloatT.interrupt();
 										}
 									} catch (InterruptedException e) {
 										// TODO Auto-generated catch block
 										e.printStackTrace();
 									}
 									
 									appState.closeMain = true;//�ύ�ɹ��Źر�������
 									xiaozuyijianActicity.this.finish();; //���ص���ڽ���
 							} else if ("����ʧ��".equals(tmp)) {
 								Toast toast = Toast.makeText(getApplicationContext(), "����������ʧ�ܣ��������ύ��", Toast.LENGTH_SHORT);
 								toast.setGravity(Gravity.CENTER, 0, 0);
 								toast.show();
 								
 								System.out.println("С�����/ͶƱ�ύʧ��\r\n");
 								//�ύʧ�ܼ�������������
 							}

 							
 						}
 					}).show();
 	}
 	
 	private String submitxiaozuyijian(String dataTrasform) {
 		String tmp = "����ʧ��";
 		
 	// Ҫ���ʵ�web servlet
 				// ע�⣺IP�Ͷ˿��Ǳ��ص� ��Ҫ�������IP�Ͷ˿�
 				String servletUrl = "";
 				if ("xiaozuyijian".equals(appState.workfloat)){
 					servletUrl = appState.HttpHead + "/expert/opinion";
 				}else if ("toupiao".equals(appState.workfloat)){
 					servletUrl = appState.HttpHead + "/expert/vote";
 				}
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
 						out.writeBytes(dataTrasform.toString());
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
 	
 	private String submitcheck(String dataTrasform) {
 		
 	// Ҫ���ʵ�web servlet
 				// ע�⣺IP�Ͷ˿��Ǳ��ص� ��Ҫ�������IP�Ͷ˿�
 				String servletUrl = "";

 				servletUrl = appState.HttpHead + "/expert/check";

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
 						out.writeBytes(dataTrasform.toString());
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

		// Ҫ���ʵ�web servlet
		// ע�⣺IP�Ͷ˿��Ǳ��ص� ��Ҫ�������IP�Ͷ˿�
		String servletUrl = appState.HttpHead + "/expert/complete?pwid=" + URLEncoder.encode(appState.pinweiName);
		
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
				
				
				//downloadfinish = true;  //ǿ����true
				if ("��������".equals(tmp)){
					appState.workfloat = "pinfen";
				}else if ("����ͶƱ".equals(tmp)){
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
						appState.closeMain = true;//�ύ�ɹ��Źر�������
						xiaozuyijianActicity.this.finish();; //���ص���ڽ���
					}
					
					if (appState.pinshenjieshu) {
						
					}
				}else if ("����д���".equals(tmp)){
					appState.workfloat = "xiaozuyijian";

				}else if ("ͶƱ����".equals(tmp)) {

				}else if("�������".equals(tmp)){

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
     
 	
 	private Handler messageHandler;
 	private int cnt = 0;

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
			if (!((String) msg.obj == null)) {
				if ("send".equals((String) msg.obj))	{
//					submitXiaozuyijian();
				}else if ("request".equals((String) msg.obj))	{
					getWokfloat();
				}				
			}
		}
	}
	
	// ����״̬k����----------------------------------------
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
		String pwhid = appState.pwhid;// ��ί��
		String pwid = appState.pinweiName;// ��ί
		StringBuilder dataTransformb = new StringBuilder();
		dataTransformb.append("pwhid=" + URLEncoder.encode(pwhid) // ��ί��
				+ "&pwid=" + URLEncoder.encode(pwid) // ��ί
				+ "&data=[");

		HashMap<String, Object> m = new HashMap<String, Object>();		
			// �ϳ��ύ����,����ʱ��ʱ����
			for (int cur = 0; cur < appState.people_total; cur++) {
				m = lst.get(cur);
				s = m.get("opinion").toString();
//				if ( !s.equals(xiaozuyijianArray[cur]) ){
//					tijiao = true;
					xiaozuyijianArray[cur] = s;
					
					dataTransformb.append("{\"id\":\""
							+ URLEncoder.encode(appState.peopleList.get(cur).get("id").toString()) + "\","// ������
							+ "\"opinion\":\"" + URLEncoder.encode(xiaozuyijianArray[cur]) + "\","// С�����
							//+ "\"total\":\"" + URLEncoder.encode(appState.scoreList.get(cur).get("pinjunfen").toString()) + "\"," // ���˷�
							+ "\"total\":\"" + URLEncoder.encode("-1") + "\"," // ���˷�
							+ "\"group_score\":\"" + URLEncoder.encode(xiaozufenArray[cur]) + "\"" // С���
							+ "},");
//				}				
			}

		
		dataTransformb.deleteCharAt(dataTransformb.length() - 1);// ȥ�����һ������
		dataTransformb.append("]");

//		if (tijiao){
			String tmp = submitxiaozuyijian(dataTransformb.toString());
			if ("���ճɹ�".equals(tmp)){ 
					Toast toast = Toast.makeText(getApplicationContext(),"�ύ�ɹ���",  Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0,0);
					toast.cancel();
					toast.show();
					
					System.out.println("С�����/ͶƱ�ύ�ɹ�\r\n");								
//					appState.closeMain = true;//�ύ�ɹ��Źر�������
//					finish();
					//�ύ�ɹ����һ��״̬
					getWokfloat();
			} else if ("����ʧ��".equals(tmp)) {
				Toast toast = Toast.makeText(getApplicationContext(), "����������ʧ�ܣ��������ύ��", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				
				System.out.println("С�����/ͶƱ�ύʧ��\r\n");
				//�ύʧ��С���������
//				zuJian.xiaozuyijian1.setSelection(2);
			}
//		}
	}
	
	
	
}
