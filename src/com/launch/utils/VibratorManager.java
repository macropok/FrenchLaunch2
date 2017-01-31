package com.launch.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class VibratorManager {

	private static VibratorManager manager;
	private SensorManager sensorManager;
	private SensorEventListener sensorListener;
	private int rockCount = 0;

	private VibratorManager() {

	}

	public static VibratorManager getInstance() {
		if (manager == null) {
			manager = new VibratorManager();
		}
		return manager;
	}
	
	

	public void initConfigure(Context context,
			final SensorChangedListener listener) {
		sensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		Sensor sensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorListener = new SensorEventListener() {
			@SuppressWarnings("deprecation")
			public void onSensorChanged(SensorEvent e) {
				float[] values = e.values;
				double x2 = values[0]/SensorManager.GRAVITY_EARTH; 
				double  y2 = values[1]/SensorManager.GRAVITY_EARTH; 
				double z2 = values[2]/SensorManager.GRAVITY_EARTH; 

				double t = Math.sqrt(Math.abs(x2)*Math.abs(x2) + 
						Math.abs(y2)*Math.abs(y2) + Math.abs(z2)*Math.abs(z2)); 

				
				String str = "X£º" + values[0] + "£¬Y£º" + values[1] + "£¬Z£º"
						+ values[2];
				
//				if(t > 2.5) {
					if (listener != null) {
						listener.change(t);
					}
//				}
				
//				if (e.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//					if (rockCount > 0) {
//						rockCount = 0;
//						if (listener != null) {
//							listener.change();
//						}
//						return;
//					}
//					float newX = e.values[SensorManager.DATA_X];
//					float newY = e.values[SensorManager.DATA_Y];
//					float newZ = e.values[SensorManager.DATA_Z];
//					System.out.println(str);
//					if ((newZ >= 19 || newY >= 19 || newX > 19)) {
//						rockCount++;
//						System.out.println(str);
//						return;
//					}
////					if ((newZ <= -10)
////							&& rockCount % 2 == 1) {
////						rockCount++;
////						System.out.println(str);
////						return;
////					}
//
//				}
			}

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {

			}
		};

		sensorManager.registerListener(sensorListener, sensor,
				SensorManager.SENSOR_DELAY_GAME);
	}

	public void unregisterListener() {
		if (sensorManager != null) {
			sensorManager.unregisterListener(sensorListener);
		}
		manager = null;
	}

	public interface SensorChangedListener {

		void change(double t);

	}
}
