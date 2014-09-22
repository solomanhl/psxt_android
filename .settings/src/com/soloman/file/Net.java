/**
 * ��������
 * 
 * @author ����
 * 
 */
package com.soloman.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Net {
	private URL url = null; 
	/**
	 * 
	 * @param urlStr
	 * @param path
	 * @param fileName
	 * @return 
	 * 		-1:�ļ����س���
	 * 		 0:�ļ����سɹ�
	 * 		 1:�ļ��Ѿ�����
	 */
	public int downFile(String urlStr, String path, String fileName)
	{
		InputStream inputStream = null;
		int rtn=0;
		try 
		{
			FileUtils fileUtils = new FileUtils();//��FileUtils��FileUtils.java
			
			/*
			if(fileUtils.isFileExist(path + fileName))
			{
				rtn=1;
				return rtn;
			} 
			else {
			*/
			
				inputStream = getInputStreamFromURL(urlStr);	//����
				if (inputStream != null){//ȷʵ���ص��˲�д�ļ�������0�ļ�,������ɾ��
					if(fileUtils.isFileExist(path + fileName)){	//����У���ɾ��
						fileUtils.deleteFile(path + fileName);
						System.out.println("ɾ����" +  path + fileName);
					}
					//File resultFile = fileUtils.write2SDFromInput(path, fileName, inputStream);	//����
					File resultFile = fileUtils.writeFromInput(path, fileName, inputStream);	//����
					if(resultFile == null)
					{
						rtn=-1;
						return rtn;
				}
				
				
				}
			//}	// end else
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			rtn=-1;
			return rtn;
		}
		finally
		{
			try 
			{
				if (rtn!=1)//��ֹ�ļ�����ʱ��ִ������������
				{
					if (inputStream != null)
						inputStream.close();
				}
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		rtn=0;
		return rtn;
	}

	
	
	/**
	 * ����URL�õ�������
	 * @param urlStr
	 * @return
	 */
	public InputStream getInputStreamFromURL(String urlStr) 
	{
		
		HttpURLConnection urlConn = null;
		InputStream inputStream = null;
		try 
		{
			url = new URL(urlStr);
			urlConn = (HttpURLConnection)url.openConnection();
			inputStream = urlConn.getInputStream();
			
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return inputStream;
	}
	
}
