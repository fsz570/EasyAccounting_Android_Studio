package com.fsz570.easyaccounting.ui;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.fsz570.easyaccounting.R;
import com.fsz570.easyaccounting.vo.CategoryVo;

public class CategoryButton extends Button {

	private static final String TAG = "CategoryButton";
	
	private Activity parentActivity;

	private CategoryVo categoryVo;

	private View.OnClickListener categoryBtnOnClickListener;

	public CategoryButton(Context context) {
		super(context);
		parentActivity = ((Activity)context);

		categoryBtnOnClickListener = genCategoryBtnOnClickListener();
	}

	public CategoryButton setCategory(CategoryVo categoryVo) {
		this.categoryVo = categoryVo;

		if (categoryVo.getTranCategoryName() != null) {
			setText(categoryVo.getTranCategoryName());
		}

//		if (categoryVo.getCategory_icon_id() != null) {
//			int imageResource = parentActivity.getResources().getIdentifier(
//					categoryVo.getCategory_icon_id(), "drawable",
//					parentActivity.getPackageName());
//			setCompoundDrawablesWithIntrinsicBounds(0, imageResource, 0, 0);
//		}

		setOnClickListener(categoryBtnOnClickListener);

		return this;
	}

	private View.OnClickListener genCategoryBtnOnClickListener() {
		return new View.OnClickListener() {
			public void onClick(View v) {

				Spinner categorySpinner = (Spinner) parentActivity.findViewById(R.id.input_category_spinner);
				ArrayAdapter<CategoryVo> eventAdapter = (ArrayAdapter<CategoryVo>) categorySpinner.getAdapter();
				
				for(int i=0;i<eventAdapter.getCount();i++){
					CategoryVo vo = eventAdapter.getItem(i);
					if(vo.getId() == ((CategoryButton)v).getCategoryVo().getId()){
						categorySpinner.setSelection(i);
						break;
					}
				}
			}
		};
	}

	public CategoryVo getCategoryVo() {
		return categoryVo;
	}

	public void setCategoryVo(CategoryVo categoryVo) {
		this.categoryVo = categoryVo;
	}
	
	

}
