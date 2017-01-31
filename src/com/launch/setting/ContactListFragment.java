package com.launch.setting;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.launch.sort.DragSortListView;
import com.launch.sqlite.Contact;
import com.launch.sqlite.Contact.ContactType;
import com.launch.sqlite.ContactService;
import com.launch.ui.R;
import com.launch.ui.UserApplication;

public class ContactListFragment extends Fragment implements
		OnItemClickListener, OnClickListener {

	private DragSortListView list;

	private ContactAdapter adapter;

	private ContactService service;

	private String code;

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
			service.remove(adapter.getItem(which));
			adapter.remove(adapter.getItem(which));
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

	public ContactListFragment(String _code) {
		this.code = _code;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.contact_list_layout, null);
		view.findViewById(R.id.add).setOnClickListener(this);
		list = (DragSortListView) view.findViewById(R.id.contacts);
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
		return view;
	}

	private void initView() {
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
				convertView = infalter.inflate(R.layout.contact_item_layout,
						null);
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
			UserApplication application = (UserApplication) getActivity()
					.getApplication();
			application.showFragment(Menu.getNextMenuCode(code, 0));
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		 Contact contact = adapter.getItem(position);
		 ContactAddFragment add = new ContactAddFragment(contact);
		 UserApplication.getInstance().showFragment(Menu.getNextMenuCode(code,
		 0), add);
	}
}
