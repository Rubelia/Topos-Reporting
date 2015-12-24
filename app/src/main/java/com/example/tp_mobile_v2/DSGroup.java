package com.example.tp_mobile_v2;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tp_mobile_v2.AsynTask.DSGroup_AsyncTask;
import com.example.tp_mobile_v2.Object.Group;
import com.example.tp_mobile_v2.database.SQLController;

public class DSGroup extends Activity {

	public final static int MENU_SETTING = 1;
	public final static int MENU_EXIT = 2;
	public final static int MENU_LANGUAGE = 3;
	public final static int MENU_ABOUT = 4;
	public final static int MENU_RESET = 5;
	
	static final int SETTING_DIALOG_ID = 0;
	static final int ABOUT_DIALOG_ID = 1;	
	static final int SETTING_DIALOG_PASSWORD = 3;
	static final int SET_ALARM_TO_RUN_REPORT = 4;

	private static final int PASS_NEW_ID = 1;
	private static final int REPEAT_PASS_NEW_ID = 2;
	private static final int IP_OLD_ID = 1;
	private static final int IP_NEW_CLEAR = 4;

	private static final String LOCATION = "DSGroup";
	SQLController sqlController;
	String username;
	String IP = "";
	DSGroup_AsyncTask mytt;
	ArrayList<Group> arrGroup = new ArrayList<>();
	ArrayList<Exception> list_excep = new ArrayList<>();
	ArrayList<String> arr_err;
	Calendar c;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dsgroup);

//		String user = new String();
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			username = extras.getString("inputUserName");
			IP = extras.getString("IP");
			Log.d(LOCATION, IP);
		}


		sqlController = new SQLController(getApplicationContext());
		TextView mTitleTextView = (TextView) findViewById(R.id.txt_title);
		mTitleTextView.setText("Danh sách nhóm báo cáo");
		mTitleTextView.setTextSize(14);

		ImageButton imageButton = (ImageButton) findViewById(R.id.btnSetting);
		imageButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
//				Toast.makeText(getApplicationContext(), "Chức năng chưa hoàn thiện", Toast.LENGTH_SHORT).show();
				openOptionsMenu();
			}
		});
		getListGroup();
		
		ListView list_group = (ListView) findViewById(R.id.ds_group);
		list_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
                Group group = arrGroup.get(position);
//                ArrayList<Report> send_arrayReport = group.getArrayReport();
				Intent list_report_view = new Intent (getApplicationContext(),DSReport.class);
//				Bundle b = new Bundle();
//				b.putSerializable("list_report", send_arrayReport);
//				group.set
				String id_group = group.getId();
				String group_name = group.getName();
				System.out.println(id_group);
				list_report_view.putExtra("Id_Group",id_group);
				list_report_view.putExtra("Group", group_name);
				list_report_view.putExtra("IP", IP);
				
				startActivity(list_report_view);
			}
		});
		for (Exception e : list_excep) {
		      // Do whatever you want for the exception here
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(e.getMessage());
			AlertDialog alert = builder.create();
			alert.show();
		}
	}
	
	public void getListGroup() {
		mytt = new DSGroup_AsyncTask(this, IP);
		mytt.execute();
		arrGroup = mytt.arrGroup;
	}
	
	@Override
    protected Dialog onCreateDialog(int id){
    	switch(id){
			case SETTING_DIALOG_PASSWORD:
				Toast.makeText(getApplicationContext(),"Chức năng đang thực hiện!",Toast.LENGTH_SHORT).show();
				return null;
			case SETTING_DIALOG_ID:
				return createSettingDialog();
			case ABOUT_DIALOG_ID:
				return createAboutDialog();
			case SET_ALARM_TO_RUN_REPORT:
				return createSetAlarmDialog();
		default:
			return null;
    	}
    }

	private Dialog createSetAlarmDialog() {
		final Dialog dialog = new Dialog(DSGroup.this);
		dialog.setContentView(R.layout.set_alarm);
		dialog.setTitle("Cài giờ chạy báo cáo");
		dialog.setCancelable(true);

		return dialog;
	}

	@Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
	        case SETTING_DIALOG_PASSWORD:
	            // Clear the input box. 	
	            EditText text1 = (EditText) dialog.findViewById(PASS_NEW_ID);
	        	text1.setText("");
	        	EditText clear_ip1 = (EditText) dialog.findViewById(REPEAT_PASS_NEW_ID);
	        	clear_ip1.setText("");
	            break;
            case SETTING_DIALOG_ID:
                // Clear the input box.
				sqlController.open();
				IP = sqlController.getStrIP();
                EditText text = (EditText) dialog.findViewById(IP_OLD_ID);
            	text.setText(IP);
            	EditText clear_ip = (EditText) dialog.findViewById(IP_NEW_CLEAR);
            	clear_ip.setText("");
                break;
            case ABOUT_DIALOG_ID:
            	TextView about = (TextView) dialog.findViewById(R.id.about);
            	about.setText("\t"+"Công ty: Thinh Phat JSC" +
            	"\n"+
            			"\t"+"Phiên bản: 2.0");
            	break;
            default:
            	break;
        }
    }

    private Dialog createResetPassDialog(String in_user){
    	final Dialog dialog = new Dialog(this);
    	final String name = in_user;
    	dialog.setContentView(R.layout.reset_password);
    	dialog.setTitle("Tạo mới mật khẩu");
    	dialog.setCancelable(false);
    	final Button ok = (Button) dialog.findViewById(R.id.btnSave);
        final Button cancel = (Button) dialog.findViewById(R.id.btnExit);
        final EditText editNewPass = (EditText) dialog.findViewById(R.id.editNewPass);
        final EditText editRepeatPass = (EditText) dialog.findViewById(R.id.editRepeat);
        
        editNewPass.setId(PASS_NEW_ID);
        editRepeatPass.setId(REPEAT_PASS_NEW_ID);
        
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { 
            	arr_err = new ArrayList<String>();
            	if(editNewPass.getText().toString().isEmpty()) {
            		String tmp = "Vui lòng điền mật khẩu mới!";
            		arr_err.add(tmp);
            		showNotice(arr_err);
            	} else {
            		if(editRepeatPass.getText().toString().isEmpty()) {
            			String tmp = "Vui lòng nhập lại mật khẩu!";
                		arr_err.add(tmp);
                		showNotice(arr_err);
            		} else {
            			String editNew = editNewPass.getText().toString();
            			String editRepeat = editRepeatPass.getText().toString();
            			if(!editNew.equalsIgnoreCase(editRepeat)) {
            				String tmp = "Mật khẩu không trùng khớp";
            				arr_err.add(tmp);
            				showNotice(arr_err);
            			} else {
            				String tmp = "UPDATE User SET password = '" + editRepeat +"' where username ='"+name +"'";
            				Toast.makeText(getApplicationContext(), "Đã cập nhật lại mật khẩu!",Toast.LENGTH_LONG).show();
            				dialog.dismiss();
            			}
            		}
            	}
              }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    	return dialog;
    }

    private Dialog createSettingDialog(){
    	final Dialog dialog = new Dialog(DSGroup.this);
    	dialog.setContentView(R.layout.setting_view);
    	dialog.setTitle("Cài đặt");
    	dialog.setCancelable(false);

    	final Button ok = (Button) dialog.findViewById(R.id.bt_ok);
        final Button cancel = (Button) dialog.findViewById(R.id.bt_cancel);
        final EditText IPNew = (EditText) dialog.findViewById(R.id.IPNew);

        final EditText ip_old = (EditText) dialog.findViewById(R.id.IP_Old);

        ip_old.setId(IP_OLD_ID);
        IPNew.setId(IP_NEW_CLEAR);

//        KV_New.setVisibility(true);
       ip_old.setFocusable(false);
//       kv_old.setFocusable(false);
//       ch_old.setFocusable(false);

        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
		//xu ly su kien khi nhap dữ liệu vào :3
				arr_err = new ArrayList<String>();
            	String ip_new = IPNew.getText().toString();
//            	String kv_new = KV_New.getText().toString();
//            	String ch_new = CH_New.getText().toString();

            	if (ip_new.length() != 0) {
					sqlController.open();
					try {
						sqlController.updateIP(ip_new);
					} catch (SQLException err) {
						arr_err.add(err.getMessage());
						showNotice(arr_err);
					}
					sqlController.close();
            		Toast.makeText(getApplicationContext(), "Đã cập nhật xong IP",Toast.LENGTH_LONG).show();
                }
//            	if (kv_new.length() != 0)
//            	{
//            		db.execSQL("UPDATE config SET KV ='"+ kv_new +"'");
//            		Toast.makeText(getApplicationContext(), "Đã cập nhật xong KV",Toast.LENGTH_LONG).show();
//                }
//            	if (ch_new.length() != 0)
//            	{
//            		db.execSQL("UPDATE config SET CH ='"+ ch_new +"'");
//            		Toast.makeText(getApplicationContext(), "Đã cập nhật xong CH",Toast.LENGTH_LONG).show();
//                }
            	dialog.dismiss();
              }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    	return dialog;
    }

    private Dialog createAboutDialog(){
    	final Dialog dialog = new Dialog(DSGroup.this);
    	dialog.setContentView(R.layout.about_view);
    	dialog.setTitle("Thông tin");
    	dialog.setCancelable(true);
    	return dialog;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//    	MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu, menu);
        getMenuInflater().inflate(R.menu.main, menu);
//        menu.add(0,MENU_SETTING,0,)
        return true;
    }
    

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        switch (item.getItemId()) {
        	case R.id.menu_setting:
        		showDialog(SETTING_DIALOG_ID);
        		return true;

        	case R.id.menu_reset:
        		showDialog(SETTING_DIALOG_PASSWORD);
//        case R.id.menu_language:
////        	String language = new String();
////            language = "en";
////            Locale locale = new Locale(language);
////            Configuration config = new Configuration();
////            config.locale = locale;
////            getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
//        	Toast.makeText(getApplicationContext(),"Chức năng chưa hoàn thiện!", Toast.LENGTH_LONG).show();
//            return true;
        	return true;
//        case R.id.menu_save:
//        	SaveListViewToPdf();
//        	return true;
//        case R.id.menu_open:
//        	openPdf();
//        	return true;
//        case R.id.menu_share:
//        	try {
//				CreatNewFileInSdCard();
//			} catch (DocumentException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
////        	System.out.println("")
//        	Toast.makeText(getApplicationContext(), "Created...", Toast.LENGTH_LONG).show();
//        	sharePDF();
////        	Toast.makeText(getApplicationContext(), "Created...", Toast.LENGTH_LONG).show();
//            System.out.println("Run to here -->");
//            return true;
        case R.id.menu_about:
        	showDialog(ABOUT_DIALOG_ID);
        	return true;
        case R.id.menu_exit:
        	finish();
        	return true;
        default:
        	return super.onOptionsItemSelected(item);
        }
        
    }
    public void showNotice (ArrayList<String> str) {
		String outStr = "";
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		for(int i=0;i<str.size();i++) {
			outStr += str.get(i);
		}
		builder.setMessage(outStr);
		AlertDialog alert = builder.create();
		alert.show();
	}
}
