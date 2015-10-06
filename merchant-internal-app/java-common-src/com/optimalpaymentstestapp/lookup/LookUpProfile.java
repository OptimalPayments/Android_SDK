package com.optimalpaymentstestapp.lookup;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.optimalpaymentstestapp.Menu;
import com.optimalpaymentstestapp.OptimalPayApplication;
import com.optimalpaymentstestapp.R;
import com.optimalpaymentstestapp.UpdateCard;
import com.optimalpaymentstestapp.connection.HttpsServerConnection;
import com.optimalpaymentstestapp.utils.AppSettings;
import com.optimalpaymentstestapp.utils.Constants;
import com.optimalpaymentstestapp.utils.Utils;
import com.optimalpaymentstestapp.lookup.SearchResults;
/**
 * @author Manisha.Rani
 * 
 */
public class LookUpProfile extends Activity {
	// Button
	private Button mUpdateButton;
	private Button mCheckoutButton;
	private Button mBackButton;
	private Button mSearchButton;
	// String
	private String mProfileID;
	// EditText
	private EditText mProfileIDEditText;
	private Context mContext;
	private HttpsServerConnection mServer_conn;
	private HashMap<String,ArrayList<SearchResults>> mLookupMap;
	private ListView mLookUpProfileListView;
	private LookUpProfileAdapter mLookUpProfileAdapter;
	private static final String TAG = "LookUpProfile";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lookupprofile);
		init();
	}

	/**
	 * This method is called when initialize UI
	 * 
	 * @return Void
	 * 
	 */

	private void init() {
		mContext = this;
		mServer_conn = new HttpsServerConnection();
		mProfileIDEditText = (EditText) findViewById(R.id.et_profile_id);
		mUpdateButton = (Button) findViewById(R.id.btn_Update);
		mCheckoutButton = (Button) findViewById(R.id.btn_Checkout);
		mBackButton = (Button) findViewById(R.id.btn_back);
		mSearchButton = (Button) findViewById(R.id.btn_search);
		mUpdateButton.setOnClickListener(mClickListener);
		mCheckoutButton.setOnClickListener(mClickListener);
		mBackButton.setOnClickListener(mClickListener);
		mSearchButton.setOnClickListener(mClickListener);
		mLookUpProfileListView = (ListView) findViewById(R.id.lookup_list);
		mLookUpProfileListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mLookUpProfileListView.setOnItemClickListener(onItemClickListener);
		String strProfileID = AppSettings.getString(Constants.PROFILE_ID_SAVE, "");
		if(!Utils.isEmpty(strProfileID)){
		  mProfileIDEditText.setText(strProfileID);
		}
	}

	private HashMap<String,ArrayList<SearchResults>>sendRequestToServer() {
		excuteLookUpAsyncTask();
		return null;
	}

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,long arg3) {
		}
	};

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
			case R.id.btn_Update:
				if (LookUpProfileAdapter.isRadioButtonChecked) {
					final Intent intentCardDetails = new Intent(LookUpProfile.this,UpdateCard.class);
					intentCardDetails.putExtra(Constants.CARD_ID,LookUpProfileAdapter.mCardId);
					intentCardDetails.putExtra(Constants.PROFILE_ID,mProfileIDEditText.getText().toString());
					intentCardDetails.putExtra(Constants.LOOKUP_CARD_DATA, mLookupMap);
					startActivity(intentCardDetails);
					finish();
				} else {
					Utils.showDialogAlert(Constants.PLEASE_SELECT_CARD,mContext);
				}
				break;
			case R.id.btn_Checkout:
				if (LookUpProfileAdapter.isRadioButtonChecked) {
					final Intent intentCheckout = new Intent(LookUpProfile.this, CheckOutOnly.class);
					intentCheckout.putExtra(Constants.PAYMENT_TOKEN,LookUpProfileAdapter.mPaymentToken);
					intentCheckout.putExtra(Constants.RANDOM_MERCHANT_REFRENCE_NUMBER_LOOKUP_CHECKOUT,Utils.twelveDigitRandomAlphanumeric());
					startActivity(intentCheckout);
					finish();
				} else {
					Utils.showDialogAlert(Constants.PLEASE_SELECT_CARD,mContext);
				}
				break;
			case R.id.btn_back:
				final Intent intent = new Intent(LookUpProfile.this, Menu.class);
				startActivity(intent);
				finish();
				break;
			case R.id.btn_search:
				mLookupMap = sendRequestToServer();
				break;

			default:
				break;
			}
		}
	};

	/**
	 * This method is used to start async task
	 * 
	 * @return Void
	 * 
	 */
	private void excuteLookUpAsyncTask() {
		getValuesFromEditText();
		if(Utils.isEmpty(mProfileID)){
			Utils.showDialogAlert(Constants.PLEASE_ENTER_PROFILE_ID, mContext);
			return;
		}
		// Check Internet connection
		if (!isCheckInternet()) {
			Toast.makeText(getApplicationContext(),	Constants.PLEASE_TURN_ON_YOUR_INTERNET, Toast.LENGTH_LONG).show();
			return;
		}
	  	String urlString = getResources().getString(R.string.url_lookupprofile)+ mProfileID + "?fields=cards,addresses";
		new RequestSendViaAsynctask().execute(urlString);
	}

	//AsycTask for lookup
	public class Wrapper {
		public String responseString;
		public String url;
	}

	public class RequestSendViaAsynctask extends AsyncTask<String, String, Wrapper> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Utils.startProgressDialog(LookUpProfile.this,getString(R.string.loading_text));
		}

		@Override
		protected Wrapper doInBackground(String... params) {
			String url = params[0];
			String responseString = sendRequestToserver(url);
			Wrapper wrapper = new Wrapper();
			wrapper.responseString = responseString;
			wrapper.url = url;
			return wrapper;
		}

		protected void onPostExecute(Wrapper wrapper) {
			Utils.stopProgressDialog();
			if (wrapper != null && !Utils.isEmpty(wrapper.responseString)) {
				if(wrapper.responseString.contains(Constants.CONNECTION_REFUSED)){
					 Utils.showDialogAlert(Constants.PLEASE_TURN_ON_YOUR_INTERNET,mContext);
					 return;
				}
				mLookupMap = parseJSONObject(mServer_conn,wrapper.responseString);
				if(mLookupMap != null){
					mLookUpProfileAdapter = new LookUpProfileAdapter(mContext,mLookupMap.get("cardData"), 0);
					mLookUpProfileListView.setVisibility(View.VISIBLE);
					mLookUpProfileListView.setAdapter(mLookUpProfileAdapter);
					Log.e(TAG, "" + mLookupMap);
				}else{
					return;
				}
			}
		}
	}

	/**
	 * This method is called when send request to the the server
	 * 
	 * 
	 * @return void
	 * 
	 */

	private String sendRequestToserver(String url) {
		String responseString = null;
		HttpsServerConnection httpsConnObj = new HttpsServerConnection();
		try {
			String username = getResources().getString(R.string.user_servertoserver) ;
			String password = getResources().getString(R.string.password_servertoserver);// server side SDk
			String base64EncodedCredentials = Base64.encodeToString((username + ":" + password).getBytes("UTF-8"), Base64.NO_WRAP);
		    responseString = httpsConnObj.requestGetUrl(url,base64EncodedCredentials,Constants.GET);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseString;

	}

	/**
	 * This method is called when parse the LookUp response
	 * 
	 * @param ConnectionToServer,String
	 * @return ArrayList
	 * 
	 */

	private HashMap<String, ArrayList<SearchResults>> parseJSONObject(HttpsServerConnection server_conn, String responseString) {
		ArrayList<SearchResults> searchArrays = null;
		ArrayList<SearchResults> searchAddressesArrays = null;
	    String CardBillingAddressId = null;
		HashMap<String, ArrayList<SearchResults>> hashMap = new HashMap<String,ArrayList<SearchResults>>();
	  try {
			SearchResults sResults = null;
			final ArrayList<String> responseParams = new ArrayList<String>();
			if (!Utils.isEmpty(responseString)&& responseString.contains("error")) {
				mCheckoutButton.setVisibility(View.GONE);
				mUpdateButton.setVisibility(View.GONE);
				responseParams.add("error");
				@SuppressWarnings("unchecked")
				final Map<String, Object> map = server_conn.readAndParseJSON1(responseString, responseParams);
				final String errorDetails = (String) map.get("error");
				final ArrayList<String> responseParamsData = new ArrayList<String>();
				responseParamsData.add("code");
				responseParamsData.add("message");
				@SuppressWarnings("unchecked")
				final Map<String, Object> map1 = server_conn.readAndParseJSON1(errorDetails, responseParamsData);
				String strCode = (String) map1.get("code");
				String strMessage = (String) map1.get("message");
				showDialogAlert(strCode +": "+ strMessage, mContext);
				
			} else {
				 if(!Utils.isEmpty(responseString)){
				mCheckoutButton.setVisibility(View.VISIBLE);
				mUpdateButton.setVisibility(View.VISIBLE);
				searchArrays = new ArrayList<SearchResults>();
				searchAddressesArrays = new ArrayList<SearchResults>();
				responseParams.add("cards");
				responseParams.add("addresses");
				@SuppressWarnings("unchecked")
				final Map<String, Object> map = server_conn.readAndParseJSON(responseString, responseParams);
				try {
					final JSONArray strCardDetails = (JSONArray) map.get("cards");
					for (int count = 0; strCardDetails != null&& count < strCardDetails.length(); count++) {
						JSONObject jobject = strCardDetails.getJSONObject(count);
						JSONObject json = jobject.getJSONObject("cardExpiry");
					       if(jobject.has("defaultCardIndicator")){
					    	   CardBillingAddressId =  jobject.getString("billingAddressId");
							sResults = new SearchResults(
									jobject.getString("id"),
									jobject.getString("billingAddressId"),
									jobject.getString("status"),
									jobject.getString("holderName"),
									jobject.getString("cardBin"),
									jobject.getString("lastDigits"),
									json.getString("month"),
									json.getString("year"),
									jobject.getString("cardType"),
									jobject.getString("paymentToken"),
									jobject.getBoolean("defaultCardIndicator"));
					       }else{
							    sResults = new SearchResults(
							    	jobject.getString("id"),
									jobject.getString("billingAddressId"),
									jobject.getString("status"),
									jobject.getString("holderName"),
									jobject.getString("cardBin"),
									jobject.getString("lastDigits"),
									json.getString("month"),
									json.getString("year"),
									jobject.getString("cardType"),
									jobject.getString("paymentToken"));
					       }
					       
						searchArrays.add(sResults);
					}
					final JSONArray strAddressDetails = (JSONArray) map.get("addresses");
					for (int count = 0; strAddressDetails != null&& count < strAddressDetails.length(); count++) {
						JSONObject jobject = strAddressDetails.getJSONObject(count);
						  String addressId = jobject.getString("id");
						  if(CardBillingAddressId != null ){
							  if(addressId != null && CardBillingAddressId.equals(addressId) ){
						sResults = new SearchResults(
								jobject.getString("id"),
								jobject.getString("street"),
								jobject.getString("street2"),
								jobject.getString("city"),
								jobject.getString("country"),
								jobject.getString("state"),
								jobject.getString("zip"));

						searchAddressesArrays.add(sResults);
						  }
						  }
					}
					  hashMap.put("cardData", searchArrays);
					  hashMap.put("addressData", searchAddressesArrays);
						return hashMap;
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
			}catch (Exception e) {
			e.printStackTrace();
		}
	  		return null;
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
	 * This method is called when get values from edit text
	 * 
	 * @return void
	 * 
	 */
	private void getValuesFromEditText() {
		mProfileID = mProfileIDEditText.getText().toString();
	}

	/**
	 * show alert dialog
	 * 
	 * @return void
	 */

	private void showDialogAlert(String alertMessage, Context context) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setMessage(alertMessage);
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if(mLookUpProfileListView != null){
							mLookUpProfileListView.setVisibility(View.GONE);
						}

					}
				});
		  alertDialog.show();
	}
}
