package com.fsz570.easyaccounting;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fsz570.easyaccounting.util.Calculator;
import com.fsz570.easyaccounting.util.Consts;
import com.fsz570.easyaccounting.util.Utils;
import com.fsz570.easyaccounting.vo.CategoryVo;
import com.fsz570.easyaccounting.vo.EventVo;
import com.fsz570.easyaccounting.vo.MonthlyBudgetVo;
import com.fsz570.easyaccounting.vo.TransactionVo;

import java.text.DecimalFormat;
import java.util.List;

public class InputFragment extends Fragment {
	
	private static final String TAG = "InputFragment";
	
	private View rootView;
	private AccountingActivity parentActivity;
	
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

    private RelativeLayout budgetLayout;
    private ImageView ivExpense;
    private TextView tvBudget;
	 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
    	parentActivity = ((AccountingActivity)getActivity());
        rootView = inflater.inflate(R.layout.fragment_input, container, false);

        return rootView;
    }

    @Override
    public void onResume(){
        Log.d(TAG, "onResume()");
        super.onResume();

        initUi();
    }
    
	private void initUi(){
		Log.d(TAG, "initUi()");

        initBudget();
		initEventSpinner();
		initCategorySpinner();
		initCalculator();
        initComment();
        initBudget();
	}

    private void initBudget(){
        Log.d(TAG, "initBudget()");
        budgetLayout = (RelativeLayout) rootView.findViewById(R.id.budget_layout);
        ivExpense = (ImageView) rootView.findViewById(R.id.iv_expense);
        tvBudget = (TextView) rootView.findViewById(R.id.tv_budget_bar);

        updateBudgetBar(parentActivity.getMonthlyBudgetVo(getCurrentDate()));
    }

    public void updateBudgetBar(MonthlyBudgetVo vo) {
        Log.d(TAG, "updateBudgetBar()");
//        TextView dateText = (TextView) rootView.findViewById(R.id.date_text);
//        String now = dateText.getText().toString();

        //long monthlyBudget = parentActivity.getMonthlyBudget();
        final long monthlyBudget = vo.getMonthlyBudget();
        final long expenseThisMonth = vo.getMonthlyExpense();

        if (monthlyBudget > 0) {
            budgetLayout.setVisibility(View.VISIBLE);
            tvBudget.setText(Utils.formatAmount(expenseThisMonth) + " / " + Utils.formatAmount(monthlyBudget));

            budgetLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            int budgetLayoutWidth = budgetLayout.getMeasuredWidth();

            Log.d(TAG, "budgetLayoutWidth : " + budgetLayoutWidth);
            Log.d(TAG, "expenseThisMonth : " + expenseThisMonth);
            Log.d(TAG, "monthlyBudget : " + monthlyBudget);

            if (expenseThisMonth < monthlyBudget) {
                ivExpense.getLayoutParams().width = (int) (budgetLayoutWidth * expenseThisMonth / monthlyBudget);
            } else {
                ivExpense.getLayoutParams().width = budgetLayoutWidth;
            }
        } else {
            budgetLayout.setVisibility(View.GONE);
        }

/*        parentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (monthlyBudget > 0) {
                    budgetLayout.setVisibility(View.VISIBLE);
                    tvBudget.setText(expenseThisMonth + " / " + monthlyBudget);

                    budgetLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

                    int budgetLayoutWidth = budgetLayout.getMeasuredWidth();

                    Log.d(TAG, "budgetLayoutWidth : " + budgetLayoutWidth);
                    Log.d(TAG, "expenseThisMonth : " + expenseThisMonth);
                    Log.d(TAG, "monthlyBudget : " + monthlyBudget);

                    if (expenseThisMonth < monthlyBudget) {
                        ivExpense.getLayoutParams().width = (int) (budgetLayoutWidth * expenseThisMonth / monthlyBudget);
                    } else {
                        ivExpense.getLayoutParams().width = budgetLayoutWidth;
                    }
                } else {
                    budgetLayout.setVisibility(View.GONE);
                }
            }
        });*/
    }

    private void initEventSpinner(){
		Log.d(TAG, "initEventSpinner()");
		
		this.eventSpinner = (Spinner) rootView.findViewById(R.id.input_event_spinner);

        //TODO modify this to Handler / Message
		List <EventVo> events = parentActivity.getDbAdapter().getEnabledEvents();

        ArrayAdapter<EventVo> dataAdapter = new ArrayAdapter<EventVo>((getActivity()),
                R.layout.simple_spinner_item, events);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

	    // attaching data adapter to spinner
		eventSpinner.setAdapter(dataAdapter);
	}
	
	private void initCategorySpinner(){
		Log.d(TAG, "initCategorySpinner()");
		
		this.categorySpinner = (Spinner) rootView.findViewById(R.id.input_category_spinner);
		
		List <CategoryVo> parentCategoryList = parentActivity.getDbAdapter().getCategories();
		List <CategoryVo> categoryList = Utils.populateCategory(parentCategoryList);

        ArrayAdapter<CategoryVo> dataAdapter = new ArrayAdapter<CategoryVo>(parentActivity,
                R.layout.simple_spinner_item, categoryList);
		dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

	    // attaching data adapter to spinner
		categorySpinner.setAdapter(dataAdapter);
        categorySpinner.requestFocus();
	}

	private void initCalculator() {
        calCalculationInput = (EditText) rootView.findViewById(R.id.cal_calculation_input);
        calCalculationInput.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);

        calculator = new Calculator();

        df.setMinimumFractionDigits(0);
        df.setMaximumFractionDigits(4);
        df.setMinimumIntegerDigits(1);
        df.setMaximumIntegerDigits(8);
        df.setGroupingSize(3);
        df.setGroupingUsed(true);

        //Set OnClickListener for all buttons
        View.OnClickListener btnClickListener = genCalculatorOnClickListener();
        for (int btnId : btnAllAry) {
            ((Button) rootView.findViewById(btnId)).setOnClickListener(btnClickListener);
        }
        Button btnDot = (Button) rootView.findViewById(R.id.cal_btn_dot);
        btnDot.setText(Consts.DECIMAL_SEPRATOR);
    }

    private void initComment(){
        final EditText mEditText = (EditText)rootView.findViewById(R.id.transaction_item_comment);
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus){
                if(hasFocus) {
                    mEditText.setHint("");
                }else if(!hasFocus && (mEditText.length() == 0)){
                    mEditText.setHint(getResources().getString(R.string.comment));
                }
            }
        });
    }
	
	private View.OnClickListener genCalculatorOnClickListener() {
		View.OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
		        String buttonPressed = ((Button) v).getText().toString();
		        
		        if (isContainDigit(btnDigitAry, v.getId())) {
		 
		            // digit was pressed
		            if (userIsInTheMiddleOfTypingANumber) {
		 
		                if (buttonPressed.equals(Consts.DECIMAL_SEPRATOR) && calCalculationInput.getText().toString().contains(Consts.DECIMAL_SEPRATOR)) {
		                    // ERROR PREVENTION
		                    // Eliminate entering multiple decimals
		                } else {
                            //calCalculationInput.append(buttonPressed);
                            StringBuffer tmpBuf = new StringBuffer(calCalculationInput.getText().toString());
                            tmpBuf.append(buttonPressed);

                            //Avoid parseDouble eat the input of "0."
                            if(Utils.isContainOnlyZeroAndDot(tmpBuf.toString())) {
                                calCalculationInput.setText(tmpBuf.toString());
                            }else if(buttonPressed.equals(Consts.DECIMAL_SEPRATOR)){
                                calCalculationInput.setText(tmpBuf.toString());
                            }else{
                                if(tmpBuf.indexOf(Consts.DECIMAL_SEPRATOR)>0 && (tmpBuf.length() - tmpBuf.lastIndexOf(Consts.DECIMAL_SEPRATOR))<4){
                                    calCalculationInput.setText(tmpBuf.toString());
                                }else {
                                    calCalculationInput.setText(Utils.formatAmount(tmpBuf.toString()));
                                }
                            }
		                }
		 
		            } else {
		 
		                if (buttonPressed.equals(Consts.DECIMAL_SEPRATOR)) {
		                    // ERROR PREVENTION
		                    // This will avoid error if only the decimal is hit before an operator, by placing a leading zero
		                    // before the decimal
		                	calCalculationInput.setText(0 + buttonPressed);
		                } else {
		                	//calCalculationInput.setText(Utils.formatAmount(Utils.parseDouble(buttonPressed)));
                            calCalculationInput.setText(buttonPressed);
		                }
		 
		                userIsInTheMiddleOfTypingANumber = true;
		            }
		 
		        } else {
		            // operation was pressed
		            if (userIsInTheMiddleOfTypingANumber) {
		                calculator.setOperand(Utils.parseDouble(calCalculationInput.getText().toString()));
		                userIsInTheMiddleOfTypingANumber = false;
		            }
		 
		            calculator.performOperation(buttonPressed);
		            calCalculationInput.setText(df.format(calculator.getResult()));
		 
		        }
				
				switch(v.getId()){
					case R.id.cal_btn_cancel :
						break;
					case R.id.cal_btn_confirm :
						break;
					case R.id.cal_btn_insert :
						Toast.makeText(parentActivity, getResources().getString(R.string.insert_transaction_success),
								Toast.LENGTH_LONG).show();
//                        new insertTransInBackground().execute(genTransVo());
                        MonthlyBudgetVo vo = parentActivity.insertTrans(genTransVo());
                        updateBudgetBar(vo);
                        calCalculationInput.setText("");
                        ((EditText)rootView.findViewById(R.id.transaction_item_comment)).setText("");
                        v.requestFocus();
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

	private TransactionVo genTransVo(){
		TransactionVo transVo = null;
		
		try{
			double tranAmount = Utils.parseDouble(calCalculationInput.getText().toString());
			
			transVo = new TransactionVo();
			transVo.setTranDate(getCurrentDate());
			transVo.setTranCategoryId(getInputCategoryId());

			int tranEventId = parentActivity.getInputEventId();
			if(tranEventId!=EventVo.EMPTY_EVENT_ID){
				transVo.setTranEventId(tranEventId);
			}
			transVo.setTranAmount(tranAmount);
            transVo.setTranComment(((EditText)parentActivity.findViewById(R.id.transaction_item_comment)).getText().toString());
		}catch(Exception e){
			Log.d(TAG, "Fail : " + e.getMessage());
		}
		
		return transVo;
	}

    private class insertTransInBackground extends AsyncTask<TransactionVo, Void, MonthlyBudgetVo>{

        @Override
        protected MonthlyBudgetVo doInBackground(TransactionVo... vo){
            try {
                String now = vo[0].getTranDate();
                parentActivity.insertTrans(vo[0]);
                long monthlyBudget = parentActivity.getMonthlyBudget();
                long monthlyExpense = parentActivity.getExpenseByMonth(now);

                return new MonthlyBudgetVo(monthlyBudget, monthlyExpense);
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(MonthlyBudgetVo vo){
            super.onPostExecute(vo);

            if(vo != null) {
                updateBudgetBar(vo);
            }
        }
    }

    private String getCurrentDate(){
        TextView dateText = (TextView)rootView.findViewById(R.id.date_text);
        String now = dateText.getText().toString();

        return now;
    }

    private int getInputCategoryId(){
        CategoryVo categoryVo = (CategoryVo)categorySpinner.getSelectedItem();
        if (categoryVo == null) {
            return Consts.NO_CATEGORY_ID;
        } else {
            return categoryVo.getId();
        }
    }

}
