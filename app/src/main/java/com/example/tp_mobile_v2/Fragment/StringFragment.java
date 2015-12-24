package com.example.tp_mobile_v2.Fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tp_mobile_v2.Object.Frag;
import com.example.tp_mobile_v2.Object.Parameter;
import com.example.tp_mobile_v2.Object.Report;
import com.example.tp_mobile_v2.R;
import com.example.tp_mobile_v2.ShowParameter;
import com.example.tp_mobile_v2.Show_Detail_Report;
import com.example.tp_mobile_v2.ViewHolder;


public class StringFragment extends Fragment {
    // Store instance variables

	Activity activity;
	private static final String LOCATION = "StringFragment";
	static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
	static final int TYPE_ARRSTRING = 1;
	static final int TYPE_STRING = 2;
	static final int TYPE_DATE = 3;
	static final int TYPE_NUMBER = 4;
	static final int TYPE_BOOLEAN = 5;
	
	static Parameter para ;
	ArrayList<Parameter> arr_para;
	private static ArrayList<Exception> list_excep;
	ArrayList<String> arr_err;

	Report report;
	Report old_report;
	Frag frag;
	private String IP ;
	private String sql_Old;
	String getEdit;
	private boolean isAdd;
	ViewPager mViewPager;
	ViewHolder holder;
    Button btnBack, btnNext;

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = this.getArguments();
        IP = b.getString("inputIP");
        setHasOptionsMenu(true);
        try {
        	para = (Parameter) b.getSerializable("inputPara");
        	frag = (Frag) b.getSerializable("inputFrag");
        	report = (Report) b.getSerializable("inputReport");
			old_report = (Report) b.getSerializable("old_report");
        } catch (Exception e) {
        	list_excep.add(e);
        }
        arr_para = new ArrayList<>();
        list_excep = new ArrayList<>();
		isAdd = true;
		sql_Old = report.getSql();

		activity = this.getActivity();
    }
    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
        View view = inflater.inflate(R.layout.type_string, container, false);
        holder = new ViewHolder();
        holder.edit = (EditText) view.findViewById(R.id.txtParaStr);
        holder.setIsChoose(false);
        view.setTag(holder);
        holder.edit.requestFocus();
//        holder.edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				// TODO Auto-generated method stub
//				if (!hasFocus) {
//					holder.isChoose = true;
//				} else {
////					Log.d("EditText: ","not focus");
//				}
//			}
//		});
		ImageButton imgBtnScanBarcode = (ImageButton) view.findViewById(R.id.imgBtnScanBarcode);
		imgBtnScanBarcode.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					//start the scanning activity from the com.google.zxing.client.android.SCAN intent
					Intent intent = new Intent(ACTION_SCAN);
					intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
					startActivityForResult(intent, 0);
				} catch (ActivityNotFoundException anfe) {
					//on catch, show the download dialog
					showDialog(activity, getString(R.string.mess_not_found_scand_tool), getString(R.string.mess_ass_download_scand_tool), getString(R.string.mess_yes_download_scand_tool), getString(R.string.mess_no_download_scand_tool)).show();
				}
			}
		});

        TextView txtPara = (TextView) view.findViewById(R.id.txtName);
        txtPara.setText(para.getCaption());
        TextView txtDes = (TextView) view.findViewById(R.id.txtDes);
        txtDes.setText(para.getDes());
        btnBack = (Button) view.findViewById(R.id.btnBack);
        btnNext = (Button) view.findViewById(R.id.btnNext);

        String myTag = getTag();
        ((ShowParameter) getActivity()).setTagStrFragment(myTag);

        if(frag.position == 0) {
        	btnBack.setVisibility(View.INVISIBLE);
        	btnNext.setOnClickListener(new View.OnClickListener() {
     			
     			@Override
     			public void onClick(View v) {
     				// TODO Auto-generated method stub
     				arr_err = new ArrayList<>();
     				if( holder.edit.getText().toString().equalsIgnoreCase("")) {
						String a = getString(R.string.text_view_notice_string);
     					arr_err.add(a);
     					showNotice(arr_err);
     				} else {
     					para.setReplace(getEdit);
     					int t = setTypeNext(frag);
     					setChangeFragment(t);
						mViewPager = (ViewPager)getActivity().findViewById(R.id.pager);
						mViewPager.setCurrentItem(frag.position + 1);
     				}				
     			}
     		});
        }
        else if(frag.position == frag.max_list -1) {
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
     				if( holder.edit.getText().toString().equalsIgnoreCase("")) {
     					String a = getString(R.string.text_view_notice_string);
     					arr_err.add(a);
     					showNotice(arr_err);
     				} else {
     					para.setReplace(holder.edit.getText().toString());
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
     				if( holder.edit.getText().toString().equalsIgnoreCase("")) {
     					String a = getString(R.string.text_view_notice_string);
     					arr_err.add(a);
     					showNotice(arr_err);
     				} else {
     					para.setReplace(getEdit);
     					int t = setTypeNext(frag);
						AddOrReplacePara();
						setChangeFragment(t);
						mViewPager = (ViewPager)getActivity().findViewById(R.id.pager);
						mViewPager.setCurrentItem(frag.position + 1);
     				}
     			}
     		});
        }
        for (Exception e : list_excep) {
		      // Do whatever you want for the exception here
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(e.getMessage());
			AlertDialog alert = builder.create();
			alert.show();
		}
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_for_scan, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
			case R.id.action_scan_barcode:
				try {
					//start the scanning activity from the com.google.zxing.client.android.SCAN intent
					Intent intent = new Intent(ACTION_SCAN);
					intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
					startActivityForResult(intent, 0);
				} catch (ActivityNotFoundException anfe) {
					//on catch, show the download dialog
					showDialog(this.getActivity(), getString(R.string.mess_not_found_scand_tool), getString(R.string.mess_ass_download_scand_tool), getString(R.string.mess_yes_download_scand_tool), getString(R.string.mess_no_download_scand_tool)).show();
				}
				return true;
            default:
                break;
        }
        return true;
    }
	private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
		AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
		downloadDialog.setTitle(title);
		downloadDialog.setMessage(message);
		downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i) {
				Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				try {
					act.startActivity(intent);
				} catch (ActivityNotFoundException anfe) {
					anfe.printStackTrace();
				}
			}
		});
		downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i) {
			}
		});
		return downloadDialog.show();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == Activity.RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
//                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

				holder.edit.setText(contents);
//				Log.d(LOCATION,contents);
			}
		}
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
    public void showDetailReport(Report report) {
		Intent show_report = new Intent(getActivity().getApplicationContext(),Show_Detail_Report.class);
		Bundle b = new Bundle();
		b.putSerializable("info_report", report);
		b.putSerializable("old_report", old_report);
		show_report.putExtras(b);
		show_report.putExtra("IP", IP);
		startActivity(show_report);
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
		if(isAdd) {
			arr_para.add(para);
			isAdd = false;
		} else {
			for(int i=0;i<arr_para.size();i++) {
				Log.d(LOCATION,"Item in " + i + " has name: " + arr_para.get(i).getRep());
			}
			Log.d(LOCATION,"Run here <-----------------");
			arr_para.set(frag.position,para);
			for(int i=0;i<arr_para.size();i++) {
				Log.d(LOCATION,"Item in " + i + " has name: " + arr_para.get(i).getRep());
			}
		}
	}
}