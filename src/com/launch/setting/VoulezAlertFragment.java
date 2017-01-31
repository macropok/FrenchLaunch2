package com.launch.setting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.launch.ui.R;
import com.launch.utils.Preferences;

public class VoulezAlertFragment extends Fragment {

	public enum Voulez {
		NON, ALERT, PARTOUT;
	}

	// private SwitchButton toggle;

	private Voulez longClick = Voulez.NON;
	private RadioGroup rg;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.voulez_alert_setting_layout, null);
		// toggle = (SwitchButton) view.findViewById(R.id.toggle);
		rg = (RadioGroup) view.findViewById(R.id.group);
		initView();
		return view;
	}

	private void initView() {
		longClick = Preferences.getLongClick(getActivity());
		if (longClick == Voulez.NON) {
			rg.check(R.id.non);
		} else if (longClick == Voulez.ALERT) {
			rg.check(R.id.alert);
		} else if (longClick == Voulez.PARTOUT) {
			rg.check(R.id.partout);
		}
	}

	private void collect() {
		int id = rg.getCheckedRadioButtonId();
		if (id == R.id.non) {
			Preferences.setLongClick(getActivity(), Voulez.NON);
		} else if (id == R.id.alert) {
			Preferences.setLongClick(getActivity(), Voulez.ALERT);
		} else if (R.id.partout == id) {
			Preferences.setLongClick(getActivity(), Voulez.PARTOUT);
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		collect();
	}
}
