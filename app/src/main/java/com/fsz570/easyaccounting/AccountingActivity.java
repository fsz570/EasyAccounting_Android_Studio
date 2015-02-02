package com.fsz570.easyaccounting;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.fsz570.db_utils.DBAdapter;
import com.fsz570.easyaccounting.adapter.TabsPagerAdapter;
import com.fsz570.easyaccounting.vo.CategoryVo;
import com.fsz570.easyaccounting.vo.EventVo;
import com.fsz570.easyaccounting.vo.PieChartDataVo;
import com.fsz570.easyaccounting.vo.TransactionVo;

import java.util.List;
import java.util.Locale;

public class AccountingActivity extends FragmentActivity implements
		ActionBar.TabListener { 
	
	private static final String TAG = "AccountingActivity";

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private int[] tabs_icon = { R.drawable.tab_input, R.drawable.tab_list, R.drawable.tab_chart, R.drawable.tab_setting };
    private int[] tabs_title = { R.string.tab_input, R.string.tab_list, R.string.tab_chart, R.string.tab_setting };
    private int[] tabs_background = {R.layout.tab_input_background, R.layout.tab_list_background, R.layout.tab_chart_background, R.layout.tab_setting_background};
    private int[] tabs_image_view_id = {R.id.image_view_input, R.id.image_view_list, R.id.image_view_chart, R.id.image_view_setting};
	
	//Database
	DBAdapter dbAdapter = null;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        actionBar = getActionBar();

        setContentView(R.layout.activity_accounting);

        //initDB();
        initUI();
    	
	}

    @Override
    protected void onStart(){
        Log.d(TAG, "onStart()");
        super.onStart();

        initDB();
    }

    @Override
    protected void onStop(){

        if(dbAdapter != null){
            dbAdapter.close();
        }
        super.onStop();
    }

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}
	

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			return PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
            return getString(tabs_title[position]).toUpperCase(l);
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_accounting,
					container, false);
			return rootView;
		}
	}
	
	
	private void initDB(){
		Log.d(TAG, "initDB() start");
        //Instant the DB Adapter will create the DB is it not exist.
		dbAdapter = new DBAdapter(AccountingActivity.this);

        // code that needs 6 seconds for execution
        try{
        	dbAdapter.createDataBase();
            dbAdapter.openDataBase();
            initData();

        }catch(Exception e){
        	Log.d(TAG, "initDB() Exception");
            if(e.getMessage() !=null) {
                Log.d(TAG, e.getMessage());
            }else{
                e.printStackTrace();
            }
        }finally{
        	//dbAdapter.close();
        }
        // after finishing, close the progress bar
        Log.d(TAG, "initDB() end.");
	}

    private void initData(){

        Log.d(TAG, "Language : " + Locale.getDefault().getLanguage()); // zh

        if(dbAdapter.getCategories().size() == 0 ){
            initDefaultCategory();
        }
    }

    private void initDefaultCategory(){
        dbAdapter.initDefaultCategory();
    }


	private void initUI(){
		Log.d(TAG, "initUI()");
		initTabs();
	}
	
	private void initTabs(){
		Log.d(TAG, "initTabs()");
		
        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);

        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
 
        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int tabWidth = metrics.widthPixels / 4;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(tabWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		for (int i = 0; i < 4; i++) {
            LinearLayout customView  = (LinearLayout)inflater.inflate(tabs_background[i], null);
            customView.setLayoutParams(layoutParams);
            ImageView imageView = (ImageView)customView.findViewById(tabs_image_view_id[i]);
            imageView.setMinimumWidth(tabWidth-1);

			actionBar.addTab(actionBar.newTab().setIcon(tabs_icon[i])
                    .setTabListener(this).setCustomView(customView));
		}
        
		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
	
	public DBAdapter getDbAdapter(){
		return this.dbAdapter;
	}
	
	public void insertTrans(TransactionVo transVo){
		
		try{
			dbAdapter.insertTrans(transVo);

		}catch(Exception e){
			Log.d(TAG, "insertTrans fail : " + e.getMessage());
		}
	}
	
	public List<TransactionVo> queryTrans(int eventId, int categoryId){
		
		Log.d(TAG, "queryTrans() Event ID : " + eventId);
		Log.d(TAG, "queryTrans() Category ID : " + categoryId);
		
		return dbAdapter.getTransactionsWithCondition(eventId, categoryId);
	}
	
	public List<TransactionVo> queryTransWithDateRange(String startDate, String endDate, int eventId, int categoryId){
		
		Log.d(TAG, "queryTrans()");
		Log.d(TAG, "Start Date : " + startDate + "; End Date : " + endDate);
		Log.d(TAG, "queryTrans() Event ID : " + eventId);
		Log.d(TAG, "queryTrans() Category ID : " + categoryId);
		
		return dbAdapter.getTransactions(startDate, endDate, eventId, categoryId);
	}
	
	public List<PieChartDataVo> queryTransForPieChar(String startDate, String endDate, int eventId, int categoryId){
		
		Log.d(TAG, "queryTransForPieChar()");
		Log.d(TAG, "Start Date : " + startDate + "; End Date : " + endDate + "; Event ID : " + eventId+ "; Category ID : " + categoryId);
		
		return dbAdapter.getTransactionsForChart(startDate, endDate, eventId, categoryId);
	}
	
public List<PieChartDataVo> queryAllTransForChar(int eventId, int categoryId){
		
		Log.d(TAG, "queryAllTransForChar()");
		Log.d(TAG, "Event ID : " + eventId);
		Log.d(TAG, "Category ID : " + categoryId);
		
		return dbAdapter.getAllTransactionsForChart(eventId, categoryId);
	}
	
	public int getInputEventId(){
		Spinner eventSpinner = (Spinner)findViewById(R.id.input_event_spinner);
		return ((EventVo)eventSpinner.getSelectedItem()).getId();
	}
	
	public int getInputCategoryId(){
		Spinner categorySpinner = (Spinner)findViewById(R.id.input_category_spinner);
		return ((CategoryVo)categorySpinner.getSelectedItem()).getId();
	}
	
	public void deleteTransaction(TransactionVo transVo){
		
		dbAdapter.deleteTransaction(transVo);
	}
	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//
//		Log.d(TAG, "AccountingActivity.onActivityResult()");
//
//		// matches the result code passed from ChildActivity
//		if (requestCode == Consts.ACTIVITY_REQUEST_CODE_FOR_UPDATE_TRANSACTION) {
//			if (resultCode == RESULT_CANCELED) {
//				Toast.makeText(this, getResources().getString(R.string.btn_cancel_text), Toast.LENGTH_SHORT).show();
//			} else if (resultCode == RESULT_OK) {
//				Toast.makeText(this, getResources().getString(R.string.btn_confirm_text), Toast.LENGTH_SHORT).show();
//			}
//
//			((ListFragment)((TabsPagerAdapter)viewPager.getAdapter()).getItem(1)).refreshListData();
//		}
//	}
}
