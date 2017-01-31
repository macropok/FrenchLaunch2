package com.launch.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.launch.sqlite.AlertService;
import com.launch.sqlite.BatterieAlert;
import com.launch.sqlite.ChuteAlert;
import com.launch.sqlite.ConfortAlert;
import com.launch.sqlite.InactiviteAlert;
import com.launch.ui.UserApplication;
import com.launch.utils.AlarmQueue;
import com.launch.utils.GPSUtil;
import com.launch.utils.MessageQuene;
import com.launch.utils.MessageUtils.NX;
import com.launch.utils.Preferences;
import com.launch.utils.VibratorManager;
import com.launch.utils.VibratorManager.SensorChangedListener;

public class ChuteService extends Service {

	public final static int ALL_TYPE = 1;
	private LocationListener locationListener;
	private BatterieBroadcastReceiver batterieReceiver;
    private String TAG = "test";

    private LocationClient mLocationClient;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent == null) {
			return START_NOT_STICKY;
		}
		int type = intent.getIntExtra("type", -1);
		if(type == -1) {
			return START_NOT_STICKY;
		}
		switch(type) {		
		case ALL_TYPE:
			launchChute();
			launchBatterieAlert();
			launchLocationAlert();
			initLocationClient();
			break;
		}
		return super.onStartCommand(intent, flags, startId);
	}
	private boolean checkLevel(int level, double t) {
		if((level == 2 && t > 2) || (level == 1 && t > 3) || (level == 0 && t >4)) {
			return true;
		}
		return false;
	}
	
	private SensorChangedListener changeListener = new SensorChangedListener() {

		@Override
		public void change(double t) {
			AlertService<ChuteAlert> service = UserApplication.getInstance()
					.getChuteService();
			ChuteAlert alert = service.getAlert();
//			System.out.println(t);
			if (alert != null && alert.isActive() && checkLevel(alert.getLevel(), t)) {
				// ˤ��
				System.out.println("ˤ��");
				
				//long time = (alert.getDeactivate()) * 60 * 1000;
				long time = (alert.getDeactivate()) * 1000;
				MessageQuene.getInstance(ChuteService.this).addMessage(NX.CHUTES, time, true);
			}
		}
		
	};
	
	private void launchChute() {
		VibratorManager.getInstance().initConfigure(this, changeListener);
	}
	
	private void initLocationClient() {
        mLocationClient = new LocationClient(this, new ConnectionCallbacks() {

            @Override
            public void onDisconnected() {
                Log.d(TAG, "onDisconnected() - ����ʧ��");
            }

            @Override
            public void onConnected(Bundle arg0) {

                Log.d(TAG, "onConnected() - ���ӳɹ�");

                getLocation();

            }
        }, new OnConnectionFailedListener() {

            @Override
            public void onConnectionFailed(ConnectionResult arg0) {

                Log.d(TAG, "onConnectionFailed() - ����ʧ��");

            }
        });
        
        locationHandler.sendEmptyMessageDelayed(0, 1000);
    }

	private boolean servicesConnected() {

        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        }

        // Google Play services was not available for some reason
        else {
            return false;
        }
    }

	private Handler locationHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (mLocationClient.isConnected()) {
                getLocation();
            } else {
                mLocationClient.connect();
            }
			sendEmptyMessageDelayed(0, 1000);
		};
	};
	
	private void getLocation() {
        // If Google Play Services is available
        if (servicesConnected()) {

            // Get the current location
            Location currentLocation = mLocationClient.getLastLocation();

            Log.d("test", "" + currentLocation);
            UserApplication.getInstance().setCurrentLocation(currentLocation);
            if (currentLocation != null) {
                Log.d(TAG, "" + currentLocation.getLatitude());
                Log.d(TAG, "" + currentLocation.getLongitude());
            }

        }
    }
	
	public void launchLocationAlert() {
		if (locationListener != null) {
			return;
		}

		locationListener = new MyLocationListener();
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(GPSUtil.getBestProvider(this), 500, 1,
				locationListener);
	}
	
	public void launchBatterieAlert() {
		if (batterieReceiver != null) {
			return;
		}
		batterieReceiver = new BatterieBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(batterieReceiver, filter);
	}
	

	public void closeBattireAlert() {
		unregisterReceiver(batterieReceiver);
		batterieReceiver = null;
	}

	
	@Override
	public void onDestroy() {
		super.onDestroy();
		VibratorManager.getInstance().unregisterListener();
		closeBattireAlert();
		/*
		 * �ͷ���Դ
		 */

		UserApplication.getInstance().onDestory();
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.removeUpdates(locationListener);
		locationListener = null;
		
	}
	
	
	
	private class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			AlertService<ConfortAlert> service = UserApplication.getInstance()
					.getConfortService();
			ConfortAlert confortAlert = service.getAlert();
			// double lo1 = 108.90, la1 = 34.1;// ��һ����γ��
			// double lo2 = 115.4648060, la2 = 38.8738910;// �ڶ�����γ��
			double lo1 = Preferences.getLng(ChuteService.this), la1 = Preferences
					.getLat(ChuteService.this);// ��һ����γ��
			Location last = UserApplication.getInstance().getCurrentLocation();

			double lo2 = location.getLongitude(), la2 = location.getLatitude();// �ڶ�����γ��
			if ((lo1 != 0 && la1 != 0 && confortAlert != null)
					&& GPSUtil.getDistance(lo1, la1, lo2, la2) > Integer
							.parseInt(confortAlert.getRadius())
					&& confortAlert != null && confortAlert.isActive()) {
				System.out.println("send location message");
				MessageQuene.getInstance(ChuteService.this).addMessage(NX.CONFORT, false);
			} else if (last != null
					&& GPSUtil.getDistance(last.getLongitude(),
							last.getLatitude(), lo2, la2) < 3
					&& Preferences.isAlarm(ChuteService.this,
							AlarmQueue.INACTIVITY_ACTION)) {
				AlertService<InactiviteAlert> inactiviteservice = UserApplication
						.getInstance().getInactiviteService();
				InactiviteAlert inactiviteAlert = inactiviteservice.getAlert();
				if (inactiviteAlert != null) {
					long time = System.currentTimeMillis()
							+ (inactiviteAlert.getDesarmocage() + (inactiviteAlert
									.getLalerte()) * 60) * 60 * 1000;
					AlarmQueue.getInstance().registerAlarmTime(
							ChuteService.this, AlarmQueue.INACTIVITY_ACTION,
							time);
				}
			}
			UserApplication.getInstance().setCurrentLocation(location);

		}

		@Override
		public void onProviderDisabled(String arg0) {

		}

		@Override
		public void onProviderEnabled(String arg0) {

		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

		}

	}
	
	class BatterieBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			UserApplication application = UserApplication.getInstance();
			BatterieAlert batterieAlert = application.getBatterieService()
					.getAlert();

			if (batterieAlert == null || !batterieAlert.isActive()) {
				return;
			}
			int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
			int batterie = -1;
			if (rawlevel >= 0 && scale > 0) {
				batterie = (rawlevel * 100) / scale;
			}
			if (batterie <= batterieAlert.getMinbatterie()) {
				System.out.println("�����������");
				long time = System.currentTimeMillis()
						+ (batterieAlert.getTime()) * 60 * 1000;
				AlarmQueue.getInstance().registerAlarmTime(ChuteService.this,
						AlarmQueue.BATTERIE_ACTION, time);

				// MessageQuene.getInstance().addMessage(NX.BATTERIE, true);
			}

		}

	}
	
	

}

