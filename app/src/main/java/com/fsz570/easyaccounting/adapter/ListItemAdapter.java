package com.fsz570.easyaccounting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fsz570.easyaccounting.R;
import com.fsz570.easyaccounting.vo.TransactionVo;

import java.util.ArrayList;
import java.util.List;

public class ListItemAdapter extends BaseAdapter {
	
	private static final String TAG = "ListItemAdapter";
	private LayoutInflater mLayoutInflater;
	
	private List<TransactionVo> mList;
	
	public ListItemAdapter(Context mContext){
        mLayoutInflater = LayoutInflater.from(mContext);
        
        mList = new ArrayList<TransactionVo>();
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
		ListItemHolder holder;
		
		if(null == view){
            view = mLayoutInflater.inflate(R.layout.list_item_view, null);
            holder = new ListItemHolder();
            holder.setCategoryTextView((TextView) view.findViewById(R.id.transaction_item_category));
            holder.setAmountTextView((TextView) view.findViewById(R.id.transaction_item_amount));
            holder.setDateTextView((TextView) view.findViewById(R.id.transaction_item_date));
            holder.setEventTextView((TextView) view.findViewById(R.id.transaction_item_event));
            holder.setCommentTextView((TextView) view.findViewById(R.id.transaction_item_comment));
            view.setTag(holder);
        }else{
            holder = (ListItemHolder) view.getTag();
        }
		
		holder.getCategoryTextView().setText(mList.get(position).getTranCategoryName());
		holder.getAmountTextView().setText(mList.get(position).getTranAmountString());
		holder.getDateTextView().setText(mList.get(position).getTranDate());
        if(mList.get(position).getTranEventName() != null && mList.get(position).getTranEventName().length() > 0) {
            holder.getEventTextView().setText("["+mList.get(position).getTranEventName()+"]");
        }
        holder.getCommentTextView().setText(mList.get(position).getTranComment());
		
		return view;
	}
	
	public void setDataSource(List<TransactionVo> mList){
		this.mList.clear();
		this.mList.addAll(mList);
		
		notifyDataSetChanged();
	}

}
