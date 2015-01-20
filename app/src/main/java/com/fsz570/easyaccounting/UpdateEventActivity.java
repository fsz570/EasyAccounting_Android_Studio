package com.fsz570.easyaccounting;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.fsz570.db_utils.DBAdapter;
import com.fsz570.easyaccounting.adapter.ListItemEventAdapter;
import com.fsz570.easyaccounting.vo.EventVo;

import java.util.List;

public class UpdateEventActivity extends Activity {

	private static final String TAG = "UpdateTransactionActivity";

	// Database
	DBAdapter dbAdapter = null;

	private ListView listView;
	private ListItemEventAdapter listItemAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_event);

		initDB();
		initUi();
	}
	
	private void initUi() {
		initListView();
	}

	// Init list view
	private void initListView() {

		listItemAdapter = new ListItemEventAdapter(this);
		listItemAdapter.setDataSource(getEventAllData(true));

		listView = (ListView) findViewById(R.id.update_event_list_view);
		listView.setAdapter(listItemAdapter);
	}
	
	
	
	public void dismissActivity(View v) {
	    finish();
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
		Log.d(TAG, "initDB() start");
		// Instant the DB Adapter will create the DB is it not exist.
		dbAdapter = new DBAdapter(UpdateEventActivity.this);

		// code that needs 6 seconds for execution
		try {
			dbAdapter.createDataBase();

		} catch (Exception e) {
			Log.d(TAG, "initDB() Exception");
			Log.d(TAG, e.getMessage());
		} finally {
			dbAdapter.close();
		}
		// after finishing, close the progress bar
		Log.d(TAG, "initDB() end.");
	}

	public DBAdapter getDbAdapter() {
		return dbAdapter;
	}

	public void setDbAdapter(DBAdapter dbAdapter) {
		this.dbAdapter = dbAdapter;
	}
	
	
}
