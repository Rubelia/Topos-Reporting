package com.example.tp_mobile_v2.ArrayAdapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tp_mobile_v2.Object.Contact;
import com.example.tp_mobile_v2.R;
import com.example.tp_mobile_v2.ViewHolder;

import java.util.ArrayList;

/**
 * Created by LapTrinhMobile on 12/14/2015.
 */
public class Contact_ArrayAdapter extends ArrayAdapter<Contact> {
    Activity context = null;
    ArrayList<Contact> myArray = null;
    int layoutId;
    public ViewHolder holder;

    public Contact_ArrayAdapter(Activity context, int layoutId, ArrayList<Contact> arr){
        super(context, layoutId, arr);
        this.context = context;
        this.layoutId = layoutId;
        this.myArray = arr;
    }

    public View getView( final int position, View convertView, ViewGroup parent){

        if(convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(layoutId, null);

            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.txtViewNameContact);
            holder.txtDes = (TextView) convertView.findViewById(R.id.txtViewPhoneNumber);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            holder.item_contact = (LinearLayout) convertView.findViewById(R.id.item_contact);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        convertView.setClickable(true);
//        convertView.setFocusable(true);
        final Contact emp = myArray.get(position);
        holder.txtName.setText(emp.getName() + "   :");
        holder.txtDes.setText(emp.getPhoneNumber());
        return convertView;
    }
}