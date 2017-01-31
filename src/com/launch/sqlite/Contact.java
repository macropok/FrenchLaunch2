package com.launch.sqlite;

import android.text.TextUtils;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Contact {

	public enum ContactType{
		ENFANT, VOISIN, AMI, AIDE, PROCHES
	};
	
	@DatabaseField(generatedId=true)
	private int id = -1;
	@DatabaseField
	private String name;
	@DatabaseField
	private String phone;
	@DatabaseField
	private String email;
	@DatabaseField(defaultValue="0")
	private long phoneContactId;
	@DatabaseField
	private ContactType type;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public long getPhoneContactId() {
		return phoneContactId;
	}
	public void setPhoneContactId(long _id) {
		this.phoneContactId = _id;
	}
	public ContactType getType() {
		return type;
	}
	public void setType(ContactType type) {
		this.type = type;
	}
	
	public boolean checkInvalid() {
		if(TextUtils.isEmpty(name) && TextUtils.isEmpty(email) && TextUtils.isEmpty(phone)) {
			return false;
		}
		return true;
	}
}
