package com.fsz570.easyaccounting;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.fsz570.db_utils.DBAdapter;

public class EasyMoneyApplication extends Application{

    private static String TAG = "EasyMoneyApplication";
    private static DBAdapter dbAdapter = null;
    private static Context context = null;

    @Override
    public void onCreate()
    {
        Log.d(TAG, "onCreate()");
        super.onCreate();

        context = this;
    }

    public static synchronized DBAdapter getDbAdapterInstance(){
        Log.d(TAG, "getDbAdapterInstance()");

        if(dbAdapter == null) {
            Log.d(TAG, "create DBAdapter!");
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    dbAdapter = DBAdapter.getInstance(context);
                }
            });
            t.start();

            try {
                Log.d(TAG, "Before Join!");
                t.join();
                Log.d(TAG, "After Join!");
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        }

        return dbAdapter;
    }
}
