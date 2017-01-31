package com.launch.ui;

import java.sql.SQLException;
import java.util.ArrayList;

import com.launch.sqlite.AlertService;
import com.launch.sqlite.ConfortAlert;
import com.launch.ui.view.SwitchButton;
import com.launch.utils.SosFunction;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MesAlertesSecuriteFragment extends Fragment {
	public static MesAlertesSecuriteFragment fragment;

	private View containerView;
	
	ArrayAdapter<Integer> countDownAdapter;
	
	ArrayAdapter<Integer> securiteDistanceAdapter;
	
	private SwitchButton toggle;
	
	private Spinner radius;
	
	private Spinner deactivationTime;

	private ConfortAlert alert;

	private AlertService<ConfortAlert> service;
	
	public static MesAlertesSecuriteFragment getInstance() {
		if (fragment == null) {
			fragment = new MesAlertesSecuriteFragment();
			Bundle bundle = new Bundle();
			bundle.putString("Title", "Mes Alertes");
			bundle.putInt("bgColor", 0xff414b39);
			bundle.putInt("ImageId", R.drawable.zone);
			fragment.setArguments(bundle);
		}
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((PageActivity)getActivity()).setIcon(getArguments().getInt("bgColor"), getArguments().getInt("ImageId"));
		containerView = inflater.inflate(R.layout.mes_alertes_securite_ui, container, false);
		Spinner spinnerCancelCountdown = (Spinner)containerView.findViewById(R.id.spinnerCancelCountdown);
		ArrayList<Integer> vals = new ArrayList<Integer>();
		for(int i=10;i<100;i+=5)
		{
			vals.add(i);
		}
		vals.add(99);
		countDownAdapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, vals);
		countDownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerCancelCountdown.setAdapter(countDownAdapter);
		
		Spinner securiteDistance = (Spinner)containerView.findViewById(R.id.spinnerSecuriteDistanceBeforeAlert);
		ArrayList<Integer> vals1 = new ArrayList<Integer>();
		for(int i=100;i<=1000;i+=50)
		{
			vals1.add(i);
		}
		securiteDistanceAdapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, vals1);
		securiteDistanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		securiteDistance.setAdapter(securiteDistanceAdapter);
		
		containerView.findViewById(R.id.btnBack).setOnClickListener(UserApplication.getInstance().getActivity());
		
		toggle = (SwitchButton) containerView.findViewById(R.id.toggleSecurite);
		radius = (Spinner) containerView.findViewById(R.id.spinnerSecuriteDistanceBeforeAlert);
		UserApplication application = (UserApplication) getActivity()
				.getApplication();
		try {
			service = (AlertService<ConfortAlert>) application.getHelper().getAlertService(ConfortAlert.class);
			alert = service.getAlert();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		initView();
		return containerView;
		
	}
	
	private void initView() {
		if (alert == null) {
			alert = new ConfortAlert();
		}
		toggle.setChecked(alert.isActive());
		radius.setSelection(securiteDistanceAdapter.getPosition(Integer.parseInt(alert.getRadius())));
	}

	private void collect() {
		alert.setRadius(String.valueOf(radius.getSelectedItem()));
		alert.setActive(toggle.isChecked());
//		if(toggle.isChecked()) {
//			Preferences.setAlarm(getActivity(), AlertUtil.LOCATION_ACTION, true);
//		} else {
//			Preferences.setAlarm(getActivity(), AlertUtil.LOCATION_ACTION, false);
//		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (service != null) {
			collect();
			if (service.saveOrUpdateAlert(alert)) {
				//Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
			} else {
				//Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
}
