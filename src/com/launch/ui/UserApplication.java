package com.launch.ui;

import java.sql.SQLException;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.support.v4.app.Fragment;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.launch.service.ScreenDialogUtils;
import com.launch.sqlite.AlertService;
import com.launch.sqlite.AvanceService;
import com.launch.sqlite.BatterieAlert;
import com.launch.sqlite.ChuteAlert;
import com.launch.sqlite.ConfortAlert;
import com.launch.sqlite.ContactService;
import com.launch.sqlite.DatabaseHelper;
import com.launch.sqlite.InactiviteAlert;

public class UserApplication extends Application {

	private PageActivity mActivity;
	
	private static UserApplication application;
	
	private LaunchFragment threeFragment;
	
	private AlertService<BatterieAlert> batterieService;
	private AlertService<ChuteAlert> chuteService;
	private AlertService<ConfortAlert> confortService;
	private AvanceService avanceService;

	private ContactService contactService;
	private DatabaseHelper databaseHelper = null;
	private AlertService<InactiviteAlert> inactiviteService;

	
	public static UserApplication getInstance() {
		return application;
	}
	
	public void setLaunchFragment(LaunchFragment _fragment) {
		threeFragment = _fragment;
	}
	
	public void onDestory() {
		confortService = null;
		contactService = null;
		inactiviteService  = null;
		batterieService = null;
		avanceService = null;
		chuteService = null;
	}
	
	private Location currentLocation;
	
	public void setCurrentLocation(Location location) {
		currentLocation = location;
	}
	
	public Location getCurrentLocation() {
		return currentLocation;
	}
	
	
	public UserApplication() {
		application = this;
	}
	
	public ContactService getContactService() {
		if(contactService == null) 
		{
			try {
				contactService = getHelper().getContactService();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return contactService;
	}
	
	public AvanceService getAvanceService() {
		if(avanceService == null) 
		{
			try {
				avanceService = getHelper().getAvanceService();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return avanceService;
	}
	
	public AlertService<InactiviteAlert> getInactiviteService() {
		if(inactiviteService == null) 
		{
			try {
				inactiviteService = (AlertService<InactiviteAlert>) getHelper().getAlertService(InactiviteAlert.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return inactiviteService;
	}

	public AlertService<ChuteAlert> getChuteService() {
		if(chuteService == null) 
		{
			try {
				chuteService = (AlertService<ChuteAlert>) getHelper().getAlertService(ChuteAlert.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return chuteService;
	}
	
	public AlertService<BatterieAlert> getBatterieService() {
		if(batterieService == null) 
		{
			try {
				batterieService = (AlertService<BatterieAlert>) getHelper().getAlertService(BatterieAlert.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return batterieService;
	}
	public AlertService<ConfortAlert> getConfortService() {
		if(confortService == null) 
		{
			try {
				confortService = (AlertService<ConfortAlert>) getHelper().getAlertService(ConfortAlert.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return confortService;
	}
	
	public void setAcvitity(PageActivity _activity) {
		this.mActivity = _activity;
	}
	
	public boolean popFragment() {
		LaunchFragment fragments = threeFragment;
		if(fragments != null) {
			return fragments.popFragment();
		}
		return false;
	}
	
	public PageActivity getActivity() {
		return mActivity;
	}
	
	public Context getGlobalContext(Context context) {
		try {
			return createPackageContext(
					ScreenDialogUtils.getGlobalPackage(context),
					Context.CONTEXT_INCLUDE_CODE
							| Context.CONTEXT_IGNORE_SECURITY);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
	}

	public DatabaseHelper getHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(this,
					DatabaseHelper.class);
		}
		return databaseHelper;
	}

	
	public void showFragment(String code) {
		LaunchFragment fragments = threeFragment;
		if(fragments != null) {
			fragments.launchFragment(code);
		}
	}
	
	public void showFragment(String code, Fragment fragment) {
		LaunchFragment fragments = threeFragment;
		if(fragments != null) {
			fragments.launchFragment(code, fragment);
		}
	}
	
	
}
