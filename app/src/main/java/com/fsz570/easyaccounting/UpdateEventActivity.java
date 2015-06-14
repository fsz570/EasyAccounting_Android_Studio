package com.fsz570.easyaccounting;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.fsz570.db_utils.DBAdapter;
import com.fsz570.easyaccounting.adapter.ListItemEventAdapter;
import com.fsz570.easyaccounting.vo.EventVo;

import java.util.List;

public class UpdateEventActivity extends Activity {

	private static final String TAG = "UpdateEventActivity";

	// Database
	DBAdapter dbAdapter = null;

	private ListView listView;
	private ListItemEventAdapter listItemAdapter;
    private EditText newEventEditText;

    InputMethodManager imm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
//            Utils.enableStrictMode();
        }
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_event);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
	
	private void initUi() {
		initListView();
	}

	// Init list view
	private void initListView() {

		listItemAdapter = new ListItemEventAdapter(this);

        //這種寫法在某些機型上不知為何會有問題
		//listItemAdapter.setDataSource(getEventAllData(true));
        listItemAdapter.setDataSource(getEventAllData(false));

		listView = (ListView) findViewById(R.id.update_event_list_view);
		listView.setAdapter(listItemAdapter);

        newEventEditText = (EditText) findViewById(R.id.new_event_text);
	}
	
	
	
	public void dismissActivity(View v) {
	    finish();
	}

    public void addNewTag(View v){
        if(newEventEditText.getText().length()>0) {
            dbAdapter.newEvent(newEventEditText.getText().toString().trim());
            listItemAdapter.setDataSource(getEventAllData(false));
            newEventEditText.getText().clear();
            imm.hideSoftInputFromWindow(newEventEditText.getWindowToken(), 0);
        }
    }

	public List<EventVo> getEventAllData(boolean addNewEventOnTop) {
				
		if(addNewEventOnTop){
			List<EventVo> eventVoList = dbAdapter.getEventAllData();
			eventVoList.add(0, new EventVo(EventVo.NEW_EVENT_ID, getResources().getString(R.string.edit_text_new_event), EventVo.ENABLED));
			
			return eventVoList;
		}else{
			return dbAdapter.getEventAllData();
		}

		
	}

	private void initDB() {
		Log.d(TAG, "initDB()");
		// Instant the DB Adapter will create the DB is it not exist.
//		dbAdapter = new DBAdapter(UpdateEventActivity.this);
        dbAdapter = ((EasyMoneyApplication)getApplication()).getDbAdapterInstance();
        Log.d("DB_OPEN","dbAdapter opened? " + dbAdapter.isOpen());

//		// code that needs 6 seconds for execution
//		try {
//            dbAdapter.openDataBase();
//		} catch (Exception e) {
//			Log.d(TAG, "initDB() Exception");
//			Log.d(TAG, e.getMessage());
//		} finally {
//			//dbAdapter.close();
//		}
//		// after finishing, close the progress bar
		Log.d(TAG, "initDB() end.");
	}

	public DBAdapter getDbAdapter() {
		return dbAdapter;
	}

}
