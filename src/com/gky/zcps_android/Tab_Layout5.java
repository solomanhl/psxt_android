/**
 * ���ֺ�ͶƱ
 * 
 * @author ����
 * 
 */
package com.gky.zcps_android;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import com.soloman.file.FileUtils;
import com.soloman.intent.SendIntent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Tab_Layout5 extends Activity {
	private Global_var appState; // ���ȫ�ֱ���;

	private RelativeLayout lianghua, bulianghua;
	private TextView textView_cpid5, textView_cpname5, textView_cpvotestate5, textView_cpcompany5;
	private EditText textView_cpfenshu1, textView_cpfenshu2,
			textView_cpfenshu3, textView_cpzongfen;
	private RadioButton radioButton_agree, radioButton_against,
			radioButton_waiver, radio_tongyi, radio_butongyi,
			radioButton_agree_yijian, radioButton_against_yijian;
	private Button button_vote, button_cancelvote, button_pinfensave, button_xiaozuyijian,
			button_zhengce5, button_zhengce5_2, button_czsm5, button_czsm5_2;
	
	private RadioButton radio_gerentongyi, radio_gerenbutongyi;

	private TextView textView_pogeyijian;
	private EditText textView_cppogeyijian;
	private RadioGroup radioGroup_poge, radioGroup_yijian;

	private TextView textView_pogejielun;
	private TextView textView_cppogejielun;// �Ƹ����
	private TextView textView_cppinjunfen;// ƽ����
	private TextView textView_ceshi5;
	private TextView textView_cpceshi5;// ���Խ��
	private TextView textView_cpenglish5;// Ӣ��ɼ�
	private TextView textView_cpcomputer5;// �����
	
	private Button button_chakan1,button_chakan2;//�鿴��ϸ

	private Cursor cursor;

	private String state;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		appState = ((Global_var) getApplicationContext()); // ���ȫ�ֱ���
		super.onCreate(savedInstanceState);

		if ("pinfen".equals(appState.tab5_state)) {
			// ���ֽ׶�
			setContentView(R.layout.tab_layout5_pinfen);

			lianghua =  (RelativeLayout) findViewById(R.id.lianghua);
			bulianghua =  (RelativeLayout) findViewById(R.id.bulianghua);
					
			
			radio_gerentongyi = (RadioButton) findViewById(R.id.radio_gerentongyi);
			radio_gerenbutongyi = (RadioButton) findViewById(R.id.radio_gerenbutongyi);
			
			textView_cpid5 = (TextView) findViewById(R.id.textView_cpid5);
			textView_cpname5 = (TextView) findViewById(R.id.textView_cpname5);
			textView_cpzongfen = (EditText) findViewById(R.id.textView_cpzongfen);
			textView_cpfenshu1 = (EditText) findViewById(R.id.textView_cpfenshu1);
			textView_cpfenshu2 = (EditText) findViewById(R.id.textView_cpfenshu2);
			textView_cpfenshu3 = (EditText) findViewById(R.id.textView_cpfenshu3);
			//textView_cppogeyijian = (EditText) findViewById(R.id.textView_cppogeyijian);
			textView_cpcompany5 = (TextView) findViewById(R.id.textView_cpcompany5);
			
			button_pinfensave = (Button) findViewById(R.id.button_pinfensave);
			button_zhengce5 = (Button) findViewById(R.id.button_zhengce5);
			button_czsm5 = (Button) findViewById(R.id.button_czsm5);
			
			radio_tongyi = (RadioButton) findViewById(R.id.radio_tongyi);
			radio_butongyi = (RadioButton) findViewById(R.id.radio_butongyi);

			textView_pogeyijian = (TextView) findViewById(R.id.textView_pogeyijian);
			textView_cppogeyijian = (EditText) findViewById(R.id.textView_cppogeyijian);
			radioGroup_poge = (RadioGroup) findViewById(R.id.radioGroup_poge);
			
			button_chakan1 = (Button) findViewById(R.id.button_chakan1);
			button_chakan2 = (Button) findViewById(R.id.button_chakan2);
			
			
			textView_cpfenshu1.setText("");  
			textView_cpfenshu2.setText("");  
			textView_cpfenshu3.setText("");  
			textView_cppogeyijian.setText("");  

			textView_cpfenshu1.addTextChangedListener(new TextWatcher() {
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// TODO Auto-generated method stub
				}

				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub
				}

				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					Log.i("TAG", textView_cpfenshu1.getText().toString());
					total();
				}
			});
			textView_cpfenshu2.addTextChangedListener(new TextWatcher() {
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// TODO Auto-generated method stub
				}

				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub
				}

				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					Log.i("TAG", textView_cpfenshu1.getText().toString());
					total();
				}
			});
			textView_cpfenshu3.addTextChangedListener(new TextWatcher() {
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// TODO Auto-generated method stub
				}

				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub
				}

				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					Log.i("TAG", textView_cpfenshu1.getText().toString());
					total();
				}
			});

		} else if ("toupiao".equals(appState.tab5_state)) {
			// ͶƱ�׶�
			setContentView(R.layout.tab_layout5);

			textView_cpid5 = (TextView) findViewById(R.id.textView_cpid5);
			textView_cpname5 = (TextView) findViewById(R.id.textView_cpname5);
			textView_cpcompany5 = (TextView) findViewById(R.id.textView_cpcompany5);
			textView_cpvotestate5 = (TextView) findViewById(R.id.textView_cpvotestate5);
			
			button_zhengce5_2 = (Button) findViewById(R.id.button_zhengce5_2);
			button_czsm5_2 = (Button) findViewById(R.id.button_czsm5_2);

			radioButton_agree = (RadioButton) findViewById(R.id.radioButton_agree);
			radioButton_against = (RadioButton) findViewById(R.id.radioButton_against);
			radioButton_waiver = (RadioButton) findViewById(R.id.radioButton_waiver);

			button_vote = (Button) findViewById(R.id.button_vote);
			//button_cancelvote = (Button) findViewById(R.id.button_cancelvote);

			textView_pogejielun = (TextView) findViewById(R.id.textView_pogejielun);// �Ƹ����
			textView_cppogejielun = (TextView) findViewById(R.id.textView_cppogejielun);// �Ƹ����
			textView_cppinjunfen = (TextView) findViewById(R.id.textView_cppinjunfen);// ƽ����
			textView_ceshi5 = (TextView) findViewById(R.id.textView_ceshi5);// ���Խ��
			textView_cpceshi5 = (TextView) findViewById(R.id.textView_cpceshi5);// ���Խ��
			textView_cpenglish5 = (TextView) findViewById(R.id.textView_cpenglish5);// Ӣ��ɼ�
			textView_cpcomputer5 = (TextView) findViewById(R.id.textView_cpcomputer5);// �����
			
			
		}else if ("xiaozuyijian".equals(appState.tab5_state)) {
			// С������׶�
			setContentView(R.layout.tab_layout5_xiaozuyijian);

			textView_cpid5 = (TextView) findViewById(R.id.textView_cpid5);
			textView_cpname5 = (TextView) findViewById(R.id.textView_cpname5);
			textView_cpcompany5 = (TextView) findViewById(R.id.textView_cpcompany5);
			textView_cpvotestate5 = (TextView) findViewById(R.id.textView_cpvotestate5);
			
			button_zhengce5_2 = (Button) findViewById(R.id.button_zhengce5_2);
			button_czsm5_2 = (Button) findViewById(R.id.button_czsm5_2);

			radioButton_agree = (RadioButton) findViewById(R.id.radioButton_agree_yijian);
			radioButton_against = (RadioButton) findViewById(R.id.radioButton_against_yijian);

			button_xiaozuyijian = (Button) findViewById(R.id.button_xiaozuyijian);
			//button_cancelvote = (Button) findViewById(R.id.button_cancelvote);

			textView_pogejielun = (TextView) findViewById(R.id.textView_pogejielun);// �Ƹ����
			textView_cppogejielun = (TextView) findViewById(R.id.textView_cppogejielun);// �Ƹ����
			textView_cppinjunfen = (TextView) findViewById(R.id.textView_cppinjunfen);// ƽ����
			textView_ceshi5 = (TextView) findViewById(R.id.textView_ceshi5);// ���Խ��
			textView_cpceshi5 = (TextView) findViewById(R.id.textView_cpceshi5);// ���Խ��
			textView_cpenglish5 = (TextView) findViewById(R.id.textView_cpenglish5);// Ӣ��ɼ�
			textView_cpcomputer5 = (TextView) findViewById(R.id.textView_cpcomputer5);// �����
			
			
		}

		
		
		
	}

	 @Override    
		protected void onDestroy() { 
	    	super.onDestroy(); 
	    	if ("pinfen".equals(appState.tab5_state)   || "xiaozuyijian".equals(appState.tab5_state) ) {
	    		SharedPreferences userInfo = getSharedPreferences("tab5_pinfen", 0);  
	    			userInfo.edit().putString("f1", "").commit();
	    			userInfo.edit().putString("f2", "").commit();
	    			userInfo.edit().putString("f3", "").commit();  
	    			//userInfo.edit().putString("po", textView_cppogeyijian.getText().toString()).commit();  
	    			userInfo.edit().putString("po", "").commit(); 
	    			userInfo.edit().putString("postate","not_use").commit();
	    		}else if ("toupiao".equals(appState.tab5_state)) {
	    			SharedPreferences userInfo = getSharedPreferences("tab5_toupiao", 0);  
	    			userInfo.edit().putString("toupiao","").commit();
	    		}
		}
	 
	 
	@Override
	protected void onPause() {
		super.onPause();
		if ("pinfen".equals(appState.tab5_state)  || "xiaozuyijian".equals(appState.tab5_state) ) {
		SharedPreferences userInfo = getSharedPreferences("tab5_pinfen", 0);  
			userInfo.edit().putString("f1", textView_cpfenshu1.getText().toString()).commit();
			userInfo.edit().putString("f2", textView_cpfenshu2.getText().toString()).commit();
			userInfo.edit().putString("f3", textView_cpfenshu3.getText().toString()).commit();  
			//if(textView_cppogejielun.isShown()){
				userInfo.edit().putString("po", textView_cppogeyijian.getText().toString()).commit();  
				userInfo.edit().putString("postate",appState.pogebutton).commit();  
			//}
		}else if ("toupiao".equals(appState.tab5_state)) {
			SharedPreferences userInfo = getSharedPreferences("tab5_toupiao", 0);  
			userInfo.edit().putString("toupiao", appState.voteState).commit();
			
		}
	}
	
	private void getSharePrefe() {
		// TODO Auto-generated method stub
		if ("pinfen".equals(appState.tab5_state)   || "xiaozuyijian".equals(appState.tab5_state) ) {
			SharedPreferences userInfo = getSharedPreferences("tab5_pinfen", 0);  
			String f1 = userInfo.getString("f1", "");  
			String f2 = userInfo.getString("f2", "");  
			String f3 = userInfo.getString("f3", "");  
			
			
			if (appState.xianchangfenzu == true  || "".equals(f1)) {
				//����ֳ�����ģʽ������û���ҵ�shareprefe������xml�����ݿ������
//				textView_cpfenshu1.setText(f1);  
//				textView_cpfenshu2.setText(f2);  
//				textView_cpfenshu3.setText(f3);  
				textView_cpzongfen.setText( appState.scoreList.get(appState.people_cur).get("pinjunfen").toString() );
				
				//if(textView_cppogejielun.isShown()){
					//String po = userInfo.getString("po", "");  
					//textView_cppogeyijian.setText(po);  
					//appState.pogebutton = userInfo.getString("postate", "");  
				appState.pogebutton = appState.scoreList.get(appState.people_cur).get("pogejielun").toString() ;
					if ("yes".equals(appState.pogebutton) ){
						radio_tongyi.setChecked(true);
					}else if ("no".equals(appState.pogebutton) ){
						radio_butongyi.setChecked(true);
					}else{
						radio_tongyi.setChecked(true);
						appState.pogebutton = "not_use";
					}
				//}
			}else {
				//���������������ֽ׶δ���
				textView_cpfenshu1.setText(f1);  
				textView_cpfenshu2.setText(f2);  
				textView_cpfenshu3.setText(f3);  
				
				//if(textView_cppogejielun.isShown()){
					String po = userInfo.getString("po", "");  
					textView_cppogeyijian.setText(po);  
					appState.pogebutton = userInfo.getString("postate", "");  
					if ("yes".equals(appState.pogebutton) ){
						radio_tongyi.setChecked(true);
					}else if ("no".equals(appState.pogebutton) ){
						radio_butongyi.setChecked(true);
					}else{
						radio_tongyi.setChecked(true);
						appState.pogebutton = "not_use";
					}
				//}
			}
			
			
		}else if ("toupiao".equals(appState.tab5_state)) {
			SharedPreferences userInfo = getSharedPreferences("tab5_toupiao", 0);  
			appState.voteState = userInfo.getString("toupiao", "δͶƱ");  
		}
	}
	
	
	private void total() {
		int f1, f2, f3;

		if (textView_cpfenshu1.getText().toString() != null
				&& textView_cpfenshu1.getText().length() > 0) {
			f1 = Integer.valueOf(textView_cpfenshu1.getText().toString());
			if (f1 > 20) {
				f1 = 20;
				textView_cpfenshu1.setText(String.valueOf(f1));
			}
		} else {
			f1 = 0;
		}
		if (textView_cpfenshu2.getText().toString() != null
				&& textView_cpfenshu2.getText().length() > 0) {
			f2 = Integer.valueOf(textView_cpfenshu2.getText().toString());
			if (f2 > 30) {
				f2 = 30;
				textView_cpfenshu2.setText(String.valueOf(f2));
			}
		} else {
			f2 = 0;
		}
		if (textView_cpfenshu3.getText().toString() != null
				&& textView_cpfenshu3.getText().length() > 0) {
			f3 = Integer.valueOf(textView_cpfenshu3.getText().toString());
			if (f3 > 50) {
				f3 = 50;
				textView_cpfenshu3.setText(String.valueOf(f3));
			}
		} else {
			f3 = 0;
		}
		textView_cpzongfen.setText(String.valueOf(f1 + f2 + f3));

	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i("info", "Tab_Layout5_onResume");

		if ("pinfen".equals(appState.tab5_state)  || "xiaozuyijian".equals(appState.tab5_state)  ) {
			// ���ֽ׶�  С������׶�
			textView_cpzongfen.setText("");
			textView_cpfenshu1.setText("");
			textView_cpfenshu2.setText("");
			textView_cpfenshu3.setText("");
			textView_cppogeyijian.setText("");
			radio_tongyi.setChecked(true);
			
			if ("����".equals(appState.peopleList.get(appState.people_cur).get("lianghua").toString()) ) {
				appState.lianghua = true;
				lianghua.setVisibility(View.VISIBLE);
				bulianghua.setVisibility(View.INVISIBLE);
			} else {
				appState.lianghua = false;
				lianghua.setVisibility(View.INVISIBLE);
				bulianghua.setVisibility(View.VISIBLE);
			}
			
			

			// ׼������
			textView_cpid5.setText(appState.peopleList.get(appState.people_cur).get("id").toString());
			textView_cpname5.setText(appState.peopleList.get(appState.people_cur).get("name").toString());
			textView_cpcompany5.setText(appState.peopleList.get(appState.people_cur).get("company").toString());
			
			getSharePrefe();//�ָ�״̬
			
			// �걨����Ϊ04������Ҫ�Ƹ���������Ҫ�Ƹ�
						if ("04".equals(appState.peopleList.get(appState.people_cur)
								.get("shenbaoleixin").toString())) {
							
							//ȡ���Ƹ���
							textView_pogeyijian.setVisibility(View.GONE);	
							textView_cppogeyijian.setVisibility(View.GONE);
							radioGroup_poge.setVisibility(View.GONE);
							
							//appState.pogebutton = "yes";// ��ʼ��
							appState.pogebutton = "not_use";// ��ʼ��

						} else {
							textView_pogeyijian.setVisibility(View.GONE);
							textView_cppogeyijian.setVisibility(View.GONE);
							radioGroup_poge.setVisibility(View.GONE);
							appState.pogebutton = "not_use";// ��ʼ��

						}
			
			// ����Ѿ����֣�����ʾ����
			appState.getDB();
			cursor = appState.queryTable(appState.peopleList.get(appState.people_cur).get("id").toString());
			if (cursor != null && cursor.getCount() > 0) {// �Ѿ�������
				cursor.moveToNext();
				
				if (appState.lianghua ) {	//����
					textView_cpzongfen.setText(cursor.getString(1));
				} else {	//������
					if ( "yes".equals(cursor.getString(1))) {
						radio_gerentongyi.setChecked(true);
						appState.gerenyijian = "yes";
					} else if ("no".equals(cursor.getString(1)) ) {
						radio_gerenbutongyi.setChecked(true);
						appState.gerenyijian = "no";
					} else {
						radio_gerentongyi.setChecked(true);
						appState.gerenyijian = "yes";
					}
				}
				
				
				textView_cpfenshu1.setText(cursor.getString(6));
				textView_cpfenshu2.setText(cursor.getString(7));
				textView_cpfenshu3.setText(cursor.getString(8));
				textView_cppogeyijian.setText(cursor.getString(2));
				if (cursor.getString(3).equalsIgnoreCase("yes")) {
					radio_tongyi.setChecked(true);
				} else if (cursor.getString(3).equalsIgnoreCase("no")) {
					radio_butongyi.setChecked(true);
				}

				
				//if (Integer.valueOf(cursor.getString(5)) > 0){//�Ѿ��ύ������
				if (!"pinfen".equals(appState.workfloat)){//�������ֽ׶β��ɲ���
					textView_cpfenshu1.setEnabled(false);
					textView_cpfenshu2.setEnabled(false);
					textView_cpfenshu3.setEnabled(false);
					textView_cppogeyijian.setEnabled(false);
					radio_tongyi.setEnabled(false);
					radio_butongyi.setEnabled(false);
					button_pinfensave.setEnabled(false);
					button_chakan1.setEnabled(false);
				}else {//���ֽ׶����ǿ��Բ���
					textView_cpfenshu1.setEnabled(true);
					textView_cpfenshu2.setEnabled(true);
					textView_cpfenshu3.setEnabled(true);
					textView_cppogeyijian.setEnabled(true);
					radio_tongyi.setEnabled(true);
					radio_butongyi.setEnabled(true);					
					button_pinfensave.setEnabled(true);
					button_chakan1.setEnabled(true);
				}
				
			
				//button_pinfensave.setEnabled(false);

				//textView_cppogeyijian.setEnabled(false);
				//radio_tongyi.setEnabled(false);
				//radio_butongyi.setEnabled(false);
			} else {// û������
				textView_cpfenshu1.setEnabled(true);
				textView_cpfenshu2.setEnabled(true);
				textView_cpfenshu3.setEnabled(true);
				button_pinfensave.setEnabled(true);
				textView_cppogeyijian.setEnabled(true);				
				radio_tongyi.setEnabled(true);
				radio_butongyi.setEnabled(true);
				
				button_chakan1.setEnabled(true);
				
				

			}
			cursor.close();
			appState.dbClose();
		} else if ("toupiao".equals(appState.tab5_state)) {// ͶƱ------------------------------

			// �걨����Ϊ04������Ҫ�Ƹ���������Ҫ�Ƹ�
			if ("04".equals(appState.peopleList.get(appState.people_cur)
					.get("shenbaoleixin").toString())) {
				textView_pogejielun.setVisibility(View.VISIBLE);
				textView_cppogejielun.setVisibility(View.VISIBLE);

			} else {

				textView_pogejielun.setVisibility(View.INVISIBLE);
				textView_cppogejielun.setVisibility(View.INVISIBLE);

			}
			
			//���Խ��
			if ("".equals(appState.peopleList.get(appState.people_cur).get("ceshijieguo").toString())) {
				textView_ceshi5.setVisibility(View.INVISIBLE);
				textView_cpceshi5.setVisibility(View.INVISIBLE);
			} else {
				textView_ceshi5.setVisibility(View.VISIBLE);
				textView_cpceshi5.setVisibility(View.VISIBLE);
				}

			getSharePrefe();//�ָ�״̬
			// ��ȡͶƱ״̬
			appState.getDB();
			cursor = appState.queryTable(appState.peopleList.get(appState.people_cur).get("id").toString());
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToNext();
				appState.voteState = cursor.getString(4);// ��ȡͶƱͬ�� �޳� ���� δͶƱ
				/*
				if(cursor.getString(4).equalsIgnoreCase("ͬ��")){
					radioButton_agree.setChecked(true);
				}else if(cursor.getString(4).equalsIgnoreCase("����")){
					radioButton_against.setChecked(true);
				}else {
					radioButton_waiver.setChecked(true);
				}
				*/
				
			} else {
				appState.voteState = "δͶƱ";
				
				
			}
			cursor.close();
			//appState.dbClose();

			update();
			// appState.isUpdated = false;
			Log.i("info", "upDated");

			//appState.getDB();
			//cursor = appState.queryTable_tijiaostate("3");
			//if (cursor == null || cursor.getCount() == 0) {//û���ύ
			if ("toupiao".equals(appState.workfloat)) {//ͶƱ�׶ζ����Բ���
				
				radioButton_agree.setEnabled(true);
				radioButton_against.setEnabled(true);
				radioButton_waiver.setEnabled(true);

				//radioButton_agree.setChecked(false);
				//radioButton_against.setChecked(false);
				radioButton_waiver.setChecked(true);// Ĭ��
			} else {
				radioButton_agree.setEnabled(false);
				radioButton_against.setEnabled(false);
				radioButton_waiver.setEnabled(false);

				
			}
			cursor.close();
			appState.dbClose();
			
			// ͶƱͬ�� ���� ��Ȩ δͶƱ
			if ("�޳�".equals(appState.voteState)) {
				radioButton_agree.setChecked(true);
			} else if ("����".equals(appState.voteState)) {
				radioButton_against.setChecked(true);
			} else if ("��Ȩ".equals(appState.voteState)) {
				radioButton_waiver.setChecked(true);
			}
			
			
		}//////endtoupiao
		else if ("xiaozuyijian".equals(appState.tab5_state)) {// С�����------------------------------

			// �걨����Ϊ04������Ҫ�Ƹ���������Ҫ�Ƹ�
			if ("04".equals(appState.peopleList.get(appState.people_cur)
					.get("shenbaoleixin").toString())) {
				textView_pogejielun.setVisibility(View.VISIBLE);
				textView_cppogejielun.setVisibility(View.VISIBLE);

			} else {

				textView_pogejielun.setVisibility(View.INVISIBLE);
				textView_cppogejielun.setVisibility(View.INVISIBLE);

			}
			
			//���Խ��
			if ("".equals(appState.peopleList.get(appState.people_cur).get("ceshijieguo").toString())) {
				textView_ceshi5.setVisibility(View.INVISIBLE);
				textView_cpceshi5.setVisibility(View.INVISIBLE);
			} else {
				textView_ceshi5.setVisibility(View.VISIBLE);
				textView_cpceshi5.setVisibility(View.VISIBLE);
				}

			//getSharePrefe();//�ָ�״̬

			update();
			// appState.isUpdated = false;
			Log.i("info", "upDated");		
		
			
		}
		
		
	}

	private void update() {
		// TODO Auto-generated method stub
		// ׼������
		textView_cpid5.setText(appState.peopleList.get(appState.people_cur).get("id").toString());
		textView_cpname5.setText(appState.peopleList.get(appState.people_cur).get("name").toString());
		textView_cpcompany5.setText(appState.peopleList.get(appState.people_cur).get("company").toString());
		textView_cpvotestate5.setText(appState.voteState);
		
		
		
		
		if ("04".equals(appState.peopleList.get(appState.people_cur).get("shenbaoleixin").toString())) {
			textView_cppogejielun.setText(appState.scoreList.get(appState.people_cur).get("pogejielun").toString());
		}
		
		
		
		if (appState.xianchangfenzu){
			textView_cppinjunfen.setText(appState.scoreList.get(appState.people_cur).get("group_score").toString());
		}else{
			textView_cppinjunfen.setText(appState.scoreList.get(appState.people_cur).get("pinjunfen").toString());
		}
		
		
		if ("".equals(appState.peopleList.get(appState.people_cur).get("ceshijieguo").toString())){
			textView_cpceshi5.setText(appState.peopleList.get(appState.people_cur).get("ceshijieguo").toString());
    	}else if ("F".equals(appState.peopleList.get(appState.people_cur).get("ceshijieguo").toString())){
    		textView_cpceshi5.setText("���ϸ�");
    	}else{
    		textView_cpceshi5.setText("�ϸ�");
    	}
		
			
		textView_cpenglish5.setText(appState.peopleList.get(appState.people_cur).get("waiyu").toString());
		textView_cpcomputer5.setText(appState.peopleList.get(appState.people_cur).get("jisuanji").toString());
	}

	
	public String xiaozuyijian ="";
	//С�����ͨ��radiobutton����¼�
	public void agree_yijian_onclick(View target) {
		Log.i("info", "����ͨ��");
		//button_vote.setEnabled(true);
		//button_cancelvote.setEnabled(true);
		xiaozuyijian = "����ͨ��";// �޳�
	}
	
	//С�������ͨ��radiobutton����¼�
		public void against_yijian_onclick(View target) {
			Log.i("info", "���鲻ͨ��");
			//button_vote.setEnabled(true);
			//button_cancelvote.setEnabled(true);
			xiaozuyijian = "���鲻ͨ��";// �޳�
		}
	
	// �޳�radiobutton����¼�
	public void radioButton_agree_onclick(View target) {
		Log.i("info", "����޳�");
		//button_vote.setEnabled(true);
		//button_cancelvote.setEnabled(true);
		appState.voteState = "�޳�";// �޳�
	}

	// ����radiobutton����¼�
	public void radioButton_against_onclick(View target) {
		Log.i("info", "�������");
		//button_vote.setEnabled(true);
		//button_cancelvote.setEnabled(true);
		appState.voteState = "����";// ����
	}

	// ��Ȩradiobutton����¼�
	public void radioButton_waiver_onclick(View target) {
		Log.i("info", "�����Ȩ");
		//button_vote.setEnabled(true);
		//button_cancelvote.setEnabled(true);
		appState.voteState = "��Ȩ";// ��Ȩ
	}
	
	// С��������水ť����¼�
		public void xiaozuyijian_onclick(View target) {
			Log.i("info", "�ύС�����");
			if ("���ճɹ�".equals(submitYijian()) ){
			
			Intent it = new Intent(this, searchActivity.class);
			startActivity(it);
			this.finish();
			}
		}
	
	
	// ͶƱ��ť����¼�
	public void vote_onclick(View target) {
		Log.i("info", "���ͶƱ");

		if (!"δͶƱ".equals(appState.voteState)) {// �Ѿ�ͶƱ
			// д���ݿ�
			appState.getDB();
			
			//�����ݿ��������
			cursor = null;
			cursor = appState.queryTable(appState.peopleList.get(appState.people_cur).get("id").toString());
			if (cursor!=null && cursor.getCount()>0){
				//�ҵ��˾͸���
				// ͶƱ0 1 2  �޳� ���� ��Ȩ
				appState.Update_toupiao(appState.peopleList.get(appState.people_cur).get("id").toString(),
						appState.voteState, // �ύͶƱ
						"2"); //�ύ״̬������/�ύ����/����ͶƱ/�ύͶƱ��0 1 2 3
			}else{
				//�Ҳ��������
				appState.add(appState.peopleList.get(appState.people_cur).get("id").toString(), 
						appState.scoreList.get(appState.people_cur).get("pinjunfen").toString(), 
						"", 
						appState.scoreList.get(appState.people_cur).get("pogejielun").toString(), 
						appState.voteState, "2", "", "", "");
			}
			
			textView_cpvotestate5.setText(appState.voteState);

			Toast toast = Toast.makeText(getApplicationContext(), "����ɹ���",
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			
			//����title----------
			int num1 = 0;
			int num2 = 0;
			
	    	Cursor cursor1 = null;
	    	if ("pinfen".equals(appState.workfloat)){
	    		cursor1 = appState.getAll();
	    		if (cursor1 != null ){
	    			num1 = cursor1.getCount();
	    			cursor1.close();
	    		}
	    	}else if ("toupiao".equals(appState.workfloat)){
	    		cursor1 = appState.queryTable_tijiaostate("2");
	    		if (cursor1 != null ){
					num1 = cursor1.getCount();
					cursor1.close();
	    		}
	    		cursor1 = appState.queryTable_tijiaostate("3");
	    		if (cursor1 != null ){
					num2 = cursor1.getCount();
					cursor1.close();
	    		}
	    		
	    		num1 += num2;
	    	}
	    	
	    	this.getParent().setTitle("��ǰ������Ա��" + String.valueOf(appState.people_cur + 1) + 
					" �� " + 
					"���в�����Ա��" + String.valueOf(appState.people_total) + 
					"����ǰ�ѱ��棺" + String.valueOf(num1)
					);
	    	
	    
			//------------------
			//appState.getDB();
		 	//cursor = appState.queryTable_tijiaostate("2");
			if (num1 >= appState.people_total) {
				//cursor.close();
				appState.dbClose();
				button_chakan2_onclick(target);
				//�ύ���ݲ��ַŵ��鿴ҳ����
				/*
				// ���� ��ʾ�Ƿ��ύ����
				new AlertDialog.Builder(this)
						.setTitle("�Ƿ��ύͶƱ���ݣ�")
						.setMessage("���в�����Ա�Ѿ�ͶƱ��ɡ�\n�����ȷ������ť�ύͶƱ����������������\n��������ء���ť���Լ����޸ġ�\nע�⣺ѡ��ȷ��֮�󣬲����޸Ĳ�����Ա��ͶƱ��Ϣ��")
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
											String tmp = submitToupiao();
											 if ("���ճɹ�".equals(tmp)){ // ״̬��Ϊ���ύ���� // �ύ״̬��δ�ύ/�ύ����/�ύͶƱ��0 1 2  3
												 appState.dbClose();
												 appState.getDB();
												 appState.Update_tijiao(appState.peopleList.get(appState.people_cur).get("id").toString(), "3");// �ύͶƱ


													Toast toast = Toast.makeText(getApplicationContext(),
															appState.peopleList.get(appState.people_cur).get("id").toString() + "�ύ�ɹ���", 
															Toast.LENGTH_SHORT);
													toast.setGravity(Gravity.CENTER, 0,0);
													toast.cancel();
													toast.show();
											} else if ("����ʧ��".equals(tmp)) {
												Toast toast = Toast.makeText(getApplicationContext(),
														appState.peopleList.get(appState.people_cur).get("id").toString() + "����������ʧ�ܣ��������ύ��",
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
			/*
			// �ύ����
			String tmp = submitToupiao();
			
			if ("���ճɹ�".equals(tmp)) {
				// ״̬��Ϊ���ύͶƱ
				// �ύ״̬��δ�ύ/�ύ����/�ύͶƱ��0 1 2
				appState.Update_toupiao(
						appState.peopleList.get(appState.people_cur).get("id")
								.toString(), appState.voteState, // �ύͶƱ
						"2"); // ��ʱ��Ϊ���ύͶƱ

				button_vote.setEnabled(false);
				button_cancelvote.setEnabled(false);

				radioButton_agree.setEnabled(false);
				radioButton_against.setEnabled(false);
				radioButton_waiver.setEnabled(false);

				textView_cpvotestate5.setText(appState.voteState);

				appState.dbClose();
				Toast toast = Toast.makeText(getApplicationContext(), "ͶƱ�ɹ���",
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
			*/

		} else {
			Toast toast = Toast.makeText(getApplicationContext(), "��ͶƱ��",
					Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
/*
	// ȡ����ť����¼�
	public void cancelvote_onclick(View target) {
		Log.i("info", "���ȡ��");

		button_vote.setEnabled(false);
		button_cancelvote.setEnabled(false);

		radioButton_agree.setChecked(false);
		radioButton_against.setChecked(false);
		radioButton_waiver.setChecked(false);
	}
*/
	// �Ƹ�ͬ��radio����¼�
	public void radio_tongyi_onclick(View target) {
		Log.i("info", "���ͬ��");
		appState.pogebutton = "yes";// ͬ��
	}

	// �Ƹ�ͬ��radio����¼�
	public void radio_butongyi_onclick(View target) {
		Log.i("info", "�����ͬ��");
		appState.pogebutton = "no";// ��ͬ��
	}
	
	// ����ͬ��radio����¼�
		public void radio_gerentongyi_onclick(View target) {
			Log.i("info", "���ͬ��");
			appState.gerenyijian = "yes";// ͬ��
		}

		// ������ͬ��radio����¼�
		public void radio_gerenbutongyi_onclick(View target) {
			Log.i("info", "�����ͬ��");
			appState.gerenyijian = "no";// ��ͬ��
		}

	// �鿴����
	public void button_zhengce5_onclick(View target) throws Exception {
		String localPath = "sites/default/files/zhengce/";
		String fileName = "";

		fileName = appState.selectZhence();

		// appState.dl_file(localPath, fileName);

		try {
			InputStream in = getResources().getAssets().open(fileName);
			// д������
			FileUtils file = new FileUtils();
			file.writeFromInput(appState.SDpath + "psxt/" + localPath,
					fileName, in);

		} catch (Exception e) {
			e.printStackTrace();
		}

		SendIntent SDintent = new SendIntent();
		Intent it = SDintent.getIntent(appState.SDpath + "psxt/" + localPath
				+ fileName);
		try {
			startActivity(it);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("error", e.toString());
			Toast toast = Toast.makeText(getApplicationContext(),
					"û�а�װ�Ķ���/���������û��������ߣ�", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

	// �鿴����
		public void button_zhengce5_2_onclick(View target) throws Exception {
			String localPath = "sites/default/files/zhengce/";
			String fileName = "";

			fileName = appState.selectZhence();

			// appState.dl_file(localPath, fileName);

			try {
				InputStream in = getResources().getAssets().open(fileName);
				// д������
				FileUtils file = new FileUtils();
				file.writeFromInput(appState.SDpath + "psxt/" + localPath,
						fileName, in);

			} catch (Exception e) {
				e.printStackTrace();
			}

			SendIntent SDintent = new SendIntent();
			Intent it = SDintent.getIntent(appState.SDpath + "psxt/" + localPath
					+ fileName);
			try {
				startActivity(it);
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("error", e.toString());
				Toast toast = Toast.makeText(getApplicationContext(),
						"û�а�װ�Ķ���/���������û��������ߣ�", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		}
		
		
		/*
	// �鿴���ֱ�׼
	public void button_biaozhun_onclick(View target) {
		String localPath = "sites/default/files/biaozhun/";
		String fileName = "pinfenbiaozhun1.jpg";

		try {
			InputStream in = getResources().getAssets().open(fileName);
			// д������
			FileUtils file = new FileUtils();
			file.writeFromInput(appState.SDpath + "psxt/" + localPath,
					fileName, in);

		} catch (Exception e) {
			e.printStackTrace();
		}

		SendIntent SDintent = new SendIntent();
		Intent it = SDintent.getIntent(appState.SDpath + "psxt/" + localPath
				+ fileName);
		try {
			startActivity(it);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("error", e.toString());
			Toast toast = Toast.makeText(getApplicationContext(),
					"û�а�װ�Ķ������ļ���ʽ����ȷ��", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
	
	// �鿴���ֱ�׼
		public void button_biaozhun2_onclick(View target) {
			String localPath = "sites/default/files/biaozhun/";
			String fileName = "pinfenbiaozhun2.jpg";

			try {
				InputStream in = getResources().getAssets().open(fileName);
				// д������
				FileUtils file = new FileUtils();
				file.writeFromInput(appState.SDpath + "psxt/" + localPath,
						fileName, in);

			} catch (Exception e) {
				e.printStackTrace();
			}

			SendIntent SDintent = new SendIntent();
			Intent it = SDintent.getIntent(appState.SDpath + "psxt/" + localPath
					+ fileName);
			try {
				startActivity(it);
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("error", e.toString());
				Toast toast = Toast.makeText(getApplicationContext(),
						"û�а�װ�Ķ������ļ���ʽ����ȷ��", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		}
		
		// �鿴���ֱ�׼
		public void button_biaozhun3_onclick(View target) {
			String localPath = "sites/default/files/biaozhun/";
			String fileName = "pinfenbiaozhun3.jpg";

			try {
				InputStream in = getResources().getAssets().open(fileName);
				// д������
				FileUtils file = new FileUtils();
				file.writeFromInput(appState.SDpath + "psxt/" + localPath,
						fileName, in);

			} catch (Exception e) {
				e.printStackTrace();
			}

			SendIntent SDintent = new SendIntent();
			Intent it = SDintent.getIntent(appState.SDpath + "psxt/" + localPath
					+ fileName);
			try {
				startActivity(it);
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("error", e.toString());
				Toast toast = Toast.makeText(getApplicationContext(),
						"û�а�װ�Ķ������ļ���ʽ����ȷ��", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		}
		*/
		

	// �ύ���ݵ���¼�
	public void button_pinfensave_onclick(View target) {
		
		Log.i("info", "�������");
		
		appState.getDB();
		

		// �걨����Ϊ04������Ҫ�Ƹ���������Ҫ�Ƹ�
		//�ĳɲ����Ƹ�
//		if ("04".equals(appState.peopleList.get(appState.people_cur).get("shenbaoleixin").toString())) {
//			// �Ƹ�
//			if (textView_cpfenshu1.getText() != null
//					&& textView_cpfenshu1.length() > 0
//					&& Integer.valueOf(textView_cpfenshu1.getText().toString()) <= 20
//					&& Integer.valueOf(textView_cpfenshu1.getText().toString()) >= 0
//					&& textView_cpfenshu2.getText() != null
//					&& textView_cpfenshu2.length() > 0
//					&& Integer.valueOf(textView_cpfenshu2.getText().toString()) <= 30
//					&& Integer.valueOf(textView_cpfenshu2.getText().toString()) >= 0
//					&& textView_cpfenshu3.getText() != null
//					&& textView_cpfenshu3.length() > 0
//					&& Integer.valueOf(textView_cpfenshu3.getText().toString()) <= 50
//					&& Integer.valueOf(textView_cpfenshu3.getText().toString()) >= 0
//					&& textView_cppogeyijian.getText() != null
//					&& textView_cppogeyijian.length() > 0) {
//				// д���ݿ�
//				cursor = appState.queryTable(appState.peopleList.get(appState.people_cur).get("id").toString());
//				if (cursor == null || cursor.getCount() == 0) {
//					// ���û�б�����������
//					appState.add(appState.peopleList.get(appState.people_cur).get("id").toString(), // id
//							textView_cpzongfen.getText().toString(), // ����
//							textView_cppogeyijian.getText().toString(), // �Ƹ����
//							appState.pogebutton, // �Ƹ�ѡ��ť
//							"δͶƱ", "0",// �ύ״̬������/�ύ����/����ͶƱ/�ύͶƱ��0 1 2 3
//							textView_cpfenshu1.getText().toString(),// ����1
//							textView_cpfenshu2.getText().toString(),// ����2
//							textView_cpfenshu3.getText().toString()// ����3
//							);
//				} else {
//					// �������������޸�
//					appState.Update_people(appState.peopleList.get(appState.people_cur).get("id").toString(), // id
//							textView_cpzongfen.getText().toString(), // ����
//							textView_cppogeyijian.getText().toString(), // �Ƹ����
//							appState.pogebutton, // �Ƹ�ѡ��ť
//							"δͶƱ", "0",// �ύ״̬������/�ύ����/����ͶƱ/�ύͶƱ��0 1 2 3
//							textView_cpfenshu1.getText().toString(),// ����1
//							textView_cpfenshu2.getText().toString(),// ����2
//							textView_cpfenshu3.getText().toString()// ����3
//							);
//				}
//				Toast toast = Toast.makeText(getApplicationContext(), "����ɹ���",
//						Toast.LENGTH_SHORT);
//				toast.setGravity(Gravity.CENTER, 0, 0);
//				toast.show();
//
//				cursor.close();
//				
//				// button_pinfensave.setEnabled(false);
//
//				/*
//				 * // �ύ���� String tmp = submitPinfen();
//				 * 
//				 * if ("���ճɹ�".equals(tmp)){ // ״̬��Ϊ���ύ���� // �ύ״̬��δ�ύ/�ύ����/�ύͶƱ��0
//				 * 1 2 appState.Update_tijiao(appState.peopleList.get(appState.
//				 * people_cur) .get("id").toString(), "1");// �ύ����
//				 * 
//				 * appState.dbClose();
//				 * 
//				 * button_pinfensave.setEnabled(false);
//				 * textView_cpzongfen.setEnabled(false);
//				 * textView_cppogeyijian.setEnabled(false);
//				 * radio_tongyi.setEnabled(false);
//				 * radio_butongyi.setEnabled(false);
//				 * 
//				 * Toast toast = Toast.makeText(getApplicationContext(),
//				 * "�ύ�ɹ���", Toast.LENGTH_LONG); toast.setGravity(Gravity.CENTER,
//				 * 0, 0); toast.show(); }else if("����ʧ��".equals(tmp)){ Toast
//				 * toast = Toast.makeText(getApplicationContext(),
//				 * "����������ʧ�ܣ��������ύ��", Toast.LENGTH_LONG);
//				 * toast.setGravity(Gravity.CENTER, 0, 0); toast.show(); }
//				 */
//
//			} else {
//				Toast toast = Toast.makeText(getApplicationContext(),
//						"����д��ȷ��Ϣ��", Toast.LENGTH_LONG);
//				toast.setGravity(Gravity.CENTER, 0, 0);
//				toast.show();
//			}
//		} else {
			// ���Ƹ�
		
		if (appState.lianghua ) {	//��������ģʽ
			if (textView_cpfenshu1.getText() != null
					&& textView_cpfenshu1.length() > 0
					&& Integer.valueOf(textView_cpfenshu1.getText().toString()) <= 20
					&& Integer.valueOf(textView_cpfenshu1.getText().toString()) >= 0
					&& textView_cpfenshu2.getText() != null
					&& textView_cpfenshu2.length() > 0
					&& Integer.valueOf(textView_cpfenshu2.getText().toString()) <= 30
					&& Integer.valueOf(textView_cpfenshu2.getText().toString()) >= 0
					&& textView_cpfenshu3.getText() != null
					&& textView_cpfenshu3.length() > 0
					&& Integer.valueOf(textView_cpfenshu3.getText().toString()) <= 50
					&& Integer.valueOf(textView_cpfenshu3.getText().toString()) >= 0) {
				// д���ݿ�
				//appState.getDB();
				cursor = appState.queryTable(appState.peopleList.get(appState.people_cur).get("id").toString());
				if (cursor == null || cursor.getCount() == 0) {
					// ����]�����^�����
					appState.add(appState.peopleList.get(appState.people_cur).get("id").toString(), // id
							textView_cpzongfen.getText().toString(), // ����
							textView_cppogeyijian.getText().toString(), // �Ƹ����
							appState.pogebutton, // �Ƹ�ѡ��ť
							"δͶƱ", "0", // �ύ״̬������/�ύ����/����ͶƱ/�ύͶƱ��0 1 2 3
							textView_cpfenshu1.getText().toString(),// ����1
							textView_cpfenshu2.getText().toString(),// ����2
							textView_cpfenshu3.getText().toString()// ����3
							);
				}else{
					//�������^������
					appState.Update_people(appState.peopleList.get(appState.people_cur).get("id").toString(), // id
							textView_cpzongfen.getText().toString(), // ����
							textView_cppogeyijian.getText().toString(), // �Ƹ����
							appState.pogebutton, // �Ƹ�ѡ��ť
							"δͶƱ", "0",// �ύ״̬������/�ύ����/����ͶƱ/�ύͶƱ��0 1 2 3
							textView_cpfenshu1.getText().toString(),// ����1
							textView_cpfenshu2.getText().toString(),// ����2
							textView_cpfenshu3.getText().toString()// ����3
							);
				}
				Toast toast = Toast.makeText(getApplicationContext(), "����ɹ���",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				
				cursor.close();

				//appState.dbClose();
				// button_pinfensave.setEnabled(false);

				/*
				 * // �ύ���� String tmp = submitPinfen();
				 * 
				 * if ("���ճɹ�".equals(tmp)){ // ״̬��Ϊ���ύ���� // �ύ״̬��δ�ύ/�ύ����/�ύͶƱ��0
				 * 1 2 appState.Update_tijiao(appState.peopleList.get(appState.
				 * people_cur) .get("id").toString(), "1");// �ύ����
				 * 
				 * appState.dbClose();
				 * 
				 * button_pinfensave.setEnabled(false);
				 * textView_cpzongfen.setEnabled(false);
				 * textView_cppogeyijian.setEnabled(false);
				 * radio_tongyi.setEnabled(false);
				 * radio_butongyi.setEnabled(false);
				 * 
				 * Toast toast = Toast.makeText(getApplicationContext(),
				 * "�ύ�ɹ���", Toast.LENGTH_LONG); toast.setGravity(Gravity.CENTER,
				 * 0, 0); toast.show(); }else if("����ʧ��".equals(tmp)){ Toast
				 * toast = Toast.makeText(getApplicationContext(),
				 * "����������ʧ�ܣ��������ύ��", Toast.LENGTH_LONG);
				 * toast.setGravity(Gravity.CENTER, 0, 0); toast.show(); }
				 */

			} else {
				Toast toast = Toast.makeText(getApplicationContext(),
						"����д��ȷ��Ϣ��", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		} else {	//����Ҫ��������
			// д���ݿ�
			//appState.getDB();
			cursor = appState.queryTable(appState.peopleList.get(appState.people_cur).get("id").toString());
			if (cursor == null || cursor.getCount() == 0) {
				// ����]�����^�����
				appState.add(appState.peopleList.get(appState.people_cur).get("id").toString(), // id
						//textView_cpzongfen.getText().toString(), // ����
						appState.gerenyijian,	//���������֣��ܷ��ֶ������������
						textView_cppogeyijian.getText().toString(), // �Ƹ����
						appState.pogebutton, // �Ƹ�ѡ��ť
						"δͶƱ", "0", // �ύ״̬������/�ύ����/����ͶƱ/�ύͶƱ��0 1 2 3
						"0",// ����1
						"0",// ����2
						"0"// ����3
						);
			}else{
				//�������^������
				appState.Update_people(appState.peopleList.get(appState.people_cur).get("id").toString(), // id
						//textView_cpzongfen.getText().toString(), // ����
						appState.gerenyijian,	//���������֣��ܷ��ֶ������������
						textView_cppogeyijian.getText().toString(), // �Ƹ����
						appState.pogebutton, // �Ƹ�ѡ��ť
						"δͶƱ", "0",// �ύ״̬������/�ύ����/����ͶƱ/�ύͶƱ��0 1 2 3
						"0",// ����1
						"0",// ����2
						"0"// ����3
						);
			}
			Toast toast = Toast.makeText(getApplicationContext(), "����ɹ���",
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			
			cursor.close();
		}
			
//		}// end ���Ƹ�

		
		//����title----------
				int num1 = 0;
				int num2 = 0;
				
		    	Cursor cursor1;
		    	if ("pinfen".equals(appState.workfloat)){
		    		cursor1 = appState.getAll();
		    		num1 = cursor1.getCount();
		    		cursor1.close();
		    	}else if ("toupiao".equals(appState.workfloat)){
		    		cursor1 = appState.queryTable_tijiaostate("2");
		    		num1 = cursor1.getCount();
		    		cursor1.close();
		    		cursor1 = appState.queryTable_tijiaostate("3");
		    		num2 = cursor1.getCount();
		    		cursor1.close();
		    		
		    		num1 += num2;
		    	}
		    	this.getParent().setTitle("��ǰ������Ա��" + String.valueOf(appState.people_cur + 1) + 
						" �� " + 
						"���в�����Ա��" + String.valueOf(appState.people_total) + 
						"����ǰ�ѱ��棺" + String.valueOf(num1)
						);
		    	
		    
				//------------------
		 
		    	
		    	if (appState.xianchangfenzu ){//����������ֳ�����ģʽ
		    		cursor = appState.queryTable(appState.peopleList.get(appState.people_cur).get("id").toString());
		    		
		    		if (cursor != null && cursor.getCount() > 0) {
		    			cursor.close();
		    			submitPinfen();
		    		
					
					//��ȡ��������������
//					 getWokfloat();
//					 
//					 if ("pinfen".equals(appState.workfloat)){
//						 Intent it = new Intent(this, searchActivity.class);
//							startActivity(it);
//							finish();
//					 }else if ("xiaozuyijian".equals(appState.workfloat)){
//							finish();
//					 }
					
		    			Intent it = new Intent(this, searchWithListActivity.class);
		    			startActivity(it);
		    			finish();
		    		}
					
				}else {
		cursor = appState.getAll();
		if (cursor.getCount() >= appState.people_total) {
			cursor.close();
			appState.dbClose();
			button_chakan1_onclick(target);
			//�ύ���ݲ��ַŵ��鿴ҳ����
			/*
			// ���� ��ʾ�Ƿ��ύ����
			new AlertDialog.Builder(this)
					.setTitle("�Ƿ��ύ�������ݣ�")
					.setMessage("���в�����Ա�Ѿ�������ɡ�\n�����ȷ������ť�ύ�������ݵ������������\n��������ء���ť���Լ����޸ġ�\nע�⣺ѡ��ȷ��֮�󣬲����޸Ĳ�����Ա�ĸ������ݣ�")
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
									for (appState.people_cur = 0 ; appState.people_cur<appState.people_total; appState.people_cur++){
										// �ύ���� 
										String tmp = submitPinfen();
										 if ("���ճɹ�".equals(tmp)){ // ״̬��Ϊ���ύ���� // �ύ״̬��δ�ύ/�ύ����/�ύͶƱ��0 1 2  3
											 appState.dbClose();
											 appState.getDB();
											 appState.Update_tijiao(appState.peopleList.get(appState.people_cur).get("id").toString(), "1");// �ύ����
											
											Toast toast = Toast.makeText(getApplicationContext(),
													appState.peopleList.get(appState.people_cur).get("id").toString() + "�ύ�ɹ���", 
													Toast.LENGTH_SHORT);
											toast.setGravity(Gravity.CENTER, 0,0);
											toast.cancel();
											toast.show();
											
										} else if ("����ʧ��".equals(tmp)) {
											Toast toast = Toast.makeText(getApplicationContext(),
													appState.peopleList.get(appState.people_cur).get("id").toString() + "����������ʧ�ܣ��������ύ��",
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
				}

		cursor.close();
		appState.dbClose();

	
		// ���û�����һ����������һ��
		
		
		
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
					Toast toast = Toast.makeText(getApplicationContext(),
							line, Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();

					line = in.readLine();
				}
				in.close();
				conn.disconnect();
				
				if ("��������".equals(tmp)){
					appState.workfloat = "pinfen";
				}else if ("����д���".equals(tmp)){
					appState.workfloat = "xiaozuyijian";
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
	
	private String submitYijian(){
		
		String pwhid = appState.pwhid;// ��ί��
		String pwid = appState.pinweiName;// ��ί
		String id = appState.peopleList.get(appState.people_cur).get("id").toString();// ������

		String tmp = "����ʧ��";
		

		StringBuilder dataTransformb = new StringBuilder();
		dataTransformb.append( "pwhid=" + URLEncoder.encode(pwhid) //��ί��
				+ "&pwid=" + URLEncoder.encode(pwid)	// ��ί
				+ "&data=[");

			//�ϳ��ύ����,����ʱ��ʱ����
			
			dataTransformb.append( "{\"id\":\"" + URLEncoder.encode(id) + "\","// ������
					+ "\"opinion\":\"" + URLEncoder.encode(xiaozuyijian) + "\""// С�����
					+ "}]");
	
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
					out.writeBytes(dataTransformb.toString());
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
	
	private String submitPinfen() {
		// TODO Auto-generated method stub
		/*
		 * (��ί�ᣩpwhid ����ίid��pwid (�����ˣ�id 10λid
		 * 
		 * ���Ƹ�poge yes no ��ѡһ (�Ƹ����ݣ�content �ַ���
		 * 
		 * ���ܷ֣�total 0~100������
		 */
		appState.getDB();
		cursor = appState.queryTable(appState.peopleList.get(appState.people_cur).get("id").toString());
		cursor.moveToNext();
		
		String pwhid = appState.pwhid;// ��ί��
		String pwid = appState.pinweiName;// ��ί
		String id = appState.peopleList.get(appState.people_cur).get("id").toString();// ������
		String poge = cursor.getString(3);
		String content = cursor.getString(2);
		String total = cursor.getString(1);
		String geren = ""; //�������
		String tmp = "����ʧ��";
		
		
		cursor.close();
		appState.dbClose();
		
		if (appState.lianghua) {	//��������ģʽ
			
		} else {	//����������ģʽ
			geren = total;	//����������ģʽ�����ݿ��ܷ�total�ֶδ���Ǹ������ yes��no
			total = "0";
		}

		StringBuilder dataTransformb = new StringBuilder();
		dataTransformb.append( "pwhid=" + URLEncoder.encode(pwhid) //��ί��
				+ "&pwid=" + URLEncoder.encode(pwid)	// ��ί
				+ "&data=[");

			//�ϳ��ύ����,����ʱ��ʱ����
			
			dataTransformb.append( "{\"id\":\"" + URLEncoder.encode(id) + "\","// ������
					+ "\"poge\":\"" + URLEncoder.encode(poge) + "\","// �Ƹ�
					+ "\"content\":\"" + URLEncoder.encode(content) + "\","  //�Ƹ����
					+ "\"gerenyijian\":\"" + URLEncoder.encode(geren) + "\","  //�������
					+ "\"total\":\"" + URLEncoder.encode(total) + "\""  //�ܷ�
					+ "}]");
		
			// Ҫ���ʵ�web servlet
			// ע�⣺IP�Ͷ˿��Ǳ��ص� ��Ҫ�������IP�Ͷ˿�
			String servletUrl = appState.HttpHead + "/expert/score";
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
					out.writeBytes(dataTransformb.toString());
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

	private String submitToupiao() {
		// TODO Auto-generated method stub
		/*
		 * (��ί�ᣩpwhid ����ίid��pwid (�����ˣ�id 10λid (ͶƱ��toupiao �޳� ���� ��Ȩ ��ѡ1
		 */
		appState.getDB();
		cursor = appState.queryTable(appState.peopleList.get(appState.people_cur).get("id").toString());
		cursor.moveToNext();
		
		String pwhid = appState.pwhid;// ��ί��
		String pwid = appState.pinweiName;// ��ί
		String id = appState.peopleList.get(appState.people_cur).get("id").toString();// ������
		String vote = cursor.getString(4);
		
		
		cursor.close();
		appState.dbClose();

		String tmp = "����ʧ��";

		// Ҫ���ʵ�web servlet
		// ע�⣺IP�Ͷ˿��Ǳ��ص� ��Ҫ�������IP�Ͷ˿�
		String servletUrl = appState.HttpHead + "/expert/vote";
		// ����������������
		String params = "pwhid=" + URLEncoder.encode(pwhid) + 
				"&" + "pwid=" + URLEncoder.encode(pwid) + 
				"&" + "id=" + URLEncoder.encode(id) + 
				"&" + "toupiao=" + URLEncoder.encode(vote);

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
	
	//�鿴��ť
	public void button_chakan1_onclick(View target) {
		appState.dbClose();
		Intent it = new Intent(this, chakanActivity.class);
		
		Bundle bundle = new Bundle();
		bundle.putString("leixin","pinfen");
		it.putExtras(bundle);
		
		startActivity(it);
	}
	public void button_chakan2_onclick(View target) {
		appState.dbClose();
		Intent it = new Intent(this, chakanActivity.class);
		
		Bundle bundle = new Bundle();
		bundle.putString("leixin","toupiao");
		it.putExtras(bundle);
		
		startActivity(it);
	}

	// �����f����ť����¼�
		public void button_czsm5_onclick(View target) {
			appState.launch_help();
		}
		
		// �����f����ť����¼�
		public void button_czsm5_2_onclick(View target) {
			appState.launch_help();
		}
		
		
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// ���¼����Ϸ��ذ�ť
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				System.out.println("���ذ�ť");
				if (appState.workfloat.equals("pinfen") && appState.xianchangfenzu) {
					Intent it = new Intent(this, searchWithListActivity.class);
	    			startActivity(it);
	    			finish();
				}
				return true;
			} else {
				return super.onKeyDown(keyCode, event);
			}
		}
}
