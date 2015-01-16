package com.fsz570.easyaccounting.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class EventVo  implements Parcelable {
	
	public static final int EMPTY_EVENT_ID = -1;
	public static final int NEW_EVENT_ID = -1;
	public static final int ENABLED = 1;
	public static final int DISABLED = 0;
	
	private int _id;
	private String eventName;
	private int enabled;
	private boolean isEditing = false;
	
	public EventVo(int _id, String eventName){
		this._id = _id;
		this.eventName = eventName;
		this.enabled = ENABLED;
	}
	
	public EventVo(int _id, String eventName, int enabled){
		this._id = _id;
		this.eventName = eventName;
		this.enabled = enabled;
	}

	public int getId() {
		return _id;
	}

	public void setId(int _id) {
		this._id = _id;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	
	public boolean isEnabled(){
		return this.enabled == ENABLED;
	}

	public boolean isEditing() {
		return isEditing;
	}

	public void setEditing(boolean isEditing) {
		this.isEditing = isEditing;
	}

	@Override
	public String toString() {
	    return this.eventName;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(_id);
		dest.writeString(eventName);
		dest.writeInt(enabled);
	}
	
	public static final Parcelable.Creator<EventVo> CREATOR = new Parcelable.Creator<EventVo>() {
		public EventVo createFromParcel(Parcel pc) {
			return new EventVo(pc);
		}

		public EventVo[] newArray(int size) {
			return new EventVo[size];
		}
	};

	public EventVo(Parcel pc) {
		_id = pc.readInt();
		eventName = pc.readString();
		enabled = pc.readInt();
	}
}
