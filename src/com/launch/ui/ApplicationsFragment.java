package com.launch.ui;

import java.util.List;

import com.launch.utils.SosFunction;
import com.launch.utils.ThirdLaunch;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ApplicationsFragment extends Fragment {
	public static ApplicationsFragment fragment;

	private View containerView;
	
	public static ApplicationsFragment getInstance() {
		if (fragment == null) {
			fragment = new ApplicationsFragment();
			Bundle bundle = new Bundle();
			bundle.putString("Title", "Applications");
			bundle.putInt("bgColor", 0xffadb117);
			bundle.putInt("ImageId", R.drawable.actionbar_applications_icon);
			fragment.setArguments(bundle);
		}
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((PageActivity)getActivity()).setIcon(getArguments().getInt("bgColor"), getArguments().getInt("ImageId"));
		containerView = inflater.inflate(R.layout.applications_ui, container, false);
		OnClickListener listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(v.getId()==R.id.button1)
					UserApplication.getInstance().getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contaier, ApplicationsListFragment.getInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
				else if(v.getId()==R.id.button2){
					//UserApplication.getInstance().getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contaier, RepertoireWriteFragment.getInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
					ThirdLaunch.launchAppStore(getActivity());
				}
				//getChildFragmentManager().beginTransaction().replace(R.id.contaier, RepertoireReadFragment.getInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack("RepertoireReadFragment").commit();
			}
		};
		Button btn1 = (Button)containerView.findViewById(R.id.button1);
		btn1.setOnClickListener(listener);
		Button btn2 = (Button)containerView.findViewById(R.id.button2);
		btn2.setOnClickListener(listener);
		containerView.findViewById(R.id.btnBack).setOnClickListener(UserApplication.getInstance().getActivity());
		return containerView;
		
	}
	
}
