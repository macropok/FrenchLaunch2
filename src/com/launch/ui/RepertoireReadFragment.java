package com.launch.ui;

import com.launch.utils.SosFunction;
import com.launch.utils.ThirdLaunch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RepertoireReadFragment extends Fragment {
	public static RepertoireReadFragment fragment;

	private View containerView;
	
	public static RepertoireReadFragment getInstance() {
		if (fragment == null) {
			fragment = new RepertoireReadFragment();
			Bundle bundle = new Bundle();
			bundle.putString("Title", "Voir contacts");
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
		containerView = inflater.inflate(R.layout.repertoire_read_ui, container, false);
		OnClickListener listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(v.getId()==R.id.button1)
				{
					//ThirdLaunch.launchViewFavorisContacts(getActivity());
					ThirdLaunch.launchCustomContactPicker(getActivity(),"favoris","view", PageActivity.VIEW_CONTACT);
				}
				else if(v.getId()==R.id.button2)
				{
					//ThirdLaunch.launchViewCommunauteContacts(getActivity());
					ThirdLaunch.launchCustomContactPicker(getActivity(),"communaute","view", PageActivity.VIEW_CONTACT);
				}
				else if(v.getId()==R.id.button3)
				{
					ThirdLaunch.launchViewAllContacts(getActivity());
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
		return containerView;
		
	}
	
}
