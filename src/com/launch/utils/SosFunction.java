package com.launch.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Scroller;

import com.launch.ui.R;

public class SosFunction implements OnTouchListener {

	private Scroller mScroller;

	private Context mContext;

	private View arrow;

	private View arrowBlock;

	private int height;

	private boolean isNorth;

	private Animation out;

	private Animation in;

	public SosFunction(Context context, View arrow, View block, int height) {
		this.mContext = context;
		mScroller = new Scroller(mContext);
		this.arrow = arrow;
		this.arrowBlock = block;
		this.height = height;
		initTouch();
		System.out.println("height : " + height);
		// handler.sendEmptyMessage(0);
		out = AnimationUtils.loadAnimation(mContext, R.anim.out_anim);
		out.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				arrowBlock.setVisibility(View.GONE);
			}
		});
		in = AnimationUtils.loadAnimation(mContext, R.anim.enter_back_anim);
		in.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				arrowBlock.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
			}
		});
	}

	// private Handler handler = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	// refreshBlock(height);
	// height -= 1;
	// handler.sendEmptyMessageDelayed(0, 100);
	// };
	// };

	private void initTouch() {
		arrow.setOnTouchListener(this);
	}

	private float startY;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			startY = event.getY();
			System.out.println("start : " + startY);
		} else if (action == MotionEvent.ACTION_MOVE) {
			float dy = event.getY() - startY;
			System.out.println(event.getY());
			isNorth = dy < 0;
			refreshBlock(dy);
			// startY = event.getY();
		} else if (action == MotionEvent.ACTION_UP) {
			float dy = event.getY() - startY;
			checkBlock(isNorth, dy);
			startY = 0;
		}
		return false;
	}

	private void checkBlock(boolean isNorth, float dy) {
		LinearLayout.LayoutParams params = (LayoutParams) arrowBlock
				.getLayoutParams();
		if (params.bottomMargin < -height / 2) {
			params.bottomMargin = (int) (-1 * height);
			if (!isNorth) {
				hiddenView(15 * 1000);
			}
		} else {
			params.bottomMargin = 0;
		}
		arrowBlock.setLayoutParams(params);
		// arrowBlock.invalidate();
	}

	private void hiddenView(long time) {
		Message msg = Message.obtain();
		msg.what = 0;
		msg.obj = time;
		handler.sendMessage(msg);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
//				arrowBlock.setAnimation(out);
//				out.start();
//				
				arrowBlock.setVisibility(View.GONE);
				handler.sendEmptyMessageDelayed(1, (Long) msg.obj);
			} else if (msg.what == 1) {
//				arrowBlock.setAnimation(in);
//				in.start();
				arrowBlock.setVisibility(View.VISIBLE);
			}

		};
	};

	private void refreshBlock(float dy) {
		System.out.println("dy : " + dy);
		LinearLayout.LayoutParams params = (LayoutParams) arrowBlock
				.getLayoutParams();
		params.bottomMargin -= (int) (dy);

		if (params.bottomMargin < -height || params.bottomMargin > 0) {
			return;
		}

		arrowBlock.setLayoutParams(params);
		System.out.println("y : " + arrow.getY());
		// startY -= (int) (dy);
	}

}
