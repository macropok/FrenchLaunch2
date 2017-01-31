package com.launch.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.launch.setting.ContactAddFragment;
import com.launch.sort.DragSortListView;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ReglagesCommunauteContactListFragment extends Fragment implements OnItemClickListener, OnClickListener {

	private View containerView;
	
	private DragSortListView list;

	private ContactAdapter adapter;

	private ContactService service;

	private String code;
	
	private TextView txtQuestion;
	
	private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
		@Override
		public void drop(int from, int to) {
			Contact item = (Contact) adapter.getItem(from);

			adapter.notifyDataSetChanged();
			adapter.remove(item);
			adapter.insert(item, to);
		}
	};

	private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
		@Override
		public void remove(int which) {
			Contact contact_to_remove = adapter.getItem(which);
			ContactUtils.removeContactFromGroup(contact_to_remove.getPhoneContactId(), ContactUtils.communaute_groups[Integer.parseInt(code)%10-1], getActivity());
			service.remove(contact_to_remove);
			adapter.remove(contact_to_remove);
		}
	};

	private DragSortListView.DragScrollProfile ssProfile = new DragSortListView.DragScrollProfile() {
		@Override
		public float getSpeed(float w, long t) {
			if (w > 0.8f) {
				// Traverse all views in a millisecond
				return ((float) adapter.getCount()) / 0.001f;
			} else {
				return 10.0f * w;
			}
		}
	};
	
	public ReglagesCommunauteContactListFragment(String _code) {
		this.code = _code;
		Bundle bundle = new Bundle();
		bundle.putString("Title", "R�glagess Votre Communauté Contact List");
		bundle.putInt("bgColor", 0xff091302);
		bundle.putInt("ImageId", R.drawable.votre_icon);
		setArguments(bundle);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((PageActivity)getActivity()).setIcon(getArguments().getInt("bgColor"), getArguments().getInt("ImageId"));
		containerView = inflater.inflate(R.layout.reglages_communaute_contact_list_ui, container, false);
		containerView.findViewById(R.id.btnBack).setOnClickListener(UserApplication.getInstance().getActivity());
		
		txtQuestion = (TextView)containerView.findViewById(R.id.txtQuestion1);
		containerView.findViewById(R.id.add).setOnClickListener(this);
		list = (DragSortListView) containerView.findViewById(R.id.contacts);
		list.setDropListener(onDrop);
		list.setRemoveListener(onRemove);
//		list.setDragScrollProfile(ssProfile);

		 list.setOnItemClickListener(this);
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
		int _index = Integer.parseInt(code)%10-1;
		txtQuestion.setText(txtQuestion.getText()+" "+getActivity().getResources().getStringArray(R.array.menu_s3)[_index]);
		ArrayList<Contact>	contacts = new ArrayList<Contact>();
		adapter = new ContactAdapter(getActivity(), R.layout.contact_item_layout, contacts);
		list.setAdapter(adapter);
	}

	@Override
	public void onResume() {
		super.onResume();
		int index = Integer.parseInt(code) % 10 - 1;
		try {
			List<Contact> temps = service
					.getContact(ContactType.values()[index]);
			if (temps != null && temps.size() > 0) {
				for(Contact contact:temps)
				{
					if(ContactUtils.readContactDetails(contact, getActivity()))
						service.updateContact(contact);
				}
				adapter.clear();
				adapter.addAll(temps);
				adapter.notifyDataSetChanged();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private class ContactAdapter extends ArrayAdapter<Contact> {

		public ContactAdapter(Context context, int resource,
				List<Contact> objects) {
			super(context, resource, objects);
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			return super.getDropDownView(position, convertView, parent);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ContactHolder holder;
			if (convertView == null) {
				LayoutInflater infalter = LayoutInflater.from(getActivity());
				holder = new ContactHolder();
				convertView = infalter.inflate(R.layout.contact_item_layout,null);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.phone = (TextView) convertView.findViewById(R.id.phone);
				convertView.setTag(holder);
			} else {
				holder = (ContactHolder) convertView.getTag();
			}
			Contact contact = getItem(position);
			holder.name.setText(contact.getName());
			holder.phone.setText(contact.getPhone());
			return convertView;
		}

	}

	private static class ContactHolder {

		public TextView name;

		public TextView phone;

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.add) {
			UserApplication.getInstance().getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contaier, new ReglagesCommunauteContactAddFragment(Integer.parseInt(code)%10-1)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		 Contact contact = adapter.getItem(position);
		 UserApplication.getInstance().getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contaier, new ReglagesCommunauteContactAddFragment(contact,Integer.parseInt(code)%10-1)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
	}
}
