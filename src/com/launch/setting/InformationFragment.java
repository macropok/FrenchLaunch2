package com.launch.setting;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.launch.sqlite.Person;
import com.launch.sqlite.PersonService;
import com.launch.ui.R;
import com.launch.ui.UserApplication;
import com.launch.utils.TimeUtils;

public class InformationFragment extends Fragment implements OnClickListener{

	private EditText nom;
	
	private EditText prenom;
	
	private View birthDay;
	
	private RadioGroup group;
	
	private Button update;
	
	private Person person;
	
	private TextView birthValue;
	
	
	private PersonService service;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.information_setting_layout, null);
		
		nom = (EditText) view.findViewById(R.id.nom);
		prenom = (EditText) view.findViewById(R.id.prenom);
		birthDay = view.findViewById(R.id.birthday);
		birthDay.setOnClickListener(this);
		birthValue = (TextView) view.findViewById(R.id.value);
		group = (RadioGroup) view.findViewById(R.id.group);
		update = (Button) view.findViewById(R.id.update);
		update.setOnClickListener(this);
		UserApplication application = (UserApplication) getActivity().getApplication();
		try {
			service =  application.getHelper().getPersonService();
			person = service.getPerson();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		initView();
		return view;
	}

	
	private void initView() {
		if(person == null) {
			person = new Person();
		}
		nom.setText(person.getNom());
		prenom.setText(person.getPrenom());
		birthValue.setText(person.getBirthday());
		group.check(person.isMale() ? R.id.homme : R.id.femme);
	}
	

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.birthday) {
			String text = birthValue.getText().toString();
			final Calendar calendar = Calendar.getInstance();
			final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			if(!TextUtils.isEmpty(text)) {
				try {
					Date date = sdf.parse(text);
					calendar.setTime(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			}
			TimeUtils.showDatapicker(getActivity(), calendar, new OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
					calendar.set(Calendar.YEAR, arg1);
					calendar.set(Calendar.MONTH,arg2);
					calendar.set(Calendar.DAY_OF_MONTH, arg3);
					Date date = calendar.getTime();
					birthValue.setText(sdf.format(date));
				}
			});
		} else if(id == R.id.update) {
			if(service != null) {
				collect();
				if(service.saveOrUpdatePerson(person)) {
					Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	
	private void collect() {
		person.setNom(nom.getText().toString());
		person.setPrenom(prenom.getText().toString());
		person.setBirthday(birthValue.getText().toString());
		person.setMale(group.getCheckedRadioButtonId() == R.id.homme ? true : false);
	}


}
