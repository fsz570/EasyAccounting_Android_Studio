package com.fsz570.easyaccounting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.fsz570.easyaccounting.adapter.ListItemAdapter;
import com.fsz570.easyaccounting.util.Consts;
import com.fsz570.easyaccounting.util.Utils;
import com.fsz570.easyaccounting.vo.CategoryVo;
import com.fsz570.easyaccounting.vo.EventVo;
import com.fsz570.easyaccounting.vo.TransactionVo;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ListFragment extends Fragment {

	private static final String TAG = "ListFragment";
	private static final int NO_CHECKED_BUTTON = -1;

	private View rootView;
	private AccountingActivity parentActivity;

	private DateFormat sdf;

	private ToggleButton queryDayBtn;
	private ToggleButton queryWeekBtn;
	private ToggleButton queryMonthBtn;
	private ToggleButton queryYearBtn;
	private ToggleButton queryAllBtn;

	private ImageButton queryLeftBtn;
	private ImageButton queryRightBtn;
	private ImageButton queryTodayBtn;
	private Button queryHideBtn;
	private LinearLayout queryPanel;
	private TextView listDurationTextview;
	private String startDateStr;
	private String listDurationTextviewFormat = "%s ~ %s";

	private Spinner eventSpinner;
	private Spinner categorySpinner;
	private ListView listView;
	private ListItemAdapter listItemAdapter;

	private int lastCheckedBtnId = NO_CHECKED_BUTTON;
	private View lastSelectedListItem;
	private boolean QUERY_PANEL_HIDE = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        Log.i(TAG, "onCreateView()");

		rootView = inflater.inflate(R.layout.fragment_list, container, false);
		parentActivity = ((AccountingActivity) getActivity());
		sdf = android.text.format.DateFormat.getDateFormat(parentActivity);
		queryPanel = ((LinearLayout)rootView.findViewById(R.id.query_panel));

		return rootView;
	}

    @Override
    public void onResume(){
        super.onResume();
        initUi();
    }

    private void initUi() {
        Log.d(TAG, "initUi()");

        initQueryPanel();
        initEventSpinner();
        initCategorySpinner();
        initListView();

        rootView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    if(lastCheckedBtnId==NO_CHECKED_BUTTON){
                        queryWeekBtn.setChecked(false);
                        queryMonthBtn.setChecked(false);
                        queryYearBtn.setChecked(false);
                        queryAllBtn.setChecked(false);

                        queryDayBtn.setChecked(true);
                        lastCheckedBtnId = R.id.query_day_btn;
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
		this.listDurationTextview = (TextView) rootView
				.findViewById(R.id.query_duration_tv);
		

		queryDayBtn.setOnClickListener(clickListener);
		queryWeekBtn.setOnClickListener(clickListener);
		queryMonthBtn.setOnClickListener(clickListener);
		queryYearBtn.setOnClickListener(clickListener);
		//listAllBtn.setOnClickListener(clickListener);
		queryAllBtn.setVisibility(View.GONE);
		queryLeftBtn.setOnClickListener(clickListener);
		queryRightBtn.setOnClickListener(clickListener);
		queryTodayBtn.setOnClickListener(clickListener);
		queryHideBtn.setOnClickListener(clickListener);

		listDurationTextview.setText(getToday());
		setStartDateStr(getToday());

	}


	private void initEventSpinner() {
		Log.d(TAG, "initEventSpinner()");

		this.eventSpinner = (Spinner) rootView
				.findViewById(R.id.query_event_spinner);

		List<EventVo> events = parentActivity.getDbAdapter().getEnabledEvents();

        ArrayAdapter<EventVo> dataAdapter = new ArrayAdapter<EventVo>(
                parentActivity,
                R.layout.simple_spinner_item, events);
        dataAdapter
                .setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		eventSpinner.setAdapter(dataAdapter);

		eventSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				EventVo eventVo = (EventVo) eventSpinner
						.getItemAtPosition(position);

                getItemLists gfl = new getItemLists();
                gfl.execute();
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
		List <CategoryVo> categoryList = Utils.populateCategory(parentCategoryList);
		
		categoryList.add(0, new CategoryVo(Consts.NO_CATEGORY_ID, Consts.NO_CATEGORY_ID, "",
			null, 0));

        ArrayAdapter<CategoryVo> dataAdapter = new ArrayAdapter<CategoryVo>(parentActivity,
                R.layout.simple_spinner_item, categoryList);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

	    // attaching data adapter to spinner
		categorySpinner.setAdapter(dataAdapter);
		
		categorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				CategoryVo categoryVo = (CategoryVo) categorySpinner
						.getItemAtPosition(position);
                getItemLists gfl = new getItemLists();
                gfl.execute();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}
		});
	}

	// Init list view
	private void initListView() {

		listItemAdapter = new ListItemAdapter(parentActivity);
        getItemLists gfl = new getItemLists();
        gfl.execute();
		//listItemAdapter.setDataSource(getTransactionData());

		listView = (ListView) rootView.findViewById(R.id.transaction_list_view);
		listView.setAdapter(listItemAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				ImageButton delBtn = (ImageButton) view
						.findViewById(R.id.list_view_item_delete_btn);
				ImageButton updBtn = (ImageButton) view
						.findViewById(R.id.list_view_item_update_btn);
				
				if (delBtn.getVisibility() == View.GONE) {
					setItemButtonVisiblity(View.VISIBLE, listItemAdapter.getItem(position), delBtn, updBtn);
					hideButtonOnLastSelectedListItem();
					lastSelectedListItem = view;
				} else {
					setItemButtonVisiblity(View.GONE, listItemAdapter.getItem(position), delBtn, updBtn);
					lastSelectedListItem = null;
				}
			}
		});
	}
	
	private void hideButtonOnLastSelectedListItem() {

		if (lastSelectedListItem == null) {
			return;
		} else {

			ImageButton delBtn = (ImageButton) lastSelectedListItem
					.findViewById(R.id.list_view_item_delete_btn);
			ImageButton updBtn = (ImageButton) lastSelectedListItem
					.findViewById(R.id.list_view_item_update_btn);

			setItemButtonVisiblity(View.GONE, lastSelectedListItem.getTag(),
					delBtn, updBtn);
			
			lastSelectedListItem = null;
		}

	}
	
	private void setItemButtonVisiblity(int visiblity, Object tag, ImageButton delBtn, ImageButton updBtn){
		if (visiblity == View.VISIBLE) {
			delBtn.setVisibility(View.VISIBLE);
			updBtn.setVisibility(View.VISIBLE);
			delBtn.setFocusable(false);
			updBtn.setFocusable(false);
			delBtn.setOnClickListener(clickListener);
			updBtn.setOnClickListener(clickListener);
			delBtn.setTag(tag);
			updBtn.setTag(tag);
		} else if (visiblity == View.GONE) {
			delBtn.setVisibility(View.GONE);
			updBtn.setVisibility(View.GONE);
		}
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
					QUERY_PANEL_HIDE = false;
					queryHideBtn.setBackgroundResource(R.drawable.hide_show_btn_up);
				}else{
					Utils.collapseHeight(queryPanel, 3);
					QUERY_PANEL_HIDE = true;
					queryHideBtn.setBackgroundResource(R.drawable.hide_show_btn_down);
				}
				break;
			case R.id.list_view_item_delete_btn:
				createDelDialog((TransactionVo)v.getTag()).show();
				break;
			case R.id.list_view_item_update_btn:			
				Intent intent = new Intent(parentActivity, UpdateTransactionActivity.class);

				intent.putExtra(TransactionVo.TRANSACTION_VO_NAME, (TransactionVo)v.getTag());
				startActivityForResult(intent, Consts.ACTIVITY_REQUEST_CODE_FOR_UPDATE_TRANSACTION);
				break;
			}
			
			//No matter what you do, clear buttons on last selected list item
			hideButtonOnLastSelectedListItem();
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
			// Not allow uncheck
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
			queryDayBtn.setChecked(true);
			lastCheckedBtnId = R.id.query_day_btn;
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
				listDurationTextview.setText(startDateStr);
				break;
			case R.id.query_week_btn:
			case R.id.query_month_btn:
			case R.id.query_year_btn:
				listDurationTextview
						.setText(String.format(listDurationTextviewFormat,
								startDateStr, getEndDate()));
				break;
			case R.id.query_all_btn :
				listDurationTextview.setText(parentActivity.getResources().getString(R.string.all));
				break;
			}

		} catch (Exception e) {
			Log.d(TAG, "changeDate error while doing " + days);
			Log.d(TAG, e.getMessage());

			listDurationTextview.setText(getToday());
			this.setStartDateStr(getToday());
		}
		// After range or backward|forward, renew data source of list
        getItemLists gfl = new getItemLists();
        gfl.execute();
	}

    private class getItemLists extends
            AsyncTask<Void, String, List<TransactionVo>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected List<TransactionVo> doInBackground(Void... params) {
            if(lastCheckedBtnId == R.id.query_all_btn){
                return parentActivity.getTransactionsWithCondition(getEventId(), getCategoryId());
            }else{
                return parentActivity.getTransactions(startDateStr, getEndDate(),
                        getEventId(), getCategoryId());
            }
        }

        @Override
        protected void onPostExecute(List<TransactionVo> result) {
            super.onPostExecute(result);

            listItemAdapter.setDataSource(result);
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

	private AlertDialog createDelDialog(final TransactionVo transVo) {
		AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);

		StringBuffer sb = new StringBuffer(String.format(
				"%s %s %s",
				new String[] { transVo.getTranDate(),
						transVo.getTranCategoryName(),
						transVo.getTranAmount() + "" }));

		if (transVo.getTranEventName() != null
				&& !"".equals(transVo.getTranEventName())) {
			sb.append(" [").append(transVo.getTranEventName()).append("]");
		}

		builder.setMessage(sb.toString()).setTitle(R.string.del_dialog_title);

		builder.setPositiveButton(R.string.cal_btn_confirm_text,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						parentActivity.deleteTransaction(transVo);
						dialog.dismiss();
						
						//Refresh data after delete transaction
						changeDate(0);
					}
				});
		builder.setNegativeButton(R.string.cal_btn_cancel_text,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});

		return builder.create();
	}
	
	public void refreshListData(){
		changeDate(0);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// matches the result code passed from ChildActivity
		if (requestCode == Consts.ACTIVITY_REQUEST_CODE_FOR_UPDATE_TRANSACTION) {
			if (resultCode == Activity.RESULT_CANCELED) {
				Toast.makeText(parentActivity, getResources().getString(R.string.cancel), Toast.LENGTH_LONG).show();
			} else if (resultCode == Activity.RESULT_OK) {
				Toast.makeText(parentActivity, getResources().getString(R.string.update_transaction_success), Toast.LENGTH_LONG).show();
                refreshListData();
			}

		}
	}

}
