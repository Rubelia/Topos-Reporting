package com.example.tp_mobile_v2.ArrayAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tp_mobile_v2.Object.Item;
import com.example.tp_mobile_v2.R;

public class Alarm_MyArrayAdapter extends ArrayAdapter<Item> {
    Activity context = null;
    ArrayList<Item> myArray = null;
    int layoutId;

    public Alarm_MyArrayAdapter(Activity context, int layoutId, ArrayList<Item> arr){
        super(context, layoutId, arr);
        this.context = context;
        this.layoutId = layoutId;
        this.myArray = arr;
    }

    public View getView( int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(layoutId, null);

        final Item emp = myArray.get(position);

        final TextView txtdisplayName =(TextView)
                convertView.findViewById(R.id.txtNganh);

        txtdisplayName.setText(emp.toName());

        final TextView txtdisplayValue =(TextView)
                convertView.findViewById(R.id.txtTien);

        String number = emp.getValue();
        double amount = Double.parseDouble(number);
        DecimalFormat formatter = new DecimalFormat("#,###");
        String formatted = formatter.format(amount);
        txtdisplayValue.setText(formatted);



//			System.out.println("getView " + position + " " + convertView);

        return convertView;
    }
}