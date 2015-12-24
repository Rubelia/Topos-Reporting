package com.example.tp_mobile_v2.Fragment;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tp_mobile_v2.Object.Frag;
import com.example.tp_mobile_v2.Object.Parameter;
import com.example.tp_mobile_v2.Object.Report;
import com.example.tp_mobile_v2.R;
import com.example.tp_mobile_v2.ShowParameter;
import com.example.tp_mobile_v2.Show_Detail_Report;
import com.example.tp_mobile_v2.ViewHolder;

public class DateFragment extends Fragment {
    // Store instance variables
	private static final String LOCATION  = "DateFragment";
	private static final int TYPE_ARRSTRING = 1;
	private static final int TYPE_STRING = 2;
	private static final int TYPE_DATE = 3;
	private static final int TYPE_NUMBER = 4;
	private static final int TYPE_BOOLEAN = 5;
	
	private ViewPager mViewPager;
	
	static ArrayList<Parameter> arr_para ;
	ArrayList<Exception> list_excep ;
	ArrayList<String> arr_err;
	
    private static String date_out;
    private static String dateRe ;
	private String sql_Old;
    String IP;
	private static boolean isAdd ;
	Parameter para;
	Frag frag;
	Report report;
	Report old_report;

	ViewHolder holder;
	Button btnBack, btnNext;
    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle e1 = this.getArguments();
        IP = e1.getString("inputIP");
        para = new Parameter();
        frag = new Frag();
        arr_para = new ArrayList<>();
        list_excep = new ArrayList<>();
        try {
        	para = (Parameter) e1.getSerializable("inputPara");
        	frag = (Frag) e1.getSerializable("inputFrag");
        	report = (Report) e1.getSerializable("inputReport");
			old_report = (Report) e1.getSerializable("old_report");
        } catch (Exception e) {
			list_excep.add(e);
			e.printStackTrace();
		}
		isAdd = true;
		sql_Old = report.getSql();
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.type_date, container, false);

        String myTag = getTag();
        ((ShowParameter) getActivity()).setTagDateFragment(myTag);

		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

        holder = new ViewHolder();
        holder.edit = (EditText) view.findViewById(R.id.editDate);
		date_out = String.valueOf(day) + "/"
				+ String.valueOf(month + 1) + "/" + String.valueOf(year);
		holder.edit.setText(date_out);
		dateRe = String.valueOf(year) + "-"  + String.valueOf(month + 1) + "-"
				+ String.valueOf(day);
        holder.txtCaption =  (TextView) view.findViewById(R.id.txtCaption);
        holder.txtDes = (TextView) view.findViewById(R.id.txtDes);
        holder.setIsChoose(false);
        view.setTag(holder);
        
        holder.txtCaption.setText( para.getCaption());
        holder.txtDes.setText(para.getDes());
        
        holder.edit.requestFocus();
        holder.edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			// TODO Auto-generated method stub
			Bundle b = new Bundle();
			b.putSerializable("inputHolder", holder);
			DialogFragment newFragment = new DatePickerFragment();
			newFragment.setArguments(b);
			newFragment.show(getFragmentManager(), "datePicker");
			}
		});
        for (Exception e : list_excep) {
		      // Do whatever you want for the exception here
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(e.getMessage());
			AlertDialog alert = builder.create();
			alert.show();
		}
        
        btnBack = (Button) view.findViewById(R.id.btnBack);
        btnNext = (Button) view.findViewById(R.id.btnNext);
        if(frag.position == 0)
        {
        	btnBack.setVisibility(View.INVISIBLE);
        	btnNext.setOnClickListener(new View.OnClickListener() {
     			
     			@Override
     			public void onClick(View v) {
     				// TODO Auto-generated method stub
     				arr_err = new ArrayList<>();
     				int t = setTypeNext( frag);
     				if(holder.edit.getText().toString().equalsIgnoreCase("")) {
     					String a = getString(R.string.text_view_notice_date);
     					arr_err.add(a);
     					showNotice(arr_err);
     				} else {
     					para.setReplace(dateRe);
						AddOrReplacePara();
						setChangeFragment(t);
						mViewPager = (ViewPager)getActivity().findViewById(R.id.pager);
						mViewPager.setCurrentItem(frag.position + 1);
     				}
     			}
     		});
        }
        else if(frag.position == frag.max_list -1)
        {
        	btnNext.setText(R.string.btn_set_text_done);
        	btnBack.setOnClickListener(new View.OnClickListener() {

    			@Override
    			public void onClick(View v) {
    				// TODO Auto-generated method stub
    				int t = setTypeBack(frag);
    				setChangeFragment(t);
					mViewPager = (ViewPager)getActivity().findViewById(R.id.pager);
					mViewPager.setCurrentItem(frag.position - 1);
    			}
    		});
        	btnNext.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					arr_err = new ArrayList<>();
     				if(holder.edit.getText().toString().equalsIgnoreCase("")) {
     					String a = getString(R.string.text_view_notice_date);
     					arr_err.add(a);
     					showNotice(arr_err);
     				} else {
     					para.setReplace(dateRe);
						AddOrReplacePara();
						String tmp = sql_Old;
     					for(int i=0;i<arr_para.size();i++) {
     						Parameter temp_para = arr_para.get(i);
     						tmp = tmp.replace(temp_para.getPara(), temp_para.getRep());
     						report.setSql(tmp);
     					}
     					showDetailReport(report);
     				}
				}
			});
        } else {
        	btnBack.setOnClickListener(new View.OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {
    				// TODO Auto-generated method stub
    				int t = setTypeBack(frag);
    				setChangeFragment(t);
					mViewPager = (ViewPager)getActivity().findViewById(R.id.pager);
					mViewPager.setCurrentItem(frag.position - 1);
    			}
    		});
        	btnNext.setOnClickListener(new View.OnClickListener() {
     			
     			@Override
     			public void onClick(View v) {
     				// TODO Auto-generated method stub
     				arr_err = new ArrayList<>();
     				if(holder.edit.getText().toString().equalsIgnoreCase("")) {
     					String a = getString(R.string.text_view_notice_date);
     					arr_err.add(a);
     					showNotice(arr_err);
     				} else {
						int t = setTypeNext(frag);
						para.setReplace(dateRe);
     					AddOrReplacePara();
						setChangeFragment(t);
						mViewPager = (ViewPager)getActivity().findViewById(R.id.pager);
						mViewPager.setCurrentItem(frag.position + 1);
     				}
     			}
     		});
        }
        return view;
    }

	public int setTypeNext(Frag frag) {
		String type_next = frag.getType_next();
		if(type_next.equalsIgnoreCase("ArrayString")) {
			return 1;
		} else if (type_next.equalsIgnoreCase("String")) {
			return 2;
		} else if (type_next.equalsIgnoreCase("DateTime")) {
			return 3;
		} else if (type_next.equalsIgnoreCase("Number")) {
			return 4;
		} else return 5;
	}
	public int setTypeBack(Frag frag) {
		String type_back = frag.getType_back();
		if(type_back.equalsIgnoreCase("ArrayString")) {
			return 1;
		} else if (type_back.equalsIgnoreCase("String")) {
			return 2;
		} else if (type_back.equalsIgnoreCase("DateTime")) {
			return 3;
		} else if (type_back.equalsIgnoreCase("Number")) {
			return 4;
		} else return 5;
	}
    public void setInArrPara(ArrayList<Parameter> in) {
    	arr_para = in;
    }
    public void showDetailReport(Report report) {
		Intent show_report = new Intent(getActivity().getApplicationContext(),Show_Detail_Report.class);
		Bundle b = new Bundle();
		b.putSerializable("info_report", report);
		b.putSerializable("old_report",old_report);
		Log.d(LOCATION,report.getSql());
		show_report.putExtras(b);
		show_report.putExtra("IP", IP);
		startActivity(show_report);
	}
    public void showNotice (ArrayList<String> str) {
		String outStr = "";
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		for(int i=0;i<str.size();i++) {
			outStr += str.get(i);
		}
		builder.setMessage(outStr);
		AlertDialog alert = builder.create();
		alert.show();
	}
    public static class DatePickerFragment extends DialogFragment
    implements DatePickerDialog.OnDateSetListener {

    EditText datepicker;
    ViewHolder holder;
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    // Use the current date as the default date in the picker

    	Bundle b = this.getArguments();
    	try {
    		holder = (ViewHolder) b.getSerializable("inputHolder");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
	    final Calendar c = Calendar.getInstance();
	    int year = c.get(Calendar.YEAR);
	    int month = c.get(Calendar.MONTH);
	    int day = c.get(Calendar.DAY_OF_MONTH);
	    date_out = String.valueOf(day) + "/"
                + String.valueOf(month + 1) + "/" + String.valueOf(year);
	    // Create a new instance of DatePickerDialog and return it
	    return new DatePickerDialog(getActivity(), this, year, month, day);
    }

		public void onDateSet(DatePicker view, int year, int month, int day) {
			datepicker = holder.edit;
			date_out = String.valueOf(day) + "/"
					+ String.valueOf(month + 1) + "/" + String.valueOf(year);
			datepicker .setText(date_out);
			dateRe = String.valueOf(year) + "-"  + String.valueOf(month + 1) + "-"
					+ String.valueOf(day);
			holder.setIsChoose(isAdd);
		}
	}
	public void setChangeFragment(int in_t) {
		switch (in_t) {
			case TYPE_ARRSTRING:
				String tagArrStr = ((ShowParameter)getActivity()).getTagArrStrFragment();
				ArrStrFragment arr_frag = (ArrStrFragment)
						getActivity().getSupportFragmentManager().findFragmentByTag(tagArrStr);
				arr_frag.setInArrPara(arr_para);
				break;
			case TYPE_STRING:
				String tagStr = ((ShowParameter)getActivity()).getTagStrFragment();
				StringFragment str_frag = (StringFragment)
						getActivity().getSupportFragmentManager().findFragmentByTag(tagStr);
				str_frag.setInArrPara(arr_para);
				break;
			case TYPE_DATE:
				String tagDate = ((ShowParameter)getActivity()).getTagDateFragment();
				DateFragment date_frag = (DateFragment)
						getActivity().getSupportFragmentManager().findFragmentByTag(tagDate);
				date_frag.setInArrPara(arr_para);
				break;
			case TYPE_NUMBER:
				String tagNumber = ((ShowParameter)getActivity()).getTagNumberFragment();
				NumberFragment number_frag = (NumberFragment)
						getActivity().getSupportFragmentManager().findFragmentByTag(tagNumber);
				number_frag.setInArrPara(arr_para);
				break;
			case TYPE_BOOLEAN:
				String tagBoolean = ((ShowParameter)getActivity()).getTagBooleanFragment();
				BooleanFragment boolean_frag = (BooleanFragment)
						getActivity().getSupportFragmentManager().findFragmentByTag(tagBoolean);
				boolean_frag.setInArrPara(arr_para);
				break;
			default:
				break;
		}
	}
	private void AddOrReplacePara() {
		if(holder.isAdd()) {
			arr_para.add(para);
			holder.setIsAdd(false);
		} else {
			for(int i=0;i<arr_para.size();i++) {
				Log.d(LOCATION,"Item in " + i + " has name: " + arr_para.get(i).getRep());
			}
			Log.d(LOCATION,"Run here <-----------------");
			Log.d(LOCATION,"position: " + frag.position);
			Log.d(LOCATION,"Arr_para: " + arr_para.size());

			arr_para.set(frag.position,para);
			for(int i=0;i<arr_para.size();i++) {
				Log.d(LOCATION,"Item in " + i + " has name: " + arr_para.get(i).getRep());
			}
		}
	}
}