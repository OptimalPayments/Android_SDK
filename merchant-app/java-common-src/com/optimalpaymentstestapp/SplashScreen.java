package com.optimalpaymentstestapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Window;

import com.optimalpaymentstestapp.R;
import com.optimalpaymentstestapp.utils.Constants;
/**
 * @author Manisha.Rani
 * 
 */
public class SplashScreen  extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// No Title Bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(SplashScreen.this, Checkout.class);
				startActivity(intent);
				finish();

			}
		}, Constants.SPLASH_TIME_OUT);
	}
}