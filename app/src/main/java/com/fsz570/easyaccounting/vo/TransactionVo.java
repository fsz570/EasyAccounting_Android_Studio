package com.fsz570.easyaccounting.vo;

import android.os.Parcel;
import android.os.Parcelable;

import com.fsz570.easyaccounting.util.Utils;

public class TransactionVo implements Parcelable{
	
	public static final String TRANSACTION_VO_NAME = "TRANSACTION_VO";

	private int _id;
	private String tranDate;
	private int tranCategoryId;
	private int tranEventId;
	private double tranAmount;
	private String tranComment;
	
	private String tranCategoryName;
	private String tranEventName;
	
	public TransactionVo(){
		
	}
	
	public TransactionVo(int _id, String tranDate, int tranCategoryId,
			int tranEventId, double tranAmount, String tranComment,
			String tranCategoryName, String tranEventName) {
		this._id = _id;
		this.tranDate = tranDate;
		this.tranCategoryId = tranCategoryId;
		this.tranEventId = tranEventId;
		this.tranAmount = tranAmount;
		this.tranComment = tranComment;
		this.tranCategoryName = tranCategoryName;
		this.tranEventName = tranEventName;
	}
	
	public int getId() {
		return _id;
	}
	public void setId(int _id) {
		this._id = _id;
	}
	public String getTranDate() {
		return tranDate;
	}
	public void setTranDate(String tranDate) {
		this.tranDate = tranDate;
	}
	public int getTranCategoryId() {
		return tranCategoryId;
	}
	public void setTranCategoryId(int tranCategoryId) {
		this.tranCategoryId = tranCategoryId;
	}
	public int getTranEventId() {
		return tranEventId;
	}
	public void setTranEventId(int tranEventId) {
		this.tranEventId = tranEventId;
	}
	public double getTranAmount() {
		return tranAmount;
	}
	public void setTranAmount(double tranAmount) {
		this.tranAmount = tranAmount;
	}
	public String getTranComment() {
		return tranComment;
	}
	public void setTranComment(String tranComment) {
		this.tranComment = tranComment;
	}
	public String getTranCategoryName() {
		return tranCategoryName;
	}
	public void setTranCategoryName(String tranCategoryName) {
		this.tranCategoryName = tranCategoryName;
	}
	public String getTranEventName() {
		return tranEventName;
	}
	public void setTranEventName(String tranEventName) {
		this.tranEventName = tranEventName;
	}
	
	public String getTranAmountString(){
		return Utils.formatAmount(this.tranAmount);
	}

	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		
		sb.append("_id : ").append(this._id).append(";");
		sb.append("tranDate : ").append(this.tranDate).append(";");
		sb.append("tranCategoryId : ").append(this.tranCategoryId).append(";");
		sb.append("tranEventId : ").append(this.tranEventId).append(";");
		sb.append("tranAmount : ").append(this.tranAmount).append(";");
		sb.append("tranComment : ").append((this.tranComment==null)?"":this.tranComment).append(";");
		
		
		return sb.toString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(_id);
		dest.writeString(tranDate);
		dest.writeInt(tranCategoryId);
		dest.writeInt(tranEventId);
		dest.writeDouble(tranAmount);
		dest.writeString(tranComment);
		dest.writeString(tranCategoryName);
		dest.writeString(tranEventName);
	}
	
	public static final Parcelable.Creator<TransactionVo> CREATOR = new Parcelable.Creator<TransactionVo>() {
		public TransactionVo createFromParcel(Parcel pc) {
			return new TransactionVo(pc);
		}

		public TransactionVo[] newArray(int size) {
			return new TransactionVo[size];
		}
	};

	public TransactionVo(Parcel pc) {
		_id = pc.readInt();
		tranDate = pc.readString();
		tranCategoryId = pc.readInt();
		tranEventId = pc.readInt();
		tranAmount = pc.readDouble();
		tranComment = pc.readString();
		tranCategoryName = pc.readString();
		tranEventName = pc.readString();
	}
}
