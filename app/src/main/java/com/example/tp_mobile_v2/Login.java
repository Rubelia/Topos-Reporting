package com.example.tp_mobile_v2;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tp_mobile_v2.AsynTask.NhanVien_AsyncTask;
import com.example.tp_mobile_v2.Object.NhanVien;
import com.example.tp_mobile_v2.database.SQLController;

public class Login extends Activity {

	private static final String LOCATION = "Login";
	private static final int SIGNUP_DIALOG_PASSWORD = 0;
	private static final int PASS_NEW_ID = 1;
	private static final int REPEAT_PASS_NEW_ID = 2;
	private static final int ENTER_PASSWORD_DIALOG = 3;

	Activity activity = this;
	String IP;
	NhanVien nhanVien;
//	SQLiteDatabase db;
	SQLController sqlController;
	ArrayList<String> arr_err;
	int isSave;
//	Button btnLogin, btnSignUp;
//	EditText user_name, password;
//	CheckBox save_account;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		sqlController = new SQLController(activity);
		isSave = 0;
		//Device Phone
		TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
		String mPhoneNumber = tMgr.getLine1Number();
//		String getSimSerialNumber = tMgr.getSimSerialNumber();
//		Log.d(LOCATION, "Số seriSIMCARD: " +getSimSerialNumber);
		// Device model
		String PhoneModel = android.os.Build.MODEL;

		// Android version
		String AndroidVersion = android.os.Build.VERSION.RELEASE;
		Log.d(LOCATION,"Device model: " +PhoneModel);
		Log.d(LOCATION,"Android version: "+AndroidVersion);

		//get infor
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			IP = extras.getString("IP");
			Log.d(LOCATION,IP);
		}
//		db = openOrCreateDatabase("DBConfig", Login.MODE_PRIVATE, null);
//        db.execSQL("CREATE TABLE IF NOT EXISTS User(ID INTERGER,username VARCHAR,password VARCHAR,saved BIT );");
//        db.execSQL("CREATE TABLE IF NOT EXISTS DashBoard(ID INTERGER,Location INTERGER,IsShow BIT,name_report VARCHAR, SQL_report TEXT,Giatri_report NVARCHAR(50), Mota_report NVARCHAR(50));");

//        arr_err = new ArrayList<>();
		final CheckBox save_account = (CheckBox) findViewById(R.id.checkBox1);
		final EditText user_name = (EditText) findViewById(R.id.editUserName);
//		user_name.setImeActionLabel("Enter", KeyEvent.KEYCODE_ENTER);
		user_name.setOnEditorActionListener(new EditText.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				arr_err = new ArrayList<String>();
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					Intent dsgroup = new Intent (getApplicationContext(),DashBoard.class);
					dsgroup.putExtra("inputUserName", "admin");
					dsgroup.putExtra("IP",IP);
					startActivity(dsgroup);
//					if (user_name.getText().toString().isEmpty()) {
//						String err = "Bạn chưa nhập Tên đăng nhập";
//						arr_err.add(err);
//						showNotice(arr_err);
//					} else {
//						if (save_account.isChecked()) isSave = 1;
//						else isSave = 0;
//						NhanVien_AsyncTask nhanVien_asyncTask = new NhanVien_AsyncTask(activity, user_name.getText().toString(), IP, isSave);
//						nhanVien_asyncTask.execute();
//					}
					return true;
				} else {
					return false;
				}
			}
		});

		String userNameIsSave ;
		sqlController.open();
		try {
			userNameIsSave = sqlController.getUserNameIsSave();
			isSave = sqlController.getIsSave(userNameIsSave);
			user_name.setText(userNameIsSave);
		} catch (SQLException err) {
			arr_err = new ArrayList<>();
			arr_err.add(err.getMessage());
			showNotice(arr_err);
		}
		sqlController.close();
		if(isSave == 1) save_account.setChecked(true);
		else save_account.setChecked(false);
//		if(userNameIsSave.equalsIgnoreCase("")) {
//
//		} else {
//			user_name.setText(userNameIsSave);
//			save_account.setChecked(true);
//			isSave = 1;
//			NhanVien_AsyncTask nhanVien_asyncTask = new NhanVien_AsyncTask(activity,user_name.getText().toString(),IP, isSave);
//			nhanVien_asyncTask.execute();
//		}
		final EditText password = (EditText) findViewById(R.id.editPassword);
		Button btnLogin = (Button) findViewById(R.id.btnLogin);

		btnLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				arr_err = new ArrayList<>();
				if(user_name.getText().toString().isEmpty()) {
					String tmp = "Vui lòng nhập người dùng!";
					arr_err.add(tmp);
					if(password.getText().toString().isEmpty()) {
						tmp = "Vui lòng nhập mật khẩu!";
						arr_err.add(tmp);
						showNotice(arr_err);
					}
					showNotice(arr_err);
				} else {
					String name = user_name.getText().toString();
					String pass = password.getText().toString();
//					Cursor check = db.rawQuery("SELECT * FROM User where username = '" +name+"'", null);
//			    	if(!check.moveToFirst()) {
//			        	db.execSQL("INSERT INTO config VALUES('1','sql3.garco10.com.vn/goldmem_service','KV001','AA')");
//			    		String tmp = "Người dùng chưa tồn tại! Xin kiểm tra lại...";
//			    		arr_err.add(tmp);
//			    		showNotice(arr_err);
//			        } else {
//			    		if(!check.getString(2).equalsIgnoreCase(pass)) {
//			    			String tmp = "Sai mật khẩu";
//			    			arr_err.add(tmp);
//			    			showNotice(arr_err);
//			    		} else {
//			    			if(save_account.isChecked()) {
//			    				Log.d("Page","140");
//			    				Log.d("Username",user_name.getText().toString());
//								sqlController.BeginTransaction();
//								sqlController.open();
//								sqlController.updateUser(user_name.getText().toString());
//			    				db.execSQL("UPDATE 	User SET saved = '0'");
//			    				db.execSQL("UPDATE 	User SET saved = '1' WHERE username = '"+ user_name.getText().toString()+"'");
//			    			} else {
//			    				db.execSQL("UPDATE 	User SET saved = '0'");
//			    			}
//			    			user_name.setText("");
//			    			password.setText("");
//			    			Intent dsgroup = new Intent (getApplication(),DashBoard.class);
//			    			dsgroup.putExtra("inputUserName",name);
//							dsgroup.putExtra("IP", IP);
//			    			startActivity(dsgroup);
//			    		}
//			    	}
				}
			}
		});
	}
	public void showNotice (ArrayList<String> str) {
		String outStr = "";
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		for(int i=0;i<str.size();i++) {
			if(i== str.size()-1) {
				outStr += str.get(i);
			} else outStr += str.get(i) + " /n";
		}
		builder.setMessage(outStr);
		AlertDialog alert = builder.create();
		alert.show();
	}
	protected Dialog onCreateDialog(int id){
    	switch(id){
    		case SIGNUP_DIALOG_PASSWORD:
    			return createSignUpDialog();
			case ENTER_PASSWORD_DIALOG:
				return createEnterPassDialog();
			default:
				return null;
    	}
    }
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case SIGNUP_DIALOG_PASSWORD:
                // Clear the input box. 	
                EditText text = (EditText) dialog.findViewById(PASS_NEW_ID);
            	text.setText("");
            	EditText clear_ip = (EditText) dialog.findViewById(REPEAT_PASS_NEW_ID);
            	clear_ip.setText("");
                break;
			case ENTER_PASSWORD_DIALOG:
				break;
            default:
            	break;
        }
    }
    private Dialog createSignUpDialog(){
		return null;
    }
	private Dialog createEnterPassDialog() {
		Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.enter_password_view);
		dialog.setTitle("Nhập mật khẩu");

		return dialog;
	}
//	@Override
//	public boolean dispatchKeyEvent(KeyEvent e) {
//		if (e.getKeyCode() == KeyEvent.FLAG_EDITOR_ACTION) {
//		/*Just switch out keycode if KEYCODE_FORWARD_DEL if its not the correct one*/
//			Toast.makeText(Login.this, "YOU CLICKED Delete KEY",
//					Toast.LENGTH_LONG).show();
//			return true;
//		}
//		Log.d(LOCATION, "Didnt work");
//		return super.dispatchKeyEvent(e);
//	};
}
