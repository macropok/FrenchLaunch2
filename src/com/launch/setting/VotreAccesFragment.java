package com.launch.setting;

import java.sql.SQLException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.launch.sqlite.Person;
import com.launch.sqlite.PersonService;
import com.launch.ui.R;
import com.launch.ui.UserApplication;

public class VotreAccesFragment extends Fragment implements OnClickListener {

	private EditText batiment;

	private EditText etae;

	private EditText prote;
	private EditText interphone;

	private Button update;

	private PersonService service;

	private Person person;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.votre_access_setting_layout, null);
		batiment = (EditText) view.findViewById(R.id.batiment);
		etae = (EditText) view.findViewById(R.id.etae);
		interphone = (EditText) view.findViewById(R.id.interphone);
		prote = (EditText) view.findViewById(R.id.porte);
		update = (Button) view.findViewById(R.id.update);
		update.setOnClickListener(this);
		UserApplication application = (UserApplication) getActivity()
				.getApplication();
		try {
			service = application.getHelper().getPersonService();
			person = service.getPerson();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		initView();
		return view;
	}

	private void initView() {
		if (person == null) {
			person = new Person();
		}
		batiment.setText(person.getBatiment());
		etae.setText(person.getEtae());
		prote.setText(person.getPorte());
		interphone.setText(person.getInterphone());
	}

	private void collect() {
		person.setBatiment(batiment.getText().toString());
		person.setEtae(etae.getText().toString());
		person.setPorte(prote.getText().toString());
		person.setInterphone(interphone.getText().toString());
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.update) {
			if (service != null) {
				collect();
				if (service.saveOrUpdatePerson(person)) {
					Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT)
							.show();
				}
			}
		}
	}
}
