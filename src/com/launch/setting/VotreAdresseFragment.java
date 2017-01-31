package com.launch.setting;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.launch.sqlite.Person;
import com.launch.sqlite.PersonService;
import com.launch.ui.R;
import com.launch.ui.UserApplication;
import com.launch.ui.view.SwitchButton;
import com.launch.utils.GPSUtil;
import com.launch.utils.Preferences;

public class VotreAdresseFragment extends Fragment implements LocationListener,
		OnClickListener, OnCheckedChangeListener {

	private EditText rue;

	private EditText ville;

	private SwitchButton toggle;

	private EditText pay;

	private Button update;

	private PersonService service;

	private Person person;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.votre_adresse_setting_layout,
				null);
		rue = (EditText) view.findViewById(R.id.rue);
		ville = (EditText) view.findViewById(R.id.ville);
		pay = (EditText) view.findViewById(R.id.pay);
		update = (Button) view.findViewById(R.id.update);
		update.setOnClickListener(this);
		toggle = (SwitchButton) view.findViewById(R.id.toggle);
		toggle.setOnCheckedChangeListener(this);
		UserApplication application = (UserApplication) getActivity()
				.getApplication();
		try {
			service = application.getHelper().getPersonService();
			person = service.getPerson();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		initView();

		return view;
	}

	private void initView() {
		if (person == null) {
			person = new Person();
		}
		rue.setText(person.getRue());
		ville.setText(person.getVille());
		pay.setText(person.getPay());
		toggle.setChecked(person.isActive());
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		person.setActive(isChecked);
		if (isChecked) {
			GPSUtil.getLocation(getActivity(), this);
		}
	}

	private void collect() {
		person.setRue(rue.getText().toString());
		person.setVille(ville.getText().toString());
		person.setPay(pay.getText().toString());
		person.setActive(toggle.isChecked());
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.update) {
			if (service != null) {
				collect();
				if (service.saveOrUpdatePerson(person)) {
					Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT)
							.show();
				}
			}
		}
	}

	@Override
	public void onLocationChanged(final Location location) {
		if (location == null) {
			return;
		}
		Preferences.saveLocation(getActivity(), location.getLatitude(),
				location.getLongitude());
		new Thread(new Runnable() {

			@Override
			public void run() {
				String url = "http://maps.google.com/maps/api/geocode/json?latlng="
						+ location.getLatitude()
						+ ","
						+ location.getLongitude()
						+ "&language=fr-FR&sensor=true";
				Address a = null;
				try {
					HttpGet httpGet = new HttpGet(url);
					BasicHttpParams httpParams = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParams,
							1000 * 30);
					HttpConnectionParams.setSoTimeout(httpParams, 1000 * 30);
					HttpResponse response = new DefaultHttpClient(httpParams)
							.execute(httpGet);

					String result = "";
					if (response.getStatusLine().getStatusCode() == 200) {
						HttpEntity entity = response.getEntity();
						result = EntityUtils.toString(entity, HTTP.UTF_8);
						JSONObject object = new JSONObject(result);
						JSONArray array = object.getJSONArray("results");
						JSONObject address = array.getJSONObject(0);
						JSONArray add = address
								.getJSONArray("address_components");
						a = new Address(Locale.CHINA);
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
							if (obj.getString("types").contains("street_number")) {
								System.out.println(obj.getString("short_name"));
								a.setPhone(obj.getString("short_name"));
							}

						}
						LocationManager lm = (LocationManager) getActivity()
								.getSystemService(Context.LOCATION_SERVICE);
						lm.removeUpdates(VotreAdresseFragment.this);
					}

				} catch (ClientProtocolException e1) {
					e1.printStackTrace();
				} catch (ParseException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Message msg = Message.obtain();
				msg.obj = a;
				handler.sendMessage(msg);
			}
		}).start();
		// System.out.println("location " + location.getLongitude() + ", "
		// + location.getLatitude());
		// Geocoder coder = new Geocoder(getActivity());
		// try {
		// List<Address> address = coder.getFromLocation(
		// location.getLatitude(), location.getLongitude(), 10);
		// if (address.size() > 0) {
		// update(address.get(0));
		// }
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Address address = (Address) msg.obj;
			update(address);
		};
	};

	private void update(Address address) {
		if (address == null) {
			return;
		}
		rue.setText(address.getSubLocality() + "," + address.getPhone());
		ville.setText(address.getLocality());
		pay.setText(address.getCountryName());
	}

	@Override
	public void onProviderDisabled(String arg0) {

	}

	@Override
	public void onProviderEnabled(String arg0) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		System.out.println(status);
	}
}
