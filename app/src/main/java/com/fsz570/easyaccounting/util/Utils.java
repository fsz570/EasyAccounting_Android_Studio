package com.fsz570.easyaccounting.util;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import com.fsz570.easyaccounting.vo.CategoryVo;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utils {

	public static Date getToday() {
		return Calendar.getInstance().getTime();
	}

	public static String formatAmount(double tranAmount) {
		NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

		// If not integer
		if (tranAmount != Math.floor(tranAmount)) {
			numberFormat.setMaximumFractionDigits(2);
			numberFormat.setMinimumFractionDigits(2);
		}

		return numberFormat.format(tranAmount);
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
	    a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density) * slowRate);
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
        return value.replaceAll("0", "").replaceAll(".","").length() == 0;
    }
}
