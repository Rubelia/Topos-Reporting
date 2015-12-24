package com.example.tp_mobile_v2;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.example.tp_mobile_v2.Object.Report;
import com.example.tp_mobile_v2.Receiver.OnAlarmReceive;

import java.util.Calendar;

public class ShowAlarm extends Activity {

    Report old_report;
    String IP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_alarm);
    }

    private void setupAlarm(int seconds, Report report, String title) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getBaseContext(), OnAlarmReceive.class);
        Bundle b = new Bundle();
        b.putString("Title",title);
        b.putString("IP", IP);
        b.putSerializable("Report", report);
        b.putSerializable("old_report", old_report);
        intent.putExtras(b);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                ShowAlarm.this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        // Getting current time and add the seconds in it
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, seconds);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        // Finish the currently running activity
        finish();
    }
}
