package com.launch.ui;

import com.launch.setting.FavorFragment.ReceiveContactReceiver;
import com.launch.sqlite.P;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ReglagesCommunauteFavorisFragment extends Fragment implements OnClickListener {
	public static ReglagesCommunauteFavorisFragment fragment1;
	public static ReglagesCommunauteFavorisFragment fragment2;

	private View containerView;
	
	private EditText name;

	private EditText phone;

	private TextView txtQuestion;
	
	private ReceiveContactReceiver receiver;

	private int index;

	private P p = null;
	
	public static ReglagesCommunauteFavorisFragment getInstance(int _index) {
		if(_index==1)
		{
			if (fragment1 == null) {
				fragment1 = new ReglagesCommunauteFavorisFragment();
				fragment1.index = 1;
				Bundle bundle = new Bundle();
				bundle.putString("Title", "R�glagess Votre Communauté Favoris");
				bundle.putInt("bgColor", 0xff091302);
				bundle.putInt("ImageId", R.drawable.votre_icon);
				fragment1.setArguments(bundle);
			}
			return fragment1;
		}
		if (fragment2 == null) {
			fragment2 = new ReglagesCommunauteFavorisFragment();
			fragment2.index = 2;
			Bundle bundle = new Bundle();
			bundle.putString("Title", "R�glagess Votre Communauté Favoris");
			bundle.putInt("bgColor", 0xff091302);
			bundle.putInt("ImageId", R.drawable.votre_icon);
			fragment2.setArguments(bundle);
		}
		return fragment2;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((PageActivity)getActivity()).setIcon(getArguments().getInt("bgColor"), getArguments().getInt("ImageId"));
		containerView = inflater.inflate(R.layout.reglages_communaute_favoris_ui, container, false);
		containerView.findViewById(R.id.btnBack).setOnClickListener(UserApplication.getInstance().getActivity());
		p = index == 1 ? Preferences.getPhoneNumber1(getActivity())
				: Preferences.getPhoneNumber2(getActivity());
		name = (EditText) containerView.findViewById(R.id.name);
		phone = (EditText) containerView.findViewById(R.id.phone);
		txtQuestion = (TextView) containerView.findViewById(R.id.txtQuestion1);
		containerView.findViewById(R.id.from).setOnClickListener(this);
		containerView.findViewById(R.id.update).setOnClickListener(this);
		initView();
		
		return containerView;
	}
	private void initView() {
		name.setText(p.name);
		phone.setText(p.number);
		txtQuestion.setText(txtQuestion.getText()+" "+String.valueOf(index));
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// getActivity().unregisterReceiver(receiver);
	}

	public class ReceiveContactReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			getActivity().unregisterReceiver(this);
			name.setText(intent.getStringExtra("name"));
			phone.setText(intent.getStringExtra("phone"));
		}
	}

	private void collect() {
		p.name = name.getText().toString();
		p.number = phone.getText().toString();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.update) {
			collect();
			if (index == 1) {
				Preferences.savePhonenumber1(getActivity(), p.name, p.number);
			} else {
				Preferences.savePhonenumber2(getActivity(), p.name, p.number);
			}
		} else if (v.getId() == R.id.from) {
			receiver = new ReceiveContactReceiver();
			IntentFilter filter = new IntentFilter("contact.add");
			getActivity().registerReceiver(receiver, filter);
			Intent intent = new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI);
			getActivity().startActivityForResult(intent,
					PageActivity.PICK_CONTACT);
		}
	}
}
