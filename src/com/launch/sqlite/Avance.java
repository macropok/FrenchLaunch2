package com.launch.sqlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Avance {

	@DatabaseField(generatedId = true)
	private int id = -1;

	@DatabaseField
	private String police = "17";

	@DatabaseField
	private String pompiers = "18";
	@DatabaseField
	private String samu = "15";
	@DatabaseField
	private String messageAudio;
	@DatabaseField(defaultValue="")
	private String messageText;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPolice() {
		return police;
	}
	public void setPolice(String police) {
		this.police = police;
	}
	public String getPompiers() {
		return pompiers;
	}
	public void setPompiers(String pompiers) {
		this.pompiers = pompiers;
	}
	public String getSamu() {
		return samu;
	}
	public void setSamu(String samu) {
		this.samu = samu;
	}
	public String getMessageAudio() {
		return messageAudio;
	}
	public void setMessageAudio(String messageAudio) {
		this.messageAudio = messageAudio;
	}
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

}
