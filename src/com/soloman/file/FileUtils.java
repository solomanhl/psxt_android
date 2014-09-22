/**
 * �ļ�����
 * 
 * @author ����
 * 
 */
package com.soloman.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;

public class FileUtils {
	public String SDPATH;

	private int FILESIZE = 4 * 1024;

	public String getSDPATH() {
		return SDPATH;
	}

	public FileUtils() {
		// �õ���ǰ�ⲿ�洢�豸��Ŀ¼( /SDCARD )
		SDPATH = Environment.getExternalStorageDirectory() + "/";
	}

	/**
	 * ��SD���ϴ����ļ�
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public File createSDFile(String fileName) throws IOException {
		File file = new File(SDPATH + fileName);
		file.createNewFile();
		return file;
	}

	public File createFile(String fileName) throws IOException {
		File file = new File(fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * ��SD���ϴ���Ŀ¼
	 * 
	 * @param dirName
	 * @return
	 */
	public File createSDDir(String dirName) {
		File dir = new File(SDPATH + dirName);
		// dir.mkdir(); //����Ŀ¼
		dir.mkdirs(); // �༶Ŀ¼
		return dir;
	}

	public File createDir(String dirName) {
		File dir = new File(dirName);
		// dir.mkdir(); //����Ŀ¼
		dir.mkdirs(); // �༶Ŀ¼
		return dir;
	}

	/**
	 * ��SD����ɾ��Ŀ¼
	 * 
	 * @param dirName
	 * @return
	 */
	public void deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					//�ݹ����
					this.deleteFile(files[i].toString());
					this.delFolder(files[i].toString());
				}
			}
			file.delete();
		} else {
			System.out.println(fileName + "��ɾ�����ļ������ڣ�" + '\n');
		}
	}

	/**
	 * ɾ���ļ���
	 * 
	 * @param folderPath
	 *            String sd�ļ���·�������� ��mnt/sdcard/ordering/ct
	 * @return
	 */
	public void delFolder(String folderPath) {
		try {
			deleteFile(folderPath); // ɾ����������������
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // ɾ�����ļ���
			System.out.println("ɾ���ļ���" + filePath + "�����ɹ�");

		} catch (Exception e) {
			System.out.println("ɾ���ļ��в�������");
			e.printStackTrace();

		}
	}

	/**
	 * �ж�SD���ϵ��ļ����Ƿ����
	 * 
	 * @param fileName
	 * @return 1������ 0��������
	 */
	public boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		return file.exists();
	}

	/**
	 * ��һ��InputStream���������д�뵽SD����
	 * 
	 * @param path
	 * @param fileName
	 * @param input
	 * @return
	 */
	public File write2SDFromInput(String path, String fileName,
			InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			createSDDir(path);// �����ļ���
			file = createSDFile(path + fileName);// ���ļ�
			output = new FileOutputStream(file);
			byte[] buffer = new byte[FILESIZE];
			int count;// countΪʵ�ʶ�ȡ���ֽ���
			while ((count = input.read(buffer)) != -1) { // ֱ������
				output.write(buffer, 0, count);
			}

			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// input.close();
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	public File writeFromInput(String path, String fileName, InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			createDir(path);// �����ļ���
			file = createFile(path + fileName);// ���ļ�
			output = new FileOutputStream(file);
			byte[] buffer = new byte[FILESIZE];
			int count;// countΪʵ�ʶ�ȡ���ֽ���
			while ((count = input.read(buffer)) != -1) { // ֱ������
				output.write(buffer, 0, count);
			}

			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// input.close();
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	public String readFile(String filePath) {
		//String rtn = "";
		StringBuilder rtnb = new StringBuilder();  

		try {
			FileReader fr = new FileReader(filePath);// ����FileReader����������ȡ�ַ���
			BufferedReader br = new BufferedReader(fr); // ����ָ���ļ�������
			// FileWriter fw = new
			// FileWriter("f:/jackie.txt");//����FileWriter��������д���ַ���
			// BufferedWriter bw = new BufferedWriter(fw); //��������ļ������
			String myreadline; // ����һ��String���͵ı���,����ÿ�ζ�ȡһ��
			while (br.ready()) {
				myreadline = br.readLine();// ��ȡһ��
				//rtn += myreadline;//ʹ��+=Ч�ʲ��ߣ��ر��ǻ��ж��ʱ��
				rtnb.append(myreadline);
				
				// bw.write(myreadline); //д���ļ�
				// bw.newLine();
				//System.out.println(myreadline);//����Ļ�����
			}
			// bw.flush(); //ˢ�¸����Ļ���
			// bw.close();
			br.close();
			// fw.close();
			br.close();
			fr.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return rtnb.toString();
	}
}
