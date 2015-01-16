package com.fsz570.easyaccounting.adapter;

import android.widget.TextView;

import com.fsz570.easyaccounting.vo.TransactionVo;

public class ListItemHolder implements java.io.Serializable {
	
	private static final long serialVersionUID = 543639596357374327L;

	private TransactionVo transVo;
	
	private TextView categoryTextView;
	private TextView amountTextView;
	private TextView dateTextView;
	private TextView eventTextView;
    private TextView commentTextView;
	
	
	public ListItemHolder(){
		
	}
	
	public ListItemHolder(TransactionVo transVo){
		this.transVo = transVo;
	}

	public TransactionVo getTransVo() {
		return transVo;
	}

	public void setTransVo(TransactionVo transVo) {
		this.transVo = transVo;
	}

	public TextView getCategoryTextView() {
		return categoryTextView;
	}

	public void setCategoryTextView(TextView categoryTextView) {
		this.categoryTextView = categoryTextView;
	}

	public TextView getAmountTextView() {
		return amountTextView;
	}

	public void setAmountTextView(TextView amountTextView) {
		this.amountTextView = amountTextView;
	}

	public TextView getDateTextView() {
		return dateTextView;
	}

	public void setDateTextView(TextView dateTextView) {
		this.dateTextView = dateTextView;
	}

	public TextView getEventTextView() {
		return eventTextView;
	}

	public void setEventTextView(TextView eventTextView) {
		this.eventTextView = eventTextView;
	}

    public TextView getCommentTextView() {
        return commentTextView;
    }

    public void setCommentTextView(TextView commentTextView) {
        this.commentTextView = commentTextView;
    }
}
