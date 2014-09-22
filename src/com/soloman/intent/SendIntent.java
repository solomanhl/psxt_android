
/**
 * 向第三方发送intend
 * 
 * @author 
 * 
 */package com.soloman.intent;

import android.content.Intent;
import android.net.Uri;
import java.io.File;

//自定义android Intent类，

//可用于获取打开以下文件的intent

//PDF,PPT,WORD,EXCEL,CHM,HTML,TEXT,AUDIO,VIDEO

public class SendIntent {
	public static Intent getIntent(String param){
		String  tmpArray[] = param.split("\\.");
		String houzhui = "";
		
		if (tmpArray.length >0 )
			houzhui = tmpArray[tmpArray.length - 1];
		
		Intent intent = new Intent();
		if ("pdf".equals(houzhui)){
			intent =  getPDFIntent(param);
		}else if ("jpg".equals(houzhui)){
			intent = getImageFileIntent(param);
		}else if ("doc".equals(houzhui) || "docx".equals(houzhui)){
			intent = getWordFileIntent(param);
		}else if ("mp4".equals(houzhui) || "3gp".equals(houzhui) || "avi".equals(houzhui) || "dat".equals(houzhui)){
			intent = getVideoFileIntent(param);
		}
		else if ("mp3".equals(houzhui)){
			intent = getAudioFileIntent(param);
		}
		else if ("apk".equals(houzhui)){
			intent = getApkFileIntent(param);
		}
		return intent;
	}
	
	public static Intent getPDFIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/pdf");
		return intent;
	}
	
	// android获取一个用于打开图片文件的intent
	public static Intent getImageFileIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "image/*");
		return intent;
	}
	
	// android获取一个用于打开Word文件的intent
	public static Intent getWordFileIntent(String param){
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/msword");
		return intent;
	}
	
	//android获取一个用于打开视频文件的intent
	  public static Intent getVideoFileIntent( String param ){
	    Intent intent = new Intent("android.intent.action.VIEW");
	    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    intent.putExtra("oneshot", 0);
	    intent.putExtra("configchange", 0);
	    Uri uri = Uri.fromFile(new File(param ));
	    intent.setDataAndType(uri, "video/*");
	    return intent;
	  }
	  
	//android获取一个用于打开音频文件的intent
	  public static Intent getAudioFileIntent( String param ){
	    Intent intent = new Intent("android.intent.action.VIEW");
	    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    intent.putExtra("oneshot", 0);
	    intent.putExtra("configchange", 0);
	    Uri uri = Uri.fromFile(new File(param ));
	    intent.setDataAndType(uri, "audio/*");
	    return intent;
	  }
	  
	//android获取一个用于打开APK文件的intent
	  public static Intent getApkFileIntent( String param ){
	    Intent intent = new Intent("android.intent.action.VIEW");
	    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    intent.putExtra("oneshot", 0);
	    intent.putExtra("configchange", 0);
	    Uri uri = Uri.fromFile(new File(param ));
	    intent.setDataAndType(uri, "application/vnd.android.package-archive");
	    return intent;
	  }
}
