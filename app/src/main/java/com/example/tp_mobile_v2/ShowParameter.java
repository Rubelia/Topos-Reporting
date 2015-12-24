package com.example.tp_mobile_v2;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.example.tp_mobile_v2.Fragment.ArrStrFragment;
import com.example.tp_mobile_v2.Fragment.BooleanFragment;
import com.example.tp_mobile_v2.Fragment.DateFragment;
import com.example.tp_mobile_v2.Fragment.NumberFragment;
import com.example.tp_mobile_v2.Fragment.StringFragment;
import com.example.tp_mobile_v2.Object.Frag;
import com.example.tp_mobile_v2.Object.Parameter;
import com.example.tp_mobile_v2.Object.Report;

public class ShowParameter extends FragmentActivity {

	public String IP;
	private Report report;
    private Report old_report;
	ArrayList<Exception> list_excep;
	ArrayList<String> arr_type;
//	static Frag frag;
	
	
	private TabHost mTabHost;
    ViewPager mViewPager;
    private TabsAdapter mTabsAdapter;
    
    String TagArrStrFragment;
    String TagStrFragment;
    String TagDateFragment;
    String TagNumberFragment;
    String TagBooleanFragment;
    //set tag fragment
    public void setTagArrStrFragment(String in_arrstr)
    {
    	TagArrStrFragment = in_arrstr;
    }
    public void setTagStrFragment(String in_str)
    {
    	TagStrFragment = in_str;
    }
    public void setTagDateFragment(String in_date)
    {
    	TagDateFragment = in_date;
    }
    public void setTagNumberFragment(String in_number)
    {
    	TagNumberFragment = in_number;
    }
    public void setTagBooleanFragment(String in_boolean)
    {
    	TagBooleanFragment = in_boolean;
    }
    //get tag fragment
    public String getTagArrStrFragment() {
    	return TagArrStrFragment;
    }
    public String getTagStrFragment() {
    	return TagStrFragment;
    }
    public String getTagDateFragment() {
    	return TagDateFragment;
    }
    public String getTagNumberFragment() {
    	return TagNumberFragment;
    }
    public String getTagBooleanFragment() {
    	return TagBooleanFragment;
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_parameter);
		//define
		IP = "";
		report = new Report();
		list_excep = new ArrayList<>();
		//tabhost
		mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();

        mViewPager = (ViewPager)findViewById(R.id.pager);
        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
		
		//get bundle
		Bundle extras = getIntent().getExtras();
		if(extras !=null) {
		    IP = extras.getString("IP");
		}
		try {
			Bundle b = getIntent().getExtras();
			report = (Report) b.getSerializable("info_report");
            old_report = (Report) b.getSerializable("old_report");
		} catch (Exception e) {
			list_excep.add(e);
			e.printStackTrace();
		}
		
		int max_list = report.getList().size();
        if(max_list == 1) {
            arr_type = new ArrayList<>(max_list);
            Parameter tmp = report.getList().get(0);
            arr_type.add(report.getList().get(0).getDataType());
            Frag frag = new Frag(0,max_list,arr_type.get(0),"null","null");
            addFragment(0,max_list,tmp,frag);
        } else {
            arr_type = new ArrayList<>(max_list);
            for(int i=0;i<max_list;i++) {
                arr_type.add(report.getList().get(i).getDataType());
            }
            for(int i=0;i<report.getList().size();i++) {
                max_list = report.getList().size();
                Parameter tmp = report.getList().get(i);
                if(i==0) {
                    Frag frag = new Frag(i,max_list,arr_type.get(i),"null",arr_type.get(i+1));
                    addFragment(i,max_list,tmp,frag);
                } else if (i == max_list -1) {
                    max_list = report.getList().size();
                    Frag frag = new Frag(i,max_list,arr_type.get(i),arr_type.get(i-1),"null");
                    addFragment(i,max_list,tmp,frag);
                } else {
                    Frag frag = new Frag(i,max_list,arr_type.get(i),arr_type.get(i-1),arr_type.get(i+1));
                    addFragment(i,max_list,tmp,frag);
                }
            }
        }

//        mTabsAdapter.addTab(mTabHost.newTabSpec("one").setIndicator("One"), ArrStrFragment.class, null);
//        mTabsAdapter.addTab(mTabHost.newTabSpec("two").setIndicator("Two"), DateFragment.class, null);

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }
	
	// class tabadapter
	public static class TabsAdapter extends FragmentPagerAdapter implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener
    {
        private final Context mContext;
        private final TabHost mTabHost;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<>();

        static final class TabInfo
        {
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(String _tag, Class<?> _class, Bundle _args)
            {
                clss = _class;
                args = _args;
            }
        }

        static class DummyTabFactory implements TabHost.TabContentFactory
        {
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
            TabWidget widget = mTabHost.getTabWidget();
            int oldFocusability = widget.getDescendantFocusability();
            widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            mTabHost.setCurrentTab(position);
//            mTabHost.getTabWidget().getChildAt(position).setVisibility(View.INVISIBLE);
            widget.setDescendantFocusability(oldFocusability);
        }
        public void onPageScrollStateChanged(int state) {
        }
    }
	public void addFragment(int i, int in_max, Parameter in_para, Frag in_frag) {
		Bundle b = new Bundle();
		b.putSerializable("inputPara", in_para);
		b.putSerializable("inputFrag", in_frag);
		b.putSerializable("inputReport", report);
        b.putSerializable("old_report", old_report);
//		Log.d("SQL of report: ",report.getSql());
		b.putString("inputIP", IP);
		if( in_para.getDataType().equalsIgnoreCase("ArrayString")) {
//			Log.d("Here ArrayString: ","<------");
//			Log.d("I: ",""+i);
			mTabsAdapter.addTab(mTabHost.newTabSpec("Page "+i).setIndicator("Page "+i), ArrStrFragment.class, b);
		} else if( in_para.getDataType().equalsIgnoreCase("String")) {
//			Log.d("Here String: ","<------");
//			Log.d("I: ",""+i);
			mTabsAdapter.addTab(mTabHost.newTabSpec("Page "+i).setIndicator("Page "+i), StringFragment.class, b);
		} else if( in_para.getDataType().equalsIgnoreCase("DateTime")) {
//			Log.d("Here DateTime: ","<------");
//			Log.d("I: ",""+i);
			mTabsAdapter.addTab(mTabHost.newTabSpec("Page "+i).setIndicator("Page "+i), DateFragment.class, b);
		} else if( in_para.getDataType().equalsIgnoreCase("Number")) {
//			Log.d("Here Number: ","<------");
//			Log.d("I: ",""+i);
			mTabsAdapter.addTab(mTabHost.newTabSpec("Page "+i).setIndicator("Page "+i), NumberFragment.class, b);
		} else if( in_para.getDataType().equalsIgnoreCase("Boolean")) {
//			Log.d("Here Boolean: ","<------");
//			Log.d("I: ",""+i);
			mTabsAdapter.addTab(mTabHost.newTabSpec("Page "+i).setIndicator("Page "+i), BooleanFragment.class, b);
		}	
	}
}
