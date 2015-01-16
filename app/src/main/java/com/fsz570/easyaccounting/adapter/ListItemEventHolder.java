package com.fsz570.easyaccounting.adapter;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fsz570.easyaccounting.vo.EventVo;

public class ListItemEventHolder implements java.io.Serializable {
	
	private static final long serialVersionUID = -3519728813962368883L;

	private EventVo eventVo;
	
	private ToggleButton enableButton;
	private TextView eventNameTextView;
	private EditText eventNameEditText;
	private ImageButton cancelButton;
	private ImageButton confirmButton;
	
	public ListItemEventHolder(){
		
	}
	
	public ListItemEventHolder(EventVo eventVo){
		this.eventVo = eventVo;
	}

	public EventVo getEventVo() {
		return eventVo;
	}

	public void setEventVo(EventVo eventVo) {
		this.eventVo = eventVo;
	}

	public ToggleButton getEnableButton() {
		return enableButton;
	}

	public void setEnableButton(ToggleButton enableButton) {
		this.enableButton = enableButton;
	}

	public EditText getEventNameEditText() {
		return eventNameEditText;
	}

	public void setEventNameEditText(EditText eventNameEditText) {
		this.eventNameEditText = eventNameEditText;
	}

	public TextView getEventNameTextView() {
		return eventNameTextView;
	}

	public void setEventNameTextView(TextView eventNameTextView) {
		this.eventNameTextView = eventNameTextView;
	}

	public ImageButton getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(ImageButton cancelButton) {
		this.cancelButton = cancelButton;
	}

	public ImageButton getConfirmButton() {
		return confirmButton;
	}

	public void setConfirmButton(ImageButton confirmButton) {
		this.confirmButton = confirmButton;
	}



}
