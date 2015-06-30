package com.fsz570.easyaccounting;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fsz570.easyaccounting.util.Consts;
import com.fsz570.easyaccounting.util.Utils;
import com.fsz570.easyaccounting.vo.CategoryVo;
import com.fsz570.easyaccounting.vo.EventVo;
import com.fsz570.easyaccounting.vo.PieChartDataVo;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ChartFragment extends Fragment {
	
	private static final String TAG = "ChartFragment";
	
	private static final int NO_CHECKED_BUTTON = -1;
	
	private View rootView;
	private AccountingActivity parentActivity;

	private DateFormat sdf;

	private ToggleButton queryDayBtn, queryWeekBtn, queryMonthBtn, queryYearBtn, queryAllBtn;

	private ImageButton queryLeftBtn;
	private ImageButton queryRightBtn;
	private ImageButton queryTodayBtn;
	private Button queryHideBtn;
	private LinearLayout queryPanel;
	private TextView listDurationTextView;
	private String startDateStr;
	private String listDurationTextViewFormat = "%s ~ %s";

	private Spinner eventSpinner;
	private Spinner categorySpinner;

	private int lastCheckedBtnId = NO_CHECKED_BUTTON;
	private boolean QUERY_PANEL_HIDE = false;
	private PieChart pieChart;
	 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		rootView = inflater.inflate(R.layout.fragment_chart, container, false);
		parentActivity = ((AccountingActivity) getActivity());
		sdf = android.text.format.DateFormat.getDateFormat(parentActivity);
		queryPanel = ((LinearLayout)rootView.findViewById(R.id.chart_query_panel));

		return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        initUi();
    }

    private void initUi(){
    	Log.d(TAG, "initUi()");

        initQueryPanel();
        initChart();
        initEventSpinner();
        initCategorySpinner();

        rootView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {

                    if(lastCheckedBtnId==NO_CHECKED_BUTTON){
                        queryDayBtn.setChecked(false);
                        queryMonthBtn.setChecked(false);
                        queryYearBtn.setChecked(false);
                        queryAllBtn.setChecked(false);

                        queryWeekBtn.setChecked(true);
                        lastCheckedBtnId = R.id.query_week_btn;
                        changeDate(0);
                    }else{
                        changeDate(0);
                    }
                }
            }
        });
    }
    
	// Initial UI setting
	private void initQueryPanel() {

		RadioGroup toggleGroup = ((RadioGroup) rootView.findViewById(R.id.query_toggle_group));
		toggleGroup.setWeightSum(4);
		toggleGroup.setOnCheckedChangeListener(toggleListener);

		this.queryDayBtn = (ToggleButton) rootView
				.findViewById(R.id.query_day_btn);
		this.queryWeekBtn = (ToggleButton) rootView
				.findViewById(R.id.query_week_btn);
		this.queryMonthBtn = (ToggleButton) rootView
				.findViewById(R.id.query_month_btn);
		this.queryYearBtn = (ToggleButton) rootView
				.findViewById(R.id.query_year_btn);
		this.queryAllBtn = (ToggleButton) rootView
				.findViewById(R.id.query_all_btn);

		this.queryLeftBtn = (ImageButton) rootView
				.findViewById(R.id.query_left_btn);
		this.queryRightBtn = (ImageButton) rootView
				.findViewById(R.id.query_right_btn);
		this.queryTodayBtn = (ImageButton) rootView
				.findViewById(R.id.query_today_btn);
		this.queryHideBtn = (Button) rootView
				.findViewById(R.id.query_hide_btn);
		this.listDurationTextView = (TextView) rootView
				.findViewById(R.id.query_duration_tv);

		//listDayBtn.setOnClickListener(clickListener);
		queryDayBtn.setVisibility(View.GONE);
		queryWeekBtn.setOnClickListener(clickListener);
		queryMonthBtn.setOnClickListener(clickListener);
		queryYearBtn.setOnClickListener(clickListener);
		queryAllBtn.setOnClickListener(clickListener);
		queryLeftBtn.setOnClickListener(clickListener);
		queryRightBtn.setOnClickListener(clickListener);
		queryTodayBtn.setOnClickListener(clickListener);
		queryHideBtn.setOnClickListener(clickListener);

		listDurationTextView.setText(getToday());
		setStartDateStr(getToday());



    }
	
	private void initEventSpinner() {
		Log.d(TAG, "initEventSpinner()");

		this.eventSpinner = (Spinner) rootView
				.findViewById(R.id.query_event_spinner);

		List<EventVo> events = parentActivity.getDbAdapter().getEnabledEvents();
        ArrayAdapter<EventVo> dataAdapter = new ArrayAdapter<EventVo>(
                ((AccountingActivity) getActivity()),
                R.layout.simple_spinner_item, events);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		eventSpinner.setAdapter(dataAdapter);

		eventSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				EventVo eventVo = (EventVo) eventSpinner
						.getItemAtPosition(position);

				//Update Chart
				pieChart.setData(getTransactionDataWithCondition(eventVo.getId(), getCategoryId()));
		        // undo all highlights
		        pieChart.highlightValues(null);

		        pieChart.invalidate();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});
	}
	
	private void initCategorySpinner(){
		Log.d(TAG, "initCategorySpinner()");
		
		this.categorySpinner = (Spinner) rootView.findViewById(R.id.query_category_spinner);
		
		List <CategoryVo> parentCategoryList = parentActivity.getDbAdapter().getCategories();
		
		parentCategoryList.add(0, new CategoryVo(Consts.NO_CATEGORY_ID, Consts.NO_CATEGORY_ID, "",
			null, 0));
		
        ArrayAdapter<CategoryVo> dataAdapter = new ArrayAdapter<CategoryVo>(parentActivity,
                R.layout.simple_spinner_item, parentCategoryList);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

	    // attaching data adapter to spinner
		categorySpinner.setAdapter(dataAdapter);
		
		categorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				CategoryVo categoryVo = (CategoryVo) categorySpinner
						.getItemAtPosition(position);
				Log.d(TAG, "OnItemSelectedListener : " + categoryVo.getTranCategoryName());

				//Update Chart
				pieChart.setData(getTransactionDataWithCondition(getEventId(), categoryVo.getId()));
		        // undo all highlights
		        pieChart.highlightValues(null);

		        pieChart.invalidate();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});
	}


	// Click Listener for all buttons
	private View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.query_day_btn:
			case R.id.query_week_btn:
			case R.id.query_month_btn:
			case R.id.query_year_btn:
				// If range button clicked
				onToggle((ToggleButton) v);
				if(!(queryLeftBtn.isEnabled() && queryRightBtn.isEnabled() && queryTodayBtn.isEnabled())){
					queryLeftBtn.setEnabled(true);
					queryRightBtn.setEnabled(true);
					queryTodayBtn.setEnabled(true);
				}
				break;
			case R.id.query_all_btn:
				// If range button clicked
				onToggle((ToggleButton) v);
				queryLeftBtn.setEnabled(false);
				queryRightBtn.setEnabled(false);
				queryTodayBtn.setEnabled(false);
				break;
			// If left or right button clicked
			case R.id.query_left_btn:
				shiftDuration(-1); // backward
				break;
			case R.id.query_right_btn:
				shiftDuration(1); // forward
				break;
			case R.id.query_today_btn:
				changeDate(new Date(), 0); // Today
				break;
			case R.id.query_hide_btn:
				LayoutParams params=queryPanel.getLayoutParams();
				
				if(QUERY_PANEL_HIDE){
					Utils.expandHeight(queryPanel, 3);
					//params.height=LayoutParams.WRAP_CONTENT ;
					//queryPanel.setLayoutParams(params);
					QUERY_PANEL_HIDE = false;
					queryHideBtn.setBackgroundResource(R.drawable.hide_show_btn_up);
				}else{				
					Utils.collapseHeight(queryPanel, 3);
					//params.height=0 ;
					//queryPanel.setLayoutParams(params);
					QUERY_PANEL_HIDE = true;
					queryHideBtn.setBackgroundResource(R.drawable.hide_show_btn_down);
				}
				break;

			}
		}
	};

	// Trigger while click toggle button
	public void onToggle(ToggleButton view) {
		if (lastCheckedBtnId != view.getId()) {
			((RadioGroup) view.getParent()).check(view.getId());
			lastCheckedBtnId = view.getId();
			changeDate(new Date(), 0);
			//changeDate(0);
		} else {
			// Not allow unchecked
			view.setChecked(true);
		}
	}

	private RadioGroup.OnCheckedChangeListener toggleListener = new RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(final RadioGroup radioGroup, final int i) {
			for (int j = 0; j < radioGroup.getChildCount(); j++) {
				final ToggleButton view = (ToggleButton) radioGroup
						.getChildAt(j);
				view.setChecked(view.getId() == i);
			}
		}
	};

	// Shift duration ( backward or forward )
	private void shiftDuration(int direction) {
		switch (lastCheckedBtnId) {
		case R.id.query_day_btn:
			changeDate(direction * 1);
			break;
		case R.id.query_week_btn:
			changeDate(direction * 7);
			break;
		case R.id.query_month_btn:
			changeDate(direction);
			break;
		case R.id.query_year_btn:
			changeDate(direction);
			break;
		case NO_CHECKED_BUTTON:
			queryWeekBtn.setChecked(true);
			lastCheckedBtnId = R.id.query_week_btn;
			changeDate(direction * 1);
			break;
		}
	}

	// For day and week, using current date on screen
	private void changeDate(int days) {

		changeDate(getStartDate(), days);
	}

	// Get start date of current duration
	private Date getStartDate() {
		Date nowDate;

		try {
			nowDate = sdf.parse(startDateStr);
		} catch (Exception e) {
			Log.e(TAG, "getNowDate() : " + e.getMessage());
			nowDate = new Date();
		}

		return nowDate;
	}

	private void changeDate(Date nowDate, int days) {
		Calendar cTmp = Calendar.getInstance();
		Calendar cStart = Calendar.getInstance();

		cTmp.setTime(nowDate);
		cStart.setTime(nowDate);
		int month = cTmp.get(Calendar.MONTH);
		int year = cTmp.get(Calendar.YEAR);

		if (lastCheckedBtnId == R.id.query_week_btn) {
			int dayInWeek = cTmp.get(Calendar.DAY_OF_WEEK) -1; //Sunday = 1
			cStart.add(Calendar.DATE, dayInWeek * -1);
			cStart.add(Calendar.DATE, days); // forward or backward
		} else if (lastCheckedBtnId == R.id.query_month_btn) {
			cStart.set(year, month, 1);
			cStart.add(Calendar.MONTH, days); // forward or backward
		} else if (lastCheckedBtnId == R.id.query_year_btn) {
			cStart.set(year, Calendar.JANUARY, 1);
			cStart.add(Calendar.YEAR, days); // forward or backward
		} else {
			cStart.add(Calendar.DATE, days); // forward or backward
		}

		try {

			setStartDateStr(cStart.getTime());

			switch (lastCheckedBtnId) {
			case R.id.query_day_btn:
				listDurationTextView.setText(startDateStr);
				break;
			case R.id.query_week_btn:
			case R.id.query_month_btn:
			case R.id.query_year_btn:
				listDurationTextView
						.setText(String.format(listDurationTextViewFormat,
                                startDateStr, getEndDate()));
				break;
			case R.id.query_all_btn :
				listDurationTextView.setText(parentActivity.getResources().getString(R.string.all));
				break;
			}

		} catch (Exception e) {
			Log.d(TAG, "changeDate error while doing " + days);
			Log.d(TAG, e.getMessage());

			listDurationTextView.setText(getToday());
			this.setStartDateStr(getToday());
		}

		pieChart.setData(getTransactionData());
        // undo all highlights
        pieChart.highlightValues(null);

        pieChart.invalidate();
	}
	
	private PieData getTransactionData() {
		return getTransactionDataWithCondition(getEventId(), getCategoryId());
		
	}
	
	private PieData getTransactionDataWithCondition(int eventId, int categoryId) {
		if(lastCheckedBtnId == R.id.query_all_btn){
			return translateToChartData(parentActivity.queryAllTransForChar(eventId, categoryId));
		}else{
			return translateToChartData(parentActivity.queryTransForPieChar(startDateStr, getEndDate(), eventId, categoryId));
		}
	}


	private int getMaxDays(Date date, int maxDayField) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		int duration = 1;

		if (maxDayField == Calendar.DAY_OF_MONTH) {
			duration = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		} else if (maxDayField == Calendar.DAY_OF_YEAR) {
			duration = cal.getActualMaximum(Calendar.DAY_OF_YEAR);
		}

		return duration;
	}

	private int getDuration(Date date) {

		int duration = 1;

		switch (lastCheckedBtnId) {
		case R.id.query_day_btn:
			duration = 1;
			break;
		case R.id.query_week_btn:
			duration = 7;
			break;
		case R.id.query_month_btn:
			duration = getMaxDays(date, Calendar.DAY_OF_MONTH);
			break;
		case R.id.query_year_btn:
			duration = getMaxDays(date, Calendar.DAY_OF_YEAR);
			break;
		default:
			duration = 1;
		}

		return duration;
	}

	private String getEndDate() {
		Date date;
		Calendar cal = new GregorianCalendar();

		try {
			date = sdf.parse(startDateStr);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			date = new Date();
		}

		cal.setTime(date);
		switch (lastCheckedBtnId) {
		case R.id.query_day_btn:
		case R.id.query_week_btn:
			cal.add(Calendar.DATE, getDuration(date) - 1);
			break;
		case R.id.query_month_btn:
			// Last day in same month
			cal.set(Calendar.DATE, getDuration(date));
			break;
		case R.id.query_year_btn:
			// 12/31
			cal.set(Calendar.MONTH, Calendar.DECEMBER);
			cal.set(Calendar.DATE, 31);
			break;
		}

		return sdf.format(cal.getTime());
	}

	// Get today string
	private String getToday() {
		return sdf.format(Utils.getToday());
	}

	private void setStartDateStr(Date startDate) {
		try {
			this.startDateStr = sdf.format(startDate);
		} catch (Exception e) {
			Log.e(TAG, "setStartDateStr() : " + e.getMessage());
			this.startDateStr = getToday();
		}
	}

	private void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}

	private int getEventId() {

		EventVo eventVo = (EventVo) eventSpinner.getSelectedItem();
		if (eventVo == null) {
			return Consts.NO_EVENT_ID;
		} else {
			return eventVo.getId();
		}
	}

	private int getCategoryId() {
		CategoryVo categoryVo = (CategoryVo) categorySpinner.getSelectedItem();
		if (categoryVo == null) {
			return Consts.NO_CATEGORY_ID;
		} else {
			return categoryVo.getId();
		}
	}

	public void refreshListData(){
		changeDate(0);
	}
	
    
    private void initChart(){
    	pieChart = (PieChart) rootView.findViewById(R.id.pie_chart);
    	
    	pieChart.setDrawHoleEnabled(true);
    	pieChart.setHoleColor(Color.rgb(235, 235, 235));
    	pieChart.setHoleRadius(45f);
    	pieChart.setDrawCenterText(true);

    	pieChart.setValueTextColor(Color.DKGRAY);
    	pieChart.setDrawYValues(true);
    	pieChart.setDrawXValues(true);
    	pieChart.setValueFormatter(MyValueFormatter.newInstance());
    	
    	pieChart.setRotationAngle(0);
    	pieChart.setRotationEnabled(true);

    	pieChart.setDescription("");
    	pieChart.setFocusable(false);
    	pieChart.setNoDataText("");
    }
    
    private PieData translateToChartData(List<PieChartDataVo> barChartDataVoList) {


    	ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        double total = 0;

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < barChartDataVoList.size(); i++) {
        	xVals.add(barChartDataVoList.get(i).getCategoryName());
            yVals1.add(new Entry(barChartDataVoList.get(i).getAmount(), i));
            total += barChartDataVoList.get(i).getAmount();
        }


        PieDataSet set1 = new PieDataSet(yVals1, "");
        set1.setSliceSpace(3f);
        
        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        
        colors.add(ColorTemplate.getHoloBlue());

        set1.setColors(colors);
        
        pieChart.setCenterText(String.format(getResources().getString(R.string.bar_chart_center_text_format), Utils.formatAmount(total)));

        return new PieData(xVals, set1);
    }
    
    private static class MyValueFormatter implements ValueFormatter{
    	
    	private static MyValueFormatter myValueFormatter = new MyValueFormatter();
    	
    	public static MyValueFormatter newInstance(){
    		return myValueFormatter;
    	}
    	
    	private MyValueFormatter() {};
    	
    	@Override
    	public String getFormattedValue(float value){
    		return Utils.formatAmount(value);
    	}
    }
}
