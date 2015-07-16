package com.fsz570.easyaccounting;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fsz570.db_utils.DBAdapter;
import com.fsz570.easyaccounting.util.Consts;
import com.fsz570.easyaccounting.util.Utils;


public class UpdateBudgetActivity extends Activity {

    private static final String TAG = "UpdateBudgetActivity";

    Button btnConfirm, btnCancel;
    EditText etMonthlyBidget;

    DBAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
//            Utils.enableStrictMode();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_budget);
    }

    @Override
    protected void onStart(){
        super.onStart();
        initDB();
    }

    @Override
    protected void onResume(){
        super.onResume();
        initUi();
    }

    @Override
    protected void onStop(){
        if(dbAdapter != null){
            dbAdapter.close();
        }
        super.onStop();
    }

    private void initDB(){
        Log.d(TAG, "initDB() start");
        //Instant the DB Adapter will create the DB is it not exist.
//        dbAdapter = new DBAdapter(this);
        dbAdapter = ((EasyMoneyApplication)getApplication()).getDbAdapterInstance();
        Log.d("DB_OPEN","dbAdapter opened? " + dbAdapter.isOpen());

//        // code that needs 6 seconds for execution
//        try{
//            dbAdapter.openDataBase();
//        }catch(Exception e){
//            Log.d(TAG, "initDB() Exception");
//            Log.d(TAG, e.getMessage());
//        }finally{
//            //dbAdapter.close();
//        }
//        // after finishing, close the progress bar
        Log.d(TAG, "initDB() end.");
    }

    private void initUi(){
        Log.d(TAG, "initUi()");

        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        etMonthlyBidget = (EditText)findViewById(R.id.et_monthly_budget);
        Log.d("DB_OPEN","dbAdapter opened? " + dbAdapter.isOpen());
        etMonthlyBidget.setText(""+dbAdapter.getMonthlyBudget());

        btnConfirm.setOnClickListener(clickListener);
        btnCancel.setOnClickListener(clickListener);
    }

    private Button.OnClickListener clickListener = new Button.OnClickListener(){

        @Override
        public void onClick(View v){
            switch(v.getId()){
                case R.id.btn_cancel :
                    finish();
                    break;
                case R.id.btn_confirm :
                    Log.d("DB_OPEN","dbAdapter opened? " + dbAdapter.isOpen());
                    dbAdapter.updateMonthlyBudget(getMonthlyBudget());
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.update_budget_success), Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    };

    private long getMonthlyBudget(){
        long monthlyBudget = 0;

        try{
            String monthlyBudgetStr = etMonthlyBidget.getText().toString();
            monthlyBudget = Utils.parseLong(monthlyBudgetStr);
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

        return monthlyBudget;
    }
}
