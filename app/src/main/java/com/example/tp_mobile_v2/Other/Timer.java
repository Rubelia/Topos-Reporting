package com.example.tp_mobile_v2.Other;

import java.util.Calendar;

/**
 * Created by LapTrinhMobile on 12/7/2015.
 */
public class Timer {
    Calendar c;
    int date;
    int day;
    int month;
    int year;
    int hour;
    int minute;
    int second;
    int milisecond;
//    String state;
    public Timer() {
        c = Calendar.getInstance();
        day  = c.get(Calendar.DATE);
        date = c.get(Calendar.DAY_OF_WEEK);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        second = c.get(Calendar.SECOND);
        milisecond = c.get(Calendar.MILLISECOND);

    }

    public int getTimeNowAsSecond(){
        return get24Hour()*3600 + minute*60 + second;
    }
    public int getDay() { return  day; }
    public int getDate() {
        return date;
    }
    public int getMonth() { return month; }
    public int getYear() { return year; }
    public int getHour() {
        return hour;
    }
    public  int get24Hour() {
        return c.get(Calendar.HOUR_OF_DAY);
    }
    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public int getMilisecond() {
        return milisecond;
    }

    public String getFullTime() {
        String result = day + "-" + month + "-" + year + " " + c.get(Calendar.HOUR_OF_DAY) + "." + minute + ":" + milisecond;
        return  result;
    }

    public String getStrDate() {
        String result = year + "-" + month + "-" + day;
        return result;
    }
    public String getString() {
        String str_month = ""+month;
        String str_date ;
        if(str_month.equalsIgnoreCase("10")| str_month.equalsIgnoreCase("11") | str_month.equalsIgnoreCase("12")) {
            str_date = str_month+""+ year;
        } else {
            str_date = 0+"" + str_month+""+ year;
        }
        return str_date;
    }
}
