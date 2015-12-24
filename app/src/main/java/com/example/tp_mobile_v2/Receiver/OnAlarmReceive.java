package com.example.tp_mobile_v2.Receiver;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tp_mobile_v2.AsynTask.Alarm_Detail_Report_AsyncTask;
import com.example.tp_mobile_v2.AsynTask.Detail_Report_AsynTask;
import com.example.tp_mobile_v2.Next;
import com.example.tp_mobile_v2.Object.Parameter;
import com.example.tp_mobile_v2.Object.Report;
import com.example.tp_mobile_v2.Other.Timer;
import com.example.tp_mobile_v2.R;
import com.example.tp_mobile_v2.ShowAlarm;
import com.example.tp_mobile_v2.Show_Detail_Report;
import com.example.tp_mobile_v2.database.SQLController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by LapTrinhMobile on 12/8/2015.
 */
public class OnAlarmReceive extends BroadcastReceiver {

    private static final String LOCATION = "";
    SQLController sqlController;
    Report report;
    Report old_report;
    Context myContext;
    String IP, title;
    Timer timer;

    //
//    private NotificationManager manager;
    private NotificationManager mNotificationManager;
//    private NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.d(Globals.TAG, "BroadcastReceiver, in onReceive:");
        myContext = context;
        sqlController = new SQLController(context);
        Toast.makeText(context,"Chạy báo cáo",Toast.LENGTH_SHORT).show();
        // Start the MainActivity
//        Intent a = new Intent(context, ShowAlarm.class);
//        a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Bundle v = new Bundle();
//        a.putExtras(v);
//        context.startActivity(a);
//        report = new Report();
        timer = new Timer();
//        c = Calendar.getInstance();
//        date = c.get(Calendar.DAY_OF_WEEK);
//        if(date == Calendar.THURSDAY)
//            Log.d(LOCATION,"Thứ 5");
        Bundle extras = intent.getExtras();
        if(extras != null) {
            report = (Report) extras.getSerializable("Report");
            old_report = (Report) extras.getSerializable("old_report");
            IP = extras.getString("IP");
            title = extras.getString("Title");
        } else Toast.makeText(context,"Không có Intent",Toast.LENGTH_SHORT).show();
        assert title != null;
        if(title.equalsIgnoreCase("Alarm")) {
            Parameter para_report = new Parameter(old_report.getSql());
            if(para_report.isHasDate()) {
                String tmp = old_report.getSql().replace("{thangnam}",timer.getString());
                Log.d(LOCATION,"SQL " + tmp);
                old_report.setSql(tmp);
                if(old_report.getMaBC().equalsIgnoreCase("null")) {
                    notice(old_report);
                } else {
                    setParameter(old_report);
                }
            } else {
                if(old_report.getMaBC().equalsIgnoreCase("null")) {
                    notice(old_report);
                } else {
                    setParameter(old_report);
                }
            }
        } else {
            if(!old_report.getSql().equalsIgnoreCase("")) {
                String dates = "";
//            String dates = "Friday,Tuesday,Wednesday";
                try {
                    sqlController.open();
                    dates = sqlController.getDates(old_report.getId());
                    sqlController.close();
                } catch (SQLException ex) {
                    Log.d(LOCATION,ex.getMessage());
                }
                ArrayList<String> arr = arrDateFromDates(dates);
//                for(int i=0;i<arr.size();i++) {
//                    Log.d(LOCATION,arr.get(i));
//                }
                if(checkDate(timer.getDate(),arr)) {
                    Intent i = new Intent(context, Show_Detail_Report.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Bundle b = new Bundle();
                    b.putString("IP",IP);
                    b.putSerializable("info_report", old_report);
                    i.putExtras(b);
                    try {
                        SmsManager smsManager = SmsManager.getDefault();
//                    smsManager.sendTextMessage("01226838234", null, "Test app", null, null);
                        Toast.makeText(context, "SMS sent.", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    context.startActivity(i);
                } else Toast.makeText(context,"Not today",Toast.LENGTH_SHORT).show();
            } else Toast.makeText(context,"Không có báo cáo",Toast.LENGTH_SHORT).show();
        }

    }
    public ArrayList<String> arrDateFromDates(String dates) {
        ArrayList<String> result = new ArrayList<>();
        int start = 0;
        for(int i=0;i<dates.length();i++) {
            if(dates.charAt(i) == ',') {
                String temp = dates.substring(start,i);
                result.add(temp);
                start = i+1;
            } else if(i == dates.length()-1) {
                String temp = dates.substring(start,i+1);
                result.add(temp);
            }
        }
        return result;
    }
    public boolean checkDate(int in_dates, ArrayList<String> in_arr) {
        switch (in_dates) {
            case Calendar.MONDAY:
                for(int i=0;i<in_arr.size();i++) {
                    if(in_arr.get(i).equalsIgnoreCase("Monday")) return true;
                }
            case Calendar.TUESDAY:
                for(int i=0;i<in_arr.size();i++) {
                    if(in_arr.get(i).equalsIgnoreCase("Tuesday")) return true;
                }
            case Calendar.WEDNESDAY:
                for(int i=0;i<in_arr.size();i++) {
                    if(in_arr.get(i).equalsIgnoreCase("Wednesday")) return true;
                }
                break;
            case Calendar.THURSDAY:
                for(int i=0;i<in_arr.size();i++) {
                    if(in_arr.get(i).equalsIgnoreCase("Thursday")) return true;
                }
                break;
            case Calendar.FRIDAY:
                for(int i=0;i<in_arr.size();i++) {
                    if(in_arr.get(i).equalsIgnoreCase("Friday")) return true;
                }
            case Calendar.SATURDAY:
                for(int i=0;i<in_arr.size();i++) {
                    if(in_arr.get(i).equalsIgnoreCase("Saturday")) return true;
                }
            case Calendar.SUNDAY:
                for(int i=0;i<in_arr.size();i++) {
                    if(in_arr.get(i).equalsIgnoreCase("Sunday")) return true;
                }
            default:
                break;
        }
        return false;
    }
    public void notice(Report in) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(myContext);
        mBuilder.setContentTitle("Tên báo cáo: "+in.getName());
        mBuilder.setContentText("Đã chạy xong báo cáo");
        mBuilder.setTicker("Bạn có thông báo!");
        mBuilder.setSmallIcon(R.drawable.topos_reporting_small);
        Intent resultIntent = new Intent(myContext, Show_Detail_Report.class);
        Bundle b = new Bundle();
        b.putSerializable("info_report", in);
        b.putSerializable("old_report", old_report);
        resultIntent.putExtras(b);
        resultIntent.putExtra("IP", IP);
        TaskStackBuilder stackBuilder = null;
        PendingIntent resultPendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            stackBuilder = TaskStackBuilder.create(myContext);
            stackBuilder.addParentStack(Show_Detail_Report.class);
            stackBuilder.addNextIntent(resultIntent);
            resultPendingIntent = stackBuilder.getPendingIntent(
                    0, PendingIntent.FLAG_UPDATE_CURRENT );
        }
        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager =(NotificationManager) myContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder.setAutoCancel(true);
        mNotificationManager.notify(Integer.valueOf(in.getId()), mBuilder.build());
    }

    public void setParameter(Report in) {
        Log.d(LOCATION, in.getSql());
        String sql = in.getSql();
        ArrayList<String> arr_Para = new ArrayList<>();
        for(int i=0;i<sql.length();i++) {
            if(sql.charAt(i) == '@') {
                for(int j=i;j<sql.length();j++) {
                    if(sql.charAt(j) == '\'') {
                        String temp = sql.substring(i,j);
                        j = sql.length();
                        arr_Para.add(temp);
                    }
                }
            }
        }
        String date = timer.getStrDate();
        for(int i=0;i<arr_Para.size();i++) {
            sql = sql.replace(arr_Para.get(i),date);
//            Log.d(LOCATION,sql);
        }
        in.setSql(sql);
        notice(in);

    }
}
