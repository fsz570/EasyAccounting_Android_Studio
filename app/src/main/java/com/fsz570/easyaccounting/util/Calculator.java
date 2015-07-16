package com.fsz570.easyaccounting.util;

import java.text.DecimalFormatSymbols;

public class Calculator {

	// 3 + 6 = 9
    // 3 & 6 are called the operand.
    // The + is called the operator.
    // 9 is the result of the operation.
    private double mOperand;
    private double mWaitingOperand;
    private String mWaitingOperator;
 
    // operator types
    public static final String ADD = "+";
    public static final String SUBTRACT = "-";
    public static final String MULTIPLY = "*";
    public static final String DIVIDE = "/";
 
    public static final String CLEAR = "C" ;
    public static final String EQUAL = "=" ;
 
    // constructor
    public Calculator() {
        // initialize variables upon start
        mOperand = 0;
        mWaitingOperand = 0;
        mWaitingOperator = "";
    }
 
    public void setOperand(double operand) {
        mOperand = operand;
    }
 
    public double getResult() {
        return mOperand;
    }
 
    public String toString() {
        return Double.toString(mOperand);
    }
 
    public double performOperation(String operator) {
 
        if (operator.equals(CLEAR)) {
            mOperand = 0;
            mWaitingOperator = "";
            mWaitingOperand = 0;
            // mCalculatorMemory = 0;
        } else if (operator.equals(EQUAL)) {
        	performWaitingOperation();
            mWaitingOperator = "";
            mWaitingOperand = mOperand;
        } else{
            performWaitingOperation();
            mWaitingOperator = operator;
            mWaitingOperand = mOperand;
        }
 
        return mOperand;
    }
 
    public void performWaitingOperation() {
 
        if (mWaitingOperator.equals(ADD)) {
            mOperand = mWaitingOperand + mOperand;
        } else if (mWaitingOperator.equals(SUBTRACT)) {
            mOperand = mWaitingOperand - mOperand;
        } else if (mWaitingOperator.equals(MULTIPLY)) {
            mOperand = mWaitingOperand * mOperand;
        } else if (mWaitingOperator.equals(DIVIDE)) {
            if (mOperand != 0) {
                mOperand = mWaitingOperand / mOperand;
            }
        }
 
    }

}
