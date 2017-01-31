package com.launch.sqlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Person {

	@DatabaseField(generatedId = true)
	private int id = -1;

	@DatabaseField(defaultValue="")
	private String nom;

	@DatabaseField(defaultValue="")
	private String prenom;

	@DatabaseField(defaultValue="")
	private String birthday;

	@DatabaseField
	private boolean isMale;

	// /////////////////
	@DatabaseField(defaultValue="")
	private String rue;
	@DatabaseField(defaultValue="")
	private String ville;
	@DatabaseField(defaultValue="")
	private String pay;
	@DatabaseField(defaultValue="")
	private boolean isActive;

	// ////////////////
	@DatabaseField(defaultValue="")
	private String batiment;
	@DatabaseField(defaultValue="")
	private String etae;
	@DatabaseField(defaultValue="")
	private String porte;
	@DatabaseField(defaultValue="")
	private String interphone;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public boolean isMale() {
		return isMale;
	}
	public void setMale(boolean isMale) {
		this.isMale = isMale;
	}
	public String getRue() {
		return rue;
	}
	public void setRue(String rue) {
		this.rue = rue;
	}
	public String getVille() {
		return ville;
	}
	public void setVille(String ville) {
		this.ville = ville;
	}
	public String getPay() {
		return pay;
	}
	public void setPay(String pay) {
		this.pay = pay;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public String getBatiment() {
		return batiment;
	}
	public void setBatiment(String batiment) {
		this.batiment = batiment;
	}
	public String getEtae() {
		return etae;
	}
	public void setEtae(String etae) {
		this.etae = etae;
	}
	public String getPorte() {
		return porte;
	}
	public void setPorte(String porte) {
		this.porte = porte;
	}
	public String getInterphone() {
		return interphone;
	}
	public void setInterphone(String interphone) {
		this.interphone = interphone;
	}

	
	
}
