package com.optimalpaymentstestapp;

/**
 * Created by Manisha.Rani on 26-06-2015.
 *
 */

import android.app.Application;
import android.content.Context;

public class OptimalPayApplication extends Application {
	public static Context mApplicationContext;

	/**
	 * On Create.
	 */
	public void onCreate() {
		super.onCreate();
		mApplicationContext = this;
	} // end of onCreate()
} // end of class OptimalPayApplication