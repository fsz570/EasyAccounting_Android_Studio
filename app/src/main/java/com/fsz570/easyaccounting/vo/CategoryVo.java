package com.fsz570.easyaccounting.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class CategoryVo implements Parcelable{
	
	public static final int NEW_CATEGORY_ID = -1;
	public static final int UPDATE_CATEGORY_TEXT_ID = 0;
	
	private static final String TAG = "CategoryVo";
	
	private int _id;
	private int parentId;
	private String tranCategoryName;
	private String categoryIconId;
	private int seq;
	
	private boolean isParent;
	private int parentPosition;
	private int childPosition;

	private boolean isShowUpBtn = false;
	private boolean isShowDownBtn = false;
	
	private List<CategoryVo> childCategory;
	
	public CategoryVo(int _id, int parentId, String tranCategoryName,
			String categoryIconId, int seq) {
		this._id = _id;
		this.parentId = parentId;
		this.tranCategoryName = tranCategoryName;
		this.categoryIconId = categoryIconId;
		this.seq = seq;
		childCategory = new ArrayList<CategoryVo>();
	}

	public int getId() {
		return _id;
	}

	public void setId(int _id) {
		this._id = _id;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getTranCategoryName() {
		return tranCategoryName;
	}

	public void setTranCategoryName(String tranCategoryName) {
		this.tranCategoryName = tranCategoryName;
	}

	public String getCategoryIconId() {
		return categoryIconId;
	}

	public void setCategoryIconId(String categoryIconId) {
		this.categoryIconId = categoryIconId;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}
	
	public List<CategoryVo> getChildCategory() {
		if(childCategory == null){
			return new ArrayList<CategoryVo>();
		}
		return childCategory;
	}

	public void setChildCategory(List<CategoryVo> childCategory) {
		this.childCategory = childCategory;
	}
	
	public void addChildCategory(CategoryVo childVo){
		
		if(childCategory == null){
			childCategory = new ArrayList<CategoryVo>();
		}
		
		childCategory.add(childVo);
	}

	public boolean isParent() {
		return isParent;
	}

	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}

	public int getParentPosition() {
		return parentPosition;
	}

	public void setParentPosition(int parentPosition) {
		this.parentPosition = parentPosition;
	}

	public int getChildPosition() {
		return childPosition;
	}

	public void setChildPosition(int childPosition) {
		this.childPosition = childPosition;
	}

	public boolean isShowUpBtn() {
//		Log.d(TAG, "_id : " + _id);
		if(_id == NEW_CATEGORY_ID){
			return false;
		}else{
			return isShowUpBtn;
		}
	}

	public void setShowUpBtn(boolean isShowUpBtn) {
		this.isShowUpBtn = isShowUpBtn;
	}

	public boolean isShowDownBtn() {
		if(_id == NEW_CATEGORY_ID){
			return false;
		}else{
			return isShowDownBtn;
		}
	}

	public void setShowDownBtn(boolean isShowDownBtn) {
		this.isShowDownBtn = isShowDownBtn;
	}

	@Override
	public String toString() {
		if(parentId == 0){
			return this.tranCategoryName;
		}else{
			return "    " + this.tranCategoryName;
		}
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(_id);
		dest.writeInt(parentId);
		dest.writeString(tranCategoryName);
		dest.writeString(categoryIconId);
		dest.writeInt(seq);
	}
	
	public static final Parcelable.Creator<CategoryVo> CREATOR = new Parcelable.Creator<CategoryVo>() {
		public CategoryVo createFromParcel(Parcel pc) {
			return new CategoryVo(pc);
		}

		public CategoryVo[] newArray(int size) {
			return new CategoryVo[size];
		}
	};

	public CategoryVo(Parcel pc) {
		_id = pc.readInt();
		parentId = pc.readInt();
		tranCategoryName = pc.readString();
		categoryIconId = pc.readString();
		seq = pc.readInt();
	}
}
