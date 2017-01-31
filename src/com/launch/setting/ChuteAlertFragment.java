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
import android.widget.Spinner;
import android.widget.Toast;

import com.launch.sqlite.AlertService;
import com.launch.sqlite.ChuteAlert;
import com.launch.ui.R;
import com.launch.ui.UserApplication;
import com.launch.ui.view.SwitchButton;

public class ChuteAlertFragment extends Fragment implements TextWatcher , OnFocusChangeListener{

	private SwitchButton toggle;
//	private AbstractWheel deactivateTime;
	private EditText deactivateTime;
	private ChuteAlert alert;
	
	private AlertService<ChuteAlert> service;
	private Spinner levels;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.chute_alert_setting_layout, null);
		toggle = (SwitchButton) view.findViewById(R.id.toggle);
		levels = (Spinner) view.findViewById(R.id.levels);
		UserApplication application = (UserApplication) getActivity().getApplication();
//		deactivateTime = (WheelVerticalView) view.findViewById(R.id.deactivateTime);
		deactivateTime = (EditText) view.findViewById(R.id.deactivateTime);
		deactivateTime.setOnFocusChangeListener(this);
		deactivateTime.addTextChangedListener(this);
		try {
			service =  (AlertService<ChuteAlert>) application.getHelper().getAlertService(ChuteAlert.class);
			alert = service.getAlert();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		initView();
		return view;
	}

	
	private void initView() {
		if(alert == null) {
			alert = new ChuteAlert();
		}
		toggle.setChecked(alert.isActive());
		deactivateTime.setText(Integer.toString(alert.getDeactivate()));
		levels.setSelection(alert.getLevel());
//		deactivateTime.setViewAdapter(new TimeWheelAdapter(getActivity(), TimeUtils.getMinteArray()));
//		deactivateTime.setCurrentItem(alert.getDeactivate());
	}
	
	

	private void collect() {
		if(deactivateTime.length() == 0) {
			alert.setDeactivate(0);
		} else { 
			alert.setDeactivate(Integer.parseInt(deactivateTime.getText().toString()));
		}
		
		alert.setLevel(levels.getSelectedItemPosition());
		
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
	public void afterTextChanged(Editable text) {
	}


	@Override
	public void beforeTextChanged(CharSequence str, int arg1, int arg2,
			int arg3) {
		
	}


	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		if(arg0.length() == 0) {
			return;
		}
		try {		
			int number = Integer.parseInt(deactivateTime.getText().toString());
			if(number >=0 && number <= 30) {
				return;
			}
			if(number > 30) {
				number = 30;
			}
			if(number < 0) {
				number = 0;
			}
			deactivateTime.setText(number + "");
			deactivateTime.setSelection(deactivateTime.getText().length());
		} catch(NumberFormatException e) {
			deactivateTime.setText("0");
		}
	}


	@Override
	public void onFocusChange(View arg0, boolean hasFocus) {
		if(!hasFocus) {
			return;
		}
		String str = deactivateTime.getText().toString();
		if(str.length() == 1 && str.equals("0")) {
			deactivateTime.setText("");
		}
	}
	
	
}
