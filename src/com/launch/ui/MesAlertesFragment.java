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

public class MesAlertesFragment extends Fragment {
	public static MesAlertesFragment fragment;

	private View containerView;
	
	public static MesAlertesFragment getInstance() {
		if (fragment == null) {
			fragment = new MesAlertesFragment();
			Bundle bundle = new Bundle();
			bundle.putString("Title", "Mes Alertes");
			bundle.putInt("bgColor", 0xff414b39);
			bundle.putInt("ImageId", R.drawable.actionbar_mes_alertes_icon);
			fragment.setArguments(bundle);
		}
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((PageActivity)getActivity()).setIcon(getArguments().getInt("bgColor"), getArguments().getInt("ImageId"));
		containerView = inflater.inflate(R.layout.mes_alertes_ui, container, false);
		OnClickListener listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(v.getId()==R.id.button1)
					UserApplication.getInstance().getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contaier, MesAlertesChuteFragment.getInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
				else if(v.getId()==R.id.button2)
					UserApplication.getInstance().getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contaier, MesAlertesInactiviteFragment.getInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
				else if(v.getId()==R.id.button3)
					UserApplication.getInstance().getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contaier, MesAlertesBatterieFragment.getInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
				else if(v.getId()==R.id.button4)
					UserApplication.getInstance().getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contaier, MesAlertesSecuriteFragment.getInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
				//getChildFragmentManager().beginTransaction().replace(R.id.contaier, RepertoireReadFragment.getInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack("RepertoireReadFragment").commit();
			}
		};
		Button btn1 = (Button)containerView.findViewById(R.id.button1);
		btn1.setOnClickListener(listener);
		Button btn2 = (Button)containerView.findViewById(R.id.button2);
		btn2.setOnClickListener(listener);
		Button btn3 = (Button)containerView.findViewById(R.id.button3);
		btn3.setOnClickListener(listener);
		Button btn4 = (Button)containerView.findViewById(R.id.button4);
		btn4.setOnClickListener(listener);
		containerView.findViewById(R.id.btnBack).setOnClickListener(UserApplication.getInstance().getActivity());
		return containerView;
		
	}
	
}
