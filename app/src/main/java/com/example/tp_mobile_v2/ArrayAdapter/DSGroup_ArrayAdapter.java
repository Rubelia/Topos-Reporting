package com.example.tp_mobile_v2.ArrayAdapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tp_mobile_v2.Object.Group;
import com.example.tp_mobile_v2.R;

public class DSGroup_ArrayAdapter extends ArrayAdapter<Group> {
	Activity context = null;
	ArrayList<Group> myArray = null;
	int layoutId;
	
	public DSGroup_ArrayAdapter(Activity context, int layoutId, ArrayList<Group> arr){
		super(context, layoutId, arr);
		this.context = context;
		this.layoutId = layoutId;
		this.myArray = arr;
	}
	
	public View getView( int position, View convertView, ViewGroup parent){

			LayoutInflater inflater = context.getLayoutInflater();
			convertView = inflater.inflate(layoutId, null);

			final Group emp = myArray.get(position);
			
			final TextView txtdisplayId =(TextView)
			convertView.findViewById(R.id.txtIdReport);
			
			txtdisplayId.setText(emp.getId());
			
			final TextView txtdisplayName =(TextView)
			convertView.findViewById(R.id.txtNameReport);
			
			txtdisplayName.setText(emp.getName());

//			System.out.println("Position " + position + "GroupID" + emp.getId()  + convertView);


//			String number = temp.getValue();
//		    double amount = Double.parseDouble(number);
//			DecimalFormat formatter = new DecimalFormat("#,###");
//		    String formatted = formatter.format(amount);
//			txtdisplayValue.setText(formatted);



		return convertView;
	}
}