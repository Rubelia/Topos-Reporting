package com.example.tp_mobile_v2;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.provider.ContactsContract;


import com.example.tp_mobile_v2.ArrayAdapter.Contact_ArrayAdapter;
import com.example.tp_mobile_v2.Object.Alarm;
import com.example.tp_mobile_v2.Object.Contact;
import com.example.tp_mobile_v2.Object.Report;
import com.example.tp_mobile_v2.Other.Timer;
import com.example.tp_mobile_v2.Receiver.OnAlarmReceive;
import com.example.tp_mobile_v2.database.SQLController;

import java.util.ArrayList;
import java.util.Calendar;


public class SetAlarmToRunReport extends Activity {

    Activity activity = this;
    private static final String LOCATION = "SetAlarmToRunReport";
    Report report;
    Report old_report;
    TextView txtViewAlarm, txtViewRepeat,txtViewPhoneList;
    Button btnSave, btnExit, btnClear;
    Timer timer;
    LinearLayout linearSetAlarm;
    SQLController sqlController;
    String IP;
    Alarm alarm;
    ArrayList<Contact> arr_Contac ;

    int timeSetAlarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm_to_run_report);

        arr_Contac = new ArrayList<>();
        alarm = new Alarm();
        sqlController = new SQLController(getApplicationContext());
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            IP = extras.getString("IP");
//            user_name = extras.getString("inputUserName");
            report = (Report) extras.getSerializable("Report");
            old_report = (Report) extras.getSerializable("old_report");
//            Log.d("SetAlarm",report.getSql());
//            Log.d("SetAlarm",old_report.getSql());
        }
        assert report != null;
        alarm.setReport_ID(report.getId());
        timer = new Timer();

        final RadioButton radioBtnAlarm = (RadioButton) findViewById(R.id.radioBtnAlarm);
        final RadioButton radioBtnSendSMS = (RadioButton) findViewById(R.id.radioBtnSendSMS);

        radioBtnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!radioBtnAlarm.isChecked()) {
                    radioBtnAlarm.setChecked(true);
                    radioBtnSendSMS.setChecked(false);
                } else {

                }
            }
        });

        radioBtnSendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!radioBtnSendSMS.isChecked()) {
                    radioBtnSendSMS.setChecked(true);
                    radioBtnAlarm.setChecked(false);
                } else {

                }
            }
        });
        linearSetAlarm = (LinearLayout) findViewById(R.id.linearSetAlarm);
        txtViewAlarm = (TextView) findViewById(R.id.txtViewAlarm);
        txtViewRepeat = (TextView) findViewById(R.id.txtViewRepeat);
        txtViewPhoneList = (TextView) findViewById(R.id.txtViewPhoneList);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioBtnAlarm.isChecked()) {
                    Timer temp = new Timer();
                    int timeSet ;
                    int timeDefault = 23*3600 + 30*60;
                    int timeNow = temp.getTimeNowAsSecond();
                    if(timeNow > timeDefault) {
                        timeSet = timeNow - timeDefault;
                    } else timeSet = 86400 + timeDefault - timeNow;
                    Toast.makeText(getApplicationContext(),"Giờ hiện tại:" + timeNow, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),"Giờ cài đặt:" + timeDefault, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),"Giờ chạy:" + timeSet, Toast.LENGTH_SHORT).show();
                    setupAlarm(timeSet, report, "Alarm");
                    setupAlarm(3,report,"Alarm");
                } else {
                    try {
                        sqlController.open();
                        sqlController.insertAlarm(alarm);
                        sqlController.close();
                    } catch (SQLException sqlE) {
                        Log.d(LOCATION,sqlE.getMessage());
                    }
                    timer = new Timer();

                    setupAlarm(3,report,"SendSMS");
                }

            }
        });
        btnExit = (Button) findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timer timers = new Timer();
                alarm = new Alarm();
                txtViewAlarm.setText("" + timers.getHour() + ":" + timers.getMinute());
                txtViewRepeat.setText("Không bao giờ");
                txtViewPhoneList.setText("Chưa chọn số");
            }
        });
        txtViewAlarm.setText("" + timer.get24Hour() + ":" + timer.getMinute());
        timeSetAlarm = timer.get24Hour() * 3600 + timer.getMinute() * 60 + timer.getSecond();
        alarm.setHours(timer.get24Hour());
        alarm.setMinutes(timer.getMinute());
        alarm.setDates("");
        linearSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Chức năng đang thực hiện", Toast.LENGTH_SHORT).show();
                showDialogSetAlarm();
            }
        });
        ImageButton imgBtnChooseDate = (ImageButton) findViewById(R.id.imgBtnChooseDate);
        imgBtnChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChooseDate();
            }
        });

        LinearLayout linearChoosePhone = (LinearLayout) findViewById(R.id.linearChoosePhone);
        linearChoosePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arr_Contac = new ArrayList<Contact>();
                ContentResolver cr = getContentResolver();
                Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                        null, null, null, null);
                assert cur != null;
                if (cur.getCount() > 0) {
                    while (cur.moveToNext()) {
                        Contact temp = new Contact();
                        String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        temp.setId(id);
                        temp.setName(name);
                        if (Integer.parseInt(cur.getString(
                                cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                            Cursor pCur = cr.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                                    new String[]{id}, null);
                            assert pCur != null;
                            while (pCur.moveToNext()) {
                                String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                temp.setPhoneNumber(phoneNo);
                                arr_Contac.add(temp);
                            }
                            pCur.close();
                        }
                    }
                }
                Dialog dialog = showListContactDialog();
                dialog.show();
            }
        });
    }


    private Dialog showListContactDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.show_list_contac_phone_number);
        dialog.setTitle("Chọn số điện thoại");
        dialog.setCancelable(false);

        Button btnExit = (Button) dialog.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button btnSave = (Button) dialog.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Đang thực hiện", Toast.LENGTH_SHORT).show();
                String listPhone = "";
                for(int i=0;i<arr_Contac.size();i++) {
                    if(i == arr_Contac.size()-1 && arr_Contac.get(i).isCheck()) {
                        listPhone += arr_Contac.get(i).getPhoneNumber() ;
                    } else if( arr_Contac.get(i).isCheck()) listPhone += arr_Contac.get(i).getPhoneNumber() + ",";
                }
                txtViewPhoneList.setText(listPhone);
                alarm.setListPhoneFromDatabase(listPhone);
                dialog.dismiss();
            }
        });

        ListView list = (ListView) dialog.findViewById(R.id.list);
        list.setItemsCanFocus(true);
        Contact_ArrayAdapter adapter = new Contact_ArrayAdapter(this,R.layout.item_contact,arr_Contac);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final ViewHolder holder = (ViewHolder) view.getTag();
                holder.item_contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(LOCATION, position + "");
                        if (holder.checkBox.isChecked()) {
                            arr_Contac.get(position).setIsCheck(false);
                            holder.checkBox.setChecked(false);
                        } else {
                            arr_Contac.get(position).setIsCheck(true);
                            holder.checkBox.setChecked(true);
                        }
                    }
                });
            }
        });
        return dialog;
    }

    private void showDialogSetAlarm() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.set_alarm);
        dialog.setCancelable(false);

        final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
        TextView txtTime = (TextView) dialog.findViewById(R.id.txtTime);
        txtTime.setText(timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute());
        Button btnExit = (Button) dialog.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button btnSave = (Button) dialog.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String is24  = "";
//                if(timePicker.is24HourView())
//                    is24 = "PM";
//                else is24 = "AM";
                txtViewAlarm.setText(timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute() +" " + is24);
                timeSetAlarm = timePicker.getCurrentHour()*3600 + timePicker.getCurrentMinute() *60 ;
                alarm.setHours(timePicker.getCurrentHour());
                alarm.setMinutes(timePicker.getCurrentMinute());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showDialogChooseDate() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.choose_date);
        dialog.setTitle("Chọn ngày trong tuần");
        dialog.setCancelable(false);
        final ArrayList<String> arr = new ArrayList<>();

        final CheckBox checkBoxMonday = (CheckBox) dialog.findViewById(R.id.checkBoxMonday);
        final CheckBox checkBoxTuesday = (CheckBox) dialog.findViewById(R.id.checkBoxTuesday);
        final CheckBox checkBoxWednesday = (CheckBox) dialog.findViewById(R.id.checkBoxWednesday);
        final CheckBox checkBoxThursday = (CheckBox) dialog.findViewById(R.id.checkBoxThursday);
        final CheckBox checkBoxFriday = (CheckBox) dialog.findViewById(R.id.checkBoxFriday);
        final CheckBox checkBoxSaturday = (CheckBox) dialog.findViewById(R.id.checkBoxSaturday);
        final CheckBox checkBoxSunday = (CheckBox) dialog.findViewById(R.id.checkBoxSunday);

        Button btnExit = (Button) dialog.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button btnSave = (Button) dialog.findViewById(R.id.btnOK);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!arr.isEmpty()) {
                    String date = "";
                    for(int i=0;i<arr.size();i++) {
                        if(i == arr.size()-1) {
                            date += arr.get(i) ;
                        } else date += arr.get(i) + ",";
                    }
                    alarm.setDates(date);
                }
                dialog.dismiss();
            }
        });
        View.OnClickListener clickBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                switch (v.getId() /*to get clicked view id**/) {
                    case R.id.linearMonday:
                        if(!checkBoxMonday.isChecked()) {
                            arr.add("Monday");
                        } else {
                            arr.remove("Monday");
                        }
                        checkBoxMonday.setChecked(!checkBoxMonday.isChecked());
                        break;
                    case R.id.linearTuesday:
                        if(!checkBoxTuesday.isChecked()) {
                            arr.add("Tuesday");
                        } else {
                            arr.remove("Tuesday");
                        }
                        checkBoxTuesday.setChecked(!checkBoxTuesday.isChecked());
                        break;
                    case R.id.linearWednesday:
                        if(!checkBoxWednesday.isChecked()) {
                            arr.add("Wednesday");
                        } else {
                            arr.remove("Wednesday");
                        }
                        checkBoxWednesday.setChecked(!checkBoxWednesday.isChecked());
                        break;
                    case R.id.linearThursday:
                        if(!checkBoxThursday.isChecked()) {
                            arr.add("Thursday");
                        } else {
                            arr.remove("Thursday");
                        }
                        checkBoxThursday.setChecked(!checkBoxThursday.isChecked());
                        break;
                    case R.id.linearFriday:
                        if(!checkBoxFriday.isChecked()) {
                            arr.add("Friday");
                        } else {
                            arr.remove("Friday");
                        }
                        checkBoxFriday.setChecked(!checkBoxFriday.isChecked());
                        break;
                    case R.id.linearSaturday:
                        if(!checkBoxSaturday.isChecked()) {
                            arr.add("Saturday");
                        } else {
                            arr.remove("Saturday");
                        }
                        checkBoxSaturday.setChecked(!checkBoxSaturday.isChecked());
                        break;
                    case R.id.linearSunday:
                        if(!checkBoxSunday.isChecked()) {
                            arr.add("Sunday");
                        } else {
                            arr.remove("Sunday");
                        }
                        checkBoxSunday.setChecked(!checkBoxSunday.isChecked());
                        break;
                    default:
                        break;
                }
            }
        };
        LinearLayout linearMonday = (LinearLayout) dialog.findViewById(R.id.linearMonday);
        LinearLayout linearTuesday = (LinearLayout) dialog.findViewById(R.id.linearTuesday);
        LinearLayout linearWednesday = (LinearLayout) dialog.findViewById(R.id.linearWednesday);
        LinearLayout linearThursday = (LinearLayout) dialog.findViewById(R.id.linearThursday);
        LinearLayout linearFriday = (LinearLayout) dialog.findViewById(R.id.linearFriday);
        LinearLayout linearSaturday = (LinearLayout) dialog.findViewById(R.id.linearSaturday);
        LinearLayout linearSunday = (LinearLayout) dialog.findViewById(R.id.linearSunday);
        linearMonday.setOnClickListener(clickBtn); linearTuesday.setOnClickListener(clickBtn); linearWednesday.setOnClickListener(clickBtn);
        linearThursday.setOnClickListener(clickBtn); linearFriday.setOnClickListener(clickBtn); linearSaturday.setOnClickListener(clickBtn);
        linearSunday.setOnClickListener(clickBtn);

        dialog.show();
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
                SetAlarmToRunReport.this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        // Getting current time and add the seconds in it
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, seconds);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        // Finish the currently running activity
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();

        if(!arr_Contac.isEmpty()) {

        }

    }
}
