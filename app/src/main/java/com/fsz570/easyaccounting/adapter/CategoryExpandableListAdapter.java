package com.fsz570.easyaccounting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fsz570.easyaccounting.R;
import com.fsz570.easyaccounting.UpdateCategoryActivity;
import com.fsz570.easyaccounting.vo.CategoryVo;

import java.util.List;

public class CategoryExpandableListAdapter extends BaseExpandableListAdapter {
	
	private List<CategoryVo> parentCategoryList;
	private UpdateCategoryActivity parentActivity;
	private static final int UP = -1;
	private static final int DOWN = 1;
	
	private static final String TAG = "CategoryExpandableListAdapter";
	
	public CategoryExpandableListAdapter(Context context, List<CategoryVo> parentCategoryList){
		this.parentActivity = (UpdateCategoryActivity) context;
		this.setDataSource(parentCategoryList);
	}
	
	public void setDataSource(List<CategoryVo> parentCategoryList){
		
		if(this.parentCategoryList == null){
			this.parentCategoryList = parentCategoryList;
		}else{
			this.parentCategoryList.clear();
			this.parentCategoryList.addAll(parentCategoryList);
		}
		
		addNewCategoryItems(parentCategoryList);
		
		notifyDataSetChanged();
	}
	
	private void addNewCategoryItems(List<CategoryVo> paretnCategoryList) {
		for (CategoryVo parentCategoryVo : paretnCategoryList) {
			parentCategoryVo.getChildCategory().add(
					0,
					new CategoryVo(CategoryVo.NEW_CATEGORY_ID, parentCategoryVo
							.getId(), parentActivity.getResources().getString(
							R.string.edit_text_new_sub_category), null, 0));
		}
		paretnCategoryList
				.add(0,
						new CategoryVo(CategoryVo.NEW_CATEGORY_ID,
								CategoryVo.NEW_CATEGORY_ID,
								parentActivity.getResources().getString(
										R.string.edit_text_new_main_category),
								null, 0));
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return parentCategoryList.get(groupPosition).getChildCategory().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return parentCategoryList.get(groupPosition).getChildCategory().get(childPosition).hashCode();
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
	        boolean isLastChild, View convertView, ViewGroup parent) {

	    View v = convertView;

	    if (v == null) {
	        LayoutInflater inflater = (LayoutInflater)parentActivity.getSystemService
	                  (Context.LAYOUT_INFLATER_SERVICE);
	        v = inflater.inflate(R.layout.category_child, parent, false);
	    }

	    TextView childCategoryTextView = (TextView) v.findViewById(R.id.category_child_textview);
	    ImageButton upBtn = (ImageButton) v.findViewById(R.id.category_up_btn);
	    ImageButton downBtn = (ImageButton) v.findViewById(R.id.category_down_btn);
	    
	    upBtn.setOnClickListener(clickListener);
	    downBtn.setOnClickListener(clickListener);

	    CategoryVo childCategory = parentCategoryList.get(groupPosition).getChildCategory().get(childPosition);
	    childCategory.setParent(false);
	    childCategory.setParentPosition(groupPosition);
	    childCategory.setChildPosition(childPosition);
	    
	    if(childCategory.isShowUpBtn()){
	    	upBtn.setVisibility(View.VISIBLE);
	    	upBtn.setFocusable(false);
	    	parentActivity.setLastSelectedView(v);
	    }else{
	    	upBtn.setVisibility(View.GONE);
	    }
	    if(childCategory.isShowDownBtn()){
	    	downBtn.setVisibility(View.VISIBLE);
	    	downBtn.setFocusable(false);
	    }else{
	    	downBtn.setVisibility(View.GONE);
	    }

	    childCategoryTextView.setText(childCategory.getTranCategoryName());
	    v.setTag(childCategory);
	    upBtn.setTag(childCategory);
	    downBtn.setTag(childCategory);

	    return v;

	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (parentCategoryList != null){
			return (parentCategoryList.get(groupPosition).getChildCategory() == null)?0:parentCategoryList.get(groupPosition).getChildCategory().size();
		}else{
			return 0;
		}
	}

	@Override
	public Object getGroup(int groupPosition) {
		return parentCategoryList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return (parentCategoryList == null)?0:parentCategoryList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return parentCategoryList.get(groupPosition).hashCode();
	}

	@Override
	public View getGroupView(final int groupPosition, final boolean isExpanded,
			final View convertView, final ViewGroup parent) {

	    View v = convertView;

	    if (v == null) {
	        LayoutInflater inflater = (LayoutInflater)parentActivity.getSystemService
	                  (Context.LAYOUT_INFLATER_SERVICE);
	        v = inflater.inflate(R.layout.category_parent, parent, false);
	    }

	    TextView parentCategoryTextView = (TextView) v.findViewById(R.id.category_parent_textview);
	    ImageButton upBtn = (ImageButton) v.findViewById(R.id.category_up_btn);
	    ImageButton downBtn = (ImageButton) v.findViewById(R.id.category_down_btn);
	    ImageView listHeaderArrow = (ImageView) v.findViewById(R.id.category_expand_view);
	    
	    upBtn.setOnClickListener(clickListener);
	    downBtn.setOnClickListener(clickListener);
	    listHeaderArrow.setOnClickListener(clickListener);

	    CategoryVo parentCategory = parentCategoryList.get(groupPosition);
	    parentCategory.setParent(true);
	    parentCategory.setParentPosition(groupPosition);
	    
	    if(parentCategory.isShowUpBtn()){
	    	upBtn.setVisibility(View.VISIBLE);
	    	upBtn.setFocusable(false);
	    	parentActivity.setLastSelectedView(v);
	    }else{
	    	upBtn.setVisibility(View.GONE);
	    }
	    if(parentCategory.isShowDownBtn()){
	    	downBtn.setVisibility(View.VISIBLE);
	    	downBtn.setFocusable(false);
	    }else{
	    	downBtn.setVisibility(View.GONE);
	    }

	    parentCategoryTextView.setText(parentCategory.getTranCategoryName());
	    v.setTag(parentCategory);
	    upBtn.setTag(parentCategory);
	    downBtn.setTag(parentCategory);
	    
	    if(parentCategory.getId() != CategoryVo.NEW_CATEGORY_ID){
		    int imageResourceId = isExpanded ? R.drawable.btn_expand : R.drawable.btn_collapse;
		    listHeaderArrow.setImageResource(imageResourceId);
		    
		    listHeaderArrow.setOnClickListener(new View.OnClickListener() {
	
		        @Override
		        public void onClick(View v) {
	
					if (isExpanded)
						((ExpandableListView) parent).collapseGroup(groupPosition);
					else
						((ExpandableListView) parent).expandGroup(groupPosition,
								true);
	
		        }
		    });
	    }else{
	    	listHeaderArrow.setBackgroundResource(R.drawable.empty_background_36x36_96);
	    }


	    return v;

	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	private View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			CategoryVo vo = (CategoryVo) v.getTag();
			
			switch (v.getId()) {
			case R.id.category_up_btn:
				swapPosition(vo, UP);
				break;
			case R.id.category_down_btn:
				swapPosition(vo, DOWN);
				break;
			}
		}

	};

	private void swapPosition(CategoryVo vo, int direction) {

		if(vo.isParent()){
			if(vo.getParentPosition() == 1 && direction == UP){
				return; //Do nothing
			}else if(vo.getParentPosition() == (getGroupCount()-1) && direction == DOWN){
				return; //Do nothing
			}else{
				CategoryVo tmp = (CategoryVo)getGroup(vo.getParentPosition() + direction);
				tmp.setShowUpBtn(false);
				tmp.setShowDownBtn(false);
				parentCategoryList.set(vo.getParentPosition() + direction, vo);
				parentCategoryList.set(vo.getParentPosition(), tmp);
			}
		}else{
			if(vo.getChildPosition() == 1 && direction == UP){
				return; //Do nothing
			}else if(vo.getChildPosition() == (getChildrenCount(vo.getParentPosition())-1 ) && direction == DOWN){
				return; //Do nothing
			}else{
				CategoryVo tmp = (CategoryVo)getChild(vo.getParentPosition(),vo.getChildPosition() + direction);
				tmp.setShowUpBtn(false);
				tmp.setShowDownBtn(false);
				parentCategoryList.get(vo.getParentPosition()).getChildCategory().set(vo.getChildPosition() + direction, vo);
				parentCategoryList.get(vo.getParentPosition()).getChildCategory().set(vo.getChildPosition(), tmp);
			}
		}
		
		notifyDataSetChanged();
		parentActivity.updateCategorySeq(parentCategoryList);
	}
}
