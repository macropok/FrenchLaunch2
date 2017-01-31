package com.launch.sqlite;

import java.io.File;
import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Database helper class used to manage the creation and upgrading of your
 * database. This class also usually provides the DAOs used by the other
 * classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	public DatabaseHelper(Context context) {
		super(context, /*Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +*/ DATABASE_NAME, null, DATABASE_VERSION);
	}
	// name of the database file for your application -- change to something
	// appropriate for your app
	private static final String DATABASE_NAME = "launch.db";
	// any time you make changes to your database objects, you may have to
	// increase the database version
	private static final int DATABASE_VERSION = 9;

	// the DAO object we use to access the SimpleData table
	private PersonService personService = null;
	
	private AvanceService avanceService = null;
	
	private ContactService contactService = null;
	
	private AlertService<? extends Alert> alertService = null;


	/**
	 * This is called when the database is first created. Usually you should
	 * call createTable statements here to create the tables that will store
	 * your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, Person.class);
			TableUtils.createTable(connectionSource, InactiviteAlert.class);
			TableUtils.createTable(connectionSource, ChuteAlert.class);
			TableUtils.createTable(connectionSource, BatterieAlert.class);
			TableUtils.createTable(connectionSource, ConfortAlert.class);
			TableUtils.createTable(connectionSource, Avance.class);
			TableUtils.createTable(connectionSource, Contact.class);
			
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * This is called when your application is upgraded and it has a higher
	 * version number. This allows you to adjust the various data to match the
	 * new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, Person.class, true);
			TableUtils.dropTable(connectionSource, InactiviteAlert.class, true);
			TableUtils.dropTable(connectionSource, ChuteAlert.class, true);
			TableUtils.dropTable(connectionSource, BatterieAlert.class, true);
			TableUtils.dropTable(connectionSource, ConfortAlert.class, true);
			TableUtils.dropTable(connectionSource, Avance.class, true);
			TableUtils.dropTable(connectionSource, Contact.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao
	 * for our SimpleData class. It will create it or just give the cached
	 * value. RuntimeExceptionDao only through RuntimeExceptions.
	 * 
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PersonService getPersonService() throws SQLException {
		if (personService == null) {
			personService = new PersonService((Dao<Person, Integer>) getDao(Person.class));
		}
		return personService;
	}
	
	@SuppressWarnings("unchecked")
	public AvanceService getAvanceService() throws SQLException {
		if (avanceService == null) {
			avanceService = new AvanceService((Dao<Avance, Integer>) getDao(Avance.class));
		}
		return avanceService;
	}
	
	@SuppressWarnings("unchecked")
	public ContactService getContactService() throws SQLException {
		if (contactService == null) {
			contactService = new ContactService((Dao<Contact, Integer>) getDao(Contact.class));
		}
		return contactService;
	}
	
	@SuppressWarnings("unchecked")
	public AlertService<? extends Alert> getAlertService(Class<? extends Alert> cls) throws SQLException {
		alertService = new AlertService((Dao<?, Integer>) getDao(cls));
		return alertService;
	}
	
	

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		personService = null;
		alertService = null;
	}
}
