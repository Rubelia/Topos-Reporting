package com.example.tp_mobile_v2.ArrayAdapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tp_mobile_v2.Object.Report;
import com.example.tp_mobile_v2.R;


public class DSReport_ArrayAdapter extends ArrayAdapter<Report> {
	Activity context = null;
	ArrayList<Report> myArray = null;
	int layoutId;
	
	public DSReport_ArrayAdapter(Activity context, int layoutId, ArrayList<Report> arr){
		super(context, layoutId, arr);
		this.context = context;
		this.layoutId = layoutId;
		this.myArray = arr;
	}
	
	public View getView( int position, View convertView, ViewGroup parent){

			LayoutInflater inflater = context.getLayoutInflater();
			convertView = inflater.inflate(layoutId, null);


			final Report emp = myArray.get(position);
		
			
			final TextView txtdisplayId =(TextView)
			convertView.findViewById(R.id.txtIdReport);
			
			txtdisplayId.setText(emp.getId());
			
			final TextView txtdisplayName =(TextView)
			convertView.findViewById(R.id.txtNameReport);
			
			txtdisplayName.setText(emp.getName());
			
//			System.out.println("Position " + position + "ReportID" + emp.getId()  + convertView);
		
		return convertView;
	}
}