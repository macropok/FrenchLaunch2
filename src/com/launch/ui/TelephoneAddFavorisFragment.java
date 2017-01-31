package com.launch.ui;

import com.launch.setting.FavorFragment.ReceiveContactReceiver;
import com.launch.utils.Preferences;
import com.launch.utils.SosFunction;
import com.launch.utils.ThirdLaunch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TelephoneAddFavorisFragment extends Fragment {
	public static TelephoneAddFavorisFragment fragment;

	private View containerView;
	
	private ReceiveContactReceiver receiver;
	
	private int index;
	
	public static TelephoneAddFavorisFragment getInstance(int _index) {
		if (fragment == null) {
			fragment = new TelephoneAddFavorisFragment();
			Bundle bundle = new Bundle();
			bundle.putString("Title", "Telephone");
			bundle.putInt("bgColor", 0xff51b63c);
			bundle.putInt("ImageId", R.drawable.actionbar_tel_icon);
			fragment.setArguments(bundle);
		}
		fragment.index=_index;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((PageActivity)getActivity()).setIcon(getArguments().getInt("bgColor"), getArguments().getInt("ImageId"));
		containerView = inflater.inflate(R.layout.telephone_add_favoris_ui, container, false);
		OnClickListener listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(v.getId()==R.id.button1){
					//UserApplication.getInstance().getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contaier, RepertoireReadFragment.getInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
					IntentFilter filter = new IntentFilter("contact.add");
					getActivity().registerReceiver(receiver, filter);
					Intent intent = new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
					getActivity().startActivityForResult(intent, PageActivity.PICK_CONTACT);
				}
				else if(v.getId()==R.id.button2){
					//UserApplication.getInstance().getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contaier, RepertoireWriteFragment.getInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
					containerView.findViewById(R.id.btnBack).performClick();
				}
			}
		};
		Button btn1 = (Button)containerView.findViewById(R.id.button1);
		btn1.setOnClickListener(listener);
		Button btn2 = (Button)containerView.findViewById(R.id.button2);
		btn2.setOnClickListener(listener);
		containerView.findViewById(R.id.btnBack).setOnClickListener(UserApplication.getInstance().getActivity());
		receiver = new ReceiveContactReceiver();
		return containerView;
	}
	
	public class ReceiveContactReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			getActivity().unregisterReceiver(this);
			if(index==1)
				Preferences.savePhonenumber1(getActivity(), intent.getStringExtra("name"), intent.getStringExtra("phone"));
			else
				Preferences.savePhonenumber2(getActivity(), intent.getStringExtra("name"), intent.getStringExtra("phone"));
			containerView.findViewById(R.id.btnBack).performClick();
			//name.setText(intent.getStringExtra("name"));
			//phone.setText(intent.getStringExtra("phone"));
		}
	}
	
}
