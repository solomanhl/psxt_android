/**
 * ѡ�����
 * 
 * @author ����
 * 
 */
package com.gky.zcps_android;

import java.io.BufferedReader;
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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class EntranceActivity extends Activity {
	private Global_var appState; // ���ȫ�ֱ���;
	private FileUtils file = new FileUtils();

	private Button button_login, button_download, button_start, button_xiaozuyijian,
			button_toupiaostart, button_quit, button_jiyu, button_czsm0;
	private EditText userName;
	private EditText passWord;
	private ProgressBar progressBar1;

	private TextView user, mima, info;

	private Cursor cursor;

	private boolean downloadfinish = false;
	private boolean parseXMLfinish = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		appState = ((Global_var) getApplicationContext()); // ���ȫ�ֱ���
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// ���óɺ���

		// �ж��Ƿ����2.3
		isMoreThan();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_entrance);

		button_login = (Button) findViewById(R.id.button_login);
		button_download = (Button) findViewById(R.id.button_download);
		button_start = (Button) findViewById(R.id.button_start);
		button_xiaozuyijian= (Button) findViewById(R.id.button_xiaozuyijian);
		button_toupiaostart = (Button) findViewById(R.id.button_toupiaostart);
		button_quit = (Button) findViewById(R.id.button_quit);
		button_jiyu = (Button) findViewById(R.id.button_jiyu);
		button_czsm0 = (Button) findViewById(R.id.button_czsm0);

		user = (TextView) findViewById(R.id.user);
		mima = (TextView) findViewById(R.id.mima);
		info = (TextView) findViewById(R.id.info);
		userName = (EditText) findViewById(R.id.user_name);
		passWord = (EditText) findViewById(R.id.pass_word);

		progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);

		button_start.setEnabled(false);
		button_start.setVisibility(View.INVISIBLE);
		button_xiaozuyijian.setEnabled(false);
		button_xiaozuyijian.setVisibility(View.INVISIBLE);		
		button_toupiaostart.setEnabled(false);
		button_toupiaostart.setVisibility(View.INVISIBLE);
		button_jiyu.setEnabled(false);
		button_jiyu.setVisibility(View.INVISIBLE);
		// button_quit.setEnabled(false);
		// button_quit.setVisibility(View.INVISIBLE);

		// �õ���ǰ�̵߳�Looperʵ�������ڵ�ǰ�߳���UI�߳�Ҳ����ͨ��Looper.getMainLooper()�õ�
		Looper looper = Looper.myLooper();
		// �˴��������Բ���Ҫ����Looper����Ϊ HandlerĬ�Ͼ�ʹ�õ�ǰ�̵߳�Looper
		messageHandler = new MessageHandler(looper);

		appState.firstIn = true;
		downloadfinish = false;
		parseXMLfinish = false;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i("info", "Entrance_onResume");

		// ��������δ�ύ�ģ�tijiaostate=0���ύ����

		// ����ͶƱδ�ύ�ģ�tijiaostate=2���ύͶƱ

		if (!appState.firstIn) { // ��������һ�ν���ʱ���жϣ������¼���߷��ص�ʱ���ж�
			// ѡ�����
			selectWorkfoat();
			// appState.firstIn = false;
		}

	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (!appState.firstIn) { // ��������һ�ν���ʱ���жϣ������¼���߷��ص�ʱ���ж�
			if (appState.xianchangfenzu) {
				runThread = false;
			}
		}
	}

	// �˵�-------------------------------------------------
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_menu, menu);
		menu.add(0,0, 0,"�������");
		menu.add(0,1, 0,"�������");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// ��Ӧÿ���˵���(ͨ���˵����ID)
		case 0:// �������
				// do something here
			alertAbout();
			break;
		case 1:// �������
				// do something here
			updateAPK();
			break;
		default:
			// ��û�д�����¼�����������������
			return super.onOptionsItemSelected(item);
		}
		// ����true��ʾ������˵�����¼�������Ҫ�����¼�����������ȥ��
		return true;
	}

	// --------------------------------------------------------

	private void alertAbout() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this)
				.setTitle("�������")
				.setMessage(
						this.getString(R.string.about) + "\n\n��ǰ�汾��"
								+ this.getString(R.string.ver))
				/*
				 * .setNegativeButton("ȡ��", new
				 * DialogInterface.OnClickListener() {
				 * 
				 * @Override public void onClick(DialogInterface dialog, int
				 * which) {
				 * 
				 * } })
				 */
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				}).show();
	}

	private void updateAPK() {		
		Log.i("info", "����APK");
		new dlapkThread().start();		
	}

	// ����apk����----------------------------------------
	public class dlapkThread extends Thread {

		public dlapkThread() {

		}

		@Override
		public void run() {
			downloadapk();
			
			//openapk
			
		}

	}
	
	private void downloadapk(){
		//����
		updateHandler(null);
		//if ( appState.panduanfuwuqi() ){//���������ͨ��
			// ����apk
			file.deleteFile(appState.SDpath + "psxt/sites/default/files/psxt_android.apk");
			int f = appState.dl_file("sites/default/files/", "psxt_android.apk", "");
			// -1���� 0���� 1�Ѿ�����
			if (f == 0) {

			} else if (f == -1) {
				//Toast toast = Toast.makeText(getApplicationContext(),"��ǰû�д��ٿ��������", Toast.LENGTH_LONG);
				Toast toast = Toast.makeText(getApplicationContext(),"���ظ��°�����", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
			updateHandler(null);
			
			//��
			SendIntent SDintent = new SendIntent();
			Intent it = SDintent.getIntent(appState.SDpath + "psxt/sites/default/files/psxt_android.apk");

			try {
				startActivity(it);
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("error", e.toString());
				Toast toast = Toast.makeText(getApplicationContext(), "��װ������",
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
			
		//}
		
		
	}
	
	
	
	//�ֳ�������getWorkfloat()
	public void getWokfloat(){

					// Ҫ���ʵ�web servlet
					// ע�⣺IP�Ͷ˿��Ǳ��ص� ��Ҫ�������IP�Ͷ˿�
					String servletUrl = appState.HttpHead + "/expert/complete?pwid=" + URLEncoder.encode(appState.pinweiName);
					
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
//								Toast toast = Toast.makeText(getApplicationContext(), line, Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();

								line = in.readLine();
							}
							in.close();
							conn.disconnect();
							
							
							//downloadfinish = true;  //ǿ����true
							if ("��������".equals(tmp)){
								appState.workfloat = "pinfen";
								button_toupiaostart.setVisibility(View.INVISIBLE);
								button_toupiaostart.setEnabled(false);
								button_xiaozuyijian.setVisibility(View.INVISIBLE);
								button_xiaozuyijian.setEnabled(false);
								button_start.setVisibility(View.VISIBLE);
								button_start.setEnabled(true);
							}else if ("����ͶƱ".equals(tmp)){
								appState.workfloat = "toupiao";
								button_toupiaostart.setVisibility(View.VISIBLE);
								button_toupiaostart.setEnabled(true);
								button_start.setVisibility(View.INVISIBLE);
								button_start.setEnabled(false);
								button_xiaozuyijian.setVisibility(View.INVISIBLE);
								button_xiaozuyijian.setEnabled(false);

								appState.people_cur = 0;
								
								if (appState.pinshenjieshu) {
									exitAPP();
								}
							}else if ("����д���".equals(tmp)){
								appState.workfloat = "xiaozuyijian";
								
								//��������鳤��ֱ������ͶƱ
								if (  ! ("����ίԱ".equals(appState.weiyuanjibie)
										|| "�鳤".equals(appState.weiyuanjibie)
										|| "ίԱ".equals(appState.weiyuanjibie))  ) {	//����������������κ���ί��½��ֱ�ӽ���С�����
									button_toupiaostart.setVisibility(View.VISIBLE);
									button_toupiaostart.setEnabled(true);
									button_start.setVisibility(View.INVISIBLE);
									button_start.setEnabled(false);
									button_xiaozuyijian.setVisibility(View.INVISIBLE);
									button_xiaozuyijian.setEnabled(false);

									appState.people_cur = 0;
								}else {
								button_toupiaostart.setVisibility(View.INVISIBLE);
								button_toupiaostart.setEnabled(false);
								button_start.setVisibility(View.INVISIBLE);
								button_start.setEnabled(false);
								button_xiaozuyijian.setVisibility(View.VISIBLE);
								button_xiaozuyijian.setEnabled(true);
								}
							}else if ("ͶƱ����".equals(tmp)) {
								exitAPP();
							}else if("�������".equals(tmp)){
								exitAPP();
//								appState.getDB();
//								//����������˳������
//								appState.clearTable("canpinrenyuan");// ������ݿ�
//								appState.clearTable("failed");// ������ݿ�
//								appState.clearTable("pwh");// ������ݿ�
//								appState.dbClose();
//								file.delFolder(appState.SDpath + "psxt");
//								
//								// ����һ����ʾ��
//								new AlertDialog.Builder(this)
//										.setTitle("��Ϣ")
//										.setMessage("����������Ѿ����������˳���")
//										/*
//										 * .setNegativeButton("ȡ��", new
//										 * DialogInterface.OnClickListener() {
//										 * 
//										 * @Override public void onClick(DialogInterface
//										 * dialog, int which) {
//										 * 
//										 * } })
//										 */
//										.setPositiveButton("ȷ��",
//												new DialogInterface.OnClickListener() {
//													public void onClick(
//															DialogInterface dialog,
//															int whichButton) {
//														finish();
//													}
//												}).show();
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
	
	
	public void exitAPP () {
		
		
		appState.getDB();
		//����������˳������
		appState.clearTable("canpinrenyuan");// ������ݿ�
		appState.clearTable("failed");// ������ݿ�
		appState.clearTable("pwh");// ������ݿ�
		appState.dbClose();
		file.delFolder(appState.SDpath + "psxt");
		
		// ����һ����ʾ��
		new AlertDialog.Builder(this)
				.setTitle("��Ϣ")
				.setMessage("����������Ѿ����������˳���")
				/*
				 * .setNegativeButton("ȡ��", new
				 * DialogInterface.OnClickListener() {
				 * 
				 * @Override public void onClick(DialogInterface
				 * dialog, int which) {
				 * 
				 * } })
				 */
				.setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialog,
									int whichButton) {
								
									runThread = false;//ֹͣ�߳�
									try {
										updateworkfloatT.sleep(1);
										if (updateworkfloatT != null){
											updateworkfloatT.interrupt();
										}
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}		
									android.os.Process.killProcess(android.os.Process.myPid());  

							}
						}).show();
	}

	public Thread updateworkfloatT;
	public boolean runThread;
	private void selectWorkfoat() {
		// TODO Auto-generated method stub
		//������ֳ�����
		if (appState.xianchangfenzu) {
			//�����������׶�       ��������   ����д���   ����ͶƱ
			//д���߳����� ��������
			//getWokfloat();
			runThread = true;
			updateworkfloatT = new updateWorkfloatThread();
			updateworkfloatT.start();
			
			button_login.setVisibility(View.INVISIBLE);

		}else { //�����ֳ�����
			if ("denglu".equals(appState.workfloat)) {
				// button_login.setText("��ί��¼");
				// button_login.setTextColor(0xffffffff);
				button_login.setBackgroundResource(R.drawable.denlu_selector);
			} else if ("kan1".equals(appState.workfloat)) {
				// button_login.setText("�鿴����");
				// button_login.setTextColor(0xaaff0000);
				button_login.setBackgroundResource(R.drawable.jilv_selector);
			} else if ("kan2".equals(appState.workfloat)) {
				// button_login.setText("�鿴����");
				// button_login.setTextColor(0xaa00aa00);
				button_login.setBackgroundResource(R.drawable.zhence1_selector);			
			} else if ("yichen".equals(appState.workfloat)) {
				// button_login.setText("�鿴���");
				// button_login.setTextColor(0xaa00aa00);
				button_login.setBackgroundResource(R.drawable.yichen);
			} else {
				button_login.setVisibility(View.INVISIBLE);

				appState.getDB();
				// ��������˶��ύ�ˣ����̽��뵽ͶƱ
				// cursor = appState.getAll();
				cursor = appState.queryTable_tijiaostate("1");// �ύ״̬������/�ύ����/����ͶƱ/�ύͶƱ��0
																// 1 2 3
				int cnt1 = 0;
				int cnt2 = 0;
				int cnt3 = 0;
				if (cursor != null) {
					cnt1 = cursor.getCount();
				}
				Cursor cursor2 = appState.queryTable_tijiaostate("2");
				if (cursor2 != null) {
					cnt2 = cursor2.getCount();
				}
				Cursor cursor3 = appState.queryTable_tijiaostate("3");
				if (cursor3 != null) {
					cnt3 = cursor3.getCount();
				}

				if ((cursor != null || cursor2 != null || cursor3 != null)
						&& (cnt1 + cnt2 + cnt3) >= appState.people_pinfentotal
						&& appState.people_pinfentotal != 0) {
					// ���ж��Ƿ�ȫ���ύ����

					appState.workfloat = "toupiao"; // ����pinfen����ͶƱtoupiao
					button_toupiaostart.setVisibility(View.VISIBLE);
					button_toupiaostart.setEnabled(true);
					button_start.setVisibility(View.INVISIBLE);
					button_start.setEnabled(false);
					button_xiaozuyijian.setVisibility(View.INVISIBLE);
					button_xiaozuyijian.setEnabled(false);

					// temp��ʱ��appState.people_cur��0
					appState.people_cur = 0;
				} else {
					appState.workfloat = "pinfen"; // ����pinfen����ͶƱtoupiao
					button_toupiaostart.setVisibility(View.INVISIBLE);
					button_toupiaostart.setEnabled(false);
					button_xiaozuyijian.setVisibility(View.INVISIBLE);
					button_xiaozuyijian.setEnabled(false);
					button_start.setVisibility(View.VISIBLE);
					button_start.setEnabled(true);
				}
				cursor.close();
				cursor2.close();
				cursor3.close();

				// ��������˵�tijiaostate��Ϊ3����ȫ��ͶƱ�Ѿ��ύ
				cursor = appState.queryTable_tijiaostate("3");
				if (cursor != null && cursor.getCount() >= appState.people_total
						&& appState.people_total != 0) {
					cursor.close();
					// ȫ���ύ
					appState.workfloat = "wanchen";

					button_login.setEnabled(false);
					button_download.setEnabled(false);
					button_start.setEnabled(false);
					button_toupiaostart.setEnabled(false);
					
					button_xiaozuyijian.setVisibility(View.INVISIBLE);
					button_xiaozuyijian.setEnabled(false);

					button_login.setVisibility(View.INVISIBLE);
					button_start.setVisibility(View.INVISIBLE);
					button_toupiaostart.setVisibility(View.INVISIBLE);

					if ("����ίԱ".equals(appState.weiyuanjibie)
							|| "�鳤".equals(appState.weiyuanjibie)) {
						button_start.setEnabled(false);
						button_toupiaostart.setEnabled(false);
						button_jiyu.setVisibility(View.VISIBLE);
						button_jiyu.setEnabled(true);

						button_xiaozuyijian.setVisibility(View.INVISIBLE);
						button_xiaozuyijian.setEnabled(false);
						
						button_start.setVisibility(View.INVISIBLE);
						button_toupiaostart.setVisibility(View.INVISIBLE);

						cursor = appState.queryFailed_zhuangtai("1");

						// ���ȫ���ύ
						if ((cursor != null && cursor.getCount() > 0
								&& appState.failedList != null
								&& cursor.getCount() == appState.failedList.size() && appState.failedList
									.size() != 0)) {
							cursor.close();

							appState.clearTable("canpinrenyuan");// ������ݿ�
							appState.clearTable("failed");// ������ݿ�
							appState.clearTable("pwh");// ������ݿ�
							file.delFolder(appState.SDpath + "psxt");

							// ����һ����ʾ��
							new AlertDialog.Builder(this)
									.setTitle("��Ϣ")
									.setMessage("�������μ����Ѿ��ύ��ɣ����˳���")
									/*
									 * .setNegativeButton("ȡ��", new
									 * DialogInterface.OnClickListener() {
									 * 
									 * @Override public void onClick(DialogInterface
									 * dialog, int which) {
									 * 
									 * } })
									 */
									.setPositiveButton("ȷ��",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int whichButton) {
													finish();
												}
											}).show();
						}

					} else if ("ίԱ".equals(appState.weiyuanjibie)
							|| "������ίԱ".equals(appState.weiyuanjibie)) {
						appState.clearTable("canpinrenyuan");// ������ݿ�
						appState.clearTable("pwh");// ������ݿ�
						file.delFolder(appState.SDpath + "psxt");
						// ����һ����ʾ��
						new AlertDialog.Builder(this)
								.setTitle("��Ϣ")
								.setMessage("���в�����Ա�Ѿ�ͶƱ��ɣ����˳���")
								/*
								 * .setNegativeButton("ȡ��", new
								 * DialogInterface.OnClickListener() {
								 * 
								 * @Override public void onClick(DialogInterface
								 * dialog, int which) {
								 * 
								 * } })
								 */
								.setPositiveButton("ȷ��",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int whichButton) {
												finish();
											}
										}).show();
					}
					cursor.close();
				}
				cursor.close();
				appState.dbClose();
			}
		}
		
		
		
	}

	public void isMoreThan() {
		float fv = Float.valueOf(appState.version.substring(0, 3).trim());
		if (fv > 2.3) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork() // ��������滻ΪdetectAll()
																			// �Ͱ����˴��̶�д������I/O
					.penaltyLog() // ��ӡlogcat����ȻҲ���Զ�λ��dropbox��ͨ���ļ�������Ӧ��log
					.build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects() // ̽��SQLite���ݿ����
					.penaltyLog() // ��ӡlogcat
					.penaltyDeath().build());
		}
	}

	// ��¼��ť����¼�
	public void button_login_onclick(View target) {
		Log.i("info", "�����¼");
		appState.firstIn = false;

		if ("denglu".equals(appState.workfloat)) {
			appState.panduanfuwuqi();
			// ��ȡ��ί����
			String username = URLEncoder.encode(userName.getText().toString());
			// ��ȡ����
			String password = passWord.getText().toString();

			// Ҫ���ʵ�web servlet
			// ע�⣺IP�Ͷ˿��Ǳ��ص� ��Ҫ�������IP�Ͷ˿�
			String servletUrl = appState.HttpHead + "/expert/login?";
			// ����������������
			String params = "userName=" + username + "&" + "passWord="
					+ password;
			// ����������·��
			String requestUrl = servletUrl + params;
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

					String tmpchenguoArray[] = tmp.split("\\|");

				  if ("��¼�ɹ�".equals(tmpchenguoArray[0])) {
					//if (true){  //����
						appState.pwhid = tmpchenguoArray[2];

						userName.setVisibility(4);// ����
						passWord.setVisibility(4);// ����
						// button_login.setVisibility(4);
						user.setVisibility(4);
						mima.setVisibility(View.INVISIBLE);
						/*
						 * mima.setVisibility(0);
						 * mima.setText(userName.getText() + "�ѵ�¼");
						 * mima.setTextSize(32); mima.setTextColor(0xaaffffff);
						 * mima.setBackgroundDrawable(null);
						 */

						info.setVisibility(View.VISIBLE);
						info.setText(userName.getText() + "�ѵ�¼");
						info.setTextSize(32);
						info.setTextColor(0xaaffffff);

						appState.pinweiName = userName.getText().toString();

						appState.workfloat = "kan1";
						// button_login.setText("�鿴����");
						// button_login.setTextColor(0x88ff0000);
						button_login
								.setBackgroundResource(R.drawable.jilv_selector);

						// ����ίԱ| ������ίԱ|ίԱ|�鳤
						appState.weiyuanjibie = tmpchenguoArray[1];

						//�Ƿ��ֳ�����
						if ("�ֳ�����".equals(tmpchenguoArray[3])) {
							appState.xianchangfenzu = true;
							appState.workfloat = "pinfen";
//							button_start.setVisibility(View.VISIBLE);
//							button_start.setEnabled(true);
							selectWorkfoat();
//							//�ֳ�����ֱ�������������棬ͶƱ�׶γ���
//							if ("pinfen".equals(appState.workfloat)){
//								Intent it = new Intent(EntranceActivity.this, searchActivity.class);
//								//Bundle bundle = new Bundle();
//								//it.putExtras(bundle);
//
//								startActivity(it);
//							}
//
//							
//							
						}else{						
							appState.xianchangfenzu = false;
						}
						
						if (!appState.xianchangfenzu){  //�����ֳ�����
							// ��������
							button_download_onclick(target);// ���߳�
						}
						


						// �鿴���� ���� ���߳�
						// new readThread().start();
						// read_jilv();
						// read_zhence();

						/*
						 * if
						 * (file.isFileExist("psxt/conference/applicant.xml")) {
						 * // ����һ���� appState.decodeXML(appState.SDpath +
						 * "psxt/conference/applicant.xml");
						 * button_start.setEnabled(true); }else{ //��������
						 * button_download_onclick(target); }
						 */
					} else if ("��¼ʧ��".equals(tmpchenguoArray[0])) {
						//userName.setText("");
						passWord.setText("");
					}

					in.close();
					conn.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
					Log.i("info", e.toString());
					Toast toast = Toast.makeText(getApplicationContext(),
							"��¼��ʱ����ִ���", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			} catch (MalformedURLException e) {

				e.printStackTrace();
			}

		} else if ("kan1".equals(appState.workfloat)) {
			appState.workfloat = "kan2";
			read_jilv();
		} else if ("kan2".equals(appState.workfloat)) {
			if (parseXMLfinish) {
				appState.workfloat = "yichen";
				read_zhence();
				// button_start.setVisibility(View.VISIBLE);
				// button_start.setEnabled(true);
			}else{
				Toast toast = Toast.makeText(getApplicationContext(), "���ڴ������ݣ����Ժ󡭡�", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		} else if ("yichen".equals(appState.workfloat)) {
			appState.workfloat = "pinfen";
			read_yichen();
			button_start.setVisibility(View.VISIBLE);
			button_start.setEnabled(true);
		}
	}

	// �鿴����
	private void read_yichen() {
		// TODO Auto-generated method stub
		int i = 0;

		// String localPath = "sites/default/files/jilv/";
		// String fileName = "yichen.doc";


		/*
		 * try { InputStream in = getResources().getAssets().open(fileName); //
		 * д������ FileUtils file = new FileUtils();
		 * file.writeFromInput(appState.SDpath + "psxt/" + localPath, fileName,
		 * in);
		 * 
		 * } catch (Exception e) { e.printStackTrace(); }
		 */

		SendIntent SDintent = new SendIntent();
		// Intent it = SDintent.getIntent(appState.SDpath + "psxt/" + localPath + fileName);
		Intent it = SDintent.getIntent(appState.SDpath + "psxt/sites/default/files/yichen.doc");

		try {
			startActivity(it);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("error", e.toString());
			Toast toast = Toast.makeText(getApplicationContext(),
					"û�а�װ�Ķ���/���������û������ļ���", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

	// �鿴����
	private void read_jilv() {
		// TODO Auto-generated method stub
		int i = 0;

		String localPath = "sites/default/files/jilv/";
		String fileName = "jilv.doc";

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
					"û�а�װ�Ķ���/���������û������ļ���", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

	// �鿴����
	private void read_zhence() {
		// TODO Auto-generated method stub
		long i = 0;
		while (appState.peopleList == null && i < 65535) {
			i++;
		}
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

	// ���� ���� ����----------------------------------------
	public class readThread extends Thread {

		public readThread() {

		}

		@Override
		public void run() {
			read_jilv();
			read_zhence();
		}

	}

	// ���ذ�ť����¼�
	public void button_download_onclick(View target) {
		Log.i("info", "�������");

		new dlThread().start();

	}

	// ��ʼ���ְ�ť����¼�
	public void button_start_onclick(View target) {
		Log.i("info", "�����ʼ����");
		appState.firstIn = false;
				
			if (downloadfinish) {
				appState.searchLastPeople("0");// �ύ״̬������/�ύ����/����ͶƱ/�ύͶƱ��0 1 2 3
				appState.tab5_state = "pinfen";

				// �鿴���� ���� ���߳�
				// new readThread().start();

				//�Ƿ��ֳ�����
				if (appState.xianchangfenzu) {
					
					//selectWorkfoat();
					// �ֳ��������ֽ׶�ֱ��������������
					if ("pinfen".equals(appState.workfloat)) {
						button_start.setEnabled(false);
						// �ֳ�����ֱ��������������
						Intent it = new Intent(EntranceActivity.this, searchWithListActivity.class);
						startActivity(it);
						button_start.setEnabled(true);
					}
					
					
				}else {
				Intent it = new Intent(EntranceActivity.this, MainActivity.class);

				Bundle bundle = new Bundle();
				bundle.putString("jiyu", "��ͨ");
				it.putExtras(bundle);

				startActivity(it);
				}
			} else {
				
				 Toast toast = Toast.makeText(getApplicationContext(), "�����������ϣ����Ժ�...", Toast.LENGTH_LONG);
				 toast.setGravity(Gravity.CENTER, 0, 0); toast.show();
				 
			}

		
		
		
	}

	
	// С�������ť����¼�
		public void xiaozuyijian_onclick(View target) {
			Log.i("info", "С�����");
			appState.firstIn = false;
		
//			appState.tab5_state = "xiaozuyijian";
//			Intent it = new Intent(EntranceActivity.this, searchActivity.class);
//			startActivity(it);			
			button_xiaozuyijian.setEnabled(false);
			
			new updateOpinionThread().start();
		}
	
		// С�����ǰ���½���----------------------------------------
		public class updateOpinionThread extends Thread {

			public updateOpinionThread() {

			}

			@Override
			public void run() {
				updateOpinion();
			}
		}
		
		private void updateOpinion() {

			String applicant = "applicant_one.xml";
//			String applicant = "applicant.xml";
//				if (downloadfinish) {

					
					// ����һ�λ�����Ϣ��
					// �ж���ͨ
					if (appState.panduanfuwuqi()) {// ���������ͨ��
						// �������������ǰ��/���ܲ��裩
						updateHandler( "1/3");
						
						int f = 0;
						// ����һ����

							file.deleteFile(appState.SDpath + "psxt/conference/" + applicant);
							f = appState.dl_file("conference/", applicant,
									"?pwid=" + URLEncoder.encode(appState.pinweiName)
										+ "&code=\'\'" 
											+ "&type=2");
						
//						f = appState.dl_file("conference/", "applicant.xml", "?pwid="
//								+ URLEncoder.encode(appState.pinweiName) + "&type=0");					
						
						
						// �������������ǰ��/���ܲ��裩
						updateHandler( "1/3");
						// -1���� 0���� 1�Ѿ�����
						if (f == 0) {

						} else if (f == -1) {
							Toast toast = Toast.makeText(getApplicationContext(),
									"��ǰû�д��ٿ��������", Toast.LENGTH_LONG);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}

						
						// �������������ǰ��/���ܲ��裩
						updateHandler( "2/3");
						// ����һ����
						appState.decodeXML(appState.SDpath + "psxt/conference/" + applicant);
						// �������������ǰ��/���ܲ��裩
						updateHandler( "2/3");
						Log.i("info", "С�����ǰ����" + applicant);
					}
					


						// �������������ǰ��/���ܲ��裩
						updateHandler( "3/3");
//						download_score();
//						updateHandler( "3/3");

						// �������������ǰ��/���ܲ��裩
						updateHandler( "successful" );
						appState.tab5_state = "xiaozuyijian";
						
						
						
						Intent it = new Intent(EntranceActivity.this, xiaozuyijianActicity.class);
						startActivity(it);
//				}
			
				
		}
		
	
	// ��ʼͶƱ��ť����¼�
	public void button_toupiaostart_onclick(View target) {
		Log.i("info", "�����ʼͶƱ");
		
		if ("toupiao".equals(appState.workfloat)) {
			appState.firstIn = false;
			button_toupiaostart.setEnabled(false);
			new dlscoreThread().start();		
		}else {
			Toast toast = Toast.makeText(getApplicationContext(),"���ڵȴ�����С�飬���Ժ�...", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
		
	}
	
	
	// �������ֽ���----------------------------------------
	public class dlscoreThread extends Thread {

		public dlscoreThread() {

		}

		@Override
		public void run() {
			downloadscore();
			
		}

	}
	
	private void downloadscore() {
				
		String applicant = "";
		if (appState.xianchangfenzu){
			runThread = false;//ֹͣ�߳�
			try {
				updateworkfloatT.sleep(1);
				if (updateworkfloatT != null){
					updateworkfloatT.interrupt();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			applicant = "applicant_one.xml";
		}else{
			applicant = "applicant.xml";
		}
//			if (downloadfinish) {

//				if ( download_score() ){	//����ʱ����

					updateHandler( "0/4");
				// ����һ�λ�����Ϣ��
				// �ж���ͨ
				//if (appState.panduanfuwuqi()) {// ���������ͨ��
					// �������������ǰ��/���ܲ��裩
					updateHandler( "1/4");
					
					int f = 0;
					// ����һ����
					if (appState.xianchangfenzu) {
						file.deleteFile(appState.SDpath + "psxt/conference/" + applicant);
						f = appState.dl_file("conference/", applicant,
								"?pwid=" + URLEncoder.encode(appState.pinweiName)
									+ "&code=\'\'" 
										+ "&type=1");
					}else{
						file.deleteFile(appState.SDpath + "psxt/conference/" + applicant);
						f = appState.dl_file("conference/", applicant,
								"?pwid=" + URLEncoder.encode(appState.pinweiName)
										+ "&type=1");
					}
					
					// �������������ǰ��/���ܲ��裩
					updateHandler( "1/4");
					// -1���� 0���� 1�Ѿ�����
					if (f == 0) {

					} else if (f == -1) {
						Toast toast = Toast.makeText(getApplicationContext(),
								"��ǰû�д��ٿ��������", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}

					
					// �������������ǰ��/���ܲ��裩
					updateHandler( "2/4");
					// ����һ����
					appState.decodeXML(appState.SDpath + "psxt/conference/" + applicant);
					// �������������ǰ��/���ܲ��裩
					updateHandler( "2/4");
					Log.i("info", "ͶƱǰ����" + applicant);
				//}

				boolean ceshiwanchen = true;
				
				if (appState.xianchangfenzu) {
					if (appState.peopleList != null ) {
						// �ж��Ƿ�������
						for (int i = 0; i < appState.people_total; i++) {
							// �������������ǰ��/���ܲ��裩
							updateHandler( "3/4");
							if ("01".equals(appState.scoreList.get(i).get("noeduc")
									.toString())
									&& "".equals(appState.scoreList.get(i)
											.get("dbresult").toString())) {
								ceshiwanchen = false;
								break;
							}
						}
					} else {
						ceshiwanchen = false;
					}
				}else {
					if (appState.peopleList != null && appState.people_total > 0) {
						// �ж��Ƿ�������
						for (int i = 0; i < appState.people_total; i++) {
							// �������������ǰ��/���ܲ��裩
							updateHandler( "3/4");
							if ("01".equals(appState.scoreList.get(i).get("noeduc")
									.toString())
									&& "".equals(appState.scoreList.get(i)
											.get("dbresult").toString())) {
								ceshiwanchen = false;
								break;
							}
						}
					} else {
						ceshiwanchen = false;
					}
				}
				
				
				
				if (ceshiwanchen) {
					// �������������ǰ��/���ܲ��裩
					updateHandler( "4/4");
					appState.searchLastPeople("2");// �ύ״̬������/�ύ����/����ͶƱ/�ύͶƱ��0 1 2 3
					// �������������ǰ��/���ܲ��裩
					updateHandler( "successful" );
					appState.tab5_state = "toupiao";					
					
//					Intent it = new Intent(EntranceActivity.this, MainActivity.class);
					Intent it = new Intent(EntranceActivity.this, xiaozuyijianActicity.class);

					Bundle bundle = new Bundle();
					bundle.putString("jiyu", "��ͨ");
					it.putExtras(bundle);

					startActivity(it);
				} else {
//					runThread = true;
//					updateworkfloatT.start();
//					Toast toast = Toast.makeText(getApplicationContext(),
//							"���ڵȴ�������ί��ɣ����Ժ�...", Toast.LENGTH_LONG);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
				}
//				}	//����ʱ����
//			}
		
		
	}
	
	
	
	

	// ��ʼ���ְ�ť����¼�
	public void button_quit_onclick(View target) {
		// finish();
		new AlertDialog.Builder(this).setTitle("�˳�����")
				.setMessage("�Ƿ�Ҫ�˳��人�е�������ϵͳ��")
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				})
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						//finish();//finish()ֻ����ֹ���̣߳����߳���Ȼ����
						android.os.Process.killProcess(android.os.Process.myPid());  
					}
				}).show();
	}

	// ����ίԱ���鳤�����ﰴť����¼�
	public void button_jiyu_onclick(View target) {
		boolean jiyu = false;
		// �ж��Ƿ������д����
		appState.panduanfuwuqi();
		// ��ȡ��ί����
		String username = URLEncoder.encode(userName.getText().toString());

		// Ҫ���ʵ�web servlet
		// ע�⣺IP�Ͷ˿��Ǳ��ص� ��Ҫ�������IP�Ͷ˿�
		String servletUrl = appState.HttpHead + "/getfailed";
		// ����������������

		// ����������·��
		String requestUrl = servletUrl;// + params;
		System.out.println("url===" + requestUrl);
		try {
			URL url = new URL(requestUrl);
			try {
				// �������
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setConnectTimeout(appState.REQUEST_TIMEOUT);
				conn.setReadTimeout(appState.SO_TIMEOUT);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				String line = in.readLine();
				String tmp = line;

				if ("�ȴ�".equals(tmp)) {
					jiyu = false;
				} else if ("��ʼ".equals(tmp)) {
					jiyu = true;
				}
			} catch (IOException e) {
				e.printStackTrace();
				Toast toast = Toast.makeText(getApplicationContext(),
						"���μ���������������ִ���", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		} catch (MalformedURLException e) {

			e.printStackTrace();
		}
		// ------------------

		if (jiyu) {
			// �ж���ͨ
			if (appState.panduanfuwuqi()) {// ���������ͨ��
				// ����һ����
				file.deleteFile(appState.SDpath + "psxt/expert/failed.xml");
				int f = appState.dl_file("expert/", "failed.xml", "?pwid="
						+ URLEncoder.encode(appState.pinweiName));
				// -1���� 0���� 1�Ѿ�����
				if (f == 0) {

				} else if (f == -1) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"���س���", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
				// ����һ����
				appState.decodeXML_failed(appState.SDpath
						+ "psxt/expert/failed.xml");
				if (appState.failedList != null
						&& appState.failedList.size() > 0) {
					// �в�ͨ����
					appState.people_cur = 0;
					appState.people_total = appState.failedList.size();
					appState.workfloat = "pinyu";

					Intent it = new Intent(EntranceActivity.this,
							MainActivity.class);

					Bundle bundle = new Bundle();
					bundle.putString("jiyu", "���μ���");
					it.putExtras(bundle);

					startActivity(it);
				} else {
					// û�в�ͨ����
					appState.getDB();
					appState.clearTable("canpinrenyuan");// ������ݿ�
					appState.clearTable("failed");// ������ݿ�
					appState.clearTable("pwh");// ������ݿ�
					appState.dbClose();
					file.delFolder(appState.SDpath + "psxt");
					// exitTag = true;
					// ����һ����ʾ��
					new AlertDialog.Builder(this)
							.setTitle("��Ϣ")
							.setMessage("���м�����д��ɣ����˳���")
							/*
							 * .setNegativeButton("ȡ��", new
							 * DialogInterface.OnClickListener() {
							 * 
							 * @Override public void onClick(DialogInterface
							 * dialog, int which) {
							 * 
							 * } })
							 */
							.setPositiveButton("ȷ��",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											finish();
										}
									}).show();

				}
			}
		} else {// �����ܿ�ʼд����

		}
	}

	// ���ؽ���----------------------------------------
	public class dlThread extends Thread {

		public dlThread() {

		}

		@Override
		public void run() {
			download();
		}

	}

	private Handler messageHandler;

	private void updateHandler(Object obj) {
		// ����һ��Message���󣬲��ѵõ���������Ϣ��ֵ��Message����
		Message message = Message.obtain();// ��һ��
		message = Message.obtain();// ��һ��
		message.obj = obj; // �ڶ���
		messageHandler.sendMessage(message);// ������
	}

	private void download() {
		// TODO Auto-generated method stub

		updateHandler(null);
		// updateHandler("������Ա");

		// �ж���ͨ
		if (appState.panduanfuwuqi()) {// ���������ͨ��
			// ����һ����
			
			//���ص��Ե�ʱ����ʱ���ε�
			file.deleteFile(appState.SDpath + "psxt/conference/applicant.xml");
			int f = appState.dl_file("conference/", "applicant.xml", "?pwid="
					+ URLEncoder.encode(appState.pinweiName) + "&type=0");					
			
			// -1���� 0���� 1�Ѿ�����
			if (f == 0) {

			} else if (f == -1) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"��ǰû�д��ٿ��������", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
			
			
			
			// ����һ����
			appState.decodeXML(appState.SDpath + "psxt/conference/applicant.xml");
			appState.people_pinfentotal = appState.people_total;

			if (appState.people_total > 0) {
				parseXMLfinish = true;	
			} else {
				parseXMLfinish = false;
			}
			// ��һ�����ø�����Ϣ

			// ���￪ʼ��ȡ���жϷ�������pwhid�뱾�ص�pwhid�Ƿ����
			String tmppwhid = "";
			String needDownload = "no";
			appState.getDB();

			Cursor cursorpwh;
			cursorpwh = appState.getpwh();
			Log.i("info", "pwh Cursor = " + cursorpwh.toString());
			if (cursorpwh != null && cursorpwh.getCount() != 0) {// ����ί���¼���Ƚ�
				cursorpwh.moveToNext();
				tmppwhid = cursorpwh.getString(0);
				if (tmppwhid.equals(appState.pwhid)) {// ��ͬ ��������
					needDownload = "no";
				} else {// ������Ҫ���أ��������ݿ�
					needDownload = "yes";
					appState.Update_pwh(tmppwhid, appState.pwhid);// ����oldpwhid, newpwhid
				}

			} else {// ������Ҫ���أ�������ݿ�
				needDownload = "yes";
				appState.addpinweihui(appState.pwhid);
			}

			cursorpwh.close();
			appState.dbClose();

			if ("no".equals(needDownload)) { // �������������ί��pwhid�뱾��pwhid��ȣ�������
				// ����һ���ɹ���handler��Ϣ
				updateHandler("successful");
			} else { // �������ظ���
				// �������
				String tmpyichen = appState.yichen;
				// ��·�����ļ����ֿ�
				String pathyichen = "sites/default/files/";
				String filenameyichen = "yichen.doc";

				if (filenameyichen != "" && filenameyichen != null)
					appState.dl_file(pathyichen, filenameyichen, "");

				// ����ÿ���˵ĸ���
				for (int i = 0; i < appState.people_total; i++) {
					// ���ؽ���������ǰ��/����������
					updateHandler(String.valueOf(i + 1) + "/"
							+ String.valueOf(appState.people_total));
					
					/*
					 * �ĳ�����һ�������Ƭ
					 */
					/* ��ʱ����
					//һ����
					String tmp = appState.peopleList.get(i).get("yilanbiao").toString();
					String tmpyilanbiaoArray[] = tmp.split("\\|");
					for (int j = 0; j < tmpyilanbiaoArray.length; j++) {// һ����
						// ��·�����ļ����ֿ�
						String path = "";
						String filename;
						String pathArray[] = tmpyilanbiaoArray[j].split("/");
						for (int z = 0; z < pathArray.length - 1; z++) {
							// ǰ�涼��·�������һ�����ļ���
							path = path + pathArray[z] + "/";
						}
						filename = pathArray[pathArray.length - 1];

						if (filename != "" && filename != null)
							appState.dl_file(path, filename, "");
					}
					//��Ƭ
					tmp = appState.peopleList.get(i).get("zhaopian").toString();
					String tmpzhaopianArray[] = tmp.split("\\|");
					for (int j = 0; j < tmpzhaopianArray.length; j++) {// ��Ƭ
						// ��·�����ļ����ֿ�
						String path = "";
						String filename;
						String pathArray[] = tmpzhaopianArray[j].split("/");
						for (int z = 0; z < pathArray.length - 1; z++) {
							// ǰ�涼��·�������һ�����ļ���
							path = path + pathArray[z] + "/";
						}
						filename = pathArray[pathArray.length - 1];

						if (filename != "" && filename != null)
							appState.dl_file(path, filename, "");
					}
					*/

					/*֤�����ɹ������ġ����ö���������
					// ֤��
					String tmp = appState.peopleList.get(i).get("xuelifujian")
							.toString();
					// ��·�����ļ����ֿ�
					String path1 = "";
					String filename1;
					String pathArray1[] = tmp.split("/");
					for (int z = 0; z < pathArray1.length - 1; z++) {
						// ǰ�涼��·�������һ�����ļ���
						path1 = path1 + pathArray1[z] + "/";
					}
					filename1 = pathArray1[pathArray1.length - 1];

					if (filename1 != "" && filename1 != null)
						appState.dl_file(path1, filename1, "");

					tmp = appState.chengguoList.get(i).get("chengguofujian")
							.toString();
					String tmpchenguoArray[] = tmp.split("\\|");
					for (int j = 0; j < tmpchenguoArray.length; j++) {// �ɹ�
						// ��·�����ļ����ֿ�
						String path = "";
						String filename;
						String pathArray[] = tmpchenguoArray[j].split("/");
						for (int z = 0; z < pathArray.length - 1; z++) {
							// ǰ�涼��·�������һ�����ļ���
							path = path + pathArray[z] + "/";
						}
						filename = pathArray[pathArray.length - 1];

						if (filename != "" && filename != null)
							appState.dl_file(path, filename, "");
					}

					tmp = appState.lunwenList.get(i).get("lunwenfujian")
							.toString();
					String tmplunwenArray[] = tmp.split("\\|");
					for (int j = 0; j < tmplunwenArray.length; j++) {// ����
						// ��·�����ļ����ֿ�
						String path = "";
						String filename;
						String pathArray[] = tmplunwenArray[j].split("/");
						for (int z = 0; z < pathArray.length - 1; z++) {
							// ǰ�涼��·�������һ�����ļ���
							path = path + pathArray[z] + "/";
						}
						filename = pathArray[pathArray.length - 1];

						if (filename != "" && filename != null)
							appState.dl_file(path, filename, "");
					}

					tmp = appState.huojiangList.get(i).get("huojiangfujian")
							.toString();
					String tmphuojiangArray[] = tmp.split("\\|");
					for (int j = 0; j < tmphuojiangArray.length; j++) {// ����
						// ��·�����ļ����ֿ�
						String path = "";
						String filename;
						String pathArray[] = tmphuojiangArray[j].split("/");
						for (int z = 0; z < pathArray.length - 1; z++) {
							// ǰ�涼��·�������һ�����ļ���
							path = path + pathArray[z] + "/";
						}
						filename = pathArray[pathArray.length - 1];

						if (filename != "" && filename != null)
							appState.dl_file(path, filename, "");
					}
					*/

					// ȫ�������꣬ʹ��button_start.setEnabled(true);
					updateHandler("successful");
				}
			}

			// ѡ�����
			// selectWorkfoat();//ֱ�ӵ��ú��и��¿ؼ��ķ����ǲ��ǻᱨ��
			downloadfinish = true;
			updateHandler("selectWorkfoat");
		} else {// ��������ͨ
			Toast toast = Toast.makeText(getApplicationContext(),
					"�޷����ӵ������������������磡", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();

		}
	}

	// ���໯һ��Handler
	class MessageHandler extends Handler {
		public MessageHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			// ����UI
			if (!((String) msg.obj == null)) {
				if ("successful".equals((String) msg.obj)) {
					// button_start.setVisibility(View.VISIBLE);
					// button_start.setEnabled(true);
					progressBar1.setVisibility(View.GONE);// 0�ɼ� 4���� 8gone
					info.setText(userName.getText() + "�ѵ�¼");
					
					if ("xiaozuyijian".equals(appState.workfloat))
						button_xiaozuyijian.setEnabled(true);
					if ("toupiao".equals(appState.workfloat))
						button_toupiaostart.setEnabled(true);
				} else if ("selectWorkfoat".equals((String) msg.obj)) {
					if (appState.xianchangfenzu) {
						getWokfloat();
					}else{
						selectWorkfoat();
					}
				} else {
					// ���½���
					String progressArray[] = ((String) msg.obj).split("/");
					progressBar1.setVisibility(View.VISIBLE);// 0�ɼ� 4���� 8gone
					progressBar1.setMax(Integer.valueOf(progressArray[1]));
					progressBar1.setProgress(Integer.valueOf(progressArray[0]));

					info.setText(userName.getText() + "�ѵ�¼" + "    ��������"
							+ (String) msg.obj + "�����Ժ�....");
				}
			}

		}
	}

	private boolean download_score() {
		// TODO Auto-generated method stub
		try {
			// �ж���ͨ
			if (appState.panduanfuwuqi()) {// ���������ͨ��
				// ����һ����
				file.deleteFile(appState.SDpath + "psxt/expert/score.xml");
				appState.dl_file("expert/", "score.xml", "");
				Log.i("info", "������score.xml");
				// ����һ����
				try {
					appState.decodeXML_score(appState.SDpath
							+ "psxt/expert/score.xml");
				} catch (Exception e) {
					e.printStackTrace();
					Log.i("info", "decodeXML_score()��������");
					return false;
				}
				// -1���� 0���� 1�Ѿ�����
				if (appState.scoreList != null) {
					return true;
				} else {
					Toast toast = Toast.makeText(getApplicationContext(),
							"������ί�������֣����Ժ�����...", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					return false;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("info", e.toString());
			return false;
		}

		return true;
	}

	// ------------------------------------------------------------------

	// ��ⰴ��
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// ���¼����Ϸ��ذ�ť
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			System.out.println("���ذ�ť");
			new AlertDialog.Builder(this)
					.setTitle("�˳�����")
					.setMessage("�Ƿ�Ҫ�˳��人�е�������ϵͳ��")
					.setNegativeButton("ȡ��",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							})
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									//finish();//���߳��޷��˳�
									android.os.Process.killProcess(android.os.Process.myPid());
								}
							}).show();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	// �����f����ť����¼�
	public void button_czsm0_onclick(View target) {
		appState.launch_help();
	}
	
	
	// ����״̬k����----------------------------------------
		public class updateWorkfloatThread extends Thread {

			public updateWorkfloatThread() {

			}

			private int cnt = 0;
			
			@Override
			public void run() {
				while (runThread && !this.isInterrupted() ) {
					System.out.println("updateWorkfloatThread run again");
					cnt ++;
					if ( cnt == 1) {
						updateHandler("selectWorkfoat");
					}
					if ( cnt >150) {
						cnt = 0;
					}
					
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				}
				
			}

		}
}
