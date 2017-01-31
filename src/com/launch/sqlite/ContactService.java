package com.launch.sqlite;

import java.sql.SQLException;
import java.util.List;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.launch.sqlite.Contact.ContactType;

public class ContactService {

	private Dao<Contact, Integer> contactDao;

	public ContactService(Dao<Contact, Integer> dao) {
		this.contactDao = dao;
	}
	
	public boolean saveOrUpdateContact(Contact contact) {
		try {
			if(contact.getId() != -1) {
				updateContact(contact);
			} else {
				contactDao.create(contact);				
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Log.i("Sqlite", "insert fail");
			return false;
		}
		return true;
	
	}
	
	public void remove(Contact contact) {
		try {
			contactDao.delete(contact);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Contact> getAllContacts() {
		QueryBuilder<Contact, Integer> builder = contactDao.queryBuilder();
		try {
			return builder.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void updateContact(Contact contact) {
		try {
			contactDao.update(contact);
		} catch (SQLException e) {
			e.printStackTrace();
			Log.i("Sqlite", "update fail");
		}
	}
	
	
	public List<Contact> getContact(ContactType type) throws SQLException {
		if (contactDao == null) {
			return null;
		}
		QueryBuilder<Contact, Integer> builder = contactDao.queryBuilder();
		builder.where().eq("type", type);
		
		List<Contact> contacts = builder.query();
		return contacts;
	}
	
	public List<Contact> getContact(long phoneContactId) throws SQLException {
		if (contactDao == null) {
			return null;
		}
		QueryBuilder<Contact, Integer> builder = contactDao.queryBuilder();
		builder.where().eq("phoneContactId", phoneContactId);
		
		List<Contact> contacts = builder.query();
		return contacts;
	}

}
