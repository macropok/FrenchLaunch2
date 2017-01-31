package com.launch.ui;

import java.sql.SQLException;

import com.launch.sqlite.Person;
import com.launch.sqlite.PersonService;
import com.launch.utils.SosFunction;
import com.launch.utils.ThirdLaunch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ReglagesProfileAccessFragment extends Fragment implements OnClickListener{
	public static ReglagesProfileAccessFragment fragment;

	private View containerView;
	
	private EditText batiment;

	private EditText etae;

	private EditText prote;
	private EditText interphone;

	private Button update;

	private PersonService service;

	private Person person;
	
	public static ReglagesProfileAccessFragment getInstance() {
		if (fragment == null) {
			fragment = new ReglagesProfileAccessFragment();
			Bundle bundle = new Bundle();
			bundle.putString("Title", "R�glagess Mon Profil Votre acces");
			bundle.putInt("bgColor", 0xff091302);
			bundle.putInt("ImageId", R.drawable.votre_acces);
			fragment.setArguments(bundle);
		}
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((PageActivity)getActivity()).setIcon(getArguments().getInt("bgColor"), getArguments().getInt("ImageId"));
		containerView = inflater.inflate(R.layout.reglages_profil_access_ui, container, false);
		containerView.findViewById(R.id.btnBack).setOnClickListener(UserApplication.getInstance().getActivity());
		
		batiment = (EditText) containerView.findViewById(R.id.batiment);
		etae = (EditText) containerView.findViewById(R.id.etage);
		interphone = (EditText) containerView.findViewById(R.id.interphone);
		prote = (EditText) containerView.findViewById(R.id.porte);
		update = (Button) containerView.findViewById(R.id.update);
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
		return containerView;
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
