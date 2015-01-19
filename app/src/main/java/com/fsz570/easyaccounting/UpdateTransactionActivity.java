package com.fsz570.easyaccounting;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fsz570.db_utils.DBAdapter;
import com.fsz570.easyaccounting.util.Calculator;
import com.fsz570.easyaccounting.util.Utils;
import com.fsz570.easyaccounting.vo.CategoryVo;
import com.fsz570.easyaccounting.vo.EventVo;
import com.fsz570.easyaccounting.vo.TransactionVo;

import java.text.DecimalFormat;
import java.util.List;

public class UpdateTransactionActivity extends Activity {
	
	private static final String TAG = "UpdateTransactionActivity";
	
	TransactionVo transVo;
	
	//Input element
	private Spinner eventSpinner;
	private Spinner categorySpinner;

	//Calculator
	private String dateStr;
	private EditText calCalculationInput;
	
	private final int btnDigitAry[] = { R.id.cal_btn_dot, R.id.cal_btn_0,
			R.id.cal_btn_1, R.id.cal_btn_2, R.id.cal_btn_3, R.id.cal_btn_4,
			R.id.cal_btn_5, R.id.cal_btn_6, R.id.cal_btn_7, R.id.cal_btn_8,
			R.id.cal_btn_9 };
	
	private final int btnAllAry[] = { R.id.cal_btn_insert, R.id.cal_btn_cancel, R.id.cal_btn_confirm,
			R.id.cal_btn_dot, R.id.cal_btn_clear, R.id.cal_btn_equal, R.id.cal_btn_plus,
			R.id.cal_btn_minus, R.id.cal_btn_times, R.id.cal_btn_divid,
			R.id.cal_btn_0, R.id.cal_btn_1, R.id.cal_btn_2, R.id.cal_btn_3,
			R.id.cal_btn_4, R.id.cal_btn_5, R.id.cal_btn_6, R.id.cal_btn_7,
			R.id.cal_btn_8, R.id.cal_btn_9 };
	 
	private DecimalFormat df = new DecimalFormat("@###########");
    private Calculator calculator;
    private Boolean userIsInTheMiddleOfTypingANumber = false;
    
	//Database
	DBAdapter dbAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fragment_input);
		
		transVo = (TransactionVo)getIntent().getParcelableExtra(TransactionVo.TRANSACTION_VO_NAME);
		
		initDB();
		initInput();
	}
	
	private void initDB(){
		Log.d(TAG, "initDB() start");
        //Instant the DB Adapter will create the DB is it not exist.
		dbAdapter = new DBAdapter(UpdateTransactionActivity.this);

        // code that needs 6 seconds for execution
        try{
        	dbAdapter.createDataBase();

        }catch(Exception e){
        	Log.d(TAG, "initDB() Exception");
        	Log.d(TAG, e.getMessage());
        }finally{
        	dbAdapter.close();	
        }
        // after finishing, close the progress bar
        Log.d(TAG, "initDB() end.");
	}
	
	private void initInput(){
		Log.d(TAG, "initInput()");
		
		TextView dateText = (TextView)findViewById(R.id.date_text);
		dateText.setText(transVo.getTranDate());

        EditText tranComment = (EditText)findViewById(R.id.transaction_item_comment);
        tranComment.setText(transVo.getTranComment());

		initEventSpinner();
		initCategorySpinner();
		initCalculator();
	}
	
	private void initEventSpinner(){
		Log.d(TAG, "initEventSpinner()");
		
		this.eventSpinner = (Spinner) findViewById(R.id.input_event_spinner);
		
		List <EventVo> events = dbAdapter.getEnabledEvents();
		ArrayAdapter<EventVo> dataAdapter = new ArrayAdapter<EventVo>(this,
			    R.layout.simple_spinner_item, events);
		
		dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
	    // attaching data adapter to spinner
		eventSpinner.setAdapter(dataAdapter);
		if(transVo.getTranEventId() != EventVo.EMPTY_EVENT_ID){
			eventSpinner.setSelection(getSelectEvent(transVo.getTranEventId(), events));
		}
	}
	
	private int getSelectEvent(int eventId, List <EventVo> eventList){
		
		for(int i=0; i < eventList.size(); i++){
			if(eventList.get(i).getId() == eventId){
				return i;
			}
		}
		
		return 0;
	}
	
	private void initCategorySpinner(){
		Log.d(TAG, "initCategorySpinner()");
		
		this.categorySpinner = (Spinner) findViewById(R.id.input_category_spinner);
		
		List <CategoryVo> parentCategoryList = dbAdapter.getCategories();
		List <CategoryVo> categoryList = Utils.populateCategory(parentCategoryList);
		
		ArrayAdapter<CategoryVo> dataAdapter = new ArrayAdapter<CategoryVo>(this,
			    R.layout.simple_spinner_item, categoryList);
		
		dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
	    // attaching data adapter to spinner
		categorySpinner.setAdapter(dataAdapter);
		categorySpinner.setSelection(getSelectCategory(transVo.getTranCategoryId(), categoryList));
	}
	
	private int getSelectCategory(int categoryId, List <CategoryVo> categoryList){
		
		for(int i=0; i < categoryList.size(); i++){
			if(categoryList.get(i).getId() == categoryId){
				return i;
			}
		}
		
		return 0;
	}

	private void initCalculator(){
		
		calCalculationInput = (EditText)findViewById(R.id.cal_calculation_input);
        calCalculationInput.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);

		calculator = new Calculator();
		
		df.setMinimumFractionDigits(0);
		df.setMaximumFractionDigits(2);
        df.setMinimumIntegerDigits(1);
        df.setMaximumIntegerDigits(8);
		
		//Set OnClickListener for all buttons
        View.OnClickListener btnClickListener = genCalculatorOnClickListener();
		for(int btnId:btnAllAry){
			((Button) findViewById(btnId)).setOnClickListener(btnClickListener);
		}
		
		((LinearLayout)findViewById(R.id.cal_table_row_btn)).removeView((findViewById(R.id.cal_btn_insert)));

		Button cancelBtn = ((Button)findViewById(R.id.cal_btn_cancel));
		Button confirmBtn = ((Button)findViewById(R.id.cal_btn_confirm));
		cancelBtn.setVisibility(View.VISIBLE);
		confirmBtn.setVisibility(View.VISIBLE);
		
		
		calCalculationInput.setText(transVo.getTranAmountString());
	}
	
	private View.OnClickListener genCalculatorOnClickListener() {
		View.OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
		        String buttonPressed = ((Button) v).getText().toString();
		        
		        if (isContainDigit(btnDigitAry, v.getId())) {
		 
		            // digit was pressed
		            if (userIsInTheMiddleOfTypingANumber) {
		 
		                if (buttonPressed.equals(".") && calCalculationInput.getText().toString().contains(".")) {
		                    // ERROR PREVENTION
		                    // Eliminate entering multiple decimals
		                } else {
		                	calCalculationInput.append(buttonPressed);
		                }
		 
		            } else {
		 
		                if (buttonPressed.equals(".")) {
		                    // ERROR PREVENTION
		                    // This will avoid error if only the decimal is hit before an operator, by placing a leading zero
		                    // before the decimal
		                	calCalculationInput.setText(0 + buttonPressed);
		                } else {
		                	calCalculationInput.setText(buttonPressed);
		                }
		 
		                userIsInTheMiddleOfTypingANumber = true;
		            }
		 
		            //Avoid confirm/cancel trigger calculator
		        } else if(!(v.getId()==R.id.cal_btn_confirm || v.getId()==R.id.cal_btn_cancel )){
		            // operation was pressed
		            if (userIsInTheMiddleOfTypingANumber) {
		                calculator.setOperand(Double.parseDouble(calCalculationInput.getText().toString()));
		                userIsInTheMiddleOfTypingANumber = false;
		            }
		 
		            calculator.performOperation(buttonPressed);
		            calCalculationInput.setText(df.format(calculator.getResult()));
		 
		        }
				
				switch(v.getId()){
					case R.id.cal_btn_cancel :
//						Toast.makeText(UpdateTransactionActivity.this, ((Button) v).getText(),
//                                Toast.LENGTH_SHORT).show();
						setResult(RESULT_CANCELED);
						finish();
						break;
					case R.id.cal_btn_confirm :
//						Toast.makeText(UpdateTransactionActivity.this, ((Button) v).getText(),
//								Toast.LENGTH_SHORT).show();
						updateTrans(genTransVo()); 
						setResult(RESULT_OK);
						finish();
						break;
				}
			}

		};

		return listener;
	}

	private boolean isContainDigit(int[] intAry, int value){
		
		for(int i:intAry){
			if(i==value){
				return true;
			}
		}
		
		return false;
	}
	
	public void updateTrans(TransactionVo transVo){
		
		try{
			dbAdapter.updateTrans(transVo);

		}catch(Exception e){
			Log.d(TAG, "updateTrans fail : " + e.getMessage());
		}
	}
	
	private TransactionVo genTransVo(){
		
		Log.d(TAG, "genTransVo()");
			
		try{
			double tranAmount = Double.parseDouble(calCalculationInput.getText().toString());
			
			TextView dateText = (TextView)findViewById(R.id.date_text);
			dateStr = dateText.getText().toString();
			transVo.setTranDate(dateStr);
			transVo.setTranCategoryId(getInputCategoryId());

			int tranEventId = getInputEventId();
//			if(tranEventId!=EventVo.EMPTY_EVENT_ID){
				transVo.setTranEventId(tranEventId);
//			}
			transVo.setTranAmount(tranAmount);
            transVo.setTranComment(((EditText)findViewById(R.id.transaction_item_comment)).getText().toString());
		}catch(Exception e){
			Log.d(TAG, "Fail : " + e.getMessage());
		}
		
		return transVo;
	}
	
	public int getInputEventId(){
		Spinner eventSpinner = (Spinner)findViewById(R.id.input_event_spinner);
		return ((EventVo)eventSpinner.getSelectedItem()).getId();
	}
	
	public int getInputCategoryId(){
		Spinner categorySpinner = (Spinner)findViewById(R.id.input_category_spinner);
		return ((CategoryVo)categorySpinner.getSelectedItem()).getId();
	}
}