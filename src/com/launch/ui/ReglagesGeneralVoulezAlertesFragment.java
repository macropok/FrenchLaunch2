package com.launch.ui;

import com.launch.setting.VoulezAlertFragment.Voulez;
import com.launch.utils.Preferences;
import com.launch.utils.SosFunction;
import com.launch.utils.ThirdLaunch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.RadioGroup;

public class ReglagesGeneralVoulezAlertesFragment extends Fragment {
	public static ReglagesGeneralVoulezAlertesFragment fragment;

	private View containerView;
	
	private Voulez longClick = Voulez.NON;
	
	private RadioGroup rg;
	
	public static ReglagesGeneralVoulezAlertesFragment getInstance() {
		if (fragment == null) {
			fragment = new ReglagesGeneralVoulezAlertesFragment();
			Bundle bundle = new Bundle();
			bundle.putString("Title", "R�glagess General Voulez Alertes");
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
		containerView = inflater.inflate(R.layout.reglages_general_voulez_alertes_setting_ui, container, false);
		rg = (RadioGroup) containerView.findViewById(R.id.radio_voulez_alertes);
		containerView.findViewById(R.id.btnBack).setOnClickListener(UserApplication.getInstance().getActivity());
		initView();
		return containerView;
		
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
