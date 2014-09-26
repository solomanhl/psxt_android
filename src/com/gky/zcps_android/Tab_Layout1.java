/**
 * 基本信息
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
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Tab_Layout1 extends Activity
{
	private Global_var appState; // 获得全局变量;
	
	private TextView textView_cpid; //编号
	private TextView textView_cpname; //姓名
	private TextView textView_cpsex;//性别
	private TextView textView_cpborn;//生日
	private TextView textView_cpcompany;//工作单位
	private TextView textView_cpeducation;//学历
	//private TextView textView_cppost;//行政职务
	//private TextView textView_cppolitical;//政治面貌
	private TextView textView_cpcurtech;//技术职务
	private TextView textView_cpbeingtime;//受聘时间
	
	private TextView textView_cpwhere;//毕业院校
	private TextView textView_cpprofessional;//所学专业
	private TextView textView_cpwhen;//毕业时间
	private TextView textView_othereducation;//其它学历
	private TextView textView_cpothereducation;//其它学历
	
	private TextView textView_where2;
	private TextView textView_cpwhere2;
	private TextView textView_when2;
	private TextView textView_cpwhen2;
	private TextView textView_professional2;
	private TextView textView_cpprofessional2;
	
	
	private TextView textView_cpcurwork;//从事专业
	
	private TextView textView_cpcurshenfenzhen;//身份证
	private TextView textView_cpcurshenbaoleixin;//申报类型
	private TextView textView_cpcurshenbaojibie;//申报级别
	private TextView textView_cpcurpinweihui;//评委会
	private TextView textView_cpyouxiu;//优秀
	private TextView textView_cpchenzhi;//称职
	private TextView textView_cpjibenchenzhi;//基本称职
	private TextView textView_ceshi;//测试结果
	private TextView textView_cpceshi;//测试结果
	private TextView textView_cpenglish;//外语成绩
	private TextView textView_cpcomputer;//计算机
	private TextView textView_cpcontinuouseducation;//继续教育
	
	private Button button_zhengce1, button_xuelifujian, button_czsm1;
	
	

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	appState = ((Global_var) getApplicationContext()); // 获得全局变量
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout1);
        
        findView();    
    }
    
    private void findView() {
		// TODO Auto-generated method stub
    	textView_cpid = (TextView) findViewById(R.id.textView_cpid); // 编号        
    	textView_cpname = (TextView) findViewById(R.id.textView_cpname); // 姓名    
    	textView_cpsex = (TextView) findViewById(R.id.textView_cpsex); // 性别
    	textView_cpborn = (TextView) findViewById(R.id.textView_cpborn); // 生日    
    	textView_cpcompany = (TextView) findViewById(R.id.textView_cpcompany); // 工作单位    
    	textView_cpeducation = (TextView) findViewById(R.id.textView_cpeducation); // 学历    
    	//textView_cppost = (TextView) findViewById(R.id.textView_cppost); // 行政职务    
    	//textView_cppolitical = (TextView) findViewById(R.id.textView_cppolitical); // 政治面貌    
    	textView_cpcurtech = (TextView) findViewById(R.id.textView_cpcurtech); // 技术职务    
    	textView_cpbeingtime = (TextView) findViewById(R.id.textView_cpbeingtime); // 受聘时间    
    	textView_cpwhere = (TextView) findViewById(R.id.textView_cpwhere); // 毕业院校    
    	textView_cpprofessional = (TextView) findViewById(R.id.textView_cpprofessional); // 专业    
    	textView_cpwhen = (TextView) findViewById(R.id.textView_cpwhen); // 毕业时间   
    	
    	
    	
    	textView_othereducation = (TextView) findViewById(R.id.textView_othereducation); // 其他学历    
    	textView_cpothereducation = (TextView) findViewById(R.id.textView_cpothereducation); // 其他学历    
    	textView_cpwhere = (TextView) findViewById(R.id.textView_cpwhere);
    	textView_where2 = (TextView) findViewById(R.id.textView_where2);
    	textView_cpwhere2 = (TextView) findViewById(R.id.textView_cpwhere2);
    	textView_cpwhen = (TextView) findViewById(R.id.textView_cpwhen);
    	textView_when2 = (TextView) findViewById(R.id.textView_when2);
    	textView_cpwhen2 = (TextView) findViewById(R.id.textView_cpwhen2);
    	textView_cpprofessional = (TextView) findViewById(R.id.textView_cpprofessional);
    	textView_professional2 = (TextView) findViewById(R.id.textView_professional2);
    	textView_cpprofessional2 = (TextView) findViewById(R.id.textView_cpprofessional2);
    	
    	
    	
    	textView_cpcurwork = (TextView) findViewById(R.id.textView_cpcurwork); // 从事专业    
    	
    	textView_cpcurshenfenzhen = (TextView) findViewById(R.id.textView_cpcurshenfenzhen);//身份证
    	textView_cpcurshenbaoleixin = (TextView) findViewById(R.id.textView_cpcurshenbaoleixin);//申报类型
    	textView_cpcurshenbaojibie = (TextView) findViewById(R.id.textView_cpcurshenbaojibie);//申报级别
    	textView_cpcurpinweihui = (TextView) findViewById(R.id.textView_cpcurpinweihui);//评委会
    	textView_cpyouxiu = (TextView) findViewById(R.id.textView_cpyouxiu);//优秀
    	textView_cpchenzhi = (TextView) findViewById(R.id.textView_cpchenzhi);//称职
    	textView_cpjibenchenzhi = (TextView) findViewById(R.id.textView_cpjibenchenzhi);//基本称职
    	textView_ceshi = (TextView) findViewById(R.id.textView_ceshi);//测试结果
    	textView_cpceshi = (TextView) findViewById(R.id.textView_cpceshi);//测试结果
    	textView_cpenglish = (TextView) findViewById(R.id.textView_cpenglish);//外语成绩
    	textView_cpcomputer = (TextView) findViewById(R.id.textView_cpcomputer);//计算机
    	textView_cpcontinuouseducation = (TextView) findViewById(R.id.textView_cpcontinuouseducation);//继续教育
    	
    	button_zhengce1 = (Button) findViewById(R.id.button_zhengce1);
    	button_xuelifujian = (Button) findViewById(R.id.button_xuelifujian);
    	button_czsm1 = (Button) findViewById(R.id.button_czsm1);
    	
	}

	@Override 
    public void onResume(){
    	super.onResume();
    	Log.i("info","Tab_Layout1_onResume");
    	if (appState.isUpdated){
    		if (appState.people_cur < 0){
    			appState.people_cur = 0;
    		}
    		update();
    		//appState.isUpdated = false;
    		Log.i("info","upDated");    	}
    	
    }
    
    public void update(){
    	textView_cpid.setText(appState.peopleList.get(appState.people_cur).get("id").toString());//id
    	textView_cpname.setText(appState.peopleList.get(appState.people_cur).get("name").toString());// 姓名    
    	textView_cpsex.setText(appState.peopleList.get(appState.people_cur).get("sex").toString()); // 性别
    	textView_cpborn.setText(appState.peopleList.get(appState.people_cur).get("birth").toString()); // 生日    
    	textView_cpcompany.setText(appState.peopleList.get(appState.people_cur).get("company").toString()); // 工作单位    
    	textView_cpeducation.setText(appState.peopleList.get(appState.people_cur).get("xueli").toString());// 学历    
    	//textView_cppost.setText(appState.peopleList.get(appState.people_cur).get("xinzhenzhiwu").toString()); // 行政职务    
    	//textView_cppolitical.setText(appState.peopleList.get(appState.people_cur).get("zhenzhi").toString()); // 政治面貌    
    	textView_cpcurtech.setText(appState.peopleList.get(appState.people_cur).get("jishuzhiwu").toString()); // 技术职务    
    	textView_cpbeingtime.setText(appState.peopleList.get(appState.people_cur).get("shoupinshijian").toString() + "年"); // 受聘时间    
    	textView_cpwhere.setText(appState.peopleList.get(appState.people_cur).get("xuexiao").toString());// 毕业院校    
    	textView_cpprofessional.setText(appState.peopleList.get(appState.people_cur).get("suoxuezhuanye").toString()); // 专业    
    	textView_cpwhen.setText(appState.peopleList.get(appState.people_cur).get("biyeshijian").toString()); // 毕业时间   
    	
    	if ("pinfen".equals(appState.workfloat)){
    		//button_xuelifujian.setVisibility(View.VISIBLE);
    		button_xuelifujian.setVisibility(View.INVISIBLE);
    	}else if ("toupiao".equals(appState.workfloat)){
    		button_xuelifujian.setVisibility(View.INVISIBLE);
    	}
    	
    	String tmp = appState.peopleList.get(appState.people_cur).get("xueli2").toString();
    	if ("".equals(tmp)){
    		textView_where2.setVisibility(View.INVISIBLE);
    		textView_professional2.setVisibility(View.INVISIBLE);
    		textView_when2.setVisibility(View.INVISIBLE);
    		textView_othereducation.setVisibility(View.INVISIBLE);
    		
    		textView_cpwhere2.setVisibility(View.INVISIBLE);
    		textView_cpprofessional2.setVisibility(View.INVISIBLE);
    		textView_cpwhen2.setVisibility(View.INVISIBLE);
    		textView_cpothereducation.setVisibility(View.INVISIBLE);
    	}else{
    		textView_where2.setVisibility(View.VISIBLE);
    		textView_professional2.setVisibility(View.VISIBLE);
    		textView_when2.setVisibility(View.VISIBLE);
    		textView_othereducation.setVisibility(View.VISIBLE);
    		
    		textView_cpwhere2.setVisibility(View.VISIBLE);
    		textView_cpprofessional2.setVisibility(View.VISIBLE);
    		textView_cpwhen2.setVisibility(View.VISIBLE);
    		textView_cpothereducation.setVisibility(View.VISIBLE);
    		
    		textView_cpwhere2.setText(appState.peopleList.get(appState.people_cur).get("xuexiao2").toString());// 毕业院校    
        	textView_cpprofessional2.setText(appState.peopleList.get(appState.people_cur).get("suoxuezhuanye2").toString()); // 专业    
        	textView_cpwhen2.setText(appState.peopleList.get(appState.people_cur).get("biyeshijian2").toString()); // 毕业时间         	
        	textView_cpothereducation.setText(tmp); // 其他学历    	
    	}
    	
    	
    	
    	
    	
    	
    	textView_cpcurwork.setText(appState.peopleList.get(appState.people_cur).get("congshizhuanye").toString()); // 从事专业    	
    	
    	textView_cpcurshenfenzhen.setText(appState.peopleList.get(appState.people_cur).get("shenfenzhen").toString());//身份证
    	
    	
    	
    	if ("04".equals(appState.peopleList.get(appState.people_cur).get("shenbaoleixin").toString())) {
    		textView_cpcurshenbaoleixin.setText("破格");
		}else if ("01".equals(appState.peopleList.get(appState.people_cur).get("shenbaoleixin").toString())) {
			textView_cpcurshenbaoleixin.setText("正常");
		}else if ("05".equals(appState.peopleList.get(appState.people_cur).get("shenbaoleixin").toString())) {
			textView_cpcurshenbaoleixin.setText("平转、确认");
		}else if ("08".equals(appState.peopleList.get(appState.people_cur).get("shenbaoleixin").toString())) {
			textView_cpcurshenbaoleixin.setText("特殊人才");
		}else {
			textView_cpcurshenbaoleixin.setText("");
		}
    	//textView_cpcurshenbaoleixin.setText(appState.peopleList.get(appState.people_cur).get("shenbaoleixin").toString());//申报类型
    	
    	
    	
    	textView_cpcurshenbaojibie.setText(appState.peopleList.get(appState.people_cur).get("shenbaojibie").toString());//申报级别
    	textView_cpcurpinweihui.setText(appState.peopleList.get(appState.people_cur).get("shenbaopinweihui").toString());//评委会
    	textView_cpyouxiu.setText(appState.peopleList.get(appState.people_cur).get("youxiu").toString() + "次");//优秀
    	textView_cpchenzhi.setText(appState.peopleList.get(appState.people_cur).get("chengzhi").toString()  + "次");//称职
    	textView_cpjibenchenzhi.setText(appState.peopleList.get(appState.people_cur).get("jibenchengzhi").toString()  + "次");//基本称职
    	
    	
    	if ("".equals(appState.peopleList.get(appState.people_cur).get("ceshijieguo").toString())){
    		textView_ceshi.setVisibility(View.INVISIBLE);
    		textView_cpceshi.setVisibility(View.INVISIBLE);
    	}else if ("F".equals(appState.peopleList.get(appState.people_cur).get("ceshijieguo").toString())){
    		textView_ceshi.setVisibility(View.VISIBLE);
    		textView_cpceshi.setVisibility(View.VISIBLE);
    		textView_cpceshi.setText("不合格" );//测试结果
    	}else{
    		textView_ceshi.setVisibility(View.VISIBLE);
    		textView_cpceshi.setVisibility(View.VISIBLE);
    		textView_cpceshi.setText(appState.peopleList.get(appState.people_cur).get("ceshijieguo").toString() +
    				"年合格");//测试结果
    	}
    	
    	
    	textView_cpenglish.setText(appState.peopleList.get(appState.people_cur).get("waiyu").toString());//外语成绩
    	textView_cpcomputer.setText(appState.peopleList.get(appState.people_cur).get("jisuanji").toString());//计算机
    	
    	if ("Y".equals(appState.peopleList.get(appState.people_cur).get("jixujinxiu").toString())){
    		textView_cpcontinuouseducation.setText("达标");//继续教育
    	}else {
    		textView_cpcontinuouseducation.setText("");//继续教育
    	}
    	
    	
    	
    	
    	//临时用
		appState.voteState = "未投票";
    }
    
 // 查看政策
 	public void button_zhengce1_onclick(View target) throws Exception {
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
 	
 	public void button_xuelifujian_onclick(View target)  {
 		//String localPath = appState.SDpath + "psxt/" + appState.peopleList.get(appState.people_cur).get("xuelifujian").toString();
 		
 		//暂时不需要了
// 		String localPath = appState.SDpath + "psxt/" + appState.peopleList.get(appState.people_cur).get("yilanbiao").toString();
//		SendIntent SDintent = new SendIntent();
//		Intent it = SDintent.getIntent(localPath);
//		try{
//			startActivity(it);
//		}catch( Exception e){
//			e.printStackTrace();
//			Log.e("error", e.toString());
//			Toast toast = Toast.makeText(getApplicationContext(), "没有安装阅读器/播放器或文件不存在！", Toast.LENGTH_LONG);
//			toast.setGravity(Gravity.CENTER, 0, 0);
//			toast.show();
//		}
 	}
 	
 // 操作說明按钮点击事件
 	public void button_czsm1_onclick(View target) {
 		appState.launch_help();
 	}
 	
 	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下键盘上返回按钮
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			System.out.println("返回按钮");
			if (appState.workfloat.equals("pinfen") && appState.xianchangfenzu) {
				Intent it = new Intent(this, searchWithListActivity.class);
    			startActivity(it);
    			finish();
			}
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}
