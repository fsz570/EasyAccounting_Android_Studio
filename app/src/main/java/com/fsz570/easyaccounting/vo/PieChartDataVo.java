package com.fsz570.easyaccounting.vo;

public class PieChartDataVo {
	
	private String categoryName;
	private float amount;
	
	public PieChartDataVo(String categoryName, float amount){
		this.categoryName = categoryName;
		this.amount = amount;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	
}
