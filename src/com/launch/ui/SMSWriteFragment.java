package com.launch.ui;

import com.launch.ui.TelephoneAddFavorisFragment.ReceiveContactReceiver;
import com.launch.utils.Preferences;
import com.launch.utils.SosFunction;
import com.launch.utils.ThirdLaunch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SMSWriteFragment extends Fragment {
	public static SMSWriteFragment fragment;

	private View containerView;
	
	private ReceiveContactReceiver receiver;
	
	public static SMSWriteFragment getInstance() {
		if (fragment == null) {
			fragment = new SMSWriteFragment();
			Bundle bundle = new Bundle();
			bundle.putString("Title", "Ecrire un SMS");
			bundle.putInt("bgColor", 0xff375e8f);
			bundle.putInt("ImageId", R.drawable.actionbar_sms_icon);
			fragment.setArguments(bundle);
		}
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		containerView = inflater.inflate(R.layout.sms_write_ui, container, false);
		OnClickListener listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(v.getId()==R.id.button1)
				{
					//ThirdLaunch.launchPickFavorisContact(getActivity());
					IntentFilter filter = new IntentFilter("contact.add");
					getActivity().registerReceiver(receiver, filter);
					ThirdLaunch.launchCustomContactPicker(getActivity(),"favoris","pick", PageActivity.SMS_CUSTOM_CONTACT_PICKER);
				}
				else if(v.getId()==R.id.button2)
				{
					IntentFilter filter = new IntentFilter("contact.add");
					getActivity().registerReceiver(receiver, filter);
					//ThirdLaunch.launchPickCommunateContact(getActivity());
					//ThirdLaunch.launchPickContact(getActivity());
					ThirdLaunch.launchCustomContactPicker(getActivity(),"communaute","pick",PageActivity.SMS_CUSTOM_CONTACT_PICKER);
				}else if(v.getId()==R.id.button3)
				{
					IntentFilter filter = new IntentFilter("contact.add");
					getActivity().registerReceiver(receiver, filter);
					ThirdLaunch.launchPickContact(getActivity());
				}else if(v.getId()==R.id.button4)
				{
					UserApplication.getInstance().getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contaier, SMSSpecificNumberFragment.getInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
				}
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
		containerView.findViewById(R.id.btnBack).setOnClickListener(UserApplication.getInstance().getActivity());
		receiver = new ReceiveContactReceiver();

		return containerView;
		
	}
	public class ReceiveContactReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			getActivity().unregisterReceiver(this);
			if(!intent.getStringExtra("phone").equals(""))
				ThirdLaunch.launchNewSMS(getActivity(),intent.getStringExtra("phone"));
			//name.setText(intent.getStringExtra("name"));
			//phone.setText(intent.getStringExtra("phone"));
		}
	}
}
