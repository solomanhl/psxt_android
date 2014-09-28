/**
 * 全局变量
 * 
 * @author 贺亮
 * 
 */
package com.gky.zcps_android;

import com.soloman.DB.Database;
import com.soloman.file.FileUtils;
import com.soloman.xml.XML;
import com.soloman.file.Net;
import com.soloman.intent.SendIntent;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;


public class Global_var extends Application {
	private final static int CWJ_HEAP_SIZE = 48* 1024* 1024 ; 
	
	public boolean xianchangfenzu = false;    //是现场分组，还是建评委会时分组
	public boolean lianghua = true;	//是否量化
	public boolean pinshenjieshu = false;		//评审结束标志
	
	public String pinweiName;
	public String weiyuanjibie = "";//主任委员|委员|组长
	public boolean jiyu = false;//主任评委是否填写了寄语
	
	public String pwhid ;//评委会id    现在用作的是分组id
	public String yichen; //议程链接

	public String firm;
	//public Integer version = Integer.valueOf(android.os.Build.VERSION.SDK);
	public String version = android.os.Build.VERSION.RELEASE;
	public float screenWidth, screenHeight;
	public float density; // 屏幕密度（0.75 / 1.0 /1.25/ 1.5）
	public int densityDpi; // 屏幕密度DPI（120 / 160 /200/ 240）
	public float wh; // 长宽比
	public TelephonyManager tm;
	public String IMEI;

	
	//
	public String SDpath;
	public String excelfile;

	public boolean closeMain = false;//是否关闭主窗体？
	public Boolean isUpdated = true;
	public boolean firstIn = true;//点击程序第一次进的时候不判断，点击登录或者返回的时候判断
	public String voteState = "未投票";//投票结果0 1 2 同意 赞成 反对
	public String pogebutton = "not_use"; //yes  no  not_use同意 不同意 未使用
	public String workfloat = "denglu"; //denglu登录  查看纪律kan1   查看政策kan2 评分pinfen，或者投票toupiao，或者评语 pinyu, 或者完成wanchen
	public String tab5_state= "pinfen"; //评分、投票tab专用
	
	public String gerenyijian = "yes";	//不量化评分，个人意见同意，不同意
	
	

	public TabWidget mTabWidget;
	public Intent layout1intent, layout2intent, layout3intent, layout4intent,
			layout5intent;
	public Intent layout1intent2, layout2intent2, layout3intent2,
			layout4intent2, layout5intent2;

	public TabHost tabhost;
	
	public int people_pinfentotal;//评分阶段的人数可能会少于投票阶段的
	public int people_total;
	public int people_cur;
	/*
	public String []chenguo;   //成果附件路径
	public String []lunwen; //论文附件路径
	public String []biaozhang; //表彰附件路径
	*/
	
	private Database database; 	
	
	

	public String getSDpath() {
		SDpath = Environment.getExternalStorageDirectory() + "/";
		return SDpath;
	}

	public void keepScreenAlive() {
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

		PowerManager.WakeLock mWakeLock = pm.newWakeLock(
				PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag"); // in onResume()
																// call
		mWakeLock.acquire(); // in onPause() call

		mWakeLock.release();
	}

	public void updateTabHost(String jiyu) {
		if ("普通".equals(jiyu)) {
			layout1intent = new Intent();
			layout1intent.setClass(this, Tab_Layout1.class);
			TabHost.TabSpec layout1spec = tabhost.newTabSpec("tab1");// 制造一个新的标签tab1
			layout1spec.setIndicator("基本信息")// ,
					// getResources().getDrawable(R.drawable.gimp))//
					// 设置一下显示的标题为("基本信息"，设置一下标签图标为gimp
					.setContent(layout1intent); // 设置一下该标签页的布局内容为layout1intent
			tabhost.addTab(layout1spec);

			/*只要基本信息和评分、投票
			layout2intent = new Intent();
			layout2intent.setClass(this, Tab_Layout2.class);
			TabHost.TabSpec layout2spec = tabhost.newTabSpec("tab2");// 制造一个新的标签tab2
			layout2spec.setIndicator("技术工作简历")// ,
					// getResources().getDrawable(R.drawable.mumule))//
					// 设置一下显示的标题为("考核与能力"，设置一下标签图标为gimp
					.setContent(layout2intent); // 设置一下该标签页的布局内容为layout2intent
			tabhost.addTab(layout2spec);

			layout3intent = new Intent();
			layout3intent.setClass(this, Tab_Layout3.class);
			TabHost.TabSpec layout3spec = tabhost.newTabSpec("tab3");// 制造一个新的标签tab3
			layout3spec.setIndicator("主要业绩与成果")// ,
					// getResources().getDrawable(R.drawable.gimp))//
					// 设置一下显示的标题为("业绩与成果"，设置一下标签图标为gimp
					.setContent(layout3intent); // 设置一下该标签页的布局内容为layout3intent
			tabhost.addTab(layout3spec);

			layout4intent = new Intent();
			layout4intent.setClass(this, Tab_Layout4.class);
			TabHost.TabSpec layout4spec = tabhost.newTabSpec("tab4");// 制造一个新的标签tab4
			layout4spec.setIndicator("论文发表与表彰")// ,
					// getResources().getDrawable(R.drawable.mumule))//
					// 设置一下显示的标题为("论文发表与"，设置一下标签图标为gimp
					.setContent(layout4intent); // 设置一下该标签页的布局内容为layout4intent
			tabhost.addTab(layout4spec);
			*/

			layout5intent = new Intent();
			layout5intent.setClass(this, Tab_Layout5.class);
			TabHost.TabSpec layout5spec = tabhost.newTabSpec("tab5");// 制造一个新的标签tab5
			if ("pinfen".equals(tab5_state)) {
				// 评分阶段
				layout5spec.setIndicator("评分、破格意见")// ,
						.setContent(layout5intent); // 设置一下该标签页的布局内容为layout4intent
			} else if ("toupiao".equals(tab5_state)) {
				layout5spec.setIndicator("投票")// ,
						// getResources().getDrawable(R.drawable.mumule))//
						// 设置一下显示的标题为("论文发表与"，设置一下标签图标为gimp
						.setContent(layout5intent); // 设置一下该标签页的布局内容为layout4intent
			}else if ("xiaozuyijian".equals(tab5_state)) {
				layout5spec.setIndicator("小组意见")// ,
				// getResources().getDrawable(R.drawable.mumule))//
				// 设置一下显示的标题为("论文发表与"，设置一下标签图标为gimp
				.setContent(layout5intent); // 设置一下该标签页的布局内容为layout4intent
	}
			tabhost.addTab(layout5spec);

			mTabWidget = tabhost.getTabWidget();
			/* 对Tab标签的定制 */
			for (int i = 0; i < mTabWidget.getChildCount(); i++) {
				/*
				 * /、* 得到每个标签的视图 View view = mTabWidget.getChildAt(i); /、* 设置每个标签的背景
				 * if (tabhost.getCurrentTab() == i) {
				 * view.setBackgroundDrawable(getResources().getDrawable(
				 * R.drawable.mumule));
				 * 
				 * } else { view.setBackgroundDrawable(getResources().getDrawable(
				 * R.drawable.gimp)); }
				 */
				TextView tv = (TextView) mTabWidget.getChildAt(i).findViewById(
						android.R.id.title);
				/* 设置tab内字体的大小 */
				tv.setTextSize(20);
			}
		}else if ("主任寄语".equals(jiyu)){
			layout1intent = new Intent();
			layout1intent.setClass(this, Tab_Layout_jiyu.class);
			TabHost.TabSpec layout_jiyu_spec = tabhost.newTabSpec("tab_jiyu");// 制造一个新的标签tab1
			layout_jiyu_spec.setIndicator("主任寄语")// ,
					// getResources().getDrawable(R.drawable.gimp))//
					// 设置一下显示的标题为("基本信息"，设置一下标签图标为gimp
					.setContent(layout1intent); // 设置一下该标签页的布局内容为layout1intent
			tabhost.addTab(layout_jiyu_spec);
			
			layout2intent = new Intent();
			layout2intent.setClass(this, Tab_Layout1.class);
			TabHost.TabSpec layout2spec = tabhost.newTabSpec("tab2");// 制造一个新的标签tab1
			layout2spec.setIndicator("基本信息")// ,
					// getResources().getDrawable(R.drawable.gimp))//
					// 设置一下显示的标题为("基本信息"，设置一下标签图标为gimp
					.setContent(layout2intent); // 设置一下该标签页的布局内容为layout1intent
			tabhost.addTab(layout2spec);
		}
		
		updateTabBackground();
	}
	
	/** 
     * 更新Tab标签的背景图 
     * 
     * @param tabHost 
     */ 
	//private void updateTabBackground(final TabHost tabHost) { 
    public void updateTabBackground() { 
        for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++) { 
            View vvv = tabhost.getTabWidget().getChildAt(i); 
            if (tabhost.getCurrentTab() == i) { 
                // 选中后的背景 
                //vvv.setBackgroundDrawable(getResources().getDrawable( android.R.drawable.spinner_background)); 
                vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_host)); 
            } else { 
                // 非选择的背景 
                //vvv.setBackgroundDrawable(null); 
            	vvv.setBackgroundDrawable(getResources().getDrawable( R.drawable.tab_host_back)); 
            } 
        } 
    } 
	
	
	public String selectZhence(){
		String fileName = "";
		if (peopleList != null){
			Log.i("info", peopleList.get(people_cur).get("shenbaopinweihui").toString());
			
			if ("图书资料".equals(peopleList.get(people_cur).get("shenbaopinweihui").toString())) {
				fileName = "tushu.doc";
			} else if ("档案".equals(peopleList.get(people_cur).get("shenbaopinweihui").toString())) {
				fileName = "dangan.doc";
			} else if ("播音".equals(peopleList.get(people_cur).get("shenbaopinweihui").toString())) {
				fileName = "boyin.doc";
			} else if ("社会科学".equals(peopleList.get(people_cur).get("shenbaopinweihui").toString())) {
				fileName = "shehui.doc";
			} else if ("经济".equals(peopleList.get(people_cur).get("shenbaopinweihui").toString())) {
				fileName = "jinji.doc";
			} else {
				fileName = "nothing.doc";
			}
		}
		return fileName;
	}
	
	//---------------------------------------------------------------------------------
	public Net net = new Net();
	//我的服务器
	public String HttpHead = "";
	public String HttpHead_bak = "http://192.168.2.111";
	public String HttpHead_bak1 = "http://192.168.2.111";
	public String HttpHead_bak2 = "http://solomanhl.3322.org:81";
		//private String HttpHead = "http://soloman.vicp.cc:8880";
		//private String HttpHead_bak = "http://solomanhl.3322.org:8880";
		//private String HttpHead_bak2 = "http://192.168.1.25:8880";
		
		//客户的服务器
		/*
		private String HttpHead = "http://moonchan.5166.info:8880";
		private String HttpHead_bak = "http://moonchan.3322.org:8880";
		private String HttpHead_bak2 = "http://192.168.1.25:8880";
		*/
		
		public boolean isServerOpen() {
			//HttpPost httpPost = new HttpPost(HttpHead + "/psxt/fujian/canpinrenyuan.xml");	
			HttpPost httpPost = new HttpPost(HttpHead + "/conference/applicant.xml");

			 HttpClient httpClient = getHttpClient();

			 int ret = 201;
			HttpResponse httpResponse = null;
			try {
				httpResponse = httpClient.execute(httpPost); //android4.x强行停止,
				ret = httpResponse.getStatusLine().getStatusCode();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.i("info",e.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.i("info",e.toString());
			}
			
			 if (HttpStatus.SC_OK == ret) {
			 //success
				 return true;
			 }else{
			 //link failed
				 return false;
			 }
		}
		
		public static final int REQUEST_TIMEOUT = 5*1000;//设置请求超时5秒钟  
		public static final int SO_TIMEOUT = 30*1000;  //设置等待数据超时时间30秒钟  
		   /** 
		    * 添加请求超时时间和等待时间 
		    * @author spring sky  
		    * Email vipa1888@163.com 
		    * QQ: 840950105 
		    * My name: 石明政 
		    * @return HttpClient对象 
		    */  
		   public HttpClient getHttpClient(){  
		    BasicHttpParams httpParams = new BasicHttpParams();  
		    HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);  
		    HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);  
		    HttpClient client = new DefaultHttpClient(httpParams);  
		    return client;  
		}  
	
		public boolean panduanfuwuqi(){
			HttpHead = HttpHead_bak;
			/*
			//多个域名
			if (isServerOpen()){
				Log.i("info",HttpHead + "is open");
			}else{
				HttpHead = HttpHead_bak1;
				if (isServerOpen()){
					System.out.println(HttpHead);
				}else{
					HttpHead = HttpHead_bak2;
					System.out.println(HttpHead);
					if (!isServerOpen()){
						return false;
					}
				}
			}
			*/
			
			//单个域名
			if (isServerOpen()){
				Log.i("info",HttpHead + "is open");
			}else{
				return false;
			}
			
			return true;
		}
		/*
		 * @param String remotepath:服务器路径，格式为/.../xx.yyy
		 * 
		 */
	public int dl_file(String filepath, String filename, String param) {
		// TODO Auto-generated method stub
		/*
		if (isServerOpen()){
			Log.i("info",HttpHead + "is open");
		}else{
			HttpHead = HttpHead_bak;
			if (isServerOpen()){
				System.out.println(HttpHead);
			}else{
				HttpHead = HttpHead_bak2;
				System.out.println(HttpHead);
			}
		}*/
		HttpHead = HttpHead_bak;
		int i = net.downFile(HttpHead + "/" + filepath + URLEncoder.encode(filename) + param, 
							SDpath + "psxt/" + filepath, 
							filename );
		Log.i("info",String.valueOf(i));
		//英文名两种都没问题
		//net.downFile(HttpHead + "/ct/food.xml" , 	"/ordering/", 	"food.xml");
		return i;
	}
	
	
	
	public FileUtils fileUtils = new FileUtils();
	// 参评人员
	public ArrayList<HashMap<String, Object>> peopleList = null;
	
	//成果论文获奖表彰都不要了
	public ArrayList<HashMap<String, Object>> chengguoList = null;
	public ArrayList<HashMap<String, Object>> lunwenList = null;
	public ArrayList<HashMap<String, Object>> huojiangList = null;
	
	
	

	// 解析XML得到完整的菜单
	public void decodeXML(String XMLfile) {
		try {
			peopleList = null;
			people_total = 0;
			String resultstr = null;// 用于存放XML文件内容
			// 读取本地XML文件
			resultstr = fileUtils.readFile(XMLfile).trim(); // 创建FileReader对象，用来读取字符流

			// 创建一个SAXFactory
			SAXParserFactory factory = SAXParserFactory.newInstance();
			XMLReader reader = factory.newSAXParser().getXMLReader();
			XML myContentHandler = new XML();
			reader.setContentHandler(myContentHandler);// 为XMLReader设置内容处理器
			reader.parse(new InputSource(new StringReader(resultstr)));// 开始解析
			
			//pwhid =  myContentHandler.pwhid; 
		yichen = myContentHandler.yichen;
			peopleList = myContentHandler.peopleList; 
			//kaoheList = myContentHandler.kaoheList; 
			/*成果论文获奖不要了
			chengguoList = myContentHandler.chengguoList; 
			lunwenList = myContentHandler.lunwenList; 
			huojiangList = myContentHandler.huojiangList; 
			*/
			
			scoreList = myContentHandler.scoreList; 
			
			people_total = peopleList.size();
			people_cur = 0;
		} catch (Exception e) {
			e.printStackTrace();
			peopleList = null;
			//kaoheList = null; 
			/*成果论文获奖不要了
			chengguoList = null; 
			lunwenList = null; 
			huojiangList = null; 
			*/
			
			scoreList = null; 
		}
	}
	
	
	public ArrayList<HashMap<String, Object>> scoreList = null;
	// 解析XML得到破格和平均分
		public void decodeXML_score(String XMLfile) {
			try {
				scoreList = null;
				String resultstr = null;// 用于存放XML文件内容
				// 读取本地XML文件
				resultstr = fileUtils.readFile(XMLfile).trim(); // 创建FileReader对象，用来读取字符流

				// 创建一个SAXFactory
				SAXParserFactory factory = SAXParserFactory.newInstance();
				XMLReader reader = factory.newSAXParser().getXMLReader();
				XML myContentHandler = new XML();
				reader.setContentHandler(myContentHandler);// 为XMLReader设置内容处理器
				reader.parse(new InputSource(new StringReader(resultstr)));// 开始解析
				
				//pwhid =  myContentHandler.pwhid; 
				scoreList = myContentHandler.scoreList; 

			} catch (Exception e) {
				e.printStackTrace();
				scoreList = null;
			}
		}
		
		public ArrayList<HashMap<String, Object>> failedList = null;
		// 解析XML得到破格和平均分
			public void decodeXML_failed(String XMLfile) {
				try {
					scoreList = null;
					String resultstr = null;// 用于存放XML文件内容
					// 读取本地XML文件
					resultstr = fileUtils.readFile(XMLfile).trim(); // 创建FileReader对象，用来读取字符流

					// 创建一个SAXFactory
					SAXParserFactory factory = SAXParserFactory.newInstance();
					XMLReader reader = factory.newSAXParser().getXMLReader();
					XML myContentHandler = new XML();
					reader.setContentHandler(myContentHandler);// 为XMLReader设置内容处理器
					reader.parse(new InputSource(new StringReader(resultstr)));// 开始解析
					
					//pwhid =  myContentHandler.pwhid; 
					failedList = myContentHandler.failedList; 

				} catch (Exception e) {
					e.printStackTrace();
					failedList = null;
				}
			}
	
	//数据库-----------------------------------
	public Database getDB()
	{
		System.out.println("全局getDB");
		database = new Database(this);
		database.open();
		return database;
	}
	
	public Cursor queryTable(String id){
		Cursor cursor = database.queryTable(id);
		return cursor;
	}
	
	public Cursor queryTable_tijiaostate(String tijiaostate){
		Cursor cursor = database.queryTable_tijiaostate(tijiaostate);//tijiaostate都为2，则全部投票已经提交
		return cursor;
	}
	
	
	public Cursor getAll(){

		Cursor cursor = database.getAll();
		return cursor;
	}
	
	public void add(String id, String pinfen, String pogeyijian, String poge, String toupiao, String tijiaostate,
			String f1,String f2,String f3){
		database.add(id, pinfen, pogeyijian, poge, toupiao, tijiaostate,f1 ,f2, f3);
	}
	
	public void add(String id, String xiaozufen, String xiaozuyijian){
		database.add( id,  xiaozufen,  xiaozuyijian);
	}
	
	public Cursor getToupiao(){
		Cursor cursor = database.getToupiao();
		return cursor;
	}
	
	public void Update_people(String id, String pinfen, String pogeyijian, String poge, String toupiao, String tijiaostate,
			String f1,String f2,String f3){
		database.Update_people(id, pinfen, pogeyijian, poge, toupiao, tijiaostate,f1 ,f2, f3);
	}
	
	public void Update_people(String id,  String xiaozufen, String xiaozuyijian){
		database.Update_people(id,  xiaozufen, xiaozuyijian);
	}
	
	public void Update_tijiao(String id,  String tijiaostate){
		database.Update_tijiao(id, tijiaostate);
	}
	
	public void Update_toupiao(String id, String toupiao, String tijiaostate){
		database.Update_toupiao(id, toupiao, tijiaostate);
	}
	
	public void clearTable(String tablename) {
		// TODO Auto-generated method stub
		database.clearThis(tablename);
	}
	
	public void del(int i){
		//database.delFoodlist(i);
	}
	
	
	public Cursor getpwh(){
		Cursor cursor = database.getpwh();
		return cursor;
	}
	
	public void addpinweihui(String id) {
		database.addpinweihui(id);
	}
	
	public void Update_pwh(String id, String newid) {
		database.Update_pwh(id, newid);
	}
	
	public void dbClose(){
		database.close();
	}
	
	//寻找退出前操作的人员
	/*
	 * @param String tijiaostate  
	 * 提交状态（未提交/提交评分/提交投票）0 1 2
	 */
	public void searchLastPeople(String tijiaostate){//提交状态（未提交/提交评分/提交投票）0 1 2
		int j = 0;
		getDB();
		people_cur = 0;
		Cursor cursor = null;
		for (j = 0 ;j<people_total -1;j++){
			cursor = database.queryTable_tijiaostate(peopleList.get(j).get("id").toString(),tijiaostate);
			if (cursor != null && cursor.getCount() >0){//评分了就会有记录，1表示已经提交,往后找
				cursor.close();
			}else{
				people_cur = j;
				cursor.close();
				break;
			}
			
		}
		
		
		
		/*
		Cursor cursor = getAll();		
		
		tmp = cursor.getCount();
		
		
		while (cursor!=null && cursor.getCount()>0 && !cursor.isLast() ){
			cursor.moveToNext();
			if (cursor.getString(5).equals(tijiaostate)){//tijiaostate
				//已评分或者已投票
				i++;
			}else{
				//未评分或者未投票
				people_cur = i;
				break;
			}			
		}	
		*/
		//这一段很重要
		if (j < people_total){
			people_cur = j;
		}else{
			people_cur = people_total-1;
		}
		
		//cursor.close();
		dbClose();
	}
	
	
	//未通过的    主任寄语
	public void Add_pinyu(String id, String pinyu, String zhuangtai){
		database.Add_pinyu(id, pinyu, zhuangtai);
	}
	
	public Cursor queryFailed(String id){
		Cursor cursor = database.queryFailed(id);
		return cursor;
	}
	
	public Cursor queryFailed_zhuangtai(String zhuangtai){
		Cursor cursor = database.queryFailed_zhuangtai(zhuangtai);//zhuangtai都为1，则全部投票已经提交
		return cursor;
	}
	
	public void Update_pinyu(String id,  String pinyu){
		database.Update_pinyu(id, pinyu);
	}
	
	public void Update_failedState(String id,  String zhuangtai){
		database.Update_failedState(id, zhuangtai);
	}
	
	public Cursor getFailedAll(){

		Cursor cursor = database.getFailedAll();
		return cursor;
	}
	
	
	
	public void launch_help(){
		String localPath = "sites/default/files/help/";
  		String fileName = "caozuoshuomin.pdf";


  		try {
  			InputStream in = getResources().getAssets().open(fileName);
  			// 写到本地
  			FileUtils file = new FileUtils();
  			file.writeFromInput(SDpath + "psxt/" + localPath,
  					fileName, in);

  		} catch (Exception e) {
  			e.printStackTrace();
  		}

  		SendIntent SDintent = new SendIntent();
  		Intent it = SDintent.getIntent(SDpath + "psxt/" + localPath
  				+ fileName);
  		try {
  			startActivity(it);
  		} catch (Exception e) {
  			e.printStackTrace();
  			Log.e("error", e.toString());
  			Toast toast = Toast.makeText(getApplicationContext(),
  					"打开失败！", Toast.LENGTH_LONG);
  			toast.setGravity(Gravity.CENTER, 0, 0);
  			toast.show();
  		}
	}
}
