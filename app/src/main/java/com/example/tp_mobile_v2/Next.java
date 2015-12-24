package com.example.tp_mobile_v2;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;

import com.example.tp_mobile_v2.AsynTask.Parameter_AsyncTask;
import com.example.tp_mobile_v2.Object.Report;

public class Next extends Activity {

//	public final String TAG = Next.class.getSimpleName();
//	float initialX, initialY;
	
	ArrayList<Exception> list_excep = new ArrayList<>();
//	ArrayList<String> arr_err;
	public String IP = "";
	public Report report;
	public Report old_report;

	Parameter_AsyncTask mytt ;
//	Button btnNext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_next);
		
	        // or if you have already created a Gesture Detector.
	        //   myGestureListener = new MyGestureListener(this, getExistingGestureDetector());


	        // Example of setting listener. The onTouchEvent will now be called on your listener
//	        ((ListView)findViewById(R.id.list)).setOnTouchListener(myGestureListener);

	        
		Bundle extras = getIntent().getExtras();
		if(extras !=null) {
		    IP = extras.getString("IP");
		}
		report = new Report();
		try {
			Bundle b = getIntent().getExtras();
			report = (Report) b.getSerializable("info_report");
			old_report = (Report) b.getSerializable("old_report");
		} catch (Exception e) {
			list_excep.add(e);
			e.printStackTrace();
		}
		
		getDataPara(report);
//
//		Button btnNext = (Button) findViewById(R.id.btnNext);
//		btnNext.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				Log.d("Sixe list: ", report.)
//				arr_err = new ArrayList<>();
//				if(report.getList().isEmpty()) {
//					String tmp = "Không lấy được dữ liệu. Vui lòng kiểm tra cơ sở dữ liệu lại!";
//					arr_err.add(tmp);
//					showNotice(arr_err);
//				} else {
//
////					Intent show_parameter = new Intent(getApplicationContext(),ShowParameter.class);
////
////					Bundle b = new Bundle();
////
////					b.putSerializable("info_report", report);
////					show_parameter.putExtras(b);
////					show_parameter.putExtra("IP", IP);
////					startActivity(show_parameter);
//				}
//
//			}
//		});
		for (Exception e : list_excep) {
		      // Do whatever you want for the exception here
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(e.getMessage());
			AlertDialog alert = builder.create();
			alert.show();
		}
	}
	
	public void getDataPara(Report in_report) {
		mytt = new Parameter_AsyncTask(this, IP, in_report, old_report);
		mytt.execute();
		report = new Report(mytt.report);
	}

	 

}
