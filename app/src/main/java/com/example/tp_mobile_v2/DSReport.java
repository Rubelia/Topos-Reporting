package com.example.tp_mobile_v2;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tp_mobile_v2.AsynTask.DSReport_AsyncTask;
import com.example.tp_mobile_v2.AsynTask.Parameter_AsyncTask;
import com.example.tp_mobile_v2.Object.Parameter;
import com.example.tp_mobile_v2.Object.Report;

public class DSReport extends Activity {
	String IP = "";
	String Id_Group = "";
	String Group_name = "";
	boolean isOkClicked = false;
	boolean getDataParaDone = false;

	DSReport_AsyncTask mytt;
	Parameter_AsyncTask para_asyntask;
	
	ArrayList<Exception> list_excep = new ArrayList<>();
	ArrayList<Report> arrReport;
	Report send_report, temp ;
	Calendar calendar;
	private int year, month, day;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dsreport);

		calendar = Calendar.getInstance();
	    year = calendar.get(Calendar.YEAR);
	    month = calendar.get(Calendar.MONTH);
	    day = calendar.get(Calendar.DAY_OF_MONTH);
	    
		Bundle extras = getIntent().getExtras();
		if(extras !=null) {
		    IP = extras.getString("IP");
		    Id_Group = extras.getString("Id_Group");
		    Group_name = extras.getString("Group");
		}

		TextView mTitleTextView = (TextView) findViewById(R.id.txt_title);
		mTitleTextView.setText(Group_name);
		mTitleTextView.setTextSize(14);

		ImageButton imageButton = (ImageButton) findViewById(R.id.btnSetting);
		imageButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
//				Toast.makeText(getApplicationContext(), "Chức năng chưa hoàn thiện", Toast.LENGTH_SHORT).show();
				openOptionsMenu();
			}
		});
		getInfoReport();
		ListView list_report = (ListView) findViewById(R.id.list_report);
		list_report.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				send_report = new Report ( arrReport.get(position));
				Parameter para_report = new Parameter(send_report.getSql());
				if(para_report.isHasDate()) {
					showDatePickerDialog();
				} else {
					if(send_report.getMaBC().equalsIgnoreCase("null")) {
						showDetailReport(send_report);
					} else {
						GetDataParameter(send_report);
					}
				}
			}
		});
		Button btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
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
	public void showDetailReport(Report report) {
		Intent show_report = new Intent(getApplicationContext(),Show_Detail_Report.class);
		Bundle b = new Bundle();
		b.putSerializable("info_report", report);
		b.putSerializable("old_report", report);
		show_report.putExtras(b);
		show_report.putExtra("IP", IP);
		startActivity(show_report);
	}
	public void showDatePickerDialog() {
		final Calendar c = Calendar.getInstance();

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        
        final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            // when dialog box is closed, below method will be called.
            public void onDateSet(DatePicker view, int selectedYear,
                    int selectedMonth, int selectedDay) {
                    year = selectedYear;
                    month = selectedMonth;
                    day = selectedDay;
            }
        };
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePickerListener, year, month, day);

        datePickerDialog.setTitle("Chỉ chọn tháng, năm");
        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"Hủy",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            dialog.cancel();
                            isOkClicked = false;
                        }
                    }
                });
        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Đồng ý",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            isOkClicked = true;
                            DatePicker datePicker = datePickerDialog.getDatePicker();
                            datePickerListener.onDateSet(datePicker,
                                    datePicker.getYear(),
                                    datePicker.getMonth(),
                                    datePicker.getDayOfMonth());
                            month += 1;
                            String str_month = ""+month;
                            String str_date ;
                            if(str_month.equalsIgnoreCase("10")| str_month.equalsIgnoreCase("11") | str_month.equalsIgnoreCase("12")) {
                            	str_date = str_month+""+ year;
                            } else {
                            	str_date = 0+"" + str_month+""+ year;
                            }
//                            Log.d("Date selected: ",str_date);
                            temp = new Report(send_report);
                            String tmp = temp.getSql().replace("{thangnam}",str_date);
                    		temp.setSql(tmp);
	                    	if(temp.getMaBC().equalsIgnoreCase("null")) {
//	                    		Log.d("SQL new to showDetail: ",temp.getSql());
	        					showDetailReport(temp);
	        				} else {
//	        					Log.d("SQL new to GetData: ",temp.getSql());
//	        					getDataPara(send_report);
//	        					System.out.println("Sizze: " + send_report.getList().size());
	        					GetDataParameter(temp);
	        				}
                        }
                    }
                });
        datePickerDialog.setCancelable(false);
        datePickerDialog.show();
	}
	
	public void GetDataParameter(Report report) {
		Intent show_parameter = new Intent(getApplicationContext(),Next.class);
		Bundle b = new Bundle();
		b.putSerializable("info_report", report);
		b.putSerializable("old_report", report);
		show_parameter.putExtras(b);
		show_parameter.putExtra("IP", IP);
		startActivity(show_parameter);
	}
	   
	public void getDataPara(Report in_report) {
		para_asyntask = new Parameter_AsyncTask(this, IP, in_report,in_report);
		para_asyntask.execute();
		send_report = para_asyntask.report;
		getDataParaDone = para_asyntask.isFinish;
	}
	public void getInfoReport(){
		mytt = new DSReport_AsyncTask(this, IP, Id_Group);
		mytt.execute();
		arrReport = mytt.arrReport;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dsreport, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
