package com.launch.service;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;


public class ScreenDialogUtils {
	
	private WindowManager mWindowManager;
	
	private View flashControlView;
	
	private LayoutParams smallWindowParams;
	
	private static ScreenDialogUtils utils;
	
	private ScreenDialogUtils(){
		
	}
	
	public static ScreenDialogUtils getInstance() {
		if(utils == null) {
			utils = new ScreenDialogUtils();
		}
		return utils;
	}
	
	public interface FlashContralInterface {
		public void onConfirm();
		
		public void onCancel();
	};
	
	/**
	 * 
	 * @param context
	 *            必须为应用程序的Context.
	 * @return WindowManager的实例，用于控制在屏幕上添加或移除悬浮窗。
	 */
	private WindowManager getWindowManager(Context context) {
		if (mWindowManager == null) {
			mWindowManager = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
		}
		return mWindowManager;
	}
	
	
	/**
	 * 判断当前界面是否是桌面
	 */
	private static boolean isHome(Context context) {
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
		return getHomes(context).contains(rti.get(0).topActivity.getPackageName());
	}

	/**
	 * 获得属于桌面的应用的应用包名称
	 * 
	 * @return 返回包含所有包名的字符串列表
	 */
	private static List<String> getHomes(Context context) {
		List<String> names = new ArrayList<String>();
		PackageManager packageManager = context.getPackageManager();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		for (ResolveInfo ri : resolveInfo) {
			names.add(ri.activityInfo.packageName);
		}
		return names;
	}
	
	public static String getGlobalPackage(Context context) {
		List<String> homes = getHomes(context);
		if(homes.size() > 0) {
			return homes.get(0);
		}
		return context.getPackageName(); 
	}
	
	
	
}
