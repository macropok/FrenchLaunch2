package com.launch.setting;

import java.sql.SQLException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.launch.sqlite.Avance;
import com.launch.sqlite.AvanceService;
import com.launch.ui.R;
import com.launch.ui.UserApplication;

public class MessageFragment extends Fragment implements OnClickListener {

	private TextView audio;

	private TextView text;

	private Avance avance;

	private AvanceService service;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.message_setting_layout, null);
		audio = (EditText) view.findViewById(R.id.audio);
		text = (EditText) view.findViewById(R.id.text);
		UserApplication application = (UserApplication) getActivity()
				.getApplication();
		view.findViewById(R.id.update).setOnClickListener(this);
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
		return view;
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
