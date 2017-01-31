package com.launch.sqlite;

import com.j256.ormlite.field.DatabaseField;

public class Alert {

	@DatabaseField(generatedId=true)
	private int id=-1;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
