package com.launch.utils;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

public class GPSUtil {

	/**
	 * ���ص�ǰGPS����״̬��trueΪ������falseΪ�ر�
	 * 
	 * @return
	 */
	public static boolean isOpen(Context context) {
		LocationManager lm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	public static void getLocation(Context context, LocationListener listener) {
		LocationManager lm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		String provider = getBestProvider(context);
		Location location = lm.getLastKnownLocation(provider); // ͨ��GPS��ȡλ��
		if (location != null && listener != null) {
			listener.onLocationChanged(location);
		} else {
			lm.requestLocationUpdates(provider, 1000, 500, listener);
		}
	}

	public static double getDistance(double longitude1, double latitude1,
			double longitude2, double latitude2) {
		double Lat1 = rad(latitude1);
		double Lat2 = rad(latitude2);
		double a = Lat1 - Lat2;
		double b = rad(longitude1) - rad(longitude2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(Lat1) * Math.cos(Lat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * 6378137.0;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	public static final void openGPS(Context context) {
		if(!isOpen(context)) {
			Intent GPSIntent = new Intent();
			GPSIntent.setClassName("com.android.settings",
					"com.android.settings.widget.SettingsAppWidgetProvider");
			GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
			GPSIntent.setData(Uri.parse("custom:3"));
			try {
				PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
			} catch (CanceledException e) {
				e.printStackTrace();
			}
		}

		
	}

	/**
	 * �����õ��ṩ��
	 * 
	 * @param context
	 * @return
	 */
	public static String getBestProvider(Context context) {
		LocationManager lm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// ����Criteria����
		Criteria criteria = new Criteria();
		// ���ö�λ��ȷ�� Criteria.ACCURACY_COARSE �Ƚϴ��ԣ� Criteria.ACCURACY_FINE��ȽϾ�ϸ
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// �����Ƿ���Ҫ������Ϣ Altitude
		criteria.setAltitudeRequired(false);
		// �����Ƿ���Ҫ��λ��Ϣ Bearing
		criteria.setBearingRequired(false);
		// �����Ƿ�������Ӫ���շ�
		criteria.setCostAllowed(true);
		// ���öԵ�Դ������
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		// ��ȡGPS��Ϣ�ṩ��
		String provider = lm.getBestProvider(criteria, true);
		Toast.makeText(context, provider, Toast.LENGTH_SHORT).show();
		return provider;
	}

}
