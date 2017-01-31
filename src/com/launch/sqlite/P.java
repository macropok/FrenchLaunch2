package com.launch.sqlite;

import android.os.Parcel;
import android.os.Parcelable;

public class P implements Parcelable {

	public String number;
	
	public String name;
	
	public String email;

	public long contactId;
	
	public int count;

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
}
