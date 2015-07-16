package com.fsz570.easyaccounting.util;

import java.text.DecimalFormatSymbols;

public class Consts {
	
	public static final int ACTIVITY_REQUEST_CODE_FOR_UPDATE_TRANSACTION = 0;
    public static final int ACTIVITY_REQUEST_CODE_FOR_UPDATE_BUDGET = 0;

	public static final int NO_EVENT_ID = -1;
	public static final int NO_CATEGORY_ID = -1;

    public static final String PARAM_MONTHLY_BUDGET_NAME = "MONTHLY_BUDGET";

	public static final String DECIMAL_SEPRATOR = String.valueOf(DecimalFormatSymbols.getInstance().getDecimalSeparator());
	public static final String GROUPING_SEPRATOR = String.valueOf(DecimalFormatSymbols.getInstance().getGroupingSeparator());
}
