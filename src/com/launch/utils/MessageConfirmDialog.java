package com.launch.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.launch.ui.R;
import com.launch.utils.MessageUtils.NX;

public class MessageConfirmDialog extends Dialog implements android.view.View.OnClickListener{

	private TextView message;
	
	private Context activity;
	
	private OnConfirmListener listener;
	
	private NX nx;
	public MessageConfirmDialog(Context context, NX nx, OnConfirmListener listener) {
		super(context, R.style.dialog);
		this.listener = listener;
		this.nx = nx;
		this.activity =  context;
	}
	
	
	
	public interface OnConfirmListener {
		public void onConfirm(NX nx) ;
	}

	public MessageConfirmDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_confirm_layout);
		message = (TextView) findViewById(R.id.message);
		findViewById(R.id.ok).setOnClickListener(this);
		findViewById(R.id.cancel).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.ok) {
			dismiss();
			if(listener != null) {
				listener.onConfirm(nx);
			}
		} else if(R.id.cancel == id) {
			dismiss();
		}
		
	}
}