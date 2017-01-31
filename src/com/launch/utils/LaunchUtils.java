package com.launch.utils;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.Contacts;
import android.telephony.SmsManager;
import android.text.TextUtils;

import com.launch.email.MailInfo;

public class LaunchUtils {

	public static void sendMessageByinline(String content, String phonenumber,
			Context context) {
		if(TextUtils.isEmpty(phonenumber) ||  TextUtils.isEmpty(content)) {
			return;
		}
		SmsManager smsManager = SmsManager.getDefault();
		if (content.length() > 70) {  
		    ArrayList<String> msgs = smsManager.divideMessage(content);  
		    smsManager.sendMultipartTextMessage(phonenumber, null, msgs, null, null);  
		} else {  
			smsManager.sendTextMessage(phonenumber, null, content, null, null);  
		}  
	}

	public static void sendMessageByoutline(String content, String phonenumber,
			Context context) {
		if(TextUtils.isEmpty(content)) {
			return;
		}
		Uri uri = Uri.parse("smsto:");
		Intent sms = new Intent(Intent.ACTION_VIEW);
		sms.putExtra("sms_body", content);
//		 sms.setType("vnd.android-dir/mms-sms");
		context.startActivity(sms);
	}

	public static void sendEmailByinline(final String content,
			final String toMail, Context context) throws MessagingException {
		if(TextUtils.isEmpty(content) || TextUtils.isEmpty(toMail)) {
			return;
		}
		// new Thread() {
		//
		// @Override
		// public void run() {
		// try {
		// EmailSender sender = new EmailSender();
		// sender.setProperties("smtp.126.com", "25");
		// sender.setMessage(toMail, "EmailSender",content);
		// sender.setReceiver(new String[] { toMail});
		// sender.sendEmail("smtp.126.com", toMail, "kalen520");
		// } catch (AddressException e) {
		// e.printStackTrace();
		// } catch (MessagingException e) {
		// e.printStackTrace();
		// }
		// }
		// };

		if(TextUtils.isEmpty(toMail)) {
			return;
		}
		new Thread() {
			@Override
			public void run() {
//			
				//1.0
				//MailSender sender = new MailSender("smtp.gmail.com",
//						"loic.texier@dgmail.fr", "texier2014", false);
//				try {
//					sender.sendMail(content, content, "kalen_sytina@126.com",
//							toMail);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				//2.0
				MailInfo sm = new MailInfo("mail.kdtbeta.com");//GMAIL·þÎñÆ÷STMPµØÖ·  
		        sm.setNamePass("launcher@kdtbeta.com", "launcher2014");  
		        sm.setFrom("launcher@kdtbeta.com");  
		        sm.setTo(toMail);  
		          
		        sm.setSubject("Mail");//TITLE  
		        sm.setBody(content);//CONTENT  
		          
		        sm.setNeedAuth(true);  
		        boolean b = sm.setOut();  
		        if (b) {  
		            System.out.println("Mail send succeed!");  
		        } else {  
		            System.out.println("Mail send failed!");  
		        }  
			}
		}.start();
	}

	public static void sendEmailByoutline(String content, String toMail,
			Context context) {
		if(TextUtils.isEmpty(content) || TextUtils.isEmpty(toMail)) {
			return;
		}
		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_EMAIL, toMail);
		email.putExtra(Intent.EXTRA_TEXT, content);
		email.setType("text/plain");
		context.startActivity(email);
		// Intent intent = new Intent("com.google.android.gm.action.AUTO_SEND");
		// intent.setType("plain/text");
		// String[] reciver = new String[] { toMail };
		// String subject = "email title";
		// String body = "email body";
		// intent.putExtra(android.content.Intent.EXTRA_EMAIL, reciver);
		// intent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		// intent.putExtra(android.content.Intent.EXTRA_TEXT, body);
		// context.startActivity(intent);
		// Toast.makeText(context, "mail send successful", Toast.LENGTH_SHORT)
		// .show();
	}

	public static void openContacts(Context context) {
		Intent contact = new Intent();
		contact.setAction(Intent.ACTION_VIEW);
		contact.setData(Contacts.People.CONTENT_URI);
		context.startActivity(contact);
	}

	public static void callphoneByinline(String phonenumber, Context context) {
		Uri uri1 = Uri.parse("tel:" + phonenumber);
		Intent call1 = new Intent(Intent.ACTION_CALL, uri1);
		context.startActivity(call1);
	}
	
	

	public static void launchEmail(String content, String toMail,
			Context context) {
		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_EMAIL, toMail);
		email.putExtra(Intent.EXTRA_TEXT, content);
		email.putExtra(Intent.EXTRA_SUBJECT, content);
		email.setType("text/plain");

		PackageManager pm = context.getPackageManager();
		final List<ResolveInfo> rinfo = pm.queryIntentActivities(email, 0);
		System.out.println(rinfo.size());
		ResolveInfo findInfo = null;
		for (int i = 0; i < rinfo.size(); i++) {
			ResolveInfo info = (ResolveInfo) rinfo.get(i);
			if (info.activityInfo.name.contains("mail")
					|| info.activityInfo.name.contains("Mail")) {
				if (info.activityInfo.name.contains("Gmail")) {
					findInfo = info;
					break;

				} else {
					findInfo = info;
				}
			}
			System.out.println(info.activityInfo.packageName + " -- "
					+ info.activityInfo.name);
		}

		if(findInfo != null) {
			email.setClassName(findInfo.activityInfo.packageName,
					findInfo.activityInfo.name);
			context.startActivity(email);
		} else {
			context.startActivity(Intent.createChooser(email, "Choose Email Client"));   
		}
	}

	public static void callphoneByoutline(String phonenumber, Context context) {
		Uri uri = Uri.parse("tel:" + phonenumber);
		Intent tel = new Intent(Intent.ACTION_DIAL, uri);
		context.startActivity(tel);
	}
	
	
	
	public static Intent getIntentByKeywords(Context context, String... keyword) {
		PackageManager pm = context.getPackageManager();
		Intent launch = new Intent(Intent.ACTION_MAIN, null);
		launch.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> packages = pm.queryIntentActivities(launch,
				PackageManager.GET_ACTIVITIES);
		Intent intent = null;
		for (ResolveInfo info : packages) {
			if (info == null) {
				continue;
			}
			
			for(String args : keyword) {
				if(info.activityInfo.packageName.contains(args)) {
					intent = pm.getLaunchIntentForPackage(info.activityInfo.packageName);
					break;
				}
			}
			
			System.out.println(info.activityInfo.packageName);
		}

		return intent;
	}
}
