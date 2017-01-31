package com.launch.setting;

import java.sql.SQLException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.launch.sqlite.AlertService;
import com.launch.sqlite.BatterieAlert;
import com.launch.ui.R;
import com.launch.ui.UserApplication;
import com.launch.ui.view.SwitchButton;

public class BatterieAlertFragment extends Fragment implements TextWatcher, OnFocusChangeListener {

	private SwitchButton toggle;
	
//	private AbstractWheel minBatterie;
//	
//	
//	private AbstractWheel deactivateTime;
	
	private EditText minBatterie;
	
	private EditText deactivateTime;
	
	
	private BatterieAlert alert;
	
	private AlertService<BatterieAlert> service;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.batterie_alert_setting_layout, null);
		toggle = (SwitchButton) view.findViewById(R.id.toggle);
//		deactivateTime = (WheelVerticalView) view.findViewById(R.id.deactivateTime);
//		minBatterie = (WheelVerticalView) view.findViewById(R.id.minBatterie);
		
		deactivateTime = (EditText) view.findViewById(R.id.deactivateTime);
		deactivateTime.setOnFocusChangeListener(this);
		minBatterie = (EditText) view.findViewById(R.id.minBatterie);
		minBatterie.setOnFocusChangeListener(this);
		
		deactivateTime.addTextChangedListener(this);
		minBatterie.addTextChangedListener(this);
		UserApplication application = (UserApplication) getActivity().getApplication();
		try {
			service =  (AlertService<BatterieAlert>) application.getHelper().getAlertService(BatterieAlert.class);
			alert = service.getAlert();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		initView();
		return view;
	}

	
	private void initView() {
		if(alert == null) {
			alert = new BatterieAlert();
		}
		toggle.setChecked(alert.isActive());
//
//		deactivateTime.setViewAdapter(new TimeWheelAdapter(getActivity(), TimeUtils.getBatterieArray()));
//		deactivateTime.setCurrentItem(alert.getTime());
//		
//		minBatterie.setViewAdapter(new TimeWheelAdapter(getActivity(), TimeUtils.getPreArray()));
//		minBatterie.setCurrentItem(alert.getMinbatterie());
		
		deactivateTime.setText(Integer.toString(alert.getTime()));
		minBatterie.setText(Integer.toString(alert.getMinbatterie()));
	}
	
	

	private void collect() {
//		alert.setMinbatterie(minBatterie.getCurrentItem());
//		alert.setTime(deactivateTime.getCurrentItem());
		alert.setMinbatterie(minBatterie.length() == 0 ? 1 : Integer.parseInt(minBatterie.getText().toString()));
		alert.setTime(deactivateTime.length() == 0 ? 1 : Integer.parseInt(deactivateTime.getText().toString()));
		alert.setActive(toggle.isChecked());
	}

	
	@Override
	public void onPause() {
		super.onPause();
		if (service != null) {
			collect();
			if (service.saveOrUpdateAlert(alert)) {
				Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}


	@Override
	public void afterTextChanged(Editable editable) {
		if(editable.length() == 0) {
			return;
		}
		if(editable == deactivateTime.getEditableText()) {
			try {		
				int number = Integer.parseInt(deactivateTime.getText().toString());
				if(number >=1 && number <= 30) {
					return;
				}
				if(number > 30) {
					number = 30;
				}
				if(number < 1) {
					number = 1;
				}
				deactivateTime.setText(number + "");
				deactivateTime.setSelection(deactivateTime.length());
			} catch(NumberFormatException e) {
				deactivateTime.setText("1");
				deactivateTime.setSelection(deactivateTime.length());
			}
		} else if(editable == minBatterie.getEditableText()) {
			try {		
				int number = Integer.parseInt(minBatterie.getText().toString());
				if(number >=1 && number <= 100) {
					return;
				}
				if(number > 100) {
					number = 100;
				}
				if(number < 1) {
					number = 1;
				}
				minBatterie.setText(number + "");
				minBatterie.setSelection(minBatterie.length());
			} catch(NumberFormatException e) {
				minBatterie.setText("1");
				minBatterie.setSelection(1);
			}
		}		
	}


	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		
	}


	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onFocusChange(View edittext, boolean hasFocus) {
		if(!hasFocus) {
			if(edittext == minBatterie) {
				String str = minBatterie.getText().toString();
				if(str.length() == 0) {
					minBatterie.setText("1");
					minBatterie.setSelection(1);
				}
			} else if(edittext == deactivateTime) {
				String str = deactivateTime.getText().toString();
				if(str.length() == 0) {
					deactivateTime.setText("1");
					deactivateTime.setSelection(1);
				}
			}
			return;
		}
		if(edittext == minBatterie) {
			String str = minBatterie.getText().toString();
			if(str.length() == 1 && str.equals("1")) {
				minBatterie.setText("");
			}
		} else if(edittext == deactivateTime) {
			String str = deactivateTime.getText().toString();
			if(str.length() == 1 && str.equals("1")) {
				deactivateTime.setText("");
			}
		}
		
	}

}
