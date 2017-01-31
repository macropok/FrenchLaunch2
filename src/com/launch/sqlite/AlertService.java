package com.launch.sqlite;

import java.sql.SQLException;
import java.util.List;

import android.util.Log;

import com.j256.ormlite.dao.Dao;

public class AlertService<T extends Alert> {

	private Dao<T, Integer> alertDao;

	public AlertService(Dao<T, Integer> dao) {
		this.alertDao = dao;
	}
	
	public boolean saveOrUpdateAlert(T alert) {
		try {
			if(alert.getId() != -1) {
				updateAlert(alert);
			} else {
				alertDao.create(alert);				
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Log.i("Sqlite", "insert fail");
			return false;
		}
		return true;
	}
	
	public void updateAlert(T alert) {
		try {
			alertDao.update(alert);
		} catch (SQLException e) {
			e.printStackTrace();
			Log.i("Sqlite", "update fail");
		}
	}
	
	
	public T getAlert(){
		if (alertDao == null) {
			return null;
		}
		List<T> alerts = null;
		try {
			alerts = alertDao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(alerts != null && alerts.size() > 0) {
			return alerts.get(0);
		}
		return null;
	}

}
