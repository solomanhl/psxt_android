/**
 * 欢迎页面
 * 
 * @author 
 * 
 */
package com.gky.zcps_android;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

/**
 * @Description:进场动画
 * 
 * @Author 贺亮
 * 
 * @date 2012-10-23
 * 
 * @version V1.0
 */
public class WelcomeActivity extends Activity {
	private Global_var appState; // 获得全局变量;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		appState = ((Global_var) getApplicationContext()); // 获得全局变量
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 设置成横屏

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		appState.keepScreenAlive();//保持屏幕常量
		appState.getSDpath();
		//appState.getDB();
		appState.workfloat = "denglu";
		appState.closeMain = false;//不关闭主窗体
		
		getDeviceInfo();
		Log.i("info","设备参数：\r\n屏幕宽度-" + appState.screenWidth + "屏幕高度-" + appState.screenHeight + "屏幕密度-" + appState.density + "密度dpi-" + appState.densityDpi);
		Log.i("info","固件：" + appState.firm);
		Log.i("info","IMEI：" + appState.IMEI);
		
		Log.i("info", "设备参数：\r\n屏幕宽度-" + appState.screenWidth + "屏幕高度-" + appState.screenHeight + "屏幕密度-" + appState.density + "密度dpi-" + appState.densityDpi);  
		Log.i("info", "固件：" + appState.firm);
		Log.i("info", "IMEI：" + appState.IMEI);

		ImageView logo = (ImageView) findViewById(R.id.logo2);
		// 设定进入动画
		Animation logo_animation = AnimationUtils.loadAnimation(
				WelcomeActivity.this, R.anim.push_left_in);
		logo.setAnimation(logo_animation);
		logo_animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				//Intent it = new Intent(WelcomeActivity.this, MainActivity.class);
				Intent it = new Intent(WelcomeActivity.this, EntranceActivity.class);
				startActivity(it);
				// overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				WelcomeActivity.this.finish();
			}
		});
	}

	private void getDeviceInfo() {
		// TODO Auto-generated method stub
			DisplayMetrics metric = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metric);
			appState.screenWidth = metric.widthPixels; // 屏幕宽度（像素）
			appState.screenHeight = metric.heightPixels; // 屏幕高度（像素）
			appState.density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
			appState.densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）
			appState.wh = appState.screenHeight / appState.screenWidth;

			appState.firm = android.os.Build.VERSION.RELEASE;
			appState.tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			/*
			 * 唯一的设备ID： GSM手机的 IMEI 和 CDMA手机的 MEID. Return null if device ID is not
			 * available.
			 */
			appState.IMEI = appState.tm.getDeviceId();// String
	}
}
