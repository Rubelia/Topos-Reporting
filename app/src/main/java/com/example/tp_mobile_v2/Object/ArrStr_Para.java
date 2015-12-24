package com.example.tp_mobile_v2.Object;

public class ArrStr_Para {
	private String values_1 ;
	private String values_2 ;
	private String check;
	boolean selected ;
	public ArrStr_Para() {
		values_1 = new String();
		values_2 = new String();
		new String();
		check = new String();
		selected = false;
	}
	
	//set data
	public void  setValues1(String in) {
		this.values_1 = in;
	}
	public void setValues2 (String in) {
		this.values_2 = in;
	}
	public void setTxt (String in) {
	}
	public void setCheck (String in) {
		this.check = in;
	}
	public void setSelected(boolean in) {
		this.selected = in;
	}
	//get data
	public String getValues1() {
		return this.values_1;
	}
	public String getValues2() {
		return this.values_2;
	}
	public boolean isSelected() {
		return selected;
	}
	public String getCheck() {
		return this.check;
	}
//	public boolean isSelected()
}