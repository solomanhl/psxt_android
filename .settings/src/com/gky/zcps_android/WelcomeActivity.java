/**
 * ��ӭҳ��
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
 * @Description:��������
 * 
 * @Author ����
 * 
 * @date 2012-10-23
 * 
 * @version V1.0
 */
public class WelcomeActivity extends Activity {
	private Global_var appState; // ���ȫ�ֱ���;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		appState = ((Global_var) getApplicationContext()); // ���ȫ�ֱ���
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// ���óɺ���

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		appState.keepScreenAlive();//������Ļ����
		appState.getSDpath();
		//appState.getDB();
		appState.workfloat = "denglu";
		appState.closeMain = false;//���ر�������
		
		getDeviceInfo();
		Log.i("info","�豸������\r\n��Ļ���-" + appState.screenWidth + "��Ļ�߶�-" + appState.screenHeight + "��Ļ�ܶ�-" + appState.density + "�ܶ�dpi-" + appState.densityDpi);
		Log.i("info","�̼���" + appState.firm);
		Log.i("info","IMEI��" + appState.IMEI);
		
		Log.i("info", "�豸������\r\n��Ļ���-" + appState.screenWidth + "��Ļ�߶�-" + appState.screenHeight + "��Ļ�ܶ�-" + appState.density + "�ܶ�dpi-" + appState.densityDpi);  
		Log.i("info", "�̼���" + appState.firm);
		Log.i("info", "IMEI��" + appState.IMEI);

		ImageView logo = (ImageView) findViewById(R.id.logo2);
		// �趨���붯��
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
			appState.screenWidth = metric.widthPixels; // ��Ļ��ȣ����أ�
			appState.screenHeight = metric.heightPixels; // ��Ļ�߶ȣ����أ�
			appState.density = metric.density; // ��Ļ�ܶȣ�0.75 / 1.0 / 1.5��
			appState.densityDpi = metric.densityDpi; // ��Ļ�ܶ�DPI��120 / 160 / 240��
			appState.wh = appState.screenHeight / appState.screenWidth;

			appState.firm = android.os.Build.VERSION.RELEASE;
			appState.tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			/*
			 * Ψһ���豸ID�� GSM�ֻ��� IMEI �� CDMA�ֻ��� MEID. Return null if device ID is not
			 * available.
			 */
			appState.IMEI = appState.tm.getDeviceId();// String
	}
}
