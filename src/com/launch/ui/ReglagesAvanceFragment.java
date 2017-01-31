package com.launch.ui;

import java.sql.SQLException;

import com.launch.sqlite.Avance;
import com.launch.sqlite.AvanceService;
import com.launch.utils.PincodeDialog;
import com.launch.utils.SosFunction;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ReglagesAvanceFragment extends Fragment implements OnClickListener {
	public static ReglagesAvanceFragment fragment;

	private View containerView;
	
	private EditText police;
	
	private EditText pompiers;
	
	private EditText samu;
	
	private Avance avance;
	
	
	private AvanceService service;
	
	public static ReglagesAvanceFragment getInstance() {
		if (fragment == null) {
			fragment = new ReglagesAvanceFragment();
			Bundle bundle = new Bundle();
			bundle.putString("Title", "R�glagess Avance");
			bundle.putInt("bgColor", 0xff091302);
			bundle.putInt("ImageId", R.drawable.setting_icon);
			fragment.setArguments(bundle);
		}
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((PageActivity)getActivity()).setIcon(getArguments().getInt("bgColor"), getArguments().getInt("ImageId"));
		containerView = inflater.inflate(R.layout.reglages_avance_ui, container, false);
		containerView.findViewById(R.id.btnBack).setOnClickListener(UserApplication.getInstance().getActivity());
		containerView.findViewById(R.id.messages).setOnClickListener(this);
		police = (EditText) containerView.findViewById(R.id.police);
		pompiers = (EditText) containerView.findViewById(R.id.pompiers);
		samu = (EditText) containerView.findViewById(R.id.samu);
		containerView.findViewById(R.id.update).setOnClickListener(this);
		UserApplication application = (UserApplication) getActivity().getApplication();
		showPincode();
		try {
			service =  application.getHelper().getAvanceService();
			avance = service.getAvance();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		initView();
		return containerView;
		
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
		PincodeDialog dialog = new PincodeDialog(getActivity(), R.style.dialog, containerView.findViewById(R.id.btnBack));
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
			UserApplication.getInstance().getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contaier, ReglagesAvanceMessageSettingFragment.getInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
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
