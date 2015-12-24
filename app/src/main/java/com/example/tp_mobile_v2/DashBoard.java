package com.example.tp_mobile_v2;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tp_mobile_v2.ArrayAdapter.DSReport_ArrayAdapter;
import com.example.tp_mobile_v2.AsynTask.DashBoard_AsyncTask;
import com.example.tp_mobile_v2.Fragment.PieChartFragment;
import com.example.tp_mobile_v2.Object.Report;
import com.example.tp_mobile_v2.database.SQLController;

public class DashBoard extends FragmentActivity {

	Activity activity = this;

	private static final String LOCATION = "DashBoard";
	public final static int SETTING_REPORT_IN_DASHBOARD = 0;
	public final static int EDIT_NAME_REPORT = 1;
	public final static int CHECK_DEL_REPORT = 2;
	static final int SET_ALARM_TO_RUN_REPORT = 3;

	public Report report;
	DSReport_ArrayAdapter adapter = null;
	public ArrayList<Report> arr_report;
	public ArrayList<Report> pie_report;
	SQLController sqlController;
//	SQLiteDatabase db;
	String IP, user_name ;
	ArrayList<String> arr_err;
	public String warn_text ;
	private TabHost mTabHost;
    ViewPager mViewPager;
    private TabsAdapter mTabsAdapter;

	public String TagPieChartFragment;
	public void setTagPieChartFragment(String in_str)
    {
		TagPieChartFragment = in_str;
    }
//	public String getTagPieChartFragment() {
//    	return TagPieChartFragment;
//    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dash_board);
		//define
		mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();

        mViewPager = (ViewPager)findViewById(R.id.pager);
        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
		//get IP in database
//		db = openOrCreateDatabase("DBConfig", DashBoard.MODE_PRIVATE, null);
//        db.execSQL("CREATE TABLE IF NOT EXISTS config(ID INTERGER,IP VARCHAR,KV VARCHAR,CH VARCHAR);");
//        Cursor get_ip = db.rawQuery("SELECT * FROM config", null);
//        if(!get_ip.moveToFirst()) {
//        	db.execSQL("INSERT INTO config VALUES('1','sql3.garco10.com.vn/goldmem_service','KV001','AA')");
//        } else {
//        	Cursor get = db.rawQuery("SELECT * FROM config WHERE ID = '1'" , null);
//        	if(get.moveToFirst()) {
//            	IP = get.getString(1);
//            }
//        }

		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			IP = extras.getString("IP");
			user_name = extras.getString("inputUserName");
		}
		sqlController = new SQLController(activity);
		sqlController.open();
		IP = sqlController.getStrIP();
//		Log.d(LOCATION, IP);
		sqlController.close();

		ImageButton btnNew = (ImageButton) findViewById(R.id.btnNew);
		ImageButton btnSetting = (ImageButton) findViewById(R.id.btnSetting);

		ListView list = (ListView) findViewById(R.id.list);
		arr_report = new ArrayList<>();
		report = new Report(getReportInDatabase());
//		showPieChart(report);
		showListReport(arr_report);
		list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
//				Toast.makeText(getApplicationContext(), "Chức năng chưa thực hiện",Toast.LENGTH_LONG).show();
				report = arr_report.get(position);
				showDialog(SETTING_REPORT_IN_DASHBOARD);
				return true;
			}
		});
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
			Report temp = new Report(arr_report.get(position));
			Intent show_report = new Intent(getApplicationContext(),Show_Detail_Report.class);
			Bundle b = new Bundle();
			b.putSerializable("info_report", temp);
			show_report.putExtras(b);
			show_report.putExtra("IP", IP);
			startActivity(show_report);
			}
		});
		
		btnNew.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent ds_group = new Intent (getApplicationContext(),DSGroup.class);
				Bundle b = new Bundle();
				b.putString("IP", IP);
				b.putString("inputUserName",user_name);
				ds_group.putExtras(b);
				startActivity(ds_group);
			}
		});
		btnSetting.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Toast.makeText(getApplicationContext(), "Chức năng chưa thực hiện",Toast.LENGTH_LONG).show();
//				showDialog(SET_ALARM_TO_RUN_REPORT);
//				Intent view_setAlarm = new Intent(getApplicationContext(),SetAlarmToRunReport.class);
//				Bundle b = new Bundle();
//				b.putString("IP",IP);
//				startActivity(view_setAlarm);
			}
		});
		for(int i=0;i<pie_report.size();i++) {
//			Log.d("Position: ",""+i);
			addFragment(i, pie_report.get(i));
		}
		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
	    }
	}
	public void showListReport(ArrayList<Report> arr) {
		if(arr.isEmpty()) {
		} else {
			adapter = new DSReport_ArrayAdapter(this,R.layout.reports_list_layout, arr);
			ListView lv = (ListView) findViewById(R.id.list);
			lv.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
    protected Dialog onCreateDialog(int id){
    	switch(id){
			case SETTING_REPORT_IN_DASHBOARD :
				return createSettingReport(report);
			case CHECK_DEL_REPORT:
				return createCheckDelReport(warn_text);
			case SET_ALARM_TO_RUN_REPORT:
				return createSetAlarmDialog();
		//    	case SHOW_IMG_PIE_CHART:
		//    		return creatImgPieChart();
			default:
				return null;
    	}
    }
    
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
 
        switch (id) {
	        case SETTING_REPORT_IN_DASHBOARD:
	            // Clear the input box. 	
	            EditText text1 = (EditText) dialog.findViewById(EDIT_NAME_REPORT);
	        	text1.setText("");
            	break;
	        case CHECK_DEL_REPORT:
	        	break;
            default:
            	break;
        }
    }
    private Dialog createSettingReport(Report in_report){
    	final Dialog dialog = new Dialog(DashBoard.this);
    	final Report get_report = new Report(in_report);
    	dialog.setContentView(R.layout.setting_report_in_dashboard);
    	dialog.setTitle("Tùy chỉnh báo cáo");
    	dialog.setCancelable(false);
    	
    	final Button ok = (Button) dialog.findViewById(R.id.btnSave);
        final Button cancel = (Button) dialog.findViewById(R.id.btnCancel);
        final Button del = (Button) dialog.findViewById(R.id.btnDel);
        final EditText new_name = (EditText) dialog.findViewById(R.id.edit_new_name_report);
                
        new_name.setId(EDIT_NAME_REPORT);

        del.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				warn_text = "Bạn có chắc là muốn xóa báo cáo ra khỏi Danh sách hay không?";
				showDialog(CHECK_DEL_REPORT);
				dialog.dismiss();
			}
		});
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	arr_err = new ArrayList<>();
		//xu ly su kien khi nhap dữ liệu vào :3
            	if(new_name.getText().toString().isEmpty()) {
            		String tmp = "Vui lòng điền tên báo cáo!";
            		arr_err.add(tmp);
            		showNotice(arr_err);
            	} else {
            		String name = new_name.getText().toString();
                	if (name.length() != 0) {
						sqlController.open();
//						sqlController.BeginTransaction();
						try {
							sqlController.updateDashBoard(name, get_report.getId());
						} catch (SQLException err) {
							arr_err.add(err.getMessage());
							showNotice(arr_err);
						}
//                		db.execSQL("UPDATE DASHBOARD SET name_report ='"+ name +"' WHERE ID = '" +get_report.getId()+ "' " );
//                		Report temp = new Report(getReportInDatabase());
//						sqlController.endTransaction();
						sqlController.close();
                		showListReport(arr_report);
                		Toast.makeText(getApplicationContext(), "Đã cập nhật xong tên báo cáo",Toast.LENGTH_LONG).show();
                    }
                	dialog.dismiss();
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
    private Dialog createCheckDelReport(String in_text){
    	final Dialog dialog = new Dialog(DashBoard.this);
    	dialog.setContentView(R.layout.check_del_dialog);
    	dialog.setTitle("Cảnh báo");
    	dialog.setCancelable(true);
    	
    	final Button ok = (Button) dialog.findViewById(R.id.bt_ok);
        final Button cancel = (Button) dialog.findViewById(R.id.bt_cancel);
        final TextView text_warn = (TextView) dialog.findViewById(R.id.txt_arr_notice); 
        text_warn.setText(in_text);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
				arr_err = new ArrayList<String>();
				sqlController.open();
				try {
					sqlController.deleteDashboard(report.getId());
				} catch (SQLException err) {
					arr_err.add(err.getMessage());
					showNotice(arr_err);
				}
				sqlController.close();
//            	db.execSQL("DELETE FROM DASHBOARD WHERE ID = '" +report.getId() + "'");
//              db.execSQL("UPDATE DASHBOARD SET IP ='"+ name +"'");
	    		
//	    		Report temp = new Report(getReportInDatabase());
            	showListReport(arr_report);
            	Toast.makeText(getApplicationContext(), "Đã xóa báo cáo",Toast.LENGTH_LONG).show();
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
    
    public Report getReportInDatabase() {
    	Report result = new Report();
    	arr_report = new ArrayList<>();
    	pie_report = new ArrayList<>();
		sqlController.open();
    	Cursor check = sqlController.getReportIsShowInDashBoard();
//		sqlController.close();
		if(!check.moveToFirst()) {
        } else {
			while(!check.isAfterLast()) {
				if(check.getString(2).equalsIgnoreCase("1")) {
					int location = check.getInt(1);
					switch (location) {
					case 1:
						Report temp_pie_chart = new Report();
						temp_pie_chart.setId(check.getString(0));
						temp_pie_chart.setName(check.getString(3));
						temp_pie_chart.setSql(check.getString(4));
						temp_pie_chart.setTab_Value(check.getString(5));
						temp_pie_chart.setTab_Des(check.getString(6));
						temp_pie_chart.setLocation(1);
						pie_report.add(temp_pie_chart);
						break;
					case 2:
						Report temp = new Report();
						temp.setId(check.getString(0));
						temp.setName(check.getString(3));
						temp.setSql(check.getString(4));
						temp.setTab_Value(check.getString(5));
						temp.setTab_Des(check.getString(6));
						arr_report.add(temp);
						break;
					case 3:
						Report temp_p3 = new Report();
						temp_p3.setId(check.getString(0));
						temp_p3.setName(check.getString(3));
						temp_p3.setSql(check.getString(4));
						temp_p3.setTab_Value(check.getString(5));
						temp_p3.setTab_Des(check.getString(6));
						temp_p3.setLocation(3);
						showLocation(temp_p3);
						break;
					case 4:
						Report temp_p4 = new Report();
						temp_p4.setId(check.getString(0));
						temp_p4.setName(check.getString(3));
						temp_p4.setSql(check.getString(4));
						temp_p4.setTab_Value(check.getString(5));
						temp_p4.setTab_Des(check.getString(6));
						
						result.setLocation(4);
						showLocation(temp_p4);
						break;
					default:
							break;
					}
				} else {
					Log.d(LOCATION,"Không có báo cáo để hiển thị");
				}
			       check.moveToNext();
			}
		}
		sqlController.close();
    	return result;
    }
    
    public void showLocation(Report in) {
    	DashBoard_AsyncTask mytt = new DashBoard_AsyncTask(this, IP, in);
    	mytt.execute();
    }
//    public void showPieChart(Report in_report) {
//    	Pie_chart_3d_AsyncTask mytt = new Pie_chart_3d_AsyncTask(this,in_report, IP);
//		mytt.execute();
//    }
    //tab adapter
    public static class TabsAdapter extends FragmentPagerAdapter implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
        private final Context mContext;
        private final TabHost mTabHost;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<>();

        static final class TabInfo {
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                clss = _class;
                args = _args;
            }
        }

        static class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;
            public DummyTabFactory(Context context)
            {
                mContext = context;
            }
            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabsAdapter(FragmentActivity activity, TabHost tabHost, ViewPager pager) {
            super(activity.getSupportFragmentManager());
            mContext = activity;
            mTabHost = tabHost;
            mViewPager = pager;
            mTabHost.setOnTabChangedListener(this);
//            mTabHost.setVisibility(View.INVISIBLE);
//            mTabHost.getTabWidget().getChildAt(0).setVisibility(View.INVISIBLE);
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
            tabSpec.setContent(new DummyTabFactory(mContext));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);
            mTabs.add(info);
            mTabHost.addTab(tabSpec);
            notifyDataSetChanged();
        }

        public int getCount() {
//        	Log.d("Size of mTabs: ",""+mTabs.size());
            return mTabs.size();
        }

        public Fragment getItem(int position) {
            TabInfo info = mTabs.get(position);
//            Fragment.
            return Fragment.instantiate(mContext, info.clss.getName(), info.args);

        }

        public void onTabChanged(String tabId) {
//            int position = mTabHost.getCurrentTab();
//            mViewPager.setCurrentItem(position);
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
            // Unfortunately when TabHost changes the current tab, it kindly
            // also takes care of putting focus on it when not in touch mode.
            // The jerk.
            // This hack tries to prevent this from pulling focus out of our
            // ViewPager.
//        	Log.d("Position: ",""+position);
            TabWidget widget = mTabHost.getTabWidget();
            int oldFocusability = widget.getDescendantFocusability();
            widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            mTabHost.setCurrentTab(position);
            widget.setDescendantFocusability(oldFocusability);
        }

        public void onPageScrollStateChanged(int state) {
        }
    }

    public void addFragment(int i,Report in_report) {
		Bundle b = new Bundle();
		b.putSerializable("inputReport", in_report);
		b.putString("inputIP", IP);
		mTabsAdapter.addTab(mTabHost.newTabSpec("Page "+i).setIndicator("Page "+i), PieChartFragment.class, b);
		mTabHost.getTabWidget().getChildAt(i).setVisibility(View.INVISIBLE);
	}

	private Dialog createSetAlarmDialog() {
		final Dialog dialog = new Dialog(DashBoard.this);
		dialog.setContentView(R.layout.set_alarm);
		dialog.setTitle("Cài giờ chạy báo cáo");
		dialog.setCancelable(true);

		final TextView txtTime = (TextView) dialog.findViewById(R.id.txtTime);
//		final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
//		txtTime.setText(timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute());

		Button btnSave = (Button) dialog.findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				txtTime.setText(timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute());
			}
		});
		return dialog;
	}

}