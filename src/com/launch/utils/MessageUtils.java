package com.launch.utils;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.location.Address;
import android.location.Location;

import com.launch.sqlite.Person;
import com.launch.sqlite.PersonService;
import com.launch.ui.UserApplication;

public class MessageUtils {

	public static final String format = "%1$s %2$s a envoyé un SOS de nature %3$s à l'heure %4$s. Il est localisé à l'endroit %5$s.";

	public enum NX {
		SANTE("Santé"), FEU("Feu"), AGRESSION("Agression"), CHUTES("Chûtes"), INACTIVITE(
				"Inactivité"), BATTERIE("Batterie"), CONFORT("Zone de confort");

		private String name;

		NX(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

	}

	public interface OnPickMessageListener {
		public void onBackAddress(NX nx, Address message);
	}

	public static String getMessageText(NX nx, String location) {
		UserApplication application = UserApplication.getInstance();
		try {
			PersonService personService = application.getHelper()
					.getPersonService();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Person person = personService.getPerson();
			return String.format(format, person.getNom(), person.getPrenom(),
					nx.getName(),
					sdf.format(Calendar.getInstance(Locale.FRENCH).getTime()),
					location);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void getCurrentPositionAddress(
			OnPickMessageListener listener, NX nx) {
		UserApplication application = UserApplication.getInstance();
		Location location = application.getCurrentLocation();
		if (location == null) {
			if (listener != null) {
				listener.onBackAddress(nx, null);
			}
		} else {
			getPostionAddress(nx, listener, location);
		}
	}

	public static Address getCurrentPositionAddress(NX nx) {
		UserApplication application = UserApplication.getInstance();
		Location location = application.getCurrentLocation();
		if (location != null) {
			return getPostionAddress(nx, location);
		}
		return null;
	}

	public static void getPostionAddress(NX nx, OnPickMessageListener listener,
			Location location) {
		System.out.println("start loading " + nx.getName() + " location");
		GetPositionThread thread = new GetPositionThread(nx, listener, location);
		thread.start();
	}

	public static Address getPostionAddress(NX nx, Location location) {
		String url = "http://maps.google.com/maps/api/geocode/json?latlng="
				+ location.getLongitude() + "," + location.getLatitude()
				+ "&language=fr-FR&sensor=true";
		try {
			HttpGet httpGet = new HttpGet(url);
			BasicHttpParams httpParams = new BasicHttpParams();  
	        HttpConnectionParams.setConnectionTimeout(httpParams, 1000*30);  
	        HttpConnectionParams.setSoTimeout(httpParams, 1000*30);  
			HttpResponse response = new DefaultHttpClient(httpParams)
					.execute(httpGet);
			String result = "";
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, HTTP.UTF_8);
			} else {
				return null;
			}

			JSONObject object = new JSONObject(result);
			JSONArray array = object.getJSONArray("results");
			JSONObject address = array.getJSONObject(0);
//			JSONArray add = address.getJSONArray("address_components");
			Address a = new Address(Locale.CHINA);
//			for (int i = 0; i < add.length(); i++) {
//				JSONObject obj = add.getJSONObject(i);
//				if (obj.getString("types").contains("country")) {
//					System.out.println(obj.getString("long_name"));
//					a.setCountryName(obj.getString("long_name"));
//				}
//				if (obj.getString("types").contains("locality")) {
//					System.out.println(obj.getString("long_name"));
//					a.setLocality(obj.getString("long_name"));
//				}
//
//				if (obj.getString("types").contains("route")) {
//					System.out.println(obj.getString("long_name"));
//					a.setSubLocality(obj.getString("long_name"));
//				}
//				
//				if(obj.getString("types").contains("street_number")) {
//					a.setAddressLine(0, object.getString("short_name"));
//				}

//			}
			a.setAddressLine(0, address.getString("formatted_address"));
			a.setLatitude(location.getLatitude());
			a.setLongitude(location.getLongitude());
			return a;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static class GetPositionThread extends Thread {
		private OnPickMessageListener listener;
		private Location location;
		private NX nx;

		public GetPositionThread(NX nx, OnPickMessageListener listener,
				Location location) {
			this.location = location;
			this.listener = listener;
			this.nx = nx;
		}

		@Override
		public void run() {
			String url = "http://maps.google.com/maps/api/geocode/json?latlng="
					+ location.getLatitude() + "," + location.getLongitude()
					+ "&language=fr-FR&sensor=true";
			try {
				HttpGet httpGet = new HttpGet(url);
				HttpResponse response = new DefaultHttpClient()
						.execute(httpGet);
				String result = "";
				if (response.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = response.getEntity();
					result = EntityUtils.toString(entity, HTTP.UTF_8);
				}

				JSONObject object = new JSONObject(result);
				JSONArray array = object.getJSONArray("results");
				JSONObject address = array.getJSONObject(0);
				JSONArray add = address.getJSONArray("address_components");
				Address a = new Address(Locale.CHINA);
				for (int i = 0; i < add.length(); i++) {
					JSONObject obj = add.getJSONObject(i);
					if (obj.getString("types").contains("country")) {
						System.out.println(obj.getString("long_name"));
						a.setCountryName(obj.getString("long_name"));
					}
					if (obj.getString("types").contains("locality")) {
						System.out.println(obj.getString("long_name"));
						a.setLocality(obj.getString("long_name"));
					}

					if (obj.getString("types").contains("route")) {
						System.out.println(obj.getString("long_name"));
						a.setSubLocality(obj.getString("long_name"));
					}

				}
				if (listener != null) {
					listener.onBackAddress(nx, a);
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (listener != null) {
					listener.onBackAddress(nx, null);
				}
			}
		}

	}
}
