package com.launch.ui;

import java.sql.SQLException;
import java.util.ArrayList;

import com.launch.sqlite.AlertService;
import com.launch.sqlite.BatterieAlert;
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

public class MesAlertesBatterieFragment extends Fragment {
	public static MesAlertesBatterieFragment fragment;

	private View containerView;
	
	ArrayAdapter<Integer> countDownAdapter;
	
	ArrayAdapter<Integer> batterieMinimumAdapter;
	
	private SwitchButton toggle;
	
	private Spinner minBatterie;
	
	private Spinner deactivateTime;
	
	
	private BatterieAlert alert;
	
	private AlertService<BatterieAlert> service;
	
	public static MesAlertesBatterieFragment getInstance() {
		if (fragment == null) {
			fragment = new MesAlertesBatterieFragment();
			Bundle bundle = new Bundle();
			bundle.putString("Title", "Mes Alertes");
			bundle.putInt("bgColor", 0xff414b39);
			bundle.putInt("ImageId", R.drawable.batterie);
			fragment.setArguments(bundle);
		}
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((PageActivity)getActivity()).setIcon(getArguments().getInt("bgColor"), getArguments().getInt("ImageId"));
		containerView = inflater.inflate(R.layout.mes_alertes_batterie_ui, container, false);
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
		
		Spinner batterieMinimum = (Spinner)containerView.findViewById(R.id.spinnerBatterieMinimumBeforeAlert);
		ArrayList<Integer> vals1 = new ArrayList<Integer>();
		for(int i=5;i<100;i+=5)
		{
			vals1.add(i);
		}
		vals1.add(99);
		batterieMinimumAdapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, vals1);
		batterieMinimumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		batterieMinimum.setAdapter(batterieMinimumAdapter);
		
		//batterieMinimum.setSelection(3);
		containerView.findViewById(R.id.btnBack).setOnClickListener(UserApplication.getInstance().getActivity());
		
		toggle = (SwitchButton) containerView.findViewById(R.id.toggleBatterie);
//		deactivateTime = (WheelVerticalView) view.findViewById(R.id.deactivateTime);
//		minBatterie = (WheelVerticalView) view.findViewById(R.id.minBatterie);
		
		deactivateTime = (Spinner) containerView.findViewById(R.id.spinnerCancelCountdown);
		minBatterie = (Spinner) containerView.findViewById(R.id.spinnerBatterieMinimumBeforeAlert);
		
		UserApplication application = (UserApplication) getActivity().getApplication();
		try {
			service =  (AlertService<BatterieAlert>) application.getHelper().getAlertService(BatterieAlert.class);
			alert = service.getAlert();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		initView();
		return containerView;
		
	}
	
	private void initView() {
		if(alert == null) {
			alert = new BatterieAlert();
		}
		toggle.setChecked(alert.isActive());

		deactivateTime.setSelection(countDownAdapter.getPosition(alert.getTime()));
		minBatterie.setSelection(batterieMinimumAdapter.getPosition(alert.getMinbatterie()));
	}
	
	

	private void collect() {
//		alert.setMinbatterie(minBatterie.getCurrentItem());
//		alert.setTime(deactivateTime.getCurrentItem());
		alert.setMinbatterie((Integer)minBatterie.getSelectedItem());
		alert.setTime((Integer)deactivateTime.getSelectedItem());
		alert.setActive(toggle.isChecked());
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
