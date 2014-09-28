/**
 * ����xml
 * 
 * @author ����
 * 
 */
package com.soloman.xml;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XML extends DefaultHandler {
	String currentPeople = "";

	//��ί��id
	public String pwhid = "";
	//�������
	public String yichen = "";
	//������Ϣ
	String name = "";
	String id = "";
	String shenfenzhen = "";
	String sex = "";
	String birth = "";
	String company = "";
	String xueli = "";
	String xinzhenzhiwu = "";
	String zhenzhi = "";
	String jishuzhiwu = "";
	String shoupinshijian = "";
	String xuexiao = "";
	String suoxuezhuanye = "";
	String biyeshijian = "";
	String xueli2 = "";
	String xuexiao2="";
	String suoxuezhuanye2="";
	String biyeshijian2="";
	//String xuelifujian = "";
	String congshizhuanye = "";
	//String zhuanjigongzuo="";
	String waiyu="";
	//String waiyufujian="";
	String jisuanji="";
	//String jisuanjifujian="";
	String jixujinxiu="";
	//String jixujinxiufujian="";
	String shenbaoleixin="";
	String shenbaojibie="";
	String shenbaopinweihui="";
	String ceshijieguo="";	
	String youxiu="";
	String chengzhi="";
	String jibenchengzhi="";
	//����
	String kaoheniandu = "";
	String kaohechenji = "";
	//������ί
	String expert_name = "";
	//�Ƿ�����
	String lianghua = "";
	String gerenyijian = "";	//���������ֻ��и������
	
	/*�⼸������Ҫ��
	//�ɹ�
	String chengguominchen = "";
	String benrenzuoyong = "";
	String chengguoshijian = "";
	String chengguoneirong = "";
	String chengguofujian = "";
	//����
	String lunwenminchen = "";
	String lunwenshijian = "";
	String lunwenzuozhe = "";
	String lunwenfujian = "";
	//�񽱱���
	String huojiangminchen = "";
	String huojiangshijian = "";
	String huojiangfujian = "";
	*/
	
	//����һ����  ��Ƭ
	String yilanbiao = "";
	String zhaopian = "";
	
	//������Ϣ
	String pogejielun="";
	String opinion =""; //С�����
	String pinjunfen = "";
	String group_score = "";	//С������
	String noeduc="";
	String dbresult="";
	
	public ArrayList<HashMap<String, Object>> peopleList = new ArrayList<HashMap<String, Object>>();
	//public ArrayList<HashMap<String, Object>> kaoheList = new ArrayList<HashMap<String, Object>>();
	/*�ɹ����Ļ񽱲�Ҫ��
	public ArrayList<HashMap<String, Object>> chengguoList = new ArrayList<HashMap<String, Object>>();
	public ArrayList<HashMap<String, Object>> lunwenList = new ArrayList<HashMap<String, Object>>();
	public ArrayList<HashMap<String, Object>> huojiangList = new ArrayList<HashMap<String, Object>>();
	*/
	
	
	public ArrayList<HashMap<String, Object>> scoreList = new ArrayList<HashMap<String, Object>>();//�Ƹ��ƽ����
	
	//��ͨ������
	String id_f = "";
	String name_f = "";
	String company_f = "";
	public ArrayList<HashMap<String, Object>> failedList = new ArrayList<HashMap<String, Object>>();//��ͨ������

	public void startDocument() throws SAXException {
		System.out.println("XML������ʼ");
	}

	public void startElement(String nameSpaceURI, String localName,	String qName, Attributes attr) throws SAXException {
		
		if ("root".equals(localName.trim())) {
			//System.out.println("��ʼ����root");
			// ��ȡ��ǩ��ȫ������
			//System.out.println(attr.getLocalName(0) + "=" + attr.getValue(0));
			
			pwhid = 	attr.getValue(1);		
			yichen = attr.getValue(2);
		} else if ("people".equals(localName.trim())) {
			//System.out.println("��ʼ����people");
			// ��ȡ��ǩ��ȫ������
			//System.out.println(attr.getLocalName(0) + "=" + attr.getValue(0));
			currentPeople = localName;
			getJichuxinxi(nameSpaceURI, localName,	qName, attr);			
		} else if ("kaohe".equals(localName.trim())){
			//System.out.println("��ʼ����kaohe");
			// ��ȡ��ǩ��ȫ������
			//System.out.println(attr.getLocalName(0) + "=" + attr.getValue(0));
			//getKaohe(nameSpaceURI, localName,	qName, attr);						
		} 
		/*�ɹ����Ļ񽱲�Ҫ��
		else if ("chengguo".equals(localName.trim())){
			//System.out.println("��ʼ����chengguo");
			// ��ȡ��ǩ��ȫ������
			//System.out.println(attr.getLocalName(0) + "=" + attr.getValue(0));	
			getChengguo(nameSpaceURI, localName,	qName, attr);
		}else if ("lunwen".equals(localName.trim())){
			//System.out.println("��ʼ����lunwen");
			// ��ȡ��ǩ��ȫ������
			//System.out.println(attr.getLocalName(0) + "=" + attr.getValue(0));	
			getLunwen(nameSpaceURI, localName,	qName, attr);
		}else if ("huojiang".equals(localName.trim())){
			//System.out.println("��ʼ����huojiang");
			// ��ȡ��ǩ��ȫ������
			//System.out.println(attr.getLocalName(0) + "=" + attr.getValue(0));	
			getHuojiang(nameSpaceURI, localName,	qName, attr);
		}
		*/		
		
		else if ("ext".equals(localName.trim())){
			//System.out.println("��ʼ����ext���Ƹ���ۺ�ƽ����");
			// ��ȡ��ǩ��ȫ������
			//System.out.println(attr.getLocalName(0) + "=" + attr.getValue(0));	
			getExt(nameSpaceURI, localName,	qName, attr);
		}else if ("f".equals(localName.trim())){
			//System.out.println("��ʼ����f����ͨ������");
			// ��ȡ��ǩ��ȫ������
			//System.out.println(attr.getLocalName(0) + "=" + attr.getValue(0));	
			getF(nameSpaceURI, localName,	qName, attr);
		}
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		
	}

	public void endElement(String nameSpaceURI, String localName, String qName)	throws SAXException {
		//System.out.println("localName = " + localName);
		if ("root".equals(localName.trim())) {
			clearAll();
		}
	}

	public void endDocument() throws SAXException {
		System.out.println("XML�������");
	}
	
	private void clearAll(){
		currentPeople = "";

		//pwhid = "";
		// ������Ϣ
		name = "";
		id = "";
		shenfenzhen = "";
		sex = "";
		birth = "";
		company = "";
		xueli = "";
		xinzhenzhiwu = "";
		zhenzhi = "";
		jishuzhiwu = "";
		shoupinshijian = "";
		xuexiao = "";
		suoxuezhuanye = "";
		biyeshijian = "";
		xueli2 = "";
		xuexiao2="";
		suoxuezhuanye2="";
		 biyeshijian2="";
		//xuelifujian = "";
		congshizhuanye = "";
		//zhuanjigongzuo = "";
		waiyu = "";
		//waiyufujian = "";
		jisuanji = "";
		//jisuanjifujian = "";
		jixujinxiu = "";
		//jixujinxiufujian = "";
		shenbaoleixin="";
		shenbaojibie="";
		shenbaopinweihui="";
		ceshijieguo="";
		youxiu="";
		chengzhi="";
		jibenchengzhi="";
		// ����
		kaoheniandu = "";
		kaohechenji = "";
		//������ί
		expert_name = "";
		//�Ƿ�����
		lianghua = "";
		//�������
		gerenyijian = "";
		
		/*�ɹ����Ļ񽱲�Ҫ��
		// �ɹ�
		chengguominchen = "";
		benrenzuoyong = "";
		chengguoshijian = "";
		chengguoneirong = "";
		chengguofujian = "";
		// ����
		lunwenminchen = "";
		lunwenshijian = "";
		lunwenzuozhe = "";
		lunwenfujian = "";
		// �񽱱���
		huojiangminchen = "";
		huojiangshijian = "";
		huojiangfujian = "";
		*/
		
		//����һ������Ƭ
		yilanbiao = "";
		zhaopian = "";
		
		pogejielun="";
		opinion ="";
		pinjunfen = "";
		group_score = "";
		noeduc="";
		dbresult="";
	}
	
	private void getJichuxinxi(String nameSpaceURI, String localName,	String qName, Attributes attr){
		name = attr.getValue("name");
		id = attr.getValue("id");
		shenfenzhen = attr.getValue("shenfenzhen");
		sex = attr.getValue("sex");
		birth = attr.getValue("birth");
		company = attr.getValue("company");
		xueli = attr.getValue("xueli");
		xinzhenzhiwu = attr.getValue("xinzhenzhiwu");
		zhenzhi = attr.getValue("zhenzhi");
		jishuzhiwu = attr.getValue("jishuzhiwu");
		shoupinshijian = attr.getValue("shoupinshijian");
		xuexiao = attr.getValue("xuexiao");
		suoxuezhuanye = attr.getValue("suoxuezhuanye");
		biyeshijian = attr.getValue("biyeshijian");
		xueli2 = attr.getValue("xueli2");
		xuexiao2= attr.getValue("xuexiao2");
		suoxuezhuanye2= attr.getValue("suoxuezhuanye2");
		 biyeshijian2= attr.getValue("biyeshijian2");
		//xuelifujian = attr.getValue("xuelifujian");
		congshizhuanye = attr.getValue("congshizhuanye");
		//zhuanjigongzuo = attr.getValue("zhuanjigongzuo");
		waiyu = attr.getValue("waiyu");
		//waiyufujian = attr.getValue("waiyufujian");
		jisuanji = attr.getValue("jisuanji");
		//jisuanjifujian = attr.getValue("jisuanjifujian");
		jixujinxiu = attr.getValue("jixujinxiu");
		//jixujinxiufujian = attr.getValue("jixujinxiufujian");
		shenbaoleixin = attr.getValue("shenbaoleixin");
		shenbaojibie = attr.getValue("shenbaojibie");
		shenbaopinweihui = attr.getValue("shenbaopinweihui");
		ceshijieguo = attr.getValue("ceshijieguo");

		youxiu = attr.getValue("youxiu");
		chengzhi = attr.getValue("chengzhi");
		jibenchengzhi = attr.getValue("jibenchengzhi");
		
		//����һ������Ƭ
		yilanbiao =attr.getValue("ylb");
		zhaopian = attr.getValue("zp");
		
		//������ί
		expert_name = attr.getValue("expert_name");
		
		//�Ƿ�����
		lianghua = attr.getValue("lianghua");
		
		//�������
		gerenyijian = attr.getValue("gerenyijian");

		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("name", new String(name.getBytes(), "utf-8"));
			map.put("id", new String(id.getBytes(), "utf-8"));
			map.put("shenfenzhen", new String(shenfenzhen.getBytes(), "utf-8"));
			map.put("sex", new String(sex.getBytes(), "utf-8"));
			map.put("birth", new String(birth.getBytes(), "utf-8"));
			map.put("company", new String(company.getBytes(), "utf-8"));
			map.put("xueli", new String(xueli.getBytes(), "utf-8"));
			map.put("xinzhenzhiwu", new String(xinzhenzhiwu.getBytes(), "utf-8"));
			map.put("zhenzhi", new String(zhenzhi.getBytes(), "utf-8"));
			map.put("jishuzhiwu", new String(jishuzhiwu.getBytes(), "utf-8"));
			map.put("shoupinshijian", new String(shoupinshijian.getBytes(), "utf-8"));
			map.put("xuexiao", new String(xuexiao.getBytes(), "utf-8"));
			map.put("suoxuezhuanye", new String(suoxuezhuanye.getBytes(), "utf-8"));
			map.put("biyeshijian", new String(biyeshijian.getBytes(), "utf-8"));
			map.put("xueli2", new String(xueli2.getBytes(), "utf-8"));
			map.put("xuexiao2", new String(xuexiao2.getBytes(), "utf-8"));
			map.put("suoxuezhuanye2", new String(suoxuezhuanye2.getBytes(), "utf-8"));
			map.put("biyeshijian2", new String(biyeshijian2.getBytes(), "utf-8"));
			//map.put("xuelifujian", new String(xuelifujian.getBytes(), "utf-8"));
			map.put("congshizhuanye", new String(congshizhuanye.getBytes(), "utf-8"));
			//map.put("zhuanjigongzuo", new String(zhuanjigongzuo.getBytes(), "utf-8"));
			map.put("waiyu", new String(waiyu.getBytes(), "utf-8"));
			//map.put("waiyufujian", new String(waiyufujian.getBytes(), "utf-8"));
			map.put("jisuanji", new String(jisuanji.getBytes(), "utf-8"));
			//map.put("jisuanjifujian", new String(jisuanjifujian.getBytes(), "utf-8"));
			map.put("jixujinxiu", new String(jixujinxiu.getBytes(), "utf-8"));
			//map.put("jixujinxiufujian", new String(jixujinxiufujian.getBytes(), "utf-8"));
			
			map.put("shenbaoleixin", new String(shenbaoleixin.getBytes(), "utf-8"));
			map.put("shenbaojibie", new String(shenbaojibie.getBytes(), "utf-8"));
			map.put("shenbaopinweihui", new String(shenbaopinweihui.getBytes(), "utf-8"));
			map.put("ceshijieguo", new String(ceshijieguo.getBytes(), "utf-8"));
			
			map.put("youxiu", new String(youxiu.getBytes(), "utf-8"));
			map.put("chengzhi", new String(chengzhi.getBytes(), "utf-8"));
			map.put("jibenchengzhi", new String(jibenchengzhi.getBytes(), "utf-8"));
			
			map.put("expert_name", new String(expert_name.getBytes(), "utf-8"));
			
			map.put("lianghua", new String(lianghua.getBytes(), "utf-8"));
			map.put("gerenyijian", new String(gerenyijian.getBytes(), "utf-8"));
			
			
			//����һ�������Ƭ
			//map.put("yilanbiao", new String(yilanbiao.getBytes(), "utf-8"));
			//map.put("zhaopian", new String(zhaopian.getBytes(), "utf-8"));
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		peopleList.add(map);
	}
	
	/*
	private void getKaohe(String nameSpaceURI, String localName,	String qName, Attributes attr){
		kaoheniandu = attr.getValue("niandu");
		kaohechenji = attr.getValue("chengji");		

		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("kaoheniandu", new String(kaoheniandu.getBytes(), "utf-8"));
			map.put("kaohechenji", new String(kaohechenji.getBytes(), "utf-8"));			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		kaoheList.add(map);
	}
	*/
	
	/*�ɹ����Ļ񽱲�Ҫ��
	private void getChengguo(String nameSpaceURI, String localName,	String qName, Attributes attr){
		chengguominchen = attr.getValue("name");
		benrenzuoyong = attr.getValue("benrenzuoyong");		
		chengguoshijian = attr.getValue("shijian");
		chengguoneirong = attr.getValue("neirong");
		chengguoneirong.replace("\r\n", "\n");
		chengguofujian = attr.getValue("fujian");

		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("chengguominchen", new String(chengguominchen.getBytes(), "utf-8"));
			map.put("benrenzuoyong", new String(benrenzuoyong.getBytes(), "utf-8"));	
			map.put("chengguoshijian", new String(chengguoshijian.getBytes(), "utf-8"));
			map.put("chengguoneirong", new String(chengguoneirong.getBytes(), "utf-8"));
			map.put("chengguofujian", new String(chengguofujian.getBytes(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		chengguoList.add(map);
	}
	
	private void getLunwen(String nameSpaceURI, String localName,	String qName, Attributes attr){
		lunwenminchen = attr.getValue("name");
		lunwenshijian = attr.getValue("shijian");		
		lunwenzuozhe = attr.getValue("zuozhe");
		lunwenfujian = attr.getValue("fujian");

		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("lunwenminchen", new String(lunwenminchen.getBytes(), "utf-8"));
			map.put("lunwenshijian", new String(lunwenshijian.getBytes(), "utf-8"));	
			map.put("lunwenzuozhe", new String(lunwenzuozhe.getBytes(), "utf-8"));
			map.put("lunwenfujian", new String(lunwenfujian.getBytes(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lunwenList.add(map);
	}
	
	private void getHuojiang(String nameSpaceURI, String localName,	String qName, Attributes attr){
		huojiangminchen = attr.getValue("name");
		huojiangshijian = attr.getValue("shijian");		
		huojiangfujian = attr.getValue("fujian");

		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("huojiangminchen", new String(huojiangminchen.getBytes(), "utf-8"));
			map.put("huojiangshijian", new String(huojiangshijian.getBytes(), "utf-8"));	
			map.put("huojiangfujian", new String(huojiangfujian.getBytes(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		huojiangList.add(map);
	}
	*/
	
	
	
	private void getExt(String nameSpaceURI, String localName,	String qName, Attributes attr){
		pogejielun = attr.getValue("pogejielun");
		opinion = attr.getValue("opinion");
		pinjunfen = attr.getValue("pinjunfen");		
		group_score = attr.getValue("group_score");		
		noeduc = attr.getValue("noeduc");	
		dbresult = attr.getValue("dbresult");		
		

		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("pogejielun", new String(pogejielun.getBytes(), "utf-8"));
			map.put("opinion", new String(opinion.getBytes(), "utf-8"));
			map.put("pinjunfen", new String(pinjunfen.getBytes(), "utf-8"));	
			map.put("group_score", new String(group_score.getBytes(), "utf-8"));	
			map.put("noeduc", new String(noeduc.getBytes(), "utf-8"));	
			map.put("dbresult", new String(dbresult.getBytes(), "utf-8"));	
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scoreList.add(map);
	}
	
	private void getF(String nameSpaceURI, String localName,	String qName, Attributes attr){
		id_f = attr.getValue("id_f");
		name_f = attr.getValue("name_f");		
		company_f = attr.getValue("company_f");

		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("id_f", new String(id_f.getBytes(), "utf-8"));
			map.put("name_f", new String(name_f.getBytes(), "utf-8"));	
			map.put("company_f", new String(company_f.getBytes(), "utf-8"));	
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		failedList.add(map);
	}
}
