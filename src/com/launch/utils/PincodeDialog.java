package com.launch.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.launch.ui.R;
import com.launch.ui.UserApplication;

public class PincodeDialog extends Dialog implements android.view.View.OnClickListener{

	private EditText code;
	
	private FragmentActivity activity;
	
	private View backBtn;
	
	public PincodeDialog(Context context, int theme, View _back) {
		super(context, theme);
		this.activity =  (FragmentActivity) context;
		backBtn = _back;
	}

	public PincodeDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pincode_layout);
		code = (EditText) findViewById(R.id.code);
		findViewById(R.id.ok).setOnClickListener(this);
		findViewById(R.id.cancel).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.ok) {
			String pin = code.getText().toString();
			if(TextUtils.isEmpty(pin)) {
				return;
			}
			if(TextUtils.equals(pin, "0000")){
				dismiss();
			}	
		} else if(R.id.cancel == id) {
			if(backBtn!=null)
				backBtn.performClick();
			else
				((UserApplication) activity.getApplication()).popFragment();
			dismiss();
			//UserApplication application = (UserApplication) activity.getApplication();
			//application.popFragment();
		}
		
	}
}