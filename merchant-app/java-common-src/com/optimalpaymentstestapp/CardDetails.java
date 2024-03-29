package com.optimalpaymentstestapp;

/**
 * Created by Manisha.Rani on 26-06-2015.
 *
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.optimalpayments.Environment;
import com.optimalpayments.OptimalApiClient;
import com.optimalpayments.common.CardExpiry;
import com.optimalpayments.common.Error;
import com.optimalpayments.customervault.Card;
import com.optimalpayments.customervault.SingleUseToken;
import com.optimalpaymentstestapp.utils.Constants;
import com.optimalpaymentstestapp.utils.Utils;

public class CardDetails extends Activity {

	// EditText
	private EditText mNameOnCardEditText;
	private EditText mCardNoEditText;
	private EditText mMonthEditText;
	private EditText mYearEditText;
	private EditText mStreet1EditText;
	private EditText mStreet2EditText;
	private EditText mCityEditText;
	private EditText mCountryEditText;
	private EditText mStateEditText;
	private EditText mZipEditText;

	// Context
	private Context mContext;

	//String
	private String mCardHolderName;
	private String mCardNo;
	private String mMonth;
	private String mYear;
	private String mStreet1;
	private String mStreet2;
	private String mCity;
	private String mCountry;
	private String mState;
	private String mZip;
	private OptimalApiClient client;

    /**
     * On Create Activity.
     * @param savedInstanceState Object of Bundle holding instance state.
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.carddetails);
		init();
	} // end of onCreate()

	/**
	 * This method is called when initialize UI
	 */
	private void init() {
		mContext = this;
		Button mBackButton = (Button) findViewById(R.id.btn_back);
		Button mOKButton = (Button) findViewById(R.id.btn_Confirm);
		mBackButton.setOnClickListener(mClickListener);
		mOKButton.setOnClickListener(mClickListener);
		
		mNameOnCardEditText = (EditText) findViewById(R.id.et_name_on_card);
		mCardNoEditText = (EditText) findViewById(R.id.et_card_no);
		mMonthEditText = (EditText) findViewById(R.id.et_month);
		mYearEditText = (EditText) findViewById(R.id.et_year);
		mStreet1EditText = (EditText) findViewById(R.id.et_street1);
		mStreet2EditText = (EditText) findViewById(R.id.et_street2);
		mCityEditText = (EditText) findViewById(R.id.et_city);
		mCountryEditText = (EditText) findViewById(R.id.et_country);
		mStateEditText = (EditText) findViewById(R.id.et_state);
		mZipEditText = (EditText) findViewById(R.id.et_zip);
	} // end of init()

	/**
	 * This method is called when button click listener
	 */
	private final OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_back:
				final Intent intent = new Intent(CardDetails.this,Checkout.class);
				startActivity(intent);
				finish();
				break;
			case R.id.btn_Confirm:
				buttonConfirmClick();
				break;
			
			default:
				break;
			}
		}
	}; // end of onClickListener
	
	/**
	 * This method is used to execute async task
	 */
	private void buttonConfirmClick(){
		getValuesfromEditText();
		//Check Internet connection
		if (!isCheckInternet()) {
			Toast.makeText(getApplicationContext(),	Constants.PLEASE_TURN_ON_YOUR_INTERNET,Toast.LENGTH_LONG).show();
			return;
		}
		//Execute AsyncTask for SingleUseToken
		 new SingleUseTokenTask().execute();
	} // end of buttonConfirmClick()

	/**
	 * Single Use Token Task.
	 * This class is used to create the single use token.
	 */
	private class SingleUseTokenTask extends AsyncTask<String, Void, SingleUseToken> {

        /**
         * On Pre Execute.
         */
        @Override
		protected void onPreExecute() {
			super.onPreExecute();
			Utils.startProgressDialog(CardDetails.this,
					getString(R.string.loading_text));
		} // end of onPreExecute()

		/**
		 * On Post Execute.
		 * @param singleUseTokenObject
		 */
		@Override
		protected void onPostExecute(SingleUseToken singleUseTokenObject) {
			super.onPostExecute(singleUseTokenObject);
			try {
				Utils.stopProgressDialog();
				String mPaymentToken = singleUseTokenObject.getPaymentToken();
				CardExpiry cardExp = null;
				Integer strCardExpMonth = null;
				Integer strCardExpYear =null;
				String lastDigits = null;

				Card card = singleUseTokenObject.getCard();
				if(card != null){
			    lastDigits = card.getLastDigits();
			    cardExp = card.getCardExpiry();
				}
				if(cardExp != null){
				 strCardExpMonth = cardExp.getMonth();
				 strCardExpYear = cardExp.getYear();
				}

				Error error = singleUseTokenObject.getError();

				String connectivityError = singleUseTokenObject.getConnectivityError();
				if (!Utils.isEmpty(connectivityError)) {
					Utils.showDialogAlert(Constants.PLEASE_TURN_ON_YOUR_INTERNET,mContext);
				}
				if (error != null) {
					String strMessage = error.getMessage();
					String strCode = error.getCode();
					Utils.showDialogAlert(strCode + ": "+ strMessage, mContext);
				} else if (!Utils.isEmpty(mPaymentToken)) {
					if (!Utils.isEmpty(lastDigits) && (strCardExpMonth > -1 && strCardExpYear > -1)) {
						showDialogAlert("SingleUseToken :" + "  " + mPaymentToken +"\n"+"Card Last Digit :"+" "+ lastDigits +"\n"+"Expiry Date :"+" "+strCardExpMonth+"- "+strCardExpYear, mContext);
					} else {
						 showDialogAlert("SingleUseToken :" + "  " + mPaymentToken,	mContext);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} // end of onPostExecute()

		/**
		 * Do In Background.
		 * @param args
		 * @return Single Use Token Object.
		 */
		@Override
		protected SingleUseToken doInBackground(String... args) {
			return singleUseTokenRequest();

		} // end of doInBackground()
	} // end of class SingleUseTokenTask

	/**
	 * This method will make a call to Single Use Token  API.
	 *
	 * @return Single Use Token Object.
	 */
	private SingleUseToken singleUseTokenRequest(){
		 int year = 0;
		 int month = 0;
	   client = new OptimalApiClient(getResources().getString(R.string.user_sbox),
                                getResources().getString(R.string.password_sbox),
                                Environment.TEST,
                                getResources().getString(R.string.merchantaccountno));

        // Retrieve values from Edit Text to process the single use token object.
	    getValuesfromEditText();

		try {
			SingleUseToken sObjResponse;
            // Check if Month field of Card Expiry
			if (!Utils.isEmpty(mMonth)) {
				month = Integer.parseInt(mMonth);
			}

            // Check if Year field of Card Expiry
			if (!Utils.isEmpty(mYear)) {
				year = Integer.parseInt(mYear);
			}

            // Make API call for single use token
            sObjResponse = client.customerVaultService().createSingleUseToken(
                    SingleUseToken.builder()
						.card()
                        	.holderName(mCardHolderName)
                            .cardNum(mCardNo)
                            .cardExpiry()
                            	.month(month)
                                .year(year)
                                .done()
                            .billingAddress()
                                .street(mStreet1)
                                .street2(mStreet2)
                                .city(mCity)
                                .country(mCountry)
                                .state(mState)
                                .zip(mZip)
                                .done()
                            .done()
                        .build());

			return sObjResponse;
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	} // end of singleUseTokenRequest()

	/**
	 * This method is used to get values from edit text
	 */
	private void getValuesfromEditText() {
		mCardHolderName = mNameOnCardEditText.getText().toString();
		mCardNo = mCardNoEditText.getText().toString();
		mMonth = mMonthEditText.getText().toString();
		mYear = mYearEditText.getText().toString();
		mStreet1 = mStreet1EditText.getText().toString();
		mStreet2 = mStreet2EditText.getText().toString();
		mCity = mCityEditText.getText().toString();
		mCountry = mCountryEditText.getText().toString();
		mState = mStateEditText.getText().toString();
		mZip = mZipEditText.getText().toString();
	} // end of getValuesfromEditText()

	/**
	 * This method is called when check the Internet
	 * 
	 * @return Boolean value for checking whether Internet setting is ON/OFF.
	 * 
	 */
	private boolean isCheckInternet() {
		boolean isNetworkAvailable = Utils.isNetworkAvailable(OptimalPayApplication.mApplicationContext);
		boolean isOnline = Utils.isOnline(OptimalPayApplication.mApplicationContext);
		if (isNetworkAvailable) {
			return true;
		} else if (isOnline) {
			return true;
		}
		return false;
	} // end of isCheckInternet()
     
	/**
	 * This method is called when back pressed finished the activity
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intentCheckOut = new Intent(CardDetails.this,Checkout.class);
		startActivity(intentCheckOut);
		finish();
	} // end of onBackPressed()

	/**
	 * This method is called show alert dialog
	 *
	 * @param alertMessage Alert message for the Dialog Box.
	 * @param context Context object.
	 */
	private void showDialogAlert(String alertMessage, Context context) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setMessage(alertMessage);
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(CardDetails.this,Checkout.class);
						startActivity(intent);
						finish();
					}
				});
		  alertDialog.show();
	} // end of showDialogAlert()

	/**
	 * On Destroy.
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(client != null){
			client = null;
		}
	} // end of onDestroy()
	
} // end of class CardDetails
