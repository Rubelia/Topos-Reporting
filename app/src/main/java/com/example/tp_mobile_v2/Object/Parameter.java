package com.example.tp_mobile_v2.Object;

import java.io.Serializable;

public class Parameter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3874217930623095070L;
	String caption;
	String para;
	String datatype;
	String SQL;
	String str;
	String des;
	String replace;
	boolean hasDate ;
	boolean hasParameter;
	int numDatePicker;
	
	public Parameter() {
		caption = "";
		para = "";
		datatype = "";
		SQL = "";
		hasDate = false;
		hasParameter = false;
		str = "";
		des = "";
		replace = "";
	}
	public Parameter(String inputStr){
		caption = "";
		para = "";
		datatype = "";
		SQL = "";
		hasDate = false;
		hasParameter = false;
		str = "";
		des = "";
		replace = "";
		str = inputStr;
		hasDate = false;
		hasParameter = false;
		CheckDate();
		CheckParameter();
	}
	public int getNumDatePicker(String in) {
		int result = 0;
		int start = CheckDate(in);
		if(start > 0) {
			result ++;
			getNumDatePicker(in.substring(start,in.length()));
		}
		return result;
	}
	public int CheckDate(String in)
	{
		StringBuffer buff = new StringBuffer(in);
		return buff.indexOf("{thangnam}");

	}
	public void CheckDate()
	{
		StringBuffer buff = new StringBuffer(str);
		int start = buff.indexOf("{thangnam}");
		if(start > 0) {
			hasDate = true;
		}
		else hasDate = false;
	}
	public void Repalce(String date) {
		StringBuffer buff = new StringBuffer(str);
		int start = buff.indexOf("{thangnam}");
		int end = start + 10;
		if(start > 0) {
			StringBuilder a = new StringBuilder();
            a.append(str);
            a.replace(start, end, date);
		}
		str = str.toString();
//		System.out.println(str);
	}
	public String getStr()
	{
		return this.str;
	}
	public boolean CheckParameter() {
//		Pattern pattern = Pattern.compile("\\s)");
//		Matcher matcher = pattern.matcher(str);
//		hasParameter = matcher.find();
		StringBuffer buff = new StringBuffer(str);
		int start = buff.indexOf("@");
		if( start > 0)
			return hasParameter = true;
		else return hasParameter =  false;
	}
	//set data
	public void setCaption (String inCaption) {
		this.caption = inCaption;
	}
	public void setPara (String inPara) {
		this.para = inPara;
	}
	public void setDataType (String inDataType) {
		this.datatype = inDataType;
	}
	public void setSQL (String inSql) {
		this.SQL = inSql;
	}
	public void setDes (String inDes) {
		this.des = inDes ;
	}
	public void setReplace (String inRe) {
		this.replace = inRe;
	}
	//get data
	public String getCaption() {
		return this.caption;
	}
	public String getPara() {
		return this.para;
	}
	public String getDataType() {
		return this.datatype;
	}
	public String getSQL() {
		return this.SQL;
	}
	public String getDes() {
		return this.des;
	}
	public String getRep() {
		return this.replace;
	}

	public boolean isHasDate() {
		return hasDate;
	}
}