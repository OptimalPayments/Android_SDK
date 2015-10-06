package com.optimalpaymentstestapp;

import com.optimalpaymentstestapp.utils.Constants;
import com.optimalpaymentstestapp.utils.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * @author Manisha.Rani
 * 
 */

public class Checkout extends Activity {
	// Button
	private Button mCheckOutButton;
	private Button mBackButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkout);
		init();
	}

	/**
	 * This method is called when initialize UI
	 * 
	 * @return Void
	 * 
	 */

	private void init() {
		mCheckOutButton = (Button) findViewById(R.id.btn_Checkout);
		mBackButton = (Button)findViewById(R.id.btn_back);
		mCheckOutButton.setOnClickListener(mClickListener);
		mBackButton.setOnClickListener(mClickListener);
	}

	/**
	 * This method is called when button click listener
	 * 
	 * @return Void
	 * 
	 */
	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_Checkout:
				 Intent intent = new Intent(Checkout.this,PayNow.class);
				 intent.putExtra(Constants.RANDOM_MERCHANT_REFRENCE_NUMBER, Utils.twelveDigitRandomAlphanumeric());
				 startActivity(intent);
				finish();
				break;
			case R.id.btn_back:
				Intent intentBack = new Intent(Checkout.this,Menu.class);
				startActivity(intentBack);
				finish();
				break;

			default:
				break;
			}
		}
	};
	
	/**
	 * This method is called when back pressed finished the activity
	 * 
	 * 
	 * @return void
	 * 
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intentCheckOut = new Intent(Checkout.this, Menu.class);
		startActivity(intentCheckOut);
		finish();
	}
	
}
