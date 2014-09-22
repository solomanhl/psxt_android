/**
 * �����
 * @author ����
 */
package com.gky.zcps_android;


import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.animation.AnimationUtils;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class MainActivity extends TabActivity {
	private ViewFlipper viewFlipper1;
	
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 100;
	/*
	 * private Animation slideLeftIn; private Animation slideLeftOut; private
	 * Animation slideRightIn; private Animation slideRightOut; private
	 * ViewFlipper viewFlipper;
	 */
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;

	Bundle bundle;
	String jiyu;
	

	private Global_var appState; // ���ȫ�ֱ���;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		appState = ((Global_var) getApplicationContext()); // ���ȫ�ֱ���
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// ���óɺ���

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		bundle = this.getIntent().getExtras();
		jiyu = bundle.getString("jiyu");

		appState.firstIn = false;
		// ����ViewFlipper
		viewFlipper1 = (ViewFlipper) findViewById(R.id.viewFlipper1);		
		
		// ��������-------------
		/*
		 * viewFlipper = (ViewFlipper)findViewById(R.id.flipper); slideLeftIn =
		 * AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
		 * slideLeftOut = AnimationUtils.loadAnimation(this,
		 * R.anim.slide_left_out); slideRightIn =
		 * AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
		 * slideRightOut = AnimationUtils.loadAnimation(this,
		 * R.anim.slide_right_out);
		 */

		gestureDetector = new GestureDetector(new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {

					return true;  //gestureDetector��ӦonTouch�¼�
				}
				return false;
			}
			
		};
		
		// ------------------------------
		appState.tabhost = this.getTabHost();// ��TabActivity�����ȡ����Tab��TabHost
		// LayoutInflater.from(this).inflate(R.layout.activity_main,
		// tabhost.getTabContentView(), true);
		// from(this)�����TabActivity��ȡLayoutInflater
		// R.layout.main ���Tab����
		// ͨ��TabHost��ô��Tab��ǩҳ���ݵ�FrameLayout
		// �Ƿ�inflate ˩ϵ��������Ԫ����

		// tabhost1.setBackgroundColor(Color.argb(150, 22, 70, 150));//
		// ����һ��TabHost����ɫ

		//viewFlipper1.addView(appState.tabhost);
		appState.updateTabHost(jiyu); //
		
		/* �����Tabѡ���ʱ�򣬸��ĵ�ǰTab��ǩ�ı��� */
		appState.tabhost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				appState.updateTabBackground();

			}
		});

		
		
		
	}//end onCreate();

	@Override 
    public void onResume(){
    	super.onResume(); 
    	
    	if (appState.closeMain ){
    		appState.closeMain = false;//���ر�������
    		finish();
    	}
    	
//    	if (appState.xianchangfenzu){
//    		
//    	}else{
    		int num1 = 0;
        	int num2 = 0;
        	appState.getDB();
        	Cursor cursor ;
        	if ("pinfen".equals(appState.workfloat)){
        		cursor = appState.getAll();
        		num1 = cursor.getCount();
        		cursor.close();
        	}else if ("toupiao".equals(appState.workfloat)){
        		cursor = appState.queryTable_tijiaostate("2");
        		num1 = cursor.getCount();
        		cursor.close();
        		cursor = appState.queryTable_tijiaostate("3");
        		num2 = cursor.getCount();
        		cursor.close();
        		
        		num1 += num2;
        	}
        	setTitle("��ǰ������Ա��" + String.valueOf(appState.people_cur + 1) + 
    				" �� " + 
    				"���в�����Ա��" + String.valueOf(appState.people_total) + 
    				"����ǰ�ѱ��棺" + String.valueOf(num1)
    				);

        	appState.dbClose();
//    	}
    	
    	
	}
	
	//�˵�-------------------------------------------------
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_menu, menu);
		menu.add("�������");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// ��Ӧÿ���˵���(ͨ���˵����ID)
		case 0://�������
			// do something here
			alertAbout();
			break;
		case 1:
			// do something here
			break;		
		default:
			// ��û�д�����¼�����������������
			return super.onOptionsItemSelected(item);
		}
		// ����true��ʾ������˵�����¼�������Ҫ�����¼�����������ȥ��
		return true;
	}

	//--------------------------------------------------------


	private void alertAbout() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this)
		.setTitle("�������")
		.setMessage( this.getString(R.string.about) + 
					"\n\n��ǰ�汾��" + 
					this.getString(R.string.ver) )
		/*
		.setNegativeButton("ȡ��",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				})
				*/
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						
					}
				}).show();			
	}

	// onTouchEvent()-----------------------------------
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (gestureDetector.onTouchEvent(event))
			return true;
		else
			return false;
	}

	// ------------------------------------------------

	// �������Ʋ���---------------------------------------
	class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					// �������붯��
					viewFlipper1.setInAnimation(AnimationUtils.loadAnimation(
							getApplicationContext(), R.anim.slide_left_in));
					// �����г�����
					viewFlipper1.setOutAnimation(AnimationUtils.loadAnimation(
							getApplicationContext(), R.anim.slide_left_out));
					
					// ��������
					
					if (appState.people_cur < (appState.people_total -1) ) {
						appState.people_cur++;
						appState.isUpdated = true;
						appState.tabhost.setCurrentTab(1);
						appState.tabhost.setCurrentTab(0);
						// �л�����һ��
						viewFlipper1.showNext();
						clearPre();
						//����title----------
						int num1 = 0;
						int num2 = 0;
						
						appState.getDB();
				    	Cursor cursor2 = null;
				    	if ("pinfen".equals(appState.workfloat)){
				    		cursor2 = appState.getAll();
				    		if (cursor2 != null ){
				    			num1 = cursor2.getCount();
				    			cursor2.close();
				    		}
				    	}else if ("toupiao".equals(appState.workfloat)){
				    		cursor2 = appState.queryTable_tijiaostate("2");
				    		if (cursor2 != null ){
				    			num1 = cursor2.getCount();
				    			cursor2.close();
				    		}
				    		cursor2 = appState.queryTable_tijiaostate("3");
				    		if (cursor2 != null ){
				    			num2 = cursor2.getCount();
				    			cursor2.close();
				    		}
				    		
				    		num1 += num2;
				    	}else if ("pinyu".equals(appState.workfloat)){
				    		cursor2 = appState.queryFailed_zhuangtai("0");
				    		if (cursor2 != null ){
				    			num1 = cursor2.getCount();
				    			cursor2.close();
				    		}
				    	}
				    	appState.dbClose();
				    	setTitle("��ǰ������Ա��" + String.valueOf(appState.people_cur + 1) + 
								" �� " + 
								"���в�����Ա��" + String.valueOf(appState.people_total) + 
								"����ǰ�ѱ��棺" + String.valueOf(num1)
								);
				    	
				    
						//------------------
				    	
				    	
				    	appState.dbClose();
						Log.i("info", "viewFlipper1.showNext()");
					}else{
						Toast toast = Toast.makeText(getApplicationContext(),"�Ѿ������һ��������Ա��", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						Log.i("info", "���һ��������Ա");
					}
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					// �������붯��
					viewFlipper1.setInAnimation(AnimationUtils.loadAnimation(
							getApplicationContext(), R.anim.slide_right_in));
					// �����г�����
					viewFlipper1.setOutAnimation(AnimationUtils.loadAnimation(
							getApplicationContext(), R.anim.slide_right_out));
					// ��������
					
					if (appState.people_cur > 0) {
					appState.people_cur--;
					appState.isUpdated = true;
					appState.tabhost.setCurrentTab(1);
					appState.tabhost.setCurrentTab(0);
					// ��ʾ��һ��
					viewFlipper1.showPrevious();
					 clearPre();
					//����title----------
					int num1 = 0;
					int num2 = 0;
					
					appState.getDB();
			    	Cursor cursor3 = null;
			    	if ("pinfen".equals(appState.workfloat)){
			    		cursor3 = appState.getAll();
			    		if (cursor3 != null ){
			    			num1 = cursor3.getCount();
			    			cursor3.close();
			    		}
			    	}else if ("toupiao".equals(appState.workfloat)){
			    		cursor3 = appState.queryTable_tijiaostate("2");
			    		if (cursor3 != null ){
			    			num1 = cursor3.getCount();
			    			cursor3.close();
			    		}
			    		cursor3 = appState.queryTable_tijiaostate("3");
			    		if (cursor3 != null ){
			    			num2 = cursor3.getCount();
			    			cursor3.close();
			    		}
			    		
			    		num1 += num2;
			    	}else if ("pinyu".equals(appState.workfloat)){
			    		cursor3 = appState.queryFailed_zhuangtai("0");
			    		if (cursor3 != null ){
			    			num1 = cursor3.getCount();
			    			cursor3.close();
			    		}
			    	}
			    	appState.dbClose();
			    	setTitle("��ǰ������Ա��" + String.valueOf(appState.people_cur + 1) + 
							" �� " + 
							"���в�����Ա��" + String.valueOf(appState.people_total) + 
							"����ǰ�ѱ��棺" + String.valueOf(num1)
							);
			    	
			   
					//------------------
					
					Log.i("info", "viewFlipper1.showPrevious()");
					}else{
						Toast toast = Toast.makeText(getApplicationContext(), "�Ѿ��ǵ�һ��������Ա��", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						Log.i("info", "��һ��������Ա");
					}
				}
			} catch (Exception e) {
				// nothing
				Log.i("info", e.toString());
			}
			return false;
		}

		
	}
	// ----------------------------------------
	
	private void clearPre() {
		// TODO Auto-generated method stub
		if ("pinfen".equals(appState.workfloat)) {
    		SharedPreferences userInfo = getSharedPreferences("tab5_pinfen", 0);  
    			userInfo.edit().putString("f1", "").commit();
    			userInfo.edit().putString("f2", "").commit();
    			userInfo.edit().putString("f3", "").commit();  
    			userInfo.edit().putString("po", "").commit();  
				userInfo.edit().putString("postate","not_use").commit();    
    		}else if ("toupiao".equals(appState.workfloat)) {
    			SharedPreferences userInfo = getSharedPreferences("tab5_toupiao", 0);  
    			userInfo.edit().putString("toupiao", "").commit();
    		}else if ("pinyu".equals(appState.workfloat)){
    			SharedPreferences userInfo = getSharedPreferences("tab_pinyu", 0);  
    			userInfo.edit().putString("pinyu","").commit();
    		}
	}
	
	 @Override    
		protected void onDestroy() { 
	    	super.onDestroy(); 
	    	clearPre();
	 }

}
