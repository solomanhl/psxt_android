/**
 * 选择入口
 * 
 * @author 贺亮
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
	private Global_var appState; // 获得全局变量;
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
		appState = ((Global_var) getApplicationContext()); // 获得全局变量
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 设置成横屏

		// 判断是否大于2.3
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

		// 得到当前线程的Looper实例，由于当前线程是UI线程也可以通过Looper.getMainLooper()得到
		Looper looper = Looper.myLooper();
		// 此处甚至可以不需要设置Looper，因为 Handler默认就使用当前线程的Looper
		messageHandler = new MessageHandler(looper);

		appState.firstIn = true;
		downloadfinish = false;
		parseXMLfinish = false;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i("info", "Entrance_onResume");

		// 存在评分未提交的，tijiaostate=0，提交评分

		// 存在投票未提交的，tijiaostate=2，提交投票

		if (!appState.firstIn) { // 点击程序第一次进的时候不判断，点击登录或者返回的时候判断
			// 选择入口
			selectWorkfoat();
			// appState.firstIn = false;
		}

	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (!appState.firstIn) { // 点击程序第一次进的时候不判断，点击登录或者返回的时候判断
			if (appState.xianchangfenzu) {
				runThread = false;
			}
		}
	}

	// 菜单-------------------------------------------------
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_menu, menu);
		menu.add(0,0, 0,"关于软件");
		menu.add(0,1, 0,"软件升级");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// 响应每个菜单项(通过菜单项的ID)
		case 0:// 关于软件
				// do something here
			alertAbout();
			break;
		case 1:// 软件更新
				// do something here
			updateAPK();
			break;
		default:
			// 对没有处理的事件，交给父类来处理
			return super.onOptionsItemSelected(item);
		}
		// 返回true表示处理完菜单项的事件，不需要将该事件继续传播下去了
		return true;
	}

	// --------------------------------------------------------

	private void alertAbout() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this)
				.setTitle("关于软件")
				.setMessage(
						this.getString(R.string.about) + "\n\n当前版本："
								+ this.getString(R.string.ver))
				/*
				 * .setNegativeButton("取消", new
				 * DialogInterface.OnClickListener() {
				 * 
				 * @Override public void onClick(DialogInterface dialog, int
				 * which) {
				 * 
				 * } })
				 */
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				}).show();
	}

	private void updateAPK() {		
		Log.i("info", "下载APK");
		new dlapkThread().start();		
	}

	// 下载apk进程----------------------------------------
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
		//下载
		updateHandler(null);
		//if ( appState.panduanfuwuqi() ){//如果服务器通了
			// 下载apk
			file.deleteFile(appState.SDpath + "psxt/sites/default/files/psxt_android.apk");
			int f = appState.dl_file("sites/default/files/", "psxt_android.apk", "");
			// -1出错 0正常 1已经存在
			if (f == 0) {

			} else if (f == -1) {
				//Toast toast = Toast.makeText(getApplicationContext(),"当前没有待召开的评审会", Toast.LENGTH_LONG);
				Toast toast = Toast.makeText(getApplicationContext(),"下载更新包出错", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
			updateHandler(null);
			
			//打开
			SendIntent SDintent = new SendIntent();
			Intent it = SDintent.getIntent(appState.SDpath + "psxt/sites/default/files/psxt_android.apk");

			try {
				startActivity(it);
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("error", e.toString());
				Toast toast = Toast.makeText(getApplicationContext(), "安装包错误！",
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
			
		//}
		
		
	}
	
	
	
	//现场分组用getWorkfloat()
	public void getWokfloat(){

					// 要访问的web servlet
					// 注意：IP和端口是本地的 需要换成你的IP和端口
					String servletUrl = appState.HttpHead + "/expert/complete?pwid=" + URLEncoder.encode(appState.pinweiName);
					
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
//								Toast toast = Toast.makeText(getApplicationContext(), line, Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();

								line = in.readLine();
							}
							in.close();
							conn.disconnect();
							
							
							//downloadfinish = true;  //强制置true
							if ("正在评分".equals(tmp)){
								appState.workfloat = "pinfen";
								button_toupiaostart.setVisibility(View.INVISIBLE);
								button_toupiaostart.setEnabled(false);
								button_xiaozuyijian.setVisibility(View.INVISIBLE);
								button_xiaozuyijian.setEnabled(false);
								button_start.setVisibility(View.VISIBLE);
								button_start.setEnabled(true);
							}else if ("正在投票".equals(tmp)){
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
							}else if ("正在写意见".equals(tmp)){
								appState.workfloat = "xiaozuyijian";
								
								//如果不是组长，直接跳到投票
								if (  ! ("主任委员".equals(appState.weiyuanjibie)
										|| "组长".equals(appState.weiyuanjibie)
										|| "委员".equals(appState.weiyuanjibie))  ) {	//按照曲主任意见，任何评委登陆后直接进入小组意见
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
							}else if ("投票结束".equals(tmp)) {
								exitAPP();
							}else if("评审结束".equals(tmp)){
								exitAPP();
//								appState.getDB();
//								//评审结束，退出，清空
//								appState.clearTable("canpinrenyuan");// 清空数据库
//								appState.clearTable("failed");// 清空数据库
//								appState.clearTable("pwh");// 清空数据库
//								appState.dbClose();
//								file.delFolder(appState.SDpath + "psxt");
//								
//								// 弹出一个提示框
//								new AlertDialog.Builder(this)
//										.setTitle("信息")
//										.setMessage("本次评审会已经结束，请退出！")
//										/*
//										 * .setNegativeButton("取消", new
//										 * DialogInterface.OnClickListener() {
//										 * 
//										 * @Override public void onClick(DialogInterface
//										 * dialog, int which) {
//										 * 
//										 * } })
//										 */
//										.setPositiveButton("确定",
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
									"请求超时或出现错误", Toast.LENGTH_LONG);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					} catch (MalformedURLException e) {

						e.printStackTrace();
					}
	}
	
	
	public void exitAPP () {
		
		
		appState.getDB();
		//评审结束，退出，清空
		appState.clearTable("canpinrenyuan");// 清空数据库
		appState.clearTable("failed");// 清空数据库
		appState.clearTable("pwh");// 清空数据库
		appState.dbClose();
		file.delFolder(appState.SDpath + "psxt");
		
		// 弹出一个提示框
		new AlertDialog.Builder(this)
				.setTitle("信息")
				.setMessage("本次评审会已经结束，请退出！")
				/*
				 * .setNegativeButton("取消", new
				 * DialogInterface.OnClickListener() {
				 * 
				 * @Override public void onClick(DialogInterface
				 * dialog, int which) {
				 * 
				 * } })
				 */
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialog,
									int whichButton) {
								
									runThread = false;//停止线程
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
		//如果是现场分组
		if (appState.xianchangfenzu) {
			//想服务器请求阶段       正在评分   正在写意见   正在投票
			//写到线程里面 不断请求
			//getWokfloat();
			runThread = true;
			updateworkfloatT = new updateWorkfloatThread();
			updateworkfloatT.start();
			
			button_login.setVisibility(View.INVISIBLE);

		}else { //不是现场分组
			if ("denglu".equals(appState.workfloat)) {
				// button_login.setText("评委登录");
				// button_login.setTextColor(0xffffffff);
				button_login.setBackgroundResource(R.drawable.denlu_selector);
			} else if ("kan1".equals(appState.workfloat)) {
				// button_login.setText("查看纪律");
				// button_login.setTextColor(0xaaff0000);
				button_login.setBackgroundResource(R.drawable.jilv_selector);
			} else if ("kan2".equals(appState.workfloat)) {
				// button_login.setText("查看政策");
				// button_login.setTextColor(0xaa00aa00);
				button_login.setBackgroundResource(R.drawable.zhence1_selector);			
			} else if ("yichen".equals(appState.workfloat)) {
				// button_login.setText("查看议程");
				// button_login.setTextColor(0xaa00aa00);
				button_login.setBackgroundResource(R.drawable.yichen);
			} else {
				button_login.setVisibility(View.INVISIBLE);

				appState.getDB();
				// 如果所有人都提交了，流程进入到投票
				// cursor = appState.getAll();
				cursor = appState.queryTable_tijiaostate("1");// 提交状态（保存/提交评分/保存投票/提交投票）0
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
					// 待判断是否全部提交分数

					appState.workfloat = "toupiao"; // 评分pinfen或者投票toupiao
					button_toupiaostart.setVisibility(View.VISIBLE);
					button_toupiaostart.setEnabled(true);
					button_start.setVisibility(View.INVISIBLE);
					button_start.setEnabled(false);
					button_xiaozuyijian.setVisibility(View.INVISIBLE);
					button_xiaozuyijian.setEnabled(false);

					// temp临时将appState.people_cur置0
					appState.people_cur = 0;
				} else {
					appState.workfloat = "pinfen"; // 评分pinfen或者投票toupiao
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

				// 如果所有人的tijiaostate都为3，则全部投票已经提交
				cursor = appState.queryTable_tijiaostate("3");
				if (cursor != null && cursor.getCount() >= appState.people_total
						&& appState.people_total != 0) {
					cursor.close();
					// 全部提交
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

					if ("主任委员".equals(appState.weiyuanjibie)
							|| "组长".equals(appState.weiyuanjibie)) {
						button_start.setEnabled(false);
						button_toupiaostart.setEnabled(false);
						button_jiyu.setVisibility(View.VISIBLE);
						button_jiyu.setEnabled(true);

						button_xiaozuyijian.setVisibility(View.INVISIBLE);
						button_xiaozuyijian.setEnabled(false);
						
						button_start.setVisibility(View.INVISIBLE);
						button_toupiaostart.setVisibility(View.INVISIBLE);

						cursor = appState.queryFailed_zhuangtai("1");

						// 如果全部提交
						if ((cursor != null && cursor.getCount() > 0
								&& appState.failedList != null
								&& cursor.getCount() == appState.failedList.size() && appState.failedList
									.size() != 0)) {
							cursor.close();

							appState.clearTable("canpinrenyuan");// 清空数据库
							appState.clearTable("failed");// 清空数据库
							appState.clearTable("pwh");// 清空数据库
							file.delFolder(appState.SDpath + "psxt");

							// 弹出一个提示框
							new AlertDialog.Builder(this)
									.setTitle("信息")
									.setMessage("所有主任寄语已经提交完成，请退出！")
									/*
									 * .setNegativeButton("取消", new
									 * DialogInterface.OnClickListener() {
									 * 
									 * @Override public void onClick(DialogInterface
									 * dialog, int which) {
									 * 
									 * } })
									 */
									.setPositiveButton("确定",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int whichButton) {
													finish();
												}
											}).show();
						}

					} else if ("委员".equals(appState.weiyuanjibie)
							|| "副主任委员".equals(appState.weiyuanjibie)) {
						appState.clearTable("canpinrenyuan");// 清空数据库
						appState.clearTable("pwh");// 清空数据库
						file.delFolder(appState.SDpath + "psxt");
						// 弹出一个提示框
						new AlertDialog.Builder(this)
								.setTitle("信息")
								.setMessage("所有参评人员已经投票完成，请退出！")
								/*
								 * .setNegativeButton("取消", new
								 * DialogInterface.OnClickListener() {
								 * 
								 * @Override public void onClick(DialogInterface
								 * dialog, int which) {
								 * 
								 * } })
								 */
								.setPositiveButton("确定",
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
					.detectDiskReads().detectDiskWrites().detectNetwork() // 这里可以替换为detectAll()
																			// 就包括了磁盘读写和网络I/O
					.penaltyLog() // 打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
					.build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
					.penaltyLog() // 打印logcat
					.penaltyDeath().build());
		}
	}

	// 登录按钮点击事件
	public void button_login_onclick(View target) {
		Log.i("info", "点击登录");
		appState.firstIn = false;

		if ("denglu".equals(appState.workfloat)) {
			appState.panduanfuwuqi();
			// 获取评委姓名
			String username = URLEncoder.encode(userName.getText().toString());
			// 获取密码
			String password = passWord.getText().toString();

			// 要访问的web servlet
			// 注意：IP和端口是本地的 需要换成你的IP和端口
			String servletUrl = appState.HttpHead + "/expert/login?";
			// 将参数传给服务器
			String params = "userName=" + username + "&" + "passWord="
					+ password;
			// 完整的请求路径
			String requestUrl = servletUrl + params;
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

					String tmpchenguoArray[] = tmp.split("\\|");

				  if ("登录成功".equals(tmpchenguoArray[0])) {
					//if (true){  //调试
						appState.pwhid = tmpchenguoArray[2];

						userName.setVisibility(4);// 隐藏
						passWord.setVisibility(4);// 隐藏
						// button_login.setVisibility(4);
						user.setVisibility(4);
						mima.setVisibility(View.INVISIBLE);
						/*
						 * mima.setVisibility(0);
						 * mima.setText(userName.getText() + "已登录");
						 * mima.setTextSize(32); mima.setTextColor(0xaaffffff);
						 * mima.setBackgroundDrawable(null);
						 */

						info.setVisibility(View.VISIBLE);
						info.setText(userName.getText() + "已登录");
						info.setTextSize(32);
						info.setTextColor(0xaaffffff);

						appState.pinweiName = userName.getText().toString();

						appState.workfloat = "kan1";
						// button_login.setText("查看纪律");
						// button_login.setTextColor(0x88ff0000);
						button_login
								.setBackgroundResource(R.drawable.jilv_selector);

						// 主任委员| 副主任委员|委员|组长
						appState.weiyuanjibie = tmpchenguoArray[1];

						//是否现场分组
						if ("现场分组".equals(tmpchenguoArray[3])) {
							appState.xianchangfenzu = true;
							appState.workfloat = "pinfen";
//							button_start.setVisibility(View.VISIBLE);
//							button_start.setEnabled(true);
							selectWorkfoat();
//							//现场分组直接跳到搜索界面，投票阶段除外
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
						
						if (!appState.xianchangfenzu){  //不是现场分组
							// 下载资料
							button_download_onclick(target);// 另开线程
						}
						


						// 查看政策 纪律 另开线程
						// new readThread().start();
						// read_jilv();
						// read_zhence();

						/*
						 * if
						 * (file.isFileExist("psxt/conference/applicant.xml")) {
						 * // 解析一览表 appState.decodeXML(appState.SDpath +
						 * "psxt/conference/applicant.xml");
						 * button_start.setEnabled(true); }else{ //下载资料
						 * button_download_onclick(target); }
						 */
					} else if ("登录失败".equals(tmpchenguoArray[0])) {
						//userName.setText("");
						passWord.setText("");
					}

					in.close();
					conn.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
					Log.i("info", e.toString());
					Toast toast = Toast.makeText(getApplicationContext(),
							"登录超时或出现错误", Toast.LENGTH_LONG);
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
				Toast toast = Toast.makeText(getApplicationContext(), "正在处理数据，请稍后……", Toast.LENGTH_LONG);
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

	// 查看纪律
	private void read_yichen() {
		// TODO Auto-generated method stub
		int i = 0;

		// String localPath = "sites/default/files/jilv/";
		// String fileName = "yichen.doc";


		/*
		 * try { InputStream in = getResources().getAssets().open(fileName); //
		 * 写到本地 FileUtils file = new FileUtils();
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
					"没有安装阅读器/浏览器，或没有相关文件！", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

	// 查看纪律
	private void read_jilv() {
		// TODO Auto-generated method stub
		int i = 0;

		String localPath = "sites/default/files/jilv/";
		String fileName = "jilv.doc";

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
					"没有安装阅读器/浏览器，或没有相关文件！", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

	// 查看政策
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

	// 纪律 政策 进程----------------------------------------
	public class readThread extends Thread {

		public readThread() {

		}

		@Override
		public void run() {
			read_jilv();
			read_zhence();
		}

	}

	// 下载按钮点击事件
	public void button_download_onclick(View target) {
		Log.i("info", "点击下载");

		new dlThread().start();

	}

	// 开始评分按钮点击事件
	public void button_start_onclick(View target) {
		Log.i("info", "点击开始评分");
		appState.firstIn = false;
				
			if (downloadfinish) {
				appState.searchLastPeople("0");// 提交状态（保存/提交评分/保存投票/提交投票）0 1 2 3
				appState.tab5_state = "pinfen";

				// 查看政策 纪律 另开线程
				// new readThread().start();

				//是否现场分组
				if (appState.xianchangfenzu) {
					
					//selectWorkfoat();
					// 现场分组评分阶段直接跳到搜索界面
					if ("pinfen".equals(appState.workfloat)) {
						button_start.setEnabled(false);
						// 现场分组直接跳到搜索界面
						Intent it = new Intent(EntranceActivity.this, searchWithListActivity.class);
						startActivity(it);
						button_start.setEnabled(true);
					}
					
					
				}else {
				Intent it = new Intent(EntranceActivity.this, MainActivity.class);

				Bundle bundle = new Bundle();
				bundle.putString("jiyu", "普通");
				it.putExtras(bundle);

				startActivity(it);
				}
			} else {
				
				 Toast toast = Toast.makeText(getApplicationContext(), "正在下载资料，请稍后...", Toast.LENGTH_LONG);
				 toast.setGravity(Gravity.CENTER, 0, 0); toast.show();
				 
			}

		
		
		
	}

	
	// 小组意见按钮点击事件
		public void xiaozuyijian_onclick(View target) {
			Log.i("info", "小组意见");
			appState.firstIn = false;
		
//			appState.tab5_state = "xiaozuyijian";
//			Intent it = new Intent(EntranceActivity.this, searchActivity.class);
//			startActivity(it);			
			button_xiaozuyijian.setEnabled(false);
			
			new updateOpinionThread().start();
		}
	
		// 小组意见前更新进程----------------------------------------
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

					
					// 再下一次基本信息表
					// 判断联通
					if (appState.panduanfuwuqi()) {// 如果服务器通了
						// 处理进度条（当前）/（总步骤）
						updateHandler( "1/3");
						
						int f = 0;
						// 下载一览表

							file.deleteFile(appState.SDpath + "psxt/conference/" + applicant);
							f = appState.dl_file("conference/", applicant,
									"?pwid=" + URLEncoder.encode(appState.pinweiName)
										+ "&code=\'\'" 
											+ "&type=2");
						
//						f = appState.dl_file("conference/", "applicant.xml", "?pwid="
//								+ URLEncoder.encode(appState.pinweiName) + "&type=0");					
						
						
						// 处理进度条（当前）/（总步骤）
						updateHandler( "1/3");
						// -1出错 0正常 1已经存在
						if (f == 0) {

						} else if (f == -1) {
							Toast toast = Toast.makeText(getApplicationContext(),
									"当前没有待召开的评审会", Toast.LENGTH_LONG);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}

						
						// 处理进度条（当前）/（总步骤）
						updateHandler( "2/3");
						// 解析一览表
						appState.decodeXML(appState.SDpath + "psxt/conference/" + applicant);
						// 处理进度条（当前）/（总步骤）
						updateHandler( "2/3");
						Log.i("info", "小组意见前更新" + applicant);
					}
					


						// 处理进度条（当前）/（总步骤）
						updateHandler( "3/3");
//						download_score();
//						updateHandler( "3/3");

						// 处理进度条（当前）/（总步骤）
						updateHandler( "successful" );
						appState.tab5_state = "xiaozuyijian";
						
						
						
						Intent it = new Intent(EntranceActivity.this, xiaozuyijianActicity.class);
						startActivity(it);
//				}
			
				
		}
		
	
	// 开始投票按钮点击事件
	public void button_toupiaostart_onclick(View target) {
		Log.i("info", "点击开始投票");
		
		if ("toupiao".equals(appState.workfloat)) {
			appState.firstIn = false;
			button_toupiaostart.setEnabled(false);
			new dlscoreThread().start();		
		}else {
			Toast toast = Toast.makeText(getApplicationContext(),"正在等待其他小组，请稍后...", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
		
	}
	
	
	// 下载评分进程----------------------------------------
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
			runThread = false;//停止线程
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

//				if ( download_score() ){	//调试时屏蔽

					updateHandler( "0/4");
				// 再下一次基本信息表
				// 判断联通
				//if (appState.panduanfuwuqi()) {// 如果服务器通了
					// 处理进度条（当前）/（总步骤）
					updateHandler( "1/4");
					
					int f = 0;
					// 下载一览表
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
					
					// 处理进度条（当前）/（总步骤）
					updateHandler( "1/4");
					// -1出错 0正常 1已经存在
					if (f == 0) {

					} else if (f == -1) {
						Toast toast = Toast.makeText(getApplicationContext(),
								"当前没有待召开的评审会", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}

					
					// 处理进度条（当前）/（总步骤）
					updateHandler( "2/4");
					// 解析一览表
					appState.decodeXML(appState.SDpath + "psxt/conference/" + applicant);
					// 处理进度条（当前）/（总步骤）
					updateHandler( "2/4");
					Log.i("info", "投票前更新" + applicant);
				//}

				boolean ceshiwanchen = true;
				
				if (appState.xianchangfenzu) {
					if (appState.peopleList != null ) {
						// 判断是否测试完成
						for (int i = 0; i < appState.people_total; i++) {
							// 处理进度条（当前）/（总步骤）
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
						// 判断是否测试完成
						for (int i = 0; i < appState.people_total; i++) {
							// 处理进度条（当前）/（总步骤）
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
					// 处理进度条（当前）/（总步骤）
					updateHandler( "4/4");
					appState.searchLastPeople("2");// 提交状态（保存/提交评分/保存投票/提交投票）0 1 2 3
					// 处理进度条（当前）/（总步骤）
					updateHandler( "successful" );
					appState.tab5_state = "toupiao";					
					
//					Intent it = new Intent(EntranceActivity.this, MainActivity.class);
					Intent it = new Intent(EntranceActivity.this, xiaozuyijianActicity.class);

					Bundle bundle = new Bundle();
					bundle.putString("jiyu", "普通");
					it.putExtras(bundle);

					startActivity(it);
				} else {
//					runThread = true;
//					updateworkfloatT.start();
//					Toast toast = Toast.makeText(getApplicationContext(),
//							"正在等待其他评委完成，请稍后...", Toast.LENGTH_LONG);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
				}
//				}	//调试时屏蔽
//			}
		
		
	}
	
	
	
	

	// 开始评分按钮点击事件
	public void button_quit_onclick(View target) {
		// finish();
		new AlertDialog.Builder(this).setTitle("退出程序")
				.setMessage("是否要退出武汉市电子评审系统？")
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				})
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						//finish();//finish()只能终止主线程，子线程仍然运行
						android.os.Process.killProcess(android.os.Process.myPid());  
					}
				}).show();
	}

	// 主任委员（组长）寄语按钮点击事件
	public void button_jiyu_onclick(View target) {
		boolean jiyu = false;
		// 判断是否可以填写寄语
		appState.panduanfuwuqi();
		// 获取评委姓名
		String username = URLEncoder.encode(userName.getText().toString());

		// 要访问的web servlet
		// 注意：IP和端口是本地的 需要换成你的IP和端口
		String servletUrl = appState.HttpHead + "/getfailed";
		// 将参数传给服务器

		// 完整的请求路径
		String requestUrl = servletUrl;// + params;
		System.out.println("url===" + requestUrl);
		try {
			URL url = new URL(requestUrl);
			try {
				// 获得连接
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setConnectTimeout(appState.REQUEST_TIMEOUT);
				conn.setReadTimeout(appState.SO_TIMEOUT);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				String line = in.readLine();
				String tmp = line;

				if ("等待".equals(tmp)) {
					jiyu = false;
				} else if ("开始".equals(tmp)) {
					jiyu = true;
				}
			} catch (IOException e) {
				e.printStackTrace();
				Toast toast = Toast.makeText(getApplicationContext(),
						"主任寄语请求服务器出现错误", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		} catch (MalformedURLException e) {

			e.printStackTrace();
		}
		// ------------------

		if (jiyu) {
			// 判断联通
			if (appState.panduanfuwuqi()) {// 如果服务器通了
				// 下载一览表
				file.deleteFile(appState.SDpath + "psxt/expert/failed.xml");
				int f = appState.dl_file("expert/", "failed.xml", "?pwid="
						+ URLEncoder.encode(appState.pinweiName));
				// -1出错 0正常 1已经存在
				if (f == 0) {

				} else if (f == -1) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"下载出错", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
				// 解析一览表
				appState.decodeXML_failed(appState.SDpath
						+ "psxt/expert/failed.xml");
				if (appState.failedList != null
						&& appState.failedList.size() > 0) {
					// 有不通过的
					appState.people_cur = 0;
					appState.people_total = appState.failedList.size();
					appState.workfloat = "pinyu";

					Intent it = new Intent(EntranceActivity.this,
							MainActivity.class);

					Bundle bundle = new Bundle();
					bundle.putString("jiyu", "主任寄语");
					it.putExtras(bundle);

					startActivity(it);
				} else {
					// 没有不通过的
					appState.getDB();
					appState.clearTable("canpinrenyuan");// 清空数据库
					appState.clearTable("failed");// 清空数据库
					appState.clearTable("pwh");// 清空数据库
					appState.dbClose();
					file.delFolder(appState.SDpath + "psxt");
					// exitTag = true;
					// 弹出一个提示框
					new AlertDialog.Builder(this)
							.setTitle("信息")
							.setMessage("所有寄语填写完成，请退出！")
							/*
							 * .setNegativeButton("取消", new
							 * DialogInterface.OnClickListener() {
							 * 
							 * @Override public void onClick(DialogInterface
							 * dialog, int which) {
							 * 
							 * } })
							 */
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											finish();
										}
									}).show();

				}
			}
		} else {// 还不能开始写寄语

		}
	}

	// 下载进程----------------------------------------
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
		// 创建一个Message对象，并把得到的网络信息赋值给Message对象
		Message message = Message.obtain();// 第一步
		message = Message.obtain();// 第一步
		message.obj = obj; // 第二步
		messageHandler.sendMessage(message);// 第三步
	}

	private void download() {
		// TODO Auto-generated method stub

		updateHandler(null);
		// updateHandler("参评人员");

		// 判断联通
		if (appState.panduanfuwuqi()) {// 如果服务器通了
			// 下载一览表
			
			//本地调试的时候暂时屏蔽掉
			file.deleteFile(appState.SDpath + "psxt/conference/applicant.xml");
			int f = appState.dl_file("conference/", "applicant.xml", "?pwid="
					+ URLEncoder.encode(appState.pinweiName) + "&type=0");					
			
			// -1出错 0正常 1已经存在
			if (f == 0) {

			} else if (f == -1) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"当前没有待召开的评审会", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
			
			
			
			// 解析一览表
			appState.decodeXML(appState.SDpath + "psxt/conference/applicant.xml");
			appState.people_pinfentotal = appState.people_total;

			if (appState.people_total > 0) {
				parseXMLfinish = true;	
			} else {
				parseXMLfinish = false;
			}
			// 从一览表获得附件信息

			// 这里开始读取并判断服务器的pwhid与本地的pwhid是否相等
			String tmppwhid = "";
			String needDownload = "no";
			appState.getDB();

			Cursor cursorpwh;
			cursorpwh = appState.getpwh();
			Log.i("info", "pwh Cursor = " + cursorpwh.toString());
			if (cursorpwh != null && cursorpwh.getCount() != 0) {// 有评委会记录，比较
				cursorpwh.moveToNext();
				tmppwhid = cursorpwh.getString(0);
				if (tmppwhid.equals(appState.pwhid)) {// 相同 不用下载
					needDownload = "no";
				} else {// 否则需要下载，更新数据库
					needDownload = "yes";
					appState.Update_pwh(tmppwhid, appState.pwhid);// 更新oldpwhid, newpwhid
				}

			} else {// 否则需要下载，添加数据库
				needDownload = "yes";
				appState.addpinweihui(appState.pwhid);
			}

			cursorpwh.close();
			appState.dbClose();

			if ("no".equals(needDownload)) { // 如果服务器的评委会pwhid与本地pwhid相等，则不下载
				// 更新一条成功的handler消息
				updateHandler("successful");
			} else { // 否则下载附件
				// 下载议程
				String tmpyichen = appState.yichen;
				// 吧路径和文件名分开
				String pathyichen = "sites/default/files/";
				String filenameyichen = "yichen.doc";

				if (filenameyichen != "" && filenameyichen != null)
					appState.dl_file(pathyichen, filenameyichen, "");

				// 下载每个人的附件
				for (int i = 0; i < appState.people_total; i++) {
					// 下载进度条（当前）/（总人数）
					updateHandler(String.valueOf(i + 1) + "/"
							+ String.valueOf(appState.people_total));
					
					/*
					 * 改成下载一览表和照片
					 */
					/* 暂时不下
					//一览表
					String tmp = appState.peopleList.get(i).get("yilanbiao").toString();
					String tmpyilanbiaoArray[] = tmp.split("\\|");
					for (int j = 0; j < tmpyilanbiaoArray.length; j++) {// 一览表
						// 吧路径和文件名分开
						String path = "";
						String filename;
						String pathArray[] = tmpyilanbiaoArray[j].split("/");
						for (int z = 0; z < pathArray.length - 1; z++) {
							// 前面都是路径，最后一个是文件名
							path = path + pathArray[z] + "/";
						}
						filename = pathArray[pathArray.length - 1];

						if (filename != "" && filename != null)
							appState.dl_file(path, filename, "");
					}
					//照片
					tmp = appState.peopleList.get(i).get("zhaopian").toString();
					String tmpzhaopianArray[] = tmp.split("\\|");
					for (int j = 0; j < tmpzhaopianArray.length; j++) {// 照片
						// 吧路径和文件名分开
						String path = "";
						String filename;
						String pathArray[] = tmpzhaopianArray[j].split("/");
						for (int z = 0; z < pathArray.length - 1; z++) {
							// 前面都是路径，最后一个是文件名
							path = path + pathArray[z] + "/";
						}
						filename = pathArray[pathArray.length - 1];

						if (filename != "" && filename != null)
							appState.dl_file(path, filename, "");
					}
					*/

					/*证件、成果、论文、表彰都不下载了
					// 证件
					String tmp = appState.peopleList.get(i).get("xuelifujian")
							.toString();
					// 吧路径和文件名分开
					String path1 = "";
					String filename1;
					String pathArray1[] = tmp.split("/");
					for (int z = 0; z < pathArray1.length - 1; z++) {
						// 前面都是路径，最后一个是文件名
						path1 = path1 + pathArray1[z] + "/";
					}
					filename1 = pathArray1[pathArray1.length - 1];

					if (filename1 != "" && filename1 != null)
						appState.dl_file(path1, filename1, "");

					tmp = appState.chengguoList.get(i).get("chengguofujian")
							.toString();
					String tmpchenguoArray[] = tmp.split("\\|");
					for (int j = 0; j < tmpchenguoArray.length; j++) {// 成果
						// 吧路径和文件名分开
						String path = "";
						String filename;
						String pathArray[] = tmpchenguoArray[j].split("/");
						for (int z = 0; z < pathArray.length - 1; z++) {
							// 前面都是路径，最后一个是文件名
							path = path + pathArray[z] + "/";
						}
						filename = pathArray[pathArray.length - 1];

						if (filename != "" && filename != null)
							appState.dl_file(path, filename, "");
					}

					tmp = appState.lunwenList.get(i).get("lunwenfujian")
							.toString();
					String tmplunwenArray[] = tmp.split("\\|");
					for (int j = 0; j < tmplunwenArray.length; j++) {// 论文
						// 吧路径和文件名分开
						String path = "";
						String filename;
						String pathArray[] = tmplunwenArray[j].split("/");
						for (int z = 0; z < pathArray.length - 1; z++) {
							// 前面都是路径，最后一个是文件名
							path = path + pathArray[z] + "/";
						}
						filename = pathArray[pathArray.length - 1];

						if (filename != "" && filename != null)
							appState.dl_file(path, filename, "");
					}

					tmp = appState.huojiangList.get(i).get("huojiangfujian")
							.toString();
					String tmphuojiangArray[] = tmp.split("\\|");
					for (int j = 0; j < tmphuojiangArray.length; j++) {// 表彰
						// 吧路径和文件名分开
						String path = "";
						String filename;
						String pathArray[] = tmphuojiangArray[j].split("/");
						for (int z = 0; z < pathArray.length - 1; z++) {
							// 前面都是路径，最后一个是文件名
							path = path + pathArray[z] + "/";
						}
						filename = pathArray[pathArray.length - 1];

						if (filename != "" && filename != null)
							appState.dl_file(path, filename, "");
					}
					*/

					// 全部下载完，使能button_start.setEnabled(true);
					updateHandler("successful");
				}
			}

			// 选择入口
			// selectWorkfoat();//直接调用含有更新控件的方法是不是会报错
			downloadfinish = true;
			updateHandler("selectWorkfoat");
		} else {// 服务器不通
			Toast toast = Toast.makeText(getApplicationContext(),
					"无法连接到服务器，请连接网络！", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();

		}
	}

	// 子类化一个Handler
	class MessageHandler extends Handler {
		public MessageHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			// 更新UI
			if (!((String) msg.obj == null)) {
				if ("successful".equals((String) msg.obj)) {
					// button_start.setVisibility(View.VISIBLE);
					// button_start.setEnabled(true);
					progressBar1.setVisibility(View.GONE);// 0可见 4隐藏 8gone
					info.setText(userName.getText() + "已登录");
					
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
					// 更新进度
					String progressArray[] = ((String) msg.obj).split("/");
					progressBar1.setVisibility(View.VISIBLE);// 0可见 4隐藏 8gone
					progressBar1.setMax(Integer.valueOf(progressArray[1]));
					progressBar1.setProgress(Integer.valueOf(progressArray[0]));

					info.setText(userName.getText() + "已登录" + "    正在下载"
							+ (String) msg.obj + "，请稍后....");
				}
			}

		}
	}

	private boolean download_score() {
		// TODO Auto-generated method stub
		try {
			// 判断联通
			if (appState.panduanfuwuqi()) {// 如果服务器通了
				// 下载一览表
				file.deleteFile(appState.SDpath + "psxt/expert/score.xml");
				appState.dl_file("expert/", "score.xml", "");
				Log.i("info", "已下载score.xml");
				// 解析一览表
				try {
					appState.decodeXML_score(appState.SDpath
							+ "psxt/expert/score.xml");
				} catch (Exception e) {
					e.printStackTrace();
					Log.i("info", "decodeXML_score()发生错误");
					return false;
				}
				// -1出错 0正常 1已经存在
				if (appState.scoreList != null) {
					return true;
				} else {
					Toast toast = Toast.makeText(getApplicationContext(),
							"其它评委正在评分，请稍后再试...", Toast.LENGTH_LONG);
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

	// 检测按键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下键盘上返回按钮
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			System.out.println("返回按钮");
			new AlertDialog.Builder(this)
					.setTitle("退出程序")
					.setMessage("是否要退出武汉市电子评审系统？")
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							})
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									//finish();//子线程无法退出
									android.os.Process.killProcess(android.os.Process.myPid());
								}
							}).show();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	// 操作說明按钮点击事件
	public void button_czsm0_onclick(View target) {
		appState.launch_help();
	}
	
	
	// 更新状态k进程----------------------------------------
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
