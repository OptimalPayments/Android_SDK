package com.optimalpaymentstestapp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.optimalpayments.Environment;
import com.optimalpayments.OptimalApiClient;
import com.optimalpayments.common.CardExpiry;
import com.optimalpayments.common.Error;
import com.optimalpayments.common.OptimalException;
import com.optimalpayments.customervault.BillingAddress;
import com.optimalpayments.customervault.Card;
import com.optimalpayments.customervault.SingleUseToken;
import com.optimalpaymentstestapp.connection.HttpsServerConnection;
import com.optimalpaymentstestapp.lookup.LookUpProfile;
import com.optimalpaymentstestapp.lookup.SearchResults;
import com.optimalpaymentstestapp.utils.Constants;
import com.optimalpaymentstestapp.utils.Utils;
/**
 * @author Manisha.Rani
 * 
 */

public class UpdateCard extends Activity {
    protected static final String TAG = "UpdateCard";
    // Button
    private Button mOKButton;
    private Button mBackButton;
    private Button mCancelButton;
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
    private HttpsServerConnection mServer_conn;
    private String mPaymentToken;
    protected static final int PAYMENT_TOKEN = 0;
    private String mCardID;
    private String mProfileID;
	private OptimalApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updatecard);
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
        mBackButton = (Button) findViewById(R.id.btn_back);
        mOKButton = (Button) findViewById(R.id.btn_Confirm);
        mCancelButton = (Button) findViewById(R.id.btn_Cancle);
        mBackButton.setOnClickListener(mClickListener);
        mOKButton.setOnClickListener(mClickListener);
        mCancelButton.setOnClickListener(mClickListener);

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

        Intent intent = getIntent();
        mCardID = intent.getStringExtra(Constants.CARD_ID);
        mProfileID = intent.getStringExtra(Constants.PROFILE_ID);
          
        fillValuesIntoEditText(intent );
        mCardNoEditText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	mCardNoEditText.setText("");
            }
        });
    }
    
    private void fillValuesIntoEditText(  Intent intent ){
    	@SuppressWarnings("unchecked")
		HashMap<String, ArrayList<SearchResults>> lookupMapData = (HashMap<String,ArrayList<SearchResults>>)intent.getSerializableExtra(Constants.LOOKUP_CARD_DATA);
    	  ArrayList<SearchResults> lookupCardData = lookupMapData != null ?lookupMapData.get("cardData"):null;
         if(lookupCardData != null && lookupCardData.size() >0){
         	SearchResults searchResults = (SearchResults)lookupCardData.get(0);
         	if(searchResults!=null){
         		mNameOnCardEditText.setText(searchResults.getHolderName() !=null ? searchResults.getHolderName():"");
         		mMonthEditText.setText(searchResults.getMonth() !=null ? searchResults.getMonth() :"");
         		mYearEditText.setText(searchResults.getYear()!=null ? searchResults.getYear() :"");
         		mCardNoEditText.setText(searchResults.getLastDigits()!=null ? "XXXX-XXXX-XXXX-"+searchResults.getLastDigits() :"");
         		
         	}
         }
         ArrayList<SearchResults> lookupAddressData = lookupMapData != null ?lookupMapData.get("addressData"):null;
         if(lookupAddressData != null && lookupAddressData.size() >0){
          	SearchResults searchResults = (SearchResults)lookupAddressData.get(0);
          	if(searchResults!=null){
          		mStreet1EditText.setText(searchResults.getStreet()!=null ? searchResults.getStreet() :"");
         		mStreet2EditText.setText(searchResults.getStreet2()!=null ? searchResults.getStreet2() :"");
         		mCityEditText.setText(searchResults.getCity()!=null ? searchResults.getCity() :"");
         		mStateEditText.setText(searchResults.getState()!=null ? searchResults.getState() :"");
         		mCountryEditText.setText(searchResults.getCountry()!=null ? searchResults.getCountry() :"");
         		mZipEditText.setText(searchResults.getZip()!=null ? searchResults.getZip() :"");
          	}
          }
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
                    Intent intentCheckOut = new Intent(UpdateCard.this, LookUpProfile.class);
                    startActivity(intentCheckOut);
                    finish();
                    break;
                case R.id.btn_Confirm:
                    buttonConfirmClick();
                    break;
                case R.id.btn_Cancle:
                    final Intent intentCancel = new Intent(UpdateCard.this,LookUpProfile.class);
                    startActivity(intentCancel);
                    finish();
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * This method is used to excute async task
     *
     * @return Void
     *
     */
    private void buttonConfirmClick() {
        // Check Internet connection
        if(Utils.isEmpty(mProfileID)){
            Toast.makeText(getApplicationContext(), Constants.PROFILE_ID_IS_EMPTY, Toast.LENGTH_LONG).show();
            return;
        }
        if(Utils.isEmpty(mCardID)){
            Toast.makeText(getApplicationContext(), Constants.CARD_ID_IS_EMPTY, Toast.LENGTH_LONG).show();
            return;
        }
        try {
            if (!isCheckInternet()) {
                Toast.makeText(getApplicationContext(),Constants.PLEASE_TURN_ON_YOUR_INTERNET,Toast.LENGTH_LONG).show();
                return;
            }
            //Excute AyscnTask for SingleUseToken
            new SingleUseTokenTask().execute();

        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }
    //Asyn task for SingleUseToken

    class SingleUseTokenTask extends AsyncTask<String, Void, SingleUseToken> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.startProgressDialog(UpdateCard.this,getString(R.string.loading_text));
        }
        @Override
        protected void onPostExecute(SingleUseToken singleUseTokenObject) {
            super.onPostExecute(singleUseTokenObject);
            Utils.stopProgressDialog();
            try {
				mPaymentToken = singleUseTokenObject.getPaymentToken();
				Error error = singleUseTokenObject.getError();
				if (!Utils.isEmpty(mPaymentToken)) {
				    createProfileRequest();
				} else if (error != null) {
				    String strMessage = error.getMessage();
				    String strCode = error.getCode();
				    Utils.showDialogAlert(strCode + ": " + strMessage, mContext);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
        }

        @Override
        protected SingleUseToken doInBackground(String... args) {
            SingleUseToken singleUseTokenObject = singleUseTokenRequest();
            return singleUseTokenObject;

        }
    }

    /**
     * This method is used to return single token object
     *
     * @return SingleUseToken
     *
     */
    private SingleUseToken singleUseTokenRequest(){
         client = new OptimalApiClient(getResources().getString(R.string.user_sbox), getResources().getString(R.string.password_sbox), Environment.TEST,getResources().getString(R.string.merchantaccountno));
         getValuesfromEditText();
         SingleUseToken sObj = new SingleUseToken();
        Card card = new Card();
        CardExpiry exp = new CardExpiry();
        BillingAddress billing = new BillingAddress();
        card.setHolderName(mCardHolderName);
        card.setCardNum(mCardNo);
        if(!Utils.isEmpty(mMonth)){
        exp.setMonth(Integer.parseInt(mMonth));
        }if(!Utils.isEmpty(mYear)){
        exp.setYear(Integer.parseInt(mYear));
        }
        card.setCardExpiry(exp);
        billing.setStreet(mStreet1);
        billing.setStreet2(mStreet2);
        billing.setCity(mCity);
        billing.setCountry(mCountry);
        billing.setState(mState);
        billing.setZip(mZip);
        card.setBillingAddress(billing);
        sObj.setCard(card);
		try {
			SingleUseToken sObjResponse = client.customerVaultService().createSingleUseToken(sObj);
			return sObjResponse;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OptimalException e) {
			e.printStackTrace();
		}
		
        return null;
    }

    /**
     * This method is used for create update card request
     *
     * @return void
     *
     *
     */
    private void createProfileRequest() {
        // Check Internet connection
        if (!isCheckInternet()) {
            Toast.makeText(getApplicationContext(),Constants.PLEASE_TURN_ON_YOUR_INTERNET, Toast.LENGTH_LONG).show();
            return;
        }
        String strUrl = getResources().getString(R.string.url_updatecard)+mProfileID+"/cards"+"/"+mCardID;
        new RequestDataSendViaAsynctask().execute(strUrl);
    }

    //Async task for update card
    private class Wrapper
    {
        public String responseString;
    }
    public class RequestDataSendViaAsynctask extends AsyncTask<String, String, Wrapper> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.startProgressDialog(UpdateCard.this, getString(R.string.loading_text));
        }

        @Override
        protected Wrapper doInBackground(String... params) {
            String url = params[0];
            String responseString = sendRequestToserver(url);
            Wrapper wrapper = new Wrapper();
            wrapper.responseString =  responseString;
            return wrapper;
        }

		protected void onPostExecute(Wrapper wrapper) {
			Utils.stopProgressDialog();
			if (wrapper != null && !Utils.isEmpty(wrapper.responseString)) {
				if(wrapper.responseString.contains(Constants.CONNECTION_REFUSED)){
					 Utils.showDialogAlert(Constants.PLEASE_TURN_ON_YOUR_INTERNET,mContext);
					 return;
				}
			}
			parseCreateProfile(mServer_conn, wrapper.responseString);
		}
    }


    /**
     * This method is called when send request to server
     * * @return String
     *
     */
    private String sendRequestToserver(String url) {
        String responseString = null;
        try {
            HttpsServerConnection httpsConnObj = new HttpsServerConnection();
            responseString = null;
            String base64EncodedCredentials = null;
            getValuesfromEditText();
            String username = getResources().getString(R.string.user_servertoserver);
            String password = getResources().getString(R.string.password_servertoserver);
            base64EncodedCredentials = Base64.encodeToString((username + ":" + password).getBytes("UTF-8"), Base64.NO_WRAP);
            responseString = httpsConnObj.requestUrl(base64EncodedCredentials,url ,createJsonObjectForCardUpdate(),Constants.PUT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch(Exception e){
        	e.printStackTrace();
        }

        return responseString;
    }


    /**
     * This method is used to create Json Object for card update
     *
     *
     * @return Json Object
     *
     */
    private JSONObject createJsonObjectForCardUpdate() {
        try {
            JSONObject json = new JSONObject();
            if(!Utils.isEmpty(mPaymentToken)){
                json.put("singleUseToken",mPaymentToken);
            }else{
                Log.e(TAG, "Payment token is null");
            }
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method is called when parse the update card response

     * @return void
     *
     */
    private void parseCreateProfile(HttpsServerConnection server_conn,String responseString) {
        final ArrayList<String> responseParams = new ArrayList<String>();
        responseParams.add("id");
        
        if (!Utils.isEmpty(responseString)&& responseString.contains("error")) {
            responseParams.add("error");
            @SuppressWarnings("unchecked")
            final Map<String, Object> map = server_conn.readAndParseJSON1(responseString, responseParams);
            try {
                final String errorDetails = (String) map.get("error");
                final ArrayList<String> responseParamsData = new ArrayList<String>();
                responseParamsData.add("code");
                responseParamsData.add("message");
                @SuppressWarnings("unchecked")
                final Map<String, Object> map1 = server_conn.readAndParseJSON1(errorDetails, responseParamsData);
                String strCode = (String) map1.get("code");
                String strMessage = (String) map1.get("message");
                Utils.showDialogAlert(strCode+": "+strMessage, mContext);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            responseParams.add("status");
            @SuppressWarnings("unchecked")
            Map<String, Object> map = server_conn.readAndParseJSON(responseString, responseParams);
            String strStatus = (String) map.get("status");
            if(!Utils.isEmpty(strStatus)&&strStatus.equals("ACTIVE")){
                showDialogAlert(Constants.UPDATED_CARD_IS_SUCCESSFFULY,mContext);
            }
        }

    }

    /**
     * This method is used to get values from edit text
     *
     * @return Void
     *
     */

    private void getValuesfromEditText() {
        try {
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
			
			if(mCardNo != null && mCardNo.contains("XXXX-XXXX-XXXX-1111")){
			   mCardNo = getResources().getString(R.string.card_number);
	   }else if (!Utils.isEmpty(mCardNo) && mCardNo.length() != 16) {
			   	 Log.e(TAG, Constants.PLEASE_ENTER_VALID_CARD_NUMER);	
	   }
		} catch (Exception e) {
			e.printStackTrace();
		}
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
        Intent intentCheckOut = new Intent(UpdateCard.this, Menu.class);
        startActivity(intentCheckOut);
        finish();
    }
    /**
     * This method is called show alert dialog
     * @return void
     *
     */
    private void showDialogAlert(String alertMessage, Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setMessage(alertMessage);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(UpdateCard.this,Menu.class);
                        startActivity(intent);
                        finish();
                    }
                });
        alertDialog.show();
    }
    @Override
	protected void onDestroy() {
		super.onDestroy();
		if(client != null){
			client = null;
		}
		
	}
}

