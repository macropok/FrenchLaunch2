package com.launch.ui;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.launch.utils.SosFunction;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ApplicationsListFragment extends Fragment implements OnItemClickListener {
	public static ApplicationsListFragment fragment;

	private View containerView;
	
	private GridView apps;

	private AppAdapter adapter;
	
	public static ApplicationsListFragment getInstance() {
		if (fragment == null) {
			fragment = new ApplicationsListFragment();
			Bundle bundle = new Bundle();
			bundle.putString("Title", "My Applications");
			bundle.putInt("bgColor", 0xffadb117);
			bundle.putInt("ImageId", R.drawable.actionbar_applications_icon);
			fragment.setArguments(bundle);
		}
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		containerView = inflater.inflate(R.layout.applications_list_ui, container, false);
		containerView.findViewById(R.id.btnBack).setOnClickListener(UserApplication.getInstance().getActivity());
		this.apps = (GridView) containerView.findViewById(R.id.apps);
		this.apps.setOnItemClickListener( this);
		loadApp();
		return containerView;
		
	}

	private void loadApp() {
		PackageManager pm = getActivity().getPackageManager();
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
		Collections.sort(infos, new Comparator<ResolveInfo>() {

			@Override
			public int compare(ResolveInfo lhs, ResolveInfo rhs) {
				// TODO Auto-generated method stub
				return lhs.loadLabel(getActivity().getPackageManager()).toString().compareTo(rhs.loadLabel(getActivity().getPackageManager()).toString());
			}
		});
		adapter = new AppAdapter(infos);
		try{
			this.apps.setAdapter(adapter);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private class AppAdapter extends BaseAdapter {

		public List<ResolveInfo> infos;

		public AppAdapter(List<ResolveInfo> _infos) {
			this.infos = _infos;
		}

		@Override
		public int getCount() {
			return infos.size();
		}

		@Override
		public Object getItem(int arg0) {
			return infos.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int positon, View convertView, ViewGroup arg2) {
			AppHolder holder = null;
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(ApplicationsListFragment.this.getActivity());
				convertView = inflater.inflate(R.layout.app_home_item, null);
				holder = new AppHolder();
				holder.appIcon = (ImageView) convertView
						.findViewById(R.id.app_icon);
				holder.appName = (TextView) convertView
						.findViewById(R.id.app_name);
				convertView.setTag(holder);
			} else {
				holder = (AppHolder) convertView.getTag();
			}

			ResolveInfo info = infos.get(positon);
			holder.appIcon.setImageDrawable(info.loadIcon(getActivity().getPackageManager()));
			holder.appName.setText(info.loadLabel(getActivity().getPackageManager()));
			return convertView;
		}

	}

	private class AppHolder {
		public ImageView appIcon;

		public TextView appName;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		ResolveInfo info = (ResolveInfo) adapter.getItem(position);
		Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage(
				info.activityInfo.packageName);
		startActivity(intent);
	}
	
}
