package com.launch.ui;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.launch.sqlite.Person;
import com.launch.sqlite.PersonService;
import com.launch.utils.SosFunction;
import com.launch.utils.ThirdLaunch;
import com.launch.utils.TimeUtils;

import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ReglagesProfileInformationFragment extends Fragment implements OnClickListener{
	public static ReglagesProfileInformationFragment fragment;

	private View containerView;
	
	private EditText nom;
	
	private EditText prenom;
	
	private View birthDay;
	
	private RadioGroup group;
	
	private Button update;
	
	private Person person;
	
	private TextView birthValue;
	
	
	private PersonService service;
	
	public static ReglagesProfileInformationFragment getInstance() {
		if (fragment == null) {
			fragment = new ReglagesProfileInformationFragment();
			Bundle bundle = new Bundle();
			bundle.putString("Title", "R�glagess Mon Profil Informations Personnelles");
			bundle.putInt("bgColor", 0xff091302);
			bundle.putInt("ImageId", R.drawable.informations);
			fragment.setArguments(bundle);
		}
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((PageActivity)getActivity()).setIcon(getArguments().getInt("bgColor"), getArguments().getInt("ImageId"));
		containerView = inflater.inflate(R.layout.reglages_profil_information_ui, container, false);
		containerView.findViewById(R.id.btnBack).setOnClickListener(UserApplication.getInstance().getActivity());
		nom = (EditText) containerView.findViewById(R.id.nom);
		prenom = (EditText) containerView.findViewById(R.id.prenom);
		birthDay = containerView.findViewById(R.id.birthday);
		birthDay.setOnClickListener(this);
		birthValue = (TextView) containerView.findViewById(R.id.value);
		group = (RadioGroup) containerView.findViewById(R.id.group);
		update = (Button) containerView.findViewById(R.id.update);
		update.setOnClickListener(this);
		UserApplication application = (UserApplication) getActivity().getApplication();
		try {
			service =  application.getHelper().getPersonService();
			person = service.getPerson();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		initView();
		return containerView;
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
