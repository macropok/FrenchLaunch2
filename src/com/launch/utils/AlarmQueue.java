package com.launch.utils;

import java.util.HashMap;
import java.util.LinkedList;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.launch.ui.AlarmActivity;
import com.launch.ui.UserApplication;
import com.launch.ui.alert.AlarmBroadcastReceiver;
import com.launch.ui.alert.InactiviteBroadcastReceiver;

public class AlarmQueue  {

	private static AlarmQueue instance;

	public static final String INACTIVITY_ACTION = "inactivity.alarm.response";
	public static final String CHUTE_ACTION = "chute.alarm.response";
	public static final String BATTERIE_ACTION = "batterie.alarm.response";
	public static final String LOCATION_ACTION = "location.alarm.response";

	private LinkedList<String> queue;

	private HashMap<String, Boolean> alarms;
	
	private AlarmQueue() {
		queue = new LinkedList<String>();
		alarms = new HashMap<String, Boolean>();
	}

	public static AlarmQueue getInstance() {
		if (instance == null) {
			instance = new AlarmQueue();
		}

		return instance;
	}

	public void registerAlarmTime(Context context, String action,
			long time) {
		if(alarms.size() != 0 && alarms.get(action)) {
			return;
		}
		System.out.println("register alarm time : " + time);
		alarms.put(action, true);
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent();
		intent.setAction(action);
		intent.setClass(context, AlarmBroadcastReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		am.set(AlarmManager.RTC_WAKEUP, time, pi);
	}
	
	public void registerAlarmTimeByInactivite(Context context, long time) {
		unRegisterAlarmTimeByactivite(context);
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent();
		intent.setClass(context, InactiviteBroadcastReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		am.set(AlarmManager.RTC_WAKEUP, time, pi);
	}


	public void unRegisterAlarmTimeByactivite(Context context) {
		Preferences.setAlarm(context, INACTIVITY_ACTION, false);
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent();
		intent.setClass(context, AlarmBroadcastReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		am.cancel(pi);
	}
	
	public void unRegisterAlarmTime(Context context, String action) {
		alarms.put(action, false);
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent();
		intent.setAction(action);
		intent.setClass(context, AlarmBroadcastReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		am.cancel(pi);
	}

	
	public void addAction(String action) {
		queue.add(action);
		executeAlarm();		
	}
	
	public void onDestory() {
		for(String action : alarms.keySet()) {
			unRegisterAlarmTime(UserApplication.getInstance().getActivity(), action);
		}
		alarms.clear();
		queue.clear();
	}
	
	public void finishAlarm(String action) {
		System.out.println("execute alarm finish");
		alarms.put(action, false);
		executeAlarm();
	}

	private void executeAlarm() {
		if (queue.size() <= 0) {
			System.out.println("alarm size 0, so alarm execute finish");
			return;
		}
		String action = queue.poll();
		System.out.println("execute alarm action :" + action);

		Intent alarm = new Intent(UserApplication.getInstance().getActivity(), AlarmActivity.class);
		if (TextUtils.equals(action, AlarmQueue.BATTERIE_ACTION)) {
			alarm.setAction(AlarmQueue.BATTERIE_ACTION);
		} else if (TextUtils.equals(action, AlarmQueue.CHUTE_ACTION)) {
			alarm.setAction(AlarmQueue.CHUTE_ACTION);
		}
		UserApplication.getInstance().getActivity().startActivityForResult(alarm, 110);
	}

}
