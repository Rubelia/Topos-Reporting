package com.example.tp_mobile_v2.Fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tp_mobile_v2.Object.Frag;
import com.example.tp_mobile_v2.Object.Parameter;
import com.example.tp_mobile_v2.Object.Report;
import com.example.tp_mobile_v2.R;
import com.example.tp_mobile_v2.ShowParameter;
import com.example.tp_mobile_v2.Show_Detail_Report;
import com.example.tp_mobile_v2.ViewHolder;

public class BooleanFragment extends Fragment {
    // Store instance variables

//	private String LOCATION = "BooleanFragment";
	static final int TYPE_ARRSTRING = 1;
	static final int TYPE_STRING = 2;
	static final int TYPE_DATE = 3;
	static final int TYPE_NUMBER = 4;
	static final int TYPE_BOOLEAN = 5;
	
	static Parameter para ;
	Report report;
	Report old_report;
	ArrayList<Parameter> arr_para;
	private static ArrayList<Exception> list_excep;
	ArrayList<String> arr_err;

	private ViewPager mViewPager;
	Frag frag;
	private static String IP ;
	private String sql_Old;
	private boolean isAdd ;

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
    	} catch (Exception e) {
        	list_excep.add(e);
        }
        arr_para = new ArrayList<>();
        list_excep = new ArrayList<>();
		isAdd = true;
		sql_Old = report.getSql();
    }
   
    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
        View view = inflater.inflate(R.layout.type_boolean, container, false);

        final ArrayList<Boo> arr = new ArrayList<>();
        final ArrayList<Boo> arr_temp = new ArrayList<>();
        Boo boo1 = new Boo();
        boo1.setName("True");
        boo1.setValues(1);
        Boo boo2 = new Boo();
        boo2.setName("False");
        boo2.setValues(0);
        arr.add(boo1);
        arr.add(boo2);
        MyAdapter adapter = new MyAdapter(getActivity(), R.layout.para_item, arr);
        
        ListView list = (ListView) view.findViewById(R.id.list);
		list.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
//				isReSelected = false;
				Boo temp = arr.get(position);
				if(!temp.isSelected ) {
					String ballotBoxCheck = "&#9745";
					Spanned spanBallotBoxCheck = Html.fromHtml(ballotBoxCheck);
					
					TextView itemChecked = (TextView) view.findViewById(R.id.txtSymbol);
					itemChecked.setText(spanBallotBoxCheck );
					temp.isSelected = true;
					arr_temp.add(temp);
				} else {
					String ballotBox = "&#9744;";
					Spanned spanBallotBox = Html.fromHtml(ballotBox);
					
					TextView itemChecked = (TextView) view.findViewById(R.id.txtSymbol);
					itemChecked.setText(spanBallotBox );
					temp.isSelected = false;
					arr_temp.remove(temp);	
				}
			}
		});
        
        TextView txtDes = (TextView) view.findViewById(R.id.txtDes);
        txtDes.setText(para.getDes());
        btnBack = (Button) view.findViewById(R.id.btnBack);
        btnNext = (Button) view.findViewById(R.id.btnNext);

        String myTag = getTag();
        ((ShowParameter) getActivity()).setTagBooleanFragment(myTag);
        
        if(frag.position == 0) {
        	btnBack.setVisibility(View.INVISIBLE);
        	btnNext.setOnClickListener(new View.OnClickListener() {
     			
     			@Override
     			public void onClick(View v) {
     				// TODO Auto-generated method stub
     				arr_err = new ArrayList<>();
     				if(arr_temp.isEmpty()) {
						String a = getString(R.string.text_view_notice_boolean_1);
     					arr_err.add(a);
     					showNotice(arr_err);
     				} else if(arr_temp.size() == 2) {
						String a = getString(R.string.text_view_notice_boolean_2);
     					arr_err.add(a);
     					showNotice(arr_err);
     				} else {
     					for(int i=0;i<arr_temp.size();i++) {
     						Boo tmp = arr_temp.get(i);
     						if(tmp.isSelected) {
     							para.setReplace(""+tmp.getValues());
     						}
     					}
     					para.setReplace(arr.get(0).getValues() + "");
     					int t = setTypeNext(frag);
						setChangeFragment(t);
						mViewPager = (ViewPager)getActivity().findViewById(R.id.pager);
						mViewPager.setCurrentItem(frag.position + 1);
         			}
 				}
     		});
        }
        else if (frag.position == frag.max_list - 1) {
			btnNext.setText(getString(R.string.btn_set_text_done));
			btnBack.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int t = setTypeBack( frag);
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
					if (arr_temp.isEmpty()) {
						String a = getString(R.string.text_view_notice_boolean_1);
						arr_err.add(a);
						showNotice(arr_err);
					} else if (arr_temp.size() == 2) {
						String a = getString(R.string.text_view_notice_boolean_2);
						arr_err.add(a);
						showNotice(arr_err);
					} else {
						for (int i = 0; i < arr_temp.size(); i++) {
							Boo tmp = arr_temp.get(i);
							if (tmp.isSelected) {
								para.setReplace("" + tmp.getValues());
							}
						}
						AddOrReplacePara();
						String tmp = sql_Old;
						for (int i = 0; i < arr_para.size(); i++) {
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
					if (arr_temp.isEmpty()) {
						String a = getString(R.string.text_view_notice_boolean_1);
						arr_err.add(a);
						showNotice(arr_err);
					} else if (arr_temp.size() == 2) {
						String a = getString(R.string.text_view_notice_boolean_2);
						arr_err.add(a);
						showNotice(arr_err);
					} else {
						for (int i = 0; i < arr_temp.size(); i++) {
							Boo tmp = arr_temp.get(i);
							if (tmp.isSelected) {
								para.setReplace("" + tmp.getValues());
							}
						}
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
    public class MyAdapter extends ArrayAdapter<Boo> {
    	Activity context = null;
    	ArrayList<Boo> myArray = null;
    	int layoutId;

    	public MyAdapter(Activity contextCha, int layoutId, ArrayList<Boo> arr){
    		super(contextCha, layoutId, arr);
    		this.context = contextCha;
    		this.layoutId = layoutId;
    		this.myArray = arr;
    	}
    	public View getView( int position, View convertView, ViewGroup parent){
    		ViewHolder holder;
    		if(convertView == null) {
    			LayoutInflater inflater = context.getLayoutInflater();
        		convertView = inflater.inflate(layoutId, null);
        		holder = new ViewHolder();
        		holder.txtSymbol = (TextView) convertView.findViewById(R.id.txtSymbol);
        		holder.txtName = (TextView) convertView.findViewById(R.id.txtArrPara);
        		convertView.setTag(holder);
    		} else {
    			holder = (ViewHolder) convertView.getTag();
    		}
    		
    		final Boo emp = myArray.get(position);
    		Typeface face;
    		face = Typeface.createFromAsset(context.getAssets(), "Symbola.otf");
    		
    		String ballotBox = "&#9744;";
    		Spanned spanBallotBox = Html.fromHtml(ballotBox);
    		holder.txtSymbol.setTypeface(face);
    		holder.txtSymbol.setText(spanBallotBox) ;
    		holder.txtName.setText(emp.getName());
    			
    		return convertView;
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
		if(isAdd) {
			arr_para.add(para);
			isAdd = false;
		} else {
			arr_para.set(frag.position,para);
		}
	}
    public class Boo {
    	String name;
    	int values;
    	boolean isSelected;
    	Boo()
    	{
    		values = 0;
    		name = "";
    		isSelected = false;
    	}
    	//set
    	public void setValues(int in_values) {
    		values = in_values;
    	}
    	public void setName(String in_name) {
    		name = in_name;
    	}
    	public String getName() {
    		return name;
    	}
    	public int getValues() {
    		return values;
    	}


    }

}