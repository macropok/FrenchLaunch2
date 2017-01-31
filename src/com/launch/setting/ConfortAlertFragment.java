package com.launch.setting;

import java.sql.SQLException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.launch.sqlite.AlertService;
import com.launch.sqlite.ConfortAlert;
import com.launch.ui.R;
import com.launch.ui.UserApplication;
import com.launch.ui.view.SwitchButton;

public class ConfortAlertFragment extends Fragment implements TextWatcher, OnFocusChangeListener{

	private SwitchButton toggle;
	private EditText radius;

	private ConfortAlert alert;

	private AlertService<ConfortAlert> service;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.confort_alert_setting_layout,
				null);
		toggle = (SwitchButton) view.findViewById(R.id.toggle);
		radius = (EditText) view.findViewById(R.id.radius);
		radius.setOnFocusChangeListener(this);
		radius.addTextChangedListener(this);
		UserApplication application = (UserApplication) getActivity()
				.getApplication();
		try {
			service = (AlertService<ConfortAlert>) application.getHelper()
					.getAlertService(ConfortAlert.class);
			alert = service.getAlert();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		initView();
		return view;
	}

	private void initView() {
		if (alert == null) {
			alert = new ConfortAlert();
		}
		toggle.setChecked(alert.isActive());
		radius.setText(alert.getRadius());
	}

	private void collect() {
		alert.setRadius(radius.length() ==0 ? "150" : radius.getText().toString());
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
				Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}
	

	@Override
	public void onFocusChange(View edittext, boolean hasFocus) {
		if(!hasFocus) {
			if(edittext == radius) {
				String str = radius.getText().toString();
				if(str.length() == 0) {
					radius.setText("150");
					radius.setSelection(3);
				}
			}
			return;
		}
		if(edittext == radius) {
			String str = radius.getText().toString();
			if(str.length() == 3 && str.equals("150")) {
				radius.setText("");
			}
		}
		
	}

	@Override
	public void afterTextChanged(Editable editable) {
		if(editable.length() == 0) {
			return;
		}
		int number = Integer.parseInt(editable.toString());
		if(number >= 150 && number <= 900000) {
			return;
		}
		if(number < 150) {
			number = 150;
		}
		if(number > 900000) {
			number = 900000;
		}
		radius.setText(Integer.toString(number));
		radius.setSelection(radius.getText().length());
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		
	}

}
