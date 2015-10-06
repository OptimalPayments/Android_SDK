package com.optimalpaymentstestapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.optimalpaymentstestapp.OptimalPayApplication;

/**
 * @author ManishaR
 * 
 */
public class AppSettings {

	private static final String PREFERENCES = "Preferences";

	static protected SharedPreferences getSharedPreferences(Context context) {

		return context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
	}

	/**
	 * @param setting
	 * @param value
	 */
	public static void setString(String setting, String value) {
		SharedPreferences settings = getSharedPreferences(OptimalPayApplication.mApplicationContext);
		Editor edit = settings.edit();
		edit.putString(setting, value);
		edit.commit();
	}

	public static void setBoolean(String setting, boolean value) {
		SharedPreferences settings = getSharedPreferences(OptimalPayApplication.mApplicationContext);
		Editor edit = settings.edit();
		edit.putBoolean(setting, value);
		edit.commit();
	}
	
	/**
	 * @param setting
	 * @param def
	 * @return
	 */
	public static String getString(String setting, String def) {
		SharedPreferences settings = getSharedPreferences(OptimalPayApplication.mApplicationContext);
		return settings.getString(setting, def);
	}

	public static boolean getBoolean(String setting, boolean def) {
		SharedPreferences settings = getSharedPreferences(OptimalPayApplication.mApplicationContext);
		return settings.getBoolean(setting, def);
	}
	
}
