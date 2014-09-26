/**
 * ������Ϣ
 * 
 * @author ����
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
	private Global_var appState; // ���ȫ�ֱ���;
	
	private TextView textView_cpid; //���
	private TextView textView_cpname; //����
	private TextView textView_cpsex;//�Ա�
	private TextView textView_cpborn;//����
	private TextView textView_cpcompany;//������λ
	private TextView textView_cpeducation;//ѧ��
	//private TextView textView_cppost;//����ְ��
	//private TextView textView_cppolitical;//������ò
	private TextView textView_cpcurtech;//����ְ��
	private TextView textView_cpbeingtime;//��Ƹʱ��
	
	private TextView textView_cpwhere;//��ҵԺУ
	private TextView textView_cpprofessional;//��ѧרҵ
	private TextView textView_cpwhen;//��ҵʱ��
	private TextView textView_othereducation;//����ѧ��
	private TextView textView_cpothereducation;//����ѧ��
	
	private TextView textView_where2;
	private TextView textView_cpwhere2;
	private TextView textView_when2;
	private TextView textView_cpwhen2;
	private TextView textView_professional2;
	private TextView textView_cpprofessional2;
	
	
	private TextView textView_cpcurwork;//����רҵ
	
	private TextView textView_cpcurshenfenzhen;//���֤
	private TextView textView_cpcurshenbaoleixin;//�걨����
	private TextView textView_cpcurshenbaojibie;//�걨����
	private TextView textView_cpcurpinweihui;//��ί��
	private TextView textView_cpyouxiu;//����
	private TextView textView_cpchenzhi;//��ְ
	private TextView textView_cpjibenchenzhi;//������ְ
	private TextView textView_ceshi;//���Խ��
	private TextView textView_cpceshi;//���Խ��
	private TextView textView_cpenglish;//����ɼ�
	private TextView textView_cpcomputer;//�����
	private TextView textView_cpcontinuouseducation;//��������
	
	private Button button_zhengce1, button_xuelifujian, button_czsm1;
	
	

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	appState = ((Global_var) getApplicationContext()); // ���ȫ�ֱ���
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout1);
        
        findView();    
    }
    
    private void findView() {
		// TODO Auto-generated method stub
    	textView_cpid = (TextView) findViewById(R.id.textView_cpid); // ���        
    	textView_cpname = (TextView) findViewById(R.id.textView_cpname); // ����    
    	textView_cpsex = (TextView) findViewById(R.id.textView_cpsex); // �Ա�
    	textView_cpborn = (TextView) findViewById(R.id.textView_cpborn); // ����    
    	textView_cpcompany = (TextView) findViewById(R.id.textView_cpcompany); // ������λ    
    	textView_cpeducation = (TextView) findViewById(R.id.textView_cpeducation); // ѧ��    
    	//textView_cppost = (TextView) findViewById(R.id.textView_cppost); // ����ְ��    
    	//textView_cppolitical = (TextView) findViewById(R.id.textView_cppolitical); // ������ò    
    	textView_cpcurtech = (TextView) findViewById(R.id.textView_cpcurtech); // ����ְ��    
    	textView_cpbeingtime = (TextView) findViewById(R.id.textView_cpbeingtime); // ��Ƹʱ��    
    	textView_cpwhere = (TextView) findViewById(R.id.textView_cpwhere); // ��ҵԺУ    
    	textView_cpprofessional = (TextView) findViewById(R.id.textView_cpprofessional); // רҵ    
    	textView_cpwhen = (TextView) findViewById(R.id.textView_cpwhen); // ��ҵʱ��   
    	
    	
    	
    	textView_othereducation = (TextView) findViewById(R.id.textView_othereducation); // ����ѧ��    
    	textView_cpothereducation = (TextView) findViewById(R.id.textView_cpothereducation); // ����ѧ��    
    	textView_cpwhere = (TextView) findViewById(R.id.textView_cpwhere);
    	textView_where2 = (TextView) findViewById(R.id.textView_where2);
    	textView_cpwhere2 = (TextView) findViewById(R.id.textView_cpwhere2);
    	textView_cpwhen = (TextView) findViewById(R.id.textView_cpwhen);
    	textView_when2 = (TextView) findViewById(R.id.textView_when2);
    	textView_cpwhen2 = (TextView) findViewById(R.id.textView_cpwhen2);
    	textView_cpprofessional = (TextView) findViewById(R.id.textView_cpprofessional);
    	textView_professional2 = (TextView) findViewById(R.id.textView_professional2);
    	textView_cpprofessional2 = (TextView) findViewById(R.id.textView_cpprofessional2);
    	
    	
    	
    	textView_cpcurwork = (TextView) findViewById(R.id.textView_cpcurwork); // ����רҵ    
    	
    	textView_cpcurshenfenzhen = (TextView) findViewById(R.id.textView_cpcurshenfenzhen);//���֤
    	textView_cpcurshenbaoleixin = (TextView) findViewById(R.id.textView_cpcurshenbaoleixin);//�걨����
    	textView_cpcurshenbaojibie = (TextView) findViewById(R.id.textView_cpcurshenbaojibie);//�걨����
    	textView_cpcurpinweihui = (TextView) findViewById(R.id.textView_cpcurpinweihui);//��ί��
    	textView_cpyouxiu = (TextView) findViewById(R.id.textView_cpyouxiu);//����
    	textView_cpchenzhi = (TextView) findViewById(R.id.textView_cpchenzhi);//��ְ
    	textView_cpjibenchenzhi = (TextView) findViewById(R.id.textView_cpjibenchenzhi);//������ְ
    	textView_ceshi = (TextView) findViewById(R.id.textView_ceshi);//���Խ��
    	textView_cpceshi = (TextView) findViewById(R.id.textView_cpceshi);//���Խ��
    	textView_cpenglish = (TextView) findViewById(R.id.textView_cpenglish);//����ɼ�
    	textView_cpcomputer = (TextView) findViewById(R.id.textView_cpcomputer);//�����
    	textView_cpcontinuouseducation = (TextView) findViewById(R.id.textView_cpcontinuouseducation);//��������
    	
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
    	textView_cpname.setText(appState.peopleList.get(appState.people_cur).get("name").toString());// ����    
    	textView_cpsex.setText(appState.peopleList.get(appState.people_cur).get("sex").toString()); // �Ա�
    	textView_cpborn.setText(appState.peopleList.get(appState.people_cur).get("birth").toString()); // ����    
    	textView_cpcompany.setText(appState.peopleList.get(appState.people_cur).get("company").toString()); // ������λ    
    	textView_cpeducation.setText(appState.peopleList.get(appState.people_cur).get("xueli").toString());// ѧ��    
    	//textView_cppost.setText(appState.peopleList.get(appState.people_cur).get("xinzhenzhiwu").toString()); // ����ְ��    
    	//textView_cppolitical.setText(appState.peopleList.get(appState.people_cur).get("zhenzhi").toString()); // ������ò    
    	textView_cpcurtech.setText(appState.peopleList.get(appState.people_cur).get("jishuzhiwu").toString()); // ����ְ��    
    	textView_cpbeingtime.setText(appState.peopleList.get(appState.people_cur).get("shoupinshijian").toString() + "��"); // ��Ƹʱ��    
    	textView_cpwhere.setText(appState.peopleList.get(appState.people_cur).get("xuexiao").toString());// ��ҵԺУ    
    	textView_cpprofessional.setText(appState.peopleList.get(appState.people_cur).get("suoxuezhuanye").toString()); // רҵ    
    	textView_cpwhen.setText(appState.peopleList.get(appState.people_cur).get("biyeshijian").toString()); // ��ҵʱ��   
    	
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
    		
    		textView_cpwhere2.setText(appState.peopleList.get(appState.people_cur).get("xuexiao2").toString());// ��ҵԺУ    
        	textView_cpprofessional2.setText(appState.peopleList.get(appState.people_cur).get("suoxuezhuanye2").toString()); // רҵ    
        	textView_cpwhen2.setText(appState.peopleList.get(appState.people_cur).get("biyeshijian2").toString()); // ��ҵʱ��         	
        	textView_cpothereducation.setText(tmp); // ����ѧ��    	
    	}
    	
    	
    	
    	
    	
    	
    	textView_cpcurwork.setText(appState.peopleList.get(appState.people_cur).get("congshizhuanye").toString()); // ����רҵ    	
    	
    	textView_cpcurshenfenzhen.setText(appState.peopleList.get(appState.people_cur).get("shenfenzhen").toString());//���֤
    	
    	
    	
    	if ("04".equals(appState.peopleList.get(appState.people_cur).get("shenbaoleixin").toString())) {
    		textView_cpcurshenbaoleixin.setText("�Ƹ�");
		}else if ("01".equals(appState.peopleList.get(appState.people_cur).get("shenbaoleixin").toString())) {
			textView_cpcurshenbaoleixin.setText("����");
		}else if ("05".equals(appState.peopleList.get(appState.people_cur).get("shenbaoleixin").toString())) {
			textView_cpcurshenbaoleixin.setText("ƽת��ȷ��");
		}else if ("08".equals(appState.peopleList.get(appState.people_cur).get("shenbaoleixin").toString())) {
			textView_cpcurshenbaoleixin.setText("�����˲�");
		}else {
			textView_cpcurshenbaoleixin.setText("");
		}
    	//textView_cpcurshenbaoleixin.setText(appState.peopleList.get(appState.people_cur).get("shenbaoleixin").toString());//�걨����
    	
    	
    	
    	textView_cpcurshenbaojibie.setText(appState.peopleList.get(appState.people_cur).get("shenbaojibie").toString());//�걨����
    	textView_cpcurpinweihui.setText(appState.peopleList.get(appState.people_cur).get("shenbaopinweihui").toString());//��ί��
    	textView_cpyouxiu.setText(appState.peopleList.get(appState.people_cur).get("youxiu").toString() + "��");//����
    	textView_cpchenzhi.setText(appState.peopleList.get(appState.people_cur).get("chengzhi").toString()  + "��");//��ְ
    	textView_cpjibenchenzhi.setText(appState.peopleList.get(appState.people_cur).get("jibenchengzhi").toString()  + "��");//������ְ
    	
    	
    	if ("".equals(appState.peopleList.get(appState.people_cur).get("ceshijieguo").toString())){
    		textView_ceshi.setVisibility(View.INVISIBLE);
    		textView_cpceshi.setVisibility(View.INVISIBLE);
    	}else if ("F".equals(appState.peopleList.get(appState.people_cur).get("ceshijieguo").toString())){
    		textView_ceshi.setVisibility(View.VISIBLE);
    		textView_cpceshi.setVisibility(View.VISIBLE);
    		textView_cpceshi.setText("���ϸ�" );//���Խ��
    	}else{
    		textView_ceshi.setVisibility(View.VISIBLE);
    		textView_cpceshi.setVisibility(View.VISIBLE);
    		textView_cpceshi.setText(appState.peopleList.get(appState.people_cur).get("ceshijieguo").toString() +
    				"��ϸ�");//���Խ��
    	}
    	
    	
    	textView_cpenglish.setText(appState.peopleList.get(appState.people_cur).get("waiyu").toString());//����ɼ�
    	textView_cpcomputer.setText(appState.peopleList.get(appState.people_cur).get("jisuanji").toString());//�����
    	
    	if ("Y".equals(appState.peopleList.get(appState.people_cur).get("jixujinxiu").toString())){
    		textView_cpcontinuouseducation.setText("���");//��������
    	}else {
    		textView_cpcontinuouseducation.setText("");//��������
    	}
    	
    	
    	
    	
    	//��ʱ��
		appState.voteState = "δͶƱ";
    }
    
 // �鿴����
 	public void button_zhengce1_onclick(View target) throws Exception {
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
 	
 	public void button_xuelifujian_onclick(View target)  {
 		//String localPath = appState.SDpath + "psxt/" + appState.peopleList.get(appState.people_cur).get("xuelifujian").toString();
 		
 		//��ʱ����Ҫ��
// 		String localPath = appState.SDpath + "psxt/" + appState.peopleList.get(appState.people_cur).get("yilanbiao").toString();
//		SendIntent SDintent = new SendIntent();
//		Intent it = SDintent.getIntent(localPath);
//		try{
//			startActivity(it);
//		}catch( Exception e){
//			e.printStackTrace();
//			Log.e("error", e.toString());
//			Toast toast = Toast.makeText(getApplicationContext(), "û�а�װ�Ķ���/���������ļ������ڣ�", Toast.LENGTH_LONG);
//			toast.setGravity(Gravity.CENTER, 0, 0);
//			toast.show();
//		}
 	}
 	
 // �����f����ť����¼�
 	public void button_czsm1_onclick(View target) {
 		appState.launch_help();
 	}
 	
 	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// ���¼����Ϸ��ذ�ť
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			System.out.println("���ذ�ť");
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
