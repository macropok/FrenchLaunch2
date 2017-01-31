package com.launch.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class CustomLinearLayout extends ViewGroup {

	public CustomLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public CustomLinearLayout(Context context) {
		  super(context);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		float width =  getResources().getDisplayMetrics().widthPixels - 3 * getResources().getDimension(R.dimen.margin_space);
		setMeasuredDimension((int) (width * 2 / 3 + getResources().getDimension(R.dimen.margin_space)), getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
		
		 for(int i= 0;i<getChildCount();i++){ 
             View v = getChildAt(i); 
             int measureWidth = MeasureSpec.makeMeasureSpec((int) (width * 2 / 3 + getResources().getDimension(R.dimen.margin_space)), MeasureSpec.EXACTLY);
             int measureHeight = MeasureSpec.makeMeasureSpec(getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec), MeasureSpec.EXACTLY);
             v.measure(measureWidth, measureHeight); 
         } 
	}
	 
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		getChildAt(0).layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
	
	}

}
