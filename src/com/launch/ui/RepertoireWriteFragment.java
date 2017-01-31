package com.launch.ui;

import com.launch.ui.SMSWriteFragment.ReceiveContactReceiver;
import com.launch.utils.SosFunction;
import com.launch.utils.ThirdLaunch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class RepertoireWriteFragment extends Fragment {
	public static RepertoireWriteFragment fragment;

	private View containerView;
	
	private ReceiveContactReceiver receiver;
	
	public static RepertoireWriteFragment getInstance() {
		if (fragment == null) {
			fragment = new RepertoireWriteFragment();
			Bundle bundle = new Bundle();
			bundle.putString("Title", "Modifier un contact");
			bundle.putInt("bgColor", 0xff60ad9d);
			bundle.putInt("ImageId", R.drawable.actionbar_repertoire_icon);
			fragment.setArguments(bundle);
		}
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((PageActivity)getActivity()).setIcon(getArguments().getInt("bgColor"), getArguments().getInt("ImageId"));
		containerView = inflater.inflate(R.layout.repertoire_write_ui, container, false);
OnClickListener listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(v.getId()==R.id.button1)
				{
					ThirdLaunch.launchCustomContactPicker(getActivity(),"favoris","pick",PageActivity.EDIT_CONTACT);
				}
				else if(v.getId()==R.id.button2)
				{
					ThirdLaunch.launchCustomContactPicker(getActivity(),"communaute","pick",PageActivity.EDIT_CONTACT);
				}
				else if(v.getId()==R.id.button3)
				{
					
					ThirdLaunch.launchEditAllContacts(getActivity());
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
		containerView.findViewById(R.id.btnBack).setOnClickListener(UserApplication.getInstance().getActivity());
		receiver = new ReceiveContactReceiver();
		IntentFilter filter;
		filter = new IntentFilter("contact.edit");
		getActivity().registerReceiver(receiver, filter);

		return containerView;
		
	}
	public class ReceiveContactReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			//getActivity().unregisterReceiver(this);
			long contactId = intent.getLongExtra("contactId",0);
			String uri = intent.getStringExtra("uri");
			Intent edit_intent = new Intent(Intent.ACTION_EDIT,ContactsContract.Contacts.CONTENT_URI);
			edit_intent.putExtra("finishActivityOnSaveCompleted", true);
			if(contactId!=0)
			{
				edit_intent.setDataAndType(Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactId)), ContactsContract.Contacts.CONTENT_ITEM_TYPE);
				//Toast.makeText(getActivity(), "Picked", Toast.LENGTH_LONG).show();
				getActivity().startActivity(edit_intent);
			}else if(uri!=null)
			{
				edit_intent.setDataAndType(Uri.parse(uri), ContactsContract.Contacts.CONTENT_ITEM_TYPE);
				getActivity().startActivity(edit_intent);
			}else{
				int index = intent.getIntExtra("index",0);
				UserApplication.getInstance().getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contaier, ReglagesCommunauteFavorisFragment.getInstance(index)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
			}
		}
	}
	
}
