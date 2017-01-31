package com.launch.ui;

import com.launch.utils.SosFunction;
import com.launch.utils.ThirdLaunch;
import com.launch.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TelephoneDialFragment extends Fragment implements OnClickListener {
	public static TelephoneDialFragment fragment;

	private View containerView;
	
	private TextView one_txt,two_txt,three_txt,four_txt,five_txt,six_txt,seven_txt,eight_txt,nine_txt,star_txt,zero_txt,hash_txt;
	private LinearLayout linear_one,linear_two,linear_three,linear_four,linear_five,linear_six,linear_seven,linear_eight,linear_nine,linear_star,linear_zero,linear_hash;
	private ImageView call_btn;
	private EditText input_number_btn;
	private ImageView clear_btn;
	
	public static TelephoneDialFragment getInstance() {
		if (fragment == null) {
			fragment = new TelephoneDialFragment();
			Bundle bundle = new Bundle();
			bundle.putString("Title", "Telephone");
			bundle.putInt("bgColor", 0xff51b63c);
			bundle.putInt("ImageId", R.drawable.actionbar_tel_icon);
			fragment.setArguments(bundle);
		}
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((PageActivity)getActivity()).setIcon(getArguments().getInt("bgColor"), getArguments().getInt("ImageId"));
		containerView = inflater.inflate(R.layout.telephone_dial_ui, container, false);
		Get_Id();
		set_Type_Face();
		get_Linear_views();
		setOnClickListener();
		
		containerView.findViewById(R.id.btnBack).setOnClickListener(UserApplication.getInstance().getActivity());
		return containerView;
	}
	public void Get_Id(){
		input_number_btn=(EditText) containerView.findViewById(R.id.input_number_btn);
		clear_btn=(ImageView)containerView.findViewById(R.id.clear_btn);
		
		one_txt=(TextView) containerView.findViewById(R.id.one_txt);
		two_txt=(TextView) containerView.findViewById(R.id.two_txt);
		three_txt=(TextView) containerView.findViewById(R.id.three_txt);
		four_txt=(TextView) containerView.findViewById(R.id.four_txt);
		
		five_txt=(TextView) containerView.findViewById(R.id.five_txt);
		six_txt=(TextView) containerView.findViewById(R.id.six_txt);
		seven_txt=(TextView) containerView.findViewById(R.id.seven_txt);
		eight_txt=(TextView) containerView.findViewById(R.id.eight_txt);
		
		nine_txt=(TextView) containerView.findViewById(R.id.nine_txt);
		star_txt=(TextView) containerView.findViewById(R.id.star_txt);
		zero_txt=(TextView) containerView.findViewById(R.id.zero_txt);
		hash_txt=(TextView) containerView.findViewById(R.id.hash_txt);
	}
	
	public void set_Type_Face(){
		one_txt.setTypeface(Utils.getAvailableFontBold(UserApplication.getInstance()));
		two_txt.setTypeface(Utils.getAvailableFontBold(UserApplication.getInstance()));
		three_txt.setTypeface(Utils.getAvailableFontBold(UserApplication.getInstance()));
		four_txt.setTypeface(Utils.getAvailableFontBold(UserApplication.getInstance()));
		
		five_txt.setTypeface(Utils.getAvailableFontBold(UserApplication.getInstance()));
		six_txt.setTypeface(Utils.getAvailableFontBold(UserApplication.getInstance()));
		seven_txt.setTypeface(Utils.getAvailableFontBold(UserApplication.getInstance()));
		eight_txt.setTypeface(Utils.getAvailableFontBold(UserApplication.getInstance()));
		
		nine_txt.setTypeface(Utils.getAvailableFontBold(UserApplication.getInstance()));
		star_txt.setTypeface(Utils.getAvailableFontBold(UserApplication.getInstance()));
		zero_txt.setTypeface(Utils.getAvailableFontBold(UserApplication.getInstance()));
		hash_txt.setTypeface(Utils.getAvailableFontBold(UserApplication.getInstance()));
	}
	
	public void get_Linear_views(){
		linear_one=(LinearLayout) containerView.findViewById(R.id.linear_one);
		linear_two=(LinearLayout) containerView.findViewById(R.id.linear_two);
		linear_three=(LinearLayout) containerView.findViewById(R.id.linear_three);
		linear_four=(LinearLayout) containerView.findViewById(R.id.linear_four);
		
		linear_five=(LinearLayout) containerView.findViewById(R.id.linear_five);
		linear_six=(LinearLayout) containerView.findViewById(R.id.linear_six);
		linear_seven=(LinearLayout) containerView.findViewById(R.id.linear_seven);
		linear_eight=(LinearLayout) containerView.findViewById(R.id.linear_eight);
		
		linear_nine=(LinearLayout) containerView.findViewById(R.id.linear_nine);
		linear_star=(LinearLayout) containerView.findViewById(R.id.linear_star);
		linear_zero=(LinearLayout) containerView.findViewById(R.id.linear_zero);
		linear_hash=(LinearLayout) containerView.findViewById(R.id.linear_hash);
		
		call_btn=(ImageView) containerView.findViewById(R.id.call_btn);
		
		  // add PhoneStateListener
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        //telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);

	}
	
	public void setOnClickListener(){
		linear_one.setOnClickListener(this);
		linear_two.setOnClickListener(this);
		linear_three.setOnClickListener(this);
		linear_four.setOnClickListener(this);
		
		linear_five.setOnClickListener(this);
		linear_six.setOnClickListener(this);
		linear_seven.setOnClickListener(this);
		linear_eight.setOnClickListener(this);
		
		linear_nine.setOnClickListener(this);
		linear_star.setOnClickListener(this);
		linear_zero.setOnClickListener(this);
		linear_hash.setOnClickListener(this);
		
		call_btn.setOnClickListener(this);
		clear_btn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.call_btn:
			
			try {
				//if (input_number_btn != null && (input_number_btn.getText().length()==10||input_number_btn.getText().length()==11)) {
				//startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + input_number_btn.getText())));
				/*}else if(input_number_btn != null && input_number_btn.getText().length()==0){
					Toast.makeText(getApplicationContext(), "You missed to type the number!", Toast.LENGTH_SHORT).show();
				}else if(input_number_btn != null && input_number_btn.getText().length()<10){
					Toast.makeText(getApplicationContext(),"Check whether you entered correct number!",Toast.LENGTH_SHORT).show();
				}*/
				} catch (Exception e) {
			}
			 Intent callIntent = new Intent(Intent.ACTION_CALL);
             callIntent.setData(Uri.parse("tel:"+input_number_btn.getText().toString().trim()));
             startActivity(callIntent);
			break;
			
		case R.id.linear_one:
			input_number_btn.setText(input_number_btn.getText().toString().trim().concat("1"));
			break;
			
		case R.id.linear_two:
			input_number_btn.setText(input_number_btn.getText().toString().trim().concat("2"));
			break;
			
		case R.id.linear_three:
			
			input_number_btn.setText(input_number_btn.getText().toString().trim().concat("3"));
			break;
			
		case R.id.linear_four:
			input_number_btn.setText(input_number_btn.getText().toString().trim().concat("4"));
			break;
			
		case R.id.linear_five:
			input_number_btn.setText(input_number_btn.getText().toString().trim().concat("5"));
			break;
			
		case R.id.linear_six:
			input_number_btn.setText(input_number_btn.getText().toString().trim().concat("6"));
			break;
			
		case R.id.linear_seven:
			input_number_btn.setText(input_number_btn.getText().toString().trim().concat("7"));
			break;
			
		case R.id.linear_eight:
			input_number_btn.setText(input_number_btn.getText().toString().trim().concat("8"));
			break;
			
		case R.id.linear_nine:
			input_number_btn.setText(input_number_btn.getText().toString().trim().concat("9"));
			break;
			
		case R.id.linear_star:
			input_number_btn.setText(input_number_btn.getText().toString().trim().concat("*"));
			break;
			
		case R.id.linear_zero:
			input_number_btn.setText(input_number_btn.getText().toString().trim().concat("0"));
			break;
			
		case R.id.linear_hash:
			input_number_btn.setText(input_number_btn.getText().toString().trim().concat("#"));
			break;
			
		case R.id.clear_btn:
			
			if (input_number_btn.getText().toString().length() > 0) {
				input_number_btn.setText(input_number_btn.getText().toString().trim().substring(0, input_number_btn.getText().toString().trim().length()-1));
			}
			
			break;

		default:
			break;
		}
	}
	
	//monitor phone call activities
    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        String LOG_TAG = "LOGGING 123";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended, 
                // need detect flag from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");

                if (isPhoneCalling) {

                    Log.i(LOG_TAG, "restart app");

                    // restart app
                    Intent i = getActivity().getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(
                        		getActivity().getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                    isPhoneCalling = false;
                }

            }
        }
    }
	
}
