package com.example.tp_mobile_v2.ArrayAdapter;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tp_mobile_v2.Object.ArrStr_Para;
import com.example.tp_mobile_v2.R;
import com.example.tp_mobile_v2.ViewHolder;


public class ArrStr_Para_ArrayAdapter extends ArrayAdapter<ArrStr_Para> {
	Activity context = null;
	public ArrayList<ArrStr_Para> myArray = null;
	int layoutId;
	boolean isClick = false;
	
	public ArrStr_Para_ArrayAdapter(Activity contextCha, int layoutId, ArrayList<ArrStr_Para> arr){
		super(contextCha, layoutId, arr);
		this.context = contextCha;
		this.layoutId = layoutId;
		this.myArray = arr;
	}
	public View getView( int position, View convertView, ViewGroup parent){
		ViewHolder holder;
		if(convertView == null)
		{
			LayoutInflater inflater = context.getLayoutInflater();
			convertView = inflater.inflate(layoutId, null);
			holder = new ViewHolder();
			holder.txtSymbol = (TextView) convertView.findViewById(R.id.txtSymbol);
			holder.txtName = (TextView) convertView.findViewById(R.id.txtArrPara);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		

		final ArrStr_Para emp = myArray.get(position);
		
		Typeface face;
		face = Typeface.createFromAsset(context.getAssets(), "Symbola.otf");
		
		String ballotBox = "&#9744;";
		Spanned spanBallotBox = Html.fromHtml(ballotBox);
		holder.txtSymbol.setTypeface(face);
		holder.txtSymbol.setText(spanBallotBox) ;
		
		holder.txtName.setText(emp.getValues1()+ " - " + emp.getValues2());
		return convertView;
	}
}