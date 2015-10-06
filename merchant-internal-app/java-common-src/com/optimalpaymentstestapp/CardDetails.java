package com.optimalpaymentstestapp;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
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
import com.optimalpayments.common.OptimalException;
import com.optimalpayments.customervault.BillingAddress;
import com.optimalpayments.customervault.Card;
import com.optimalpayments.customervault.SingleUseToken;
import com.optimalpaymentstestapp.utils.Constants;
import com.optimalpaymentstestapp.utils.Utils;
/**
 * @author Manisha.Rani
 * 
 */
public class CardDetails extends Activity {
    protected static final String TAG = "CardDetails";
    // Button
    private Button mOKButton;
    private Button mBackButton;
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
    private String mPaymentToken;
    protected static final int PAYMENT_TOKEN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carddetails);
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
        mBackButton = (Button) findViewById(R.id.btn_back);
        mOKButton = (Button) findViewById(R.id.btn_Confirm);
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

       // getValuesFromPrf();

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
                    final Intent intent = new Intent(CardDetails.this,PayNow.class);
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
    };


    /**
     * This method is used to excute ayscn task
     *
     * @return Void
     *
     */
    private void buttonConfirmClick(){
        getValuesfromEditText();
        //Check Internet connection
        if (!isCheckInternet()) {
            Toast.makeText(getApplicationContext(),	Constants.PLEASE_TURN_ON_YOUR_INTERNET,Toast.LENGTH_LONG).show();
            return;
        }
        //Excute AyscnTask for SingleUseToken
        new SingleUseTokenTask().execute();
    }

    class SingleUseTokenTask extends AsyncTask<String, Void, SingleUseToken> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.startProgressDialog(CardDetails.this,getString(R.string.loading_text));
        }
        @Override
        protected void onPostExecute(SingleUseToken singleUseTokenObject) {
            super.onPostExecute(singleUseTokenObject);
            try {
				Utils.stopProgressDialog();
				mPaymentToken = singleUseTokenObject.getPaymentToken();
				Error error = singleUseTokenObject.getError();

				String connectivityError = singleUseTokenObject.getConnectivityError();
				if (!Utils.isEmpty(connectivityError)) {
					Utils.showDialogAlert(Constants.PLEASE_TURN_ON_YOUR_INTERNET,mContext);
				}
				if(error != null ){
				    String strMessage = error.getMessage();
				    String strCode = error.getCode();
				    Utils.showDialogAlert(strCode + ": "+ strMessage, mContext);
				}
				else if (!Utils.isEmpty(mPaymentToken)) {
				    sendCallback(Constants.GET_PAYMENT_TOKEN, mPaymentToken,PAYMENT_TOKEN);
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

        OptimalApiClient client = new OptimalApiClient(getResources().getString(R.string.user_sbox), getResources().getString(R.string.password_sbox), Environment.TEST,getResources().getString(R.string.merchantaccountno));
        getValuesfromEditText();
        SingleUseToken sObj = new SingleUseToken();
        Card card = new Card();
        CardExpiry exp = new CardExpiry();
        BillingAddress billing = new BillingAddress();
        card.setHolderName(mCardHolderName);
		card.setCardNum(mCardNo);
		if (!Utils.isEmpty(mMonth)) {
			exp.setMonth(Integer.parseInt(mMonth));
		}
		if (!Utils.isEmpty(mYear)) {
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
			SingleUseToken sObjResponse;
			sObjResponse = client.customerVaultService().createSingleUseToken(sObj);
			return sObjResponse;
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OptimalException e) {
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		

		return null;
	}

    /**
     * This method is called send callback
     * @return Void
     *
     */
    private void sendCallback(String key,String value,int status) {
        Intent intent = new Intent();
        intent.putExtra(key, value);
        intent.putExtra(Constants.RANDOM_MERCHANT_REFRENCE_NUMBER_SINGLEUSETOKEN, Utils.twelveDigitRandomAlphanumeric());
        setResult(status, intent);
        finish();
    }

    /**
     * This method is used to get values from edit text
     *
     * @return Void
     *
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
        Intent intentCheckOut = new Intent(CardDetails.this,PayNow.class);
        startActivity(intentCheckOut);
        finish();
    }


}
