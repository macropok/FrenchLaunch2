package com.launch.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;

import com.launch.ui.PageActivity;
import com.launch.ui.R;
import com.launch.ui.UserApplication;

public class SosDialog implements OnTouchListener , OnClickListener{

	private static SosDialog singleton;
	
	private Context mContext;

	private View arrow;

	private boolean isNorth;

	private View view;

	private View arrowBlock;

	private int oldY;

	private Context globalContext;

	private View sos;
	
	public int totalHeight;
	
	private int desiredHeight;
	
	private int arrowHeight;
	
	private int sosIconHeight;
	
	private int heightDelta;
	
	private boolean moving;
	
	LayoutParams smallWindowParams;
	
	/*public SosDialog(Context gobalContext, Context context) {
		this.desiredHeight = 0;
		this.heightDelta = 0;
		this.mContext = context;
		this.view = createView();
		this.globalContext = gobalContext;
		show(gobalContext, context);
	}*/
	protected SosDialog(Context gobalContext, Context context) {
	      // Exists only to defeat instantiation.
		this.smallWindowParams = new LayoutParams();
		this.smallWindowParams.height = 0;
		this.desiredHeight = 0;
		this.heightDelta = 0;
		this.mContext = context;
		this.globalContext = gobalContext;
		this.view = createView();
		show(gobalContext, context);
	}
	
	public static SosDialog newInstance(Context gobalContext, Context context)
	{
		if(singleton == null)
		{
			singleton = new SosDialog(gobalContext, context);
			
		}
		return singleton;
	}
	public int getDesiredHeight() {
		return smallWindowParams.height;//desiredHeight;
	}

	private Handler handlerss = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// refreshBlock(height);
			// height -= 1;

			handlerss.sendEmptyMessageDelayed(0, 100);
		};
	};

	private void initTouch() {
		arrow.setOnTouchListener(this);
	}

	private WindowManager getWindowManager(Context context) {
		return (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	}

	public void show(Context globalContext, Context context) {
		WindowManager windowManager = getWindowManager(globalContext);
		//this.desiredHeight = this.totalHeight - this.arrowHeight;
		
		smallWindowParams.type = LayoutParams.TYPE_PHONE;
		smallWindowParams.format = PixelFormat.RGBA_8888;//PixelFormat.RGBA_8888;
		smallWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE;
		smallWindowParams.gravity = Gravity.LEFT | Gravity.BOTTOM;
		smallWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
		smallWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT; //getGlobalHeight();
		//smallWindowParams.windowAnimations = WindowManager.LayoutParams.ANIMATION_CHANGED;
		// smallWindowParams.x = (int) ((context.getResources()
		// .getDisplayMetrics().widthPixels - 280 * context.getResources()
		// .getDisplayMetrics().density) / 2);
		smallWindowParams.y = 0;
		smallWindowParams.x = 0;
		windowManager.addView(view, smallWindowParams);
		//((PageActivity)UserApplication.getInstance().getActivity()).getWindow().getDecorView().findViewById(R.id.contaier).setPadding(0, 0, 0, view.getHeight());
		
	}
	
	private int getGlobalHeight() {
		//return (int) (/*mContext.getResources().getDisplayMetrics().density * 10 + */getDrawableHeight(R.drawable.sos_bg) + getDrawableHeight(R.drawable.sos_shadow));
		return (int) (/*mContext.getResources().getDisplayMetrics().density * 20 + 0.5f +*/ getDrawableHeight(R.drawable.arrow) + getDrawableHeight(R.drawable.sos_icon));
	}
	
	private int getDrawableHeight(int res) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeResource(UserApplication.getInstance().getActivity().getResources(),
				R.drawable.sos_icon, options);
		//return options.outHeight;
		return UserApplication.getInstance().getActivity().getResources().getDrawable(res).getIntrinsicHeight();
	}

	private View createView() {
		LayoutInflater inflater = (LayoutInflater) UserApplication.getInstance().getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.sos_layout, null);
		this.arrow = view.findViewById(R.id.arrow);
		//this.arrow.setVisibility(View.GONE);
		
		initTouch();

		this.totalHeight = (int) (getDrawableHeight(R.drawable.sos_icon)+ getDrawableHeight(R.drawable.arrow)+getDrawableHeight(R.drawable.sos_shadow) + UserApplication.getInstance().getActivity().getResources().getDisplayMetrics().density*20);
		this.arrowHeight = getDrawableHeight(R.drawable.arrow);
		this.sosIconHeight = getDrawableHeight(R.drawable.sos_icon);
		this.arrowBlock = view.findViewById(R.id.sos_block);
		this.sos = view.findViewById(R.id.sos);
		this.sos.setOnTouchListener(this);
		return view;
	}

	public void showArrow()
	{
		this.arrow.setVisibility(View.VISIBLE);
		this.desiredHeight = this.totalHeight;
		setDialogHeight(this.desiredHeight, false);
	}
	public void hideArrow()
	{
		this.arrow.setVisibility(View.GONE);
		this.desiredHeight = this.totalHeight - this.arrowHeight;
		if(((FrameLayout.LayoutParams)arrowBlock.getLayoutParams()).bottomMargin==0)
			setDialogHeight(this.desiredHeight, true);
		else
			setDialogHeight(0, true);
	}
	public boolean isArrowShown()
	{
		return this.arrow.getVisibility()==View.VISIBLE;
	}

	private float startY;
	private int startHeight;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			startY = event.getRawY();
			startHeight = smallWindowParams.height;
			System.out.println("start : " + startY);
			this.moving = false;
		} else if (action == MotionEvent.ACTION_MOVE) {
			float dy = event.getRawY() - startY;
			System.out.println(event.getRawY());
			isNorth = dy < 0;
			refreshBlock(dy);
			if(v.getId()==R.id.sos)
				this.moving = true;
			return true;
			// startY = event.getY();
		} else if (action == MotionEvent.ACTION_UP) {
			float dy = event.getRawY() - startY;
			if(v.getId()==R.id.sos && Math.abs(dy)<3f)
			{
				ClickQueue.getInstance().executeClick(v.getId());
				return true;
			}
			checkBlock(isNorth, dy);
			arrowBlock.invalidate();
			view.invalidate();
			startY = 0;
		}
		return false;
	}


	private void checkBlock(boolean isNorth, float dy) {
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) arrowBlock
				.getLayoutParams();
		if (params.bottomMargin < 0/*-height / 2*/) {
			params.bottomMargin = (int) (-1 * this.sosIconHeight)-(int)UserApplication.getInstance().getActivity().getResources().getDisplayMetrics().density*7;
			if(((PageActivity)UserApplication.getInstance().getActivity()).getWindow().getDecorView().getRootView().isShown())
				smallWindowParams.height = 0;
			else
				smallWindowParams.height = this.totalHeight-this.sosIconHeight; //this.height;
			getWindowManager(globalContext).updateViewLayout(view, smallWindowParams);
			((PageActivity)UserApplication.getInstance().getActivity()).getWindow().getDecorView().findViewById(R.id.contaier).setPadding(0, 0, 0, smallWindowParams.height);
			if (!isNorth) {
				hiddenView(15 * 1000);
			}
		} else {
			params.bottomMargin = 0;
			handler.removeMessages(1);
			smallWindowParams.height = desiredHeight!=0?desiredHeight: getGlobalHeight();
			((PageActivity)UserApplication.getInstance().getActivity()).getWindow().getDecorView().findViewById(R.id.contaier).setPadding(0, 0, 0, smallWindowParams.height);
			getWindowManager(globalContext).updateViewLayout(view, smallWindowParams);
		}
		arrowBlock.setLayoutParams(params);
		/*if (!isNorth) {
			hiddenView(15 * 1000);
		}*/
	}
	private void refreshBlock(float dy) {
		System.out.println("dy : " + dy);
		
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) arrowBlock
				.getLayoutParams();
		params.bottomMargin -= (int) (dy);

		if (params.bottomMargin < -this.desiredHeight || params.bottomMargin > 0) {
			return;
		}

		arrowBlock.setLayoutParams(params);
		System.out.println("y : " + arrow.getY());
		/*smallWindowParams.height = startHeight + (int)dy;
		getWindowManager(globalContext).updateViewLayout(view, smallWindowParams);*/
	}
	private void hiddenView(long time) {
		Message msg = Message.obtain();
		msg.what = 0;
		msg.obj = time;
		handler.sendMessage(msg);
	}
	
	private void rotation(boolean isNorth) {
//		RotateAnimation rotate = new RotateAnimation(0f,180f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);  
//		rotate.setDuration(700);
//		arrow.setAnimation(rotate);
//		rotate.setFillAfter(true);
//		rotate.startNow();
		
	}

	public static void hide()
	{
		if(singleton!=null)
		{
			//singleton.view.setVisibility(View.GONE);
			singleton.smallWindowParams.height = 0;
			singleton.getWindowManager(singleton.globalContext).updateViewLayout(singleton.view, singleton.smallWindowParams);
			((PageActivity)UserApplication.getInstance().getActivity()).getWindow().getDecorView().findViewById(R.id.contaier).setPadding(0, 0, 0, singleton.smallWindowParams.height);
		}
	}
	public static void show(int savedHeight)
	{
		if(singleton!=null)
		{
			singleton.view.setVisibility(View.VISIBLE);
			singleton.smallWindowParams.height = savedHeight;
			singleton.getWindowManager(singleton.globalContext).updateViewLayout(singleton.view, singleton.smallWindowParams);
			((PageActivity)UserApplication.getInstance().getActivity()).getWindow().getDecorView().findViewById(R.id.contaier).setPadding(0, 0, 0, singleton.smallWindowParams.height);
		}
	}
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				rotation(true);
				handler.sendEmptyMessageDelayed(1, (Long) msg.obj);
			} else if (msg.what == 1) {
				FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) arrowBlock
						.getLayoutParams();
				params.bottomMargin = 0;
				arrowBlock.setLayoutParams(params);
				smallWindowParams.height = desiredHeight!=0?desiredHeight: getGlobalHeight();
				getWindowManager(globalContext).updateViewLayout(view, smallWindowParams);
				((PageActivity)UserApplication.getInstance().getActivity()).getWindow().getDecorView().findViewById(R.id.contaier).setPadding(0, 0, 0, smallWindowParams.height);
			}

		};
	};

	public void setDialogHeight(int hgt, boolean auto_padding)
	{
		smallWindowParams.height = hgt;
		getWindowManager(globalContext).updateViewLayout(view, smallWindowParams);
		if(auto_padding)
			((PageActivity)UserApplication.getInstance().getActivity()).getWindow().getDecorView().findViewById(R.id.contaier).setPadding(0, 0, 0, smallWindowParams.height);
	}
	@Override
	public void onClick(View v) {
		ClickQueue.getInstance().executeClick(v.getId());
	}
}