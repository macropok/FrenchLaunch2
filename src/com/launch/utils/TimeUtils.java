package com.launch.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;

public class TimeUtils {

	public static String[] getMinteArray() {
		String[] times = new String[60];
		for(int i=0; i<times.length; i++) {
			times[i] = (i + 1) + "";
		}
		return times;
	}
	
	public static String[] getBatterieArray() {
		String[] times = new String[30];
		for(int i=0; i<times.length; i++) {
			times[i] = (i + 1) + "";
		}
		return times;
	}
	
	public static String[] getTenHourArray() {
		String[] times = new String[10];
		for(int i=0; i<times.length; i++) {
			times[i] = (i + 1) + "";
		}
		return times;
	}
	
	public static String getCurrentTime() {
		final Calendar calendar = Calendar.getInstance();
		final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(calendar.getTime());
	}
	
	public static String[] getPreArray() {
		String[] times = new String[100];
		for(int i=0; i<times.length; i++) {
			times[i] = (i + 1) + "";
		}
		return times;
	}
	
	public static int getCurrentBatterie() {
		return 20;
	}
	
	public static void showDatapicker(Context context, Calendar calendar, OnDateSetListener callBack) {
		int year = calendar.get(Calendar.YEAR);
		int monthOfYear = calendar.get(Calendar.MONTH);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		DatePickerDialog dialog = new DatePickerDialog(context, callBack, year, monthOfYear, dayOfMonth);
		dialog.show();
	}
	
	public static void showTimepicker(Context context, Calendar calendar, OnTimeSetListener callBack) {
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minite = calendar.get(Calendar.MINUTE);
		TimePickerDialog dialog = new TimePickerDialog(context, callBack, hour, minite, true);
		dialog.show();
	}
}
