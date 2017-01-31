package com.launch.sqlite;

import java.sql.SQLException;
import java.util.List;

import android.util.Log;

import com.j256.ormlite.dao.Dao;

public class PersonService {

	private Dao<Person, Integer> personDao;

	public PersonService(Dao<Person, Integer> dao) {
		this.personDao = dao;
	}
	
	public boolean saveOrUpdatePerson(Person person) {
		try {
			if(person.getId() != -1) {
				updatePerson(person);
			} else {
				personDao.create(person);				
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Log.i("Sqlite", "insert fail");
			return false;
		}
		return true;
	}
	
	public void updatePerson(Person person) {
		try {
			personDao.update(person);
		} catch (SQLException e) {
			e.printStackTrace();
			Log.i("Sqlite", "update fail");
		}
	}
	
	
	public Person getPerson() throws SQLException {
		if (personDao == null) {
			return null;
		}
		List<Person> persons = personDao.queryForAll();
		if(persons != null && persons.size() > 0) {
			return persons.get(0);
		}
		return new Person();
	}

}
