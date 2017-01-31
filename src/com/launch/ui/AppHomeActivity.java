package com.launch.ui;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class AppHomeActivity extends Activity implements OnItemClickListener {

	private GridView apps;

	private AppAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_home_layout);
		apps = (GridView) findViewById(R.id.apps);
		apps.setOnItemClickListener(this);
		loadApp();
	}

	private void loadApp() {
		PackageManager pm = getPackageManager();
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
		adapter = new AppAdapter(infos);
		apps.setAdapter(adapter);
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
				LayoutInflater inflater = LayoutInflater
						.from(AppHomeActivity.this);
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
			holder.appIcon.setImageDrawable(info.loadIcon(getPackageManager()));
			holder.appName.setText(info.loadLabel(getPackageManager()));
			return convertView;
		}

	}

	private class AppHolder {
		public ImageView appIcon;

		public TextView appName;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		ResolveInfo info = (ResolveInfo) adapter.getItem(position);
		Intent intent = getPackageManager().getLaunchIntentForPackage(
				info.activityInfo.packageName);
		startActivity(intent);
	}
}
