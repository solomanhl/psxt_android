package com.gky.zcps_android;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class xiaozufen_Activity extends Activity {
	private Button login_rtn_btn;
	private TextView info;
	private EditText xiaozudafen;
	private Bundle bundle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置成横屏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xiaozufendialog);

		setTitle("小组评分");
		findviews(); // 注册控件
				
	}

	private void findviews() {
		// TODO Auto-generated method stub
		login_rtn_btn = (Button) findViewById(R.id.login_rtn_btn);
		info = (TextView) findViewById(R.id.info);
		xiaozudafen = (EditText) findViewById(R.id.xiaozudafen);
		
		bundle = this.getIntent().getExtras();
		info.setText(bundle.getString("info") );
	}


	// 返回按钮事件
	public void login_rtn_btn_onclick(View target) {
		// 返回主界面,回传参数
		int resultCode = -1;
		if ( !"".equals(xiaozudafen.getText().toString() ) ){
			resultCode =Integer.valueOf(xiaozudafen.getText().toString() );
			if (Integer.valueOf(xiaozudafen.getText().toString()) <0){
				resultCode = 0;
			}else if (Integer.valueOf(xiaozudafen.getText().toString()) > 100){
				resultCode = 100;
			}
		}
		Intent rtnIntent = new Intent();
		xiaozufen_Activity.this.setResult( resultCode, rtnIntent);
		xiaozufen_Activity.this.finish();
	}
}
