package com.launch.setting;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.launch.sqlite.AlertService;
import com.launch.sqlite.InactiviteAlert;
import com.launch.ui.R;
import com.launch.ui.UserApplication;
import com.launch.ui.view.SwitchButton;
import com.launch.utils.AlarmQueue;
import com.launch.utils.AlertHandler;
import com.launch.utils.TimeUtils;

public class InactiviteAlertFragment extends Fragment implements OnClickListener, TextWatcher, OnCheckedChangeListener , OnFocusChangeListener{

	private SwitchButton toggle;
	
	private TextView sleepTime;
	
	private EditText sommeilTime;
	
//	private AbstractWheel deactivateTime;

	private EditText avantTime;
	
	private EditText deactivateTime;
	
//	private WheelVerticalView avantTime;
	
	private InactiviteAlert alert;
	
	private AlertService<InactiviteAlert> service;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.inactivite_alert_setting_layout, null);
		toggle = (SwitchButton) view.findViewById(R.id.toggle);
		sleepTime = (TextView) view.findViewById(R.id.sleepTime);
		sleepTime.setOnClickListener(this);
		sommeilTime = (EditText) view.findViewById(R.id.sommeilTime);
		sommeilTime.addTextChangedListener(this);
		sommeilTime.setOnFocusChangeListener(this);
//		deactivateTime = (WheelVerticalView) view.findViewById(R.id.deactivateTime);
		toggle.setOnCheckedChangeListener(this);
//		avantTime = (WheelVerticalView) view.findViewById(R.id.avantTime);
		deactivateTime = (EditText) view.findViewById(R.id.deactivateTime);
		deactivateTime.setOnFocusChangeListener(this);
		deactivateTime.addTextChangedListener(this);
		avantTime = (EditText) view.findViewById(R.id.avantTime);
		avantTime.setOnFocusChangeListener(this);
		avantTime.addTextChangedListener(this);
		UserApplication application = (UserApplication) getActivity().getApplication();
		try {
			service =  (AlertService<InactiviteAlert>) application.getHelper().getAlertService(InactiviteAlert.class);
			alert = service.getAlert();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		initView();
		return view;
	}

	
	private void initView() {
		if(alert == null) {
			alert = new InactiviteAlert();
		}
		toggle.setChecked(alert.isActive());
		sleepTime.setText(TextUtils.isEmpty(alert.getCoucher()) ? TimeUtils.getCurrentTime() : alert.getCoucher());
		
		sommeilTime.setText(alert.getSommeil());
//		deactivateTime.setViewAdapter(new TimeWheelAdapter(getActivity(), TimeUtils.getMinteArray()));
//		deactivateTime.setCurrentItem(alert.getDesarmocage());
//		avantTime.setViewAdapter(new TimeWheelAdapter(getActivity(), TimeUtils.getTenHourArray()));
//		avantTime.setCurrentItem(alert.getLalerte());
		deactivateTime.setText(Integer.toString(alert.getDesarmocage()));
		avantTime.setText(Integer.toString(alert.getLalerte()));
	}

	

	private void collect() {
		alert.setCoucher(sleepTime.getText().toString());
		alert.setSommeil(sommeilTime.length() == 0 ? "1" : sommeilTime.getText().toString());
//		alert.setDesarmocage(deactivateTime.getCurrentItem());
//		alert.setDesarmocage(avantTime.getCurrentItem());
		alert.setDesarmocage(deactivateTime.length() == 0 ? 1 : Integer.parseInt(deactivateTime.getText().toString()));
		alert.setLalerte(avantTime.length() == 0 ? 0 : Integer.parseInt(avantTime.getText().toString()));
		alert.setActive(toggle.isChecked());
		if(alert.isActive()) {
			String text = sleepTime.getText().toString();
			final Calendar cd = Calendar.getInstance();
			final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			if(!TextUtils.isEmpty(text)) {
				try {
					Date date = sdf.parse(text);
					cd.setTime(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, cd.get(Calendar.HOUR_OF_DAY) + Integer.parseInt(alert.getSommeil()));
			calendar.set(Calendar.MINUTE, cd.get(Calendar.MINUTE));
			AlarmQueue.getInstance().registerAlarmTimeByInactivite(getActivity(), calendar.getTimeInMillis());
		} else {
			AlarmQueue.getInstance().unRegisterAlarmTimeByactivite(getActivity());
		}
	}

	
	
	
	
	@Override
	public void onPause() {
		super.onPause();
		if (service != null) {
			collect();
			if (service.saveOrUpdateAlert(alert)) {
				Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}


	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean checked) {
		UserApplication application = (UserApplication) getActivity().getApplication();
		if(checked) {
//			application.getHandler().launchInactivite();
		} else {
			AlertHandler.closeInactivite();
		}
	}


	@Override
	public void onClick(View arg0) {
		String text = sleepTime.getText().toString();
		final Calendar calendar = Calendar.getInstance();
		final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		if(!TextUtils.isEmpty(text)) {
			try {
				Date date = sdf.parse(text);
				calendar.setTime(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		TimeUtils.showTimepicker(getActivity(), calendar, new OnTimeSetListener() {
			
			@Override
			public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
				calendar.set(Calendar.HOUR_OF_DAY, arg1);
				calendar.set(Calendar.MINUTE,arg2);
				Date date = calendar.getTime();
				sleepTime.setText(sdf.format(date));
			}
		});		
	}


	@Override
	public void afterTextChanged(Editable editable) {
		if(editable.length() == 0) {
			return;
		}
		if(editable == avantTime.getEditableText()) {
			try {		
				int number = Integer.parseInt(avantTime.getText().toString());
				if(number >=0 && number <= 24) {
					return;
				}
				if(number > 24) {
					number = 24;
				}
				if(number < 0) {
					number = 0;
				}
				avantTime.setText(number + "");
			} catch(NumberFormatException e) {
				avantTime.setText("0");
			}finally {
				avantTime.setSelection(avantTime.length());
			}
		} else if(editable == deactivateTime.getEditableText()) {
			try {		
				int number = Integer.parseInt(deactivateTime.getText().toString());
				if(number >=1 && number <= 60) {
					return;
				}
				if(number > 60) {
					number = 60;
				}
				if(number < 1) {
					number = 1;
				}
				deactivateTime.setText(number + "");
			} catch(NumberFormatException e) {
				deactivateTime.setText("1");
			}finally {
				deactivateTime.setSelection(deactivateTime.length());
			}
		} else if(editable == sommeilTime.getEditableText()) {
			try {		
				int number = Integer.parseInt(sommeilTime.getText().toString());
				if(number >=1 && number <= 10) {
					return;
				}
				if(number > 10) {
					number = 10;
				}
				if(number < 1) {
					number = 1;
				}
				sommeilTime.setText(number + "");
			} catch(NumberFormatException e) {
				sommeilTime.setText("1");
			} finally {
				sommeilTime.setSelection(sommeilTime.length());
			}
		}
	}


	@Override
	public void beforeTextChanged(CharSequence s, int arg1, int arg2,
			int arg3) {
		
	}


	@Override
	public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
		
	}
	
	@Override
	public void onFocusChange(View edittext, boolean hasFocus) {
		if(!hasFocus) {
			if(edittext == sommeilTime) {
				String str = sommeilTime.getText().toString();
				if(str.length() == 0) {
					sommeilTime.setText("1");
					sommeilTime.setSelection(sommeilTime.length());
				}
			} else if(edittext == deactivateTime) {
				String str = deactivateTime.getText().toString();
				if(str.length() == 0) {
					deactivateTime.setText("1");
					deactivateTime.setSelection(deactivateTime.length());
				}
			} else if(edittext == avantTime) {
				String str = avantTime.getText().toString();
				if(str.length() == 0) {
					avantTime.setText("0");
					avantTime.setSelection(avantTime.length());
				}
			}
			return;
		}
		if(edittext == sommeilTime) {
			String str = sommeilTime.getText().toString();
			if(str.length() == 1 && str.equals("1")) {
				sommeilTime.setText("");
			}
		} else if(edittext == deactivateTime) {
			String str = deactivateTime.getText().toString();
			if(str.length() == 1 && str.equals("1")) {
				deactivateTime.setText("");
			}
		} else if(edittext == avantTime) {
			String str = avantTime.getText().toString();
			if(str.length() == 1 && str.equals("0")) {
				avantTime.setText("");
			}
		}
		
	}
}
