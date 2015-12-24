package com.example.tp_mobile_v2.Object;

import java.io.Serializable;



public class Frag implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	public int position, max_list;
	String type_pos, type_next, type_back;
	
	public Frag (int in_pos, int in_max,String in_type_pos, String in_type_back, String in_type_next) {
		position = in_pos;
		max_list = in_max;
		type_pos = "";
		type_next = "";
		type_back = "";
		type_pos = in_type_pos;
		type_next = in_type_next;
		type_back = in_type_back;
	}
	public Frag() {
		position = 0;
		max_list = 0;
		type_pos = "";
		type_next = "";
		type_back = "";
	}

	public int getMax_list() {
		return max_list;
	}

	public int getPosition() {
		return position;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getType_back() {
		return type_back;
	}

	public String getType_next() {
		return type_next;
	}

	public String getType_pos() {
		return type_pos;
	}

	public void setMax_list(int max_list) {
		this.max_list = max_list;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public void setType_back(String type_back) {
		this.type_back = type_back;
	}

	public void setType_next(String type_next) {
		this.type_next = type_next;
	}

	public void setType_pos(String type_pos) {
		this.type_pos = type_pos;
	}

}