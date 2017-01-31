package com.launch.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import com.launch.sqlite.AlertService;
import com.launch.sqlite.ChuteAlert;
import com.launch.ui.view.SwitchButton;
import com.launch.utils.SosFunction;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class MesAlertesChuteFragment extends Fragment {
	public static MesAlertesChuteFragment fragment;

	private View containerView;
	
	ArrayAdapter<Integer> countDownAdapter;
	
	private SwitchButton toggle;
//	private AbstractWheel deactivateTime;
	private Spinner deactivateTime;
	private ChuteAlert alert;
	
	private AlertService<ChuteAlert> service;
	private RadioGroup levels;
	private int[] radioIds = new int[] {R.id.radioLow, R.id.radioMedium, R.id.radioHigh};
	
	public static MesAlertesChuteFragment getInstance() {
		if (fragment == null) {
			fragment = new MesAlertesChuteFragment();
			Bundle bundle = new Bundle();
			bundle.putString("Title", "Mes Alertes");
			bundle.putInt("bgColor", 0xff414b39);
			bundle.putInt("ImageId", R.drawable.chute);
			fragment.setArguments(bundle);
		}
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((PageActivity)getActivity()).setIcon(getArguments().getInt("bgColor"), getArguments().getInt("ImageId"));
		containerView = inflater.inflate(R.layout.mes_alertes_chute_ui, container, false);
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
		
		toggle = (SwitchButton) containerView.findViewById(R.id.toggleChute);
		levels = (RadioGroup) containerView.findViewById(R.id.radioSensibilityRate);
		UserApplication application = (UserApplication) getActivity().getApplication();
//		deactivateTime = (WheelVerticalView) view.findViewById(R.id.deactivateTime);
		deactivateTime = (Spinner) containerView.findViewById(R.id.spinnerCancelCountdown);
		
		try {
			service =  (AlertService<ChuteAlert>) application.getHelper().getAlertService(ChuteAlert.class);
			alert = service.getAlert();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//alert.setLevel(0);
		initView();
		containerView.findViewById(R.id.btnBack).setOnClickListener(UserApplication.getInstance().getActivity());
		return containerView;
		
	}
	private void initView() {
		if(alert == null) {
			alert = new ChuteAlert();
		}
		toggle.setChecked(alert.isActive());
		
		deactivateTime.setSelection(countDownAdapter.getPosition(alert.getDeactivate()));
		levels.check(radioIds[alert.getLevel()]);
		
//		deactivateTime.setViewAdapter(new TimeWheelAdapter(getActivity(), TimeUtils.getMinteArray()));
//		deactivateTime.setCurrentItem(alert.getDeactivate());
	}
	private void collect() {
		alert.setDeactivate((Integer)deactivateTime.getSelectedItem());
		int i=0;
		for(i=0;i<radioIds.length;i++)
			if(radioIds[i]==levels.getCheckedRadioButtonId())
				break;
		alert.setLevel(i);
		
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
