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
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//���óɺ���
		appState = ((Global_var) getApplicationContext()); // ���ȫ�ֱ���	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xiaozuyijian);
		
		 xiaozufenArray  = new String [appState.people_total];
		 xiaozuyijianArray  = new String [appState.people_total];
		 lianghuaArray  = new String [appState.people_total];
		 
		 for (int i=0 ;i<appState.people_total; i++){
			 xiaozufenArray[i] = "";
			 xiaozuyijianArray[i] = "�Ƽ�";
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
				if ( !"".equals(cursor.getString(9)) && cursor.getString(9) != null ) {	//9С������
					xiaozufenArray [i] = cursor.getString(9);
				}
				if ( !"".equals(cursor.getString(10)) && cursor.getString(10) !=null ) {	//10С�����
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
			if (cursor != null && cursor.getCount() != 0) { //������ݿ�������ˣ�����
				appState.Update_people(appState.peopleList.get(i).get("id").toString(), xiaozufenArray [i], xiaozuyijianArray [i]);
			}else {	//������ݿ�û������� ���
				appState.add(appState.peopleList.get(i).get("id").toString(), xiaozufenArray [i], xiaozuyijianArray [i]);
			}
			cursor.close();
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
			if (lianghuaArray[position].equals("����")) {	//�������ֲŵ����޸�С�����ҳ��
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
			//map.put("xiaozupinfen", xiaozufenArray[i]);
			//map.put("opinion", "ͨ��");
			
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
 			zuJian.pinwei1.setText((String) data.get(position).get("pinwei"));
 			
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
 			
 			if ("�Ƽ�".equals((String) data.get(position).get("opinion"))){
 				zuJian.xiaozuyijian1.setSelection(0);
 				//zuJian.xiaozuyijian1.setBackgroundColor(0xff000000);//��ɫ
 			}else if ("���Ƽ�".equals((String) data.get(position).get("opinion"))){
 				zuJian.xiaozuyijian1.setSelection(1);
 				//zuJian.xiaozuyijian1.setBackgroundColor(0x88AA0000);//��ɫ
 			}else{
 				zuJian.xiaozuyijian1.setSelection(0);
 				//zuJian.xiaozuyijian1.setBackgroundColor(0xff000000);//��ɫ
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
 	// ���� ��ʾ�Ƿ��ύ����
		new AlertDialog.Builder(this)
				.setTitle("�Ƿ��ύС�������")
				.setMessage("�����ȷ������ť�ύͶƱ����������������\n��������ء���ť���Լ����޸ġ�\nע�⣺ѡ��ȷ��֮�󣬲����޸Ĳ�����Ա����Ϣ��")
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

						// �ϳ��ύ����,����ʱ��ʱ����
						for (int cur = 0; cur < appState.people_total; cur++) {
							dataTransformb.append("{\"id\":\""
									+ URLEncoder.encode(appState.peopleList.get(cur).get("id").toString()) + "\","// ������
									+ "\"opinion\":\"" + URLEncoder.encode(xiaozuyijianArray[cur]) + "\","// С�����
									+ "\"total\":\"" + URLEncoder.encode(appState.scoreList.get(cur).get("pinjunfen").toString()) + "\"," // ���˷�
									+ "\"group_score\":\"" + URLEncoder.encode(xiaozufenArray[cur]) + "\"" // С���
									+ "},");
						}
						dataTransformb.deleteCharAt(dataTransformb.length() - 1);// ȥ�����һ������
						dataTransformb.append("]");

						String tmp = submitxiaozuyijian(dataTransformb.toString());
						if ("���ճɹ�".equals(tmp)){ 
								Toast toast = Toast.makeText(getApplicationContext(),"�ύ�ɹ���",  Toast.LENGTH_SHORT);
								toast.setGravity(Gravity.CENTER, 0,0);
								toast.cancel();
								toast.show();
								
								System.out.println("С������ύ�ɹ�\r\n");								
								appState.closeMain = true;//�ύ�ɹ��Źر�������
								finish();
						} else if ("����ʧ��".equals(tmp)) {
							Toast toast = Toast.makeText(getApplicationContext(), "����������ʧ�ܣ��������ύ��", Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							
							System.out.println("С������ύʧ��\r\n");
							//�ύʧ�ܼ�������������
						}

						
					}
				}).show();
		
 	}
 	
 	private String submitxiaozuyijian(String dataTrasform) {
 		String tmp = "����ʧ��";
 		
 	// Ҫ���ʵ�web servlet
 				// ע�⣺IP�Ͷ˿��Ǳ��ص� ��Ҫ�������IP�Ͷ˿�
 				String servletUrl = appState.HttpHead + "/expert/opinion";
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

     
}
