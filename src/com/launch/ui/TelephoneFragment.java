package com.launch.ui;

import com.launch.sqlite.P;
import com.launch.utils.Preferences;
import com.launch.utils.SosFunction;
import com.launch.utils.ThirdLaunch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TelephoneFragment extends Fragment {
	public static TelephoneFragment fragment;

	private View containerView;
	
	public static TelephoneFragment getInstance() {
		if (fragment == null) {
			fragment = new TelephoneFragment();
			Bundle bundle = new Bundle();
			bundle.putString("Title", "Telephone");
			bundle.putInt("bgColor", 0xff51b63c);
			bundle.putInt("ImageId", R.drawable.actionbar_tel_icon);
			fragment.setArguments(bundle);
		}
		return fragment;
	}

	public void initiateCall(String number)
	{
		Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+number.trim()));
        startActivity(callIntent);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((PageActivity)getActivity()).setIcon(getArguments().getInt("bgColor"), getArguments().getInt("ImageId"));
		containerView = inflater.inflate(R.layout.telephone_ui, container, false);
		OnClickListener listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(v.getId()==R.id.button1){
					P p = Preferences.getPhoneNumber1(getActivity());
					if(p.name.equals("") || p.number.equals(""))
						UserApplication.getInstance().getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contaier, TelephoneAddFavorisFragment.getInstance(1)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
					else
						initiateCall(p.number);
				}
				else if(v.getId()==R.id.button2){
					P p = Preferences.getPhoneNumber2(getActivity());
					if(p.name.equals("") || p.number.equals(""))
						UserApplication.getInstance().getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contaier, TelephoneAddFavorisFragment.getInstance(2)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
					else
						initiateCall(p.number);
				}
				else if(v.getId()==R.id.button3)
				{
					//ThirdLaunch.launchApps(getActivity());
					//ThirdLaunch.launchDialer(getActivity());
					UserApplication.getInstance().getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contaier, TelephoneDialFragment.getInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
				}
				//getChildFragmentManager().beginTransaction().replace(R.id.contaier, RepertoireReadFragment.getInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack("RepertoireReadFragment").commit();
			}
		};
		P p1 = Preferences.getPhoneNumber1(getActivity());
		P p2 = Preferences.getPhoneNumber2(getActivity());
		Button btn1 = (Button)containerView.findViewById(R.id.button1);
		btn1.setOnClickListener(listener);
		if(!p1.name.equals("") && !p1.number.equals(""))
			btn1.setText("1. APPELLER "+p1.name);
		else
			btn1.setText("1. APPELLER FAVORI 1");
		
		Button btn2 = (Button)containerView.findViewById(R.id.button2);
		btn2.setOnClickListener(listener);
		if(!p2.name.equals("") && !p2.number.equals(""))
			btn2.setText("2. APPELLER "+p2.name);
		else
			btn2.setText("2. APPELLER FAVORI 2");
		Button btn3 = (Button)containerView.findViewById(R.id.button3);
		btn3.setOnClickListener(listener);
		containerView.findViewById(R.id.btnBack).setOnClickListener(UserApplication.getInstance().getActivity());
		return containerView;
	}
	
}
