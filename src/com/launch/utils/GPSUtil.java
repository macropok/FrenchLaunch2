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
	 * 返回当前GPS开启状态，true为开启，false为关闭
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
		Location location = lm.getLastKnownLocation(provider); // 通过GPS获取位置
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
	 * 获得最好的提供商
	 * 
	 * @param context
	 * @return
	 */
	public static String getBestProvider(Context context) {
		LocationManager lm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// 定义Criteria对象
		Criteria criteria = new Criteria();
		// 设置定位精确度 Criteria.ACCURACY_COARSE 比较粗略， Criteria.ACCURACY_FINE则比较精细
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// 设置是否需要海拔信息 Altitude
		criteria.setAltitudeRequired(false);
		// 设置是否需要方位信息 Bearing
		criteria.setBearingRequired(false);
		// 设置是否允许运营商收费
		criteria.setCostAllowed(true);
		// 设置对电源的需求
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		// 获取GPS信息提供者
		String provider = lm.getBestProvider(criteria, true);
		Toast.makeText(context, provider, Toast.LENGTH_SHORT).show();
		return provider;
	}

}
