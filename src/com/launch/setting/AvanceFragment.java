package com.launch.setting;

import java.sql.SQLException;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.EditText;
import android.widget.Toast;

import com.launch.sqlite.Avance;
import com.launch.sqlite.AvanceService;
import com.launch.ui.R;
import com.launch.ui.UserApplication;
import com.launch.utils.PincodeDialog;

public class AvanceFragment extends Fragment implements OnClickListener {

	private EditText police;
	
	private EditText pompiers;
	
	private EditText samu;
	
	private Avance avance;
	
	
	private AvanceService service;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.avance_setting_layout, null);
		view.findViewById(R.id.messages).setOnClickListener(this);
		police = (EditText) view.findViewById(R.id.police);
		pompiers = (EditText) view.findViewById(R.id.pompiers);
		samu = (EditText) view.findViewById(R.id.samu);
		view.findViewById(R.id.update).setOnClickListener(this);
		UserApplication application = (UserApplication) getActivity().getApplication();
		showPincode();
		try {
			service =  application.getHelper().getAvanceService();
			avance = service.getAvance();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		initView();
		return view;
	}

	
	private void initView() {
		if(avance == null) {
			avance = new Avance();
		}
		
		police.setText(avance.getPolice());
		pompiers.setText(avance.getPompiers());
		samu.setText(avance.getSamu());
	}

	public void showPincode() {
		PincodeDialog dialog = new PincodeDialog(getActivity(), R.style.dialog, null);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
	
	private void collect() {
		avance.setPolice(police.getText().toString());
		avance.setPompiers(pompiers.getText().toString());
		avance.setSamu(samu.getText().toString());
		
	}


	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.messages) {
			UserApplication application = (UserApplication) getActivity().getApplication();
			application.showFragment("0051");
		} else if(v.getId() == R.id.update) {
			if(service != null) {
				collect();
				if(service.saveOrUpdateAvance(avance)) {
					Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();
				}
			}
		} 
	}
}
