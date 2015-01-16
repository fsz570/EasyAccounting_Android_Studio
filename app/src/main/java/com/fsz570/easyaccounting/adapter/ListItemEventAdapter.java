package com.fsz570.easyaccounting.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fsz570.easyaccounting.R;
import com.fsz570.easyaccounting.UpdateEventActivity;
import com.fsz570.easyaccounting.vo.EventVo;

import java.util.ArrayList;
import java.util.List;

public class ListItemEventAdapter extends BaseAdapter {
	
	private static final String TAG = "ListItemEventAdapter";
	private LayoutInflater mLayoutInflater;
	
	private List<EventVo> mList;
	private UpdateEventActivity parentActivity;
	private EventVo editingEventVo = null;
	private EditText editingEditText = null;
	InputMethodManager imm;
	
	public ListItemEventAdapter(Context mContext){
        mLayoutInflater = LayoutInflater.from(mContext);
        parentActivity = (UpdateEventActivity)mContext;
        imm = (InputMethodManager) parentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        
        mList = new ArrayList<EventVo>();
	}

	@Override
	public int getCount() {

		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ListItemEventHolder holder;
		
		Log.d(TAG, "getView()");
		
		if(null == view){
            view = mLayoutInflater.inflate(R.layout.list_item_view_event, null);
            holder = new ListItemEventHolder();
            holder.setEnableButton((ToggleButton) view.findViewById(R.id.toggle_button_event_enable));
            holder.setEventNameTextView((TextView) view.findViewById(R.id.text_view_event_name));
            holder.setEventNameEditText((EditText) view.findViewById(R.id.edit_text_event_name));
            holder.setCancelButton((ImageButton) view.findViewById(R.id.btn_event_cancel));
            holder.setConfirmButton((ImageButton) view.findViewById(R.id.btn_event_confirm));
            
            view.setTag(holder);
        }else{
            holder = (ListItemEventHolder) view.getTag();
        }
		
		Log.d(TAG, "position : " + position);
		EventVo tmp = (EventVo)mList.get(position);
		
		if(tmp.getId() == EventVo.NEW_EVENT_ID){
			holder.getEnableButton().setVisibility(View.GONE);
		}else{
			Log.d(TAG, "tmp.isEnabled() : " + tmp.isEnabled());
			holder.getEnableButton().setChecked(tmp.isEnabled());
			holder.getEnableButton().setTag(tmp);
			holder.getEnableButton().setOnCheckedChangeListener(checkedChangeListener);
		}
		
		if (tmp.isEditing()) {
			holder.getEventNameTextView().setVisibility(View.GONE);
			
			if(tmp.getId() == EventVo.NEW_EVENT_ID){
				holder.getEventNameEditText().setText("");
			}else{
				holder.getEventNameEditText().setText(tmp.getEventName());
			}
			holder.getEventNameEditText().setTag(tmp);
			holder.getEventNameEditText().setOnClickListener(clickListener);
			holder.getEventNameEditText().setEnabled(true);
			holder.getEventNameEditText().setVisibility(View.VISIBLE);
			editingEditText = holder.getEventNameEditText();
//			imm.showSoftInput(editingEditText, InputMethodManager.SHOW_IMPLICIT);

            editingEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus) {
                        editingEditText.post(new Runnable() {
                            @Override
                            public void run() {
                                imm.showSoftInput(editingEditText, InputMethodManager.SHOW_IMPLICIT);
                                //Set cursor at the end
                                editingEditText.setSelection(editingEditText.getText().length());
                            }
                        });
                    }
                }
            });
            editingEditText.requestFocus();
			
			holder.getCancelButton().setTag(tmp);
			holder.getCancelButton().setOnClickListener(clickListener);
			holder.getCancelButton().setEnabled(true);
			holder.getCancelButton().setVisibility(View.VISIBLE);
			holder.getCancelButton().setFocusable(false);
			
			holder.getConfirmButton().setTag(tmp);
			holder.getConfirmButton().setOnClickListener(clickListener);
			holder.getConfirmButton().setEnabled(true);
			holder.getConfirmButton().setVisibility(View.VISIBLE);
			holder.getConfirmButton().setFocusable(false);
		} else {
			holder.getEventNameEditText().setVisibility(View.GONE);
			holder.getCancelButton().setVisibility(View.GONE);
			holder.getConfirmButton().setVisibility(View.GONE);
			
			holder.getEventNameTextView().setText(tmp.getEventName());
			holder.getEventNameTextView().setTag(tmp);
			holder.getEventNameTextView().setOnClickListener(clickListener);
			holder.getEventNameTextView().setVisibility(View.VISIBLE);
		}
		return view;
	}
	
	// Click Listener for all buttons
	private View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.text_view_event_name: 
				Log.d(TAG, "onClick editing()");
				
				//Release last editingEventVo
				if(editingEventVo != null){
					editingEventVo.setEditing(false);
				}
				
				//Set current editingEventVo
				editingEventVo = (EventVo)v.getTag();
				editingEventVo.setEditing(true);
//
//                RelativeLayout viewParent = (RelativeLayout)v.getParent();
//                ToggleButton enableButton =  (ToggleButton)viewParent.findViewById(R.id.toggle_button_event_enable);
//                TextView eventNameTextView =(TextView) viewParent.findViewById(R.id.text_view_event_name);
//                EditText eventNameEditText = (EditText) viewParent.findViewById(R.id.edit_text_event_name);
//                ImageButton cancelButton = (ImageButton) viewParent.findViewById(R.id.btn_event_cancel);
//                ImageButton confirmButton = (ImageButton) viewParent.findViewById(R.id.btn_event_confirm);
//
//                if(editingEventVo.getId() == EventVo.NEW_EVENT_ID){
//                    enableButton.setVisibility(View.GONE);
//                }
//
//                eventNameTextView.setVisibility(View.GONE);
//
//                cancelButton.setEnabled(true);
//                cancelButton.setVisibility(View.VISIBLE);
//                cancelButton.setFocusable(false);
//
//                confirmButton.setEnabled(true);
//                confirmButton.setVisibility(View.VISIBLE);
//                confirmButton.setFocusable(false);
//
//                eventNameEditText.setEnabled(true);
//                eventNameEditText.setVisibility(View.VISIBLE);
//                eventNameEditText.setFocusableInTouchMode(true);
//                eventNameEditText.requestFocus();
//                //imm.showSoftInput(eventNameEditText, InputMethodManager.SHOW_IMPLICIT);

				notifyDataSetChanged();

				break;
			case R.id.btn_event_cancel: 
				Log.d(TAG, "onClick cancel()");
				editingEventVo.setEditing(false);
				editingEventVo = null;
				
				imm.hideSoftInputFromWindow(editingEditText.getWindowToken(), 0);
				notifyDataSetChanged();
				break;
			case R.id.btn_event_confirm: 		
				Log.d(TAG, "onClick confirm()");
				if(editingEventVo.getId() == EventVo.NEW_EVENT_ID){
					parentActivity.getDbAdapter().newEvent(editingEditText.getText().toString());
				}else{
					parentActivity.getDbAdapter().updateEventName(editingEventVo.getId(), editingEditText.getText().toString());
				}
				editingEventVo.setEditing(false);
				editingEventVo = null;
				imm.hideSoftInputFromWindow(editingEditText.getWindowToken(), 0);
				setDataSource(parentActivity.getEventAllData(true));
				break;
			}

		}
	};
	
	private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

	    public void onCheckedChanged(CompoundButton v, boolean isChecked) {

			switch (v.getId()) {

			case R.id.toggle_button_event_enable:
				Log.d(TAG, "Toggle enable/disable");
				
				((EventVo)v.getTag()).setEnabled(isChecked?EventVo.ENABLED:EventVo.DISABLED);
				parentActivity.getDbAdapter().setEventEnable(((EventVo)v.getTag()).getId(), isChecked?EventVo.ENABLED:EventVo.DISABLED);
				break;
			}
	    }
	};
	
	public void setDataSource(List<EventVo> mList){

        Log.d(TAG,"setDataSource()");
		this.mList.clear();
		this.mList.addAll(mList);
		
		notifyDataSetChanged();
	}



}
