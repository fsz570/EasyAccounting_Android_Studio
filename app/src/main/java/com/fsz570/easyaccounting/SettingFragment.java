package com.fsz570.easyaccounting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fsz570.easyaccounting.util.Consts;

public class SettingFragment extends Fragment {

	private static final String TAG = "SettingFragment";
	
	private View rootView;
	private AccountingActivity parentActivity;
	
	private Button eventBtn;
	private Button categoryBtn;
    private Button rateBtn;
    private Button budgetBtn;
//    private Button helpBtn;
    private LinearLayout eventLayout;
    private LinearLayout categoryLayout;
    private LinearLayout rateLayout;
    private LinearLayout budgetLayout;
//    private LinearLayout helpLayout;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
    	parentActivity = ((AccountingActivity)getActivity());
        rootView = inflater.inflate(R.layout.fragment_setting, container, false);
         
        initUi();

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        initUi();
    }
    
    
    private void initUi(){
    	eventBtn = (Button) rootView.findViewById(R.id.setting_btn_event);
    	categoryBtn = (Button) rootView.findViewById(R.id.setting_btn_category);
        rateBtn = (Button) rootView.findViewById(R.id.setting_btn_rate);
        budgetBtn = (Button) rootView.findViewById(R.id.setting_btn_budget);
//        helpBtn = (Button) rootView.findViewById(R.id.setting_btn_help);

        eventLayout = (LinearLayout) rootView.findViewById(R.id.setting_layout_event);
        categoryLayout = (LinearLayout) rootView.findViewById(R.id.setting_layout_category);
        rateLayout = (LinearLayout) rootView.findViewById(R.id.setting_layout_rate);
        budgetLayout = (LinearLayout) rootView.findViewById(R.id.setting_layout_budget);
//        helpLayout = (LinearLayout) rootView.findViewById(R.id.setting_layout_help);
    	
    	eventBtn.setOnClickListener(clickListener);
        categoryBtn.setOnClickListener(clickListener);
        rateBtn.setOnClickListener(clickListener);
        budgetBtn.setOnClickListener(clickListener);
//        helpBtn.setOnClickListener(clickListener);

        eventLayout.setOnClickListener(clickListener);
        categoryLayout.setOnClickListener(clickListener);
        rateLayout.setOnClickListener(clickListener);
        budgetLayout.setOnClickListener(clickListener);
//        helpLayout.setOnClickListener(clickListener);
    }
    
	// Click Listener for all buttons
	private View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
				case R.id.setting_btn_event:
                    eventLayout.setPressed(true);
                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            eventLayout.performClick();
                        }
                    }, 100);
                    break;
                case R.id.setting_layout_event:
					Log.d(TAG, "start UpdateEventActivity");
					intent = new Intent(parentActivity, UpdateEventActivity.class);

					startActivity(intent);
					break;
				case R.id.setting_btn_category:
                    categoryLayout.setPressed(true);
                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            categoryLayout.performClick();
                        }
                    }, 100);
                    break;
                case R.id.setting_layout_category:
					Log.d(TAG, "start UpdateCategoryActivity");
					intent = new Intent(parentActivity, UpdateCategoryActivity.class);

					startActivity(intent);
				    break;
                case R.id.setting_btn_rate:
                    rateLayout.setPressed(true);
                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rateLayout.performClick();
                        }
                    }, 100);
                    break;
                case R.id.setting_layout_rate:
                    Log.d(TAG, "start Google play activity");
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=com.fsz570.easyaccounting"));
                    startActivity(intent);
                    break;
                case R.id.setting_btn_budget:
                    budgetLayout.setPressed(true);
                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            budgetLayout.performClick();
                        }
                    }, 100);
                    break;
                case R.id.setting_layout_budget:
                    intent = new Intent(parentActivity, UpdateBudgetActivity.class);

                    startActivityForResult(intent, Consts.ACTIVITY_REQUEST_CODE_FOR_UPDATE_BUDGET);
                    break;
//                case R.id.setting_btn_help:
//                    helpLayout.setPressed(true);
//                    (new Handler()).postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            helpLayout.performClick();
//                        }
//                    }, 100);
//                    break;
//                case R.id.setting_layout_help:
//                    Log.d(TAG, "start Help activity");
//                    intent = new Intent(parentActivity, HelpActivity.class);
//
//                    startActivity(intent);
//                    break;
			}
		}
	};

}
