package com.launch.sqlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class InactiviteAlert extends Alert {

	@DatabaseField
	private boolean isActive;
	@DatabaseField
	private String coucher;
	@DatabaseField(defaultValue="0")
	private String sommeil;
	@DatabaseField
	private int desarmocage;
	@DatabaseField
	private int lalerte;

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getCoucher() {
		return coucher;
	}

	public void setCoucher(String coucher) {
		this.coucher = coucher;
	}

	public String getSommeil() {
		return sommeil;
	}

	public void setSommeil(String sommeil) {
		this.sommeil = sommeil;
	}
	public int getDesarmocage() {
		return desarmocage;
	}

	public void setDesarmocage(int desarmocage) {
		this.desarmocage = desarmocage;
	}

	public int getLalerte() {
		return lalerte;
	}

	public void setLalerte(int lalerte) {
		this.lalerte = lalerte;
	}


}
