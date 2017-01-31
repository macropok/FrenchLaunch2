package com.launch.sqlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class BatterieAlert extends Alert{

	@DatabaseField
	private boolean isActive;
	
	@DatabaseField
	private int minbatterie;
	@DatabaseField
	private int time;

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public int getMinbatterie() {
		return minbatterie;
	}

	public void setMinbatterie(int minbatterie) {
		this.minbatterie = minbatterie;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}


	
}
