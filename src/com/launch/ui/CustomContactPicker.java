package com.launch.ui;

import java.util.ArrayList;
import java.util.TreeSet;

import com.launch.sqlite.P;
import com.launch.utils.ContactUtils;
import com.launch.utils.Preferences;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class CustomContactPicker extends Activity {
  
	 int nameLength = 0;
	 private static final int SHOW_PREFERENCES = 1;
	 private static final int PICK_CONTACT = 2;
	 final Context context = this;
	 Cursor c;
	 Uri data ;
	 String dataPath ;
	 
	 private CustomAdapter mAdapter;
	 
	 private String group;
	 
	 private String dest_action;
	 
	 //ArrayList<P> contacts = new ArrayList<P>();
	 
	 ListView contactsList;
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.custom_contact_picker);
		group = getIntent().getStringExtra("group");
		dest_action = getIntent().getStringExtra("dest_action");
		//Toast.makeText(this, group, Toast.LENGTH_SHORT).show();
		//Toast.makeText(this, dest_action, Toast.LENGTH_SHORT).show();
		contactsList = (ListView)findViewById(R.id.contactListView);
		mAdapter = new CustomAdapter(this);
		if(group.equals("favoris"))
		{
			//contacts.add(Preferences.getPhoneNumber1(UserApplication.getInstance().getActivity()));
			//contacts.add(Preferences.getPhoneNumber2(UserApplication.getInstance().getActivity()));
			mAdapter.addSectionHeaderItem("Favoris");
			mAdapter.addItem(Preferences.getPhoneNumber1(UserApplication.getInstance().getActivity()));
			mAdapter.addItem(Preferences.getPhoneNumber2(UserApplication.getInstance().getActivity()));
		}else if(group.equals("communaute"))
		{
			for(String _group: ContactUtils.communaute_groups)
			{
				mAdapter.addSectionHeaderItem(_group);
				ArrayList<P> contacts_list = ContactUtils.readContactsGroup(_group);
				for(P _contact:contacts_list)
					mAdapter.addItem(_contact);
			}
		}
		/*ArrayAdapter<P> adapter = new ArrayAdapter<P>(this, android.R.layout.simple_list_item_2, android.R.id.text1, favoris) {
		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
		    View view = super.getView(position, convertView, parent);
		    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
		    TextView text2 = (TextView) view.findViewById(android.R.id.text2);
	
		    text1.setText(favoris.get(position).name);
		    text2.setText(favoris.get(position).number);
		    return view;
		  }
		};*/
		contactsList.setAdapter(mAdapter);
		if(dest_action.equals("pick"))
		{
			contactsList.setOnItemClickListener(new OnItemClickListener() {
		
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
					if(!mAdapter.isHeader(position))
					{
						P chosenContact = (P)contactsList.getItemAtPosition(position);
						Intent outData = new Intent();
						outData.putExtra("name", chosenContact.name);
						outData.putExtra("phone", chosenContact.number);
						outData.putExtra("email", chosenContact.email);
						outData.putExtra("contactId", chosenContact.contactId);
						if(group.equals("favoris"))
							outData.putExtra("index", position);
				        setResult(Activity.RESULT_OK, outData);
				        finish();
					}
				}
			});
		}
	  }

	class CustomAdapter extends BaseAdapter {
	
		private static final int TYPE_ITEM = 0;
		private static final int TYPE_SEPARATOR = 1;
	
		private ArrayList<P> mData = new ArrayList<P>();
		private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();
	
		private LayoutInflater mInflater;
	
		public CustomAdapter(Context context) {
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
	
		public void addItem(final P item) {
			mData.add(item);
			notifyDataSetChanged();
		}
	
		public boolean isHeader(int position)
		{
			return sectionHeader.contains(position);
		}
		
		public void addSectionHeaderItem(final String item) {
			P p = new P();
			p.name = item;
			mData.add(p);
			sectionHeader.add(mData.size() - 1);
			notifyDataSetChanged();
		}
	
		@Override
		public int getItemViewType(int position) {
			return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
		}
	
		@Override
		public int getViewTypeCount() {
			return 2;
		}
	
		@Override
		public int getCount() {
			return mData.size();
		}
	
		@Override
		public P getItem(int position) {
			return mData.get(position);
		}
	
		@Override
		public long getItemId(int position) {
			return position;
		}
	
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			int rowType = getItemViewType(position);
	
			if (convertView == null) {
				holder = new ViewHolder();
				switch (rowType) {
				case TYPE_ITEM:
					convertView = mInflater.inflate(R.layout.custom_contact_item, null);
					holder.nameView = (TextView) convertView.findViewById(R.id.name);
					holder.numberView = (TextView) convertView.findViewById(R.id.number);
					holder.emailView = (TextView) convertView.findViewById(R.id.email);
					break;
				case TYPE_SEPARATOR:
					convertView = mInflater.inflate(R.layout.custom_contact_header, null);
					holder.nameView = (TextView) convertView.findViewById(R.id.textSeparator);
					break;
				}
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.nameView.setText(mData.get(position).name);
			if(rowType==TYPE_ITEM){
				holder.numberView.setText(mData.get(position).number);
				holder.emailView.setText(mData.get(position).email);
			}
			return convertView;
		}
	}
	public static class ViewHolder {
		public TextView nameView;
		public TextView numberView;
		public TextView emailView;
	}
}