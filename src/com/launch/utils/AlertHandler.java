package com.launch.utils;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import javax.mail.MessagingException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.launch.sqlite.AlertService;
import com.launch.sqlite.Avance;
import com.launch.sqlite.AvanceService;
import com.launch.sqlite.Contact;
import com.launch.sqlite.ContactService;
import com.launch.sqlite.InactiviteAlert;
import com.launch.ui.UserApplication;
import com.launch.utils.MessageUtils.NX;
import com.launch.utils.ProgressDialog.onEmailListener;
import com.launch.utils.ProgressDialog.onMessageListener;

public class AlertHandler {

	public static boolean checkSIM(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);// 取得相关系统服务
		StringBuffer sb = new StringBuffer();
		switch (tm.getSimState()) { // getSimState()取得sim的状态 有下面6中状态
		case TelephonyManager.SIM_STATE_ABSENT:
			sb.append("无卡");
			break;
		case TelephonyManager.SIM_STATE_UNKNOWN:
			sb.append("未知状态");
			break;
		case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
			sb.append("需要NetworkPIN解锁");
			break;
		case TelephonyManager.SIM_STATE_PIN_REQUIRED:
			sb.append("需要PIN解锁");
			break;
		case TelephonyManager.SIM_STATE_PUK_REQUIRED:
			sb.append("需要PUK解锁");
			break;
		case TelephonyManager.SIM_STATE_READY:
			sb.append("良好");
			return true;
		}
		return false;
	}

	public static void sendMessages(Context context, String message,
			onMessageListener listener) {
		if (!checkSIM(context)) {
			if (listener != null) {
				listener.onMmsHandle(false);
			}
			return;
		}
		AvanceService avanceService = UserApplication.getInstance()
				.getAvanceService();
		ContactService contactService = UserApplication.getInstance()
				.getContactService();
		if (avanceService == null || contactService == null) {
			if (listener != null) {
				listener.onMmsHandle(false);
			}
			return;
		}
		List<Contact> contacts = contactService.getAllContacts();
		if (contacts != null && contacts.size() > 0) {
			for (Contact contact : contacts) {
				if (contact.getPhone().isEmpty()) {
					continue;
				}
				LaunchUtils.sendMessageByinline(message, contact.getPhone(),
						UserApplication.getInstance().getActivity());
			}
		}
		if (listener != null) {
			listener.onMmsHandle(true);
		}
	}

	private static boolean isConnect(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	public static void sendEmails(Context context, String message,
			onEmailListener listener) {
		if (!isConnect(context)) {
			if (listener != null) {
				listener.onEmailHandle(false);
			}
			return;
		}
		AvanceService avanceService = UserApplication.getInstance()
				.getAvanceService();
		ContactService contactService = UserApplication.getInstance()
				.getContactService();
		if (avanceService == null || contactService == null) {
			if (listener != null) {
				listener.onEmailHandle(false);
			}
			return;
		}
		try {
			List<Contact> contacts = contactService.getAllContacts();
			if (contacts != null && contacts.size() > 0) {
				for (Contact contact : contacts) {
					if (contact.getEmail().isEmpty()) {
						continue;
					}
					LaunchUtils.sendEmailByinline(message, contact.getEmail(),
							UserApplication.getInstance().getActivity());
				}
			}
			if (listener != null) {
				listener.onEmailHandle(true);
			}
		} catch (MessagingException e) {
			e.printStackTrace();
			if (listener != null) {
				listener.onEmailHandle(false);
			}
		}

	}

	public static void sendAudioToAll() {

		AvanceService avanceService = UserApplication.getInstance()
				.getAvanceService();
		ContactService contactService = UserApplication.getInstance()
				.getContactService();
		if (avanceService == null || contactService == null) {
			return;
		}
		try {
			Avance avance = avanceService.getAvance();
			List<Contact> contacts = contactService.getAllContacts();
			if (contacts == null || contacts.size() == 0) {
				return;
			}
			for (Contact contact : contacts) {
				// LaunchUtils.sendEmailByinline(avance.getMessageText(),
				// contact.getEmail(), context);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void sendAudioToSamu() {

		AvanceService avanceService = UserApplication.getInstance()
				.getAvanceService();
		if (avanceService == null) {
			return;
		}
		try {
			Avance avance = avanceService.getAvance();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void sendAudioToPompiers() {

		AvanceService avanceService = UserApplication.getInstance()
				.getAvanceService();
		if (avanceService == null) {
			return;
		}
		try {
			Avance avance = avanceService.getAvance();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void sendAudioToPolice() {

		AvanceService avanceService = UserApplication.getInstance()
				.getAvanceService();
		if (avanceService == null) {
			return;
		}
		try {
			Avance avance = avanceService.getAvance();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void call1() {
		Context context = UserApplication.getInstance().getActivity();
		LaunchUtils.callphoneByinline(
				Preferences.getPhoneNumber1(context).number, context);
	}

	public static void call2() {
		Context context = UserApplication.getInstance().getActivity();
		LaunchUtils.callphoneByinline(
				Preferences.getPhoneNumber2(context).number, context);
	}

	public static void launchSante(Context context, String address,
			onMessageListener mmslistener, onEmailListener emaillistener) {
		String message = MessageUtils.getMessageText(NX.SANTE, address);
		sendAudioToSamu();
		sendAudioToAll();
		sendMessages(context, message, mmslistener);
		sendEmails(context, message, emaillistener);
	}

	public static void launchFeu(Context context, NX nx, String address,
			onMessageListener mmslistener, onEmailListener emaillistener) {
		String message = MessageUtils.getMessageText(nx, address);
		sendMessages(context, message, mmslistener);
		sendEmails(context, message, emaillistener);
		sendAudioToAll();
		sendAudioToPompiers();
	}

	public static void launchPolice(Context context, NX nx, String address,
			onMessageListener mmslistener, onEmailListener emaillistener) {
		String message = MessageUtils.getMessageText(nx, address);
		sendMessages(context, message, mmslistener);
		sendEmails(context, message, emaillistener);
		sendAudioToAll();
		sendAudioToPolice();
	}

	public static void launchBatterieAlert(Context context, String address,
			onMessageListener mmslistener, onEmailListener emaillistener) {
		String message = MessageUtils.getMessageText(NX.BATTERIE, address);
		sendMessages(context, message, mmslistener);
		sendEmails(context, message, emaillistener);
		sendAudioToAll();
	}

	/*********** Inactivity *******/

	public static void launchInactdivite() {
		Context context = UserApplication.getInstance().getActivity();
		AvanceService avanceService = UserApplication.getInstance()
				.getAvanceService();
		AlertService<InactiviteAlert> inactiviteService = UserApplication
				.getInstance().getInactiviteService();
		if (avanceService == null) {
			return;
		}
		InactiviteAlert alert = inactiviteService.getAlert();
		Calendar calendar = Calendar.getInstance();
		// calendar.add(Calendar.HOUR, Integer.parseInt(alert.getLalerte()));
		calendar.add(Calendar.MINUTE, alert.getDesarmocage());
		// AlertUtil.registerAlarmTime(context, AlarmQueue.INACTIVITY_ACTION,
		// calendar.getTimeInMillis());

	}

	public static void closeInactivite() {
		Context context = UserApplication.getInstance().getActivity();
		// AlertUtil.unRegisterAlarmTime(context, AlarmQueue.INACTIVITY_ACTION);
	}

}
