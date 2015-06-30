package com.fsz570.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import com.fsz570.easyaccounting.R;
import com.fsz570.easyaccounting.util.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class fszDatePicker extends TableLayout {

	private static final String TAG = "fszDatePicker";
	
	private static Activity parentActivity;

	private TextView dateText;
	private ImageButton leftBtn;
	private ImageButton rightBtn;
	private ImageButton calendarBtn;

	private static DateFormat sdf;

	private final LayoutInflater inflater;

	public fszDatePicker(Context context) {
		super(context);
		inflater = LayoutInflater.from(context);
		init(context);
	}

//	public fszDatePicker(Context context, AttributeSet attrs, int defStyle) {
//		//super(context, attrs, defStyle);
//        super(context, attrs);
//		inflater = LayoutInflater.from(context);
//		init(context);
//	}

	public fszDatePicker(Context context, AttributeSet attr) {
		super(context, attr);
		inflater = LayoutInflater.from(context);
		init(context);
	}

	private void init(Context context) {
		this.isInEditMode();
		parentActivity = (Activity)context;
		sdf = android.text.format.DateFormat.getDateFormat(context);

		initializeLayoutBasics(context);
		initialComponent();
	}
	

	private void initializeLayoutBasics(Context context) {

        this.setColumnStretchable(1, true);
		inflater.inflate(R.layout.fsz_date_picker, this);
		//setBackgroundResource(R.drawable.calendar_panel_style);
	}

	private void initialComponent() {
		this.dateText = (TextView) findViewById(R.id.date_text);
		this.leftBtn = (ImageButton) findViewById(R.id.left_btn);
		this.rightBtn = (ImageButton) findViewById(R.id.right_btn);
		this.calendarBtn = (ImageButton) findViewById(R.id.calendar_btn);

		dateText.setText(getToday());

		leftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeDate(-1);
			}
		});

		rightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeDate(1);
			}
		});

		calendarBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				DialogFragment newFragment = new DatePickerFragment();
				newFragment.show(parentActivity.getFragmentManager(),
						"datePicker");
			}
		});
	}

	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			TextView dateText = (TextView) getActivity().findViewById(R.id.date_text);
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			Calendar date = Calendar.getInstance();

			if (dateText.length() > 0) {
				// Use user inputted date.
				String dateFromField = dateText.getText().toString();
				try {
					Date date2 = sdf.parse(dateFromField);
					date.setTime(date2);

				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			int year = date.get(Calendar.YEAR);
			int month = date.get(Calendar.MONTH);
			int day = date.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			// Do something with the date chosen by the user
			TextView dateText = (TextView) getActivity().findViewById(R.id.date_text);
			Calendar date = Calendar.getInstance();
			date.set(year, month, day);
//			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			dateText.setText(sdf.format(date.getTime()));
		}
	}

	private String getToday() {
		return sdf.format(Utils.getToday());
	}

	private void changeDate(int days) {
		Calendar c = Calendar.getInstance();

		String nowDate = dateText.getText().toString();

		try {
			c.setTime(sdf.parse(nowDate));
			c.add(Calendar.DATE, days);
			dateText.setText(sdf.format(c.getTime()));
		} catch (Exception e) {
			Log.d(TAG, "changeDate error while doing " + days);
			Log.d(TAG, e.getMessage());

			dateText.setText(getToday());
		}

	}
}
