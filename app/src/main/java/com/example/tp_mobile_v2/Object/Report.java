package com.example.tp_mobile_v2.Object;

import java.io.Serializable;
import java.util.ArrayList;

public class Report implements Serializable   {
	/**
	 * 
	 */
	private static final long serialVersionUID = 383822478843535970L;
	private String name;
	private String id;
	private String sql;	
	private String table_des;
	private String table_value;
	private String mabc;
	private String type_sum;
	private ArrayList<Parameter> list_para;
	private int location;

//	private Parameter para;

	public Report() {
		name = "";
		id = "";
		sql = "";
		table_des = "";
		table_value = "";
		mabc = "";
		type_sum = "";
		list_para = new ArrayList<>();
		location = 0;
	}
	public Report(Report inputReport){
		name = inputReport.getName();
		id = inputReport.getId();
		sql = inputReport.getSql();
		table_des = inputReport.getTab_Des();
		table_value = inputReport.getTab_Value();
		mabc = inputReport.getMaBC();
		type_sum = inputReport.getType_sum();
//		para = new Parameter();
		list_para = inputReport.getList();
		location = inputReport.getLocation();
	}
	//set data
	public void setName (String name){
		this.name = name;
	}
	public void setId (String id) {
		this.id = id;
	}
	public void setSql (String sql) {
		this.sql = sql;
	}
	public void setTab_Des (String des){
		this.table_des = des;
	}
	public void setTab_Value (String value){
		this.table_value = value;
	}
	public void setMaBC (String mabc) {
		this.mabc = mabc;
	}
	public void setType_sum(String type_sum) {
		this.type_sum = type_sum;
	}

	public void setLocation (int in_location) {
		this.location = in_location;
	}
	//get data
	public String getName(){
		return name;
	}
	public String getId(){
		return id;
	}
	public String getSql() {
		return sql;
	}
	public String getTab_Des() {
		return table_des;
	}
	public String getTab_Value() {
		return table_value;
	}
	public String getMaBC() {
		return mabc;
	}
	public ArrayList<Parameter> getList() {
		return this.list_para;
	}
	public int getLocation(){
		return this.location;
	}

	public String getType_sum() {
		return type_sum;
	}
}
