package com.launch.ui;

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

public class ReglagesGeneralFragment extends Fragment {
	public static ReglagesGeneralFragment fragment;

	private View containerView;
	
	public static ReglagesGeneralFragment getInstance() {
		if (fragment == null) {
			fragment = new ReglagesGeneralFragment();
			Bundle bundle = new Bundle();
			bundle.putString("Title", "R�glagess General");
			bundle.putInt("bgColor", 0xff091302);
			bundle.putInt("ImageId", R.drawable.friends_icon);
			fragment.setArguments(bundle);
		}
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((PageActivity)getActivity()).setIcon(getArguments().getInt("bgColor"), getArguments().getInt("ImageId"));
		containerView = inflater.inflate(R.layout.reglages_general_ui, container, false);
		OnClickListener listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(v.getId()==R.id.button1)
					UserApplication.getInstance().getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contaier, ReglagesGeneralVoulezAlertesFragment.getInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
				else if(v.getId()==R.id.button2)
					ThirdLaunch.launchWifiSetting(getActivity());
				else if(v.getId()==R.id.button3)
					ThirdLaunch.launchBluetoothSetting(getActivity());
				else if(v.getId()==R.id.button4)
					ThirdLaunch.launchAudioSetting(getActivity());
				else if(v.getId()==R.id.button5)
					ThirdLaunch.launchAndroidSetting(getActivity());
				//getChildFragmentManager().beginTransaction().replace(R.id.contaier, RepertoireReadFragment.getInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack("RepertoireReadFragment").commit();
			}
		};
		Button btn1 = (Button)containerView.findViewById(R.id.button1);
		btn1.setOnClickListener(listener);
		Button btn2 = (Button)containerView.findViewById(R.id.button2);
		btn2.setOnClickListener(listener);
		Button btn3 = (Button)containerView.findViewById(R.id.button3);
		btn3.setOnClickListener(listener);
		Button btn4 = (Button)containerView.findViewById(R.id.button4);
		btn4.setOnClickListener(listener);
		Button btn5 = (Button)containerView.findViewById(R.id.button5);
		btn5.setOnClickListener(listener);
		containerView.findViewById(R.id.btnBack).setOnClickListener(UserApplication.getInstance().getActivity());
		return containerView;
	}
	
}
