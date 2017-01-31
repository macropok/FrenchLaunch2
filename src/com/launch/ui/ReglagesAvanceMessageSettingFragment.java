package com.launch.ui;

import java.sql.SQLException;

import com.launch.sqlite.Avance;
import com.launch.sqlite.AvanceService;
import com.launch.utils.SosFunction;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ReglagesAvanceMessageSettingFragment extends Fragment implements OnClickListener {
	public static ReglagesAvanceMessageSettingFragment fragment;

	private View containerView;
	
	private TextView audio;

	private TextView text;

	private Avance avance;

	private AvanceService service;
	
	public static ReglagesAvanceMessageSettingFragment getInstance() {
		if (fragment == null) {
			fragment = new ReglagesAvanceMessageSettingFragment();
			Bundle bundle = new Bundle();
			bundle.putString("Title", "R�glagess Avance Message Setting");
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
		containerView = inflater.inflate(R.layout.reglages_avance_message_setting_ui, container, false);
		containerView.findViewById(R.id.btnBack).setOnClickListener(UserApplication.getInstance().getActivity());
		
		audio = (EditText) containerView.findViewById(R.id.audio);
		text = (EditText) containerView.findViewById(R.id.text);
		UserApplication application = (UserApplication) getActivity()
				.getApplication();
		containerView.findViewById(R.id.update).setOnClickListener(this);
		try {
			service = application.getHelper().getAvanceService();
			avance = service.getAvance();
			if(TextUtils.isEmpty(avance.getMessageText())) {
				avance.setMessageText(getResources().getString(R.string.message_text));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		initView();
		return containerView;
		
	}
	
	private void initView() {
		if (avance == null) {
			avance = new Avance();
		}
		audio.setText(avance.getMessageAudio());
		text.setText(avance.getMessageText());
	}

	private void collect() {
		avance.setMessageAudio(audio.getText().toString());
		avance.setMessageText(text.getText().toString());
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.update) {
			if (service != null) {
				collect();
				if (service.saveOrUpdateAvance(avance)) {
					Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT)
							.show();
				}
			}
		}
	}
	
}
