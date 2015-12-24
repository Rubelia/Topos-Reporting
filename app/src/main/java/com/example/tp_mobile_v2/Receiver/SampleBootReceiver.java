package com.example.tp_mobile_v2.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by LapTrinhMobile on 12/14/2015.
 */
public class SampleBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
        }
    }
//    http://developer.android.com/training/scheduling/alarms.html
//    http://developer.android.com/reference/android/app/AlarmManager.html
}
