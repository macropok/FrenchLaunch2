package com.launch.ui;

import com.launch.utils.SosFunction;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EmailWriteFragment extends Fragment {
	public static EmailWriteFragment fragment;

	private View containerView;
	
	public static EmailWriteFragment getInstance() {
		if (fragment == null) {
			fragment = new EmailWriteFragment();
			Bundle bundle = new Bundle();
			bundle.putString("Title", "Ecrire un EMAIL");
			bundle.putInt("bgColor", 0xff484098);
			bundle.putInt("ImageId", R.drawable.actionbar_email_icon);
			fragment.setArguments(bundle);
		}
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		containerView = inflater.inflate(R.layout.email_write_ui, container, false);
		containerView.findViewById(R.id.btnBack).setOnClickListener(UserApplication.getInstance().getActivity());
		return containerView;
		
	}
	
}
