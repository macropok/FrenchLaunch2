package com.launch.setting;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.launch.ui.R;

public class Menu {

	private static Map<String, Integer> maps = new HashMap<String, Integer>();

	private static Map<String, Integer> titles = new HashMap<String, Integer>();

	private static Map<String, Integer> icons = new HashMap<String, Integer>();

	static {
		maps.put("0000", R.array.menu);
		maps.put("0001", R.array.menu_s1);
		maps.put("0002", R.array.menu_s2);
		maps.put("0003", R.array.menu_s3);
		maps.put("0004", R.array.menu_s4);
		// maps.put("0005", R.array.menu_s5);
		// maps.put("0054", R.array.menu_s54);

		icons.put("0000", R.array.menu_icon);
		icons.put("0004", R.array.menu_s4_icon);
		icons.put("0003", R.array.menu_s3_icon);
		icons.put("0002", R.array.menu_s2_icon);
		

		titles.put("0000", R.string.parameter);
		// titles.put("0002", R.string.title_s2);
		//
		// titles.put("0003", R.string.title_s3);
		// titles.put("0031", R.string.title_s31);
		// titles.put("0032", R.string.title_s32);
		// titles.put("0033", R.string.title_s33);
		// titles.put("0034", R.string.title_s34);
		// titles.put("0035", R.string.title_s35);
		//
		//
		// titles.put("0004", R.string.title_s4);
		// titles.put("0041", R.string.title_s41);
		// titles.put("0042", R.string.title_s42);
		// titles.put("0043", R.string.title_s43);
		// titles.put("0044", R.string.title_s44);
		//
		// titles.put("0005", R.string.title_s5);
		// titles.put("0054", R.string.title_s54);

	}

	public static int getMenuIcon(Context context, String code, int position) {
		Integer res = icons.get(code);
		if (res == null) {
			return R.drawable.setting_icon;
		}
		String[] array = context.getResources().getStringArray(res);
		if (array.length <= position) {
			return R.drawable.setting_icon;
		}
		return context.getResources().getIdentifier(array[position],
				"drawable", context.getPackageName());
	}
	
	public static int getCurrentIcon(Context context, String code) {
		if (code.equals("0000")) {
			return R.drawable.home_icon;
		}
		int c = Integer.parseInt(code);
		int parent = c / 10;
		code = String.format("%04d", parent);
		int position = c % 10 - 1;
		Integer res = icons.get(code);
		if (res == null) {
			return R.drawable.setting_icon;
		}
		String[] array = context.getResources().getStringArray(res);
		if (array.length <= position) {
			return R.drawable.setting_icon;
		}
		return context.getResources().getIdentifier(array[position],
				"drawable", context.getPackageName());
	}

	public static Fragment getFragment(String code) {
		Fragment fragement = null;
		int intCode = Integer.parseInt(code);
		if (intCode == 21) {
			fragement = new InformationFragment();
		} else if (intCode == 22) {
			fragement = new VotreAdresseFragment();
		} else if (intCode == 23) {
			fragement = new VotreAccesFragment();
		} else if (intCode == 42) {
			fragement = new InactiviteAlertFragment();
		} else if (intCode == 41) {
			fragement = new ChuteAlertFragment();
		} else if(intCode == 44) {
			fragement = new ConfortAlertFragment();
		} else if (intCode == 43) {
			fragement = new BatterieAlertFragment();
		} else if (intCode == 5) {
			fragement = new AvanceFragment();
		} else if(intCode >= 31 && intCode <= 35) {
			fragement = new ContactListFragment(code);
		} else if(intCode == 11) {
			fragement = new VoulezAlertFragment();
		} else if(intCode == 36) {
			fragement = new FavorFragment(1);
		} else if(intCode == 37) {
			fragement = new FavorFragment(2);
		}
		return fragement;
	}

	public static String getTitle(Context context, String code) {
		if (code.equals("0000")) {
			return context.getString(R.string.parameter);
		} else if(code.equals("0011")) {
			return "Voulez activer";
		} else if(code.equals("0036")) {
			return "Favoris 1";
		} else if(code.equals("0037")) {
			return "Favoris 2";
		}
		int intCode = Integer.parseInt(code);
		int preCode = intCode / 10;
		int index = intCode % 10;
		String pre = String.format("%04d", preCode);
		String[] array = context.getResources().getStringArray(getArray(pre));
		return array[index - 1];
		// return titles.get(pre);
	}

	public static boolean isExist(String code) {
		return maps.keySet().contains(code);
	}

	public static int getArray(String code) {
		return maps.get(code);
	}

	public static String getNextMenuCode(String code, int position) {
		int number = Integer.parseInt(code);
		number = number * 10 + position + 1;
		return String.format("%04d", number);
	}

}
