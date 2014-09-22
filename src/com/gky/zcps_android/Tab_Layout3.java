/**
 * 工作业绩和成果
 * 
 * @author 贺亮
 * 
 */
package com.gky.zcps_android;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.soloman.file.FileUtils;
import com.soloman.intent.SendIntent;

public class Tab_Layout3 extends Activity {
	private Global_var appState; // 获得全局变量;
	private ListView listview_results;
	private Button button_zhengce3, button_czsm3;
	private ArrayList<HashMap<String, Object>> chenguo; // 成果内容

	@Override
	public void onCreate(Bundle savedInstanceState) {
		appState = ((Global_var) getApplicationContext()); // 获得全局变量
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_layout3);

		listview_results = (ListView) findViewById(R.id.listview_results);
		button_zhengce3 = (Button) findViewById(R.id.button_zhengce3);
		button_czsm3 = (Button) findViewById(R.id.button_czsm3);
		
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i("info", "Tab_Layout3_onResume");
		if (appState.isUpdated) {
			update();
			// appState.isUpdated = false;
			Log.i("info", "upDated");
		}
		
	}

	private void update() {
		// TODO Auto-generated method stub
		// 准备数据
		chenguo = new ArrayList<HashMap<String, Object>>();
		String data_chengguominchen[] = appState.chengguoList.get(appState.people_cur).get("chengguominchen").toString().split("\\|");//要用转义字符
		String data_chengguoshijian[] = appState.chengguoList.get(appState.people_cur).get("chengguoshijian").toString().split("\\|");
		String data_chengguoneirong[] = appState.chengguoList.get(appState.people_cur).get("chengguoneirong").toString().split("\\|");
		String data_benrenzuoyong[] = appState.chengguoList.get(appState.people_cur).get("benrenzuoyong").toString().split("\\|");
		String data_chengguofujian[] = appState.chengguoList.get(appState.people_cur).get("chengguofujian").toString().split("\\|");
		
		for (int i = 0; i < data_chengguominchen.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", String.valueOf(i+1)+"、");
			
			if (i < data_benrenzuoyong.length){
				map.put("benrenzuoyong", data_benrenzuoyong[i]);
			}else{
				map.put("benrenzuoyong", "");
			}
			
			map.put("chengguominchen", data_chengguominchen[i]);
			
			if (i < data_chengguoshijian.length){
				map.put("chengguoshijian", data_chengguoshijian[i]);
			}else{
				map.put("chengguoshijian", "");
			}
			
			if (i < data_chengguoneirong.length){
				map.put("chengguoneirong", data_chengguoneirong[i]);
			}else{
				map.put("chengguoneirong", "");
			}
			
			if (i < data_chengguofujian.length){
				map.put("chengguofujian", data_chengguofujian[i]);
			}else{
				map.put("chengguofujian", "");
			}
			chenguo.add(map);
		}

		
		// 显示数据
		// 模仿SimpleAdapter实现的自己的adapter
		MyListAdapter adapter = new MyListAdapter(this, chenguo);
		listview_results.setAdapter(adapter);
	}

	// ------------------------------------------------------------------------------------------
	/*
	 * 以下是自定义的BaseAdapter类
	 */
	public class MyListAdapter extends BaseAdapter {
		private ArrayList<HashMap<String, Object>> data;
		private LayoutInflater layoutInflater;
		private Context context;

		public MyListAdapter(Context context,
				ArrayList<HashMap<String, Object>> data) {
			this.context = context;
			this.data = data;
			this.layoutInflater = LayoutInflater.from(context);
		}

		/**
		 * 获取列数
		 */
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		/**
		 * 获取某一位置的数据
		 */
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		/**
		 * 获取唯一标识
		 */
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		/**
		 * android绘制每一列的时候，都会调用这个方法
		 */

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			Log.i("info",String.valueOf(position));
			ZuJian zuJian = null;
			if (convertView == null) {
				zuJian = new ZuJian();

				// 获取组件布局
				convertView = layoutInflater.inflate(R.layout.listview_inside,
						null);

				zuJian.in_listview_id = (TextView) convertView
						.findViewById(R.id.in_listview_id);
				zuJian.in_listview_type = (TextView) convertView
						.findViewById(R.id.in_listview_type);
				zuJian.in_listview_name = (TextView) convertView
						.findViewById(R.id.in_listview_name);
				zuJian.in_listview_time = (TextView) convertView
						.findViewById(R.id.in_listview_time);
				zuJian.in_listview_detail = (TextView) convertView
						.findViewById(R.id.in_listview_detail);

				zuJian.in_listview_see = (Button) convertView
						.findViewById(R.id.in_listview_see);

				// 这里要注意，是使用的tag来存储数据的。
				convertView.setTag(zuJian);

			} else {
				zuJian = (ZuJian) convertView.getTag();
			}

			if (position == 0) {
				Log.i("info",String.valueOf(position));
				;
			}

			zuJian.in_listview_id.setText((String) data.get(position).get("id"));
			zuJian.in_listview_type.setText((String) data.get(position).get("benrenzuoyong"));
			zuJian.in_listview_name.setText((String) data.get(position).get("chengguominchen"));
			zuJian.in_listview_time.setText((String) data.get(position).get("chengguoshijian"));
			zuJian.in_listview_detail.setText((String) data.get(position).get("chengguoneirong"));
			
			
			if ( data.get(position).get("chengguofujian").toString() == null 
					|| ("".equals(data.get(position).get("chengguofujian").toString().trim()))
					|| data.get(position).get("chengguofujian").toString().trim().length() == 0 ) {
				//如果该按钮对应的附件为空，则隐藏
				zuJian.in_listview_see.setVisibility(4);//0可见 4不可见 8gone
			}else{
				zuJian.in_listview_see.setVisibility(0);//0可见 4不可见 8gone
			}

			// 点击查看，打查看文件
			zuJian.in_listview_see.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					in_listview_see(position);
				}
			});

			return convertView;
		}

		private void in_listview_see(int position) {
			// TODO Auto-generated method stub
			Log.i("info",
					"生成button_in_listview_see_onClick 事件:"
							+ String.valueOf(position));

			Log.i("info", data.get(position).get("chengguofujian").toString());

			String localPath = appState.SDpath + "psxt/" + data.get(position).get("chengguofujian").toString();
			SendIntent SDintent = new SendIntent();
			Intent it = SDintent.getIntent(localPath);
			try{
				startActivity(it);
			}catch( Exception e){
				e.printStackTrace();
				Log.e("error", e.toString());
				Toast toast = Toast.makeText(getApplicationContext(), "没有安装阅读器/播放器或文件格式不正确！", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}

		}

	}
	// ---------------------------------------------------------------------------
	
	// 查看政策
	 	public void button_zhengce3_onclick(View target) throws Exception {
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
	 	
	 // 操作說明按钮点击事件
		public void button_czsm3_onclick(View target) {
			appState.launch_help();
		}
}
