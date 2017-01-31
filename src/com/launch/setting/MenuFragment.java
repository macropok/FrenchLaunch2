package com.launch.setting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.launch.ui.R;
import com.launch.ui.UserApplication;

public class MenuFragment extends Fragment implements OnItemClickListener {

	private ListView list;

	private MenuAdapter mAdapter;

	private String code;
	
	private onSettingCallback callback;

	private MenuFragment(String mCode, onSettingCallback _callback) {
		code = mCode;
		callback = _callback;
	}

	public static MenuFragment newInstance(String code, onSettingCallback _callback) {
		return new MenuFragment(code, _callback);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menu_fragment, null);
		list = (ListView) view.findViewById(R.id.list);
		mAdapter = new MenuAdapter(getResources().getStringArray(
				Menu.getArray(code)));
		list.setAdapter(mAdapter);
		list.setOnItemClickListener(this);
		if(callback != null) {
			callback.toggleSetting(Menu.getTitle(getActivity(), code), code);
		}
		return view;
	}

	private class MenuAdapter extends BaseAdapter {

		private int[] item_bg = { R.drawable.friend_bg, R.drawable.moi_bg,
				R.drawable.votre_bg, R.drawable.alert_bg, R.drawable.avance_bg };

		private String[] names;

		public MenuAdapter(String[] mNames) {
			if (mNames == null) {
				mNames = new String[] {};
			}
			this.names = mNames;
		}

		@Override
		public int getCount() {
			return names.length;
		}

		@Override
		public Object getItem(int position) {
			return names[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MenuHolder holder = null;
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(getActivity());
				convertView = inflater.inflate(R.layout.menu_fragment_item,
						null);
				holder = new MenuHolder();
				holder.icon = (ImageView) convertView.findViewById(R.id.icon);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				convertView.setTag(holder);
			} else {
				holder = (MenuHolder) convertView.getTag();
			}

			holder.name.setText(names[position]);
			if(position >= item_bg.length) {
				holder.icon.setImageResource(R.drawable.setting_icon);
				convertView.setBackgroundResource(item_bg[item_bg.length - 1]);
			} else {
				holder.icon.setImageResource(Menu.getMenuIcon(getActivity(), code, position));
				convertView.setBackgroundResource(item_bg[position]);	
			}
			
			return convertView;
		}

	}

	private static class MenuHolder {
		public ImageView icon;

		public TextView name;

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String nextCode = Menu.getNextMenuCode(code, position);
		UserApplication application = (UserApplication) getActivity().getApplication();
		application.showFragment(nextCode);
		
	}
}
