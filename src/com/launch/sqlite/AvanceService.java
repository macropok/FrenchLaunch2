package com.launch.sqlite;

import java.sql.SQLException;
import java.util.List;

import android.util.Log;

import com.j256.ormlite.dao.Dao;

public class AvanceService {

	private Dao<Avance, Integer> avanceDao;

	public AvanceService(Dao<Avance, Integer> dao) {
		this.avanceDao = dao;
	}
	
	public boolean saveOrUpdateAvance(Avance Avance) {
		try {
			if(Avance.getId() != -1) {
				updateAvance(Avance);
			} else {
				avanceDao.create(Avance);				
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Log.i("Sqlite", "insert fail");
			return false;
		}
		return true;
	}
	
	public void updateAvance(Avance Avance) {
		try {
			avanceDao.update(Avance);
		} catch (SQLException e) {
			e.printStackTrace();
			Log.i("Sqlite", "update fail");
		}
	}
	
	
	public Avance getAvance() throws SQLException {
		if (avanceDao == null) {
			return new Avance();
		}
		List<Avance> Avances = avanceDao.queryForAll();
		if(Avances != null && Avances.size() > 0) {
			return Avances.get(0);
		}
		return new Avance();
	}

}
