package com.optimalpaymentstestapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.optimalpaymentstestapp.lookup.LookUpProfile;

/**
 * @author Manisha.Rani
 * 
 */
public class Menu extends Activity {
	// Button
	private Button mFirstUserButton;
	private Button mLookUpProfileButton;
	private Button mUpdateCardButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		init();
	}

	/**
	 * This method is called when initialize UI
	 * 
	 * @return Void
	 * 
	 */

	private void init() {
		mFirstUserButton = (Button) findViewById(R.id.btn_FirstUser);
		mLookUpProfileButton = (Button) findViewById(R.id.btn_LookUpProfile);
		mUpdateCardButton =(Button) findViewById(R.id.btn_updatecard);
		mFirstUserButton.setOnClickListener(mClickListener);
		mLookUpProfileButton.setOnClickListener(mClickListener);
		mUpdateCardButton.setOnClickListener(mClickListener);
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
			case R.id.btn_FirstUser:
				Intent intentFirstUser = new Intent(Menu.this,Checkout.class);
				startActivity(intentFirstUser);
				finish();
				break;
			case R.id.btn_LookUpProfile:
				Intent intentLookUpProfile = new Intent(Menu.this,LookUpProfile.class);
				startActivity(intentLookUpProfile);
				finish();
				break;
			case R.id.btn_updatecard:
				Intent intentUpdateCard = new Intent(Menu.this,CardDetails.class);
				startActivity(intentUpdateCard);
				finish();
				break;

			default:
				break;
			}
		}
	};
}
