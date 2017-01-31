package com.launch.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.launch.setting.VoulezAlertFragment.Voulez;
import com.launch.sqlite.P;

public class Preferences {

	public static SharedPreferences getPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	public static void setAlarm(Context context, String action, boolean open) {
		SharedPreferences share = getPreferences(context);
		Editor editor = share.edit();
		editor.putBoolean(action, open);
		editor.commit();
	}

	
	public static void saveLocation(Context context, double lat, double lng) {
		SharedPreferences share = getPreferences(context);
		Editor editor = share.edit();
		editor.putFloat("lat", (float) lat);
		editor.putFloat("lng", (float) lng);
		editor.commit();
	}
	public static void savePhonenumber1(Context context, String name, String number) {
		SharedPreferences share = getPreferences(context);
		Editor editor = share.edit();
		editor.putString("phone1name", name);
		editor.putString("phone1number", number);
		editor.commit();
	}	
	
	public static void savePhonenumber2(Context context, String name, String number) {
		SharedPreferences share = getPreferences(context);
		Editor editor = share.edit();
		editor.putString("phone2name", name);
		editor.putString("phone2number", number);
		editor.commit();
	}
	
	public static P getPhoneNumber1(Context context) {
		P p = new P();
		p.name = getPreferences(context).getString("phone1name", "");
		p.number = getPreferences(context).getString("phone1number", "");
		return p;
	}
	
	public static Voulez getLongClick(Context context) {
		return Voulez.values()[getPreferences(context).getInt("longClick", 0)];
	}
	
	public static void setLongClick(Context context, Voulez click) {
		SharedPreferences share = getPreferences(context);
		Editor editor = share.edit();
		editor.putInt("longClick", click.ordinal());
		editor.commit();
	}
	
	public static P getPhoneNumber2(Context context) {
		P p = new P();
		p.name = getPreferences(context).getString("phone2name", "");
		p.number = getPreferences(context).getString("phone2number", "");
		return p;
	}

	public static float getLat(Context context) {
		SharedPreferences share = getPreferences(context);
		return share.getFloat("lat", 0);
	}

	public static float getLng(Context context) {
		SharedPreferences share = getPreferences(context);
		return share.getFloat("lng", 0);
	}

	public static boolean isAlarm(Context context, String action) {
		SharedPreferences share = getPreferences(context);
		return share.getBoolean(action, false);
	}
}
