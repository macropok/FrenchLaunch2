package com.launch.setting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

import com.launch.sqlite.P;
import com.launch.ui.PageActivity;
import com.launch.ui.R;
import com.launch.utils.Preferences;

public class FavorFragment extends Fragment implements OnClickListener {

	private EditText name;

	private EditText phone;

	private ReceiveContactReceiver receiver;

	private int index;

	private P p = null;

	public FavorFragment(int index) {
		this.index = index;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.favor_contact_layout, null);
		p = index == 1 ? Preferences.getPhoneNumber1(getActivity())
				: Preferences.getPhoneNumber2(getActivity());
		name = (EditText) view.findViewById(R.id.name);
		phone = (EditText) view.findViewById(R.id.phone);
		view.findViewById(R.id.from).setOnClickListener(this);
		view.findViewById(R.id.update).setOnClickListener(this);
		initView();
		return view;
	}

	private void initView() {
		name.setText(p.name);
		phone.setText(p.number);
		receiver = new ReceiveContactReceiver();
		IntentFilter filter = new IntentFilter("contact.add");
		getActivity().registerReceiver(receiver, filter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// getActivity().unregisterReceiver(receiver);
	}

	public class ReceiveContactReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent intent) {
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
			Intent intent = new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI);
			getActivity().startActivityForResult(intent,
					PageActivity.PICK_CONTACT);
		}
	}

}
