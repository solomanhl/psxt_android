/**
 * 评分和投票
 * 
 * @author 贺亮
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
	private Global_var appState; // 获得全局变量;

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
	private TextView textView_cppogejielun;// 破格结论
	private TextView textView_cppinjunfen;// 平均分
	private TextView textView_ceshi5;
	private TextView textView_cpceshi5;// 测试结果
	private TextView textView_cpenglish5;// 英语成绩
	private TextView textView_cpcomputer5;// 计算机
	
	private Button button_chakan1,button_chakan2;//查看详细

	private Cursor cursor;

	private String state;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		appState = ((Global_var) getApplicationContext()); // 获得全局变量
		super.onCreate(savedInstanceState);

		if ("pinfen".equals(appState.tab5_state)) {
			// 评分阶段
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
			// 投票阶段
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

			textView_pogejielun = (TextView) findViewById(R.id.textView_pogejielun);// 破格结论
			textView_cppogejielun = (TextView) findViewById(R.id.textView_cppogejielun);// 破格结论
			textView_cppinjunfen = (TextView) findViewById(R.id.textView_cppinjunfen);// 平均分
			textView_ceshi5 = (TextView) findViewById(R.id.textView_ceshi5);// 测试结果
			textView_cpceshi5 = (TextView) findViewById(R.id.textView_cpceshi5);// 测试结果
			textView_cpenglish5 = (TextView) findViewById(R.id.textView_cpenglish5);// 英语成绩
			textView_cpcomputer5 = (TextView) findViewById(R.id.textView_cpcomputer5);// 计算机
			
			
		}else if ("xiaozuyijian".equals(appState.tab5_state)) {
			// 小组意见阶段
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

			textView_pogejielun = (TextView) findViewById(R.id.textView_pogejielun);// 破格结论
			textView_cppogejielun = (TextView) findViewById(R.id.textView_cppogejielun);// 破格结论
			textView_cppinjunfen = (TextView) findViewById(R.id.textView_cppinjunfen);// 平均分
			textView_ceshi5 = (TextView) findViewById(R.id.textView_ceshi5);// 测试结果
			textView_cpceshi5 = (TextView) findViewById(R.id.textView_cpceshi5);// 测试结果
			textView_cpenglish5 = (TextView) findViewById(R.id.textView_cpenglish5);// 英语成绩
			textView_cpcomputer5 = (TextView) findViewById(R.id.textView_cpcomputer5);// 计算机
			
			
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
				//如果现场分组模式，并且没有找到shareprefe，调用xml和数据库的数据
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
				//否则按照正常的评分阶段处理
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
			appState.voteState = userInfo.getString("toupiao", "未投票");  
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
			// 评分阶段  小组意见阶段
			textView_cpzongfen.setText("");
			textView_cpfenshu1.setText("");
			textView_cpfenshu2.setText("");
			textView_cpfenshu3.setText("");
			textView_cppogeyijian.setText("");
			radio_tongyi.setChecked(true);
			
			if ("量化".equals(appState.peopleList.get(appState.people_cur).get("lianghua").toString()) ) {
				appState.lianghua = true;
				lianghua.setVisibility(View.VISIBLE);
				bulianghua.setVisibility(View.INVISIBLE);
			} else {
				appState.lianghua = false;
				lianghua.setVisibility(View.INVISIBLE);
				bulianghua.setVisibility(View.VISIBLE);
			}
			
			

			// 准备数据
			textView_cpid5.setText(appState.peopleList.get(appState.people_cur).get("id").toString());
			textView_cpname5.setText(appState.peopleList.get(appState.people_cur).get("name").toString());
			textView_cpcompany5.setText(appState.peopleList.get(appState.people_cur).get("company").toString());
			
			getSharePrefe();//恢复状态
			
			// 申报类型为04，则需要破格，其它不需要破格
						if ("04".equals(appState.peopleList.get(appState.people_cur)
								.get("shenbaoleixin").toString())) {
							
							//取消破格功能
							textView_pogeyijian.setVisibility(View.GONE);	
							textView_cppogeyijian.setVisibility(View.GONE);
							radioGroup_poge.setVisibility(View.GONE);
							
							//appState.pogebutton = "yes";// 初始化
							appState.pogebutton = "not_use";// 初始化

						} else {
							textView_pogeyijian.setVisibility(View.GONE);
							textView_cppogeyijian.setVisibility(View.GONE);
							radioGroup_poge.setVisibility(View.GONE);
							appState.pogebutton = "not_use";// 初始化

						}
			
			// 如果已经评分，则显示评分
			appState.getDB();
			cursor = appState.queryTable(appState.peopleList.get(appState.people_cur).get("id").toString());
			if (cursor != null && cursor.getCount() > 0) {// 已经评分了
				cursor.moveToNext();
				
				if (appState.lianghua ) {	//量化
					textView_cpzongfen.setText(cursor.getString(1));
				} else {	//不量化
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

				
				//if (Integer.valueOf(cursor.getString(5)) > 0){//已经提交评分了
				if (!"pinfen".equals(appState.workfloat)){//不是评分阶段不可操作
					textView_cpfenshu1.setEnabled(false);
					textView_cpfenshu2.setEnabled(false);
					textView_cpfenshu3.setEnabled(false);
					textView_cppogeyijian.setEnabled(false);
					radio_tongyi.setEnabled(false);
					radio_butongyi.setEnabled(false);
					button_pinfensave.setEnabled(false);
					button_chakan1.setEnabled(false);
				}else {//评分阶段总是可以操作
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
			} else {// 没过评分
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
		} else if ("toupiao".equals(appState.tab5_state)) {// 投票------------------------------

			// 申报类型为04，则需要破格，其它不需要破格
			if ("04".equals(appState.peopleList.get(appState.people_cur)
					.get("shenbaoleixin").toString())) {
				textView_pogejielun.setVisibility(View.VISIBLE);
				textView_cppogejielun.setVisibility(View.VISIBLE);

			} else {

				textView_pogejielun.setVisibility(View.INVISIBLE);
				textView_cppogejielun.setVisibility(View.INVISIBLE);

			}
			
			//测试结果
			if ("".equals(appState.peopleList.get(appState.people_cur).get("ceshijieguo").toString())) {
				textView_ceshi5.setVisibility(View.INVISIBLE);
				textView_cpceshi5.setVisibility(View.INVISIBLE);
			} else {
				textView_ceshi5.setVisibility(View.VISIBLE);
				textView_cpceshi5.setVisibility(View.VISIBLE);
				}

			getSharePrefe();//恢复状态
			// 获取投票状态
			appState.getDB();
			cursor = appState.queryTable(appState.peopleList.get(appState.people_cur).get("id").toString());
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToNext();
				appState.voteState = cursor.getString(4);// 获取投票同意 赞成 反对 未投票
				/*
				if(cursor.getString(4).equalsIgnoreCase("同意")){
					radioButton_agree.setChecked(true);
				}else if(cursor.getString(4).equalsIgnoreCase("反对")){
					radioButton_against.setChecked(true);
				}else {
					radioButton_waiver.setChecked(true);
				}
				*/
				
			} else {
				appState.voteState = "未投票";
				
				
			}
			cursor.close();
			//appState.dbClose();

			update();
			// appState.isUpdated = false;
			Log.i("info", "upDated");

			//appState.getDB();
			//cursor = appState.queryTable_tijiaostate("3");
			//if (cursor == null || cursor.getCount() == 0) {//没有提交
			if ("toupiao".equals(appState.workfloat)) {//投票阶段都可以操作
				
				radioButton_agree.setEnabled(true);
				radioButton_against.setEnabled(true);
				radioButton_waiver.setEnabled(true);

				//radioButton_agree.setChecked(false);
				//radioButton_against.setChecked(false);
				radioButton_waiver.setChecked(true);// 默认
			} else {
				radioButton_agree.setEnabled(false);
				radioButton_against.setEnabled(false);
				radioButton_waiver.setEnabled(false);

				
			}
			cursor.close();
			appState.dbClose();
			
			// 投票同意 反对 弃权 未投票
			if ("赞成".equals(appState.voteState)) {
				radioButton_agree.setChecked(true);
			} else if ("反对".equals(appState.voteState)) {
				radioButton_against.setChecked(true);
			} else if ("弃权".equals(appState.voteState)) {
				radioButton_waiver.setChecked(true);
			}
			
			
		}//////endtoupiao
		else if ("xiaozuyijian".equals(appState.tab5_state)) {// 小组意见------------------------------

			// 申报类型为04，则需要破格，其它不需要破格
			if ("04".equals(appState.peopleList.get(appState.people_cur)
					.get("shenbaoleixin").toString())) {
				textView_pogejielun.setVisibility(View.VISIBLE);
				textView_cppogejielun.setVisibility(View.VISIBLE);

			} else {

				textView_pogejielun.setVisibility(View.INVISIBLE);
				textView_cppogejielun.setVisibility(View.INVISIBLE);

			}
			
			//测试结果
			if ("".equals(appState.peopleList.get(appState.people_cur).get("ceshijieguo").toString())) {
				textView_ceshi5.setVisibility(View.INVISIBLE);
				textView_cpceshi5.setVisibility(View.INVISIBLE);
			} else {
				textView_ceshi5.setVisibility(View.VISIBLE);
				textView_cpceshi5.setVisibility(View.VISIBLE);
				}

			//getSharePrefe();//恢复状态

			update();
			// appState.isUpdated = false;
			Log.i("info", "upDated");		
		
			
		}
		
		
	}

	private void update() {
		// TODO Auto-generated method stub
		// 准备数据
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
    		textView_cpceshi5.setText("不合格");
    	}else{
    		textView_cpceshi5.setText("合格");
    	}
		
			
		textView_cpenglish5.setText(appState.peopleList.get(appState.people_cur).get("waiyu").toString());
		textView_cpcomputer5.setText(appState.peopleList.get(appState.people_cur).get("jisuanji").toString());
	}

	
	public String xiaozuyijian ="";
	//小组意见通过radiobutton点击事件
	public void agree_yijian_onclick(View target) {
		Log.i("info", "建议通过");
		//button_vote.setEnabled(true);
		//button_cancelvote.setEnabled(true);
		xiaozuyijian = "建议通过";// 赞成
	}
	
	//小组意见不通过radiobutton点击事件
		public void against_yijian_onclick(View target) {
			Log.i("info", "建议不通过");
			//button_vote.setEnabled(true);
			//button_cancelvote.setEnabled(true);
			xiaozuyijian = "建议不通过";// 赞成
		}
	
	// 赞成radiobutton点击事件
	public void radioButton_agree_onclick(View target) {
		Log.i("info", "点击赞成");
		//button_vote.setEnabled(true);
		//button_cancelvote.setEnabled(true);
		appState.voteState = "赞成";// 赞成
	}

	// 反对radiobutton点击事件
	public void radioButton_against_onclick(View target) {
		Log.i("info", "点击反对");
		//button_vote.setEnabled(true);
		//button_cancelvote.setEnabled(true);
		appState.voteState = "反对";// 反对
	}

	// 弃权radiobutton点击事件
	public void radioButton_waiver_onclick(View target) {
		Log.i("info", "点击弃权");
		//button_vote.setEnabled(true);
		//button_cancelvote.setEnabled(true);
		appState.voteState = "弃权";// 弃权
	}
	
	// 小组意见保存按钮点击事件
		public void xiaozuyijian_onclick(View target) {
			Log.i("info", "提交小组意见");
			if ("接收成功".equals(submitYijian()) ){
			
			Intent it = new Intent(this, searchActivity.class);
			startActivity(it);
			this.finish();
			}
		}
	
	
	// 投票按钮点击事件
	public void vote_onclick(View target) {
		Log.i("info", "点击投票");

		if (!"未投票".equals(appState.voteState)) {// 已经投票
			// 写数据库
			appState.getDB();
			
			//在数据库找这个人
			cursor = null;
			cursor = appState.queryTable(appState.peopleList.get(appState.people_cur).get("id").toString());
			if (cursor!=null && cursor.getCount()>0){
				//找到了就更新
				// 投票0 1 2  赞成 反对 弃权
				appState.Update_toupiao(appState.peopleList.get(appState.people_cur).get("id").toString(),
						appState.voteState, // 提交投票
						"2"); //提交状态（保存/提交评分/保存投票/提交投票）0 1 2 3
			}else{
				//找不到就添加
				appState.add(appState.peopleList.get(appState.people_cur).get("id").toString(), 
						appState.scoreList.get(appState.people_cur).get("pinjunfen").toString(), 
						"", 
						appState.scoreList.get(appState.people_cur).get("pogejielun").toString(), 
						appState.voteState, "2", "", "", "");
			}
			
			textView_cpvotestate5.setText(appState.voteState);

			Toast toast = Toast.makeText(getApplicationContext(), "保存成功！",
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			
			//更新title----------
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
	    	
	    	this.getParent().setTitle("当前参评人员：" + String.valueOf(appState.people_cur + 1) + 
					" ， " + 
					"共有参评人员：" + String.valueOf(appState.people_total) + 
					"，当前已保存：" + String.valueOf(num1)
					);
	    	
	    
			//------------------
			//appState.getDB();
		 	//cursor = appState.queryTable_tijiaostate("2");
			if (num1 >= appState.people_total) {
				//cursor.close();
				appState.dbClose();
				button_chakan2_onclick(target);
				//提交数据部分放到查看页面做
				/*
				// 弹框 提示是否提交评分
				new AlertDialog.Builder(this)
						.setTitle("是否提交投票数据？")
						.setMessage("所有参评人员已经投票完成。\n点击“确定”按钮提交投票结果到评审服务器，\n点击“返回”按钮可以继续修改。\n注意：选择确定之后，不能修改参评人员的投票信息！")
						.setNegativeButton("返回",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										//this.s = "Negative";
									}
								})
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										//s = "Positive";
										//提交评分，提交成功一个更新一个数据库
										for (appState.people_cur=0; appState.people_cur<appState.people_total; appState.people_cur++){
											// 提交数据 
											String tmp = submitToupiao();
											 if ("接收成功".equals(tmp)){ // 状态改为已提交评分 // 提交状态（未提交/提交评分/提交投票）0 1 2  3
												 appState.dbClose();
												 appState.getDB();
												 appState.Update_tijiao(appState.peopleList.get(appState.people_cur).get("id").toString(), "3");// 提交投票


													Toast toast = Toast.makeText(getApplicationContext(),
															appState.peopleList.get(appState.people_cur).get("id").toString() + "提交成功！", 
															Toast.LENGTH_SHORT);
													toast.setGravity(Gravity.CENTER, 0,0);
													toast.cancel();
													toast.show();
											} else if ("接收失败".equals(tmp)) {
												Toast toast = Toast.makeText(getApplicationContext(),
														appState.peopleList.get(appState.people_cur).get("id").toString() + "服务器接收失败，请重新提交！",
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
			// 提交数据
			String tmp = submitToupiao();
			
			if ("接收成功".equals(tmp)) {
				// 状态改为已提交投票
				// 提交状态（未提交/提交评分/提交投票）0 1 2
				appState.Update_toupiao(
						appState.peopleList.get(appState.people_cur).get("id")
								.toString(), appState.voteState, // 提交投票
						"2"); // 此时改为已提交投票

				button_vote.setEnabled(false);
				button_cancelvote.setEnabled(false);

				radioButton_agree.setEnabled(false);
				radioButton_against.setEnabled(false);
				radioButton_waiver.setEnabled(false);

				textView_cpvotestate5.setText(appState.voteState);

				appState.dbClose();
				Toast toast = Toast.makeText(getApplicationContext(), "投票成功！",
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
			*/

		} else {
			Toast toast = Toast.makeText(getApplicationContext(), "请投票！",
					Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
/*
	// 取消按钮点击事件
	public void cancelvote_onclick(View target) {
		Log.i("info", "点击取消");

		button_vote.setEnabled(false);
		button_cancelvote.setEnabled(false);

		radioButton_agree.setChecked(false);
		radioButton_against.setChecked(false);
		radioButton_waiver.setChecked(false);
	}
*/
	// 破格同意radio点击事件
	public void radio_tongyi_onclick(View target) {
		Log.i("info", "点击同意");
		appState.pogebutton = "yes";// 同意
	}

	// 破格同意radio点击事件
	public void radio_butongyi_onclick(View target) {
		Log.i("info", "点击不同意");
		appState.pogebutton = "no";// 不同意
	}
	
	// 量化同意radio点击事件
		public void radio_gerentongyi_onclick(View target) {
			Log.i("info", "点击同意");
			appState.gerenyijian = "yes";// 同意
		}

		// 量化不同意radio点击事件
		public void radio_gerenbutongyi_onclick(View target) {
			Log.i("info", "点击不同意");
			appState.gerenyijian = "no";// 不同意
		}

	// 查看政策
	public void button_zhengce5_onclick(View target) throws Exception {
		String localPath = "sites/default/files/zhengce/";
		String fileName = "";

		fileName = appState.selectZhence();

		// appState.dl_file(localPath, fileName);

		try {
			InputStream in = getResources().getAssets().open(fileName);
			// 写到本地
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
					"没有安装阅读器/浏览器，或没有相关政策！", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

	// 查看政策
		public void button_zhengce5_2_onclick(View target) throws Exception {
			String localPath = "sites/default/files/zhengce/";
			String fileName = "";

			fileName = appState.selectZhence();

			// appState.dl_file(localPath, fileName);

			try {
				InputStream in = getResources().getAssets().open(fileName);
				// 写到本地
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
						"没有安装阅读器/浏览器，或没有相关政策！", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		}
		
		
		/*
	// 查看评分标准
	public void button_biaozhun_onclick(View target) {
		String localPath = "sites/default/files/biaozhun/";
		String fileName = "pinfenbiaozhun1.jpg";

		try {
			InputStream in = getResources().getAssets().open(fileName);
			// 写到本地
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
					"没有安装阅读器或文件格式不正确！", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
	
	// 查看评分标准
		public void button_biaozhun2_onclick(View target) {
			String localPath = "sites/default/files/biaozhun/";
			String fileName = "pinfenbiaozhun2.jpg";

			try {
				InputStream in = getResources().getAssets().open(fileName);
				// 写到本地
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
						"没有安装阅读器或文件格式不正确！", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		}
		
		// 查看评分标准
		public void button_biaozhun3_onclick(View target) {
			String localPath = "sites/default/files/biaozhun/";
			String fileName = "pinfenbiaozhun3.jpg";

			try {
				InputStream in = getResources().getAssets().open(fileName);
				// 写到本地
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
						"没有安装阅读器或文件格式不正确！", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		}
		*/
		

	// 提交数据点击事件
	public void button_pinfensave_onclick(View target) {
		
		Log.i("info", "点击保存");
		
		appState.getDB();
		

		// 申报类型为04，则需要破格，其它不需要破格
		//改成不管破格
//		if ("04".equals(appState.peopleList.get(appState.people_cur).get("shenbaoleixin").toString())) {
//			// 破格
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
//				// 写数据库
//				cursor = appState.queryTable(appState.peopleList.get(appState.people_cur).get("id").toString());
//				if (cursor == null || cursor.getCount() == 0) {
//					// 如果没有保存过，则添加
//					appState.add(appState.peopleList.get(appState.people_cur).get("id").toString(), // id
//							textView_cpzongfen.getText().toString(), // 评分
//							textView_cppogeyijian.getText().toString(), // 破格意见
//							appState.pogebutton, // 破格选择按钮
//							"未投票", "0",// 提交状态（保存/提交评分/保存投票/提交投票）0 1 2 3
//							textView_cpfenshu1.getText().toString(),// 分数1
//							textView_cpfenshu2.getText().toString(),// 分数2
//							textView_cpfenshu3.getText().toString()// 分数3
//							);
//				} else {
//					// 如果保存过，则修改
//					appState.Update_people(appState.peopleList.get(appState.people_cur).get("id").toString(), // id
//							textView_cpzongfen.getText().toString(), // 评分
//							textView_cppogeyijian.getText().toString(), // 破格意见
//							appState.pogebutton, // 破格选择按钮
//							"未投票", "0",// 提交状态（保存/提交评分/保存投票/提交投票）0 1 2 3
//							textView_cpfenshu1.getText().toString(),// 分数1
//							textView_cpfenshu2.getText().toString(),// 分数2
//							textView_cpfenshu3.getText().toString()// 分数3
//							);
//				}
//				Toast toast = Toast.makeText(getApplicationContext(), "保存成功！",
//						Toast.LENGTH_SHORT);
//				toast.setGravity(Gravity.CENTER, 0, 0);
//				toast.show();
//
//				cursor.close();
//				
//				// button_pinfensave.setEnabled(false);
//
//				/*
//				 * // 提交数据 String tmp = submitPinfen();
//				 * 
//				 * if ("接收成功".equals(tmp)){ // 状态改为已提交评分 // 提交状态（未提交/提交评分/提交投票）0
//				 * 1 2 appState.Update_tijiao(appState.peopleList.get(appState.
//				 * people_cur) .get("id").toString(), "1");// 提交评分
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
//				 * "提交成功！", Toast.LENGTH_LONG); toast.setGravity(Gravity.CENTER,
//				 * 0, 0); toast.show(); }else if("接收失败".equals(tmp)){ Toast
//				 * toast = Toast.makeText(getApplicationContext(),
//				 * "服务器接收失败，请重新提交！", Toast.LENGTH_LONG);
//				 * toast.setGravity(Gravity.CENTER, 0, 0); toast.show(); }
//				 */
//
//			} else {
//				Toast toast = Toast.makeText(getApplicationContext(),
//						"请填写正确信息！", Toast.LENGTH_LONG);
//				toast.setGravity(Gravity.CENTER, 0, 0);
//				toast.show();
//			}
//		} else {
			// 非破格
		
		if (appState.lianghua ) {	//量化评分模式
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
				// 写数据库
				//appState.getDB();
				cursor = appState.queryTable(appState.peopleList.get(appState.people_cur).get("id").toString());
				if (cursor == null || cursor.getCount() == 0) {
					// 如果沒保存過，添加
					appState.add(appState.peopleList.get(appState.people_cur).get("id").toString(), // id
							textView_cpzongfen.getText().toString(), // 评分
							textView_cppogeyijian.getText().toString(), // 破格意见
							appState.pogebutton, // 破格选择按钮
							"未投票", "0", // 提交状态（保存/提交评分/保存投票/提交投票）0 1 2 3
							textView_cpfenshu1.getText().toString(),// 分数1
							textView_cpfenshu2.getText().toString(),// 分数2
							textView_cpfenshu3.getText().toString()// 分数3
							);
				}else{
					//如果添加過，更新
					appState.Update_people(appState.peopleList.get(appState.people_cur).get("id").toString(), // id
							textView_cpzongfen.getText().toString(), // 评分
							textView_cppogeyijian.getText().toString(), // 破格意见
							appState.pogebutton, // 破格选择按钮
							"未投票", "0",// 提交状态（保存/提交评分/保存投票/提交投票）0 1 2 3
							textView_cpfenshu1.getText().toString(),// 分数1
							textView_cpfenshu2.getText().toString(),// 分数2
							textView_cpfenshu3.getText().toString()// 分数3
							);
				}
				Toast toast = Toast.makeText(getApplicationContext(), "保存成功！",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				
				cursor.close();

				//appState.dbClose();
				// button_pinfensave.setEnabled(false);

				/*
				 * // 提交数据 String tmp = submitPinfen();
				 * 
				 * if ("接收成功".equals(tmp)){ // 状态改为已提交评分 // 提交状态（未提交/提交评分/提交投票）0
				 * 1 2 appState.Update_tijiao(appState.peopleList.get(appState.
				 * people_cur) .get("id").toString(), "1");// 提交评分
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
				 * "提交成功！", Toast.LENGTH_LONG); toast.setGravity(Gravity.CENTER,
				 * 0, 0); toast.show(); }else if("接收失败".equals(tmp)){ Toast
				 * toast = Toast.makeText(getApplicationContext(),
				 * "服务器接收失败，请重新提交！", Toast.LENGTH_LONG);
				 * toast.setGravity(Gravity.CENTER, 0, 0); toast.show(); }
				 */

			} else {
				Toast toast = Toast.makeText(getApplicationContext(),
						"请填写正确信息！", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		} else {	//不需要量化评分
			// 写数据库
			//appState.getDB();
			cursor = appState.queryTable(appState.peopleList.get(appState.people_cur).get("id").toString());
			if (cursor == null || cursor.getCount() == 0) {
				// 如果沒保存過，添加
				appState.add(appState.peopleList.get(appState.people_cur).get("id").toString(), // id
						//textView_cpzongfen.getText().toString(), // 评分
						appState.gerenyijian,	//不量化评分，总分字段用作个人意见
						textView_cppogeyijian.getText().toString(), // 破格意见
						appState.pogebutton, // 破格选择按钮
						"未投票", "0", // 提交状态（保存/提交评分/保存投票/提交投票）0 1 2 3
						"0",// 分数1
						"0",// 分数2
						"0"// 分数3
						);
			}else{
				//如果添加過，更新
				appState.Update_people(appState.peopleList.get(appState.people_cur).get("id").toString(), // id
						//textView_cpzongfen.getText().toString(), // 评分
						appState.gerenyijian,	//不量化评分，总分字段用作个人意见
						textView_cppogeyijian.getText().toString(), // 破格意见
						appState.pogebutton, // 破格选择按钮
						"未投票", "0",// 提交状态（保存/提交评分/保存投票/提交投票）0 1 2 3
						"0",// 分数1
						"0",// 分数2
						"0"// 分数3
						);
			}
			Toast toast = Toast.makeText(getApplicationContext(), "保存成功！",
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			
			cursor.close();
		}
			
//		}// end 非破格

		
		//更新title----------
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
		    	this.getParent().setTitle("当前参评人员：" + String.valueOf(appState.people_cur + 1) + 
						" ， " + 
						"共有参评人员：" + String.valueOf(appState.people_total) + 
						"，当前已保存：" + String.valueOf(num1)
						);
		    	
		    
				//------------------
		 
		    	
		    	if (appState.xianchangfenzu ){//大型评审会现场分组模式
		    		cursor = appState.queryTable(appState.peopleList.get(appState.people_cur).get("id").toString());
		    		
		    		if (cursor != null && cursor.getCount() > 0) {
		    			cursor.close();
		    			submitPinfen();
		    		
					
					//获取服务器工作流程
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
			//提交数据部分放到查看页面做
			/*
			// 弹框 提示是否提交评分
			new AlertDialog.Builder(this)
					.setTitle("是否提交评分数据？")
					.setMessage("所有参评人员已经评分完成。\n点击“确定”按钮提交各项数据到评审服务器，\n点击“返回”按钮可以继续修改。\n注意：选择确定之后，不能修改参评人员的各项数据！")
					.setNegativeButton("返回",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									//this.s = "Negative";
								}
							})
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									//s = "Positive";
									//提交评分，提交成功一个更新一个数据库
									for (appState.people_cur = 0 ; appState.people_cur<appState.people_total; appState.people_cur++){
										// 提交数据 
										String tmp = submitPinfen();
										 if ("接收成功".equals(tmp)){ // 状态改为已提交评分 // 提交状态（未提交/提交评分/提交投票）0 1 2  3
											 appState.dbClose();
											 appState.getDB();
											 appState.Update_tijiao(appState.peopleList.get(appState.people_cur).get("id").toString(), "1");// 提交评分
											
											Toast toast = Toast.makeText(getApplicationContext(),
													appState.peopleList.get(appState.people_cur).get("id").toString() + "提交成功！", 
													Toast.LENGTH_SHORT);
											toast.setGravity(Gravity.CENTER, 0,0);
											toast.cancel();
											toast.show();
											
										} else if ("接收失败".equals(tmp)) {
											Toast toast = Toast.makeText(getApplicationContext(),
													appState.peopleList.get(appState.people_cur).get("id").toString() + "服务器接收失败，请重新提交！",
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

	
		// 如果没到最后一个，跳到下一个
		
		
		
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
					Toast toast = Toast.makeText(getApplicationContext(),
							line, Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();

					line = in.readLine();
				}
				in.close();
				conn.disconnect();
				
				if ("正在评分".equals(tmp)){
					appState.workfloat = "pinfen";
				}else if ("正在写意见".equals(tmp)){
					appState.workfloat = "xiaozuyijian";
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
	
	private String submitYijian(){
		
		String pwhid = appState.pwhid;// 评委会
		String pwid = appState.pinweiName;// 评委
		String id = appState.peopleList.get(appState.people_cur).get("id").toString();// 参评人

		String tmp = "接收失败";
		

		StringBuilder dataTransformb = new StringBuilder();
		dataTransformb.append( "pwhid=" + URLEncoder.encode(pwhid) //评委会
				+ "&pwid=" + URLEncoder.encode(pwid)	// 评委
				+ "&data=[");

			//合成提交参数,调试时暂时屏蔽
			
			dataTransformb.append( "{\"id\":\"" + URLEncoder.encode(id) + "\","// 参评人
					+ "\"opinion\":\"" + URLEncoder.encode(xiaozuyijian) + "\""// 小组意见
					+ "}]");
	
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
					out.writeBytes(dataTransformb.toString());
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
	
	private String submitPinfen() {
		// TODO Auto-generated method stub
		/*
		 * (评委会）pwhid （评委id）pwid (参评人）id 10位id
		 * 
		 * （破格）poge yes no 二选一 (破格内容）content 字符串
		 * 
		 * （总分）total 0~100整型数
		 */
		appState.getDB();
		cursor = appState.queryTable(appState.peopleList.get(appState.people_cur).get("id").toString());
		cursor.moveToNext();
		
		String pwhid = appState.pwhid;// 评委会
		String pwid = appState.pinweiName;// 评委
		String id = appState.peopleList.get(appState.people_cur).get("id").toString();// 参评人
		String poge = cursor.getString(3);
		String content = cursor.getString(2);
		String total = cursor.getString(1);
		String geren = ""; //个人意见
		String tmp = "接收失败";
		
		
		cursor.close();
		appState.dbClose();
		
		if (appState.lianghua) {	//量化评分模式
			
		} else {	//非量化评分模式
			geren = total;	//非量化评分模式，数据库总分total字段存的是个人意见 yes，no
			total = "0";
		}

		StringBuilder dataTransformb = new StringBuilder();
		dataTransformb.append( "pwhid=" + URLEncoder.encode(pwhid) //评委会
				+ "&pwid=" + URLEncoder.encode(pwid)	// 评委
				+ "&data=[");

			//合成提交参数,调试时暂时屏蔽
			
			dataTransformb.append( "{\"id\":\"" + URLEncoder.encode(id) + "\","// 参评人
					+ "\"poge\":\"" + URLEncoder.encode(poge) + "\","// 破格
					+ "\"content\":\"" + URLEncoder.encode(content) + "\","  //破格意见
					+ "\"gerenyijian\":\"" + URLEncoder.encode(geren) + "\","  //个人意见
					+ "\"total\":\"" + URLEncoder.encode(total) + "\""  //总分
					+ "}]");
		
			// 要访问的web servlet
			// 注意：IP和端口是本地的 需要换成你的IP和端口
			String servletUrl = appState.HttpHead + "/expert/score";
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
					out.writeBytes(dataTransformb.toString());
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

	private String submitToupiao() {
		// TODO Auto-generated method stub
		/*
		 * (评委会）pwhid （评委id）pwid (参评人）id 10位id (投票）toupiao 赞成 反对 弃权 三选1
		 */
		appState.getDB();
		cursor = appState.queryTable(appState.peopleList.get(appState.people_cur).get("id").toString());
		cursor.moveToNext();
		
		String pwhid = appState.pwhid;// 评委会
		String pwid = appState.pinweiName;// 评委
		String id = appState.peopleList.get(appState.people_cur).get("id").toString();// 参评人
		String vote = cursor.getString(4);
		
		
		cursor.close();
		appState.dbClose();

		String tmp = "接收失败";

		// 要访问的web servlet
		// 注意：IP和端口是本地的 需要换成你的IP和端口
		String servletUrl = appState.HttpHead + "/expert/vote";
		// 将参数传给服务器
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
				// 使用HttpURLConnection打开连接
				HttpURLConnection urlConn = (HttpURLConnection) url
						.openConnection();
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
				out.writeBytes(params);
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
	
	//查看按钮
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

	// 操作說明按钮点击事件
		public void button_czsm5_onclick(View target) {
			appState.launch_help();
		}
		
		// 操作說明按钮点击事件
		public void button_czsm5_2_onclick(View target) {
			appState.launch_help();
		}
		
		
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// 按下键盘上返回按钮
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				System.out.println("返回按钮");
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
