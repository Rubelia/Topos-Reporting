package com.example.tp_mobile_v2.Fragment;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
//import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.Button;
import android.widget.ImageView;

import com.example.tp_mobile_v2.AsynTask.Pie_chart_3d_AsyncTask;
import com.example.tp_mobile_v2.DashBoard;
import com.example.tp_mobile_v2.Object.Report;
import com.example.tp_mobile_v2.R;
import com.example.tp_mobile_v2.ViewHolder;

public class PieChartFragment extends Fragment {
    // Store instance variables
	
	Report report;
	public static ArrayList<Exception> list_excep;
	ArrayList<String> arr_err;
//	ArrayList<Boo> arr_temp;
	
	static String IP;
	
//	ViewPager mViewPager;
//	ViewHolder holder;
//    private int page, max_list;
    
//    Button btnBack, btnNext;

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = this.getArguments();
        IP = b.getString("inputIP");
        try {
        	report = (Report) b.getSerializable("inputReport");
    	} catch (Exception e) {
        	list_excep.add(e);
        }
        list_excep = new ArrayList<>();
    }
   
    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
        View view = inflater.inflate(R.layout.type_pie_chart, container, false);
        ViewHolder holder;
		
		holder = new ViewHolder();
		holder.img_pie_chart = (ImageView) view.findViewById(R.id.img_password);
		view.setTag(holder);
        
        String myTag = getTag();
        ((DashBoard) getActivity()).setTagPieChartFragment(myTag);
        
        showPieChart(report,holder);
        for (Exception e : list_excep) {
		      // Do whatever you want for the exception here
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(e.getMessage());
			AlertDialog alert = builder.create();
			alert.show();
		}
        return view;
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
    
    public void showPieChart(Report in_report, ViewHolder in_holder) {
    	Pie_chart_3d_AsyncTask mytt = new Pie_chart_3d_AsyncTask(getActivity(),in_report, IP, in_holder);
		mytt.execute();
    }
    
}