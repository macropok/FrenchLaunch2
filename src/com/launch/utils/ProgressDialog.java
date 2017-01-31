package com.launch.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.launch.ui.R;
import com.launch.utils.MessageUtils.NX;

public class ProgressDialog {

	private NX nx;

	private Button ok;

	private Context globalContext;

	private View view;

	private onCancelListener listener;

	private boolean handleMms, handleEmail;

	public ProgressDialog(Context gobalContext, Context context, NX nx,
			onCancelListener listener) {
		this.nx = nx;
		this.view = createView(context, listener);
		this.globalContext = gobalContext;
		this.listener = listener;
		show(gobalContext, context);
	}

	public NX getNx() {
		return nx;
	}

	private WindowManager getWindowManager(Context context) {
		return (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	}

	public void show(Context globalContext, Context context) {
		WindowManager windowManager = getWindowManager(globalContext);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		LinearLayout layout = (LinearLayout) view
				.findViewById(R.id.screendialog);
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) layout
				.getLayoutParams();

		LayoutParams smallWindowParams = new LayoutParams();
		smallWindowParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
		smallWindowParams.format = PixelFormat.RGBA_8888;
		smallWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE;
		smallWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
		smallWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
		;
		smallWindowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
		;
		// smallWindowParams.x = (int) ((context.getResources()
		// .getDisplayMetrics().widthPixels - 280 * context.getResources()
		// .getDisplayMetrics().density) / 2);
		// smallWindowParams.y = (int) ((context.getResources()
		// .getDisplayMetrics().heightPixels - 200 * context
		// .getResources().getDisplayMetrics().density) / 2);
		smallWindowParams.x = 0;
		smallWindowParams.y = 0;
		windowManager.addView(view, smallWindowParams);
	}

	private View createView(Context context, final onCancelListener listener) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.progress_layout, null);
		TextView title = (TextView) view.findViewById(R.id.title);
		ok = (Button) view.findViewById(R.id.cancel);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (listener != null) {
					listener.onCancel(nx, false);
				}
			}
		});
		title.setText("ALERTE " + nx.getName());
		return view;
	}

	public interface onCancelListener {
		public void onCancel(NX nx, boolean dismiss);
	}

	public interface onMessageListener {
		public void onMmsHandle(boolean issuccess);
	}

	public interface onEmailListener {
		public void onEmailHandle(boolean issuccess);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				view.findViewById(R.id.sms_finish).setVisibility(View.VISIBLE);
				view.findViewById(R.id.email_finish).setVisibility(View.VISIBLE);
				view.findViewById(R.id.sms_progress).setVisibility(View.GONE);
				view.findViewById(R.id.email_progress).setVisibility(View.GONE);
				ok.setText("OK");
				sendEmptyMessageDelayed(1, 5000);
			} else if (msg.what == 1) {
				dismiss();
			}
		};
	};

	public void dismiss() {
		if (view != null) {
			WindowManager windowManager = getWindowManager(globalContext);
			windowManager.removeView(view);
			view = null;
		}
		if (listener != null) {
			listener.onCancel(getNx(), true);
		}
	}

	public void setMMSStatus(boolean isSuccess) {
		handleMms = true;
		view.findViewById(R.id.sms_progress).setVisibility(View.GONE);
		if (isSuccess) {
			view.findViewById(R.id.sms_finish).setVisibility(View.VISIBLE);
		} else {
			view.findViewById(R.id.sms_fail).setVisibility(View.VISIBLE);
		}

		check();
	}

	public void setMailStatus(boolean isSuccess) {
		handleEmail = true;
		view.findViewById(R.id.email_progress).setVisibility(View.GONE);
		if (isSuccess) {
			view.findViewById(R.id.email_finish).setVisibility(View.VISIBLE);
		} else {
			view.findViewById(R.id.email_fail).setVisibility(View.VISIBLE);
		}

		check();
	}

	public boolean isOver() {
		return handleEmail && handleMms;
	}

	private void check() {
		if (handleEmail && handleMms) {
			handler.sendEmptyMessageDelayed(1, 5000);
		}
	}

	public void finish() {
		handler.sendEmptyMessageDelayed(0, 300);
		// ‘› ±√ª”√
	}

}