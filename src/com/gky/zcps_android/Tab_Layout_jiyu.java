/**
 * ������Ϣ
 * 
 * @author ����
 * 
 */
package com.gky.zcps_android;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Tab_Layout_jiyu extends Activity
{
	private Global_var appState; // ���ȫ�ֱ���;
	
	private TextView textView_cpidf; //���
	private TextView textView_cpnamef; //����
	private TextView textView_cpcompanyf;//������λ
	private EditText textView_cpjiyuf;//���μ���
	private Button button_zhurensave;
	
	

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	appState = ((Global_var) getApplicationContext()); // ���ȫ�ֱ���
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout_jiyu);
        
        findView();    
    }
    
    private void findView() {
		// TODO Auto-generated method stub
    	textView_cpidf = (TextView) findViewById(R.id.textView_cpidf); // ���        
    	textView_cpnamef = (TextView) findViewById(R.id.textView_cpnamef); // ����    
    	textView_cpcompanyf = (TextView) findViewById(R.id.textView_cpcompanyf); // ������λ    
    	textView_cpjiyuf = (EditText) findViewById(R.id.textView_cpjiyuf);//���μ���
    	button_zhurensave = (Button) findViewById(R.id.button_zhurensave);//
	}

	@Override 
    public void onResume(){
    	super.onResume();
    	Log.i("info","Tab_Layout_jiyu_onResume");
    	
    	getSharePrefe();//�ָ�״̬
		// ��ȡͶƱ״̬
		appState.getDB();
		cursor = appState.queryFailed(appState.failedList.get(appState.people_cur).get("id_f").toString());
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToNext();
			textView_cpjiyuf.setText(cursor.getString(cursor.getColumnIndex("pinyu")));
		} else {
			textView_cpjiyuf.setText("");
			
		}
		cursor.close();
		appState.dbClose();

		update();
		// appState.isUpdated = false;
		Log.i("info", "upDated");	
    	
    }
    
	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences userInfo = getSharedPreferences("pinyu", 0);  
		userInfo.edit().putString("pinyu", textView_cpjiyuf.getText().toString()).commit();
	}
	
	private void getSharePrefe() {
		// TODO Auto-generated method stub
		SharedPreferences userInfo = getSharedPreferences("tab_pinyu", 0);  
		String pinyu = userInfo.getString("pinyu", "");  
		
		textView_cpjiyuf.setText(pinyu);  
	}
	
	
    public void update(){
    	textView_cpidf.setText(appState.failedList.get(appState.people_cur).get("id_f").toString());//id
    	textView_cpnamef.setText(appState.failedList.get(appState.people_cur).get("name_f").toString());// ����    
    	textView_cpcompanyf.setText(appState.failedList.get(appState.people_cur).get("company_f").toString()); // ������λ    
    	
    	
  
    }
    
    
    Cursor cursor;
    public void button_zhurensave_onclick(View target) {
    	if (textView_cpjiyuf.getText() != null && textView_cpjiyuf.length() > 0) {
			// д���ݿ�
			appState.getDB();
			// 
			cursor = appState.queryFailed(appState.failedList.get(appState.people_cur).get("id_f").toString());
			if (cursor == null || cursor.getCount() == 0) {//���
				appState.Add_pinyu(appState.failedList.get(appState.people_cur).get("id_f").toString(),
						textView_cpjiyuf.getText().toString(),
						"0"); //�ύ״̬������/�ύ��0 1 
			}else {//�޸�
				appState.Update_pinyu(appState.failedList.get(appState.people_cur).get("id_f").toString(),
						textView_cpjiyuf.getText().toString()); 
			}
			
			cursor.close();
			

			Toast toast = Toast.makeText(getApplicationContext(), "����ɹ���",
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			
			//appState.getDB();
		 	cursor = appState.getFailedAll();
		 	//����title----------
		 	int num1 = 0;			
			
	    	Cursor cursor1 = appState.queryFailed_zhuangtai("0");
	    	num1 = cursor1.getCount();
	    	cursor1.close();
	    	this.getParent().setTitle("��ǰ��Ա��" + String.valueOf(appState.people_cur + 1) + 
					" �� " + 
					"������Ա��" + String.valueOf(appState.people_total) + 
					"����ǰ�ѱ��棺" + String.valueOf(num1)
					);
	    	
	
		 	//--------------------
		 	
			if (cursor.getCount() >= appState.failedList.size()) {
				cursor.close();
				openList();
				/*
				// ���� ��ʾ�Ƿ��ύ����
				new AlertDialog.Builder(this)
						.setTitle("�Ƿ��ύ���μ��")
						.setMessage("�������μ�����д��ɡ�\n�����ȷ������ť�ύ���μ��ﵽ�����������\n��������ء���ť���Լ����޸ġ�\nע�⣺ѡ��ȷ��֮�󣬲����޸�δͨ����Ա�����μ��")
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
										for (appState.people_cur=0; appState.people_cur<appState.people_total; appState.people_cur++){
											// �ύ���� 
											String tmp = submitJiyu();
											 if ("���ճɹ�".equals(tmp)){ // ״̬��Ϊ���ύ���� // �ύ״̬��δ�ύ/�ύ����/�ύͶƱ��0 1 2  3
												 appState.dbClose();
												 appState.getDB();
												 appState.Update_failedState(appState.failedList.get(appState.people_cur).get("id_f").toString(), 
														 "1");// �ύ����


													Toast toast = Toast.makeText(getApplicationContext(),
															appState.failedList.get(appState.people_cur).get("id_f").toString() + "�ύ�ɹ���", 
															Toast.LENGTH_SHORT);
													toast.setGravity(Gravity.CENTER, 0,0);
													toast.cancel();
													toast.show();
											} else if ("����ʧ��".equals(tmp)) {
												Toast toast = Toast.makeText(getApplicationContext(),
														appState.failedList.get(appState.people_cur).get("id_f").toString() + "����������ʧ�ܣ��������ύ��",
														Toast.LENGTH_SHORT);
												toast.setGravity(Gravity.CENTER, 0, 0);
												toast.show();
											}
										}//end for
										
										finish();
									}
								}).show();	
								*/		
			}
			
			cursor.close();
			appState.dbClose();


		} else {
			Toast toast = Toast.makeText(getApplicationContext(), "���������μ��",
					Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
    }
    
    private void openList(){
    	appState.dbClose();
		Intent it = new Intent(this, chakanActivity.class);
		
		Bundle bundle = new Bundle();
		bundle.putString("leixin","jiyu");
		it.putExtras(bundle);
		
		startActivity(it);
    }
    
    /*
    private String submitJiyu() {
		// TODO Auto-generated method stub
		
		 // (��ί�ᣩpwhid ����ίid��pwid (�����ˣ�id 10λid (ͶƱ��toupiao ͬ�� ���� ��Ȩ ��ѡ1
		 
		appState.getDB();
		cursor = appState.queryFailed(appState.failedList.get(appState.people_cur).get("id_f").toString());
		cursor.moveToNext();
		
		String pwhid = appState.pwhid;// ��ί��
		//String pwid = appState.pinweiName;// ��ί
		String id = appState.failedList.get(appState.people_cur).get("id_f").toString();// ������
		//String vote = cursor.getString(4);
		
		cursor.close();
		appState.dbClose();

		String tmp = "����ʧ��";

		// Ҫ���ʵ�web servlet
		// ע�⣺IP�Ͷ˿��Ǳ��ص� ��Ҫ�������IP�Ͷ˿�
		String servletUrl = appState.HttpHead + "/expert/comment";
		// ����������������
		String params = "pwhid=" + URLEncoder.encode(pwhid) + "&" + 
				"id=" + URLEncoder.encode(id) + "&" + 
				"jiyu=" + URLEncoder.encode(textView_cpjiyuf.getText().toString());

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
				HttpURLConnection urlConn = (HttpURLConnection) url
						.openConnection();
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
				out.writeBytes(params);
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
*/
}
