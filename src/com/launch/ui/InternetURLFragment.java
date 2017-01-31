package com.launch.ui;

import com.launch.utils.SosDialog;
import com.launch.utils.SosFunction;
import com.launch.utils.ThirdLaunch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class InternetURLFragment extends Fragment {
	public static InternetURLFragment fragment;

	private View containerView;
	
	private int sosHeight;
	
	public static InternetURLFragment getInstance() {
		if (fragment == null) {
			fragment = new InternetURLFragment();
			Bundle bundle = new Bundle();
			bundle.putString("Title", "Internet");
			bundle.putInt("bgColor", 0xffb23a17);
			bundle.putInt("ImageId", R.drawable.actionbar_internet_icon);
			fragment.setArguments(bundle);
		}
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((PageActivity)getActivity()).setIcon(getArguments().getInt("bgColor"), getArguments().getInt("ImageId"));
		containerView = inflater.inflate(R.layout.internet_url_ui, container, false);
		OnClickListener listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(v.getId()==R.id.btnBrowser){
					//UserApplication.getInstance().getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contaier, InternetURLFragment.getInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
					ThirdLaunch.launchInternet(getActivity(),((EditText)containerView.findViewById(R.id.txtURL)).getText().toString());
				}else if(v.getId()==R.id.txtURL)
				{
					sosHeight = SosDialog.newInstance(null, null).getDesiredHeight();
					SosDialog.hide();
				}
			}
		};
		ImageButton btnBrowser = (ImageButton)containerView.findViewById(R.id.btnBrowser);
		btnBrowser.setOnClickListener(listener);
		EditText txtURL = (EditText)containerView.findViewById(R.id.txtURL);
		txtURL.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if(actionId==EditorInfo.IME_ACTION_DONE)
				{
					SosDialog.show(sosHeight);
					//return true;
				}
				return false;
			}
		});
		txtURL.setOnClickListener(listener);
		containerView.findViewById(R.id.btnBack).setOnClickListener(UserApplication.getInstance().getActivity());
		return containerView;
		
	}
	
}
