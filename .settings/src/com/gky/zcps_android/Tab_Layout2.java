/**
 * 主要工作内容
 * 
 * @author 贺亮
 * 
 */
package com.gky.zcps_android;

import java.io.InputStream;

import com.soloman.file.FileUtils;
import com.soloman.intent.SendIntent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Tab_Layout2 extends Activity {
	private Global_var appState; // 获得全局变量;


	private EditText textView_cpworkvitae; // 专辑工作简历
	private Button button_zhengce2, button_czsm2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		appState = ((Global_var) getApplicationContext()); // 获得全局变量
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_layout2);
		
		findView();
	}
	
	private void findView() {
		// TODO Auto-generated method stub
		textView_cpworkvitae = (EditText) findViewById(R.id.textView_cpworkvitae); 
		button_zhengce2 = (Button) findViewById(R.id.button_zhengce2);
		button_czsm2 = (Button) findViewById(R.id.button_czsm2);
		
	}

	@Override 
    public void onResume(){
    	super.onResume();
    	Log.i("info","Tab_Layout2_onResume");
    	if (appState.isUpdated){
    		update();
    		Log.i("info","upDated");
    	}
    	
    }
    
    public void update(){
    	textView_cpworkvitae.setText(appState.peopleList.get(appState.people_cur).get("zhuanjigongzuo").toString());//id
    }
    
 // 查看政策
  	public void button_zhengce2_onclick(View target) throws Exception {
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
  	
 // 操作說明按钮点击事件
 	public void button_czsm2_onclick(View target) {
 		appState.launch_help();
 	}
}
