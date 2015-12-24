package com.example.tp_mobile_v2;

import java.io.Serializable;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewHolder implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public EditText edit;
	public TextView txtSymbol;
	public TextView txtName;
	public TextView txtDes;
	public TextView txtCaption;
	public ImageView img_pie_chart;
	public CheckBox checkBox;
	public LinearLayout item_contact;
	boolean isChoose;
	int position;
	boolean isAdd;
	public ViewHolder() {
		position = 0;
		edit = null;
		txtDes = null;
		txtCaption = null;
		isChoose = false;
		isAdd = true;
	}
	public void setPosition(int in_pos) {
		this.position = in_pos;
	}

	public void setIsChoose(boolean isChoose) {
		this.isChoose = isChoose;
	}

	public boolean isChoose() {
		return isChoose;
	}

	public boolean isAdd() {
		return isAdd;
	}

	public void setIsAdd(boolean isAdd) {
		this.isAdd = isAdd;
	}
}