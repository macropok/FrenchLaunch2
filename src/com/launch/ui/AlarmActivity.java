package com.launch.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.launch.sqlite.AlertService;
import com.launch.sqlite.BatterieAlert;
import com.launch.sqlite.ChuteAlert;
import com.launch.sqlite.DatabaseHelper;
import com.launch.utils.AlarmQueue;
import com.launch.utils.MessageQuene;
import com.launch.utils.MessageUtils.NX;

public class AlarmActivity extends Activity implements OnClickListener {

	private TextView message;

	private String action = null;

	private DatabaseHelper databaseHelper = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_layout);
		message = (TextView) findViewById(R.id.message);
		findViewById(R.id.yes).setOnClickListener(this);
		findViewById(R.id.no).setOnClickListener(this);
		action = getIntent().getAction();
		if (TextUtils.equals(action, AlarmQueue.BATTERIE_ACTION)) {
			message.setText("Batterie faible, Mettre le telephone à charger Voulez-vous désamorcer l’alerte");
		} else if(TextUtils.equals(action, AlarmQueue.CHUTE_ACTION)) {
			message.setText("Chûte détectée. Voulez-vous désamorcer l’alerte");
		}
	}

	public DatabaseHelper getHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(this,
					DatabaseHelper.class);
		}
		return databaseHelper;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		/*
		 * 释放资源
		 */
		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
	}

	@Override
	public void onBackPressed() {
	}

	@Override
	public void onClick(View v) {
		if (TextUtils.isEmpty(action)) {
			return;
		}
		int id = v.getId();

		try {
			if (id == R.id.yes) {
				if (TextUtils.equals(action, AlarmQueue.BATTERIE_ACTION)) {
					AlertService<BatterieAlert> batterieService = (AlertService<BatterieAlert>) getHelper()
							.getAlertService(BatterieAlert.class);
					BatterieAlert batterieAlert = batterieService.getAlert();
					batterieAlert.setActive(false);
					batterieService.saveOrUpdateAlert(batterieAlert);
				} else if(TextUtils.equals(action, AlarmQueue.CHUTE_ACTION)) {
					AlertService<ChuteAlert> chutService = (AlertService<ChuteAlert>) getHelper()
							.getAlertService(ChuteAlert.class);
					ChuteAlert chuteAlert = chutService.getAlert();
					chuteAlert.setActive(false);
					chutService.saveOrUpdateAlert(chuteAlert);
				}

			} else {
				if (TextUtils.equals(action, AlarmQueue.BATTERIE_ACTION)) {
					MessageQuene.getInstance(UserApplication.getInstance().getApplicationContext()).addMessage(NX.BATTERIE, true);
				} else if (TextUtils.equals(action, AlarmQueue.CHUTE_ACTION)) {
					MessageQuene.getInstance(UserApplication.getInstance().getApplicationContext()).addMessage(NX.CHUTES, true);
				}
			}
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
		Intent intent = new Intent();
		intent.setAction(action);
		setResult(RESULT_OK, intent);
		finish();
	}
}
