package com.launch.setting;

import java.sql.SQLException;

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
import android.widget.Toast;

import com.launch.sqlite.Contact;
import com.launch.sqlite.Contact.ContactType;
import com.launch.sqlite.ContactService;
import com.launch.ui.PageActivity;
import com.launch.ui.R;
import com.launch.ui.UserApplication;
import com.launch.utils.ContactUtils;

public class ContactAddFragment extends Fragment implements OnClickListener {

	private EditText name;

	private EditText phone;

	private EditText email;

	private ContactService service;

	private ReceiveContactReceiver receiver;

	private Contact contact;
	
	private boolean isFromContact;

	public ContactAddFragment(Contact _contact) {
		this.contact = _contact;
	}

	public ContactAddFragment(ContactType type) {
		contact = new Contact();
		contact.setType(type);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_contact_layout, null);
		name = (EditText) view.findViewById(R.id.name);
		phone = (EditText) view.findViewById(R.id.phone);
		email = (EditText) view.findViewById(R.id.email);
		view.findViewById(R.id.from).setOnClickListener(this);
		view.findViewById(R.id.update).setOnClickListener(this);
		UserApplication application = (UserApplication) getActivity()
				.getApplication();
		try {
			service = application.getHelper().getContactService();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		initView();
		return view;
	}

	private void initView() {
		email.setText(contact.getEmail());
		name.setText(contact.getName());
		phone.setText(contact.getPhone());
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
			isFromContact = true;
			email.setText(intent.getStringExtra("email"));
			name.setText(intent.getStringExtra("name"));
			phone.setText(intent.getStringExtra("phone"));
		}
	}

	private void collect() {
		contact.setEmail(email.getText().toString());
		contact.setName(name.getText().toString());
		contact.setPhone(phone.getText().toString());
		if(!contact.checkInvalid()) {
			contact = null;
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.update) {
			if (service != null) {
				collect();
				if(contact == null) {
					return;
				}
				if (service.saveOrUpdateContact(contact)) {
					Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT)
							.show();
				}
				if(!isFromContact) { 
					ContactUtils.wirtePtoContact(contact, getActivity());
				}
					
			}
		} else if (v.getId() == R.id.from) {
			Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			getActivity().startActivityForResult(intent,
					PageActivity.PICK_CONTACT);
		}
	}

}
