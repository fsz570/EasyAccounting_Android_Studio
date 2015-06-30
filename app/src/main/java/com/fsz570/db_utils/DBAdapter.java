package com.fsz570.db_utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.StrictMode;
import android.util.Log;

import com.fsz570.easyaccounting.R;
import com.fsz570.easyaccounting.util.Consts;
import com.fsz570.easyaccounting.util.Utils;
import com.fsz570.easyaccounting.vo.CategoryVo;
import com.fsz570.easyaccounting.vo.EventVo;
import com.fsz570.easyaccounting.vo.PieChartDataVo;
import com.fsz570.easyaccounting.vo.TransactionVo;

import java.util.ArrayList;
import java.util.List;

public class DBAdapter extends SQLiteOpenHelper {
	private static final String TAG = "DBAdapter";

	private static final String DB_NAME = "ez_account.db";
	private static final int DATABASE_VERSION = 2;
	
	private static final String TRANS_TABLE = "T_HIS_TRAN";
	private static final String EVENT_TABLE = "C_CFG_EVNT";
	private static final String CATEGORY_TABLE = "C_CFG_CTGR";
    private static final String PARAMETER_TABLE = "C_CFG_PARM";

	private final Context context;
	private SQLiteDatabase theDataBase;
    private static DBAdapter mInstance = null;

    private boolean isParamTableExist = false;

	public DBAdapter(Context _context) {
		super(_context, DB_NAME, null, DATABASE_VERSION);
		context = _context;
//		File outFile = context.getDatabasePath(DB_NAME);
//		DB_PATH = outFile.getPath() ;
//		Log.d(TAG, "DB_PATH : " + DB_PATH);
	}

	// Called when no database exists in
	// disk and the helper class needs
	// to create a new one.
	@Override
	public void onCreate(SQLiteDatabase _db) {
        Log.d(TAG, "onCreate()");

        _db.execSQL(createCategoryTableSql());
        _db.execSQL(createEventTableSql());
        _db.execSQL(createTransTableSql());
        _db.execSQL(createParamTableSql());
        addMonthlyBudgetParam(_db);
	}

	// Called when there is a database version mismatch meaning that
	// the version of the database on disk needs to be upgraded to
	// the current version.
	@Override
	public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
		// Log the version upgrade.
		Log.d(TAG, "Upgrading from version " + _oldVersion + " to "
				+ _newVersion + ", which will destroy all old data");
		// Upgrade the existing database to conform to the new version.
		// Multiple previous versions can be handled by comparing
		// _oldVersion and _newVersion values.
		// The simplest case is to drop the old table and create a
		// new one.

		// Create a new one.
//		 onCreate(_db);
//        if(_newVersion > _oldVersion){
//            if(_newVersion == 2 && _oldVersion == 1){
//                _db.execSQL(createParamTableSql());
//                addMonthlyBudgetParam(_db);
//            }
//        }

	}

    public static DBAdapter getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new DBAdapter(ctx.getApplicationContext());
        }
        return mInstance;
    }

    public boolean isOpen(){
        if(null == theDataBase || (null != theDataBase && !theDataBase.isOpen())){
            return false;
        }
        return true;
    }

    public void openDataBase() throws SQLException {
        // Open the database
        //String myPath = DB_PATH + DB_NAME;
//        String myPath = DB_PATH;

        //If null or not open
        if(null == theDataBase || (null != theDataBase && !theDataBase.isOpen())){
            //Trigger onCreate first
            Log.d(TAG, "openDataBase()");
            StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
                    .permitDiskReads()
                    .build());
            theDataBase = getWritableDatabase();
            StrictMode.setThreadPolicy(old);
        }
	}

	@Override
	public synchronized void close() {
		if (theDataBase != null)
			theDataBase.close();
		super.close();
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
//	public void createDataBase() throws IOException {
//        Log.d(TAG, "createDataBase()!");
//        boolean dbExist = false;
//
//		if (dbExist) {
//			// do nothing - database already exist
//			Log.d(TAG, "DB Exist!");
//		} else {
//			// By calling this method and empty database will be created into
//			// the default system path
//			// of your application so we are gonna be able to overwrite that
//			// database with our database.
//			this.getReadableDatabase();
//
//			try {
//				Log.d(TAG, "DB not exist! copy from assets.");
//				copyDataBase();
//			} catch (IOException e) {
//				throw new Error("Error copying database");
//			}
//		}
//	}


	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
//	public boolean checkDataBaseExist() {
//		SQLiteDatabase checkDB = null;
//		Log.d(TAG, "checkDataBaseExist()!");
//
//		try {
////			String myPath = DB_PATH + DB_NAME;
////			Log.d(TAG, "DB_PATH = " + DB_PATH);
////			Log.d(TAG, "myPath  = " + myPath);
//			checkDB = SQLiteDatabase.openDatabase(DB_PATH, null,
//					SQLiteDatabase.OPEN_READONLY);
//		} catch (SQLiteException e) {
//			// database does't exist yet.
//			Log.e(TAG, e.getMessage());
//		}
//
//		if (checkDB != null) {
//			Log.d(TAG, "DB Exists!");
//            checkDB.close();
//		}else{
//			Log.d(TAG, "DB not exists!");
//		}
//
//		return checkDB != null ? true : false;
//	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
//	private void copyDataBase() throws IOException {
//        Log.d(TAG, "copyDataBase()!");
//
//		InputStream myInput = null;
//		OutputStream myOutput = null;
//
//		// Open your local db as the input stream
//		try{
//			myInput = context.getAssets().open(DB_NAME);
//			// Path to the just created empty db
//			//String outFileName = DB_PATH + DB_NAME;
//			String outFileName = DB_PATH;
//			Log.d(TAG, "outFileName = " + outFileName);
//			// Open the empty db as the output stream
//			//Log.d(TAG, "External DIR : " + context.getExternalFilesDir(null).getAbsolutePath());
//			myOutput = new FileOutputStream(outFileName);
//			Log.d(TAG, "open output file!");
//
//			// transfer bytes from the input file to the output file
//			byte[] buffer = new byte[1024];
//			int length;
//			while ((length = myInput.read(buffer)) > 0) {
//				myOutput.write(buffer, 0, length);
//				Log.d(TAG, "write output file!");
//			}
//		}catch(Exception e){
//			Log.e(TAG, e.getMessage());
//		}finally{
//			// Close the streams
//			myOutput.flush();
//			myOutput.close();
//			myInput.close();
//		}
//
//	}

	//***************************************************	
	//                 Customizer method
	//***************************************************
	
	public List<EventVo> getEnabledEvents(){
		Log.d(TAG, "getEnabledEvents()!");
		
		List<EventVo> events = new ArrayList<EventVo>();
		
		events.add(new EventVo(EventVo.EMPTY_EVENT_ID, ""));
		
		openDataBase();
		
		Cursor cursor = theDataBase.rawQuery("select _id, event_name from C_CFG_EVNT where enabled = 1", null);
		
	    if ( cursor.moveToFirst () ) {
	    	Log.d(TAG, "Event count : " + cursor.getCount());
	        do {
				events.add(new EventVo(cursor.getInt(0), cursor.getString(1)));
	        } while (cursor.moveToNext());
	    }

	    // closing connection
	    cursor.close();
	    //close();
		
		return events;
	}
	
	public void updateEventName(int eventId, String newEventName){
		Log.d(TAG, "updateEventName()");
		
		ContentValues cv = new ContentValues();
		cv.put("event_name", newEventName);
		
		openDataBase();
		
		long rowId = theDataBase.update(EVENT_TABLE, cv, " _id = ? ", new String[]{String.valueOf(eventId)});
		Log.d(TAG, "rowId : " + rowId);

		//close();
	}
	
	public void deleteEvent(final int eventId){
                Log.d(TAG, "deleteEvent()");

                openDataBase();

                //Set transaction event id to null
                theDataBase.execSQL("UPDATE T_HIS_TRAN SET tran_event_id = NULL WHERE tran_event_id = ? ", new String[]{String.valueOf(eventId)});

                long rowId = theDataBase.delete(EVENT_TABLE, " _id = ? ", new String[]{String.valueOf(eventId)});
                Log.d(TAG, "rowId : " + rowId);
	}

    public void newEvent(final String newEventName) {

                Log.d(TAG, "newEvent()");

                ContentValues cv = new ContentValues();
                cv.put("event_name", newEventName);
                cv.put("enabled", EventVo.ENABLED);

                openDataBase();

                long rowId = theDataBase.insert(EVENT_TABLE, null, cv);
                Log.d(TAG, "rowId : " + rowId);
    }

    public int newParentCategory(String newCategoryName){
		Log.d(TAG, "newParentCategory()");

		ContentValues cv = new ContentValues();
		cv.put("tran_category_name",newCategoryName);
		cv.put("seq",getMaxSeq()+1);

		openDataBase();

		long rowId = theDataBase.insert(CATEGORY_TABLE, null, cv);
		Log.d(TAG, "rowId : " + rowId);

        return (int)rowId;
	}

    public void newChildCategory(final int parentId, final String newCategoryName) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "newChildCategory()");

                ContentValues cv = new ContentValues();
                cv.put("parent_id", parentId);
                cv.put("tran_category_name", newCategoryName);
                cv.put("seq", getMaxSeqInParentCategory(parentId) + 1);

                openDataBase();

                long rowId = theDataBase.insert(CATEGORY_TABLE, null, cv);
                Log.d(TAG, "rowId : " + rowId);
            }
        }).start();
    }

    public void updateCategory(final int id, final String newCategoryName){

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "updateCategory()");

                ContentValues cv = new ContentValues();
                cv.put("tran_category_name",newCategoryName);

                openDataBase();

                theDataBase.update(CATEGORY_TABLE, cv, " _id = ? ", new String[]{String.valueOf(id)});
            }
        }).start();

	}
	
	private int getMaxSeq(){
		int maxSeq = -1;
		
		openDataBase();
		
		Cursor cursor = theDataBase.rawQuery("select MAX(seq) from C_CFG_CTGR", null);
		
	    if ( cursor.moveToFirst () ) {
	    	maxSeq = cursor.getInt(0);
	    }

	    cursor.close();
	    //close();
		
		return maxSeq;
	}
	
	private int getMaxSeqInParentCategory(int parentId){
		int maxSeq = -1;
		
		openDataBase();
		
		Cursor cursor = theDataBase.rawQuery("select MAX(seq) from C_CFG_CTGR WHERE parent_id = ? ", new String[]{parentId+""});
		
	    if ( cursor.moveToFirst () ) {
	    	maxSeq = cursor.getInt(0);
	    }

	    cursor.close();
	    //close();
		
		return maxSeq;
	}
	
	public List<EventVo> getEventAllData(){
		Log.d(TAG, "getEventAllData()!");
		
		List<EventVo> events = new ArrayList<EventVo>();
		
		openDataBase();
		
		Cursor cursor = theDataBase.rawQuery("select _id, event_name, enabled from C_CFG_EVNT order by enabled desc, event_name", null);
		
	    if ( cursor.moveToFirst () ) {
	    	Log.d(TAG, "Event count : " + cursor.getCount());
	        do {
				events.add(new EventVo(cursor.getInt(0), cursor.getString(1), cursor.getInt(2)));
	        } while (cursor.moveToNext());
	    }

	    // closing cursor
	    cursor.close();

		return events;
	}
	
	public void setEventEnable(final int eventId, final int enabled){

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "setEventEnable()");

                ContentValues cv = new ContentValues();
                cv.put("enabled",enabled);

                openDataBase();

                long rowId = theDataBase.update(EVENT_TABLE, cv, " _id = ? ", new String[]{String.valueOf(eventId)});
                Log.d(TAG, "rowId : " + rowId);
            }
        }).start();
	}

	public ArrayList<CategoryVo> getCategories(){
		Log.d(TAG, "getCategories()!");

        ArrayList<CategoryVo> categoryList = new ArrayList<CategoryVo>();
		
		openDataBase();
		Cursor cursor = null;
		Cursor cursorChild = null;
		
		cursor = theDataBase.rawQuery("SELECT _id, parent_id, tran_category_name, category_icon_id, seq FROM C_CFG_CTGR WHERE parent_id IS NULL order by seq", new String[]{});
		
	    if ( cursor.moveToFirst () ) {
	        do {
//	        	Log.d(TAG, "Category : " + cursor.getString(2) + "; Seq : " + cursor.getInt(4));
	        	CategoryVo parentCategoryVo = new CategoryVo(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
	        	categoryList.add(parentCategoryVo);
	        	
	        	cursorChild = theDataBase.rawQuery("SELECT _id, parent_id, tran_category_name, category_icon_id, seq FROM C_CFG_CTGR WHERE parent_id = ? order by seq", new String[]{cursor.getString(0)});
	        	if ( cursorChild.moveToFirst () ) {
			        do {
			        	parentCategoryVo.addChildCategory(new CategoryVo(cursorChild.getInt(0), cursorChild.getInt(1), cursorChild.getString(2), cursorChild.getString(3), cursorChild.getInt(4)));
			        } while (cursorChild.moveToNext());
			    }
	        	cursorChild.close();
	        } while (cursor.moveToNext());
	    }else{
	    	return categoryList;
	    }

	    // closing cursor
	    cursor.close();

		return categoryList;
	}
	
	public List<CategoryVo> getCommonCategories(int rowNums){
		Log.d(TAG, "getCommonCategories() : " + rowNums + " row");
		
		List<CategoryVo> listCategory = new ArrayList<CategoryVo>();
		openDataBase();
		Cursor cursor = null;
		
		cursor = theDataBase.rawQuery(getCommonCategorySql(), new String[]{rowNums+""});
		if ( cursor.moveToFirst () ) {
			do {
				listCategory.add(new CategoryVo(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4)));
	        } while (cursor.moveToNext());
		}
		
	    // closing cursor
	    cursor.close();

	    return listCategory;
	}
	
	public List<TransactionVo> getTransactions(String startDate, String endDate, int eventId, int categoryId){

		List<TransactionVo> trans = new ArrayList<TransactionVo>();
		String fStartDate = Utils.transferDateFormatForSqlite(startDate, context);
		String fEndDate = Utils.transferDateFormatForSqlite(endDate, context);
		openDataBase();
		
		Cursor cursor;
		if(eventId == Consts.NO_EVENT_ID && categoryId == Consts.NO_CATEGORY_ID){
			cursor = theDataBase.rawQuery(getQueryTransactionString(eventId, categoryId), new String[] {fStartDate, fEndDate});
		}else if(eventId != Consts.NO_EVENT_ID  && categoryId == Consts.NO_CATEGORY_ID){
			cursor = theDataBase.rawQuery(getQueryTransactionString(eventId, categoryId), new String[] {fStartDate, fEndDate, eventId+""});
		}else if(eventId == Consts.NO_EVENT_ID && categoryId != Consts.NO_CATEGORY_ID){
			cursor = theDataBase.rawQuery(getQueryTransactionString(eventId, categoryId), new String[] {fStartDate, fEndDate, categoryId+"", categoryId+""});
		}else{
			cursor = theDataBase.rawQuery(getQueryTransactionString(eventId, categoryId), new String[] {fStartDate, fEndDate, eventId+"", categoryId+"", categoryId+""});
		}
		
	    if ( cursor.moveToFirst () ) {
	    	Log.d(TAG, "TransactionVo count : " + cursor.getCount());
	        do {
	        	trans.add(new TransactionVo(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getDouble(4),cursor.getString(5),cursor.getString(6),cursor.getString(7)));
	        } while (cursor.moveToNext());
	    }

	    // closing cursor
	    cursor.close();

		return trans;
	}
	
	public List<PieChartDataVo> getTransactionsForChart(String startDate, String endDate, int eventId, int categoryId){
		Log.d(TAG, "getTransactionsForChart()!");
		
		List<PieChartDataVo> trans = new ArrayList<PieChartDataVo>();
		String fStartDate = Utils.transferDateFormatForSqlite(startDate, context);
		String fEndDate = Utils.transferDateFormatForSqlite(endDate, context);
		openDataBase();
		
		Cursor cursor;
        String params[];

		if(eventId == Consts.NO_EVENT_ID && categoryId == Consts.NO_CATEGORY_ID){
            params = new String[] {fStartDate, fEndDate};
		}else if(eventId != Consts.NO_EVENT_ID && categoryId == Consts.NO_CATEGORY_ID){
            params = new String[] {fStartDate, fEndDate, eventId+""};
		}else if(eventId == Consts.NO_EVENT_ID && categoryId != Consts.NO_CATEGORY_ID){
            params = new String[] {fStartDate, fEndDate, categoryId+"", categoryId+""};
		}else{
            params = new String[] {fStartDate, fEndDate, eventId+"", categoryId+"", categoryId+""};
		}

        cursor = theDataBase.rawQuery(getQueryTransactionForChartString(true, eventId, categoryId), params);

	    if ( cursor.moveToFirst () ) {
	    	Log.d(TAG, "BarChartDataVo count : " + cursor.getCount());
	        do {
	        	trans.add(new PieChartDataVo(cursor.getString(0), cursor.getFloat(1)));
	        } while (cursor.moveToNext());
	    }

	    // closing cursor
	    cursor.close();

		return trans;
	}
	
	public List<PieChartDataVo> getAllTransactionsForChart(int eventId, int categoryId){
		Log.d(TAG, "getAllTransactionsForChart()!");
		
		List<PieChartDataVo> trans = new ArrayList<PieChartDataVo>();
		
		openDataBase();
		
		Cursor cursor;
        String params[];

		if(eventId == Consts.NO_EVENT_ID && categoryId == Consts.NO_CATEGORY_ID){
            params =  new String[] {};
		}else if(eventId != Consts.NO_EVENT_ID && categoryId == Consts.NO_CATEGORY_ID){
            params =  new String[] {eventId+""};
		}else if(eventId == Consts.NO_EVENT_ID && categoryId != Consts.NO_CATEGORY_ID){
            params =  new String[] {categoryId+"", categoryId+""};
		}else{
            params = new String[] {eventId+"", categoryId+"", categoryId+""};
		}

        cursor = theDataBase.rawQuery(getQueryTransactionForChartString(false, eventId, categoryId), params);

	    if ( cursor.moveToFirst () ) {
	    	Log.d(TAG, "BarChartDataVo count : " + cursor.getCount());
	        do {
	        	trans.add(new PieChartDataVo(cursor.getString(0), cursor.getFloat(1)));
	        } while (cursor.moveToNext());
	    }

	    // closing cursor
	    cursor.close();

		return trans;
	}
	
	public List<TransactionVo> getTransactionsWithCondition(int eventId, int categoryId){
		Log.d(TAG, "getTransactions()!");
		
		List<TransactionVo> trans = new ArrayList<TransactionVo>();
		
		openDataBase();
		
		Cursor cursor;
		
		if(eventId == Consts.NO_EVENT_ID && categoryId == Consts.NO_CATEGORY_ID){
			cursor = theDataBase.rawQuery(getQueryTransactionWithConditionString(eventId, categoryId), new String[] {});
		}else if(eventId != Consts.NO_EVENT_ID && categoryId == Consts.NO_CATEGORY_ID){
			cursor = theDataBase.rawQuery(getQueryTransactionWithConditionString(eventId, categoryId), new String[] {eventId+""});
		}else if(eventId == Consts.NO_EVENT_ID && categoryId != Consts.NO_CATEGORY_ID){
			cursor = theDataBase.rawQuery(getQueryTransactionWithConditionString(eventId, categoryId), new String[] {categoryId+"", categoryId+""});
		}else{
			cursor = theDataBase.rawQuery(getQueryTransactionWithConditionString(eventId, categoryId), new String[] {eventId+"",categoryId+"",categoryId+""});
		}
		
	    if ( cursor.moveToFirst () ) {
	    	Log.d(TAG, "TransactionVo count : " + cursor.getCount());
	        do {
	        	trans.add(new TransactionVo(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getDouble(4),cursor.getString(5),cursor.getString(6),cursor.getString(7)));
	        } while (cursor.moveToNext());
	    }

	    // closing cursor
	    cursor.close();

		return trans;
	}
	
	public void insertTrans(final TransactionVo transVo){
                Log.d(TAG, "insertTrans()");

                ContentValues cv = new ContentValues();
                cv.put("tran_date", Utils.transferDateFormatForSqlite(transVo.getTranDate(), context));
                cv.put("tran_category_id",transVo.getTranCategoryId());
                cv.put("tran_event_id",transVo.getTranEventId());
                cv.put("tran_amount",transVo.getTranAmount());
                cv.put("tran_comment",transVo.getTranComment());

                openDataBase();

                long rowId = theDataBase.insertOrThrow(TRANS_TABLE, null, cv);
                Log.d(TAG, "rowId : " + rowId);

	}

    public void updateTrans(final TransactionVo transVo) {
        Log.d(TAG, "updateTrans()");

        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentValues cv = new ContentValues();
                cv.put("tran_date", Utils.transferDateFormatForSqlite(transVo.getTranDate(), context));
                cv.put("tran_category_id", transVo.getTranCategoryId());
                cv.put("tran_event_id", transVo.getTranEventId());
                cv.put("tran_amount", transVo.getTranAmount());
                cv.put("tran_comment", transVo.getTranComment());

                openDataBase();

                long rowId = theDataBase.update(TRANS_TABLE, cv, " _id = ? ", new String[]{transVo.getId() + ""});
                Log.d(TAG, "rowId : " + rowId);
            }
        }).start();

        //close();
    }

    private String getQueryTransactionString(int eventId, int categoryId){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT TRAN._id, TRAN.tran_date, TRAN.tran_category_id, TRAN.tran_event_id, ");
		sb.append("       TRAN.tran_amount, TRAN.tran_comment, CTGR.tran_category_name, EVNT.event_name");
		sb.append("  FROM T_HIS_TRAN TRAN ");
		sb.append(" INNER JOIN C_CFG_CTGR CTGR ON TRAN.tran_category_id = CTGR._id");
		sb.append(" LEFT OUTER JOIN C_CFG_EVNT EVNT ON TRAN.tran_event_id = EVNT._id");
		sb.append(" WHERE TRAN.tran_date BETWEEN ? AND ? ");
		
		if(eventId != Consts.NO_EVENT_ID){
			sb.append(" AND TRAN.tran_event_id = ? ");
		}
		
		if(categoryId != Consts.NO_CATEGORY_ID){
			sb.append(" AND ( TRAN.tran_category_id = ? OR CTGR.parent_id = ? ) ");
		}

        sb.append(" ORDER BY TRAN.tran_date ");
		
		return sb.toString();
	}

    private String getQueryTransactionForChartString(boolean withDateClause, int eventId, int categoryId){

        if (categoryId == Consts.NO_CATEGORY_ID ){
            return getQueryTransactionForChartWithoutCategoryString(withDateClause, eventId);
        }else{
            return getQueryTransactionForChartWithCategoryString(withDateClause, eventId, categoryId);
        }
    }
	
	private String getQueryTransactionForChartWithCategoryString(boolean withDateClause, int eventId, int categoryId){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT CTGR.tran_category_name, SUM(TRAN.tran_amount)");
		sb.append("  FROM T_HIS_TRAN TRAN ");
		sb.append(" INNER JOIN C_CFG_CTGR CTGR ON TRAN.tran_category_id = CTGR._id");
		sb.append(" LEFT OUTER JOIN C_CFG_EVNT EVNT ON TRAN.tran_event_id = EVNT._id");
		
		if(withDateClause || eventId != Consts.NO_EVENT_ID){
			sb.append(" WHERE 1=1 ");
		}
		if(withDateClause){
			sb.append(" AND TRAN.tran_date BETWEEN ? AND ? ");
		}
		if(eventId != Consts.NO_EVENT_ID){
			sb.append(" AND TRAN.tran_event_id = ? ");
		}
		sb.append(" AND ( TRAN.tran_category_id = ? OR CTGR.parent_id = ? ) ");
		sb.append(" GROUP BY CTGR.tran_category_name");
		
		return sb.toString();
	}

    private String getQueryTransactionForChartWithoutCategoryString(boolean withDateClause, int eventId){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT TOP_CTGR.tran_category_name, SUM(DATA.tran_amount)");
        sb.append("  FROM C_CFG_CTGR TOP_CTGR, ");
        //Sub query
        sb.append("( SELECT ifnull(CTGR.parent_id, CTGR._id) AS id, SUM(TRAN.tran_amount) AS tran_amount ");
        sb.append("    FROM T_HIS_TRAN TRAN ");
        sb.append("   INNER JOIN C_CFG_CTGR CTGR ON TRAN.tran_category_id = CTGR._id ");
        sb.append("    LEFT OUTER JOIN C_CFG_EVNT EVNT ON TRAN.tran_event_id = EVNT._id ");

        if(withDateClause || eventId != Consts.NO_EVENT_ID){
            sb.append(" WHERE 1=1 ");
        }
        if(withDateClause){
            sb.append(" AND TRAN.tran_date BETWEEN ? AND ? ");
        }
        if(eventId != Consts.NO_EVENT_ID){
            sb.append(" AND TRAN.tran_event_id = ? ");
        }
        sb.append(" GROUP BY CTGR.tran_category_name ) AS DATA ");
        //Sub query end
        sb.append("WHERE TOP_CTGR._id = DATA.id ");
        sb.append(" GROUP BY TOP_CTGR.tran_category_name ");

        return sb.toString();
    }
	
	private String getQueryTransactionWithConditionString(int eventId, int categoryId){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT TRAN._id, TRAN.tran_date, TRAN.tran_category_id, TRAN.tran_event_id, ");
		sb.append("       TRAN.tran_amount, TRAN.tran_comment, CTGR.tran_category_name, EVNT.event_name");
		sb.append("  FROM T_HIS_TRAN TRAN ");
		sb.append(" INNER JOIN C_CFG_CTGR CTGR ON TRAN.tran_category_id = CTGR._id");
		sb.append(" LEFT OUTER JOIN C_CFG_EVNT EVNT ON TRAN.tran_event_id = EVNT._id");
		
		if(eventId != Consts.NO_EVENT_ID){
			sb.append(" AND TRAN.tran_event_id = ? ");
		}
		
		if(categoryId != Consts.NO_CATEGORY_ID){
			sb.append(" AND ( TRAN.tran_category_id = ? OR CTGR.parent_id = ? ) ");
		}
		
		return sb.toString();
	}
	
	public void deleteTransaction(final TransactionVo transVo){
        new Thread(new Runnable() {
            @Override
            public void run() {
                openDataBase();
                theDataBase.delete("T_HIS_TRAN", " _id = ? ", new String[]{transVo.getId() + ""});
            }
        }).start();
	}
	
	public void updateCategory(final int id, final int seq){

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "updateCategory()");

                ContentValues cv = new ContentValues();
                cv.put("seq", seq);

                openDataBase();

                long rowId = theDataBase.update(CATEGORY_TABLE, cv, " _id = ? ", new String[]{id+""});
            }
        }).start();
	}
	
	private String getCommonCategorySql() {
		StringBuffer sb = new StringBuffer();

		sb.append("SELECT CTGR._id, CTGR.parent_id, CTGR.tran_category_name, CTGR.category_icon_id, CTGR.seq  ");
		sb.append("  FROM C_CFG_CTGR CTGR, ");
		sb.append("       ( SELECT tran_category_id, count(tran_category_id) ");
		sb.append("           FROM (SELECT tran_category_id ");
		sb.append("                   FROM T_HIS_TRAN  ");
		sb.append("               ORDER BY tran_date DESC LIMIT ? ) ");
		sb.append("       GROUP BY tran_category_id ");
		sb.append("       ORDER BY 2 DESC LIMIT 10) CMN ");
		sb.append("WHERE CTGR._id = CMN.tran_category_id ");

		return sb.toString();
	}

    private String createCategoryTableSql(){
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(CATEGORY_TABLE).append("(")
                .append(" _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ")
                .append(" parent_id INTEGER, ")
                .append(" tran_category_name TEXT NOT NULL, ")
                .append(" category_icon_id TEXT, ")
                .append(" seq INTEGER ")
                .append(")");
        return sb.toString();
    }

    private String createEventTableSql(){
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(EVENT_TABLE).append("(")
                .append(" _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ")
                .append(" event_name TEXT NOT NULL, ")
                .append(" enabled INTEGER NOT NULL DEFAULT 1 ")
                .append(")");
        return sb.toString();
    }

    private String createTransTableSql(){
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(TRANS_TABLE).append("(")
                .append(" _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ")
                .append(" tran_date TEXT NOT NULL, ")
                .append(" tran_category_id INTEGER NOT NULL, ")
                .append(" tran_event_id INTEGER, ")
                .append(" tran_amount REAL NOT NULL, ")
                .append(" tran_comment TEXT ")
                .append(")");
        return sb.toString();
    }

    private String createParamTableSql(){
        StringBuffer sb = new StringBuffer();

        sb.append("CREATE TABLE IF NOT EXISTS ").append(PARAMETER_TABLE).append("(")
                .append(" _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ")
                .append(" parameter_name TEXT NOT NULL, ")
                .append(" parameter_value TEXT NOT NULL ")
                .append(")");
        return sb.toString();
    }

    private void addMonthlyBudgetParam(final SQLiteDatabase _db){

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "addMonthlyBudgetParam()");

                ContentValues cv = new ContentValues();
                cv.put("parameter_name",Consts.PARAM_MONTHLY_BUDGET_NAME);
                cv.put("parameter_value","0");

                long rowId = _db.insert(PARAMETER_TABLE, null, cv);
                Log.d(TAG, "rowId : " + rowId);
            }
        }).start();

    }

    public long getMonthlyBudget(){
        Log.d(TAG, "getMonthlyBudget()");
        long monthlyBudget = 0;

        openDataBase();
        Cursor cursor = theDataBase.rawQuery(getMonthlyBudgetSql(), new String[]{Consts.PARAM_MONTHLY_BUDGET_NAME});

        if ( cursor.moveToFirst () ) {
            do {
                try
                {
                    monthlyBudget = cursor.getLong(0);
                }catch(Exception e){
                    //Nothing
                }
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();

        return monthlyBudget;
    }

    public void updateMonthlyBudget(final long monthlyBudget){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "updateMonthlyBudget()");

                openDataBase();
                if(!isTableParamExists()){
                    theDataBase.execSQL(createParamTableSql());
                    addMonthlyBudgetParam(theDataBase);
                    isParamTableExist = true;
                }

                ContentValues cv = new ContentValues();
                cv.put("parameter_value",monthlyBudget);

                theDataBase.update(PARAMETER_TABLE, cv, " parameter_name = ? ", new String[]{Consts.PARAM_MONTHLY_BUDGET_NAME});
            }
        }).start();

    }

    private String getMonthlyBudgetSql(){
        StringBuffer sb = new StringBuffer();

        sb.append("SELECT parameter_value FROM ").append(PARAMETER_TABLE)
                .append(" WHERE parameter_name = ? ");

        return sb.toString();
    }

    public long getExpenseByMonth(String now){
        Log.d(TAG, "getMonthlyBudget()");
        long monthlyExpense = 0;
		String fNow = Utils.transferDateFormatForSqlite(now, context);
        openDataBase();
        Cursor cursor = theDataBase.rawQuery(getExpenseByMonthSql(), new String[]{fNow,fNow});

        if ( cursor.moveToFirst () ) {
            do {
                try
                {
                    monthlyExpense = cursor.getLong(0);
                }catch(Exception e){
                    //Nothing
                }
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();

        return monthlyExpense;
    }

    private String getExpenseByMonthSql(){
        StringBuffer sb = new StringBuffer();

        sb.append("SELECT SUM(tran_amount) FROM ").append(TRANS_TABLE)
                .append(" WHERE tran_date between date(?,'start of month') ")
                .append(" AND date(?,'start of month','+1 month','-1 day')");

        return sb.toString();
    }

    public void initDefaultCategory(){
        int pIdCatFood = newParentCategory(context.getResources().getString(R.string.cat_food));
        newChildCategory(pIdCatFood, context.getResources().getString(R.string.cat_food_dine_out));
        newChildCategory(pIdCatFood, context.getResources().getString(R.string.cat_food_food_ingredients));
        newChildCategory(pIdCatFood, context.getResources().getString(R.string.cat_food_drinks));

        long pIdCatClothing = newParentCategory(context.getResources().getString(R.string.cat_clothing));
        long pIdCatHousing = newParentCategory(context.getResources().getString(R.string.cat_housing));
        long pIdCatTraffic = newParentCategory(context.getResources().getString(R.string.cat_traffic));
        long pIdCatRecation = newParentCategory(context.getResources().getString(R.string.cat_recreation));
    }

    public boolean isTableParamExists() {
        if (isParamTableExist == false ) {
            StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
                    .permitDiskReads()
                    .build());
            openDataBase();
            Cursor cursor = theDataBase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + PARAMETER_TABLE + "'", null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.close();
                    return true;
                }
                cursor.close();
            }
            StrictMode.setThreadPolicy(old);
        }
        return false;
    }

	public CategoryVo getCategoryById(int categoryId){
		openDataBase();
		Cursor cursor = null;
		CategoryVo categoryVo = null;

		cursor = theDataBase.rawQuery("SELECT _id, parent_id, tran_category_name, category_icon_id, seq FROM C_CFG_CTGR WHERE _id = ? ", new String[]{categoryId+""});

		if ( cursor.moveToFirst () ) {
			categoryVo = new CategoryVo(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
		}

		// closing cursor
		cursor.close();
		return categoryVo;
	}

	public int removeCategory(int categoryId) {
		int trxCountInAllDeletedCategory = 0;
		openDataBase();

		List<String> removedCategoryIdList = new ArrayList<String>();
		Cursor cursor = theDataBase.rawQuery("SELECT _id FROM C_CFG_CTGR WHERE _id = ? or parent_id = ?", new String[]{categoryId + "", categoryId + ""});
		if ( cursor.moveToFirst () ) {
			do {
				removedCategoryIdList.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		cursor.close();

		for(String removedCategoryId:removedCategoryIdList) {
			cursor = theDataBase.rawQuery("SELECT COUNT(1) FROM T_HIS_TRAN WHERE tran_category_id = ?", new String[]{removedCategoryId});
			cursor.moveToFirst();
			int trxCountInDeletedCategory = cursor.getInt(0);
			cursor.close();

			if (trxCountInDeletedCategory > 0) {
				CategoryVo categoryDefault = findOrCreateCategoryDefault();
				if (categoryDefault != null) {
					theDataBase.execSQL("UPDATE T_HIS_TRAN SET tran_category_id = ? WHERE tran_category_id = ?", new String[]{categoryDefault.getId() + "", removedCategoryId});
				}
			}
			trxCountInAllDeletedCategory += trxCountInDeletedCategory;
			theDataBase.execSQL("DELETE FROM C_CFG_CTGR WHERE _id = ? ", new String[]{removedCategoryId});
		}
		return trxCountInAllDeletedCategory;
	}

	public CategoryVo findOrCreateCategoryDefault(){
		openDataBase();
		Cursor cursor = null;
		CategoryVo categoryVo = null;

		cursor = theDataBase.rawQuery("SELECT _id, parent_id, tran_category_name, category_icon_id, seq FROM C_CFG_CTGR WHERE tran_category_name = ? ", new String[]{context.getString(R.string.default_category)});

		if ( cursor.moveToFirst () ) {
			categoryVo = new CategoryVo(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
		}

		if(categoryVo == null){
			newParentCategory(context.getString(R.string.default_category));
			cursor = theDataBase.rawQuery("SELECT _id, parent_id, tran_category_name, category_icon_id, seq FROM C_CFG_CTGR WHERE tran_category_name = ? ", new String[]{context.getString(R.string.default_category)});

			if ( cursor.moveToFirst () ) {
				categoryVo = new CategoryVo(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
			}
		}

		// closing cursor
		cursor.close();
		return categoryVo;
	}
}
