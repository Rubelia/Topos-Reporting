package com.example.tp_mobile_v2.Object;

import android.database.Cursor;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by LapTrinhMobile on 12/10/2015.
 */
public class Alarm implements Serializable{
    String Report_ID;
    String date;
    int hour;
    int minutes;
    ArrayList<String> arr_phone;
    public Alarm() {

        arr_phone = new ArrayList<>();
        arr_phone.add("");
    }

    public Alarm(Cursor in) {
        Report_ID = in.getString(1);
        date = in.getString(2);
        hour = in.getInt(3);
        minutes = in.getInt(4);
        setListPhoneFromDatabase(in.getString(5));
    }
    public int getHours() {
        return hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public String getDates() {
        return date;
    }

    public String getReport_ID() {
        return Report_ID;
    }

    public void setDates(String date) {
        this.date = date;
    }

    public void setHours(int hour) {
        this.hour = hour;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setReport_ID(String report_ID) {
        Report_ID = report_ID;
    }

    public void add(String phoneNum) {
        arr_phone.add(phoneNum);
    }

    public void setListPhoneFromDatabase(String in) {
        int start = 0;
        for(int i=0;i<in.length();i++) {
            if(in.charAt(i) == ',') {
                String temp = in.substring(start,i);
                arr_phone.add(temp);
                start = i +1;
            }
        }
        for(int i=0;i<arr_phone.size();i++) {
            Log.d("ArrPhoen", arr_phone.get(i));
        }
    }
    public ArrayList<String> getArr_phone() {
        return arr_phone;
    }

    public String getListPhoneToInsertIntoDatabase() {
        String result = "";
        for(int i=0;i<arr_phone.size();i++) {
            if(i == arr_phone.size()-1)
                result += arr_phone.get(i);
            else result +=  arr_phone.get(i) + ",";
        }
        return result;
    }
}
