/**
 * ȫ�ֱ���
 * 
 * @author ����
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
	
	public boolean xianchangfenzu = false;    //���ֳ����飬���ǽ���ί��ʱ����
	public boolean lianghua = true;	//�Ƿ�����
	public boolean pinshenjieshu = false;		//���������־
	
	public String pinweiName;
	public String weiyuanjibie = "";//����ίԱ|ίԱ|�鳤
	public boolean jiyu = false;//������ί�Ƿ���д�˼���
	
	public String pwhid ;//��ί��id    �����������Ƿ���id
	public String yichen; //�������

	public String firm;
	//public Integer version = Integer.valueOf(android.os.Build.VERSION.SDK);
	public String version = android.os.Build.VERSION.RELEASE;
	public float screenWidth, screenHeight;
	public float density; // ��Ļ�ܶȣ�0.75 / 1.0 /1.25/ 1.5��
	public int densityDpi; // ��Ļ�ܶ�DPI��120 / 160 /200/ 240��
	public float wh; // �����
	public TelephonyManager tm;
	public String IMEI;

	
	//
	public String SDpath;
	public String excelfile;

	public boolean closeMain = false;//�Ƿ�ر������壿
	public Boolean isUpdated = true;
	public boolean firstIn = true;//��������һ�ν���ʱ���жϣ������¼���߷��ص�ʱ���ж�
	public String voteState = "δͶƱ";//ͶƱ���0 1 2 ͬ�� �޳� ����
	public String pogebutton = "not_use"; //yes  no  not_useͬ�� ��ͬ�� δʹ��
	public String workfloat = "denglu"; //denglu��¼  �鿴����kan1   �鿴����kan2 ����pinfen������ͶƱtoupiao���������� pinyu, �������wanchen
	public String tab5_state= "pinfen"; //���֡�ͶƱtabר��
	
	public String gerenyijian = "yes";	//���������֣��������ͬ�⣬��ͬ��
	
	

	public TabWidget mTabWidget;
	public Intent layout1intent, layout2intent, layout3intent, layout4intent,
			layout5intent;
	public Intent layout1intent2, layout2intent2, layout3intent2,
			layout4intent2, layout5intent2;

	public TabHost tabhost;
	
	public int people_pinfentotal;//���ֽ׶ε��������ܻ�����ͶƱ�׶ε�
	public int people_total;
	public int people_cur;
	/*
	public String []chenguo;   //�ɹ�����·��
	public String []lunwen; //���ĸ���·��
	public String []biaozhang; //���ø���·��
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
		if ("��ͨ".equals(jiyu)) {
			layout1intent = new Intent();
			layout1intent.setClass(this, Tab_Layout1.class);
			TabHost.TabSpec layout1spec = tabhost.newTabSpec("tab1");// ����һ���µı�ǩtab1
			layout1spec.setIndicator("������Ϣ")// ,
					// getResources().getDrawable(R.drawable.gimp))//
					// ����һ����ʾ�ı���Ϊ("������Ϣ"������һ�±�ǩͼ��Ϊgimp
					.setContent(layout1intent); // ����һ�¸ñ�ǩҳ�Ĳ�������Ϊlayout1intent
			tabhost.addTab(layout1spec);

			/*ֻҪ������Ϣ�����֡�ͶƱ
			layout2intent = new Intent();
			layout2intent.setClass(this, Tab_Layout2.class);
			TabHost.TabSpec layout2spec = tabhost.newTabSpec("tab2");// ����һ���µı�ǩtab2
			layout2spec.setIndicator("������������")// ,
					// getResources().getDrawable(R.drawable.mumule))//
					// ����һ����ʾ�ı���Ϊ("����������"������һ�±�ǩͼ��Ϊgimp
					.setContent(layout2intent); // ����һ�¸ñ�ǩҳ�Ĳ�������Ϊlayout2intent
			tabhost.addTab(layout2spec);

			layout3intent = new Intent();
			layout3intent.setClass(this, Tab_Layout3.class);
			TabHost.TabSpec layout3spec = tabhost.newTabSpec("tab3");// ����һ���µı�ǩtab3
			layout3spec.setIndicator("��Ҫҵ����ɹ�")// ,
					// getResources().getDrawable(R.drawable.gimp))//
					// ����һ����ʾ�ı���Ϊ("ҵ����ɹ�"������һ�±�ǩͼ��Ϊgimp
					.setContent(layout3intent); // ����һ�¸ñ�ǩҳ�Ĳ�������Ϊlayout3intent
			tabhost.addTab(layout3spec);

			layout4intent = new Intent();
			layout4intent.setClass(this, Tab_Layout4.class);
			TabHost.TabSpec layout4spec = tabhost.newTabSpec("tab4");// ����һ���µı�ǩtab4
			layout4spec.setIndicator("���ķ��������")// ,
					// getResources().getDrawable(R.drawable.mumule))//
					// ����һ����ʾ�ı���Ϊ("���ķ�����"������һ�±�ǩͼ��Ϊgimp
					.setContent(layout4intent); // ����һ�¸ñ�ǩҳ�Ĳ�������Ϊlayout4intent
			tabhost.addTab(layout4spec);
			*/

			layout5intent = new Intent();
			layout5intent.setClass(this, Tab_Layout5.class);
			TabHost.TabSpec layout5spec = tabhost.newTabSpec("tab5");// ����һ���µı�ǩtab5
			if ("pinfen".equals(tab5_state)) {
				// ���ֽ׶�
				layout5spec.setIndicator("���֡��Ƹ����")// ,
						.setContent(layout5intent); // ����һ�¸ñ�ǩҳ�Ĳ�������Ϊlayout4intent
			} else if ("toupiao".equals(tab5_state)) {
				layout5spec.setIndicator("ͶƱ")// ,
						// getResources().getDrawable(R.drawable.mumule))//
						// ����һ����ʾ�ı���Ϊ("���ķ�����"������һ�±�ǩͼ��Ϊgimp
						.setContent(layout5intent); // ����һ�¸ñ�ǩҳ�Ĳ�������Ϊlayout4intent
			}else if ("xiaozuyijian".equals(tab5_state)) {
				layout5spec.setIndicator("С�����")// ,
				// getResources().getDrawable(R.drawable.mumule))//
				// ����һ����ʾ�ı���Ϊ("���ķ�����"������һ�±�ǩͼ��Ϊgimp
				.setContent(layout5intent); // ����һ�¸ñ�ǩҳ�Ĳ�������Ϊlayout4intent
	}
			tabhost.addTab(layout5spec);

			mTabWidget = tabhost.getTabWidget();
			/* ��Tab��ǩ�Ķ��� */
			for (int i = 0; i < mTabWidget.getChildCount(); i++) {
				/*
				 * /��* �õ�ÿ����ǩ����ͼ View view = mTabWidget.getChildAt(i); /��* ����ÿ����ǩ�ı���
				 * if (tabhost.getCurrentTab() == i) {
				 * view.setBackgroundDrawable(getResources().getDrawable(
				 * R.drawable.mumule));
				 * 
				 * } else { view.setBackgroundDrawable(getResources().getDrawable(
				 * R.drawable.gimp)); }
				 */
				TextView tv = (TextView) mTabWidget.getChildAt(i).findViewById(
						android.R.id.title);
				/* ����tab������Ĵ�С */
				tv.setTextSize(20);
			}
		}else if ("���μ���".equals(jiyu)){
			layout1intent = new Intent();
			layout1intent.setClass(this, Tab_Layout_jiyu.class);
			TabHost.TabSpec layout_jiyu_spec = tabhost.newTabSpec("tab_jiyu");// ����һ���µı�ǩtab1
			layout_jiyu_spec.setIndicator("���μ���")// ,
					// getResources().getDrawable(R.drawable.gimp))//
					// ����һ����ʾ�ı���Ϊ("������Ϣ"������һ�±�ǩͼ��Ϊgimp
					.setContent(layout1intent); // ����һ�¸ñ�ǩҳ�Ĳ�������Ϊlayout1intent
			tabhost.addTab(layout_jiyu_spec);
			
			layout2intent = new Intent();
			layout2intent.setClass(this, Tab_Layout1.class);
			TabHost.TabSpec layout2spec = tabhost.newTabSpec("tab2");// ����һ���µı�ǩtab1
			layout2spec.setIndicator("������Ϣ")// ,
					// getResources().getDrawable(R.drawable.gimp))//
					// ����һ����ʾ�ı���Ϊ("������Ϣ"������һ�±�ǩͼ��Ϊgimp
					.setContent(layout2intent); // ����һ�¸ñ�ǩҳ�Ĳ�������Ϊlayout1intent
			tabhost.addTab(layout2spec);
		}
		
		updateTabBackground();
	}
	
	/** 
     * ����Tab��ǩ�ı���ͼ 
     * 
     * @param tabHost 
     */ 
	//private void updateTabBackground(final TabHost tabHost) { 
    public void updateTabBackground() { 
        for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++) { 
            View vvv = tabhost.getTabWidget().getChildAt(i); 
            if (tabhost.getCurrentTab() == i) { 
                // ѡ�к�ı��� 
                //vvv.setBackgroundDrawable(getResources().getDrawable( android.R.drawable.spinner_background)); 
                vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_host)); 
            } else { 
                // ��ѡ��ı��� 
                //vvv.setBackgroundDrawable(null); 
            	vvv.setBackgroundDrawable(getResources().getDrawable( R.drawable.tab_host_back)); 
            } 
        } 
    } 
	
	
	public String selectZhence(){
		String fileName = "";
		if (peopleList != null){
			Log.i("info", peopleList.get(people_cur).get("shenbaopinweihui").toString());
			
			if ("ͼ������".equals(peopleList.get(people_cur).get("shenbaopinweihui").toString())) {
				fileName = "tushu.doc";
			} else if ("����".equals(peopleList.get(people_cur).get("shenbaopinweihui").toString())) {
				fileName = "dangan.doc";
			} else if ("����".equals(peopleList.get(people_cur).get("shenbaopinweihui").toString())) {
				fileName = "boyin.doc";
			} else if ("����ѧ".equals(peopleList.get(people_cur).get("shenbaopinweihui").toString())) {
				fileName = "shehui.doc";
			} else if ("����".equals(peopleList.get(people_cur).get("shenbaopinweihui").toString())) {
				fileName = "jinji.doc";
			} else {
				fileName = "nothing.doc";
			}
		}
		return fileName;
	}
	
	//---------------------------------------------------------------------------------
	public Net net = new Net();
	//�ҵķ�����
	public String HttpHead = "";
	public String HttpHead_bak = "http://192.168.2.111";
	public String HttpHead_bak1 = "http://192.168.2.111";
	public String HttpHead_bak2 = "http://solomanhl.3322.org:81";
		//private String HttpHead = "http://soloman.vicp.cc:8880";
		//private String HttpHead_bak = "http://solomanhl.3322.org:8880";
		//private String HttpHead_bak2 = "http://192.168.1.25:8880";
		
		//�ͻ��ķ�����
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
				httpResponse = httpClient.execute(httpPost); //android4.xǿ��ֹͣ,
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
		
		public static final int REQUEST_TIMEOUT = 5*1000;//��������ʱ5����  
		public static final int SO_TIMEOUT = 30*1000;  //���õȴ����ݳ�ʱʱ��30����  
		   /** 
		    * �������ʱʱ��͵ȴ�ʱ�� 
		    * @author spring sky  
		    * Email vipa1888@163.com 
		    * QQ: 840950105 
		    * My name: ʯ���� 
		    * @return HttpClient���� 
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
			//�������
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
			
			//��������
			if (isServerOpen()){
				Log.i("info",HttpHead + "is open");
			}else{
				return false;
			}
			
			return true;
		}
		/*
		 * @param String remotepath:������·������ʽΪ/.../xx.yyy
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
		//Ӣ�������ֶ�û����
		//net.downFile(HttpHead + "/ct/food.xml" , 	"/ordering/", 	"food.xml");
		return i;
	}
	
	
	
	public FileUtils fileUtils = new FileUtils();
	// ������Ա
	public ArrayList<HashMap<String, Object>> peopleList = null;
	
	//�ɹ����Ļ񽱱��ö���Ҫ��
	public ArrayList<HashMap<String, Object>> chengguoList = null;
	public ArrayList<HashMap<String, Object>> lunwenList = null;
	public ArrayList<HashMap<String, Object>> huojiangList = null;
	
	
	

	// ����XML�õ������Ĳ˵�
	public void decodeXML(String XMLfile) {
		try {
			peopleList = null;
			people_total = 0;
			String resultstr = null;// ���ڴ��XML�ļ�����
			// ��ȡ����XML�ļ�
			resultstr = fileUtils.readFile(XMLfile).trim(); // ����FileReader����������ȡ�ַ���

			// ����һ��SAXFactory
			SAXParserFactory factory = SAXParserFactory.newInstance();
			XMLReader reader = factory.newSAXParser().getXMLReader();
			XML myContentHandler = new XML();
			reader.setContentHandler(myContentHandler);// ΪXMLReader�������ݴ�����
			reader.parse(new InputSource(new StringReader(resultstr)));// ��ʼ����
			
			//pwhid =  myContentHandler.pwhid; 
		yichen = myContentHandler.yichen;
			peopleList = myContentHandler.peopleList; 
			//kaoheList = myContentHandler.kaoheList; 
			/*�ɹ����Ļ񽱲�Ҫ��
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
			/*�ɹ����Ļ񽱲�Ҫ��
			chengguoList = null; 
			lunwenList = null; 
			huojiangList = null; 
			*/
			
			scoreList = null; 
		}
	}
	
	
	public ArrayList<HashMap<String, Object>> scoreList = null;
	// ����XML�õ��Ƹ��ƽ����
		public void decodeXML_score(String XMLfile) {
			try {
				scoreList = null;
				String resultstr = null;// ���ڴ��XML�ļ�����
				// ��ȡ����XML�ļ�
				resultstr = fileUtils.readFile(XMLfile).trim(); // ����FileReader����������ȡ�ַ���

				// ����һ��SAXFactory
				SAXParserFactory factory = SAXParserFactory.newInstance();
				XMLReader reader = factory.newSAXParser().getXMLReader();
				XML myContentHandler = new XML();
				reader.setContentHandler(myContentHandler);// ΪXMLReader�������ݴ�����
				reader.parse(new InputSource(new StringReader(resultstr)));// ��ʼ����
				
				//pwhid =  myContentHandler.pwhid; 
				scoreList = myContentHandler.scoreList; 

			} catch (Exception e) {
				e.printStackTrace();
				scoreList = null;
			}
		}
		
		public ArrayList<HashMap<String, Object>> failedList = null;
		// ����XML�õ��Ƹ��ƽ����
			public void decodeXML_failed(String XMLfile) {
				try {
					scoreList = null;
					String resultstr = null;// ���ڴ��XML�ļ�����
					// ��ȡ����XML�ļ�
					resultstr = fileUtils.readFile(XMLfile).trim(); // ����FileReader����������ȡ�ַ���

					// ����һ��SAXFactory
					SAXParserFactory factory = SAXParserFactory.newInstance();
					XMLReader reader = factory.newSAXParser().getXMLReader();
					XML myContentHandler = new XML();
					reader.setContentHandler(myContentHandler);// ΪXMLReader�������ݴ�����
					reader.parse(new InputSource(new StringReader(resultstr)));// ��ʼ����
					
					//pwhid =  myContentHandler.pwhid; 
					failedList = myContentHandler.failedList; 

				} catch (Exception e) {
					e.printStackTrace();
					failedList = null;
				}
			}
	
	//���ݿ�-----------------------------------
	public Database getDB()
	{
		System.out.println("ȫ��getDB");
		database = new Database(this);
		database.open();
		return database;
	}
	
	public Cursor queryTable(String id){
		Cursor cursor = database.queryTable(id);
		return cursor;
	}
	
	public Cursor queryTable_tijiaostate(String tijiaostate){
		Cursor cursor = database.queryTable_tijiaostate(tijiaostate);//tijiaostate��Ϊ2����ȫ��ͶƱ�Ѿ��ύ
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
	
	//Ѱ���˳�ǰ��������Ա
	/*
	 * @param String tijiaostate  
	 * �ύ״̬��δ�ύ/�ύ����/�ύͶƱ��0 1 2
	 */
	public void searchLastPeople(String tijiaostate){//�ύ״̬��δ�ύ/�ύ����/�ύͶƱ��0 1 2
		int j = 0;
		getDB();
		people_cur = 0;
		Cursor cursor = null;
		for (j = 0 ;j<people_total -1;j++){
			cursor = database.queryTable_tijiaostate(peopleList.get(j).get("id").toString(),tijiaostate);
			if (cursor != null && cursor.getCount() >0){//�����˾ͻ��м�¼��1��ʾ�Ѿ��ύ,������
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
				//�����ֻ�����ͶƱ
				i++;
			}else{
				//δ���ֻ���δͶƱ
				people_cur = i;
				break;
			}			
		}	
		*/
		//��һ�κ���Ҫ
		if (j < people_total){
			people_cur = j;
		}else{
			people_cur = people_total-1;
		}
		
		//cursor.close();
		dbClose();
	}
	
	
	//δͨ����    ���μ���
	public void Add_pinyu(String id, String pinyu, String zhuangtai){
		database.Add_pinyu(id, pinyu, zhuangtai);
	}
	
	public Cursor queryFailed(String id){
		Cursor cursor = database.queryFailed(id);
		return cursor;
	}
	
	public Cursor queryFailed_zhuangtai(String zhuangtai){
		Cursor cursor = database.queryFailed_zhuangtai(zhuangtai);//zhuangtai��Ϊ1����ȫ��ͶƱ�Ѿ��ύ
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
  			// д������
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
  					"��ʧ�ܣ�", Toast.LENGTH_LONG);
  			toast.setGravity(Gravity.CENTER, 0, 0);
  			toast.show();
  		}
	}
}
