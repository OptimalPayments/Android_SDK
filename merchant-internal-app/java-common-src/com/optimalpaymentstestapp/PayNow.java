package com.optimalpaymentstestapp;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.optimalpaymentstestapp.connection.HttpsServerConnection;
import com.optimalpaymentstestapp.utils.AppSettings;
import com.optimalpaymentstestapp.utils.Constants;
import com.optimalpaymentstestapp.utils.Utils;

/**
 * @author Manisha.Rani
 * 
 */
public class PayNow extends Activity {
	// Button
	private Button mPayButton;
	private Button mAuthorizeButton;
	private Button mBackButton;
	private Button mSkipButton;
	// EditText
	private EditText mMerchantRefEditText;
	private EditText mAmountEditText;
	private EditText mCustomerIDEditText;
	private EditText mFirstNameEditText;
	private EditText mLastNameEditText;
	private EditText mEmailEditText;
	private EditText mPhoneEditText;
	private EditText mSingleUseTokenEditText;
	// switch
	private Switch mSettleWithAuthSwitch;
	private boolean isSwitchButtonStatus = false;
	// context
	private Context mContext;
	private HttpsServerConnection mServer_conn;
	// LinearLayout
	private LinearLayout mSaveProfileLayout;
	private LinearLayout mSingleUseTokenLayout;

	// CheckBox
	private CheckBox mSaveProfileCheckBox;
	// String
	private String mMerchantRefNo;
	private String mAmount;
	private String mCustomerID;
	private String mFirstName;
	private String mLastName;
	private String mEmail;
	private String mPhone;
	private String mSingleUseToken;
	private String mStatus;
	// boolean
	private boolean isCheckedChkbox = false;

	protected static final int PAYMENT_TOKEN = 0;
	private static final String TAG = "CheckOut";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paynow);
		init();
	}

	/**
	 * This method is called when initialize UI
	 * 
	 * @return Void
	 * 
	 */
	private void init() {
		Log.e(TAG, "Inside init");

		mContext = this;
		mServer_conn = new HttpsServerConnection();
		// save profile
		mSaveProfileCheckBox = (CheckBox) findViewById(R.id.chkSaveProfile);
		mSaveProfileLayout = (LinearLayout) findViewById(R.id.saveprofilelayout);
		mSingleUseTokenLayout = (LinearLayout) findViewById(R.id.singleusetokenlayout);
		mCustomerIDEditText = (EditText) findViewById(R.id.et_merchant_customerid);
		mFirstNameEditText = (EditText) findViewById(R.id.et_first_name);
		mLastNameEditText = (EditText) findViewById(R.id.et_last_name);
		mEmailEditText = (EditText) findViewById(R.id.et_email);
		mPhoneEditText = (EditText) findViewById(R.id.et_phone);
		mSingleUseTokenEditText = (EditText) findViewById(R.id.et_singleusetoken);

		mMerchantRefEditText = (EditText) findViewById(R.id.et_merchantref);
		mAmountEditText = (EditText) findViewById(R.id.et_amount);
		mSettleWithAuthSwitch = (Switch) findViewById(R.id.switch_settle);

		mBackButton = (Button) findViewById(R.id.btn_back);
		mAuthorizeButton = (Button) findViewById(R.id.btn_authorize);
		mPayButton = (Button) findViewById(R.id.btn_Pay);
		mSkipButton = (Button) findViewById(R.id.btn_skip);

		mBackButton.setOnClickListener(mClickListener);
		mAuthorizeButton.setOnClickListener(mClickListener);
		mPayButton.setOnClickListener(mClickListener);
		mSkipButton.setOnClickListener(mClickListener);

		mMerchantRefEditText.setBackgroundColor(Color.GRAY);

		Intent intent = getIntent();
		String merchantRefNo = intent
				.getStringExtra(Constants.RANDOM_MERCHANT_REFRENCE_NUMBER);
		if (!Utils.isEmpty(merchantRefNo)) {
			mMerchantRefEditText.setText(merchantRefNo);
		}

		addListenerOnCheckBox();
		statusOfSwitchButton(mSettleWithAuthSwitch);
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
			case R.id.btn_back:
				mAuthorizeButton.setVisibility(View.GONE);
				mSaveProfileCheckBox.setVisibility(View.GONE);
				mPayButton.setVisibility(View.VISIBLE);
				mSkipButton.setVisibility(View.VISIBLE);
				isCheckedChkbox = false;
				Intent intentCheckOut = new Intent(PayNow.this, Checkout.class);
				startActivity(intentCheckOut);
				finish();
				break;
			case R.id.btn_Pay:
				buttonPayOnClick();
				break;
			case R.id.btn_authorize:
				buttonAuthorizeClick();
				break;
			case R.id.btn_skip:
				buttonSkipClick();
				break;

			default:
				break;
			}
		}

	};

	/**
	 * This method is called when Skip button is pressed
	 * 
	 * @return Void
	 * 
	 */
	private void buttonSkipClick() {
		mSingleUseTokenLayout.setVisibility(View.VISIBLE);
		mSingleUseTokenEditText.setText("");
		mAuthorizeButton.setVisibility(View.VISIBLE);
		mSaveProfileCheckBox.setVisibility(View.VISIBLE);
		mPayButton.setVisibility(View.GONE);
		mSkipButton.setVisibility(View.GONE);
	}

	/**
	 * This method is called when authorize button is pressed
	 * 
	 * @return Void
	 * 
	 */
	private void buttonAuthorizeClick() {
		getValuesFromEditText();
		if (Utils.isEmpty(mAmount)) {
			Utils.showDialogAlert(Constants.PLEASE_ENTER_AMOUNT, mContext);
			return;
		}
		if (Utils.isEmpty(mSingleUseToken)) {
			Utils.showDialogAlert(Constants.PLEASE_ENTER_SINGLE_USE_TOKEN,
					mContext);
			return;
		}
		if (isCheckedChkbox) {
			if (Utils.isEmpty(mCustomerID)) {
				Utils.showDialogAlert(Constants.PLEASE_ENTER_CUSTOMER_ID,
						mContext);
				return;
			}
		}
		// Check Internet connection
		if (!isCheckInternet()) {
			Toast.makeText(getApplicationContext(),
					Constants.PLEASE_TURN_ON_YOUR_INTERNET, Toast.LENGTH_LONG)
					.show();
			return;
		}
		String urlString = String.format(
				getResources().getString(R.string.url_authorize),
				getResources().getString(R.string.merchantaccountno)); // 89983449

		new RequestSendViaAsynctask().execute(urlString);

	}

	// Asyn task for Authorize and create profile

	public class Wrapper {
		public String responseString;
		public String url;
	}

	public class RequestSendViaAsynctask extends
			AsyncTask<String, String, Wrapper> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Utils.startProgressDialog(PayNow.this,
					getString(R.string.loading_text));
		}

		@Override
		protected Wrapper doInBackground(String... params) {
			String url = params[0];
			String responseString;
			Wrapper wrapper = null;
			responseString = sendRequestToserver(url);
			wrapper = new Wrapper();
			wrapper.responseString = responseString;
			wrapper.url = url;

			return wrapper;
		}

		protected void onPostExecute(Wrapper wrapper) {
			Utils.stopProgressDialog();
			if (wrapper != null && !Utils.isEmpty(wrapper.responseString)) {
				if (wrapper.responseString
						.contains(Constants.CONNECTION_REFUSED)) {
					Utils.showDialogAlert(
							Constants.PLEASE_TURN_ON_YOUR_INTERNET, mContext);
					return;
				}
			}
			if (wrapper != null
					&& String.format(
							getResources().getString(R.string.url_authorize),
							getResources()
									.getString(R.string.merchantaccountno))
							.equalsIgnoreCase(wrapper.url))
				parseAuthorize(mServer_conn, wrapper.responseString);
			else if (wrapper != null
					&& getResources().getString(R.string.url_create_profile)
							.equalsIgnoreCase(wrapper.url))
				parseCreateProfile(mServer_conn, wrapper.responseString);
		}
	}

	/**
	 * This method is called when send request to server
	 * 
	 * @return String
	 */
	private String sendRequestToserver(String url) {
		HttpsServerConnection httpsConneObj = new HttpsServerConnection();
		String responseString = null;
		getValuesFromEditText();
		String base64EncodedCredentials = null;
		try {
			if (String.format(getResources().getString(R.string.url_authorize),
					getResources().getString(R.string.merchantaccountno))
					.equalsIgnoreCase(url)) {

				String username = getResources().getString(R.string.user_sbox);
				String password = getResources().getString(
						R.string.password_sbox);

				try {
					base64EncodedCredentials = Base64
							.encodeToString((username + ":" + password)
									.getBytes("UTF-8"), Base64.NO_WRAP);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				responseString = httpsConneObj.requestUrl(
						base64EncodedCredentials, url,
						createAuthorizeJsonObject(), Constants.POST);

			} else if (getResources().getString(R.string.url_create_profile)
					.equalsIgnoreCase(url)) {
				String username = getResources().getString(
						R.string.user_servertoserver);
				String password = getResources().getString(
						R.string.password_servertoserver);

				try {
					base64EncodedCredentials = Base64
							.encodeToString((username + ":" + password)
									.getBytes("UTF-8"), Base64.NO_WRAP);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				responseString = httpsConneObj.requestUrl(
						base64EncodedCredentials, url,
						createProfileJsonObject(), Constants.POST);
				Log.e(TAG, "responseString" + responseString);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseString;
	}

	/**
	 * This method is used for create profile JsonObject
	 * 
	 * @return JSONObject
	 * 
	 * 
	 */
	private JSONObject createProfileJsonObject() {
		JSONObject json = new JSONObject();
		try {
			json.put("merchantCustomerId", mCustomerID);
			json.put("locale", "en_US");
			json.put("firstName", mFirstName);
			json.put("lastName", mLastName);
			json.put("email", mEmail);
			json.put("phone", mPhone);
			JSONObject card = new JSONObject();
			card.put("singleUseToken", mSingleUseToken);
			json.put("card", card);

			return json;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * This method is used for create authorize JsonObject
	 * 
	 * @return JSONObject
	 * 
	 * 
	 */
	private JSONObject createAuthorizeJsonObject() {
		JSONObject json = new JSONObject();
		try {
			json.put("merchantRefNum", mMerchantRefNo);
			json.put("amount", mAmount);
			json.put("settleWithAuth", false);
			JSONObject card = new JSONObject();
			card.put("paymentToken", mSingleUseToken);

			json.put("description", "Hand bag - Big");
			// json.put("customerIp","10.10.345.114");
			json.put("customerIp", "14.140.42.67");
			json.put("card", card);

			return json;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * This method is called when parse the authorize response
	 * 
	 * @return void
	 * 
	 */
	private void parseAuthorize(HttpsServerConnection server_conn,
			String responseString) {
		// Parse Json Object
		final ArrayList<String> responseParams = new ArrayList<String>();
		responseParams.add("id");
		if (!Utils.isEmpty(responseString) && responseString.contains("error")) {
			responseParams.add("error");
			@SuppressWarnings("unchecked")
			final Map<String, Object> map = server_conn.readAndParseJSON1(
					responseString, responseParams);
			try {
				final String errorDetails = (String) map.get("error");
				final ArrayList<String> responseParamsData = new ArrayList<String>();
				responseParamsData.add("code");
				responseParamsData.add("message");
				@SuppressWarnings("unchecked")
				final Map<String, Object> map1 = server_conn.readAndParseJSON1(
						errorDetails, responseParamsData);
				String strCode = (String) map1.get("code");
				String strMessage = (String) map1.get("message");
				Utils.showDialogAlert(strCode + ": " + strMessage, mContext);

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (isCheckedChkbox) {
				mStatus = checkedAuthorizeStatus(responseParams, server_conn,
						responseString);
				createProfileRequest();

			} else {
				String strStatus = checkedAuthorizeStatus(responseParams,
						server_conn, responseString);
				if (!Utils.isEmpty(strStatus) && strStatus.equals("COMPLETED")) {
					showDialogAlert(Constants.AutHORIZATION_SUCCESSFUL,
							mContext);
				}

			}
		}

	}

	private String checkedAuthorizeStatus(ArrayList<String> responseParams,
			HttpsServerConnection server_conn, String responseString) {
		responseParams.add("status");
		@SuppressWarnings("unchecked")
		Map<String, Object> map = server_conn.readAndParseJSON(responseString,
				responseParams);
		String strStatus = (String) map.get("status");
		return strStatus;
	}

	/**
	 * This method is used for createprofile request
	 * 
	 * @return boolean
	 * 
	 * 
	 */
	private void createProfileRequest() {
		getValuesFromEditText();
		// Check Internet connection
		if (!isCheckInternet()) {
			Toast.makeText(getApplicationContext(),
					Constants.PLEASE_TURN_ON_YOUR_INTERNET, Toast.LENGTH_LONG)
					.show();
			return;
		}
		new RequestSendViaAsynctask().execute(getResources().getString(
				R.string.url_create_profile));
	}

	/**
	 * This method is called when parse the create profile response
	 * 
	 * @return void
	 * 
	 */
	private void parseCreateProfile(HttpsServerConnection server_conn,
			String responseString) {
		// Parse Json Object
		final ArrayList<String> responseParams = new ArrayList<String>();
		responseParams.add("id");
		if (!Utils.isEmpty(responseString) && responseString.contains("error")) {
			responseParams.add("error");
			@SuppressWarnings("unchecked")
			final Map<String, Object> map = server_conn.readAndParseJSON1(
					responseString, responseParams);
			try {
				final String errorDetails = (String) map.get("error");
				final ArrayList<String> responseParamsData = new ArrayList<String>();
				responseParamsData.add("code");
				responseParamsData.add("message");
				@SuppressWarnings("unchecked")
				final Map<String, Object> map1 = server_conn.readAndParseJSON1(
						errorDetails, responseParamsData);
				String strCode = (String) map1.get("code");
				String strMessage = (String) map1.get("message");
				if (!Utils.isEmpty(mStatus) && mStatus.equals("COMPLETED")) {

					customDialog(Constants.AutHORIZATION_SUCCESSFUL,
							"Create profile error:", strCode + ": "
									+ strMessage);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			responseParams.add("id");
			@SuppressWarnings("unchecked")
			Map<String, Object> map = server_conn.readAndParseJSON(
					responseString, responseParams);
			String strprofileID = (String) map.get("id");
			if (!Utils.isEmpty(strprofileID)) {
				AppSettings.setString(Constants.PROFILE_ID_SAVE, strprofileID);
				showDialogAlert(Constants.CREATE_PROFILE_SUCCESSFUL, mContext);
			}
		}
	}

	/**
	 * This is a call back method to get the message form other Activity
	 * 
	 * @return void
	 * 
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case PAYMENT_TOKEN:
			if (data != null) {
				String strSingleUseToken = data
						.getStringExtra(Constants.GET_PAYMENT_TOKEN);
				String strMerchantRefrenceNo = data
						.getStringExtra(Constants.RANDOM_MERCHANT_REFRENCE_NUMBER_SINGLEUSETOKEN);
				if (!Utils.isEmpty(strSingleUseToken)) {
					if (!Utils.isEmpty(strMerchantRefrenceNo)) {
						if(mMerchantRefEditText != null){
						 mMerchantRefEditText.setText(strMerchantRefrenceNo);
						}
					}
					mSingleUseTokenEditText.setText(strSingleUseToken);
					Toast.makeText(getApplicationContext(),
							"PAYMENT TOKEN:" + " " + strSingleUseToken,
							Toast.LENGTH_LONG).show();
					mSaveProfileCheckBox.setVisibility(View.VISIBLE);
					mAuthorizeButton.setVisibility(View.VISIBLE);
					mPayButton.setVisibility(View.GONE);
					mSkipButton.setVisibility(View.GONE);
					mSingleUseTokenLayout.setVisibility(View.VISIBLE);
				} else {
					String strError = data.getStringExtra(Constants.ERROR);
					mAuthorizeButton.setVisibility(View.GONE);
					mSaveProfileCheckBox.setVisibility(View.GONE);
					mPayButton.setVisibility(View.VISIBLE);
					Utils.showDialogAlert(strError, mContext);
				}
			}
			break;

		}
	}

	/**
	 * This method is called when Pay now button is pressed
	 * 
	 * @return Void
	 * 
	 */
	private void buttonPayOnClick() {
		mPayButton.setVisibility(View.VISIBLE);
		String strAmount = mAmountEditText.getText().toString();
		if (!Utils.isEmpty(strAmount)
				&& new BigInteger(strAmount).longValue() > 0) {
			Intent serverIntent = new Intent(PayNow.this, CardDetails.class);
			startActivityForResult(serverIntent, PAYMENT_TOKEN);
		} else {
			Utils.showDialogAlert(Constants.PLEASE_ENTER_AMOUNT, mContext);
		}

	}

	private void statusOfSwitchButton(Switch mSettleWithAuthSwitch) {
		mSettleWithAuthSwitch.setChecked(false);
		// attach a listener to check for changes in state
		mSettleWithAuthSwitch
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						if (isChecked) {
							isSwitchButtonStatus = true;
						} else {
							isSwitchButtonStatus = false;
						}
					}

				});

		// check the current state before we display the screen
		if (mSettleWithAuthSwitch.isChecked()) {
			isSwitchButtonStatus = true;
		} else {
			isSwitchButtonStatus = false;
		}

		AppSettings.setBoolean(Constants.SWITCH_BUTTON_STATUS,
				isSwitchButtonStatus);
	}

	/**
	 * This method is called when check the Internet
	 * 
	 * @return void
	 * 
	 */

	private boolean isCheckInternet() {
		boolean isNetworkAvailable = Utils
				.isNetworkAvailable(OptimalPayApplication.mApplicationContext);
		boolean isOnline = Utils
				.isOnline(OptimalPayApplication.mApplicationContext);
		if (isNetworkAvailable) {
			return true;
		} else if (isOnline) {
			return true;
		}
		return false;
	}

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
		Intent intentCheckOut = new Intent(PayNow.this, Checkout.class);
		startActivity(intentCheckOut);
		finish();
	}

	/**
	 * This method is used to visible layout
	 * 
	 * @return void
	 * 
	 * 
	 */
	private void addListenerOnCheckBox() {
		mSaveProfileCheckBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (((CheckBox) v).isChecked()) {
					mCustomerIDEditText.setText(Utils.eightDigitRandomNumber());
					isCheckedChkbox = true;
					mSaveProfileLayout.setVisibility(View.VISIBLE);
				} else {
					isCheckedChkbox = false;
					mSaveProfileLayout.setVisibility(View.GONE);
				}

			}
		});

	}

	/**
	 * This method is used to get values from edit text
	 * 
	 * @return void
	 * 
	 * 
	 */
	private void getValuesFromEditText() {
		mAmount = mAmountEditText.getText().toString();
		mMerchantRefNo = mMerchantRefEditText.getText().toString();
		mCustomerID = mCustomerIDEditText.getText().toString();
		mFirstName = mFirstNameEditText.getText().toString();
		mLastName = mLastNameEditText.getText().toString();
		mEmail = mEmailEditText.getText().toString();
		mPhone = mPhoneEditText.getText().toString();
		mSingleUseToken = mSingleUseTokenEditText.getText().toString();
	}

	/**
	 * This method is used to show alert dialog
	 * 
	 * @return void
	 */
	private void showDialogAlert(String alertMessage, Context context) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setMessage(alertMessage);
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(PayNow.this, Menu.class);
						startActivity(intent);
						finish();
					}
				});
		alertDialog.show();
	}

	/**
	 * This method is used for email validation
	 * 
	 * @return boolean
	 * 
	 * 
	 */
	@SuppressWarnings("unused")
	private boolean emailValidation() {

		try {
			String email = mEmailEditText.getText().toString().trim();
			String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

			if (email.matches(emailPattern)) {
				return true;
			} else {
				Toast.makeText(getApplicationContext(),
						Constants.PLEASE_ENTER_VALID_EMIL_ADDRESS,
						Toast.LENGTH_SHORT).show();
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * This method is used show Custom dialog
	 * 
	 * @return Void
	 */
	private void customDialog(String strauthorizationStatus,
			String strCreateProfileError, String strCreateProfileStatus) {

		final Dialog dialog = new Dialog(mContext);
		dialog.setContentView(R.layout.customdialog);
		dialog.setTitle(strauthorizationStatus);
		TextView text = (TextView) dialog.findViewById(R.id.text);
		TextView texterror = (TextView) dialog.findViewById(R.id.texterror);
		text.setText(strCreateProfileStatus);
		texterror.setText(strCreateProfileError);
		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
}
