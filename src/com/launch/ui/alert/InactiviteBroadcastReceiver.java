package com.launch.ui.alert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.launch.utils.AlarmQueue;
import com.launch.utils.Preferences;

public class InactiviteBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		System.out.println("inactivite");
		Preferences.setAlarm(context, AlarmQueue.INACTIVITY_ACTION, true);
	}

}
