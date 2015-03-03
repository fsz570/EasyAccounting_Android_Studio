package com.fsz570.easyaccounting.vo;

public class MonthlyBudgetVo {
    private long monthlyBudget = 0;
    private long monthlyExpense = 0;

    public MonthlyBudgetVo(long monthlyBudget, long monthlyExpense){
        this.monthlyBudget = monthlyBudget;
        this.monthlyExpense = monthlyExpense;
    }

    public long getMonthlyBudget() {
        return monthlyBudget;
    }

    public void setMonthlyBudget(long monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }

    public long getMonthlyExpense() {
        return monthlyExpense;
    }

    public void setMonthlyExpense(long monthlyExpense) {
        this.monthlyExpense = monthlyExpense;
    }
}
