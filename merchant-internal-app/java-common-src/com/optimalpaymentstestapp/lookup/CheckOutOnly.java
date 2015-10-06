package com.optimalpaymentstestapp.lookup;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.optimalpaymentstestapp.Menu;
import com.optimalpaymentstestapp.OptimalPayApplication;
import com.optimalpaymentstestapp.R;
import com.optimalpaymentstestapp.connection.HttpsServerConnection;
import com.optimalpaymentstestapp.utils.AppSettings;
import com.optimalpaymentstestapp.utils.Constants;
import com.optimalpaymentstestapp.utils.Utils;
/**
 * @author Manisha.Rani
 * 
 */
public class CheckOutOnly extends Activity {
	// Button
	private Button mAuthorizeButton;
	private Button mBackButton;
	// EditText
	private EditText mMerchantRefEditText;
	private EditText mAmountEditText;
	// switch
	private Switch mSettleWithAuthSwitch;
	private boolean isSwitchButtonStatus = false;
	// context
	private Context mContext;
	// ConnectionToServer
	private HttpsServerConnection mServer_conn;
	
	protected static final int PAYMENT_TOKEN = 0;
	private static final String TAG = "CheckOut";
	//String
	private String mPaymentToken;
	private String mMerchantRefNo;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authorize);
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
		mBackButton = (Button) findViewById(R.id.btn_back);
		mAuthorizeButton = (Button) findViewById(R.id.btn_authorize);
		mBackButton.setOnClickListener(mClickListener);
		mAuthorizeButton.setOnClickListener(mClickListener);
		mMerchantRefEditText = (EditText) findViewById(R.id.et_merchantref);
		mAmountEditText = (EditText) findViewById(R.id.et_amount);
		mSettleWithAuthSwitch = (Switch) findViewById(R.id.switch_settle);
		
		mMerchantRefEditText.setBackgroundColor(Color.GRAY);
		
		Intent intent = getIntent(); 
		mPaymentToken = intent.getStringExtra(Constants.PAYMENT_TOKEN);
		mMerchantRefNo = intent.getStringExtra(Constants.RANDOM_MERCHANT_REFRENCE_NUMBER_LOOKUP_CHECKOUT);
		if(!Utils.isEmpty(mMerchantRefNo)){
			mMerchantRefEditText.setText(mMerchantRefNo);
		}
		
		getValuesFromPrf();
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
				// saveDataInSharedPrefForFutureUse();
				final Intent intent = new Intent(CheckOutOnly.this,Menu.class);
				startActivity(intent);
				finish();
				
				break;
			case R.id.btn_authorize:
				buttonAuthorizeClick();
				break;

			default:
				break;
			}
		}
	};

	/**
	 * This method is called when authorize button is pressed
	 * 
	 * @return Void
	 * 
	 */
	private void buttonAuthorizeClick(){
		//mMerchantRefEditText.setText(Utils.twelveDigitRandomAlphanumeric());
		 String strAmount = mAmountEditText.getText().toString();
		 if(Utils.isEmpty(strAmount)){ 
			Utils.showDialogAlert(Constants.PLEASE_ENTER_AMOUNT, mContext);
			 return;
		 }
		//Check Internet connection
		if (!isCheckInternet()) {
			Toast.makeText(getApplicationContext(),	Constants.PLEASE_TURN_ON_YOUR_INTERNET,Toast.LENGTH_LONG).show();
			return;
		}
		String urlString = String.format(getResources().getString(R.string.url_authorize),getResources().getString(R.string.merchantaccountno)); //89983449
		new RequestSendViaAsynctask().execute(urlString);
	}
	
	//AsyncTask 
	public class Wrapper
	{
	    public String responseString;
	    public String url;
	}
	public class RequestSendViaAsynctask extends AsyncTask<String, String, Wrapper> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Utils.startProgressDialog(CheckOutOnly.this, getString(R.string.loading_text));
		}

		@Override
		protected Wrapper doInBackground(String... params) {
			String url = params[0];
			String responseString = sendRequestToserver(url);
			Wrapper wrapper = new Wrapper();
			wrapper.responseString =  responseString;
			wrapper.url = url;
			return wrapper;
		}

		protected void onPostExecute(Wrapper wrapper) {
			Utils.stopProgressDialog();
			if(wrapper != null && !Utils.isEmpty(wrapper.responseString)){
				if(wrapper.responseString.contains(Constants.CONNECTION_REFUSED)){
					 Utils.showDialogAlert(Constants.PLEASE_TURN_ON_YOUR_INTERNET,mContext);
					 return;
				}
			 parseAuthorizeJSONObject(mServer_conn, wrapper.responseString);
			}
		}
	}
	
	/**
	 * This method is called when send request to server
	 * @return String
	 * 
	 */
	private String sendRequestToserver(String url) {
		HttpsServerConnection httpsConnObj = new HttpsServerConnection();
		String merchantRefNum = mMerchantRefEditText.getText().toString();
		String strAmount = mAmountEditText.getText().toString();
		JSONObject jsonObj = new JSONObject();
		String base64EncodedCredentials = null;
		try {
			jsonObj.put("merchantRefNum",merchantRefNum);
			jsonObj.put("amount",strAmount);
			jsonObj.put("settleWithAuth","false");
			JSONObject card= new JSONObject();
			card.put("paymentToken",mPaymentToken);
			jsonObj.put("card",card);
			
			String username = getResources().getString(R.string.user_sbox);
			String password = getResources().getString(R.string.password_sbox);
			try {
				//base64UserIDPassword = Base64.encodeToString(userIDPassword.getBytes(), Base64.NO_WRAP);
				base64EncodedCredentials = Base64.encodeToString((username + ":" + password).getBytes("UTF-8"), Base64.NO_WRAP);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		String responseString = null;
		try {
			//responseString = mServer_conn.postDataUsingJsonObject(base64UserIDPassword, url, json);
			responseString = httpsConnObj.requestUrl(base64EncodedCredentials, url, jsonObj,Constants.POST);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseString;
	}
		
	/**
	 * This method is called when parse the authorize response
	 * @return void
	 * 
	 */
	private void parseAuthorizeJSONObject(HttpsServerConnection server_conn,String responseString) {
		// Parse Json Object
		final ArrayList<String> responseParams = new ArrayList<String>();
		responseParams.add("id");
		if(!Utils.isEmpty(responseString)&&responseString.contains("error")){
		responseParams.add("error");
		@SuppressWarnings("unchecked")
		final Map<String, Object> map = server_conn.readAndParseJSON1(responseString,responseParams);
			try {
				final String errorDetails = (String) map.get("error");
				final ArrayList<String> responseParamsData = new ArrayList<String>();
				responseParamsData.add("code");
				responseParamsData.add("message");
				@SuppressWarnings("unchecked")
				final Map<String, Object> map1 = server_conn.readAndParseJSON1(errorDetails,responseParamsData);
				String strCode = (String) map1.get("code");
				String strMessage = (String) map1.get("message");
				Utils.showDialogAlert(strCode+": "+strMessage, mContext);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			responseParams.add("status");
			@SuppressWarnings("unchecked")
			Map<String, Object> map = server_conn.readAndParseJSON(responseString, responseParams);
			String strStatus = (String) map.get("status");
			if(!Utils.isEmpty(strStatus)&&strStatus.equals("COMPLETED")){
				showDialogAlert(Constants.AutHORIZATION_SUCCESSFUL , mContext);
			}
		}
		
	}
	/**
	 * This method is called when save values in shared preference
	 * 
	 * @return Void
	 * 
	 */
	/*private void saveDataInSharedPrefForFutureUse() {
		String strMerchantRef = mMerchantRefEditText.getText().toString();
		AppSettings.setString(Constants.MERCHANT_REF, strMerchantRef);
		String strAmount = mAmountEditText.getText().toString();
		AppSettings.setString(Constants.AMOUNT, strAmount);
	}*/
	
	/**
	 * This method is called when getValues from shared preference
	 * 
	 * @return Void
	 * 
	 */
	private void getValuesFromPrf() {
		/*String strMerchantRef = AppSettings.getString(Constants.MERCHANT_REF,"");
		if (!Utils.isEmpty(strMerchantRef)) {
			mMerchantRefEditText.setText(strMerchantRef);
		}*/

	/*	String strAmount = AppSettings.getString(Constants.AMOUNT, "");
		if (!Utils.isEmpty(strAmount)) {
			mAmountEditText.setText(strAmount);
		}*/
        
	}

	private void statusOfSwitchButton(Switch mSettleWithAuthSwitch) {
		mSettleWithAuthSwitch.setChecked(false);
		// attach a listener to check for changes in state
		mSettleWithAuthSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

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

		AppSettings.setBoolean(Constants.SWITCH_BUTTON_STATUS,isSwitchButtonStatus);
	}
	
	
	/**
	 * This method is called when check the Internet
	 * 
	 * @return void
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
		Intent intentCheckOut = new Intent(CheckOutOnly.this, Menu.class);
		startActivity(intentCheckOut);
		finish();
	}
	/**
	 * This method is used to show alert dialog
	 * @return void
	 * 
	 * 
	 */
	private void showDialogAlert(String alertMessage, Context context) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setMessage(alertMessage);
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
                     Intent intent = new Intent(CheckOutOnly.this,Menu.class);
                     startActivity(intent);
                     finish();
					}
				});
		  alertDialog.show();
	}
}
