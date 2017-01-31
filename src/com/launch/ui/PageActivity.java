package com.launch.ui;

import java.util.List;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.internal.la;
import com.launch.service.ChuteService;
import com.launch.setting.Menu;
import com.launch.sqlite.P;
import com.launch.utils.AlarmQueue;
import com.launch.utils.ContactUtils;
import com.launch.utils.GPSUtil;
import com.launch.utils.SosDialog;
import com.launch.utils.SosFunction;

public class PageActivity extends FragmentActivity implements OnClickListener{

	public static final int PICK_CONTACT = 100;
	public static final int SMS_PICK_FAVORIS_CONTACT = 101;
	public static final int SMS_PICK_COMMUNATE_CONTACT = 102;
	public static final int SMS_PICK_MY_CONTACT = 103;
	public static final int SMS_CUSTOM_CONTACT_PICKER = 104;
	public static final int EDIT_CONTACT = 105;
	public static final int VIEW_CONTACT = 105;
	
	private TextView settingTitle;
	
	private ImageButton icon;
	
	//private LaunchFragment launchFragment;
	
	private OnBackStackChangedListener onBackStackChangedListener = new OnBackStackChangedListener() {
		
		public void onBackStackChanged() {
			// TODO Auto-generated method stub
		    Fragment frag = getVisibleFragment();
		    if(frag!=null)
		    {
		    	try{
		    		//setTextSimple(frag.getArguments().getString("Title"));
		    		setIcon(frag.getArguments().getInt("bgColor"), frag.getArguments().getInt("ImageId"));
		    	}catch(Exception e){}
		    }
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pages_layout);

		UserApplication application = (UserApplication) getApplication();
		application.setAcvitity(this);

		GPSUtil.openGPS(this);
		if (!GPSUtil.isOpen(this)) {
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			if (!locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				Intent intent = new Intent();
				intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				try {
					startActivity(intent);
				} catch (ActivityNotFoundException ex) {

					// The Android SDK doc says that the location settings
					// activity
					// may not be found. In that case show the general settings.

					// General settings activity
					intent.setAction(Settings.ACTION_SETTINGS);
					try {
						startActivity(intent);
					} catch (Exception e) {
					}
				}
			}
		}
		
		Intent intent = new Intent(this, ChuteService.class);
		intent.putExtra("type", ChuteService.ALL_TYPE);
		startService(intent);
		// ContactUtils.initContacts(this, getContentResolver());
		ContactUtils.createCommunauteGroups();
		//Toast.makeText(this, String.valueOf(ContactUtils.readContactsGroup("favoris").get(0)),Toast.LENGTH_SHORT).show();
		if(savedInstanceState==null)
			initFragment();
		else{
			int aaa=55;
			int b=aaa;
		}
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//Toast.makeText(this, "destroy", Toast.LENGTH_LONG).show();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		//Toast.makeText(this, "pause", Toast.LENGTH_LONG).show();
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		//Toast.makeText(this, "stop", Toast.LENGTH_LONG).show();
		
		getSupportFragmentManager().removeOnBackStackChangedListener(onBackStackChangedListener);
		SosDialog sosDlg = SosDialog.newInstance(null, null);
		if(sosDlg!=null && !sosDlg.isArrowShown())
		{
			sosDlg.showArrow();
		}
		//getSupportFragmentManager().saveFragmentInstanceState(LaunchFragment.newInstance());
		//getSupportFragmentManager().beginTransaction().remove(LaunchFragment.newInstance()).commit();
		super.onStop();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		
		//Toast.makeText(this, "resume", Toast.LENGTH_LONG).show();
		
		SosDialog sosDlg = SosDialog.newInstance(null, null);
		if(sosDlg!=null && sosDlg.isArrowShown())
		{
			sosDlg.hideArrow();
		}
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		//Toast.makeText(this, "start", Toast.LENGTH_LONG).show();
		
		super.onStart();
	}

	public void setText(String text, String code) {
		settingTitle.setText(text);
		int id = Menu.getCurrentIcon(this, code);
		icon.setImageResource(id);
		System.out.println(code);
	}
	public void setTextSimple(String text) {
		settingTitle.setText(text);
	}
	public void setIcon(int bgColor, int resId) {
		icon.setBackgroundColor(bgColor);
		icon.setImageDrawable(getResources().getDrawable(resId));
		//icon.setImageResource(resId);
		//System.out.println(code);
	}

	private void initFragment() {
		//Toast.makeText(this, "start", Toast.LENGTH_LONG).show();
		//LaunchFragment.fragment = null;
		FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
		if(LaunchFragment.newInstance().getActivity()==null)
		{
			//Toast.makeText(this, "null activity", Toast.LENGTH_LONG).show();
			
		}else
		{
			//Toast.makeText(this, String.valueOf(getSupportFragmentManager().getFragments().size()), Toast.LENGTH_LONG).show();
			LaunchFragment.newInstance().getActivity().finish();
			LaunchFragment.fragment = null;
			//trans.show(LaunchFragment.newInstance());
		}
		trans.replace(R.id.contaier, LaunchFragment.newInstance());
		trans.commit();
		
		UserApplication.getInstance().setLaunchFragment(LaunchFragment.newInstance());
		
		settingTitle = (TextView) findViewById(R.id.title);
		findViewById(R.id.back).setOnClickListener(this);
		icon = (ImageButton) findViewById(R.id.icon);
		
		getSupportFragmentManager().addOnBackStackChangedListener(onBackStackChangedListener);
	}
	
	public Fragment getVisibleFragment(){
	    FragmentManager fragmentManager = getSupportFragmentManager();
	    List<Fragment> fragments = fragmentManager.getFragments();
	    for(Fragment fragment : fragments){
	        if(fragment != null && fragment.isVisible())
	            return fragment;
	    }
	    return null;
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			Uri contactData = data.getData();
			switch (reqCode) {
				case 100:
			
					
					Cursor c = getContentResolver().query(contactData, null, null, null, null);
					ContentResolver cr = getContentResolver();
					if (c.moveToFirst()) {
						String name = c
								.getString(c
										.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
						Intent intent = new Intent();
						intent.putExtra("name", name);
	
						String ContactId = c.getString(c
								.getColumnIndex(ContactsContract.Contacts._ID));
						Cursor phoneCursor = cr.query(
								ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
								null,
								ContactsContract.CommonDataKinds.Phone.CONTACT_ID
										+ "=" + ContactId, null, null);
						String phone = null;
						while (phoneCursor.moveToNext()) {
							phone = phoneCursor
									.getString(phoneCursor
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							if (!TextUtils.isEmpty(phone)) {
								break;
							}
						}
						intent.setAction("contact.add");
						intent.putExtra("phone", phone);
						intent.putExtra("contactId",Long.parseLong(ContactId));
						Cursor emails = cr.query(
								ContactsContract.CommonDataKinds.Email.CONTENT_URI,
								null,
								ContactsContract.CommonDataKinds.Email.CONTACT_ID
										+ "=" + ContactId, null, null);
						int emailIndex = 0;
						if (emails.getCount() > 0) {
							emailIndex = emails
									.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
						}
						String email = null;
						while (emails.moveToNext()) {
							email = emails.getString(emailIndex);
							if (!TextUtils.isEmpty(email)) {
								break;
							}
						}
						intent.putExtra("email", email);
						PageActivity.this.sendBroadcast(intent);
						
					}
				
	
					break;
			case EDIT_CONTACT:
				data.setAction("contact.edit");
				if(data.getData()!=null)
				{
					data.putExtra("uri", data.getData().toString());
					data.setData(null);
				}
				PageActivity.this.sendBroadcast(data);
				break;
			case SMS_CUSTOM_CONTACT_PICKER:
				if (resultCode == Activity.RESULT_OK) {
					data.setAction("contact.add");
					PageActivity.this.sendBroadcast(data);
				}
				break;
			case 110:
				AlarmQueue.getInstance().finishAlarm(data.getAction());
				break;
			}
		}
	}

	@Override
	public void onBackPressed() {
		UserApplication application = (UserApplication) getApplication();
		if (!application.popFragment() && getVisibleFragment()!=LaunchFragment.newInstance()) 
		{
			super.onBackPressed();
		}
	}

	@Override
	public void onClick(View v) {
		//LaunchFragment.newInstance().onBackFragment();
		
		if(v.getId()==R.id.btnBack)
		{
			if(getSupportFragmentManager().getBackStackEntryCount() >0)
				getSupportFragmentManager().popBackStack();
		}else if(v.getId()==R.id.back)
		{
			/*while(getSupportFragmentManager().getBackStackEntryCount() >0)
			{
				getSupportFragmentManager().popBackStack();
			}*/
			getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			
			//UserApplication.getInstance().getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contaier, LaunchFragment.newInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
		}
	}

}
