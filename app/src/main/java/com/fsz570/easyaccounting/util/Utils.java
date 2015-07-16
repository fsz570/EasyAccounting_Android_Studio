package com.fsz570.easyaccounting.util;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import com.fsz570.easyaccounting.AccountingActivity;
import com.fsz570.easyaccounting.UpdateBudgetActivity;
import com.fsz570.easyaccounting.UpdateCategoryActivity;
import com.fsz570.easyaccounting.UpdateEventActivity;
import com.fsz570.easyaccounting.UpdateTransactionActivity;
import com.fsz570.easyaccounting.vo.CategoryVo;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Utils {

    private static String TAG = "Utils";

//    private static final NumberFormat numberFormat = NumberFormat.getNumberInstance();
//    private static final DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();

    private Utils() {};

	public static Date getToday() {
		return Calendar.getInstance().getTime();
	}

	public static String formatAmount(double tranAmount) {
		//NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

        //DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
		// If not integer
//		if (tranAmount != Math.floor(tranAmount)) {
			numberFormat.setMaximumFractionDigits(2);
////			numberFormat.setMinimumFractionDigits(2);
//		}

		return numberFormat.format(tranAmount);
	}

    public static String formatAmount(String tranAmount){
        if(".".equals(Consts.GROUPING_SEPRATOR)) {
            return Utils.formatAmount(Utils.parseDouble(tranAmount.replaceAll("\\.", "")));
        }else{
            return Utils.formatAmount(Utils.parseDouble(tranAmount.replaceAll(Consts.GROUPING_SEPRATOR, "")));
        }
    }

    public static double parseDouble(String value){
        try {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            if(".".equals(Consts.GROUPING_SEPRATOR)) {
                return numberFormat.parse(value.replaceAll("\\.", "")).doubleValue();
            }else{
                return numberFormat.parse(value.replaceAll(Consts.GROUPING_SEPRATOR, "")).doubleValue();
            }
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public static long parseLong(String value){
        try {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            if(".".equals(Consts.GROUPING_SEPRATOR)) {
                return numberFormat.parse(value.replaceAll("\\.", "")).longValue();
            }else{
                return numberFormat.parse(value.replaceAll(Consts.GROUPING_SEPRATOR, "")).longValue();
            }
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }

	public static List<CategoryVo> populateCategory(
			List<CategoryVo> parentCategoryList) {

		List<CategoryVo> allCategoryVo = new ArrayList<CategoryVo>();

		if (parentCategoryList != null) {
			for (CategoryVo parentCategory : parentCategoryList) {
				allCategoryVo.add(parentCategory);
				allCategoryVo.addAll(parentCategory.getChildCategory());
			}
		}

		return allCategoryVo;
	}
	
	public static void expandHeight(final View v, final int slowRate) {
	    v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	    final int targetHeight = v.getMeasuredHeight();

	    v.getLayoutParams().height = 0;
	    v.setVisibility(View.VISIBLE);
	    Animation a = new Animation()
	    {
	        @Override
	        protected void applyTransformation(float interpolatedTime, Transformation t) {
	            v.getLayoutParams().height = interpolatedTime == 1
	                    ? LayoutParams.WRAP_CONTENT
	                    : (int)(targetHeight * interpolatedTime);
	            v.requestLayout();
	        }

	        @Override
	        public boolean willChangeBounds() {
	            return true;
	        }
	    };

	    // 1dp/ms
	    a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density) * slowRate);
	    v.startAnimation(a);
	}

	public static void collapseHeight(final View v, final int slowRate) {
	    final int initialHeight = v.getMeasuredHeight();

	    Animation a = new Animation()
	    {
	        @Override
	        protected void applyTransformation(float interpolatedTime, Transformation t) {
	            if(interpolatedTime == 1){
	                v.setVisibility(View.GONE);
	            }else{
	                v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
	                v.requestLayout();
	            }
	        }

	        @Override
	        public boolean willChangeBounds() {
	            return true;
	        }
	    };

	    // 1dp/ms
	    a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density) * slowRate);
	    v.startAnimation(a);
	}

    public static void expandWidth(final View v, final int slowRate) {
        v.measure(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        final int targetWidth = v.getMeasuredWidth();
        final int alpha = 255;

        v.getLayoutParams().width = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().width = interpolatedTime == 1
                        ? LayoutParams.MATCH_PARENT
                        : (int)(targetWidth * interpolatedTime);
                v.getBackground().setAlpha((int)(alpha * interpolatedTime));
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetWidth / v.getContext().getResources().getDisplayMetrics().density) * slowRate);
        a.setInterpolator(new LinearInterpolator());
        v.startAnimation(a);
    }

    public static void collapseWidth(final View v, final int slowRate) {
        final int initialWidth= v.getMeasuredWidth();
        final int alpha = 255;

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().width = initialWidth - (int)(initialWidth * interpolatedTime);
                    v.getBackground().setAlpha(alpha - (int)(alpha * interpolatedTime));
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialWidth / v.getContext().getResources().getDisplayMetrics().density) * slowRate);
        a.setInterpolator(new LinearInterpolator());
        v.startAnimation(a);
    }

    public static void fadeIn(final View v, final int slowRate) {
        final int alphaMax = 255;

        v.getLayoutParams().width = LayoutParams.MATCH_PARENT;
        v.getBackground().setAlpha(alphaMax);
        v.setVisibility(View.VISIBLE);
        v.requestLayout();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getBackground().setAlpha((int)(alphaMax * interpolatedTime));
//                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(slowRate * 1000);
        a.setInterpolator(new LinearInterpolator());
        v.startAnimation(a);
    }

    public static void fadeOut(final View v, final int slowRate) {
        final int alpha = 255;

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getBackground().setAlpha(alpha - (int)(alpha * interpolatedTime));
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(slowRate * 1000);
        a.setInterpolator(new LinearInterpolator());
        v.startAnimation(a);
    }

    public static boolean isContainOnlyZeroAndDot(String value){
        if(".".equals(Consts.DECIMAL_SEPRATOR)) {
            return value.replaceAll("0", "").replaceAll("\\.", "").length() == 0;
        }else{
            return value.replaceAll("0", "").replaceAll(Consts.DECIMAL_SEPRATOR, "").length() == 0;
        }
    }

    public static String transferDateFormatForSqlite(String dateStr, Context context){
        DateFormat df = android.text.format.DateFormat.getDateFormat(context);
        String result = dateStr;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date date = df.parse(dateStr);
            result = sdf.format(date);
        }catch(Exception e){
            e.printStackTrace();
        }
        Log.d(TAG, "transferDateFormatForSqlite : " + dateStr + "==>" + result);
        return result;
    }

    public static String transferSqliteDateFormatForLocal(String dateStr, Context context){
        DateFormat df = android.text.format.DateFormat.getDateFormat(context);
        String result = dateStr;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date date = sdf.parse(dateStr);
            result = df.format(date);
        }catch(Exception e){
            e.printStackTrace();
        }
        Log.d(TAG, "transferDateFormatForSqlite : " + dateStr + "==>" + result);
        return result;
    }

    public static boolean shouldItFormat(String newValue){
        boolean shouldFormat = false;


        return shouldFormat;
    }

    public static void enableStrictMode() {
        if (Utils.hasGingerbread()) {
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder =
                    new StrictMode.ThreadPolicy.Builder()
                            .detectAll()
                            .penaltyLog();
            StrictMode.VmPolicy.Builder vmPolicyBuilder =
                    new StrictMode.VmPolicy.Builder()
                            .detectAll()
                            .penaltyLog();

            if (Utils.hasHoneycomb()) {
                threadPolicyBuilder.penaltyFlashScreen();
                vmPolicyBuilder
                        .setClassInstanceLimit(AccountingActivity.class, 1)
                        .setClassInstanceLimit(UpdateTransactionActivity.class, 1)
                        .setClassInstanceLimit(UpdateCategoryActivity.class, 1)
                        .setClassInstanceLimit(UpdateEventActivity.class, 1)
                        .setClassInstanceLimit(UpdateBudgetActivity.class, 1);
            }
            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
            StrictMode.setVmPolicy(vmPolicyBuilder.build());
        }
    }

    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }
}
