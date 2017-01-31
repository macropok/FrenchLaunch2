package com.launch.sqlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class ChuteAlert extends Alert{

	@DatabaseField
	private boolean isActive;
	@DatabaseField
	private int deactivate;
	
	@DatabaseField
	private int level;
	
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public int getDeactivate() {
		return deactivate;
	}
	public void setDeactivate(int deactivate) {
		this.deactivate = deactivate;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	
}
