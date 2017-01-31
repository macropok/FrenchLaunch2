package com.launch.ui;

import java.sql.SQLException;

import com.launch.setting.FavorFragment.ReceiveContactReceiver;
import com.launch.sqlite.Contact;
import com.launch.sqlite.ContactService;
import com.launch.sqlite.P;
import com.launch.sqlite.Contact.ContactType;
import com.launch.utils.ContactUtils;
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
import android.widget.Toast;

public class ReglagesCommunauteContactAddFragment extends Fragment implements OnClickListener {

	private View containerView;
	
	private EditText name;

	private EditText phone;

	private EditText email;

	private ContactService service;

	private ReceiveContactReceiver receiver;

	private Contact contact;
	
	private boolean isFromContact;

	private int index;

	private long new_contact_id;
	
	public ReglagesCommunauteContactAddFragment()
	{
		Bundle bundle = new Bundle();
		bundle.putString("Title", "R�glagess Votre Communauté Favoris");
		bundle.putInt("bgColor", 0xff091302);
		bundle.putInt("ImageId", R.drawable.votre_icon);
		setArguments(bundle);
	}
	
	public ReglagesCommunauteContactAddFragment(Contact _contact, int _index) {
		this();
		this.contact = _contact;
		this.index = _index;
	}

	public ReglagesCommunauteContactAddFragment(int _index) {
		this();
		contact = new Contact();
		contact.setType(ContactType.values()[_index]);
		this.index = _index;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((PageActivity)getActivity()).setIcon(getArguments().getInt("bgColor"), getArguments().getInt("ImageId"));
		containerView = inflater.inflate(R.layout.reglages_communaute_add_contact_ui, container, false);
		containerView.findViewById(R.id.btnBack).setOnClickListener(UserApplication.getInstance().getActivity());
		name = (EditText) containerView.findViewById(R.id.name);
		phone = (EditText) containerView.findViewById(R.id.phone);
		email = (EditText) containerView.findViewById(R.id.email);
		containerView.findViewById(R.id.from).setOnClickListener(this);
		containerView.findViewById(R.id.update).setOnClickListener(this);
		UserApplication application = (UserApplication) getActivity()
				.getApplication();
		try {
			service = application.getHelper().getContactService();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		initView();
		return containerView;
	}
	
	private void initView() {
		email.setText(contact.getEmail());
		name.setText(contact.getName());
		phone.setText(contact.getPhone());
		
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
			isFromContact = true;
			email.setText(intent.getStringExtra("email"));
			name.setText(intent.getStringExtra("name"));
			phone.setText(intent.getStringExtra("phone"));
			new_contact_id = intent.getLongExtra("contactId",0);
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
				//Toast.makeText(getActivity(), "Before id: "+String.valueOf(contact.getId()), Toast.LENGTH_SHORT).show();
				if(isFromContact && new_contact_id!=0)
				{
					if( contact.getId()!=-1)
						ContactUtils.removeContactFromGroup(contact.getPhoneContactId(), ContactUtils.communaute_groups[index], getActivity());
					
					contact.setPhoneContactId(new_contact_id);
					ContactUtils.addContactToGroup(contact.getPhoneContactId(), ContactUtils.communaute_groups[index], getActivity());
				}
				
				boolean success = false;

				if(!isFromContact && contact.getId()==-1)
				{
					long contact_id = ContactUtils.wirtePtoContact(contact, getActivity());
					success = contact_id!=0;
					if(success)
					{
						contact.setPhoneContactId(contact_id);
						ContactUtils.addContactToGroup(contact_id, ContactUtils.communaute_groups[index], getActivity());
					}
				}else
					success = ContactUtils.updateContact(getActivity(), contact);

				if (success && service.saveOrUpdateContact(contact)) {
					Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();
				}
				//Toast.makeText(getActivity(), "After id: "+String.valueOf(contact.getId()), Toast.LENGTH_SHORT).show();
			}
			isFromContact = false;	
		} else if (v.getId() == R.id.from) {
			receiver = new ReceiveContactReceiver();
			IntentFilter filter = new IntentFilter("contact.add");
			getActivity().registerReceiver(receiver, filter);
			Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			getActivity().startActivityForResult(intent,
					PageActivity.PICK_CONTACT);
		}
	}
}
