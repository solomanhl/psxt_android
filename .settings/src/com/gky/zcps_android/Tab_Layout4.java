/**
 * 论文和表彰
 * 
 * @author 贺亮
 * 
 */
package com.gky.zcps_android;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.soloman.file.FileUtils;
import com.soloman.intent.SendIntent;

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

public class Tab_Layout4 extends Activity {
	private Global_var appState; // 获得全局变量;
	private ListView listview2_results, listview3_results;
	private Button button_zhengce4, button_czsm4;
	private ArrayList<HashMap<String, Object>> lunwen, biaozhang; // 论文、表彰
	private String id, name, time, thenum;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		appState = ((Global_var) getApplicationContext()); // 获得全局变量
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_layout4);

		listview2_results = (ListView) findViewById(R.id.listview2_results);
		listview3_results = (ListView) findViewById(R.id.listview3_results);
		button_zhengce4 = (Button) findViewById(R.id.button_zhengce4);
		button_czsm4 = (Button) findViewById(R.id.button_czsm4);
	
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i("info", "Tab_Layout4_onResume");
		if (appState.isUpdated) {
			update1();
			update2();
			// appState.isUpdated = false;
			Log.i("info", "upDated");
		}

	}

	private void update1() {
		// TODO Auto-generated method stub
		// 准备数据
		lunwen = new ArrayList<HashMap<String, Object>>();
		String data_lunwenminchen[] = appState.lunwenList.get(appState.people_cur).get("lunwenminchen").toString().split("\\|");//要用转义字符
		String data_lunwenshijian[] = appState.lunwenList.get(appState.people_cur).get("lunwenshijian").toString().split("\\|");
		String data_lunwenzuozhe[] = appState.lunwenList.get(appState.people_cur).get("lunwenzuozhe").toString().split("\\|");
		String data_lunwenfujian[] = appState.lunwenList.get(appState.people_cur).get("lunwenfujian").toString().split("\\|");

		for (int i = 0; i < data_lunwenminchen.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", String.valueOf(i + 1) + "、");
			map.put("lunwenminchen", data_lunwenminchen[i]);
			
			if (i < data_lunwenshijian.length){
				map.put("lunwenshijian", data_lunwenshijian[i]);
			}else{
				map.put("lunwenshijian", "");
			}
			
			if (i < data_lunwenzuozhe.length){
				map.put("lunwenzuozhe", data_lunwenzuozhe[i]);
			}else{
				map.put("lunwenzuozhe", "");
			}
			
			if (i < data_lunwenfujian.length){
				map.put("lunwenfujian", data_lunwenfujian[i]);
			}else{
				map.put("lunwenfujian", "");
			}

			lunwen.add(map);
		}

		// 显示数据
		// 模仿SimpleAdapter实现的自己的adapter
		MyListAdapter1 adapter = new MyListAdapter1(this, lunwen);
		listview2_results.setAdapter(adapter);
	}

	private void update2() {
		// TODO Auto-generated method stub
		// 准备数据
		biaozhang = new ArrayList<HashMap<String, Object>>();
		String data_huojiangminchen[] = appState.huojiangList.get(appState.people_cur).get("huojiangminchen").toString().split("\\|");//要用转义字符
		String data_huojiangshijian[] = appState.huojiangList.get(appState.people_cur).get("huojiangshijian").toString().split("\\|");
		String data_huojiangfujian[] = appState.huojiangList.get(appState.people_cur).get("huojiangfujian").toString().split("\\|");

		for (int i = 0; i < data_huojiangminchen.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", String.valueOf(i + 1) + "、");
			map.put("huojiangminchen", data_huojiangminchen[i]);
			
			if (i < data_huojiangshijian.length){
				map.put("huojiangshijian", data_huojiangshijian[i]);
			}else{
				map.put("huojiangshijian", "");
			}
			
			if (i < data_huojiangfujian.length){
				map.put("huojiangfujian", data_huojiangfujian[i]);
			}else{
				map.put("huojiangfujian", "");
			}

			biaozhang.add(map);
		}

		// 显示数据
		// 模仿SimpleAdapter实现的自己的adapter
		MyListAdapter2 adapter = new MyListAdapter2(this, biaozhang);
		listview3_results.setAdapter(adapter);
	}

	// ------------------------------------------------------------------------------------------
	/*
	 * 以下是自定义的BaseAdapter类
	 */
	public class MyListAdapter1 extends BaseAdapter {
		private ArrayList<HashMap<String, Object>> data;
		private LayoutInflater layoutInflater;
		private Context context;

		public MyListAdapter1(Context context,
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
			Log.i("info", String.valueOf(position));
			ZuJian2 zuJian = null;
			if (convertView == null) {
				zuJian = new ZuJian2();

				// 获取组件布局
				convertView = layoutInflater.inflate(R.layout.listview_inside2,
						null);

				zuJian.in_listview2_id = (TextView) convertView
						.findViewById(R.id.in_listview2_id);
				zuJian.in_listview2_name = (TextView) convertView
						.findViewById(R.id.in_listview2_name);
				zuJian.in_listview2_time = (TextView) convertView
						.findViewById(R.id.in_listview2_time);
				zuJian.in_listview2_thenum = (TextView) convertView
						.findViewById(R.id.in_listview2_thenum);

				zuJian.in_listview2_see = (Button) convertView
						.findViewById(R.id.in_listview2_see);

				// 这里要注意，是使用的tag来存储数据的。
				convertView.setTag(zuJian);

			} else {
				zuJian = (ZuJian2) convertView.getTag();
			}

			if (position == 0) {
				Log.i("info", String.valueOf(position));
				;
			}

			zuJian.in_listview2_id.setText((String) data.get(position).get("id"));
			zuJian.in_listview2_name.setText((String) data.get(position).get("lunwenminchen"));
			zuJian.in_listview2_time.setText((String) data.get(position).get("lunwenshijian"));
			zuJian.in_listview2_thenum.setText((String) data.get(position).get("lunwenzuozhe"));

			
			if ( data.get(position).get("lunwenfujian").toString() == null 
					|| ("".equals(data.get(position).get("lunwenfujian").toString().trim()))
					|| data.get(position).get("lunwenfujian").toString().trim().length() == 0 ) {
				//如果该按钮对应的附件为空，则隐藏
				zuJian.in_listview2_see.setVisibility(4);//0可见 4不可见 8gone
			}else{
				zuJian.in_listview2_see.setVisibility(0);//0可见 4不可见 8gone
			}
			
			
			// 点击查看，打查看文件
			zuJian.in_listview2_see.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					in_listview2_see(position);
				}
			});

			return convertView;
		}

		private void in_listview2_see(int position) {
			// TODO Auto-generated method stub
			Log.i("info",
					"生成button_in_listview2_see_onClick 事件:"
							+ String.valueOf(position));
			Log.i("info",data.get(position).get("lunwenfujian").toString());
			
			String localPath = appState.SDpath + "psxt/" + data.get(position).get("lunwenfujian").toString();
			SendIntent SDintent = new SendIntent();
			Intent it = SDintent.getIntent(localPath);
			try{
				startActivity(it);
			}catch( Exception e){
				e.printStackTrace();
				Log.e("error", e.toString());
				Toast toast = Toast.makeText(getApplicationContext(), "没有安装阅读器或文件格式不正确！", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		}

	}
	// ---------------------------------------------------------------------------
	
	// ------------------------------------------------------------------------------------------
		/*
		 * 以下是自定义的BaseAdapter类
		 */
		public class MyListAdapter2 extends BaseAdapter {
			private ArrayList<HashMap<String, Object>> data;
			private LayoutInflater layoutInflater;
			private Context context;

			public MyListAdapter2(Context context,
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
				Log.i("info", String.valueOf(position));
				ZuJian3 zuJian = null;
				if (convertView == null) {
					zuJian = new ZuJian3();

					// 获取组件布局
					convertView = layoutInflater.inflate(R.layout.listview_inside3,
							null);

					zuJian.in_listview3_id = (TextView) convertView
							.findViewById(R.id.in_listview3_id);
					zuJian.in_listview3_name = (TextView) convertView
							.findViewById(R.id.in_listview3_name);
					zuJian.in_listview3_time = (TextView) convertView
							.findViewById(R.id.in_listview3_time);

					zuJian.in_listview3_see = (Button) convertView
							.findViewById(R.id.in_listview3_see);

					// 这里要注意，是使用的tag来存储数据的。
					convertView.setTag(zuJian);

				} else {
					zuJian = (ZuJian3) convertView.getTag();
				}

				if (position == 0) {
					Log.i("info", String.valueOf(position));
					;
				}

				zuJian.in_listview3_id.setText((String) data.get(position).get("id"));
				zuJian.in_listview3_name.setText((String) data.get(position).get("huojiangminchen"));
				zuJian.in_listview3_time.setText((String) data.get(position).get("huojiangshijian"));

				
				if ( data.get(position).get("huojiangfujian").toString() == null 
						|| ("".equals(data.get(position).get("huojiangfujian").toString().trim()))
						|| data.get(position).get("huojiangfujian").toString().trim().length() == 0 ) {
					//如果该按钮对应的附件为空，则隐藏
					zuJian.in_listview3_see.setVisibility(4);//0可见 4不可见 8gone
				}else{
					zuJian.in_listview3_see.setVisibility(0);//0可见 4不可见 8gone
				}
				
				
				// 点击查看，打查看文件
				zuJian.in_listview3_see.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						in_listview3_see(position);
					}
				});

				return convertView;
			}

			private void in_listview3_see(int position) {
				// TODO Auto-generated method stub
				Log.i("info",
						"生成button_in_listview3_see_onClick 事件:"
								+ String.valueOf(position));
				Log.i("info",data.get(position).get("huojiangfujian").toString());
				
				String localPath = appState.SDpath + "psxt/" + data.get(position).get("huojiangfujian").toString();
				SendIntent SDintent = new SendIntent();
				Intent it = SDintent.getIntent(localPath);
				try{
					startActivity(it);
				}catch( Exception e){
					e.printStackTrace();
					Log.e("error", e.toString());
					Toast toast = Toast.makeText(getApplicationContext(), "没有安装阅读器或文件格式不正确！", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			}

		}
		// ---------------------------------------------------------------------------
		
		// 查看政策
	 	public void button_zhengce4_onclick(View target) throws Exception {
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
		public void button_czsm4_onclick(View target) {
			appState.launch_help();
		}
}
