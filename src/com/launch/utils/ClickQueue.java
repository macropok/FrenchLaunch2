package com.launch.utils;

import android.app.Fragment;
import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.FragmentTransaction;

import com.launch.setting.VoulezAlertFragment.Voulez;
import com.launch.ui.ApplicationsFragment;
import com.launch.ui.EmailFragment;
import com.launch.ui.InternetFragment;
import com.launch.ui.MesAlertesFragment;
import com.launch.ui.R;
import com.launch.ui.ReglagesFragment;
import com.launch.ui.RepertoireFragment;
import com.launch.ui.SMSFragment;
import com.launch.ui.TelephoneFragment;
import com.launch.ui.UserApplication;
import com.launch.ui.UtilitairesFragment;
import com.launch.utils.MessageUtils.NX;

public class ClickQueue {

	private boolean isHandle;

	private static ClickQueue instance;

	private boolean isLaunch;

	private Vibrator vibrator;

	private ClickQueue() {
		vibrator = (Vibrator) UserApplication.getInstance().getActivity()
				.getSystemService(Context.VIBRATOR_SERVICE);
	}

	public static ClickQueue getInstance() {
		if (instance == null) {
			instance = new ClickQueue();
		}

		return instance;
	}

	private void execute(int id) {
		handler.sendEmptyMessage(0);
		switch (id) {
		// case R.id.call1:
		// AlertHandler.call1();
		// break;
		//
		// case R.id.call2:
		// AlertHandler.call2();
		// break;
		// case R.id.minuteur:
		// ThirdLaunch.launchTimeApp(UserApplication.getInstance()
		// .getActivity());
		// break;
		// case R.id.tel:
		// LaunchUtils.callphoneByoutline("", UserApplication.getInstance()
		// .getActivity());
		// break;
		// case R.id.sms:
		// // LaunchUtils.sendMessageByoutline("this is a test message", "",
		// // getActivity());
		// ThirdLaunch.launchMMS(UserApplication.getInstance().getActivity());
		// break;
		// case R.id.email:
		// // LaunchUtils.launchEmail("This is a test email",
		// // "kalen_sytina@126.com", getActivity());
		// ThirdLaunch.launchEmasil(UserApplication.getInstance()
		// .getActivity());
		// break;
		// case R.id.contacts:
		// LaunchUtils.openContacts(UserApplication.getInstance()
		// .getActivity());
		// break;
		// case R.id.sante:
		// MessageQuene.getInstance(UserApplication.getInstance().getActivity()).addMessage(NX.SANTE,
		// true);
		// break;
		// case R.id.agression:
		// MessageQuene.getInstance(UserApplication.getInstance().getActivity()).addMessage(NX.AGRESSION,
		// true);
		// break;
		// case R.id.feu:
		// MessageQuene.getInstance(UserApplication.getInstance().getActivity()).addMessage(NX.FEU,
		// true);
		// break;
		// // page2
		// case R.id.camera:
		// ThirdLaunch.launchCamera(UserApplication.getInstance()
		// .getActivity());
		// break;
		// case R.id.calendar:
		// ThirdLaunch.launchCalendar(UserApplication.getInstance()
		// .getActivity());
		// break;
		// case R.id.internet:
		// ThirdLaunch.launchInternet(UserApplication.getInstance()
		// .getActivity());
		// break;
		// case R.id.social:
		// ThirdLaunch.launchSocail(UserApplication.getInstance()
		// .getActivity());
		// break;
		// // page3
		//
		// case R.id.sante2:
		// break;
		// case R.id.apps:
		// ThirdLaunch.launchApps(UserApplication.getInstance().getActivity());
		// break;
		// case R.id.mon_chemin:
		// ThirdLaunch.launchMap(UserApplication.getInstance().getActivity());
		// break;
		// case R.id.parameter:
		//
		// break;

		case R.id.telephone:
			//LaunchUtils.callphoneByoutline("", UserApplication.getInstance().getActivity());
			UserApplication.getInstance().getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contaier, TelephoneFragment.getInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
			//UserApplication.getInstance().getActivity().setTitle("Telephone");			
			//UserApplication.getInstance().getActivity().setIcon(0xff51b63c, R.drawable.actionbar_tel_icon);
			break;
		case R.id.contact:
			//LaunchUtils.openContacts(UserApplication.getInstance().getActivity());
			UserApplication.getInstance().getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contaier, RepertoireFragment.getInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
			break;
		case R.id.sms:
			//ThirdLaunch.launchMMS(UserApplication.getInstance().getActivity());
			UserApplication.getInstance().getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contaier, SMSFragment.getInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
			break;
		case R.id.email:
			//ThirdLaunch.launchEmail(UserApplication.getInstance().getActivity());
			UserApplication.getInstance().getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contaier, EmailFragment.getInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
			break;
		case R.id.camera:
			ThirdLaunch.launchCamera(UserApplication.getInstance()
					 .getActivity());
			break;
		case R.id.galerie:
			//
			ThirdLaunch.launchPhoto(UserApplication.getInstance().getActivity());
			break;
		case R.id.utilitaires:
			UserApplication.getInstance().getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contaier, UtilitairesFragment.getInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
			break;
		case R.id.internet:
			//ThirdLaunch.launchInternet(UserApplication.getInstance().getActivity());
			UserApplication.getInstance().getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contaier, InternetFragment.getInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
			break;
		case R.id.facebook:
			//
			ThirdLaunch.launchFacebook(UserApplication.getInstance().getActivity());
			break;
		case R.id.maps:
			ThirdLaunch.launchMap(UserApplication.getInstance().getActivity());
			break;
		case R.id.nothing:
			break;
		case R.id.app:
			//ThirdLaunch.launchApps(UserApplication.getInstance().getActivity());
			//ThirdLaunch.launchAppStore(UserApplication.getInstance().getActivity());
			UserApplication.getInstance().getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contaier, ApplicationsFragment.getInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
			break;
		case R.id.alert:
			UserApplication.getInstance().getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contaier, MesAlertesFragment.getInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
			break;
		case R.id.setting:
			UserApplication.getInstance().getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contaier, ReglagesFragment.getInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
			break;
		case R.id.sos:
			MessageQuene.getInstance(UserApplication.getInstance().getActivity()).addMessage(NX.SANTE,
					 true);
			break;
		}
	}

	public void executeStart() {
		if (isHandle) {
			return;
		}
		isHandle = true;
		Voulez longclick = Preferences.getLongClick(UserApplication
				.getInstance().getActivity());
		if (longclick == Voulez.NON) {
			return;
		}
		handler.sendEmptyMessageDelayed(2, 1000);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				long[] pattern = { 100, 400, 100 };
				vibrator.vibrate(pattern, 1);
				sendEmptyMessageDelayed(1, 600);
			} else if (msg.what == 2) {
				isLaunch = true;
			} else {
				isLaunch = false;
				vibrator.cancel();
			}
		};
	};

	public void cancelExecute() {
		isHandle = false;
		isLaunch = false;
		handler.removeMessages(0);
	}

	public void executeClick(int id) {
		handler.removeMessages(2);
		Voulez longclick = Preferences.getLongClick(UserApplication
				.getInstance().getActivity());
		switch (longclick) {
		case NON:
			execute(id);
			break;
		case ALERT:
			if (id == R.id.sos) {
				if (isLaunch) {
					execute(id);
				}
			} else {
				execute(id);
			}
			break;
		case PARTOUT:
			if (isLaunch) {
				execute(id);
			}
			break;
		}
		isLaunch = false;
		isHandle = false;
	}

}
