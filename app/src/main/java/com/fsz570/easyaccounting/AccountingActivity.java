package com.fsz570.easyaccounting;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
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
import com.fsz570.easyaccounting.vo.EventVo;
import com.fsz570.easyaccounting.vo.MonthlyBudgetVo;
import com.fsz570.easyaccounting.vo.PieChartDataVo;
import com.fsz570.easyaccounting.vo.TransactionVo;

import java.util.List;

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
        if (BuildConfig.DEBUG) {
//            Utils.enableStrictMode();
        }
        super.onCreate(savedInstanceState);

        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        actionBar = getActionBar();

        setContentView(R.layout.activity_accounting);
	}

    @Override
    protected void onStart(){
        Log.d(TAG, "onStart()");
        super.onStart();

        initDB();
    }

    @Override
    protected void onResume(){
        Log.d(TAG, "onResume()");
        super.onResume();
        initUi();
    }

    @Override
    protected void onStop(){
        Log.d(TAG, "onStop()");
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
		Log.d(TAG, "initDB()");
        //Instant the DB Adapter will create the DB is it not exist.
//		dbAdapter = new DBAdapter(AccountingActivity.this);
        dbAdapter = ((EasyMoneyApplication)getApplication()).getDbAdapterInstance();
        Log.d("DB_OPEN","dbAdapter opened? " + dbAdapter.isOpen());

        try{
//            if(!dbAdapter.checkDataBaseExist()) {
//                dbAdapter.createDataBase();
//            }
//            dbAdapter.openDataBase();
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

        if(dbAdapter.getCategories().size() == 0 ){
            initDefaultCategory();
        }
    }

    private void initDefaultCategory(){
        dbAdapter.initDefaultCategory();
    }


	private void initUi(){
		Log.d(TAG, "initUi()");
		initTabs();
	}
	
	private void initTabs(){
		Log.d(TAG, "initTabs()");
		
        // Initialization
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

        if(actionBar.getTabCount()==0) {
            for (int i = 0; i < 4; i++) {
                LinearLayout customView = (LinearLayout) inflater.inflate(tabs_background[i], null);
                customView.setLayoutParams(layoutParams);
                ImageView imageView = (ImageView) customView.findViewById(tabs_image_view_id[i]);
                imageView.setMinimumWidth(tabWidth - 1);

                actionBar.addTab(actionBar.newTab().setIcon(tabs_icon[i])
                        .setTabListener(this).setCustomView(customView));
            }
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
	
	public MonthlyBudgetVo insertTrans(TransactionVo transVo){
		
		try{
			dbAdapter.insertTrans(transVo);

            String now = transVo.getTranDate();
            long monthlyBudget = getMonthlyBudget();
            long monthlyExpense = getExpenseByMonth(now);

            return new MonthlyBudgetVo(monthlyBudget, monthlyExpense);
		}catch(Exception e){
			Log.d(TAG, "insertTrans fail : " + e.getMessage());
		}

        return null;
	}
	
	public List<TransactionVo> queryTrans(int eventId, int categoryId){
		return dbAdapter.getTransactionsWithCondition(eventId, categoryId);
	}
	
	public List<TransactionVo> queryTransWithDateRange(String startDate, String endDate, int eventId, int categoryId){
		return dbAdapter.getTransactions(startDate, endDate, eventId, categoryId);
	}
	
	public List<PieChartDataVo> queryTransForPieChar(String startDate, String endDate, int eventId, int categoryId){
		return dbAdapter.getTransactionsForChart(startDate, endDate, eventId, categoryId);
	}
	
    public List<PieChartDataVo> queryAllTransForChar(int eventId, int categoryId){
		return dbAdapter.getAllTransactionsForChart(eventId, categoryId);
	}
	
	public int getInputEventId(){
		Spinner eventSpinner = (Spinner)findViewById(R.id.input_event_spinner);
		return ((EventVo)eventSpinner.getSelectedItem()).getId();
	}
	
//	public int getInputCategoryId(){
//        Spinner categorySpinner = (Spinner)findViewById(R.id.input_category_spinner);
//        return ((CategoryVo)categorySpinner.getSelectedItem()).getId();
//    }
	
	public void deleteTransaction(TransactionVo transVo){
		
		dbAdapter.deleteTransaction(transVo);
	}

    public List<TransactionVo> getTransactionsWithCondition(int eventId, int categoryId){
        return dbAdapter.getTransactionsWithCondition(eventId, categoryId);
    }

    public List<TransactionVo> getTransactions(String startDate, String endDate, int eventId, int categoryId){
        return dbAdapter.getTransactions(startDate, endDate, eventId, categoryId);
    }

    public long getMonthlyBudget(){
        return dbAdapter.getMonthlyBudget();
    }

    public long getExpenseByMonth(String now){
        return dbAdapter.getExpenseByMonth(now);
    }

    public MonthlyBudgetVo getMonthlyBudgetVo(String now){
        return new MonthlyBudgetVo(getMonthlyBudget(), getExpenseByMonth(now));

    }
}
