package com.launch.ui.alert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.launch.ui.UserApplication;
import com.launch.utils.AlarmQueue;
import com.launch.utils.MessageQuene;
import com.launch.utils.MessageUtils.NX;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		System.out.println("alarm brodcast receiver");
		// AlarmQueue.getInstance().addAction(action);
		if (TextUtils.equals(action, AlarmQueue.BATTERIE_ACTION)) {
			MessageQuene.getInstance(UserApplication.getInstance().getApplicationContext()).addMessage(NX.BATTERIE, true);
		} else if (TextUtils.equals(action, AlarmQueue.INACTIVITY_ACTION)) {
			MessageQuene.getInstance(UserApplication.getInstance().getApplicationContext()).addMessage(NX.INACTIVITE, true);
		} else if (TextUtils.equals(action, AlarmQueue.CHUTE_ACTION)) {
			MessageQuene.getInstance(UserApplication.getInstance().getApplicationContext()).addMessage(NX.CHUTES, true);
		} else if (TextUtils.equals(action, AlarmQueue.LOCATION_ACTION)) {
			MessageQuene.getInstance(UserApplication.getInstance().getApplicationContext()).addMessage(NX.CONFORT, false);
		}
	}

}
