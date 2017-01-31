package com.launch.utils;

import android.content.Context;
import android.graphics.Typeface;

public class Utils {
	public static Typeface getAvailableFont(Context context) {
			return Typeface.createFromAsset(context.getAssets(), "avenir.ttf");
		}
	
		public static Typeface getAvailableFontBold(Context context) {
			return Typeface.createFromAsset(context.getAssets(), "avenir_bold.ttf");
		}
}

