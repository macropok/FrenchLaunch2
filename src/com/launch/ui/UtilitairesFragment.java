package com.launch.ui;

import com.launch.sqlite.P;
import com.launch.utils.Preferences;
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

public class UtilitairesFragment extends Fragment {
	public static UtilitairesFragment fragment;

	private View containerView;
	
	public static UtilitairesFragment getInstance() {
		if (fragment == null) {
			fragment = new UtilitairesFragment();
			Bundle bundle = new Bundle();
			bundle.putString("Title", "Utilitaires");
			bundle.putInt("bgColor", 0xff9d1442);
			bundle.putInt("ImageId", R.drawable.actionbar_utilitaires_icon);
			fragment.setArguments(bundle);
		}
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((PageActivity)getActivity()).setIcon(getArguments().getInt("bgColor"), getArguments().getInt("ImageId"));
		containerView = inflater.inflate(R.layout.utilitaires_ui, container, false);
OnClickListener listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(v.getId()==R.id.button1){
					ThirdLaunch.launchNotes(getActivity());
				}
				else if(v.getId()==R.id.button2){
					ThirdLaunch.launchCalculator(getActivity());
				}
				else if(v.getId()==R.id.button3)
				{
					ThirdLaunch.launchCalendar(getActivity());
				}
				else if(v.getId()==R.id.button4)
				{
					ThirdLaunch.launchTimeApp(getActivity());
				}
				else if(v.getId()==R.id.button5)
				{
					ThirdLaunch.launchWeather(getActivity());
				}
				else if(v.getId()==R.id.button6)
				{
					ThirdLaunch.launchTorch(getActivity());
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
		Button btn4 = (Button)containerView.findViewById(R.id.button4);
		btn4.setOnClickListener(listener);
		Button btn5 = (Button)containerView.findViewById(R.id.button5);
		btn5.setOnClickListener(listener);
		Button btn6 = (Button)containerView.findViewById(R.id.button6);
		btn6.setOnClickListener(listener);
		containerView.findViewById(R.id.btnBack).setOnClickListener(UserApplication.getInstance().getActivity());
		return containerView;
		
	}
	
}
