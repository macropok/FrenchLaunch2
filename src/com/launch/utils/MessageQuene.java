package com.launch.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.location.Address;
import android.os.Handler;
import android.os.Message;

import com.launch.ui.UserApplication;
import com.launch.utils.MessageUtils.NX;
import com.launch.utils.ProgressDialog.onCancelListener;
import com.launch.utils.ProgressDialog.onEmailListener;
import com.launch.utils.ProgressDialog.onMessageListener;

public class MessageQuene implements onEmailListener, onMessageListener, onCancelListener {

	private static MessageQuene mq = null;
	
	private Context context;

	private class MessageExtra {

		public NX nx;
		
		public long delay;

		public boolean isDialog;
	}

	private ExecutorService executor;

	private ProgressDialog mDialog;
	
	private HashMap<String, Boolean> pushed;
	
	private Queue<MessageExtra> messages;

	private MessageQuene(Context context) {
		this.context = context;
		messages = new LinkedList<MessageQuene.MessageExtra>();
		pushed = new HashMap<String, Boolean>();
	}
	
	public static MessageQuene getInstance(Context context) {
		if (mq == null) {
			mq = new MessageQuene(context);
		}
		return mq;
	}

	public void addMessage(NX nx,boolean dialog) {
		if(pushed.containsKey(nx.getName()) && pushed.get(nx.getName())) {
			return;
		}
		MessageExtra extra = new MessageExtra();
		extra.isDialog = dialog;
		extra.nx = nx;
		messages.add(extra);
		pushed.put(nx.getName(), true);
		executeMessage();
	}
	
	public void addMessage(NX nx,long time, boolean dialog) {
		if(pushed.containsKey(nx.getName()) && pushed.get(nx.getName())) {
			return;
		}
		MessageExtra extra = new MessageExtra();
		extra.isDialog = dialog;
		extra.nx = nx;
		extra.delay = time;
		messages.add(extra);
		pushed.put(nx.getName(), true);
		executeMessage();
	}

	private void showProgressDialog(NX nx) {
		mDialog = new ProgressDialog(UserApplication.getInstance().getGlobalContext(context), context, nx, this);
	}
	
	private void hideProgressDialog() {
		if(mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
	}

	public void executeMessage() {
		if (messages.size() <= 0) {
			System.out.println("execute message finished");
			return;
		}
		MessageExtra extra = messages.poll();
		if(extra.nx == NX.CHUTES) {
			executeByDelay(extra, extra.delay);
		} else {
			showProgressDialog(extra.nx);
			execute(extra);
		}
	}
	
	private void executeByDelay(MessageExtra extra, long time) {
		showProgressDialog(extra.nx);
		Message msg = Message.obtain();
		msg.what = 3;
		msg.obj = extra;
		handler.sendMessageDelayed(msg, time);
	}
	
	private void execute(MessageExtra extra) {
		executor = Executors.newFixedThreadPool(1);
		executor.execute(new MessageThread(extra, this, this));
	}
	

	private class MessageThread implements Runnable {

		private MessageExtra extra;
		
		private onMessageListener mmslistener;
		
		private onEmailListener emaillistener;

		public MessageThread(MessageExtra mExtra, onMessageListener mmslistener, onEmailListener emaillistener) {
			this.extra = mExtra;
			this.mmslistener = mmslistener;
			this.emaillistener = emaillistener;
		}

		@Override
		public void run() {
			System.out.println("loading gps address");
			Address address = MessageUtils.getCurrentPositionAddress(extra.nx);
			sendMessage(address);
//			if(listener != null) {
//				listener.onFinishCallback();
//			}
		}
		
		private void sendMessage(Address address) {
			String addr = null;
			if (address != null) {
//				addr = address.getCountryName() + "," + address.getLocality() + "," + address.getSubLocality() + "," + address.getAddressLine(0)+ "  GPS position £º("+ address.getLatitude() + "," + address.getLongitude() + ")" +"£¬google maps: http://maps.google.com/maps?ll="+ address.getLongitude() +","+ address.getLatitude() +"&spn=0.1,0.1&t=k&hl=fr-FR";
				addr = address.getAddressLine(0)+ "  GPS position £º("+ address.getLatitude() + "," + address.getLongitude() + ")" +"£¬google maps: http://maps.google.com/maps?ll="+ address.getLongitude() +","+ address.getLatitude() +"&spn=0.1,0.1&t=k&hl=fr-FR";
				System.out.println("address : " + addr);
			}
			switch (extra.nx) {
			case SANTE:
				AlertHandler.launchSante(context,addr, mmslistener, emaillistener);
				break;
			case AGRESSION:
				AlertHandler.launchPolice(context,extra.nx, addr, mmslistener, emaillistener);
				break;
			case FEU:
				AlertHandler.launchFeu(context,extra.nx, addr, mmslistener, emaillistener);
				break;
			case BATTERIE:
				AlertHandler.launchBatterieAlert(context,addr, mmslistener, emaillistener);
				break;
			case CONFORT:
				AlertHandler.launchPolice(context,extra.nx, addr, mmslistener, emaillistener);
				break;
			case CHUTES:
				AlertHandler.launchFeu(context,extra.nx,addr, mmslistener, emaillistener);
				break;
			}
		}
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 1) {
//				if(mDialog != null) {
//					mDialog.finish();
//				}
//				executor.shutdown();
//				executor.shutdownNow();
//				Thread.currentThread().interrupt();
//				executeMessage();
			} else if(msg.what == 2) {
				hideProgressDialog();
				executor.shutdown();
				executor.shutdownNow();
				Thread.currentThread().interrupt();
				executeMessage();
			} else if(msg.what == 3) {
				MessageExtra extra = (MessageExtra) msg.obj;
				execute(extra);
			} else if(msg.what == 4) {
				//mms
				if(mDialog != null) {
					mDialog.setMMSStatus((Boolean) msg.obj);
				}
				checkTaskProgress();
			} else if(msg.what == 5) {
				//email
				if(mDialog != null) {
					mDialog.setMailStatus((Boolean) msg.obj);
				}
				checkTaskProgress();
			}
		};
	};
	
	public void checkTaskProgress() {
		if(mDialog != null && mDialog.isOver()) {
			executor.shutdown();
			executor.shutdownNow();
			Thread.currentThread().interrupt();
			executeMessage();
		}
	}

//	@Override
//	public void onFinishCallback() {
//		System.out.println("message execute finish, but check message queue");
////		hideProgressDialog();
//		handler.sendEmptyMessage(1);
//	}
	
	public void setPushStatus(NX nx, boolean pushed) {
		this.pushed.put(nx.getName(), pushed);
	}

	@Override
	public void onCancel(NX nx, boolean dismiss) {
		if(!dismiss) {
			if(nx == NX.CHUTES) {
				hideProgressDialog();
				handler.removeMessages(3);	
			} else {
				handler.sendEmptyMessage(2);
			}
		}
		pushed.put(nx.getName(), false);
	}

	@Override
	public void onMmsHandle(boolean issuccess) {
		Message msg = Message.obtain();
		msg.obj = issuccess;
		msg.what = 4;
		handler.sendMessage(msg);
	}

	@Override
	public void onEmailHandle(boolean issuccess) {
		Message msg = Message.obtain();
		msg.obj = issuccess;
		msg.what = 5;
		handler.sendMessage(msg);		
	}
}
