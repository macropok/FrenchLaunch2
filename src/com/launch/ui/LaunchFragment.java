package com.launch.ui;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.launch.setting.ContactAddFragment;
import com.launch.setting.Menu;
import com.launch.setting.MenuFragment;
import com.launch.setting.MessageFragment;
import com.launch.setting.onSettingCallback;
import com.launch.sqlite.Contact.ContactType;
import com.launch.utils.ClickQueue;
import com.launch.utils.SosDialog;
import com.launch.utils.SosFunction;
import com.launch.utils.ThirdLaunch;

public class LaunchFragment extends Fragment implements OnClickListener,
		OnTouchListener, onSettingCallback {

	public static LaunchFragment fragment;

	private View containerView;
	
	private SosFunction function;
	
	private SosDialog dialog;

	public SosDialog getSOSDialog() {
		return dialog;
	}

	public static LaunchFragment newInstance() {
		if (fragment == null) {
			fragment = new LaunchFragment();
			Bundle bundle = new Bundle();
			bundle.putString("Title", "ACCUEIL SIDONE");
			bundle.putInt("bgColor", 0xFFFB4B55);
			bundle.putInt("ImageId", R.drawable.home_icon);
			fragment.setArguments(bundle);
		}
		return fragment;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((PageActivity)getActivity()).setIcon(getArguments().getInt("bgColor"), getArguments().getInt("ImageId"));
		((PageActivity)getActivity()).setTextSimple(getArguments().getString("Title"));
		containerView = inflater.inflate(R.layout.launch_ui, container, false);
		settingViewOnTouch();
		//Toast.makeText(getActivity(), "launch fragment start", Toast.LENGTH_LONG).show();
		return containerView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	private void settingViewOnTouch() {
		containerView.findViewById(R.id.telephone).setOnTouchListener(this);
		containerView.findViewById(R.id.contact).setOnTouchListener(this);
		containerView.findViewById(R.id.sms).setOnTouchListener(this);
		containerView.findViewById(R.id.email).setOnTouchListener(this);
		containerView.findViewById(R.id.camera).setOnTouchListener(this);
		containerView.findViewById(R.id.galerie).setOnTouchListener(this);
		containerView.findViewById(R.id.utilitaires).setOnTouchListener(this);
		containerView.findViewById(R.id.internet).setOnTouchListener(this);
		containerView.findViewById(R.id.facebook).setOnTouchListener(this);
		containerView.findViewById(R.id.maps).setOnTouchListener(this);

		containerView.findViewById(R.id.nothing).setOnTouchListener(this);
		containerView.findViewById(R.id.app).setOnTouchListener(this);
		containerView.findViewById(R.id.alert).setOnTouchListener(this);
		//containerView.findViewById(R.id.alert).setOnClickListener(this);
		
//		containerView.findViewById(R.id.sos).setOnTouchListener(this);
		
		
		containerView.findViewById(R.id.setting).setOnTouchListener(this);
		//containerView.findViewById(R.id.setting).setOnClickListener(this);

		BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = true;
//		Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.sos_icon, null);
//		int height = (int) (bitmap.getHeight() + getResources().getDisplayMetrics().density * 5);
//		
//		function = new SosFunction(getActivity(), containerView.findViewById(R.id.arrow), containerView.findViewById(R.id.sos_block), height);
		//if(dialog==null)
		
		dialog = SosDialog.newInstance(UserApplication.getInstance().getGlobalContext(getActivity()), getActivity()); //new SosDialog(UserApplication.getInstance().getGlobalContext(getActivity()), getActivity());
		dialog.hideArrow();
		if(dialog.getDesiredHeight()!=0 && dialog.getDesiredHeight()!=LayoutParams.WRAP_CONTENT)
		{
			((PageActivity)getActivity()).getWindow().getDecorView().findViewById(R.id.contaier).setPadding(0, 0, 0, dialog.getDesiredHeight());
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (/*v.getId() == R.id.setting || v.getId() == R.id.alert ||*/ R.id.back == v.getId()) {
			return false;
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			ClickQueue.getInstance().executeStart();
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			ClickQueue.getInstance().executeClick(v.getId());
		} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
			ClickQueue.getInstance().cancelExecute();
		}
		System.out.println(event.getAction());
		return false;
	}

	private void backSetting() {
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (getActivity().getCurrentFocus() != null) {
			imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		getChildFragmentManager().popBackStack();
	}

	private void showSetting(boolean visible) {
		// containerView.findViewById(R.id.content).setVisibility(
		// visible ? View.INVISIBLE : View.VISIBLE);
		containerView.findViewById(R.id.setting_block).setVisibility(
				visible ? View.VISIBLE : View.INVISIBLE);
	}

	private void backHome() {
		Animation outAnim = AnimationUtils.loadAnimation(getActivity(),
				R.anim.out_back_anim);
		// containerView.findViewById(R.id.content).setAnimation(outAnim);
		outAnim.start();

		Animation enterAnim = AnimationUtils.loadAnimation(getActivity(),
				R.anim.enter_back_anim);
		containerView.findViewById(R.id.setting_block).setAnimation(enterAnim);

		enterAnim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				showSetting(false);
			}
		});

		enterAnim.start();

	}

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

	}

	public boolean popFragment() {
		if (getChildFragmentManager().getBackStackEntryCount() == 0) {
			return false;
		}
		if (getChildFragmentManager().getBackStackEntryCount() == 1) {
			showSetting(false);
			((PageActivity)getActivity()).setIcon(getArguments().getInt("bgColor"), getArguments().getInt("ImageId"));
			((PageActivity)getActivity()).setTextSimple(getArguments().getString("Title"));
			//return true;
		}
		getChildFragmentManager().popBackStack();
		return true;
	}

	public void launchFragment(String code) {
		Fragment fragment = null;
		int intCode = Integer.parseInt(code);
		if (intCode >= 12 && intCode <= 15) {
			switch (intCode) {
			case 12:
				ThirdLaunch.launchWifiSetting(getActivity());
				break;
			case 13:
				ThirdLaunch.launchBluetoothSetting(getActivity());
				break;
			case 14:
				ThirdLaunch.launchAudioSetting(getActivity());
				break;
			case 15:
				ThirdLaunch.launchAndroidSetting(getActivity());
				break;
			}
		} else if (intCode >= 311 && intCode <= 351) {
			int index = intCode % 100 / 10 - 1;
			fragment = new ContactAddFragment(ContactType.values()[index]);
			toggleSetting(getString(R.string.add_contact), code);
		} else if (TextUtils.equals(code, "0051")) {
			fragment = new MessageFragment();
			toggleSetting(getString(R.string.messages), code);
		} else if (!Menu.isExist(code)) {
			fragment = Menu.getFragment(code);
			toggleSetting(Menu.getTitle(getActivity(), code), code);
		} else {
			fragment = MenuFragment.newInstance(code, this);
		}
		if (fragment == null) {
			return;
		}
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		ft.setCustomAnimations(R.anim.enter_anim, R.anim.out_anim,
				R.anim.enter_back_anim, R.anim.out_back_anim);
		ft.replace(R.id.setting_container, fragment);
		ft.addToBackStack(code);
		ft.commit();

	}

	public void onBackFragment() {
		if (getChildFragmentManager().getBackStackEntryCount() == 1) {
			showSetting(false);
		} else {
			backSetting();
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.back:
			// if (getChildFragmentManager().getBackStackEntryCount() == 1) {
			// showSetting(false);
			// } else {
			// backSetting();
			// }
			break;
		case R.id.alert:
			launchSetting("0004");
			break;
		case R.id.setting:
			launchSetting("0000");
			break;
		default:
			ClickQueue.getInstance().executeClick(v.getId());
			break;
		}
	}

	public void launchFragment(String code, Fragment fragment) {
		if (fragment == null) {
			return;
		}
		int intCode = Integer.parseInt(code);
		if (intCode >= 311 && intCode <= 351) {
			toggleSetting(getString(R.string.add_contact), code);
		}
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		ft.setCustomAnimations(R.anim.enter_anim, R.anim.out_anim,
				R.anim.enter_back_anim, R.anim.out_back_anim);
		ft.replace(R.id.setting_container, fragment);
		ft.addToBackStack(code);
		ft.commit();

	}

	public void launchSetting(String code) {
		FragmentManager manager = getChildFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		ft.replace(R.id.setting_container, MenuFragment.newInstance(code, this));
		ft.addToBackStack(code);
		ft.commit();

		Animation outAnim = AnimationUtils.loadAnimation(getActivity(),
				R.anim.out_anim);
		// containerView.findViewById(R.id.content).setAnimation(outAnim);
		outAnim.start();

		Animation enterAnim = AnimationUtils.loadAnimation(getActivity(),
				R.anim.enter_anim);
		containerView.findViewById(R.id.setting_block).setAnimation(enterAnim);
		enterAnim.start();

		enterAnim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				showSetting(true);
			}
		});
	}

	@Override
	public void toggleSetting(String title, String code) {
		if (title == null) {
			return;
		}
		UserApplication.getInstance().getActivity().setText(title, code);
	}
}
