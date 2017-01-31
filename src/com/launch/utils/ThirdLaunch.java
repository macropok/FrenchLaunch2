package com.launch.utils;

import java.util.List;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings.Global;
import android.widget.Toast;

import com.launch.ui.AppHomeActivity;
import com.launch.ui.CustomContactPicker;
import com.launch.ui.DialActivity;
import com.launch.ui.PageActivity;

public class ThirdLaunch {

	public static void launchMap(Context context) {
		Uri uri = Uri.parse("geo://");
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		if (isIntentAvailable(context, it)) {
			context.startActivity(Intent.createChooser(it, "Choose Map APP"));
		}

	}

	public static void launchApps(Context context) {
		Intent intent = new Intent(context, AppHomeActivity.class);
		context.startActivity(intent);
		
	}
	
	public static void launchAddNewContact(Context context) {
		 Intent intent = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
		 context.startActivity(intent);
	}
	
	public static void launchCustomContactPicker(Context context, String group, String action, int reqCode) {
		Intent intent = new Intent(context, CustomContactPicker.class);
		intent.putExtra("group", group);
		intent.putExtra("dest_action", action);
		((PageActivity)context).startActivityForResult(intent, reqCode);
	}
	
	public static void launchPickFavorisContact(Context context)
	{
		
		Uri uri = Uri.parse("content:/contacts/groups/system_id/"+Contacts.Groups.GROUP_MY_CONTACTS+"/members");
		final Cursor c = ((PageActivity)context).managedQuery(Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_GROUP_URI, "communate"), null, null, null, null);
		//c = context.getContentResolver().query(Contacts.CONTENT_URI, null, null, null, null);
		//Toast.makeText(context,String.valueOf(c.getCount()), Toast.LENGTH_LONG);
		
		 Intent intent = new Intent(Intent.ACTION_PICK, Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_GROUP_URI, "favoris"));
		 //intent.setType(android.provider.ContactsContract.Groups.CONTENT_TYPE);
		 ((PageActivity)context).startActivityForResult(intent, 1); 
		
	}
	public static void launchPickCommunateContact(Context context)
	{
		
		Uri uri = Uri.parse("content://contacts/groups/name/" +Uri.encode("communate") +   "/members");
		 Intent intent = new Intent(Intent.ACTION_PICK, Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_GROUP_URI, "communate"));
				             //another display style:
		 //intent.setType(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
		 ((PageActivity)context).startActivityForResult(intent, 1); 
	}
	public static void launchPickContact(Context context)
	{
		 Intent intent = new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI/*Uri.parse("content://contacts/people")*/);
				             //another display style:
		 ((PageActivity)context).startActivityForResult(intent, PageActivity.PICK_CONTACT);
		 
	}
	
	public static void launchViewFavorisContacts(Context context)
	{
		 Intent intent = new Intent(Intent.ACTION_VIEW,ContactsContract.Contacts.CONTENT_URI);
		 context.startActivity(intent);
		 
	}
	
	public static void launchViewCommunauteContacts(Context context)
	{
		 Intent intent = new Intent(Intent.ACTION_VIEW,ContactsContract.Contacts.CONTENT_URI);
		 context.startActivity(intent);
		 
	}
	
	public static void launchViewAllContacts(Context context)
	{
		 Intent intent = new Intent(Intent.ACTION_VIEW,ContactsContract.Contacts.CONTENT_URI);
		 context.startActivity(intent);
		 
	}
	
	public static void launchEditFavorisContacts(Context context)
	{
		 Intent intent = new Intent(Intent.ACTION_VIEW,ContactsContract.Contacts.CONTENT_URI);
		 context.startActivity(intent);
		 
	}
	
	public static void launchEditCommunauteContacts(Context context)
	{
		 Intent intent = new Intent(Intent.ACTION_VIEW,ContactsContract.Contacts.CONTENT_URI);
		 context.startActivity(intent);
		 
	}
	
	public static void launchEditAllContacts(Context context)
	{
		 Intent intent = new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
		 //intent.setDataAndType(ContactsContract.Contacts.CONTENT_URI, ContactsContract.Contacts.CONTENT_ITEM_TYPE);
		 ((PageActivity)context).startActivityForResult(intent, PageActivity.EDIT_CONTACT);
		 
	}
	
	public static void launchDialer(Context context) {
		Intent intent = new Intent(context, DialActivity.class);
		context.startActivity(intent);
		
	}

	public static void launchWifiSetting(Context context) {
		Intent intent = new Intent(
				android.provider.Settings.ACTION_WIFI_SETTINGS);
		context.startActivity(intent);
	}

	
	public static void launchBluetoothSetting(Context context) {
		Intent intent = new Intent(
				android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
		context.startActivity(intent);
	}

	public static void launchAudioSetting(Context context) {
		Intent intent = new Intent(
				android.provider.Settings.ACTION_SOUND_SETTINGS);
		context.startActivity(intent);
	}

	public static void launchAndroidSetting(Context context) {
		Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
		context.startActivity(intent);
	}

	public static void launchMMS(Context context) {
		Intent intent = LaunchUtils.getIntentByKeywords(context,
				"com.android.mms", "mms");
		if (intent != null) {
			context.startActivity(intent);
		}
	}
	public static void launchCalculator(Context context) {
		Intent intent = LaunchUtils.getIntentByKeywords(context,
				"com.android.calculator2", "calcul");
		if (intent != null) {
			context.startActivity(intent);
		}
	}
	
	public static void launchNewSMS(Context context, String phone) {
		Uri sms_uri = Uri.parse("smsto:"+phone); 
        Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri); 
        sms_intent.putExtra("sms_body", ""); 
        context.startActivity(sms_intent); 
	}
	public static void launchCalendar(Context context) {
		Intent intent = new Intent();
		intent.setComponent(new ComponentName("com.android.calendar",
				"com.android.calendar.LaunchActivity"));
		if (isIntentAvailable(context, intent)) {
			context.startActivity(intent);
		}
	}

	public static void launchAlarm(Context context) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.SET_ALARM");
		context.startActivity(Intent.createChooser(intent, "Choose Alarm APP"));
	}

	public static void launchSocail(Context context) {
		// Intent intent=new Intent(android.content.Intent.ACTION_SEND);
		// intent.setType("text/plain");
		// if (isIntentAvailable(context, it)) {
		// context.startActivity(intent);
		// }
		Intent intent = LaunchUtils.getIntentByKeywords(context,
				"com.skype.rover", "skype");
		if (intent != null) {
			context.startActivity(intent);
		}
	}

	public static void launchCamera(Context context) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
		if (isIntentAvailable(context, intent)) {
			context.startActivity(Intent.createChooser(intent,
					"Choose Camera APP"));
		}
	}

	public static void launchInternet(Context context, String url) {
		Uri content_url = Uri.parse("http://"+url);
		Intent internet = new Intent(Intent.ACTION_VIEW);
		internet.setData(content_url);
		context.startActivity(Intent.createChooser(internet, ""));
	}
	public static void launchSearch(Context context, String keywords) {
		//Uri content_url = Uri.parse("http://"+keywords);
		Intent search = new Intent(Intent.ACTION_WEB_SEARCH); 
		search.putExtra(SearchManager.QUERY, keywords);
		context.startActivity(search);
		//context.startActivity(Intent.createChooser(internet, ""));
	}
	public static void launchBookmarks(Context context) {
		/*Uri content_url = Uri.parse("chrome://bookmarks");
		Intent internet = new Intent(Intent.ACTION_VIEW);
		internet.setData(content_url);
		context.startActivity(Intent.createChooser(internet, ""));*/
		
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("chrome://bookmarks/"));
		intent.setComponent(new ComponentName("com.android.chrome","com.google.android.apps.chrome.bookmark.ManageBookmarkActivity"));
		if (isIntentAvailable(context, intent)) 
		{
			context.startActivity(intent);
		}
		
		/*Intent intent = LaunchUtils.getIntentByKeywords(context,"com.google.android.apps", "bookmark");
		if (intent != null) {
			context.startActivity(intent);
		}*/
		
	}

	public static void launchAppStore(Context context) {
		 Intent intent = new Intent(Intent.ACTION_MAIN);
		 intent.addCategory(Intent.CATEGORY_APP_MARKET);
		 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		 context.startActivity(intent);
//		String str = "market://details?id=" + context.getPackageName();
//		Intent localIntent = new Intent("android.intent.action.VIEW");
//		localIntent.setData(Uri.parse(str));
//		context.startActivity(localIntent);
	}
	
	public static void launchFacebook(Context context) {
		
		Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
		if(isIntentAvailable(context, intent)) {
			context.startActivity(intent);
		}
	}
	
	public static void launchPhoto(Context context) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setType("image/*");
		context.startActivity(intent);
	}

	public static void launchEmail(Context context) {

		// Uri uri = Uri.parse("mailto:xxx@abc.com");
		//
		// Intent it = new Intent(Intent.ACTION_SENDTO, uri);
		//
		// context.startActivity(it);

		Intent intent = LaunchUtils.getIntentByKeywords(context,
				"com.android.email", "email");
		if (intent != null) {
			context.startActivity(intent);
		}
	}
	
	public static void launchNotes(Context context) {

		Intent intent = LaunchUtils.getIntentByKeywords(context,
				"com.android.notes", "note");
		if (intent != null) {
			context.startActivity(intent);
		}
	}
	
	public static void launchTorch(Context context) {

		Intent intent = LaunchUtils.getIntentByKeywords(context,
				"com.android.torch", "torch");
		if (intent != null) {
			context.startActivity(intent);
		}
	}
	
	public static void launchWeather(Context context) {

		Intent intent = LaunchUtils.getIntentByKeywords(context,
				"com.android.weather", "weather");
		if (intent != null) {
			context.startActivity(intent);
		}
	}

	public static void launchTimeApp(Context context) {
		PackageManager pm = context.getPackageManager();
		Intent launch = new Intent(Intent.ACTION_MAIN, null);
		launch.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> packages = pm.queryIntentActivities(launch,
				PackageManager.GET_ACTIVITIES);
		Intent intent = new Intent();
		boolean isExsit = false;
		for (ResolveInfo info : packages) {
			if (info == null) {
				continue;
			}
			System.out.println(info.activityInfo.packageName);
			// if (info.loadLabel(pm).equals("΢��")) {
			// System.out.println("---");
			// System.out.println(info.activityInfo.packageName);
			// }

			if (info.activityInfo.packageName.contains("clock")) {
				isExsit = true;
				System.out.println(info.activityInfo.packageName);
				intent.setClassName(info.activityInfo.packageName,
						info.activityInfo.name);
				break;
			} else if (info.activityInfo.packageName.contains("alarm")
					|| info.activityInfo.packageName.contains("Alarm")) {
				isExsit = true;
				intent.setClassName(info.activityInfo.packageName,
						info.activityInfo.name);
			} else if (info.activityInfo.packageName.contains("alarm")
					|| info.activityInfo.packageName.contains("Alarm")) {
				isExsit = true;
				intent.setClassName(info.activityInfo.packageName,
						info.activityInfo.name);
			}
		}
		if (isExsit) {
			context.startActivity(intent);
		}

	}

	/**
	 * �жϵ�ǰIntent�Ƿ���ڶ�Ӧ��Ŀ��
	 * 
	 * @param context
	 * @param intent
	 * @return �Ƿ���Ч
	 */
	public static boolean isIntentAvailable(Context context, Intent intent) {
		final PackageManager packageManager = context.getPackageManager();
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

}
