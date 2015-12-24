package com.example.tp_mobile_v2.Fragment;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tp_mobile_v2.Object.Frag;
import com.example.tp_mobile_v2.Object.Parameter;
import com.example.tp_mobile_v2.Object.Report;
import com.example.tp_mobile_v2.R;
import com.example.tp_mobile_v2.ShowParameter;
import com.example.tp_mobile_v2.Show_Detail_Report;
import com.example.tp_mobile_v2.ViewHolder;

public class NumberFragment extends Fragment {

//    private static final String LOCATION = "NumberFragment";
	private static final int TYPE_ARRSTRING = 1;
	private static final int TYPE_STRING = 2;
	private static final int TYPE_DATE = 3;
	private static final int TYPE_NUMBER = 4;
	private static final int TYPE_BOOLEAN = 5;
	
	private static Parameter para ;
	ArrayList<Parameter> arr_para;
	private static ArrayList<Exception> list_excep;
	ArrayList<String> arr_err;

	Report report;
    Report old_report;
	Frag frag;
	private String IP ;
    private String sql_Old ;
	public boolean isAdd;
	ViewPager mViewPager;
	ViewHolder holder;
    Button btnBack, btnNext;

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = this.getArguments();
        IP = b.getString("inputIP");
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
        sql_Old = report.getSql();
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
        View view = inflater.inflate(R.layout.type_number, container, false);
        holder = new ViewHolder();
        holder.edit = (EditText) view.findViewById(R.id.txtInputPara);
        holder.setIsChoose(false);
        view.setTag(holder);
        isAdd = true;
        String myTag = getTag();
        ((ShowParameter) getActivity()).setTagNumberFragment(myTag);

		TextView txtPara = (TextView) view.findViewById(R.id.txtParaNumber);
        txtPara.setText(para.getCaption());
		TextView txtDes = (TextView) view.findViewById(R.id.txtDes);
        txtDes.setText(para.getDes());

        btnBack = (Button) view.findViewById(R.id.btnBack);
        btnNext = (Button) view.findViewById(R.id.btnNext);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        holder.edit.requestFocus();
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(holder.edit, InputMethodManager.SHOW_IMPLICIT);
//        holder.edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                // TODO Auto-generated method stub
//                if (!hasFocus) {
//                    String test = holder.edit.getText().toString();
//					Log.d(LOCATION,"Text was change here: "+test);
//                    holder.isChoose = true;
//                }
//            }
//        });
        if(frag.position == 0) {
            btnBack.setVisibility(View.INVISIBLE);
            btnNext.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    arr_err = new ArrayList<>();
                    if( holder.edit.getText().toString().equalsIgnoreCase("")) {
                        String a = getString(R.string.text_view_notice_number);
                        arr_err.add(a);
                        showNotice(arr_err);
                    } else {
                        para.setReplace(holder.edit.getText().toString());
                        AddOrReplacePara();
                        int t = setTypeNext(frag);
                        setChangeFragment(t);
                        mViewPager = (ViewPager)getActivity().findViewById(R.id.pager);
                        mViewPager.setCurrentItem(frag.position + 1);
                    }
                }
            });
        } else if(frag.position == frag.max_list -1) {
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
                        String a = getString(R.string.text_view_notice_number);
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
                        String a = getString(R.string.text_view_notice_number);
                        arr_err.add(a);
                        showNotice(arr_err);
                    } else {
                        para.setReplace(holder.edit.getText().toString());
                        AddOrReplacePara();
                        int t = setTypeNext(frag);
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
    public void showDetailReport(Report report)	{
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
            arr_para.set(frag.position,para);
        }
    }

}