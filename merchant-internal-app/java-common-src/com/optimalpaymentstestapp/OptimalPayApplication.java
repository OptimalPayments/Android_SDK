package com.optimalpaymentstestapp;

import android.app.Application;
import android.content.Context;
/**
 * @author Manisha.Rani
 * 
 */
public class OptimalPayApplication extends Application {
	public static Context mApplicationContext;

	public void onCreate() {
		mApplicationContext = this;
	};
}


