package com.launch.ui;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.launch.sqlite.AlertService;
import com.launch.sqlite.InactiviteAlert;
import com.launch.ui.view.SwitchButton;
import com.launch.utils.AlarmQueue;
import com.launch.utils.AlertHandler;
import com.launch.utils.SosFunction;
import com.launch.utils.TimeUtils;

import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MesAlertesInactiviteFragment extends Fragment implements OnCheckedChangeListener,OnClickListener {
	public static MesAlertesInactiviteFragment fragment;

	private View containerView;
	
	ArrayAdapter<Integer> countDownAdapter;
	
	private SwitchButton toggle;
	
	private TextView sleepTime;
	
	private TextView wakeupTime;
	
//	private AbstractWheel deactivateTime;

	private EditText avantTime;
	
	private Spinner deactivateTime;
	
//	private WheelVerticalView avantTime;
	
	private InactiviteAlert alert;
	
	private AlertService<InactiviteAlert> service;
	
	public static MesAlertesInactiviteFragment getInstance() {
		if (fragment == null) {
			fragment = new MesAlertesInactiviteFragment();
			Bundle bundle = new Bundle();
			bundle.putString("Title", "Mes Alertes");
			bundle.putInt("bgColor", 0xff414b39);
			bundle.putInt("ImageId", R.drawable.inactive);
			fragment.setArguments(bundle);
		}
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((PageActivity)getActivity()).setIcon(getArguments().getInt("bgColor"), getArguments().getInt("ImageId"));
		containerView = inflater.inflate(R.layout.mes_alertes_inactivite_ui, container, false);
		deactivateTime = (Spinner)containerView.findViewById(R.id.spinnerCancelCountdown);
		ArrayList<Integer> vals = new ArrayList<Integer>();
		for(int i=10;i<100;i+=5)
		{
			vals.add(i);
		}
		vals.add(99);
		countDownAdapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, vals);
		countDownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		deactivateTime.setAdapter(countDownAdapter);
		
		
		sleepTime = (TextView)containerView.findViewById(R.id.sleepTime);
		sleepTime.setOnClickListener(this);
		wakeupTime = (TextView)containerView.findViewById(R.id.wakeupTime);
		wakeupTime.setOnClickListener(this);
		
		containerView.findViewById(R.id.btnBack).setOnClickListener(UserApplication.getInstance().getActivity());
		
		toggle = (SwitchButton) containerView.findViewById(R.id.toggleInactivite);

		
//		deactivateTime = (WheelVerticalView) view.findViewById(R.id.deactivateTime);
		toggle.setOnCheckedChangeListener(this);
//		avantTime = (WheelVerticalView) view.findViewById(R.id.avantTime);
		avantTime = (EditText) containerView.findViewById(R.id.inactivitieTimeBeforeAlert);

		UserApplication application = (UserApplication) getActivity().getApplication();
		try {
			service =  (AlertService<InactiviteAlert>) application.getHelper().getAlertService(InactiviteAlert.class);
			alert = service.getAlert();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		initView();
		
		return containerView;
		
	}
	private void initView() {
		if(alert == null) {
			alert = new InactiviteAlert();
		}
		toggle.setChecked(alert.isActive());
		sleepTime.setText(TextUtils.isEmpty(alert.getCoucher()) ? TimeUtils.getCurrentTime() : alert.getCoucher());
		
		wakeupTime.setText(TextUtils.isEmpty(alert.getSommeil()) ? TimeUtils.getCurrentTime() : alert.getSommeil());
		//sommeilTime.setText(alert.getSommeil());
//		deactivateTime.setViewAdapter(new TimeWheelAdapter(getActivity(), TimeUtils.getMinteArray()));
//		deactivateTime.setCurrentItem(alert.getDesarmocage());
//		avantTime.setViewAdapter(new TimeWheelAdapter(getActivity(), TimeUtils.getTenHourArray()));
//		avantTime.setCurrentItem(alert.getLalerte());
		deactivateTime.setSelection(countDownAdapter.getPosition(alert.getDesarmocage()));
		avantTime.setText(Integer.toString(alert.getLalerte()));
	}

	

	private void collect() {
		alert.setCoucher(sleepTime.getText().toString());
		alert.setSommeil(wakeupTime.getText().toString());
//		alert.setDesarmocage(deactivateTime.getCurrentItem());
//		alert.setDesarmocage(avantTime.getCurrentItem());
		alert.setDesarmocage((Integer)deactivateTime.getSelectedItem());
		alert.setLalerte(avantTime.length() == 0 ? 0 : Integer.parseInt(avantTime.getText().toString()));
		alert.setActive(toggle.isChecked());
		if(alert.isActive()) {
			//String text = sleepTime.getText().toString();
			String text = wakeupTime.getText().toString();
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
			calendar.set(Calendar.HOUR_OF_DAY, cd.get(Calendar.HOUR_OF_DAY) /*+ Integer.parseInt(alert.getSommeil())*/);
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
				//Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
			} else {
				//Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();
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
		final TextView timeView = (TextView)arg0;
		String text = timeView.getText().toString();
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
				timeView.setText(sdf.format(date));
			}
		});		
	}
}
