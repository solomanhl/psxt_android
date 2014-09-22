/**
 * 基本信息
 * 
 * @author 贺亮
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
	private Global_var appState; // 获得全局变量;
	
	private TextView textView_cpidf; //编号
	private TextView textView_cpnamef; //姓名
	private TextView textView_cpcompanyf;//工作单位
	private EditText textView_cpjiyuf;//主任寄语
	private Button button_zhurensave;
	
	

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	appState = ((Global_var) getApplicationContext()); // 获得全局变量
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout_jiyu);
        
        findView();    
    }
    
    private void findView() {
		// TODO Auto-generated method stub
    	textView_cpidf = (TextView) findViewById(R.id.textView_cpidf); // 编号        
    	textView_cpnamef = (TextView) findViewById(R.id.textView_cpnamef); // 姓名    
    	textView_cpcompanyf = (TextView) findViewById(R.id.textView_cpcompanyf); // 工作单位    
    	textView_cpjiyuf = (EditText) findViewById(R.id.textView_cpjiyuf);//主任寄语
    	button_zhurensave = (Button) findViewById(R.id.button_zhurensave);//
	}

	@Override 
    public void onResume(){
    	super.onResume();
    	Log.i("info","Tab_Layout_jiyu_onResume");
    	
    	getSharePrefe();//恢复状态
		// 获取投票状态
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
    	textView_cpnamef.setText(appState.failedList.get(appState.people_cur).get("name_f").toString());// 姓名    
    	textView_cpcompanyf.setText(appState.failedList.get(appState.people_cur).get("company_f").toString()); // 工作单位    
    	
    	
  
    }
    
    
    Cursor cursor;
    public void button_zhurensave_onclick(View target) {
    	if (textView_cpjiyuf.getText() != null && textView_cpjiyuf.length() > 0) {
			// 写数据库
			appState.getDB();
			// 
			cursor = appState.queryFailed(appState.failedList.get(appState.people_cur).get("id_f").toString());
			if (cursor == null || cursor.getCount() == 0) {//添加
				appState.Add_pinyu(appState.failedList.get(appState.people_cur).get("id_f").toString(),
						textView_cpjiyuf.getText().toString(),
						"0"); //提交状态（保存/提交）0 1 
			}else {//修改
				appState.Update_pinyu(appState.failedList.get(appState.people_cur).get("id_f").toString(),
						textView_cpjiyuf.getText().toString()); 
			}
			
			cursor.close();
			

			Toast toast = Toast.makeText(getApplicationContext(), "保存成功！",
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			
			//appState.getDB();
		 	cursor = appState.getFailedAll();
		 	//更新title----------
		 	int num1 = 0;			
			
	    	Cursor cursor1 = appState.queryFailed_zhuangtai("0");
	    	num1 = cursor1.getCount();
	    	cursor1.close();
	    	this.getParent().setTitle("当前人员：" + String.valueOf(appState.people_cur + 1) + 
					" ， " + 
					"共有人员：" + String.valueOf(appState.people_total) + 
					"，当前已保存：" + String.valueOf(num1)
					);
	    	
	
		 	//--------------------
		 	
			if (cursor.getCount() >= appState.failedList.size()) {
				cursor.close();
				openList();
				/*
				// 弹框 提示是否提交评分
				new AlertDialog.Builder(this)
						.setTitle("是否提交主任寄语？")
						.setMessage("所有主任寄语填写完成。\n点击“确定”按钮提交主任寄语到评审服务器，\n点击“返回”按钮可以继续修改。\n注意：选择确定之后，不能修改未通过人员的主任寄语！")
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
											String tmp = submitJiyu();
											 if ("接收成功".equals(tmp)){ // 状态改为已提交评分 // 提交状态（未提交/提交评分/提交投票）0 1 2  3
												 appState.dbClose();
												 appState.getDB();
												 appState.Update_failedState(appState.failedList.get(appState.people_cur).get("id_f").toString(), 
														 "1");// 提交寄语


													Toast toast = Toast.makeText(getApplicationContext(),
															appState.failedList.get(appState.people_cur).get("id_f").toString() + "提交成功！", 
															Toast.LENGTH_SHORT);
													toast.setGravity(Gravity.CENTER, 0,0);
													toast.cancel();
													toast.show();
											} else if ("接收失败".equals(tmp)) {
												Toast toast = Toast.makeText(getApplicationContext(),
														appState.failedList.get(appState.people_cur).get("id_f").toString() + "服务器接收失败，请重新提交！",
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
			Toast toast = Toast.makeText(getApplicationContext(), "请输入主任寄语！",
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
		
		 // (评委会）pwhid （评委id）pwid (参评人）id 10位id (投票）toupiao 同意 反对 弃权 三选1
		 
		appState.getDB();
		cursor = appState.queryFailed(appState.failedList.get(appState.people_cur).get("id_f").toString());
		cursor.moveToNext();
		
		String pwhid = appState.pwhid;// 评委会
		//String pwid = appState.pinweiName;// 评委
		String id = appState.failedList.get(appState.people_cur).get("id_f").toString();// 参评人
		//String vote = cursor.getString(4);
		
		cursor.close();
		appState.dbClose();

		String tmp = "接收失败";

		// 要访问的web servlet
		// 注意：IP和端口是本地的 需要换成你的IP和端口
		String servletUrl = appState.HttpHead + "/expert/comment";
		// 将参数传给服务器
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
*/
}
